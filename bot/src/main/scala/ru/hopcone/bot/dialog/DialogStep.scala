package ru.hopcone.bot.dialog

import com.typesafe.scalalogging.LazyLogging
import org.json4s.jackson.Serialization._
import ru.hopcone.bot.DefaultImplicits

trait DialogStep extends LazyLogging with DefaultImplicits {

  def stepText: String

  def buttons: Seq[String]

  def transitions: PartialFunction[String, DialogStep]

  def commonButtons: Seq[String] = Seq.empty

  def getButtons: Seq[String] = buttons ++ commonButtons

  def next(input: String): Option[DialogStep] = {
    if (transitions.isDefinedAt(input)) {
      val res = Option(transitions(input))
      logger.debug(s"Searching transition for $input. Found $res")
      res
    }
    else None
  }

  def pp(v: AnyRef): String = writePretty(v)
}
