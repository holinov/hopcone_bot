package ru.hopcone.bot.dialog

import ru.hopcone.bot.dao.{CategoriesDAO, ProductsDAO}
import ru.hopcone.bot.models.Tables._
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class CategoryListStep(categories: Seq[ShopCategoryRow], prevStepFactory: () => DialogStep)
                           (implicit database: DatabaseManager, override val ctx: DialogStepContext)
  extends StepWithBack {


  logger.debug(s"CategoryListStep from $categories")

  override def stepText: String = "Что вас интересует?"

  override def buttons: Seq[String] = categories.map(_.name)

  override def onTransition: PartialFunction[String, DialogStep] = {
    case categoryName if buttons.contains(categoryName) =>
      categories.find(c => c.name == categoryName) match {
        case None =>
          logger.debug(s"No category found for input '$categoryName'")
          this
        case Some(cat) =>
          logger.debug(s"Searching for ${cat.id} ${cat.name}. \nCATS $categories")
          val subcats = CategoriesDAO.childCategories(cat)
          logger.debug(s"Subcats: $subcats")
          if (subcats.nonEmpty) {
            logger.debug(s"Found subcategories $subcats")
            CategoryListStep(subcats, () => this)
          } else {
            val categoryItems = ProductsDAO.productsInCategory(cat)
            logger.debug(s"Found $cat.\nContains items: [${categoryItems.mkString(",")}]")
            CategoryProductListStep(cat, categoryItems, this)
          }
      }

    case unknownCat =>
      logger.warn(s"Trying to look ant unknown catagory '$unknownCat'")
      this
  }

  override def prevStep: DialogStep = prevStepFactory()
}
