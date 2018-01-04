package ru.hopcone.bot.dialog.cart

import ru.hopcone.bot.data.models.DialogStepContext

abstract class DialogStepBase(implicit val ctx: DialogStepContext) extends DialogStep with CartChildStep
