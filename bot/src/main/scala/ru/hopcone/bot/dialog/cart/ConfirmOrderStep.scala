package ru.hopcone.bot.dialog.cart

import org.joda.time.DateTime
import ru.hopcone.bot.dao.{DeliveryAddressDAO, OrderItemDAO, ProductsDAO, UserInfoDAO}
import ru.hopcone.bot.dialog._
import ru.hopcone.bot.models.Tables.OrderDataRow
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class ConfirmOrderStep(prevStep: DialogStep)
                           (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack {

  import AddToCartStep._

  private lazy val order = ctx.order.get
  private lazy val root = new DialogMapBuilder().rootStep

  override def stepText: String = renderOrder("Вы заказали:", order).toString()

  private def renderOrder(title: String, order: OrderDataRow) = {
    val items = OrderItemDAO.items(order)
    val sb = StringBuilder.newBuilder.append(title).append("\n")
    items.zipWithIndex.map {
      case (cartItem, idx) =>
        sb.append(idx + 1).append(s"] ${ProductsDAO.productName(cartItem.itemId)} ${cartItem.amount} л.\n")
    }
    sb.append(s"Точка выдачи:\n${renderAddress(order.deliveryAddress.get)}\n")
    sb.append(s"Время готовности:\n${new DateTime(order.deliveredAt.get).toString("YYYY.MM.dd HH:mm")}")
  }

  override def buttons: Seq[String] = Seq(CancelOrderButton, ConfirmButton)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case CancelOrderButton =>
      logger.info(s"Canceled order ${ctx.order.get}")
      ctx.clearOrder()
      new DialogStepWithAnswer(root, "Заказ отменен")

    case ConfirmButton =>
      logger.info(s"Confirmed order ${ctx.order.get}")
      val confirmed = ctx.confirmOrder()
      val orderBuilder = renderOrder("Получен заказ:\n", confirmed)
      val user = UserInfoDAO.loadByTgId(order.userId)
      orderBuilder.append(s"\n\n[Для пользователя ${user.name}](tg://user?id=${user.telegramUserId})")
      ctx.notifyAdmins(orderBuilder.toString())
      new DialogStepWithAnswer(root, "Заказ подтвержден")
  }


  private def renderAddress(addressId: Int): String = DeliveryAddressDAO.load(addressId).address
}
