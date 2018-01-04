package ru.hopcone.bot.actors

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit._
import info.mukel.telegrambot4s.models.Message
import org.scalactic.source.Position
import org.scalatest._
import ru.hopcone.bot.BotCommands.{UserMessage, UserMessageResponse, UserMessageResponseError}
import ru.hopcone.bot.support.DBBasedSpecLike

import scala.concurrent.duration._

class UserActorSpec extends TestKit(ActorSystem("MySpec"))
  with ImplicitSender
  with DBBasedSpecLike
  with Matchers
  with BeforeAndAfterAll {
  override def afterAll() {
    TestKit.shutdownActorSystem(system)
  }

  override val dbName: String = "hopcone_database_test"

  describe("UserActor") {
    describe("basic navigation") {

      it("should navigate to 'BEER 1'") {
        expectDialog(Seq(
          "Меню" -> "Что вас интересует?",
          "Пиво" -> "Что вас интересует?",
          "IPA" -> "Что из IPA вас интересует?",
          "BEER 1" -> "Добавить в BEER 1 корзину?"
        ))
      }

      it("should allow to append 'BEER 2' to cart") {
        expectDialog(
          "Меню" -> "Что вас интересует?",
          "Пиво" -> "Что вас интересует?",
          "IPA" -> "Что из IPA вас интересует?",
          "BEER 2" -> "Добавить в BEER 2 корзину?",
          "Добавить в корзину" -> "Заказать BEER 2",
          "2" -> "Вы заказали:\n1] BEER 2 2.00 л.\n",
          "Сделать заказ" -> "Выберите адрес доставки",
          "Добавить адресс" -> "Введите адрес доставки",
          "Адрес 1" -> "Выберите адрес доставки",
          "Адрес 1" -> "Вы заказали:\n1] BEER 2 2.00 л.\nДоставка по адресу:\nАдрес 1",
          "Подтвердить заказ" -> "Заказ подтвержден\nГлавное меню"
        )
      }
    }
  }

  case class DialogActor(actorRef: ActorRef)

  private def expectDialog(expectations: (String, String)*)(implicit pos: Position): Unit = expectDialog(Seq(expectations: _*))

  private def expectDialog(expectations: Seq[(String, String)], finished: Boolean = true)
                          (implicit pos: Position): Unit = {
    implicit lazy val userActor: DialogActor = DialogActor(system.actorOf(UserActor.props(user, database)))
    expectations.foreach {
      case (msg, resp) => expectResponse(msg, resp)
    }
    if (finished) {
      expectNoMessage(1.second)
    }
  }

  private def expectResponse(msg: String, resp: String)
                            (implicit userActor: DialogActor, pos: Position): Unit = {
    userActor.actorRef ! UserMessage(Message(1, Some(user), 0, null, text = Some(msg)))
    expectMsgPF(hint = resp) {
      case UserMessageResponse(respMsg, _, _) if respMsg == resp =>
      case UserMessageResponseError(error, _) => fail(s"For message $msg\nexpected: $resp", error)
    }
  }
}
