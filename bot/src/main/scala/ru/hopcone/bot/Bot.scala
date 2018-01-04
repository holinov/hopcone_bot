package ru.hopcone.bot

import akka.actor.ActorRef
import akka.pattern.ask
import com.typesafe.config.Config
import info.mukel.telegrambot4s.api._
import info.mukel.telegrambot4s.api.declarative.ToCommand._
import info.mukel.telegrambot4s.api.declarative._
import info.mukel.telegrambot4s.models._
import org.json4s.jackson.Serialization._
import ru.hopcone.bot.BotCommands._
import ru.hopcone.bot.actors.BotActor
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.render._

import scala.concurrent.Future
import scala.io.Source
import scala.language.implicitConversions

class Bot(config: Config, db: DatabaseManager) extends TelegramBot
  with Polling
  with Commands
  with Messages
  with Help
  with ChannelPosts
  with DefaultImplicits {

  lazy val token: String = scala.util.Properties
    .envOrNone("BOT_TOKEN")
    .getOrElse(Source.fromFile("bot.token").getLines().mkString)
    .trim

  private val sep = "-" * 10
  private var botActor: ActorRef = _

  def connect(): Unit = {
    logger.info("Starting bot")
    run()
    val notificator = new AdminApi(config, this)
    botActor = system.actorOf(BotActor.props(db, notificator), "hopcone_bot")
    notificator.notify("Bot started")
    logger.info("Bot started")
  }

  def stop(): Future[Unit] = shutdown()

  onMessage { implicit msg =>
    logger.debug(s"$sep\nReceived msg: ${writePretty(msg)}\n$sep")
    askBot(UserMessage(msg)) { resp: UserMessageResponse =>
      renderResponse(resp)
    }
  }

  onChannelPost { implicit msg =>
    logger.debug(s"$sep\nReceived channel msg: ${writePretty(msg)}\n$sep")
    if (msg.text.isDefined && msg.text.contains("@fruttech_bot tellid")) {
      reply(s"Channel chat id: ${msg.chat.id}", replyToMessageId = Some(msg.messageId))
    }
  }

  onCommandWithHelp('start, 'привет)("Начать общение") { implicit msg =>
    reply(s"Рад вас видеть! У нас самое вкусное пиво! Наберите слово 'Меню' для начала работы")
  }

  override def helpHeader(): String =
    "Привет! Я знаю вот такие команды:\nДля подбробностей наберите _/help_ <команда>\n"

  private def askBot[C <: BotCommand, R <: BotResponse[C]](c: C)(action: R => Unit): Unit = {
    val user = c.message.from.get
    logger.info(s"Processing command ${c.getClass.getCanonicalName} for user ${user.username} [${user.id}]")
    (botActor ? c)
      .map(r => r.asInstanceOf[R])
      .foreach(r => action(r))
  }

  private def renderResponse[T](r: BotResponseRenderer[T])(implicit message: Message): Future[Message] = {
    val markup = r.inlineKeyboardMarkup orElse r.keyboardMarkup
    reply(r.text,
      r.parseMode,
      r.disableWebPagePreview,
      r.disableNotification,
      r.replyToMsgId,
      markup
    )
  }
}
