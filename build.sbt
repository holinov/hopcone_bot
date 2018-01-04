val json4sVersion = "3.5.3"
val slickVersion = "3.2.1"
val akkaVersion = "2.5.8"
val akkaHttpVersion = "10.0.11"

lazy val commonSettings = Seq(
  organization := "ru.hopcone",
  scalaVersion := "2.12.4",
  version := "0.1",
  scalacOptions := Seq("-feature", "-unchecked", "-deprecation"),

  libraryDependencies ++= Seq(
    // CONFIG
    "com.typesafe" % "config" % "1.3.2",

    // LOGS
    "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3",

    // SQL CONNECTIONS
    "com.typesafe.slick" %% "slick" % slickVersion,
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
    "com.typesafe.slick" %% "slick-codegen" % slickVersion,

    "org.postgresql" % "postgresql" % "42.1.4",

    // DI/IoC
    "net.codingwell" %% "scala-guice" % "4.1.1",
    "com.sandinh" %% "akka-guice" % "3.2.0",

    //TELEGRAM API
    "info.mukel" %% "telegrambot4s" % "3.0.14" excludeAll ExclusionRule("com.typesafe.akka"),

    // AKKA
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion excludeAll ExclusionRule("com.typesafe.akka", "akka-stream"),

    //CSW
    "com.github.melrief" %% "purecsv" % "0.1.1",

    // TESTS
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "org.scalatest" %% "scalatest" % "3.0.4" % Test
  )
)

lazy val bot = (project in file("bot"))
  .settings(commonSettings: _*)
  .settings(
    name := "hopcone.bot",
    mainClass := Some("ru.hopcone.bot.Main"),
    libraryDependencies ++= Seq(
      //JSON
      "org.json4s" %% "json4s-jackson" % json4sVersion,
      "org.json4s" %% "json4s-ext" % json4sVersion,
    ),
    test in assembly := {}
  )
  .settings(managedSourceDirectories in Compile += sourceManaged.value / "ru" / "hopcone" / "bot" / "models")

lazy val slickCodeGen = taskKey[Seq[File]]("slick-codegen")

lazy val root = project
  .aggregate(bot)