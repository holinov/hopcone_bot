package ru.hopcone.bot.dialog

trait StepWithBack extends DialogStepBase {
  def prevStep: DialogStep

  val BackButton = "Назад"

  protected def onTransition: PartialFunction[String, DialogStep]

  override def transitions: PartialFunction[String, DialogStep] = {
    case BackButton => prevStep
    case transition => onTransition(transition)
  }
}



