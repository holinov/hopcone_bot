package ru.hopcone.bot.actors

import akka.actor.Props
import info.mukel.telegrambot4s.models.{User => TUser}
import ru.hopcone.bot.AdminApi
import ru.hopcone.bot.BotCommands.{UserMessage, UserMessageResponse, UserMessageResponseError}
import ru.hopcone.bot.dao.UserInfoDAO
import ru.hopcone.bot.dialog.{DialogMap, DialogMapBuilder, DialogProcessor}
import ru.hopcone.bot.models.Tables.UserInfoRow
import ru.hopcone.bot.models._
import ru.hopcone.bot.state.UserSession

class UserActor(user: TUser, implicit val db: DatabaseManager, implicit val notificator: AdminApi) extends BasicActor {
  private implicit val dialogContext: DialogStepContext = DialogStepContext(UserSession(user))
  private implicit val dialogMap: DialogMap = new DialogMapBuilder().build

  private val dialogProcessor = new DialogProcessor(dialogMap)
  private var currentDialogStep = dialogProcessor.step

  private val userName: String = user.username getOrElse "No Nickname"
  UserInfoDAO.touchUser(UserInfoRow(user.id, userName, user.id, userName))

  override def receive: Receive = {
    case requestMessage: UserMessage =>
      logger.debug(s"Processing user message ${pp(requestMessage)}")
      val txt = requestMessage.message.text
      val commandResp = txt map { input =>
        try {
          val transitionResult = dialogProcessor.processInput(input)
          val nextStep = transitionResult.nextStep
          val title = nextStep.stepText
          val buttons = nextStep.getButtons
          val response = UserMessageResponse(title, buttons, requestMessage)
          currentDialogStep = nextStep
          response
        } catch {
          case e: MatchError =>
            logger.error(s"Error processing $input", e)
            UserMessageResponseError(e, requestMessage)
        }
      }

      val fileOption = requestMessage.message.document
      val fileData = for {
        file <- fileOption
        mime <- file.mimeType if Seq("text/csv", "application/vnd.openxmlformats-officedocument.wordprocessingml.document") contains mime
        fileId = file.fileId
        fileName <- file.fileName
      } yield (file, mime, fileId, fileName)
      fileData.foreach {
        case (file, mime, fileId, fileName) =>
          logger.debug(s"GOT FILE $file, $mime, $fileId")
          notificator.receiveFile(fileId, fileName)
      }


      val resp = commandResp

      resp.foreach { r =>
        logger.debug(s"Responding ${pp(r)}")
        sender() ! r
      }


    case x =>
      logger.error(s"WTF? $x")
  }
}

object UserActor {
  def props(user: TUser, db: DatabaseManager, notificator: AdminApi): Props =
    Props(new UserActor(user, db, notificator))
}