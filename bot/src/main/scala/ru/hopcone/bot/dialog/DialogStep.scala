package ru.hopcone.bot.dialog

import com.typesafe.scalalogging.LazyLogging

trait DialogStep extends LazyLogging {
  def title: String

  def buttons: Seq[String]

  def transitions: PartialFunction[String, DialogStep]

  def onTransitionTo(transitionCommand: String): PartialFunction[String, Unit] = Map.empty

  def next(input: String): Option[DialogStep] =
    if (transitions.isDefinedAt(input)) Some(transitions(input))
    else None
}
