package ru.hopcone.bot.models

import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object DB {
  protected val MaxTimeout: Duration = 10.seconds

  def drop(db: DatabaseManager): Future[Unit] = {
    Await.ready(db.run(Tables.schema.drop), MaxTimeout)
  }

  def initialize(db: DatabaseManager): Future[Unit] = {
    Await.ready(db.run(Tables.schema.create), MaxTimeout)
    Await.ready(db.run(seedData), MaxTimeout)
  }

  val BeerCategory = ShopCategoryRow(1, "Пиво")
  val FoodCategory = ShopCategoryRow(2, "Еда")
  val ApperetizersCategory = ShopCategoryRow(3, "Закуски")

  val IpaBeerCategory = ShopCategoryRow(4, "IPA", Some(BeerCategory.id))
  val ApaBeerCategory = ShopCategoryRow(5, "APA", Some(BeerCategory.id))
  val LagerBeerCategory = ShopCategoryRow(6, "Lager", Some(BeerCategory.id))


  val seedData = DBIO.seq(
    Tables.ShopCategory ++= Seq(BeerCategory, FoodCategory, ApperetizersCategory),

    Tables.ShopCategory ++= Seq(IpaBeerCategory, ApaBeerCategory, LagerBeerCategory),

    Tables.ShopItem ++= Seq(
      //FOOD
      ShopItemRow(0, "FOOD 1", Some(FoodCategory.id), 1),
      ShopItemRow(1, "FOOD 2", Some(FoodCategory.id), 2),
      //BEER
      ShopItemRow(2, "BEER 1", Some(IpaBeerCategory.id), 3),
      ShopItemRow(3, "BEER 2", Some(IpaBeerCategory.id), 4),
      ShopItemRow(4, "BEER 3", Some(IpaBeerCategory.id), 5.1)
    ),

    Tables.DeliveryAddress ++= Seq(
      DeliveryAddressRow(1, None, "HOPCONE"),
      DeliveryAddressRow(2, None, "КРАФТ & КРАБ")
    )
  )

  def database(configName: String): DatabaseManager = Database.forConfig(configName)
}
