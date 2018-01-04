package ru.hopcone.bot.dialog

trait StepWithBack extends DialogStepBase {
  def prevStep: DialogStep

  private val BackButton = "Назад"

  override def commonButtons: Seq[String] = Seq(BackButton)

  protected def onTransition: PartialFunction[String, DialogStep]

  override def transitions: PartialFunction[String, DialogStep] = {
    case BackButton => prevStep
    case transition => onTransition(transition)
  }
}



