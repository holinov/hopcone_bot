package ru.hopcone.bot.data.dao

import ru.hopcone.bot.data.models.DatabaseManager
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

object OrderDataDAO extends AbstractDAO[OrderDataRow] {
  private val insertOrderStmt = OrderData returning OrderData.map(_.orderId) into ((item, orderId) => item.copy(orderId = orderId))

  def insert(orderData: OrderDataRow)(implicit db: DatabaseManager): OrderDataRow = {
    run(insertOrderStmt += orderData)
  }
}
