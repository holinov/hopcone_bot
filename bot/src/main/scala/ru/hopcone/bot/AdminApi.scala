package ru.hopcone.bot

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import info.mukel.telegrambot4s.methods.{GetFile, ParseMode, SendMessage}
import info.mukel.telegrambot4s.models.{ChatId, File}

import scala.concurrent.Future

class AdminApi(config: Config, bot: Bot) extends LazyLogging with AsyncExecutionPoint {
  import FileUtils._

  import scala.collection.JavaConverters._

  private val adminUsers = config.getLongList("hopcone.bot.admins").asScala.toSet
  private val adminChannel = config.getString("hopcone.bot.channel")
  private val downloadDir = config.getString("hopcone.bot.download_dir")
  ensureDir(downloadDir)

  lazy val chatId: ChatId =
    if (adminChannel.startsWith("@")) ChatId(adminChannel)
    else ChatId(adminChannel.toLong)

  def isAdminChat(chatId: Long): Boolean = chatId == adminChannel.toLong

  def notifyChatId(userId: Long, notification: String): Unit =
    bot.request(SendMessage(userId, s">>\n$notification\n>>", parseMode = Some(ParseMode.Markdown)))

  def notifyByLogin(login: String, notification: String): Unit =
    bot.request(SendMessage(login, notification, parseMode = Some(ParseMode.Markdown)))

  def notify(notification: String): Unit = {
    adminUsers.foreach(uid => notifyChatId(uid, notification))
    bot.request(SendMessage(chatId, notification, parseMode = Some(ParseMode.Markdown)))
  }

  def receiveFile(fileId: String, saveAs: String): Future[String] = {
    bot.request(GetFile(fileId)) map { file =>
      val url = fileUrl(file)
      logger.info(s"Downloading file $file from $url")
      val fileName = s"$downloadDir/$saveAs"
      downloadFileOld(url, fileName)
      logger.info(s"Downloading file $file complete to $fileName")
      fileName
    }
  }

  def isAdmin(userId: Int): Boolean = adminUsers.contains(userId.toLong)

  private def fileUrl(file: File) = s"https://api.telegram.org/file/bot${bot.token}/${file.filePath.get}"
}
