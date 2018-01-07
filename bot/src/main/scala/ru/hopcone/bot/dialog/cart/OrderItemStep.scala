package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.dao.{CategoriesDAO, OrderItemDAO}
import ru.hopcone.bot.dialog.{DialogStep, StepWithBack}
import ru.hopcone.bot.models.{DB, DatabaseManager, DialogStepContext, Tables}

case class OrderItemStep(item: Tables.ShopItemRow, prevStep: AddToCartStep)
                        (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack {

  private def ammountsByUnits(units: String) = {
    units match {
      case DB.Units.Liters => Seq(BigDecimal(0.5), BigDecimal(1), BigDecimal(1.5), BigDecimal(3), BigDecimal(4), BigDecimal(5))
      case DB.Units.Counts => Range(1, 10).map(d => BigDecimal(d))
      case unit =>
        logger.warn(s"Unknown type $unit")
        Range(1, 4).map(d => BigDecimal(d))
    }
  }

  private lazy val cat = CategoriesDAO.load(item.categoryId.get)
  private lazy val unitName = cat.units
  private lazy val orderAmounts = ammountsByUnits(unitName)

  override def stepText: String = s"Заказать ${item.name}"

  override def buttons: Seq[String] = orderAmounts.map(_.toString)

  override def onTransition: PartialFunction[String, DialogStep] = {
    case amount if orderAmounts.contains(BigDecimal(amount)) =>
      val order = ensureOrder
      OrderItemDAO.changeAmount(order, item, BigDecimal(amount))
      ShowCartStep(prevStep)
  }
}
