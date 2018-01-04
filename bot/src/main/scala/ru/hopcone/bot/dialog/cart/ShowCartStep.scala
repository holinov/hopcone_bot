package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.data.dao.{OrderItemDAO, ProductsDAO}
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.dialog.StepWithBack

case class ShowCartStep(prevStep: DialogStep)
                       (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack with CartChildStep {

  import AddToCartStep._

  override def stepText: String = {
    logger.debug("Rendering shopping cart")
    val order = ensureOrder
    val sb = StringBuilder.newBuilder.append("Вы заказали:\n")
    val items = OrderItemDAO.items(order)
    items.zipWithIndex.map {
      case (cartItem, idx) =>
        sb.append(idx + 1).append(s"] ${ProductsDAO.productName(cartItem.itemId)} ${cartItem.amount} л.\n")
    }
    sb.toString()
  }

  override def buttons: Seq[String] = Seq(CheckoutButton, CancelOrderButton, BackButton)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case CheckoutButton => ShowCartStep(this)
    case CancelOrderButton =>
      ctx.clearOrder
      prevStep
  }
}
