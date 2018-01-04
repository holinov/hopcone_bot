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
    items.zipWithIndex.map {
      case (cartItem, idx) =>
        sb.append(idx + 1).append(s"] ${ProductsDAO.productName(cartItem.itemId)} ${cartItem.amount} л.\n")
    }
    sb.toString()
  }

  private lazy val Root: DialogStep = DialogMapBuilder().rootStep
  private lazy val MainMenu = Root.next(MenuButton).get

  override def buttons: Seq[String] = Seq(ContinueShopping, CheckoutButton, CancelOrderButton)

  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case CheckoutButton => SelectAddressStep(prevStep)
    case ContinueShopping => CategoryListStep(CategoriesDAO.rootCategories(), MainMenu)
    case CancelOrderButton =>
      ctx.clearOrder()
      prevStep
  }
}
