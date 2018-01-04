package ru.hopcone.bot

import com.typesafe.scalalogging.LazyLogging

object FileUtils extends LazyLogging {

  import java.io.File
  import java.net.URL

  import scala.language.postfixOps
  import sys.process._

  private var cache = Set[String]()

  private def mkdirp(path: String) {
    var prepath = ""

    for (dir <- path.split("/")) {
      prepath += (dir + "/")
      if (!cache.contains(prepath)) {
        val file = new java.io.File(prepath)
        if (!file.exists()) file.mkdir()
        cache += prepath
      }
    }
  }


  def downloadFileOld(url: String, filename: String): String = {
    new URL(url) #> new File(filename) !!
  }

  //  def downloadFile1(url: String, filename: String) {
  //    try {
  //      val src = scala.io.Source.fromURL(url)
  //      val out = new java.io.FileWriter(filename)
  //      out.write(src.mkString)
  //      out.close()
  //    } catch {
  //      case NonFatal(ex) => logger.error(s"Error downloading file from $url to $filename", ex)
  //    }
  //  }
  //
  //  def downloadFile(url: String, filename: String) = {
  //    import scala.io.Source
  //    val data = Source.fromURL(url)
  //    val writer = new FileWriter(new File(filename))
  //    try {
  //      for (line <- data.getLines()) {
  //        writer.write(line)
  //      }
  //    } catch {
  //      case NonFatal(ex) => logger.error(s"Error downloading file from $url to $filename", ex)
  //    }
  //    finally {
  //      data.close()
  //      writer.close()
  //    }
  //  }

  def ensureDir(path: String): Unit = mkdirp(path)
}
