package ru.hopcone.bot.dialog

import ru.hopcone.bot.dialog.cart.AddToCartStep
import ru.hopcone.bot.models.Tables._
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class CategoryProductListStep(category: ShopCategoryRow, products: Seq[ShopItemRow], prevStep: DialogStep)
                                  (implicit db: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack {
  override def stepText: String = s"Что из ${category.name} вас интересует?"

  override def buttons: Seq[String] = products.map(_.name.toString)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case productName =>
      products.find(_.name == productName) match {
        case Some(p) => AddToCartStep(p, this)
        case None => this
      }
  }
}
