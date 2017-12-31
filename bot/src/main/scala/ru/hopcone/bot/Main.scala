package ru.hopcone.bot

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import ru.hopcone.bot.data.models.DB

object Main extends LazyLogging {
  def main(args: Array[String]): Unit = {

    import scala.concurrent.ExecutionContext.Implicits.global

    val config = ConfigFactory.defaultApplication()
    logger.info(s"Starting with config $config")

    val db = DB.database("hopcone_database")

    try {
      val bot = new Bot(config, db)
      val initResult = db.run(DB.initDatabase)

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