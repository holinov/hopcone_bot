package ru.hopcone.bot.data.models

import ru.hopcone
import ru.hopcone.bot.models
import ru.hopcone.bot.models.Tables
import ru.hopcone.bot.models.Tables.OrderDataRow

case class DialogStepContext(userId: Int, private var ord: Option[OrderDataRow] = None) {
  def order: Option[models.Tables.OrderDataRow] = ord

  def clearOrder: Unit = ord = None

  def updateOrder(order: hopcone.bot.models.Tables.OrderDataRow): Tables.OrderDataRow = {
    ord = Option(order)
    order
  }
}
