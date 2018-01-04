package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.data.dao.OrderItemDAO
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.models.Tables

trait CartChildStep {
  def ensureOrder(implicit database: DatabaseManager, ctx: DialogStepContext): Tables.OrderDataRow = OrderItemDAO.ensureOrder
}
