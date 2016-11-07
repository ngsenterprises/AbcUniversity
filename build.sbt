import sbt._
import sbt.Keys._

name := "AbcService"

version := "1.0.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= {
  val akkaV       = "2.4.11"
  val scalaTestV  = "3.0.0"

  Seq(
    "org.apache.commons" % "commons-io" % "1.3.2",
    "com.typesafe.akka" %% "akka-actor"                         % akkaV,
    "com.typesafe.akka" %% "akka-stream"                        % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental"             % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental"  % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit"                  % akkaV,
    "com.typesafe.akka" %% "akka-slf4j"                         % akkaV,
    "com.typesafe.akka" %% "akka-http-core"                     % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "org.scalatest" %% "scalatest" % scalaTestV % "test"

  )
}

lazy val myProject =
  Project(id = "AbcService", base = file("."))
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(
      scalaVersion := "2.11.8",
      libraryDependencies ++= {
        val akkaV       = "2.4.11"//"2.4.3"
        val scalaTestV  = "3.0.0"//"2.2.6"
        Seq(
          "org.scalatest"     %% "scalatest" % "3.0.0"
        )

      }
    )

