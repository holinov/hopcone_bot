package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.models.DialogStepContext
import ru.hopcone.bot.dialog.cart.DialogStep

case class BasicDialogStep(stepText: String, buttons: Seq[String], transitions: Map[String, DialogStep])
                          (implicit ctx: DialogStepContext)
  extends DialogStep

//object BasicDialogStep {
//  def apply(title: String, transitions: Map[String, DialogStep] = Map.empty): BasicDialogStep =
//    new BasicDialogStep(title, transitions.keys.toSeq, transitions)
//}