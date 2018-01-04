package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.dialog.{DialogStep, StepWithBack}
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class SelectAddressStep(prevStep: DialogStep)
                            (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack with CartChildStep {

  private lazy val addresses = ctx.user.addresses
  private lazy val addressesValues = addresses.map(_.address)

  override def stepText: String = "Выберите адрес доставки"

  override def buttons: Seq[String] = addressesValues //++ Seq(AddAddressButton)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    //case AddAddressButton => AddAddressStep(this)
    case addressText if addressesValues.contains(addressText) =>
      val address = addresses.find(_.address == addressText).get
      ctx.setOrderDeliveryAddress(address)
      SelectDeliveryTimeStep(this)
  }
}
