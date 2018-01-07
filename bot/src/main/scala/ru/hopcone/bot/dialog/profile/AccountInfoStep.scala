package ru.hopcone.bot.dialog.profile

import ru.hopcone.bot.dialog.cart.{AddToCartStep, ShowCartStep}
import ru.hopcone.bot.dialog.{DialogStep, StepWithBack}
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class AccountInfoStep(prevStepFactory: () => DialogStep)
                          (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack {

  import AddToCartStep._

  override def stepText: String = {
    val respBuilder = StringBuilder.newBuilder
      .append(
        s"""|Информация о профиле
            | ${ctx.userId}:${ctx.userName getOrElse "No Nickname"}
            | Всего заказов: ${ctx.user.get.totalOrders}
            | В вашей корзине ${ctx.cartSize} суммарно на ${ctx.cartTotalPrice}
        """.stripMargin)
    respBuilder.toString()
  }

  override def buttons: Seq[String] = Seq(CartButton)


  override protected def onTransition: PartialFunction[String, DialogStep] = {
    case CartButton => ShowCartStep(this)
  }

  private def debugInfo(sb: StringBuilder) = {
    sb.append("-" * 10).append("\n")
    sb.append(s"CTX: $ctx")
    sb.append("-" * 10).append("\n")
  }

  override def prevStep: DialogStep = prevStepFactory()
}
