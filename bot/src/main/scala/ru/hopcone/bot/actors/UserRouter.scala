package ru.hopcone.bot.actors

import akka.actor.{Actor, ActorRef}
import info.mukel.telegrambot4s.models.User
import ru.hopcone.bot.models.DatabaseManager

trait UserRouter extends Router {
  self: Actor =>

  def db: DatabaseManager

  def createChild(tUser: User): ActorRef =
    context.actorOf(UserActor.props(tUser, db), s"user_${tUser.id}")
}
