package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.data.dao.OrderItemDAO
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.dialog.StepWithBack
import ru.hopcone.bot.models.Tables

case class AddItemToCartStep(item: Tables.ShopItemRow, prevStep: AddToCartStep)
                            (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack {
  private val orderAmounts = Seq(BigDecimal(0.5), BigDecimal(1), BigDecimal(2), BigDecimal(2.5))

  override def stepText: String = s"Заказать ${item.name}"

  override def buttons: Seq[String] = orderAmounts.map(_.toString)

  override def onTransition: PartialFunction[String, DialogStep] = {
    case amount if orderAmounts.contains(BigDecimal(amount)) =>
      val order = ensureOrder
      OrderItemDAO.changeAmount(order, item, BigDecimal(amount))
      ShowCartStep(prevStep)
  }
}
