name := "BotSpot"

version := "1.0"

scalaVersion := "2.11.7"

mainClass := Some("BotSpot")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.12" % "test",
  "org.json4s" %% "json4s-jackson" % "3.2.11",
  "com.typesafe" % "config" % "1.3.0",
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.scalaj" %% "scalaj-http" % "1.1.5",
  "com.typesafe.akka" %% "akka-actor" % "2.3.12")





