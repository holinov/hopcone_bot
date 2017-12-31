package ru.hopcone.bot.actors

import ru.hopcone.bot.{Order, OrderItem}

/**
  * Actor performing user order tracking
  */
class UserOrderActor(userId: Int, orderId: Long) extends BasicActor {

  import UserOrderActor._

  private val become = context.become(_: Receive, discardOld = true)

  override def receive: Receive = shopping(Order(userId, orderId))

  def price(itemId: Int): Float = 0.1f //TODO: Load data

  //////////////////////////////////
  // States
  //////////////////////////////////
  def shopping(order: Order): Receive = {
    case AddToCart(itemId, amount) =>
      val updated = order
        .withItem(OrderItem(itemId, amount, price(itemId)))
      become(shopping(updated))
    case AddToCart(itemId, amount) =>
      val updated = order
        .withItem(OrderItem(itemId, -amount, 0.1f))
      become(shopping(updated))

    case Checkout() => become(confirmation(order))
  }
  def confirmation(order: Order): Receive = ???
  def delivery(order: Order): Receive = ???
  def canceled(order: Order): Receive = ???
  def finished(order: Order): Receive = ???
}

object UserOrderActor {
  sealed trait UserOrderCommand

  case class AddToCart(id: Int, amount: Int) extends UserOrderCommand
  case class RemoveFromCart(id: Int, amount: Int) extends UserOrderCommand
  case class Checkout() extends UserOrderCommand
  case class Resume() extends UserOrderCommand
  case class Confirm() extends UserOrderCommand
  case class Deliver() extends UserOrderCommand
  case class Cancel() extends UserOrderCommand
}
