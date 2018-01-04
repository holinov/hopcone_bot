package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.dao.{DeliveryAddressDAO, OrderItemDAO, ProductsDAO}
import ru.hopcone.bot.dialog._
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class ConfirmOrderStep(prevStep: DialogStep)
                           (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack {


  import AddToCartStep._

  private lazy val order = ctx.order.get
  private lazy val root = new DialogMapBuilder().rootStep

  override def stepText: String = {
    val items = OrderItemDAO.items(order)
    val sb = StringBuilder.newBuilder.append("Вы заказали:\n")
    items.zipWithIndex.map {
      case (cartItem, idx) =>
        sb.append(idx + 1).append(s"] ${ProductsDAO.productName(cartItem.itemId)} ${cartItem.amount} л.\n")
    }
    sb.append(s"Доставка по адресу:\n${renderAddress(order.deliveryAddress.get)}")
    sb.toString()
  }

  override def buttons: Seq[String] = Seq(CancelOrderButton, ConfirmButton)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case CancelOrderButton =>
      logger.info(s"Canceled order ${ctx.order.get}")
      ctx.clearOrder()
      new DialogStepWithAnswer(root, "Заказ отменен")

    case ConfirmButton =>
      logger.info(s"Confirmed order ${ctx.order.get}")
      ctx.confirmOrder()
      new DialogStepWithAnswer(root, "Заказ подтвержден")
  }

  private def renderAddress(addressId: Int): String = DeliveryAddressDAO.load(addressId).address
}
