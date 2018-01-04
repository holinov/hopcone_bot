package ru.hopcone.bot.dialog

import ru.hopcone.bot.dialog.DialogMapBuilder._
import ru.hopcone.bot.support.DBBasedSpec


class DialogMapSpec extends DBBasedSpec {
  describe("DialogMap") {
    describe("as data model") {
      it("should describe linear dialog") {
        DialogMap(BasicDialogStep("start", Seq("beer", "food"), Map(
          "beer" -> BasicDialogStep("beer", Seq("ok"), Map.empty),
          "food" -> BasicDialogStep("beer", Seq("ok"), Map.empty)
        )))
      }
    }
  }

  describe("DialogMapBuilder") {
    val builder = new DialogMapBuilder().build

    println(s"ROOT: ${builder.rootStep}")
    val mainTransition = builder.rootStep.next(MenuButton)
    println(s"MT: $mainTransition .. \nROOT:${builder.rootStep}")
    val catTransition = mainTransition.get.next(mainTransition.get.buttons.head)

    it("should build root step info") {
      builder.rootStep.stepText should be(MainMenuTitle)
      builder.rootStep.buttons should have size 2
    }

    it("should build root categories info") {
      mainTransition shouldNot be(None)
      mainTransition.foreach(s => s.buttons should have size 3)
    }

    it(s"should build sub categories from ${catTransition.get.buttons}") {
      val subcatTitle = catTransition.get.buttons.head
      val subcatTransition = catTransition.map(c => {
        c.next(subcatTitle)
      }).get
      subcatTransition shouldNot be(None)
      println(s"SCTR: $subcatTransition")
      subcatTransition.get.buttons shouldNot have size 0
    }
  }

  describe("DialogProcessor") {
    val builder = new DialogMapBuilder
    val processor = new DialogProcessor(DialogMap(builder.rootStep))
    it("should navigate from root menu to product") {
      val res1 = processor.processInput(MenuButton) //MENU
      val res2 = processor.processInput(res1.nextStep.buttons.head) //BEER
      val res3 = processor.processInput(res2.nextStep.buttons.head) //IPA

      res3.nextStep.buttons should contain("BEER 1")
      res3.nextStep.buttons should contain("BEER 2")
      res3.nextStep.buttons should contain("BEER 3")
    }
  }
}