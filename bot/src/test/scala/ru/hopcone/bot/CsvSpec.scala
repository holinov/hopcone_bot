package ru.hopcone.bot

import ru.hopcone.bot.dao.{CategoriesDAO, ProductsDAO}
import ru.hopcone.bot.support.DBBasedSpec

class CsvSpec extends DBBasedSpec {
  describe("Csv") {
    describe("categories") {
      val file = "./downloads/cats.csv"
      it("should export data to file and load it back") {
        val cats = CategoriesDAO.all
        Csv.exportCatsToCsv(cats, file)

        val loadedCats = Csv.loadCats(file)
        loadedCats should be(cats)

        CategoriesDAO.forceLoad(loadedCats)
      }
    }

    describe("items") {
      val file = "./downloads/items.csv"
      it("should export data to file and load it back") {
        val cats = ProductsDAO.all
        Csv.exportItemsToCsv(cats, file)


        val loadedCats = Csv.loadItems(file)
        loadedCats should be(cats)

        ProductsDAO.forceLoad(loadedCats)
      }
    }
  }
}
