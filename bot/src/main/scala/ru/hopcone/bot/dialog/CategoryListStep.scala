package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.dao.{CategoriesDAO, ProductsDAO}
import ru.hopcone.bot.data.models.DB.Tables.ShopItems
import ru.hopcone.bot.data.models.{Database, ProductCategory, ShopItem}

case class CategoryListStep(categories: Seq[ProductCategory])(implicit database: Database) extends DialogStep {
  override def title: String = "Что вас интересует?"

  override def buttons: Seq[String] = categories.map(_.name)

  override def transitions: PartialFunction[String, DialogStep] = {
    case categoryName if buttons.contains(categoryName) =>
      categories.find(c => c.name == categoryName) match {
        case None =>
          logger.debug(s"No category found for input '$categoryName'")
          this
        case Some(cat) =>
          val subcats = CategoriesDAO.childCategories(cat)
          if (subcats.nonEmpty) {
            CategoryListStep(subcats)
          } else {
            val categoryItems = ProductsDAO.productsInCategory(cat)
            logger.debug(s"Found $cat.\nContains items: [${categoryItems.mkString(",")}]")
            CategoryProductListStep(cat, categoryItems)
          }
      }
  }
}
