package ru.hopcone.bot.dao

import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

object CategoriesDAO extends AbstractDAO[ShopCategoryRow] {

  def units(categoryId: Int)(implicit db: DatabaseManager): String = load(categoryId).units

  def load(categoryId: Int)(implicit db: DatabaseManager): ShopCategoryRow = run(ShopCategory.filter(_.id === categoryId).result).head

  def forceInsertOrUpdate(catsToLoad: List[ShopCategoryRow])(implicit db: DatabaseManager): Unit = {
    catsToLoad.foreach(c => run(ShopCategory.insertOrUpdate(c)))
  }

  def all()(implicit db: DatabaseManager): Seq[ShopCategoryRow] =
    run(ShopCategory.result)

  def rootCategories()(implicit db: DatabaseManager): Seq[ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId.isEmpty).result)

  def childCategories(cat: ShopCategoryRow)(implicit db: DatabaseManager): Seq[ShopCategoryRow] =
    run(ShopCategory.filter(_.parentId === cat.id).result)
}

