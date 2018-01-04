package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.dialog.{DialogStep, StepWithBack}
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class AddAddressStep(prevStep: DialogStep)
                         (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack with CartChildStep {

  override def stepText: String = "Введите адрес доставки"

  override def buttons: Seq[String] = Seq.empty

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case addressText =>
      ctx.user.addAddress(addressText)
      SelectAddressStep(this)
  }
}
