package ru.hopcone.bot.support

import com.typesafe.config._
import info.mukel.telegrambot4s.models.User
import org.scalatest._
import ru.hopcone.bot.data.models._
import ru.hopcone.bot.data.state.UserSession

import scala.concurrent.Await
import scala.concurrent.duration._

class BaseSpec(dbName: String = "hopcone_database_test") extends FunSpec with Matchers with BeforeAndAfterAll {
  val config: Config = ConfigFactory.load()
  val user: User = User(-1999, isBot = false, "test", Some("user"), Some("test_user"), Some("RU"))

  implicit val userSession: UserSession = UserSession(user)
  implicit val database: DatabaseManager = DB.database(dbName)
  implicit val ctx: DialogStepContext = DialogStepContext(userSession)


  override protected def beforeAll(): Unit = {
    val created = DB.initialize(database)
    Await.ready(created, 10.seconds)
  }

  override protected def afterAll(): Unit = {
    database.close()
  }
}
