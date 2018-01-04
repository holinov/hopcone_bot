package ru.hopcone.bot.dialog

import ru.hopcone.bot.dialog.cart.DialogStep

case class TransitionResult(nextStep: DialogStep, response: String)
