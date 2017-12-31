package ru.hopcone.bot.data.dao

//import ru.hopcone.bot.data.models.DB.Tables.ShopItems
import ru.hopcone.bot.data.models.Database
import ru.hopcone.bot.models.Tables._
import slick.jdbc.PostgresProfile.api._


object ProductsDAO extends AbstractDAO[ShopItemRow] {
  def productsInCategory(cat: ShopCategoryRow)(implicit db: Database): Seq[ShopItemRow] = {
    run(ShopItem.filter(_.categoryId === cat.id).result)
  }
}
