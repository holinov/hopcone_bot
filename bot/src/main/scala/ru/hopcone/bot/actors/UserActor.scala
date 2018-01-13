package ru.hopcone.bot.actors

import akka.actor.Props
import akka.pattern.pipe
import info.mukel.telegrambot4s.models.{User => TUser}
import ru.hopcone.bot.BotCommands.{AdminMessage, BotAdminMessageResponse, BotMessageResponse, BotMessageResponseError, BotResponse, UserMessage}
import ru.hopcone.bot.dao.{CategoriesDAO, ProductsDAO, UserInfoDAO}
import ru.hopcone.bot.dialog.{DialogMap, DialogMapBuilder, DialogProcessor}
import ru.hopcone.bot.models.Tables.UserInfoRow
import ru.hopcone.bot.models._
import ru.hopcone.bot.state.UserSession
import ru.hopcone.bot.{AdminApi, AsyncExecutionPoint, Csv}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.control.NonFatal



class UserActor(user: TUser, implicit val db: DatabaseManager, implicit val notificator: AdminApi)
  extends BasicActor with AsyncExecutionPoint with AdminApiCommands {
  private implicit val dialogContext: DialogStepContext = DialogStepContext(UserSession(user))
  private implicit var dialogMap: DialogMap = new DialogMapBuilder().build
  override protected implicit val ex: ExecutionContext = context.dispatcher

  private val dialogProcessor = new DialogProcessor(dialogMap)
  private var currentDialogStep = dialogProcessor.step

  private val userName: String = user.username getOrElse "No Nickname"
  UserInfoDAO.touchUser(UserInfoRow(user.id, userName, user.id, userName))


  override def receive: Receive = {
    case requestMessage: UserMessage =>
      val resp = processTextCommand(requestMessage) orElse downloadFile(requestMessage)
      resp.foreach { r =>
        logger.info(s"Responding with ${pp(r)}")
        sender() ! r
      }

    case adminRequest: AdminMessage =>
      val isAdmin = dialogContext.isAdmin
      val isAdminChat = dialogContext.isAdminChat(adminRequest.chatId)
      logger.debug(s"[$isAdmin:$isAdminChat] Performing admin request:\n${pp(adminRequest)}")
      if (isAdmin || isAdminChat) {
        logger.info(s"Performing admin request:\n${pp(adminRequest)} respond to ${sender()}")
        val result = processAdminCommand(adminRequest)
        result pipeTo sender()
      }

    case x =>
      logger.error(s"WTF? $x")
  }

  //  private def processAdminCommand(adminRequest: AdminMessage): Future[BotAdminMessageResponse] = {
  //    Future {
  //      adminRequest.text match {
  //        case mmm =>
  //          logger.info(s"ADMIN_COMMAND:\n${pp(mmm)}")
  //          123
  //      }
  //    } map { _ => BotAdminMessageResponse("123", Seq.empty, adminRequest) }
  //  }

  private def processTextCommand(requestMessage: UserMessage): Option[BotResponse[_]] = {
    val txt = requestMessage.message.text
    val commandResp = txt map { input =>
      try {
        val transitionResult = dialogProcessor.processInput(input)
        val nextStep = transitionResult.nextStep
        val title = nextStep.stepText
        val buttons = nextStep.getButtons
        val response = BotMessageResponse(title, buttons, requestMessage)
        currentDialogStep = nextStep
        response
      } catch {
        case e: MatchError =>
          logger.error(s"Error processing $input", e)
          BotMessageResponseError(e, requestMessage)
      }
    }
    commandResp
  }

  private def downloadFile(requestMessage: UserMessage): Option[BotResponse[_]] = {
    val fileOption = requestMessage.message.document

    if (!notificator.isAdmin(user.id))
      Some(BotMessageResponseError(new BotSecurityException, requestMessage))
    else
      fileOption flatMap { file =>
        for {
          mime <- file.mimeType if "text/csv" == mime
          fileName <- file.fileName
          fileId = file.fileId
        } yield (fileId, fileName)
      } map {
        case (fileId, fileName) =>
          notificator.receiveFile(fileId, fileName) map { fn =>
            Csv.header(fn) match {
              case Csv.CatsCsvHeader =>
                logger.info("Updating categories")
                val categoryRows = Csv.loadCats(fn)
                CategoriesDAO.forceInsertOrUpdate(categoryRows)
                dialogMap = new DialogMapBuilder().build
                logger.info("Updating categories done")
                s"Список категорий обновлен\nНовые категории:\n${categoryRows.map(r => s"${r.id}:${r.parentId getOrElse ""} -> ${r.name}").mkString("\n")}"
              case Csv.ItemsCsvHeader =>
                logger.info("Updating items")
                val itemRows = Csv.loadItems(fn)
                ProductsDAO.forceInsertOrUpdate(itemRows)
                logger.info("Updating items done")
                s"Список товаров обрновлен\nНовые товары:\n${itemRows.map(r => s"${r.id}:${r.categoryId getOrElse "????"} -> ${r.name} : ${r.price} руб").mkString("\n")}"
            }
          } recover {
            case NonFatal(e) =>
              logger.error("Error updating DB", e)
              e.getMessage
          } map { t =>
            notificator.notify(t)
            BotMessageResponse(t, Seq.empty, requestMessage)
          }
      } map { f => Await.result(f, 10.seconds) }
  }
}

object UserActor {
  def props(user: TUser, db: DatabaseManager, notificator: AdminApi): Props =
    Props(new UserActor(user, db, notificator))
}