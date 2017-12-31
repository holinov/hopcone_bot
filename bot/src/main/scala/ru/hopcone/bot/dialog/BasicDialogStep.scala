package ru.hopcone.bot.dialog

case class BasicDialogStep(title: String, buttons: Seq[String], transitions: Map[String, DialogStep])
  extends DialogStep

//object BasicDialogStep {
//  def apply(title: String, transitions: Map[String, DialogStep] = Map.empty): BasicDialogStep =
//    new BasicDialogStep(title, transitions.keys.toSeq, transitions)
//}