package ru.hopcone.bot.data

import slick.jdbc.PostgresProfile

package object models {
  type Database = PostgresProfile.backend.Database

  //case class ProductCategory(id: Long, name: String, description: String = "", parentId: Option[Long] = None)
  //case class ShopItem(id: Long, categoryId: Long, name: String, description: String, price:BigDecimal)
}
