package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.dao.CategoriesDAO
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}

class DialogMapBuilder(implicit db: DatabaseManager, dctx: DialogStepContext) {

  import DialogMapBuilder._

  private lazy val menuStep: CategoryListStep = CategoryListStep(CategoriesDAO.rootCategories(), rootStep)
  private lazy val accountMenuStep: BasicDialogStep = BasicDialogStep(AccountInfoButton, Seq.empty, Map.empty)

  val rootStep = BasicDialogStep(MainMenuTitle,
    Seq(MenuButton, AccountInfoButton),
    Map(
      MenuButton -> menuStep,
      AccountInfoButton -> accountMenuStep
    )
  )

  def build: DialogMap = DialogMap(rootStep)
}

object DialogMapBuilder {
  val MenuButton = "Меню"
  val AccountInfoButton = "Личный кабинет"
  val MainMenuTitle = "Главное меню"
}
