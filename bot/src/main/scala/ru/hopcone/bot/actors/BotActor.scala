package ru.hopcone.bot.actors

import akka.actor.{ActorRef, Props}
import info.mukel.telegrambot4s.models.User
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.{AdminApi, BotCommands, models}

class BotActor(override val db: DatabaseManager, val notificator: AdminApi)
  extends BasicActor
    with UserRouter {

  import BotCommands._

  import collection.mutable

  private val AdminUser = User(-1985, isBot = true, "ADMIN", None, None, None)
  private val AdminChannelsActor = context.actorOf(UserActor.props(AdminUser, db, notificator))

  private val users = new mutable.HashMap[Int, User]

  override def receive: Receive = {
    case r: BotCommand =>
      logger.debug(s"Dispatching ${pp(r)}")

      r.message.from match {
        case Some(user) =>
          users += user.id -> user
          route(user.id, r)
        case None =>
          AdminChannelsActor ! r
      }

    //      val user: User = r.message.from.get
    //      users += user.id -> user
    //      route(user.id, r)
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
