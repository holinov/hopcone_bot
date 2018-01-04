package ru.hopcone.bot.models

import ru.hopcone
import ru.hopcone.bot
import ru.hopcone.bot.dao.OrderDataDAO
import ru.hopcone.bot.models.Tables.OrderDataRow
import ru.hopcone.bot.state.UserSession
import ru.hopcone.bot.{AdminApi, models}

case class DialogStepContext(user: UserSession, private var ord: Option[OrderDataRow] = None)
                            (implicit database: DatabaseManager, notificator: AdminApi) {
  def isAdmin: Boolean = notificator.isAdmin(userId)

  def confirmOrder(): OrderDataRow = {
    val confirmed = OrderDataDAO.setStatus(ord.get, "confirmed")
    clearOrder()
    confirmed
  }

  def userId: Int = user.user.id

  def order: Option[models.Tables.OrderDataRow] = ord

  def clearOrder(): Unit = ord = None

  def updateOrder(order: hopcone.bot.models.Tables.OrderDataRow): Tables.OrderDataRow = {
    ord = Option(order)
    order
  }

  def setOrderDeliveryAddress(address: bot.models.Tables.DeliveryAddressRow): Unit = {
    ord = ord.map(o => o.copy(deliveryAddress = Some(address.id)))
  }

  def notifyAdmins(notification: String): Unit = notificator.notify(notification)
}
