package ru.hopcone.bot

import java.io
import java.util.concurrent.Executors

import akka.dispatch.ExecutionContexts
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import info.mukel.telegrambot4s.methods.{GetFile, ParseMode, SendMessage}
import info.mukel.telegrambot4s.models.File

import scala.concurrent.{ExecutionContextExecutorService, Future}


class AdminApi(config: Config, bot: Bot) extends LazyLogging {

  import FileUtils._

  import scala.collection.JavaConverters._

  private implicit val ex: ExecutionContextExecutorService = ExecutionContexts.fromExecutorService(Executors.newCachedThreadPool())

  private val adminUsers = config.getLongList("hopcone.bot.admins").asScala.toSet
  private val adminChannel = config.getString("hopcone.bot.channel")
  private val downloadDir = config.getString("hopcone.bot.download_dir")
  ensureDir(downloadDir)

  def notifyUser(userId: Long, notification: String): Unit =
    bot.request(SendMessage(userId, notification, parseMode = Some(ParseMode.Markdown)))

  def notifyByLogin(login: String, notification: String): Unit =
    bot.request(SendMessage(login, notification, parseMode = Some(ParseMode.Markdown)))

  def notify(notification: String): Unit = {
    adminUsers.foreach(uid => notifyUser(uid, notification))
    notifyByLogin(adminChannel, notification)
  }

  def receiveFile(fileId: String, saveAs: String): Future[io.File] = {
    bot.request(GetFile(fileId)) map { file =>
      val url = fileUrl(file)
      logger.info(s"Downloading file $file from $url")
      val fileName = s"$downloadDir/$saveAs"
      downloadFileOld(url, fileName)
      logger.info(s"Downloading file $file complete to $fileName")
      new io.File(fileName)
    }
  }

  private def fileUrl(file: File) = s"https://api.telegram.org/file/bot${bot.token}/${file.filePath.get}"
}
