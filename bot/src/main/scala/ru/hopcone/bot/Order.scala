package ru.hopcone.bot

case class Order(userId: Int, orderId: Long, items: List[OrderItem] = List.empty) {
  def withItem(item: OrderItem): Order = copy(items = item :: items)
}
