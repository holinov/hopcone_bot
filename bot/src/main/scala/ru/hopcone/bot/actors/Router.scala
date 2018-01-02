package ru.hopcone.bot.actors

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable

trait Router {
  self: Actor =>

  def createChild(id: Int): ActorRef

  def route(id: Int, message: Any): Unit = {
    val actor = routedActors.getOrElseUpdate(id, createChild(id))
    actor forward message
  }

  private val routedActors: mutable.Map[Int, ActorRef] = mutable.Map()
}
