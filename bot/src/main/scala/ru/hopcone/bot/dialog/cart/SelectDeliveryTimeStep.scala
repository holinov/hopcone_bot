package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.dialog.{DialogStep, StepWithBack}
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class SelectDeliveryTimeStep(prevStep: DialogStep)
                                 (implicit database: DatabaseManager, ctx: DialogStepContext)

  extends StepWithBack with CartChildStep {

  private val TimeVariants = Seq("10 минут", "30 минут", "1 час", "2 часа", "3 часа", "4 часа")

  override def stepText: String = "Выберите время когда вы заберете заказ или введите свое время"

  override def buttons: Seq[String] = TimeVariants.map(s => s"через $s")

  private val predefinedTimesRegexp = "(через )?(\\d+) (минут|час).*".r

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case predefinedTimesRegexp(_, digitStr, unit) =>
      val digit = digitStr.toInt
      val totalMinutes =
        if (unit == "час") digit * 60
        else digit
      ctx.setOrderDeliveryTime(totalMinutes)
      ConfirmOrderStep(this)
    case userInput =>
      logger.error(s"Wrong user input $userInput")
      this
  }
}
