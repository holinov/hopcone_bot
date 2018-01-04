package ru.hopcone.bot.dialog

import ru.hopcone.bot.dialog.cart.CartChildStep
import ru.hopcone.bot.models.DialogStepContext

abstract class DialogStepBase(implicit val ctx: DialogStepContext) extends DialogStep with CartChildStep
