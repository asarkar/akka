name := """user-service"""

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-feature", "-unchecked", "-deprecation", "-encoding", "utf8"
)

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.9",
  "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.9",
  "com.typesafe.akka" % "akka-http-core_2.11" % "2.4.9",
  "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.9",
  "org.mongodb" % "casbah-core_2.11" % "3.1.1",
  "com.typesafe.slick" % "slick_2.11" % "3.1.1",
  "ch.qos.logback" % "logback-core" % "1.1.7",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.9" % "test",
  "com.typesafe.akka" % "akka-http-testkit_2.11" % "2.4.9" % "test",
  "com.h2database" % "h2" % "1.4.192" % "test",
  "com.typesafe.slick" % "slick-hikaricp_2.11" % "3.1.1" % "test",
  "de.flapdoodle.embed" % "de.flapdoodle.embed.mongo" % "1.50.5" % "test"
)
