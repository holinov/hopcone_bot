package ru.hopcone.bot.actors

import akka.actor.{ActorRef, Props}
import info.mukel.telegrambot4s.models.User
import ru.hopcone.bot.BotCommands
import ru.hopcone.bot.data.models

class BotActor(override val db: models.DatabaseManager) extends BasicActor with UserRouter {

  import BotCommands._
  import collection.mutable

  private val users = new mutable.HashMap[Int,User]

  override def receive: Receive = {
    case r@UserMessage(msg) =>
      logger.info(s"Dispatching $msg")
      val user: User = msg.from.get
      users += user.id -> user
      route(user.id,r)
  }

  override def createChild(id: Int): ActorRef = super.createChild(users(id))
}

object BotActor {
  def props(db: models.DatabaseManager): Props = Props(new BotActor(db))
}
