package ru.hopcone.bot.state

import info.mukel.telegrambot4s.models.User
import ru.hopcone.bot.dao.{OrderDataDAO, UserInfoDAO}
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._

case class UserSession(user: User) {
  def totalOrders(implicit db: DatabaseManager): Int = OrderDataDAO.countUserOrder(user)

  def orders(implicit db: DatabaseManager): Seq[OrderDataRow] = OrderDataDAO.userOrders(user.id)

  def addAddress(addressText: String)
                (implicit database: DatabaseManager): Unit =
    UserInfoDAO.addAddress(user.id, addressText)

  def addresses(implicit database: DatabaseManager): Seq[DeliveryAddressRow] =
    UserInfoDAO.commonAddresses() ++ UserInfoDAO.userAddresses(user.id)

}
