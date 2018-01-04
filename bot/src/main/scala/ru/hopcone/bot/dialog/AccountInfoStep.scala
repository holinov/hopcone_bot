package ru.hopcone.bot.dialog

import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

case class AccountInfoStep(prevStep: DialogStep)
                          (implicit database: DatabaseManager, ctx: DialogStepContext)
  extends StepWithBack {

  override def stepText: String = {
    val respBuilder = StringBuilder.newBuilder
      .append(
        s"""|Информация о профиле
          |${ctx.user}
        """.stripMargin)

    respBuilder.toString()
  }

  override def buttons: Seq[String] = Seq.empty

  override protected def onTransition: PartialFunction[String, DialogStep] = Map.empty

  private def debugInfo(sb:StringBuilder) = {
    sb.append("-"*10).append("\n")
    sb.append(s"CTX: $ctx")
    sb.append("-"*10).append("\n")
  }
}
