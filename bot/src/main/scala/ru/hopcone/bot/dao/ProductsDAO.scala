package ru.hopcone.bot.dao

//import ru.hopcone.bot.models.DB.Tables.ShopItems
import ru.hopcone.bot.models.DatabaseManager
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._

import scala.util.Random


object ProductsDAO extends AbstractDAO[ShopItemRow] {

  def forceLoad(itemsToLoad: List[ShopItemRow])(implicit db: DatabaseManager): Any = {
    itemsToLoad.foreach(i => run(ShopItem.insertOrUpdate(i)))
  }

  def all()(implicit db: DatabaseManager): Seq[ShopItemRow] = run(ShopItem.result)

  def productName(itemId: Int)(implicit db: DatabaseManager): String = run(ShopItem.filter(_.id === itemId).result).head.name

  def productsInCategory(cat: ShopCategoryRow)(implicit db: DatabaseManager): Seq[ShopItemRow] = {
    run(ShopItem.filter(_.categoryId === cat.id).result)
  }

  private def nextIdx(implicit db: DatabaseManager) = Random.nextInt(run(ShopItem.size.result) - 1)+1

  def randomShopItem(implicit db: DatabaseManager): ShopItemRow = {
    run(ShopItem.drop(nextIdx).take(1).result).head
  }
}
