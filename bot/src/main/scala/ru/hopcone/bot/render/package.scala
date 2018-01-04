package ru.hopcone.bot

import com.typesafe.scalalogging.LazyLogging
import info.mukel.telegrambot4s.models.{KeyboardButton, ReplyKeyboardMarkup}
import ru.hopcone.bot.BotCommands.{IndexResponse, UserMessageResponse}

import scala.language.implicitConversions

package object render extends DefaultImplicits with LazyLogging {
  implicit def render(r: UserMessageResponse): BotResponseRenderer[UserMessageResponse] = {
    new BotResponseRenderer[UserMessageResponse] {
      //logger.debug(s"Responding with ${r}\n${r.text}\t${r.buttons}")
      override def text: String = r.text.get

      override def keyboardMarkup: Option[ReplyKeyboardMarkup] = {
        val buttons = r.buttons
          .map { case (cat) => KeyboardButton.text(cat) }
        ReplyKeyboardMarkup.singleColumn(buttons)
      }

      override def replyToMsgId: Option[Int] = r.request.message.messageId
    }
  }

  implicit def render(r: IndexResponse): BotResponseRenderer[IndexResponse] =
    new BotResponseRenderer[IndexResponse] {
      def text: String = "Что вас интересует?"

      override def keyboardMarkup: Option[ReplyKeyboardMarkup] = {
        val buttons = r.categories
          .zipWithIndex
          .map { case (cat, idx) => KeyboardButton.text(s"$idx> ${cat.name}") }
        ReplyKeyboardMarkup.singleColumn(buttons)
      }

      override def replyToMsgId: Option[Int] = r.request.message.messageId
    }


//  implicit def renderDialogStep(step: DialogStep): BotResponseRenderer[DialogStep] =
//    new BotResponseRenderer[DialogStep] {
//      override def keyboardMarkup: Option[ReplyKeyboardMarkup] = {
//        val buttons = step.buttons.map(b => KeyboardButton.text(b))
//        ReplyKeyboardMarkup.singleColumn(buttons)
//      }
//
////      override def text: String = {
////        def text = step.title
////
////      }
//    }
}
