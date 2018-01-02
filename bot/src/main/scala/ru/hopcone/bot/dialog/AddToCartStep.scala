package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.dao._
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.models.Tables._

// (implicit db: DatabaseManager) extends
// (implicit db: DatabaseManager, dctx: DialogContext)
case class AddToCartStep(item: ShopItemRow, prevStep: DialogStep)
                        (implicit db: DatabaseManager, ctx: DialogStepContext) extends StepWithBack {
  private val AddToCartButton = "Добавить в корзину"
  private val RemoveFromCartButton = "Убрать из корзины"
  private val InfoButton = "Описание"
  private val CartButton = "Показать корзину"
  private val CheckoutButton = "Сделать заказ"
  private val CancelOrderButton = "Отменить заказ"
  private val RemoveButton = "Удалить из корзины"

  private def ensureOrder: OrderDataRow = OrderItemDAO.ensureOrder

  override def stepText: String = s"Добавить в ${item.name} корзину?"

  override def buttons: Seq[String] = Seq(InfoButton, AddToCartButton, RemoveFromCartButton, CartButton, BackButton)

  override def onTransition: PartialFunction[String, DialogStep] = {
    case InfoButton => this //TODO: Answer with item description
    case RemoveFromCartButton => RemoveItemFromCartStep()
    case AddToCartButton => AddItemToCartStep()
    case CartButton => ShowCartStep()
  }

  case class ShowCartStep() extends StepWithBack {
    override def stepText: String = {
      logger.debug("Rendering shopping cart")
      val order = ensureOrder
      val sb = StringBuilder.newBuilder.append("Вы заказали:\n")
      val items = OrderItemDAO.items(order)
      items.zipWithIndex.map {
        case (cartItem, idx) =>
          sb.append(idx + 1).append(s"] ${ProductsDAO.productName(cartItem.itemId)} ${cartItem.amount} л.\n")
      }
      sb.toString()
    }

    override def buttons: Seq[String] = Seq(CheckoutButton, CancelOrderButton, BackButton)

    override def prevStep: DialogStep = AddToCartStep.this

    override protected def onTransition: PartialFunction[String, DialogStep] = {
      case CheckoutButton => ShowCartStep()
      case CancelOrderButton =>
        ctx.clearOrder
        prevStep
    }
  }

  case class RemoveItemFromCartStep() extends StepWithBack {

    override def stepText: String = s"Вы правда хотите удалить ${item.name}?"

    override def buttons: Seq[String] = Seq(RemoveButton, BackButton)

    override def prevStep: DialogStep = AddToCartStep.this

    override protected def onTransition: PartialFunction[String, DialogStep] = {
      case RemoveButton =>
        val order = ensureOrder
        OrderItemDAO.removeFromOrder(order, item.id)
        ShowCartStep()
    }
  }

  case class AddItemToCartStep() extends StepWithBack {
    private val orderAmounts = Seq(BigDecimal(0.5), BigDecimal(1), BigDecimal(2), BigDecimal(2.5))

    override def stepText: String = s"Заказать ${item.name}"

    override def buttons: Seq[String] = {
      orderAmounts.map(_.toString) ++ Seq(BackButton)
    }

    override def prevStep: DialogStep = AddToCartStep.this

    override def onTransition: PartialFunction[String, DialogStep] = {
      case amount if orderAmounts.contains(BigDecimal(amount)) =>
        val order = ensureOrder
        OrderItemDAO.changeAmount(order, item, BigDecimal(amount))
        ShowCartStep()
    }
  }

}

