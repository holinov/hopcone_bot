package ru.hopcone.bot.actors

import akka.actor.Props
import info.mukel.telegrambot4s.models.Message
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
    case msg: UserMessage =>
      logger.debug(s"Processing user message ${msg}")
      val txt = msg.message.text
      val stepResult = txt.map(dialogProcessor.processInput)
      stepResult.foreach(r => currentDialogStep = r.nextStep)
      val title = currentDialogStep.title
      val buttons = currentDialogStep.buttons
      val response = UserMessageResponse(title,buttons, msg)
      logger.debug(s"Responding $response")
      sender() ! response
    case x =>
      logger.error(s"WTF? $x")
  }
}

object UserActor {
  def props(userId: Int, db: Database): Props = Props(new UserActor(userId, db))
}