package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.dialog.{DialogStep, MainButtons, StepWithBack}
import ru.hopcone.bot.models.Tables._
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class AddToCartStep(item: ShopItemRow, prevStep: DialogStep)
                        (implicit db: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack with CartChildStep {

  import AddToCartStep._

  override def stepText: String = s"Добавить в ${item.name} корзину?"

  override def buttons: Seq[String] = Seq(InfoButton, AddToCartButton, RemoveFromCartButton, CartButton)

  override def onTransition: PartialFunction[String, DialogStep] = {
    case InfoButton => this //TODO: Answer with item description
    case RemoveFromCartButton => RemoveItemFromCartStep(item, this)
    case AddToCartButton => OrderItemStep(item, this)
    case CartButton => ShowCartStep(this)
  }
}

object AddToCartStep extends MainButtons {
  val AddToCartButton = "Добавить в корзину"
  val RemoveFromCartButton = "Убрать из корзины"
  val InfoButton = "Описание"
  val CartButton = "Показать корзину"
  val ContinueShopping = "Продолжить покупки"
  val CheckoutButton = "Сделать заказ"
  val ConfirmButton = "Подтвердить заказ"
  val CancelOrderButton = "Отменить заказ"
  val RemoveButton = "Удалить из корзины"
  val AddAddressButton = "Добавить адресс"
}