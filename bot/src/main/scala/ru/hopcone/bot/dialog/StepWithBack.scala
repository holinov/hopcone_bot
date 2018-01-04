package ru.hopcone.bot.dialog

import ru.hopcone.bot.dialog.cart.{DialogStep, DialogStepBase}

trait StepWithBack extends DialogStepBase {
  def prevStep: DialogStep

  val BackButton = "Назад"

  override def commonButtons: Seq[String] = Seq(BackButton)

  protected def onTransition: PartialFunction[String, DialogStep]

  override def transitions: PartialFunction[String, DialogStep] = {
    case BackButton => prevStep
    case transition => onTransition(transition)
  }
}



