package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.dao.CategoriesDAO
import ru.hopcone.bot.data.models.Database

class DialogMapBuilder(implicit db: Database) {

  import DialogMapBuilder._

  private val menuStep = CategoryListStep(CategoriesDAO.rootCategories())
  private val accountMenuStep = BasicDialogStep(AccountInfoButton,Seq.empty,Map.empty)


  // val rootStep: DialogStep = BasicDialogStep("root",Seq.empty,Map.empty)
  val rootStep = BasicDialogStep(MainMenuTitle,
    Seq(MenuButton, AccountInfoButton),
    Map(
      MenuButton -> menuStep ,
      AccountInfoButton -> accountMenuStep
    )
  )

  def build:DialogMap = DialogMap(rootStep)
}

object DialogMapBuilder {
  val MenuButton = "Меню"
  val AccountInfoButton = "Личный кабинет"
  val MainMenuTitle = "Главное меню"

}
