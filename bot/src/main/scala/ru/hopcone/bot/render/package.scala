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
        val buttons = r.buttons
          .map { case (cat) => KeyboardButton.text(cat) }
        ReplyKeyboardMarkup.singleColumn(buttons)
      }
    }
  }

  //  implicit def render(r: IndexResponse): BotResponseRenderer[IndexResponse] =
  //    new BotResponseRenderer[IndexResponse] {
  //      def text: String = "Что вас интересует?"
  //
//      override def keyboardMarkup: Option[ReplyKeyboardMarkup] = {
  //        val buttons = r.categories
  //          .zipWithIndex
  //          .map { case (cat, idx) => KeyboardButton.text(s"$idx> ${cat.name}") }
//        ReplyKeyboardMarkup.singleColumn(buttons)
//      }
//
  //      override def replyToMsgId: Option[Int] = r.request.message.messageId
//    }
}
