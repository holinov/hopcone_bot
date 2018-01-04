package ru.hopcone.bot.dialog


class DialogStepWithAnswer(inner: DialogStep, answer: String) extends DialogStepDecorator(inner) {
  override def stepText: String =
    s"$answer\n${super.stepText}"
}
