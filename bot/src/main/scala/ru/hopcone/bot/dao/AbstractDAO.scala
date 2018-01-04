package ru.hopcone.bot.dao

//import ru.hopcone.bot.dao.ProductsDAO.MaxTimeout
import com.typesafe.scalalogging.LazyLogging
import ru.hopcone.bot.{DefaultImplicits, models}
import slick.dbio.{DBIOAction, NoStream}

import scala.concurrent.Await
import scala.concurrent.duration._

trait AbstractDAO[EntryType] extends LazyLogging with DefaultImplicits {

  val NewEntryId: Int = -1

  protected val MaxTimeout: Duration = 10.seconds

  def run[T](insertStmt: DBIOAction[T, NoStream, Nothing])
            (implicit db: models.DatabaseManager): T =
    Await.result(db.run(insertStmt), MaxTimeout)
}
