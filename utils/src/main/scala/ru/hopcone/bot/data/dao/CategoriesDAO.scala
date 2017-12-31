package ru.hopcone.bot.data.dao

import ru.hopcone.bot.data.models
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

object CategoriesDAO extends AbstractDAO[ShopCategoryRow] {
  def rootCategories()(implicit db: models.Database) =
    run(ShopCategory.filter(_.parentId.isEmpty).result)

  def childCategories(cat: ShopCategoryRow)(implicit db: models.Database) =
    run(ShopCategory.filter(_.parentId === cat.id).result)
}