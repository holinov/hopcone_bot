package ru.hopcone.bot.data.dao

import ru.hopcone.bot.data.models.DatabaseManager
import ru.hopcone.bot.models.Tables
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

object CategoriesDAO extends AbstractDAO[ShopCategoryRow] {
  def rootCategories()(implicit db: DatabaseManager): Seq[Tables.ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId.isEmpty).result)

  def childCategories(cat: ShopCategoryRow)(implicit db: DatabaseManager): Seq[Tables.ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId === cat.id).result)
}

