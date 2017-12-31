package ru.hopcone.bot

import java.util.concurrent.TimeUnit

import akka.util.Timeout
import info.mukel.telegrambot4s.models.{KeyboardButton, ReplyKeyboardMarkup}
import org.json4s.{DefaultFormats, Formats}
import ru.hopcone.bot.BotCommands.IndexResponse
import ru.hopcone.bot.render.BotResponseRenderer

import scala.language.implicitConversions

trait DefaultImplicits {
  implicit lazy val timeout: Timeout = Timeout(10000, TimeUnit.MILLISECONDS)
  implicit lazy val formats: Formats = DefaultFormats

  // Implicit conversions
  implicit def objectToSome[T](obj: T): Option[T] = Option(obj)
}

//trait RenderResponse extends DefaultImplicits{
//
//}