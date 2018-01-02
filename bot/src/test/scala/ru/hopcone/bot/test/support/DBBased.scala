package ru.hopcone.bot.test.support

import org.scalatest.BeforeAndAfterAll
import ru.hopcone.bot.support.BaseSpec

class DBBased extends BaseSpec("hopcone_database_test") with BeforeAndAfterAll {
  //implicit val database: DatabaseManager = DB.database("hopcone_database_test")

}
