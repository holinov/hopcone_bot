package ru.hopcone.bot.dao

import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

object CategoriesDAO extends AbstractDAO[ShopCategoryRow] {
  def forceLoad(catsToLoad: List[ShopCategoryRow])(implicit db: DatabaseManager): Unit = {
    catsToLoad.foreach(c => run(ShopCategory.insertOrUpdate(c)))
  }

  def all()(implicit db: DatabaseManager): Seq[ShopCategoryRow] =
    run(ShopCategory.result)

  def rootCategories()(implicit db: DatabaseManager): Seq[ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId.isEmpty).result)

  def childCategories(cat: ShopCategoryRow)(implicit db: DatabaseManager): Seq[ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId === cat.id).result)
}

