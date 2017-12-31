package ru.hopcone.bot.data.dao

import ru.hopcone.bot.data.dao.ProductsDAO.MaxTimeout
import ru.hopcone.bot.data.models._
import slick.dbio.Effect
import slick.sql.FixedSqlStreamingAction

import scala.concurrent.Await
import scala.concurrent.duration._

trait AbstractDAO[EntryType] {

  protected val MaxTimeout: Duration = 10.seconds
  def run[T](stmt: FixedSqlStreamingAction[Seq[T], T, Effect.Read])
            (implicit db: Database)
  : Seq[T] = Await.result(db.run(stmt), MaxTimeout)

//  def run(stmt:FixedSqlStreamingAction[Seq[T], T, Effect.Read])
//         (implicit db: Database): Seq[T] =
//    db.run(stmt).result(MaxTimeout)
}
