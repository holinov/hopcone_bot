package ru.hopcone.bot.render

import info.mukel.telegrambot4s.methods.ParseMode.ParseMode
import info.mukel.telegrambot4s.models.{InlineKeyboardMarkup, ReplyKeyboardMarkup, ReplyMarkup}

trait BotResponseRenderer[T] {
  def text                  : String
  def replyToMsgId          : Option[Int] = None
  def parseMode             : Option[ParseMode] = None
  def disableWebPagePreview : Option[Boolean] = None
  def disableNotification   : Option[Boolean] = None
  def keyboardMarkup        : Option[ReplyKeyboardMarkup] = None
  def inlineKeyboardMarkup  : Option[InlineKeyboardMarkup] = None
}
