package ru.hopcone.bot.support

import com.typesafe.config.{Config, ConfigFactory}
import info.mukel.telegrambot4s.models.User
import org.scalatest.{BeforeAndAfterAll, FunSpecLike, Matchers}
import ru.hopcone.bot.data.models.{DB, DatabaseManager, DialogStepContext}
import ru.hopcone.bot.data.state.UserSession

import scala.concurrent.Await
import scala.concurrent.duration._

trait BaseSpecLike extends FunSpecLike with Matchers with BeforeAndAfterAll {
  val dbName: String
  val config: Config = ConfigFactory.load()
  val user: User = User(-1999, isBot = false, "test", Some("user"), Some("test_user"), Some("RU"))

  implicit lazy val userSession: UserSession = UserSession(user)
  implicit lazy val database: DatabaseManager = DB.database(dbName)
  implicit lazy val ctx: DialogStepContext = DialogStepContext(userSession)


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
