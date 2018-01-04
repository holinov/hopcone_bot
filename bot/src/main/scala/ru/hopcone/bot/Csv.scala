package ru.hopcone.bot

import purecsv.unsafe._
import ru.hopcone.bot.models.Tables.{ShopCategoryRow, ShopItemRow}

import scala.io.Source

object Csv {
  val CatsCsvHeader = Seq("id", "name", "parentId")
  val ItemsCsvHeader = Seq("id", "name", "categoryId", "price")

  def header(file: String): Seq[String] = {
    val headerLine = {
      val src = Source.fromFile(file)
      val line = src.getLines.take(1).toList.head
      src.close
      line
    }
    headerLine.split(",")
  }

  def exportCatsToCsv(seq: Seq[ShopCategoryRow], file: String): Unit = {
    seq.writeCSVToFileName(file, header = Some(CatsCsvHeader))
  }

  def exportItemsToCsv(seq: Seq[ShopItemRow], file: String): Unit = {
    seq.map(itemToData).toList.writeCSVToFileName(file, header = Some(ItemsCsvHeader))
  }

  def loadCats(file: String): List[ShopCategoryRow] =
    CSVReader[ShopCategoryRow].readCSVFromFileName(file, skipHeader = true)

  def loadItems(file: String): List[ShopItemRow] =
    CSVReader[ShopItemRowData].readCSVFromFileName(file, skipHeader = true).map(dataToItem)

  private def itemToData(row: ShopItemRow): ShopItemRowData = ShopItemRowData(row.id, row.name, row.categoryId, row.price.floatValue())

  private def dataToItem(row: ShopItemRowData): ShopItemRow = ShopItemRow(row.id, row.name, row.categoryId, BigDecimal(row.price.toString))

  private case class ShopItemRowData(id: Int, name: String, categoryId: Option[Int] = None, price: Float)

}

