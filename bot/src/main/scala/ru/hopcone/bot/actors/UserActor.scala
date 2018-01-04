package ru.hopcone.bot.actors

import akka.actor.Props
import info.mukel.telegrambot4s.models.{User => TUser}
import ru.hopcone.bot.BotCommands.{UserMessage, UserMessageResponse, UserMessageResponseError}
import ru.hopcone.bot.data.dao.UserInfoDAO
import ru.hopcone.bot.data.models._
import ru.hopcone.bot.data.state.UserSession
import ru.hopcone.bot.dialog.{DialogMapBuilder, DialogProcessor}
import ru.hopcone.bot.models.Tables.UserInfoRow

class UserActor(user: TUser, implicit val db: DatabaseManager) extends BasicActor {
  private implicit val dialogContext: DialogStepContext = DialogStepContext(UserSession(user))

  private val dialogMap = new DialogMapBuilder().build
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