package ru.hopcone.bot.test.support

import org.scalatest.{BeforeAndAfterAll, FunSpec, Matchers}
import ru.hopcone.bot.data.models.{DB, Database}

import scala.concurrent.Await
import scala.concurrent.duration._

class DBBased extends FunSpec with Matchers with BeforeAndAfterAll {
  implicit val db: Database = DB.database("hopcone_database_test")
  private val created = DB.initialize(db)
  Await.ready(created, 10.seconds)

  override protected def afterAll(): Unit = {
    db.close()
  }
}
