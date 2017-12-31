package ru.hopcone.bot.actors

import akka.actor.{Actor, ActorRef}
import ru.hopcone.bot.data.models.Database

trait UserRouter extends Router {
  self: Actor =>

  def db: Database

  def createChild(id: Int): ActorRef =
    context.actorOf(UserActor.props(id, db), s"user_$id")
}
