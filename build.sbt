import sbt._
import com.scalapenos.sbt.prompt.SbtPrompt.autoImport._
import com.scalapenos.sbt.prompt._

val format = taskKey[Unit]("Format files using scalafm")

promptTheme := PromptTheme(
  List(
    text(_ => "[functional-design]", fg(64)).padRight(" λ ")
  )
)

lazy val functionalDesign = (project in file(".")).settings(
  name := "Functional Design",
  version := "0.1-SNAPSHOT",
  format := {
    Command.process("scalafmtAll", state.value)
    Command.process("scalafmtSbt", state.value)
  },
  scalaVersion := "2.13.5"
  // add other settings here
)

/* scala versions and options */
scalaVersion := "2.12.12"

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

// These options will be used for *all* versions.
scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-Xlint",
  "-Xverify",
  "-feature",
  "-Ypartial-unification"
  //,"-Xfatal-warnings" // Recommend enable before you commit code
  ,
  "-language:_"
  //,"-optimise"
)

javacOptions ++= Seq(
  "-Xlint:unchecked",
  "-Xlint:deprecation",
  "-source",
  "1.7",
  "-target",
  "1.7"
)

val CatsVersion = "2.3.0"
val CatsEffectVersion = "2.3.0"
val MonixVersion = "3.1.0"
val ZIOVersion = "1.0.2"
val ShapelessVersion = "2.3.3"
val FS2Version = "2.4.6"
val AmmoniteVersion = "2.0.0"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.1",
  // -- testing --
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test",
  "org.scalatest" %% "scalatest" % "3.2.3" % "test",
  // -- Logging --
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0",
  // Cats
  "org.typelevel" %% "cats-core" % CatsVersion,
  "org.typelevel" %% "cats-effect" % CatsEffectVersion,
  // fs2
  "co.fs2" %% "fs2-core" % FS2Version,
  // monix
  "io.monix" %% "monix" % MonixVersion,
  // shapeless
  "com.chuusai" %% "shapeless" % ShapelessVersion,
  // scalaz
  "dev.zio" %% "zio" % ZIOVersion,
  "dev.zio" %% "zio-streams" % ZIOVersion,
  // type classes
  "com.github.mpilquist" %% "simulacrum" % "0.12.0",
  // li haoyi ammonite repl embed
  "com.lihaoyi" % "ammonite" % AmmoniteVersion % "test" cross CrossVersion.full
)

resolvers ++= Seq(
  "Typesafe Snapshots" at "https://repo.typesafe.com/typesafe/snapshots/",
  "Secured Central Repository" at "https://repo1.maven.org/maven2",
  Resolver.sonatypeRepo("snapshots")
)

// ammonite repl
sourceGenerators in Test += Def.task {
  val file = (sourceManaged in Test).value / "amm.scala"
  IO.write(file, """object amm extends App { ammonite.Main().run() }""")
  Seq(file)
}.taskValue
