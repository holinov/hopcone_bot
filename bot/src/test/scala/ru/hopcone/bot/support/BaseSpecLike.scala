package ru.hopcone.bot.support

import com.typesafe.config.{Config, ConfigFactory}
import info.mukel.telegrambot4s.models.User
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}
import ru.hopcone.bot.AdminApi
import ru.hopcone.bot.dialog.{DialogMap, DialogMapBuilder}
import ru.hopcone.bot.models.{DB, DatabaseManager, DialogStepContext}
import ru.hopcone.bot.state.UserSession

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseSpecLike extends FunSpecLike with Matchers with BeforeAndAfterAll {
  val dbName: String
  val config: Config = ConfigFactory.load()
  val user: User = User(-1999, isBot = false, "test", Some("user"), Some("test_user"), Some("RU"))

  implicit lazy val notificator: AdminApi = new AdminApi(config, null) {
    override def notifyChatId(userId: Long, notification: String): Unit = {}

    override def notify(notification: String): Unit = {}
  }
  implicit lazy val userSession: UserSession = UserSession(user)
  implicit lazy val ctx: DialogStepContext = DialogStepContext(userSession)
  implicit lazy val dialogMap: DialogMap = DialogMapBuilder()
  implicit lazy val database: DatabaseManager = DB.database(dbName)


  override protected def beforeAll(): Unit = {
    val dropped = DB.drop(database)
    Await.ready(dropped, 10.seconds)
    val created = DB.initialize(database)
    Await.ready(created, 10.seconds)
  }

  override protected def afterAll(): Unit = {
    database.close()
  }
}
