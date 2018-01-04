package ru.hopcone.bot

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import ru.hopcone.bot.models.DB

object Main extends LazyLogging {
  def main(args: Array[String]): Unit = {

    import scala.concurrent.ExecutionContext.Implicits.global

    val config = ConfigFactory.load()
    logger.info(s"Starting with config $config")

    val db = DB.database("hopcone_database")
    DB.drop(db)

    try {
      val bot = new Bot(config, db)
      val initResult = DB.initialize(db)

      initResult.onComplete { _ =>
        logger.info("DATABASE CREATED")
        bot.connect()
      }

      System.in.read()
      bot.stop()
    } finally {
      //sess.close()
      db.close()
    }
  }
}
