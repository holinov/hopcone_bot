package ru.hopcone.bot.state

import info.mukel.telegrambot4s.models.User
import ru.hopcone.bot.dao.UserInfoDAO
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._

case class UserSession(user: User) {
  def addAddress(addressText: String)
                (implicit database: DatabaseManager): Unit =
    UserInfoDAO.addAddress(user.id, addressText)

  def addresses(implicit database: DatabaseManager): Seq[DeliveryAddressRow] =
  //UserInfoDAO.userAddresses(user.id)
    UserInfoDAO.commonAddresses()

}
