package ru.hopcone.bot.dialog.cart


import ru.hopcone.bot.dao.{CategoriesDAO, OrderItemDAO, ProductsDAO}
import ru.hopcone.bot.dialog.{CategoryListStep, DialogMapBuilder, DialogStep, StepWithBack}
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class ShowCartStep(prevStep: DialogStep)
                       (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack with CartChildStep {

  import AddToCartStep._

  override def stepText: String = {
    logger.debug("Rendering shopping cart")
    val order = ensureOrder
    val items = OrderItemDAO.items(order)
    val sb = StringBuilder.newBuilder.append("Вы заказали:\n")
    val totalPrice = items.zipWithIndex.map {
      case (cartItem, idx) =>
        val itemInfo = ProductsDAO.load(cartItem.itemId)
        val units = CategoriesDAO.units(itemInfo.categoryId.get)
        val price = itemInfo.price * cartItem.amount
        sb.append(idx + 1).append(s"] '${ProductsDAO.productName(cartItem.itemId)}' X ${cartItem.amount} $units = ${price.formatted("%.2f")} руб.\n")
        price
    }.sum
    sb.append(s"Сумма: ${totalPrice.formatted("%.2f")} руб.\n")
    sb.append(s"Номер заказа: ${order.orderId}\n")
    sb.toString()
  }

  private lazy val Root: DialogStep = DialogMapBuilder().rootStep
  private lazy val MainMenu = Root.next(MenuButton).get

  override def buttons: Seq[String] = Seq(ContinueShopping, CheckoutButton, CancelOrderButton)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case CheckoutButton => SelectAddressStep(prevStep)
    case ContinueShopping => CategoryListStep(CategoriesDAO.rootCategories(), () => MainMenu)
    case CancelOrderButton =>
      ctx.clearOrder()
      prevStep
  }
}
