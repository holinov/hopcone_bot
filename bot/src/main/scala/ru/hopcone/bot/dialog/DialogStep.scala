package ru.hopcone.bot.dialog

import com.typesafe.scalalogging.LazyLogging

trait DialogStep extends LazyLogging {

  def stepText: String

  def buttons: Seq[String]

  def transitions: PartialFunction[String, DialogStep]

  def commonButtons: Seq[String] = Seq.empty

  def getButtons: Seq[String] = buttons ++ commonButtons

  def next(input: String): Option[DialogStep] = {
    if (transitions.isDefinedAt(input)) {
      val res = Some(transitions(input))
      logger.debug(s"Searching transition for $input. Found $res")
      res
    }
    else None
  }
}
