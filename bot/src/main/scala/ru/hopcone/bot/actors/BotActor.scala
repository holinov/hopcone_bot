package ru.hopcone.bot.actors

import akka.actor.{ActorRef, Props}
import info.mukel.telegrambot4s.models.User
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.{AdminApi, BotCommands, models}

class BotActor(override val db: DatabaseManager, val notificator: AdminApi) extends BasicActor with UserRouter {

  import BotCommands._

  import collection.mutable

  private val users = new mutable.HashMap[Int, User]

  override def receive: Receive = {
    case r@UserMessage(msg) =>
      logger.info(s"Dispatching $msg")
      val user: User = msg.from.get
      users += user.id -> user
      route(user.id, r)
  }

  override def createChild(id: Int): ActorRef = {
    val user = users(id)
    logger.debug(s"Create Actor for user $user")
    super.createChild(user, notificator)
  }
}

object BotActor {
  def props(db: models.DatabaseManager, notificator: AdminApi): Props = Props(new BotActor(db, notificator))
}
