package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.dao.OrderItemDAO
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext, Tables}

trait CartChildStep {
  def ensureOrder(implicit database: DatabaseManager, ctx: DialogStepContext): Tables.OrderDataRow = OrderItemDAO.ensureOrder
}
