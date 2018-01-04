package ru.hopcone.bot.dao

import org.postgresql.util.PSQLException
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

object UserInfoDAO extends AbstractDAO[UserInfoRow] {
  def commonAddresses()(implicit db: DatabaseManager): Seq[DeliveryAddressRow] =
    run(DeliveryAddress.filter(_.userId.isEmpty).result)

  def addAddress(userId: Int, address: String)
                (implicit db: DatabaseManager): Int =
    run(DeliveryAddress += DeliveryAddressRow(0, userId, address))

  def touchUser(user: UserInfoRow)
               (implicit db: DatabaseManager): Int =
    try {
      //TODO: update record if nickname changed
      run(UserInfo += user)
    } catch {
      case _: PSQLException => 0
    }

  def load(id: Int)
          (implicit db: DatabaseManager): UserInfoRow =
    run(UserInfo.filter(_.id === id).result).head

  def loadByTgId(id: Int)
                (implicit db: DatabaseManager): UserInfoRow =
    run(UserInfo.filter(_.telegramUserId === id).result).head

  def userAddresses(userId: Int)
                   (implicit db: DatabaseManager): Seq[DeliveryAddressRow] =
    run(DeliveryAddress.filter(_.userId === userId).result)
}
