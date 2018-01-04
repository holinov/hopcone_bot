package ru.hopcone.bot.models

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile

  import profile.api._
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(DeliveryAddress.schema, OrderData.schema, OrderItem.schema, ShopCategory.schema, ShopItem.schema, UserInfo.schema).reduceLeft(_ ++ _)

  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table DeliveryAddress
    *
    * @param id      Database column id SqlType(serial), AutoInc, PrimaryKey
    * @param userId  Database column user_id SqlType(int4)
    * @param address Database column address SqlType(text) */
  case class DeliveryAddressRow(id: Int, userId: Int, address: String)

  /** GetResult implicit for fetching DeliveryAddressRow objects using plain SQL queries */
  implicit def GetResultDeliveryAddressRow(implicit e0: GR[Int], e1: GR[String]): GR[DeliveryAddressRow] = GR {
    prs =>
      import prs._
      DeliveryAddressRow.tupled((<<[Int], <<[Int], <<[String]))
  }

  /** Table description of table delivery_address. Objects of this class serve as prototypes for rows in queries. */
  class DeliveryAddress(_tableTag: Tag) extends profile.api.Table[DeliveryAddressRow](_tableTag, "delivery_address") {
    def * = (id, userId, address) <> (DeliveryAddressRow.tupled, DeliveryAddressRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(address)).shaped.<>({ r => import r._; _1.map(_ => DeliveryAddressRow.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column address SqlType(text) */
    val address: Rep[String] = column[String]("address")

    /** Index over (userId) (database name delivery_address_user_id_idx) */
    val index1 = index("delivery_address_user_id_idx", userId)
  }

  /** Collection-like TableQuery object for table DeliveryAddress */
  lazy val DeliveryAddress = new TableQuery(tag => new DeliveryAddress(tag))

  /** Entity class storing rows of table OrderData
    *
    * @param orderId         Database column order_id SqlType(serial), AutoInc, PrimaryKey
    * @param userId          Database column user_id SqlType(int4)
    * @param totalPrice      Database column total_price SqlType(numeric), Default(None)
    * @param createdAt       Database column created_at SqlType(timestamp)
    * @param deliveredAt     Database column delivered_at SqlType(timestamp), Default(None)
    * @param status          Database column status SqlType(varchar), Length(100,true)
    * @param deliveryAddress Database column delivery_address SqlType(int4), Default(None) */
  case class OrderDataRow(orderId: Int, userId: Int, totalPrice: Option[scala.math.BigDecimal] = None, createdAt: java.sql.Timestamp, deliveredAt: Option[java.sql.Timestamp] = None, status: String, deliveryAddress: Option[Int] = None)

  /** GetResult implicit for fetching OrderDataRow objects using plain SQL queries */
  implicit def GetResultOrderDataRow(implicit e0: GR[Int], e1: GR[Option[scala.math.BigDecimal]], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]], e4: GR[String], e5: GR[Option[Int]]): GR[OrderDataRow] = GR {
    prs =>
      import prs._
      OrderDataRow.tupled((<<[Int], <<[Int], <<?[scala.math.BigDecimal], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[String], <<?[Int]))
  }

  /** Table description of table order_data. Objects of this class serve as prototypes for rows in queries. */
  class OrderData(_tableTag: Tag) extends profile.api.Table[OrderDataRow](_tableTag, "order_data") {
    def * = (orderId, userId, totalPrice, createdAt, deliveredAt, status, deliveryAddress) <> (OrderDataRow.tupled, OrderDataRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(orderId), Rep.Some(userId), totalPrice, Rep.Some(createdAt), deliveredAt, Rep.Some(status), deliveryAddress).shaped.<>({ r => import r._; _1.map(_ => OrderDataRow.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get, _7))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column order_id SqlType(serial), AutoInc, PrimaryKey */
    val orderId: Rep[Int] = column[Int]("order_id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(int4) */
    val userId: Rep[Int] = column[Int]("user_id")
    /** Database column total_price SqlType(numeric), Default(None) */
    val totalPrice: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("total_price", O.Default(None))
    /** Database column created_at SqlType(timestamp) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column delivered_at SqlType(timestamp), Default(None) */
    val deliveredAt: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("delivered_at", O.Default(None))
    /** Database column status SqlType(varchar), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100, varying = true))
    /** Database column delivery_address SqlType(int4), Default(None) */
    val deliveryAddress: Rep[Option[Int]] = column[Option[Int]]("delivery_address", O.Default(None))
  }

  /** Collection-like TableQuery object for table OrderData */
  lazy val OrderData = new TableQuery(tag => new OrderData(tag))

  /** Entity class storing rows of table OrderItem
    *
    * @param orderId    Database column order_id SqlType(int4)
    * @param itemId     Database column item_id SqlType(int4)
    * @param amount     Database column amount SqlType(numeric)
    * @param totalPrice Database column total_price SqlType(numeric)
    * @param id         Database column id SqlType(serial), AutoInc, PrimaryKey */
  case class OrderItemRow(orderId: Int, itemId: Int, amount: scala.math.BigDecimal, totalPrice: scala.math.BigDecimal, id: Int)

  /** GetResult implicit for fetching OrderItemRow objects using plain SQL queries */
  implicit def GetResultOrderItemRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal]): GR[OrderItemRow] = GR {
    prs =>
      import prs._
      OrderItemRow.tupled((<<[Int], <<[Int], <<[scala.math.BigDecimal], <<[scala.math.BigDecimal], <<[Int]))
  }

  /** Table description of table order_item. Objects of this class serve as prototypes for rows in queries. */
  class OrderItem(_tableTag: Tag) extends profile.api.Table[OrderItemRow](_tableTag, "order_item") {
    def * = (orderId, itemId, amount, totalPrice, id) <> (OrderItemRow.tupled, OrderItemRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(orderId), Rep.Some(itemId), Rep.Some(amount), Rep.Some(totalPrice), Rep.Some(id)).shaped.<>({ r => import r._; _1.map(_ => OrderItemRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column order_id SqlType(int4) */
    val orderId: Rep[Int] = column[Int]("order_id")
    /** Database column item_id SqlType(int4) */
    val itemId: Rep[Int] = column[Int]("item_id")
    /** Database column amount SqlType(numeric) */
    val amount: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("amount")
    /** Database column total_price SqlType(numeric) */
    val totalPrice: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("total_price")
    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)

    /** Index over (orderId) (database name order_item_order_id_idx) */
    val index1 = index("order_item_order_id_idx", orderId)
  }

  /** Collection-like TableQuery object for table OrderItem */
  lazy val OrderItem = new TableQuery(tag => new OrderItem(tag))

  /** Entity class storing rows of table ShopCategory
    *
    * @param id       Database column id SqlType(int4), PrimaryKey
    * @param name     Database column name SqlType(varchar)
    * @param parentId Database column parent_id SqlType(int4), Default(None) */
  case class ShopCategoryRow(id: Int, name: String, parentId: Option[Int] = None)

  /** GetResult implicit for fetching ShopCategoryRow objects using plain SQL queries */
  implicit def GetResultShopCategoryRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]]): GR[ShopCategoryRow] = GR {
    prs =>
      import prs._
      ShopCategoryRow.tupled((<<[Int], <<[String], <<?[Int]))
  }

  /** Table description of table shop_category. Objects of this class serve as prototypes for rows in queries. */
  class ShopCategory(_tableTag: Tag) extends profile.api.Table[ShopCategoryRow](_tableTag, "shop_category") {
    def * = (id, name, parentId) <> (ShopCategoryRow.tupled, ShopCategoryRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), parentId).shaped.<>({ r => import r._; _1.map(_ => ShopCategoryRow.tupled((_1.get, _2.get, _3))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column parent_id SqlType(int4), Default(None) */
    val parentId: Rep[Option[Int]] = column[Option[Int]]("parent_id", O.Default(None))

    /** Uniqueness Index over (name) (database name shop_category_name_key) */
    val index1 = index("shop_category_name_key", name, unique = true)
    /** Index over (parentId) (database name shop_category_parent_id_idx) */
    val index2 = index("shop_category_parent_id_idx", parentId)
  }

  /** Collection-like TableQuery object for table ShopCategory */
  lazy val ShopCategory = new TableQuery(tag => new ShopCategory(tag))

  /** Entity class storing rows of table ShopItem
    *
    * @param id         Database column id SqlType(serial), AutoInc, PrimaryKey
    * @param name       Database column name SqlType(varchar)
    * @param categoryId Database column category_id SqlType(int4), Default(None)
    * @param price      Database column price SqlType(numeric) */
  case class ShopItemRow(id: Int, name: String, categoryId: Option[Int] = None, price: scala.math.BigDecimal)

  /** GetResult implicit for fetching ShopItemRow objects using plain SQL queries */
  implicit def GetResultShopItemRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[scala.math.BigDecimal]): GR[ShopItemRow] = GR {
    prs =>
      import prs._
      ShopItemRow.tupled((<<[Int], <<[String], <<?[Int], <<[scala.math.BigDecimal]))
  }

  /** Table description of table shop_item. Objects of this class serve as prototypes for rows in queries. */
  class ShopItem(_tableTag: Tag) extends profile.api.Table[ShopItemRow](_tableTag, "shop_item") {
    def * = (id, name, categoryId, price) <> (ShopItemRow.tupled, ShopItemRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), categoryId, Rep.Some(price)).shaped.<>({ r => import r._; _1.map(_ => ShopItemRow.tupled((_1.get, _2.get, _3, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column category_id SqlType(int4), Default(None) */
    val categoryId: Rep[Option[Int]] = column[Option[Int]]("category_id", O.Default(None))
    /** Database column price SqlType(numeric) */
    val price: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("price")

    /** Index over (categoryId) (database name shop_item_category_id_idx) */
    val index1 = index("shop_item_category_id_idx", categoryId)
    /** Uniqueness Index over (name) (database name shop_item_name_key) */
    val index2 = index("shop_item_name_key", name, unique = true)
  }

  /** Collection-like TableQuery object for table ShopItem */
  lazy val ShopItem = new TableQuery(tag => new ShopItem(tag))

  /** Entity class storing rows of table UserInfo
    *
    * @param id             Database column id SqlType(serial), AutoInc, PrimaryKey
    * @param name           Database column name SqlType(varchar), Length(100,true)
    * @param telegramUserId Database column telegram_user_id SqlType(int4) */
  case class UserInfoRow(id: Int, name: String, telegramUserId: Int)

  /** GetResult implicit for fetching UserInfoRow objects using plain SQL queries */
  implicit def GetResultUserInfoRow(implicit e0: GR[Int], e1: GR[String]): GR[UserInfoRow] = GR {
    prs =>
      import prs._
      UserInfoRow.tupled((<<[Int], <<[String], <<[Int]))
  }

  /** Table description of table user_info. Objects of this class serve as prototypes for rows in queries. */
  class UserInfo(_tableTag: Tag) extends profile.api.Table[UserInfoRow](_tableTag, "user_info") {
    def * = (id, name, telegramUserId) <> (UserInfoRow.tupled, UserInfoRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(telegramUserId)).shaped.<>({ r => import r._; _1.map(_ => UserInfoRow.tupled((_1.get, _2.get, _3.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(serial), AutoInc, PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100, varying = true))
    /** Database column telegram_user_id SqlType(int4) */
    val telegramUserId: Rep[Int] = column[Int]("telegram_user_id")

    /** Uniqueness Index over (name,telegramUserId) (database name user_info_name_telegram_user_id_key) */
    val index1 = index("user_info_name_telegram_user_id_key", (name, telegramUserId), unique = true)
  }

  /** Collection-like TableQuery object for table UserInfo */
  lazy val UserInfo = new TableQuery(tag => new UserInfo(tag))
}
