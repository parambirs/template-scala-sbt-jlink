enablePlugins(JlinkPlugin)

name := "template-scala-sbt-jlink"

version := "0.1"

scalaVersion := "3.10.0"

// explicitly specify the following dependency becuase jlink isn't able to identify it automatically
jlinkModules += "jdk.crypto.ec"

// required for Scala 3:
jlinkIgnoreMissingDependency := JlinkIgnore.only(
  "scala.quoted" -> "scala",
  "scala.quoted.runtime" -> "scala"
)
