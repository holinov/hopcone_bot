package ru.hopcone.bot.dao

import java.sql.Timestamp

import ru.hopcone.bot
import ru.hopcone.bot.models
import ru.hopcone.bot.models.Tables._
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext, Tables}
import slick.jdbc.PostgresProfile.api._


object OrderItemDAO extends AbstractDAO[OrderItemRow] {

  val OrderStateNew = "new"
  val OrderStateCheckout = "checkout"
  val OrderStateDelivered = "delivered"

  private def buildNewOrder(userId: Int) =
    OrderDataRow(0, userId, Some(0), new Timestamp(System.currentTimeMillis()), None, OrderStateNew, None)

  def ensureOrder(implicit database: DatabaseManager, ctx: DialogStepContext): Tables.OrderDataRow = ctx.order match {
    case Some(order) => order
    case None =>
      val order = OrderDataDAO.findLastNewOrder(ctx.userId) match {
        case Some(o) => o
        case None =>
          val orderRow = buildNewOrder(ctx.userId)
          OrderDataDAO.insert(orderRow)
      }
      ctx.updateOrder(order)
  }

  def removeFromOrder(order: Tables.OrderDataRow, id: Int)
                     (implicit database: DatabaseManager): Int = {
    run(OrderItem.filter(ord => ord.orderId === order.orderId && ord.itemId === id).delete)
  }

  def changeAmount(order: Tables.OrderDataRow, item: Tables.ShopItemRow, amount: BigDecimal)
                  (implicit database: DatabaseManager): Int = {
    val found = run(OrderItem.filter(oi => oi.orderId === order.orderId && oi.itemId === item.id).result).headOption
    val action = found match {
      case None =>
        logger.debug(s"Inserting new record for order_id: ${order.orderId}. Item: $item x $amount")
        OrderItem += OrderItemRow(order.orderId, item.id, amount, amount * item.price, 0)
      case Some(record) =>
        val newAmount = record.amount + amount
        val orderItemQuery = OrderItem.filter(_.id === record.id)
        if (newAmount <= 0) {
          logger.debug(s"Deleting existing record for order_id: ${order.orderId}. Item: $item")
          orderItemQuery.delete
        }
        else {
          logger.debug(s"Updating existing record for order_id: ${order.orderId}. Item: $item by amount: $amount to: $newAmount")
          orderItemQuery.update(record.copy(amount = newAmount))
        }
    }
    run(action)
  }

  def save(order: OrderDataRow, item: ShopItemRow, amount: BigDecimal)
          (implicit database: DatabaseManager): Int = {
    run(OrderItem += OrderItemRow(order.orderId, item.id, amount, amount * item.price, 0))
  }

  def itemCount(order: OrderDataRow)
               (implicit db: DatabaseManager): Int =
    run(selectOrderItemsQuery(order).size.result)

  def items(order: OrderDataRow)
           (implicit db: DatabaseManager): Seq[models.Tables.OrderItemRow] =
    run(selectOrderItemsQuery(order).result)

  private def selectOrderItemsQuery(order: _root_.ru.hopcone.bot.models.Tables.OrderDataRow) = {
    OrderItem.filter(_.orderId === order.orderId)
  }

  def item(order: OrderDataRow, itemId: Int)
          (implicit db: DatabaseManager): Option[bot.models.Tables.OrderItemRow] =
    run(OrderItem.filter(oi => oi.orderId === order.orderId && oi.itemId === itemId).result).headOption
}
