package ru.hopcone.bot.data.dao

import ru.hopcone.bot.data.models
import ru.hopcone.bot.data.models.DB.Tables.ProductCategories
import ru.hopcone.bot.data.models.ProductCategory
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._

object CategoriesDAO extends TableQuery(new ProductCategories(_)) with AbstractDAO[ProductCategory] {

  def childCategories(cat: ProductCategory)(implicit db:models.Database) = run(filter(_.parentId === cat.id).result)

  def rootCategories()(implicit db:models.Database): Seq[ProductCategory] = run(filter(_.parentId.isEmpty).result)
}
