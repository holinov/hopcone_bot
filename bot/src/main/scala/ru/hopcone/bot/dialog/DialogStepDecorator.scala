package ru.hopcone.bot.dialog


abstract class DialogStepDecorator(inner: DialogStep) extends DialogStep {
  def stepText: String = inner.stepText

  def buttons: Seq[String] = inner.buttons

  def transitions: PartialFunction[String, DialogStep] = inner.transitions

  override def commonButtons: Seq[String] = inner.commonButtons

  override def getButtons: Seq[String] = inner.getButtons

  override def next(input: String): Option[DialogStep] = inner.next(input)
}

