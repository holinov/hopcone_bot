package ru.hopcone.bot.dao

import java.sql.Timestamp

import org.joda.time.LocalTime
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

object OrderDataDAO extends AbstractDAO[OrderDataRow] {
  def setOrderDeliveryTime(order: OrderDataRow, afterMinutes: Int)(implicit db: DatabaseManager): OrderDataRow = {
    val deliveryDate = new LocalTime().plusMinutes(afterMinutes).toDateTimeToday.toDateTime.getMillis
    val updated = order.copy(deliveredAt = Some(new Timestamp(deliveryDate)))
    update(updated)
    updated
  }

  private val InsertOrderStmt = OrderData returning OrderData.map(_.orderId) into ((item, orderId) => item.copy(orderId = orderId))

  def findLastNewOrder(userId: Int)(implicit db: DatabaseManager): Option[OrderDataRow] =
    run(OrderData.filter(o => o.userId === userId && o.status === "new").result).headOption

  def update(order: OrderDataRow)(implicit db: DatabaseManager): OrderDataRow = {
    run(OrderData.filter(_.orderId === order.orderId).update(order))
    order
  }

  def setAddress(order: OrderDataRow, deliveryAddress: DeliveryAddressRow)
                (implicit db: DatabaseManager): OrderDataRow =
    update(order.copy(deliveryAddress = Some(deliveryAddress.id)))

  def setStatus(order: OrderDataRow, status: String)
               (implicit db: DatabaseManager): OrderDataRow =
    update(order.copy(status = status))

  def insert(orderData: OrderDataRow)
            (implicit db: DatabaseManager): OrderDataRow = {
    run(InsertOrderStmt += orderData)
  }
}
