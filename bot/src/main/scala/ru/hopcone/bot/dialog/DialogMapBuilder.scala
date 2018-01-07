package ru.hopcone.bot.dialog

import ru.hopcone.bot.dao.CategoriesDAO
import ru.hopcone.bot.dialog.profile.AccountInfoStep
import ru.hopcone.bot.models.{DatabaseManager, DialogStepContext}

class DialogMapBuilder(implicit db: DatabaseManager, ctx: DialogStepContext) {

  import DialogMapBuilder._

  // DON'T REMOVE TYPES BECAUSE OF RECURSIVE LAZY VALS

  val rootStep = BasicDialogStep(MainMenuTitle,
    Seq(MenuButton, AccountInfoButton),
    Map(
      MenuButton -> userMenuStep,
      AccountInfoButton -> accountInfoStep
    )
  )

  lazy val userMenuStep: CategoryListStep = CategoryListStep(CategoriesDAO.rootCategories(), () => rootStep)
  lazy val accountInfoStep: DialogStep = AccountInfoStep(() => rootStep)


  def build: DialogMap = DialogMap(rootStep)
}

object DialogMapBuilder extends DialogButtons {
  def apply()(implicit db: DatabaseManager, ctx: DialogStepContext): DialogMap = new DialogMapBuilder().build
}
