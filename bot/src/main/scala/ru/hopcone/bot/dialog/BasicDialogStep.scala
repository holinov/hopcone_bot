package ru.hopcone.bot.dialog

import ru.hopcone.bot.models.DialogStepContext

case class BasicDialogStep(stepText: String, buttons: Seq[String], transitions: Map[String, DialogStep])
                          (implicit ctx: DialogStepContext)
  extends DialogStep

//object BasicDialogStep {
//  def apply(title: String, transitions: Map[String, DialogStep] = Map.empty): BasicDialogStep =
//    new BasicDialogStep(title, transitions.keys.toSeq, transitions)
//}