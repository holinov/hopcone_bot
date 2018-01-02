package ru.hopcone.bot.support

import com.typesafe.config._
import org.scalatest._
import ru.hopcone.bot.data.dao._
import ru.hopcone.bot.data.models._

import scala.concurrent.Await
import scala.concurrent.duration._

class BaseSpec(dbName: String = "hopcone_database_test") extends FunSpec with Matchers with BeforeAndAfterAll {
  val config: Config = ConfigFactory.load()
  implicit val database: DatabaseManager = DB.database(dbName)
  implicit val ctx: DialogStepContext = DialogStepContext(OrderItemDAO.NewEntryId)

  private val created = DB.initialize(database)
  Await.ready(created, 10.seconds)

  override protected def afterAll(): Unit = {
    database.close()
  }
}
