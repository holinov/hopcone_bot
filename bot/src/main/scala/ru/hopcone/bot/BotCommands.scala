package ru.hopcone.bot

import info.mukel.telegrambot4s.models.Message
import ru.hopcone.bot.models.Tables._

import scala.language.implicitConversions

object BotCommands extends DefaultImplicits {

  sealed trait BotCommand {
    def userId: Int = message.from.get.id

    def message: Message
  }

  sealed trait BotResponse[Request <: BotCommand] {
    def request: Request

    def replyTo: Int = request.message.from.get.id

    def text: Option[String] = Some("LA LA LA") //None
  }

  case class Index(categoryId: Option[Long], message: Message)
    extends BotCommand

  case class IndexResponse(request: Index, categories: List[ShopCategoryRow])
    extends BotResponse[Index]


  case class UserMessage(message: Message) extends BotCommand

  case class UserMessageResponse(title: String, buttons: Seq[String], request: UserMessage) //TODO ?????
    extends BotResponse[UserMessage]
  {
    override def text: Option[String] = title
  }

  case class UserMessageResponseError(error: Throwable, request: UserMessage)
    extends BotResponse[UserMessage] {
    override def text: Option[String] = error.getMessage
  }

}


