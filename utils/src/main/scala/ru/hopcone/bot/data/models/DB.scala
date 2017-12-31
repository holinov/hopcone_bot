package ru.hopcone.bot.data.models

import ru.hopcone.bot.models.Tables
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.duration._

object DB {
  protected val MaxTimeout: Duration = 10.seconds

  def initialize(db: Database) = {
    Await.ready(db.run(initDatabase), MaxTimeout)

    println(s"SEEDING DATA. ${seedData}")
    Await.ready(db.run(seedData), MaxTimeout)
  }


  val seedData = DBIO.seq(
    Tables.ShopCategory ++= Seq(
      ShopCategoryRow(0, "Пиво"),
      ShopCategoryRow(1, "Еда"),
      ShopCategoryRow(2, "Закуски")
    ),

    Tables.ShopCategory ++= Seq(
      ShopCategoryRow(3, "IPA", Some(0)),
      ShopCategoryRow(4, "APA", Some(0)),
      ShopCategoryRow(5, "Lager", Some(0)),
    ),

    Tables.ShopItem ++= Seq(
      //FOOD
      ShopItemRow(0, "FOOD 1", Some(1), 1),
      ShopItemRow(1, "FOOD 2", Some(1), 2),
      //BEER
      ShopItemRow(2, "BEER 1", Some(3), 3),
      ShopItemRow(3, "BEER 2", Some(3), 4),
      ShopItemRow(4, "BEER 3", Some(3), 5.1)
    )
  )

  lazy val initDatabase = DBIO.seq(
    //Tables
    Tables.schema.create,

    Tables.ShopCategory ++= Seq(
      ShopCategoryRow(0, "Пиво", None),
      ShopCategoryRow(3, "IPA", Some(0)),
      ShopCategoryRow(4, "APA", Some(0)),
      ShopCategoryRow(5, "APA", Some(0)),

      ShopCategoryRow(1, "Еда", None),
      ShopCategoryRow(2, "Закуски", None)
    ),

    Tables.ShopItem ++= Seq(
      //FOOD
      ShopItemRow(0, "FOOD 1", Some(1), 1),
      ShopItemRow(1, "FOOD 2", Some(1), 2),
      //BEER
      ShopItemRow(2, "BEER 1", Some(4), 3),
      ShopItemRow(3, "BEER 2", Some(4), 4),
      ShopItemRow(4, "BEER 3", Some(4), 5.1)
    )
  )

  def database(configName: String): Database = Database.forConfig(configName)

  //  object Tables {
  //
  //    class ProductCategories(tag: Tag) extends Table[ProductCategory](tag, "categories") {
  //      override def * = (id, name, description, parentId) <> (ProductCategory.tupled, ProductCategory.unapply)
  //
  //      // Fields
  //      def id = column[Long]("cat_id", O.PrimaryKey)
  //      def name = column[String]("name")
  //      def description = column[String]("description")
  //
  //      //Indexes
  //      def parent = foreignKey("parent_fk", parentId, CategoriesDAO)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  //      def parentId = column[Option[Long]]("parent_id")
  //    }
  //
  //    class ShopItems(tag: Tag) extends Table[ShopItem](tag, "items") {
  //      override def * = (id, categoryId, name, description, price) <> (ShopItem.tupled, ShopItem.unapply)
  //
  //      // Fields
  //      def id = column[Long]("item_id", O.PrimaryKey)
  //      def name = column[String]("item_name")
  //      def description = column[String]("item_description")
  //      def categoryId = column[Long]("cat_id")
  //      def price = column[BigDecimal]("item_price")
  //
  //      //Indexes
  //      def category = foreignKey("category_fk", categoryId, CategoriesDAO)(_.id)
  //    }
  //  }
}
