package ru.hopcone.bot.actors

import akka.actor.{Actor, ActorRef}
import com.typesafe.scalalogging.LazyLogging
import ru.hopcone.bot.DefaultImplicits

import scala.collection.mutable

abstract class BasicActor extends Actor with LazyLogging with DefaultImplicits
