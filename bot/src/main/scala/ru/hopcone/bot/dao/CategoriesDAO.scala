package ru.hopcone.bot.dao

import ru.hopcone.bot.models.Tables._
import ru.hopcone.bot.models.{DatabaseManager, Tables}
import slick.jdbc.PostgresProfile.api._

object CategoriesDAO extends AbstractDAO[ShopCategoryRow] {
  def rootCategories()(implicit db: DatabaseManager): Seq[Tables.ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId.isEmpty).result)

  def childCategories(cat: ShopCategoryRow)(implicit db: DatabaseManager): Seq[Tables.ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId === cat.id).result)
}

