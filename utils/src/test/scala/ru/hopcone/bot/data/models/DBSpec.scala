package ru.hopcone.bot.data.models

import org.scalatest._
import ru.hopcone.bot.models.Tables

class DBSpec extends FunSpec with Matchers {
  describe("Tables DDL object") {
    describe("(while generating DDL)"){
      it("should generate non empty DDL") {
        val statementsText = Tables.schema.createStatements.toList.mkString("\n")
        println(statementsText)
        statementsText shouldNot have size 0
      }
    }

    describe("(while populating DB)"){
      println("OK 1")
    }
  }
}
