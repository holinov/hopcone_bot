package ru.hopcone.bot.data.models

import ru.hopcone.bot.data.dao._
import slick.jdbc.PostgresProfile.api._

object DB {

  lazy val initDatabase = DBIO.seq(
    dbScheme.create,

    //TODO: SEED DB
    CategoriesDAO ++= Seq(
      ProductCategory(0, "Пиво"),
      ProductCategory(1, "Еда"),
      ProductCategory(3, "Закуски"),
      ProductCategory(4, "IPA", parentId = Some(0)),
      ProductCategory(5, "APA", parentId = Some(0)),
      ProductCategory(6, "Lager", parentId = Some(0))
    ),

    ProductsDAO ++= Seq(
      //FOOD
      ShopItem(0, 1, "FOOD 1", "NO DESC 1", 1),
      ShopItem(1, 1, "FOOD 2", "NO DESC 2", 2),
      //BEER
      ShopItem(2, 4, "BEER 1", "NO DESC 3", 3),
      ShopItem(3, 4, "BEER 2", "NO DESC 4", 4),
      ShopItem(4, 4, "BEER 3", "NO DESC 5", 5.1)
    )
  )

  lazy val dbScheme = CategoriesDAO.schema ++ ProductsDAO.schema

  def database(configName: String): Database = Database.forConfig(configName)

  object Tables {

    class ProductCategories(tag: Tag) extends Table[ProductCategory](tag, "categories") {
      override def * = (id, name, description, parentId) <> (ProductCategory.tupled, ProductCategory.unapply)

      // Fields
      def id = column[Long]("cat_id", O.PrimaryKey)
      def name = column[String]("name")
      def description = column[String]("description")

      //Indexes
      def parent = foreignKey("parent_fk", parentId, CategoriesDAO)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
      def parentId = column[Option[Long]]("parent_id")
    }

    class ShopItems(tag: Tag) extends Table[ShopItem](tag, "items") {
      override def * = (id, categoryId, name, description, price) <> (ShopItem.tupled, ShopItem.unapply)

      // Fields
      def id = column[Long]("item_id", O.PrimaryKey)
      def name = column[String]("item_name")
      def description = column[String]("item_description")
      def categoryId = column[Long]("cat_id")
      def price = column[BigDecimal]("item_price")

      //Indexes
      def category = foreignKey("category_fk", categoryId, CategoriesDAO)(_.id)
    }
  }
}
