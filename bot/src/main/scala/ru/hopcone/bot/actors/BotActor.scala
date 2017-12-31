package ru.hopcone.bot.actors

import akka.actor.Props
import akka.pattern.pipe
import ru.hopcone.bot.BotCommands
import ru.hopcone.bot.data.dao._
import ru.hopcone.bot.data.models
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class BotActor(override val db: models.Database) extends BasicActor with UserRouter {

  import BotCommands._

  override def receive: Receive = {
    case r@UserMessage(msg) =>
      logger.info(s"Dispatching $msg")
      route(msg.from.get.id,r)
  }
}

object BotActor {
  def props(db: models.Database): Props = Props(new BotActor(db))
}
