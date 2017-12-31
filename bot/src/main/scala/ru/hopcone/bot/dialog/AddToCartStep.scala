package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.models.ShopItem

case class AddToCartStep(item: ShopItem, stepBack: DialogStep) extends DialogStep {
  private val AddToCartButton = "Добавить в корзину"
  private val RemoveFromCartButton = "Убрать из корзины"
  private val InfoButton = "Описание"
  private val CancelButton = "Отмена"

  override def title: String = s"Добавить в ${item.name} корзину?"

  override def buttons: Seq[String] = Seq(InfoButton, AddToCartButton, RemoveFromCartButton, CancelButton)

  override def transitions: PartialFunction[String, DialogStep] = {
    case InfoButton => this
      //TODO: Answer with item description
    case RemoveFromCartButton => this

    case AddToCartButton => this

    case CancelButton => stepBack
  }
}
