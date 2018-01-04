package ru.hopcone.bot


//object Csv {
//
//  trait ToCsv[T] {
//    def render:String
//  }
//
//  implicit def catToCsv(c:ShopCategoryRow):ToCsv[ShopCategoryRow] = new ToCsv[ShopCategoryRow] {
//    override def render: String = ???
//  }
//
//  def exportToCsv[T](seq: Seq[ToCsv[T]]): String = seq.map(_.render).mkString("\n")
//}
