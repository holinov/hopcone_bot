package ru.hopcone.bot

import com.typesafe.scalalogging.LazyLogging
import info.mukel.telegrambot4s.models.{KeyboardButton, ReplyKeyboardMarkup}
import ru.hopcone.bot.BotCommands.UserMessageResponse

import scala.language.implicitConversions

package object render extends DefaultImplicits with LazyLogging {
  implicit def render(r: UserMessageResponse): BotResponseRenderer[UserMessageResponse] = {
    new BotResponseRenderer[UserMessageResponse] {
      override def text: String = r.text.get

      override def keyboardMarkup: Option[ReplyKeyboardMarkup] = {
        if (r.buttons.lengthCompare(2) <= 0) singleCol(r)
        else if (r.buttons.lengthCompare(6) <= 0) someCol(r, 2)
        else someCol(r, 3)
      }
    }
  }

  private def singleCol(r: UserMessageResponse) = {
    val buttons = r.buttons
      .map { case (cat) => KeyboardButton.text(cat) }
    ReplyKeyboardMarkup.singleColumn(buttons, resizeKeyboard = Some(true))
  }

  private def someCol(r: UserMessageResponse, groupSize: Int) = {
    val buttons = r.buttons
      .map { case (cat) => KeyboardButton.text(cat) }
      .grouped(groupSize)
      .toSeq

    ReplyKeyboardMarkup(buttons, Some(true))
  }

}
