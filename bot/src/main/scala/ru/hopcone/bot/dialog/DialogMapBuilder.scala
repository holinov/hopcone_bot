package ru.hopcone.bot.dialog

import ru.hopcone.bot.data.dao.CategoriesDAO
import ru.hopcone.bot.data.models.{DatabaseManager, DialogStepContext}
import ru.hopcone.bot.dialog.cart.DialogStep

class DialogMapBuilder(implicit db: DatabaseManager, ctx: DialogStepContext) {

  import DialogMapBuilder._

  // DON'T REMOVE TYPES BECAUSE OF RECURSIVE LAZY VALS
  private lazy val userMenuStep: CategoryListStep = CategoryListStep(CategoriesDAO.rootCategories(), rootStep)
  private lazy val accountInfoStep: DialogStep = AccountInfoStep(rootStep)

  val rootStep = BasicDialogStep(MainMenuTitle,
    Seq(MenuButton, AccountInfoButton),
    Map(
      MenuButton -> userMenuStep,
      AccountInfoButton -> accountInfoStep
    )
  )

  def build: DialogMap = DialogMap(rootStep)
}

object DialogMapBuilder extends DialogButtons
