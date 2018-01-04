package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.data.dao.OrderItemDAO
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.dialog.StepWithBack
import ru.hopcone.bot.dialog.cart.AddToCartStep.RemoveButton
import ru.hopcone.bot.models.Tables

case class RemoveItemFromCartStep(item: Tables.ShopItemRow, prevStep: AddToCartStep)
                                 (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack with CartChildStep {

  override def stepText: String = s"Вы правда хотите удалить ${item.name}?"

  override def buttons: Seq[String] = Seq(RemoveButton, BackButton)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case RemoveButton =>
      val order = ensureOrder
      OrderItemDAO.removeFromOrder(order, item.id)
      ShowCartStep(prevStep)
  }
}
