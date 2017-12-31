package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.models.{ProductCategory, ShopItem}

case class CategoryProductListStep(category: ProductCategory, products: Seq[ShopItem])
  extends DialogStep {
  override def title: String = s"Что из ${category.name} вас интересует?"

  override def buttons: Seq[String] = products.map(_.name)

  override def transitions: PartialFunction[String, DialogStep] = {
    case productName =>
      products.find(_.name == productName) match {
        case Some(p) => AddToCartStep(p, this)
        case None => this
      }
  }
}
