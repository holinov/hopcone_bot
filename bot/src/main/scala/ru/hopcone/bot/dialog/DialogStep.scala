package ru.hopcone.bot.dialog

import com.typesafe.scalalogging.LazyLogging
import ru.hopcone.bot.data.models.DialogStepContext


trait DialogStep extends LazyLogging {

  def stepText: String

  def buttons: Seq[String]

  def transitions: PartialFunction[String, DialogStep]

  def next(input: String): Option[DialogStep] = {
    if (transitions.isDefinedAt(input)) {
      val res = Some(transitions(input))
      logger.debug(s"Searching transition for $input. Found $res")
      res
    }
    else None
  }
}

abstract class DialogStepBase(implicit val ctx: DialogStepContext) extends DialogStep
