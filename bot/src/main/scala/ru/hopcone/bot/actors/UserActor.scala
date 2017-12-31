package ru.hopcone.bot.actors

import akka.actor.Props
import ru.hopcone.bot.BotCommands.{UserMessage, UserMessageResponse}
import ru.hopcone.bot.data.models._
import ru.hopcone.bot.dialog.{DialogMapBuilder, DialogProcessor}

//case class OrderItem(itemId:Long,)
//case class OrderData()

class UserActor(userId: Int, implicit val db: Database) extends BasicActor {
  private val dialogMap = new DialogMapBuilder().build
  private val dialogProcessor = new DialogProcessor(dialogMap)
  private var currentDialogStep = dialogProcessor.step

  override def receive: Receive = {
    case requestMessage: UserMessage =>
      logger.debug(s"Processing user message ${pp(requestMessage)}")
      val txt = requestMessage.message.text
      txt.foreach { input =>
        val nextStep = dialogProcessor.processInput(input)
        val title = nextStep.nextStep.title
        val buttons = nextStep.nextStep.buttons
        val response = UserMessageResponse(title, buttons, requestMessage)
        currentDialogStep = nextStep.nextStep

        logger.debug(s"Responding ${pp(response)}")
        sender() ! response
      }

    case x =>
      logger.error(s"WTF? $x")
  }
}

object UserActor {
  def props(userId: Int, db: Database): Props = Props(new UserActor(userId, db))
}