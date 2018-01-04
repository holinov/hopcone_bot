package ru.hopcone.bot.data.dao

import org.postgresql.util.PSQLException
import ru.hopcone.bot.data.models.DatabaseManager
import ru.hopcone.bot.models.Tables._

object UserInfoDAO extends AbstractDAO[UserInfo] {

  import slick.jdbc.PostgresProfile.api._

  def touchUser(user: UserInfoRow)(implicit db: DatabaseManager): Int =

    try {
      run(UserInfo += user)
    } catch {
      case _: PSQLException => 0
    }


  def load(id: Int)(implicit db: DatabaseManager): UserInfoRow = run(UserInfo.filter(_.id === id).result).head

  def userAddresses(userId: Int)(implicit db: DatabaseManager): Seq[DeliveryAddressRow] = run(DeliveryAddress.filter(_.userId === userId).result)
}
