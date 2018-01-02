package ru.hopcone.bot.dialog

import ru.hopcone.bot.models.Tables
import ru.hopcone.bot.models.Tables.ShopItemRow

case class Order(private var its: Map[ShopItemRow, Float]) {
  def items: Map[Tables.ShopItemRow, Float] = its

  def withItem(item: ShopItemRow, amount: Float): Order = {
    val newContents = findItemAndUpdate(item, amount)
    println(s"FOUND: $newContents")
    its = its + newContents
    println(s"UPDATED: $its")
    this
  }

  private def findItemAndUpdate(item: ShopItemRow, amount: Float) = {
    val newData = items.get(item) match {
      case None => item -> amount
      case Some(oldAmount) =>
        val pair = item -> (oldAmount + amount)
        its += pair
        pair
    }
    newData
  }
}
