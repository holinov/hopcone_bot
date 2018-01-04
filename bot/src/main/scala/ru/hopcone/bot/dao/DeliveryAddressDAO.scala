package ru.hopcone.bot.dao

import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._

object DeliveryAddressDAO extends AbstractDAO[DeliveryAddressRow] {

  import slick.jdbc.PostgresProfile.api._

  def load(id: Int)
          (implicit db: DatabaseManager): DeliveryAddressRow =
    run(DeliveryAddress.filter(_.id === id).result).head
}
