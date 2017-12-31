package ru.hopcone.bot.dialog

import ru.hopcone.bot.models.Tables.ShopItemRow


case class Order(private var items: Map[ShopItemRow, Float]) {
  def withItem(item: ShopItemRow, amount: Float): Order = {
    val newContents = (item, amount)
    items = items + newContents
    this
    //items.foreach(findItemAndUpdate)
  }

  private def findItemAndUpdate(item: ShopItemRow, amount: Float) = {
    val newData = items.get(item) match {
      case None => item -> amount
      case Some(oldAmount) => item -> (oldAmount + amount)
    }
    newData
  }
}

case class AddToCartStep(item: ShopItemRow, stepBack: DialogStep) extends DialogStep {
  private val AddToCartButton = "Добавить в корзину"
  private val RemoveFromCartButton = "Убрать из корзины"
  private val InfoButton = "Описание"
  private val CancelButton = "Отмена"

  private var order = Order(Map.empty) //TODO: real order management

  override def title: String = s"Добавить в ${item.name} корзину?"

  override def buttons: Seq[String] = Seq(InfoButton, AddToCartButton, RemoveFromCartButton, CancelButton)

  override def transitions: PartialFunction[String, DialogStep] = {
    case InfoButton => this
    //TODO: Answer with item description
    case RemoveFromCartButton => this

    case AddToCartButton => this

    case CancelButton => stepBack
  }

  case class AddItemToCartStep(item: ShopItemRow) extends DialogStep {
    private val orderAmounts = Seq(BigDecimal(0.5), BigDecimal(1), BigDecimal(2), BigDecimal(2.5))

    override def title: String = s"Заказать ${item.name}"

    override def buttons: Seq[String] = {
      orderAmounts.map(_.toString) ++ Seq(CancelButton)
    }

    override def transitions: PartialFunction[String, DialogStep] = {
      case CancelButton => AddToCartStep.this
      case amount if orderAmounts.contains(BigDecimal(amount)) =>
        order = order.withItem(item, amount.toFloat)
        this
      //order = order.withItem(item, amount)
    }
  }

}

