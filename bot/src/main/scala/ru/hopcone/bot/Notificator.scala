package ru.hopcone.bot

import com.typesafe.config.Config
import info.mukel.telegrambot4s.methods.SendMessage

class Notificator(config: Config, bot: Bot) {

  import scala.collection.JavaConverters._

  private val adminUsers = config.getLongList("hopcone.bot.admins").asScala.toSet
  private val adminChannel = config.getString("hopcone.bot.channel")

  def notifyUser(userId: Long, notification: String): Unit =
    bot.request(SendMessage(userId, notification))

  def notifyByLogin(login: String, notification: String): Unit =
    bot.request(SendMessage(login, notification))

  def notify(notification: String): Unit = {
    adminUsers.foreach(uid => notifyUser(uid, notification))
    notifyByLogin(adminChannel, notification)
  }
}
