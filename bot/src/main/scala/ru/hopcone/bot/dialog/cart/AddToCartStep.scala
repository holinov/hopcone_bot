package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.data.dao._
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.dialog.StepWithBack
import ru.hopcone.bot.dialog.cart.AddToCartStep.RemoveButton
import ru.hopcone.bot.models.Tables._

case class AddToCartStep(item: ShopItemRow, prevStep: DialogStep)
                        (implicit db: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack with CartChildStep {

  import AddToCartStep._

  override def stepText: String = s"Добавить в ${item.name} корзину?"

  override def buttons: Seq[String] = Seq(InfoButton, AddToCartButton, RemoveFromCartButton, CartButton, BackButton)

  override def onTransition: PartialFunction[String, DialogStep] = {
    case InfoButton => this //TODO: Answer with item description
    case RemoveFromCartButton => RemoveItemFromCartStep(item, this)
    case AddToCartButton => AddItemToCartStep(item, this)
    case CartButton => ShowCartStep(this)
  }
}





object AddToCartStep {
  val AddToCartButton = "Добавить в корзину"
  val RemoveFromCartButton = "Убрать из корзины"
  val InfoButton = "Описание"
  val CartButton = "Показать корзину"
  val CheckoutButton = "Сделать заказ"
  val CancelOrderButton = "Отменить заказ"
  val RemoveButton = "Удалить из корзины"
}