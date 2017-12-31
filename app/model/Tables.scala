package model
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(Categories.schema, Deliveryaddress.schema, Items.schema, Orderdata.schema, Orderitem.schema, Shopcategory.schema, Shopitem.schema, Userinfo.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Categories
   *  @param catId Database column cat_id SqlType(int8), PrimaryKey
   *  @param name Database column name SqlType(varchar)
   *  @param description Database column description SqlType(varchar)
   *  @param parentId Database column parent_id SqlType(int8), Default(None) */
  case class CategoriesRow(catId: Long, name: String, description: String, parentId: Option[Long] = None)
  /** GetResult implicit for fetching CategoriesRow objects using plain SQL queries */
  implicit def GetResultCategoriesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[Long]]): GR[CategoriesRow] = GR{
    prs =>
    CategoriesRow.tupled((<<[Long], <<[String], <<[String], <<?[Long]))
  }
  /** Table description of table categories. Objects of this class serve as prototypes for rows in queries. */
  class Categories(_tableTag: Tag) extends profile.api.Table[CategoriesRow](_tableTag, "categories") {
    def * = (catId, name, description, parentId) <> (CategoriesRow.tupled, CategoriesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(catId), Rep.Some(name), Rep.Some(description), parentId).shaped.<>({r=>; _1.map(_=> CategoriesRow.tupled((_1.get, _2.get, _3.get, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column cat_id SqlType(int8), PrimaryKey */
    val catId: Rep[Long] = column[Long]("cat_id", O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column description SqlType(varchar) */
    val description: Rep[String] = column[String]("description")
    /** Database column parent_id SqlType(int8), Default(None) */
    val parentId: Rep[Option[Long]] = column[Option[Long]]("parent_id", O.Default(None))

    /** Foreign key referencing Categories (database name parent_fk) */
    lazy val categoriesFk = foreignKey("parent_fk", parentId, Categories)(r => Rep.Some(r.catId), onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Categories */
  lazy val Categories = new TableQuery(tag => new Categories(tag))

  /** Entity class storing rows of table Deliveryaddress
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param userid Database column userid SqlType(int4)
   *  @param address Database column address SqlType(text) */
  case class DeliveryaddressRow(id: Int, userid: Int, address: String)
  /** GetResult implicit for fetching DeliveryaddressRow objects using plain SQL queries */
  implicit def GetResultDeliveryaddressRow(implicit e0: GR[Int], e1: GR[String]): GR[DeliveryaddressRow] = GR{
    prs =>
    DeliveryaddressRow.tupled((<<[Int], <<[Int], <<[String]))
  }
  /** Table description of table deliveryaddress. Objects of this class serve as prototypes for rows in queries. */
  class Deliveryaddress(_tableTag: Tag) extends profile.api.Table[DeliveryaddressRow](_tableTag, "deliveryaddress") {
    def * = (id, userid, address) <> (DeliveryaddressRow.tupled, DeliveryaddressRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userid), Rep.Some(address)).shaped.<>({r=>; _1.map(_=> DeliveryaddressRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column userid SqlType(int4) */
    val userid: Rep[Int] = column[Int]("userid")
    /** Database column address SqlType(text) */
    val address: Rep[String] = column[String]("address")

    /** Index over (userid) (database name deliveryaddress_userid_idx) */
    val index1 = index("deliveryaddress_userid_idx", userid)
  }
  /** Collection-like TableQuery object for table Deliveryaddress */
  lazy val Deliveryaddress = new TableQuery(tag => new Deliveryaddress(tag))

  /** Entity class storing rows of table Items
   *  @param itemId Database column item_id SqlType(int8), PrimaryKey
   *  @param catId Database column cat_id SqlType(int8)
   *  @param itemName Database column item_name SqlType(varchar)
   *  @param itemDescription Database column item_description SqlType(varchar)
   *  @param itemPrice Database column item_price SqlType(numeric) */
  case class ItemsRow(itemId: Long, catId: Long, itemName: String, itemDescription: String, itemPrice: scala.math.BigDecimal)
  /** GetResult implicit for fetching ItemsRow objects using plain SQL queries */
  implicit def GetResultItemsRow(implicit e0: GR[Long], e1: GR[String], e2: GR[scala.math.BigDecimal]): GR[ItemsRow] = GR{
    prs =>
    ItemsRow.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[scala.math.BigDecimal]))
  }
  /** Table description of table items. Objects of this class serve as prototypes for rows in queries. */
  class Items(_tableTag: Tag) extends profile.api.Table[ItemsRow](_tableTag, "items") {
    def * = (itemId, catId, itemName, itemDescription, itemPrice) <> (ItemsRow.tupled, ItemsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(itemId), Rep.Some(catId), Rep.Some(itemName), Rep.Some(itemDescription), Rep.Some(itemPrice)).shaped.<>({r=>; _1.map(_=> ItemsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column item_id SqlType(int8), PrimaryKey */
    val itemId: Rep[Long] = column[Long]("item_id", O.PrimaryKey)
    /** Database column cat_id SqlType(int8) */
    val catId: Rep[Long] = column[Long]("cat_id")
    /** Database column item_name SqlType(varchar) */
    val itemName: Rep[String] = column[String]("item_name")
    /** Database column item_description SqlType(varchar) */
    val itemDescription: Rep[String] = column[String]("item_description")
    /** Database column item_price SqlType(numeric) */
    val itemPrice: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("item_price")

    /** Foreign key referencing Categories (database name category_fk) */
    lazy val categoriesFk = foreignKey("category_fk", catId, Categories)(r => r.catId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Items */
  lazy val Items = new TableQuery(tag => new Items(tag))

  /** Entity class storing rows of table Orderdata
   *  @param orderid Database column orderid SqlType(int4), PrimaryKey
   *  @param userid Database column userid SqlType(int4)
   *  @param totalprice Database column totalprice SqlType(numeric), Default(None)
   *  @param createdat Database column createdat SqlType(timestamp)
   *  @param deliveredat Database column deliveredat SqlType(timestamp), Default(None)
   *  @param status Database column status SqlType(varchar), Length(100,true)
   *  @param deliveryaddress Database column deliveryaddress SqlType(int4) */
  case class OrderdataRow(orderid: Int, userid: Int, totalprice: Option[scala.math.BigDecimal] = None, createdat: java.sql.Timestamp, deliveredat: Option[java.sql.Timestamp] = None, status: String, deliveryaddress: Int)
  /** GetResult implicit for fetching OrderdataRow objects using plain SQL queries */
  implicit def GetResultOrderdataRow(implicit e0: GR[Int], e1: GR[Option[scala.math.BigDecimal]], e2: GR[java.sql.Timestamp], e3: GR[Option[java.sql.Timestamp]], e4: GR[String]): GR[OrderdataRow] = GR{
    prs =>
    OrderdataRow.tupled((<<[Int], <<[Int], <<?[scala.math.BigDecimal], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[String], <<[Int]))
  }
  /** Table description of table orderdata. Objects of this class serve as prototypes for rows in queries. */
  class Orderdata(_tableTag: Tag) extends profile.api.Table[OrderdataRow](_tableTag, "orderdata") {
    def * = (orderid, userid, totalprice, createdat, deliveredat, status, deliveryaddress) <> (OrderdataRow.tupled, OrderdataRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(orderid), Rep.Some(userid), totalprice, Rep.Some(createdat), deliveredat, Rep.Some(status), Rep.Some(deliveryaddress)).shaped.<>({r=>; _1.map(_=> OrderdataRow.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column orderid SqlType(int4), PrimaryKey */
    val orderid: Rep[Int] = column[Int]("orderid", O.PrimaryKey)
    /** Database column userid SqlType(int4) */
    val userid: Rep[Int] = column[Int]("userid")
    /** Database column totalprice SqlType(numeric), Default(None) */
    val totalprice: Rep[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("totalprice", O.Default(None))
    /** Database column createdat SqlType(timestamp) */
    val createdat: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("createdat")
    /** Database column deliveredat SqlType(timestamp), Default(None) */
    val deliveredat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("deliveredat", O.Default(None))
    /** Database column status SqlType(varchar), Length(100,true) */
    val status: Rep[String] = column[String]("status", O.Length(100,varying=true))
    /** Database column deliveryaddress SqlType(int4) */
    val deliveryaddress: Rep[Int] = column[Int]("deliveryaddress")

    /** Index over (deliveryaddress) (database name orderdata_deliveryaddress_idx) */
    val index1 = index("orderdata_deliveryaddress_idx", deliveryaddress)
    /** Index over (userid) (database name orderdata_userid_idx) */
    val index2 = index("orderdata_userid_idx", userid)
  }
  /** Collection-like TableQuery object for table Orderdata */
  lazy val Orderdata = new TableQuery(tag => new Orderdata(tag))

  /** Entity class storing rows of table Orderitem
   *  @param orderid Database column orderid SqlType(int4)
   *  @param itemid Database column itemid SqlType(int4)
   *  @param amount Database column amount SqlType(int4)
   *  @param totalprice Database column totalprice SqlType(numeric) */
  case class OrderitemRow(orderid: Int, itemid: Int, amount: Int, totalprice: scala.math.BigDecimal)
  /** GetResult implicit for fetching OrderitemRow objects using plain SQL queries */
  implicit def GetResultOrderitemRow(implicit e0: GR[Int], e1: GR[scala.math.BigDecimal]): GR[OrderitemRow] = GR{
    prs =>
    OrderitemRow.tupled((<<[Int], <<[Int], <<[Int], <<[scala.math.BigDecimal]))
  }
  /** Table description of table orderitem. Objects of this class serve as prototypes for rows in queries. */
  class Orderitem(_tableTag: Tag) extends profile.api.Table[OrderitemRow](_tableTag, "orderitem") {
    def * = (orderid, itemid, amount, totalprice) <> (OrderitemRow.tupled, OrderitemRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(orderid), Rep.Some(itemid), Rep.Some(amount), Rep.Some(totalprice)).shaped.<>({r=>; _1.map(_=> OrderitemRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column orderid SqlType(int4) */
    val orderid: Rep[Int] = column[Int]("orderid")
    /** Database column itemid SqlType(int4) */
    val itemid: Rep[Int] = column[Int]("itemid")
    /** Database column amount SqlType(int4) */
    val amount: Rep[Int] = column[Int]("amount")
    /** Database column totalprice SqlType(numeric) */
    val totalprice: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("totalprice")

    /** Index over (orderid) (database name orderitem_orderid_idx) */
    val index1 = index("orderitem_orderid_idx", orderid)
  }
  /** Collection-like TableQuery object for table Orderitem */
  lazy val Orderitem = new TableQuery(tag => new Orderitem(tag))

  /** Entity class storing rows of table Shopcategory
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param name Database column name SqlType(varchar)
   *  @param parentid Database column parentid SqlType(int4), Default(None) */
  case class ShopcategoryRow(id: Int, name: String, parentid: Option[Int] = None)
  /** GetResult implicit for fetching ShopcategoryRow objects using plain SQL queries */
  implicit def GetResultShopcategoryRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]]): GR[ShopcategoryRow] = GR{
    prs =>
    ShopcategoryRow.tupled((<<[Int], <<[String], <<?[Int]))
  }
  /** Table description of table shopcategory. Objects of this class serve as prototypes for rows in queries. */
  class Shopcategory(_tableTag: Tag) extends profile.api.Table[ShopcategoryRow](_tableTag, "shopcategory") {
    def * = (id, name, parentid) <> (ShopcategoryRow.tupled, ShopcategoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), parentid).shaped.<>({r=>; _1.map(_=> ShopcategoryRow.tupled((_1.get, _2.get, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column parentid SqlType(int4), Default(None) */
    val parentid: Rep[Option[Int]] = column[Option[Int]]("parentid", O.Default(None))

    /** Uniqueness Index over (name) (database name shopcategory_name_key) */
    val index1 = index("shopcategory_name_key", name, unique=true)
    /** Index over (parentid) (database name shopcategory_parentid_idx) */
    val index2 = index("shopcategory_parentid_idx", parentid)
  }
  /** Collection-like TableQuery object for table Shopcategory */
  lazy val Shopcategory = new TableQuery(tag => new Shopcategory(tag))

  /** Entity class storing rows of table Shopitem
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param name Database column name SqlType(varchar)
   *  @param categoryid Database column categoryid SqlType(int4), Default(None)
   *  @param price Database column price SqlType(numeric) */
  case class ShopitemRow(id: Int, name: String, categoryid: Option[Int] = None, price: scala.math.BigDecimal)
  /** GetResult implicit for fetching ShopitemRow objects using plain SQL queries */
  implicit def GetResultShopitemRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Option[Int]], e3: GR[scala.math.BigDecimal]): GR[ShopitemRow] = GR{
    prs =>
    ShopitemRow.tupled((<<[Int], <<[String], <<?[Int], <<[scala.math.BigDecimal]))
  }
  /** Table description of table shopitem. Objects of this class serve as prototypes for rows in queries. */
  class Shopitem(_tableTag: Tag) extends profile.api.Table[ShopitemRow](_tableTag, "shopitem") {
    def * = (id, name, categoryid, price) <> (ShopitemRow.tupled, ShopitemRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), categoryid, Rep.Some(price)).shaped.<>({r=>; _1.map(_=> ShopitemRow.tupled((_1.get, _2.get, _3, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar) */
    val name: Rep[String] = column[String]("name")
    /** Database column categoryid SqlType(int4), Default(None) */
    val categoryid: Rep[Option[Int]] = column[Option[Int]]("categoryid", O.Default(None))
    /** Database column price SqlType(numeric) */
    val price: Rep[scala.math.BigDecimal] = column[scala.math.BigDecimal]("price")

    /** Index over (categoryid) (database name shopitem_categoryid_idx) */
    val index1 = index("shopitem_categoryid_idx", categoryid)
    /** Uniqueness Index over (name) (database name shopitem_name_key) */
    val index2 = index("shopitem_name_key", name, unique=true)
  }
  /** Collection-like TableQuery object for table Shopitem */
  lazy val Shopitem = new TableQuery(tag => new Shopitem(tag))

  /** Entity class storing rows of table Userinfo
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(100,true)
   *  @param telegramuserid Database column telegramuserid SqlType(int4) */
  case class UserinfoRow(id: Int, name: String, telegramuserid: Int)
  /** GetResult implicit for fetching UserinfoRow objects using plain SQL queries */
  implicit def GetResultUserinfoRow(implicit e0: GR[Int], e1: GR[String]): GR[UserinfoRow] = GR{
    prs =>
    UserinfoRow.tupled((<<[Int], <<[String], <<[Int]))
  }
  /** Table description of table userinfo. Objects of this class serve as prototypes for rows in queries. */
  class Userinfo(_tableTag: Tag) extends profile.api.Table[UserinfoRow](_tableTag, "userinfo") {
    def * = (id, name, telegramuserid) <> (UserinfoRow.tupled, UserinfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(name), Rep.Some(telegramuserid)).shaped.<>({r=>; _1.map(_=> UserinfoRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(100,true) */
    val name: Rep[String] = column[String]("name", O.Length(100,varying=true))
    /** Database column telegramuserid SqlType(int4) */
    val telegramuserid: Rep[Int] = column[Int]("telegramuserid")

    /** Uniqueness Index over (name,telegramuserid) (database name userinfo_name_telegramuserid_key) */
    val index1 = index("userinfo_name_telegramuserid_key", (name, telegramuserid), unique=true)
  }
  /** Collection-like TableQuery object for table Userinfo */
  lazy val Userinfo = new TableQuery(tag => new Userinfo(tag))
}
