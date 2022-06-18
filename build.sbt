ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "Scala_2_Spark_2022",
    idePackagePrefix := Some("com.github.Eleonora8510")
  )
