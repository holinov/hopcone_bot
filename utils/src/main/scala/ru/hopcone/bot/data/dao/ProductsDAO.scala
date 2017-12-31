package ru.hopcone.bot.data.dao

import ru.hopcone.bot.data.models.DB.Tables.ShopItems
import ru.hopcone.bot.data.models.{Database, ProductCategory, ShopItem}
import slick.dbio.Effect
import slick.jdbc.PostgresProfile.api._
import slick.sql.FixedSqlStreamingAction

import scala.concurrent.Await


object ProductsDAO extends TableQuery(new ShopItems(_)) with AbstractDAO[ProductCategory] {

  private def productsInCategoryQuery(cat: ProductCategory) = {
    filter(_.categoryId === cat.id)
  }

  def productsInCategory(cat: ProductCategory)(implicit db: Database): Seq[ShopItem] = {
    run(productsInCategoryQuery(cat).result)
  }
}
