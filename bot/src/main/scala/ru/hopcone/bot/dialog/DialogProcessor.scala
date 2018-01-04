package ru.hopcone.bot.dialog

class DialogProcessor(dialogMap: DialogMap) {
  private var currentStep: DialogStep = dialogMap.rootStep


  def step:DialogStep = currentStep

  /**
    * Reset navigation to starting step
    */
  def reset(): Unit = currentStep = dialogMap.rootStep

  def processInput(input: String): TransitionResult = {
    val result = currentStep.next(input) match {
      case Some(nextStep) =>
        TransitionResult(nextStep, "Что из этого вам интересно?")
      case None =>
        TransitionResult(currentStep, "Не понял что от меня хотят.")
    }
    currentStep = result.nextStep
    result
  }
}
