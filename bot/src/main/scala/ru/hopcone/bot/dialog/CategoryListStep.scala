package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.dao.{CategoriesDAO, ProductsDAO}
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.models.Tables._

case class CategoryListStep(categories: Seq[ShopCategoryRow], prevStep: DialogStep)
                           (implicit database: DatabaseManager, override val ctx: DialogStepContext)
  extends StepWithBack {
  logger.debug(s"CategoryListStep fro $categories")

  override def stepText: String = "Что вас интересует?"

  override def buttons: Seq[String] = categories.map(_.name) ++ Seq(BackButton)

  override def onTransition: PartialFunction[String, DialogStep] = {
    case categoryName if buttons.contains(categoryName) =>
      categories.find(c => c.name == categoryName) match {
        case None =>
          logger.debug(s"No category found for input '$categoryName'")
          this
        case Some(cat) =>
          logger.debug(s"Searching for ${cat.id} ${cat.name}. \nCATS $categories")
          val subcats = CategoriesDAO.childCategories(cat)
          if (subcats.nonEmpty) {
            logger.debug(s"Found subcategories $subcats")
            CategoryListStep(subcats, this)
          } else {
            val categoryItems = ProductsDAO.productsInCategory(cat)
            logger.debug(s"Found $cat.\nContains items: [${categoryItems.mkString(",")}]")
            CategoryProductListStep(cat, categoryItems, this)
          }
      }
  }
}
