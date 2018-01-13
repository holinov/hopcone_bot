package ru.hopcone.bot.actors

import ru.hopcone.bot.BotCommands.{AdminMessage, BotAdminMessageResponse}

import scala.concurrent.Future

trait AdminApiCommands {
  this: UserActor =>

  def processAdminCommand(adminRequest: AdminMessage): Future[BotAdminMessageResponse] = {
    Future {
      logger.debug(s"Admin request ${adminRequest.text} starting")
      val res = process(adminRequest)
      logger.debug(s"Admin request ${adminRequest.text} executed")
      res
    }
  }

  private val process: PartialFunction[AdminMessage, BotAdminMessageResponse] = {
    case msg@AdminMessageByName("список") =>
      BotAdminMessageResponse(s"received ${msg.text}", Seq.empty, msg)
  }

  object AdminMessageByName {
    def unapply(arg: AdminMessage): Option[(String)] = Some(arg.text)
  }
}
