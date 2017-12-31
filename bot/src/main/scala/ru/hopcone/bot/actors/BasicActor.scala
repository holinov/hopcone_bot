package ru.hopcone.bot.actors

import akka.actor.Actor
import com.typesafe.scalalogging.LazyLogging
import org.json4s.jackson.Serialization._
import ru.hopcone.bot.DefaultImplicits

abstract class BasicActor extends Actor with LazyLogging with DefaultImplicits {
  def pp(v: AnyRef) = writePretty(v)
}
