package ru.hopcone.bot.actors

import akka.actor.Props
import info.mukel.telegrambot4s.models.{User => TUser}
import ru.hopcone.bot.BotCommands.{UserMessage, UserMessageResponse, UserMessageResponseError}
import ru.hopcone.bot.dao.UserInfoDAO
import ru.hopcone.bot.dialog.{DialogMap, DialogMapBuilder, DialogProcessor}
import ru.hopcone.bot.models.Tables.UserInfoRow
import ru.hopcone.bot.models._
import ru.hopcone.bot.state.UserSession

class UserActor(user: TUser, implicit val db: DatabaseManager) extends BasicActor {
  private implicit val dialogContext: DialogStepContext = DialogStepContext(UserSession(user))
  private implicit val dialogMap: DialogMap = new DialogMapBuilder().build

  private val dialogProcessor = new DialogProcessor(dialogMap)
  private var currentDialogStep = dialogProcessor.step

  UserInfoDAO.touchUser(UserInfoRow(user.id, user.username.get, user.id))

  override def receive: Receive = {
    case requestMessage: UserMessage =>
      logger.debug(s"Processing user message ${pp(requestMessage)}")
      val txt = requestMessage.message.text
      txt.foreach { input =>
        try {
          val nextStep = dialogProcessor.processInput(input).nextStep
          val title = nextStep.stepText
          val buttons = nextStep.getButtons
          val response = UserMessageResponse(title, buttons, requestMessage)
          currentDialogStep = nextStep

          logger.debug(s"Responding ${pp(response)}")
          sender() ! response
        } catch {
          case e: MatchError =>
            sender() ! UserMessageResponseError(e, requestMessage)
            logger.error(s"Error processing $input", e)
        }
      }

    case x =>
      logger.error(s"WTF? $x")
  }
}

object UserActor {
  def props(user: TUser, db: DatabaseManager): Props = Props(new UserActor(user, db))
}