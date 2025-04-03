// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

import scala.sys.process._
import scala.util.Try

// Import general settings from viperserver and, transitively, from carbon, silicon and silver.
// we assume that carbon/silver and silicon/silver point to the same version of the silver repo
lazy val server = project in file("viperserver")
lazy val silver_sif_extension = project in file("silver-sif-extension")

lazy val genParser = taskKey[Unit]("Generate Gobra's parser")
genParser := {
  val projectDir = baseDirectory.value
  val res: Int = (s"${projectDir.getAbsolutePath}/genparser.sh --download" !) // parentheses are not optional despite what IntelliJ suggests
  if (res != 0) {
    sys.error(s"genparser.sh exited with the non-zero exit code $res")
  }
}

// Gobra specific project settings
lazy val gobra = (project in file("."))
  .dependsOn(server % "compile->compile;test->test")
  .dependsOn(silver_sif_extension % "compile->compile;test->test")
  .settings(
    // General settings
    name := "Gobra",
    organization := "viper",
    version := "0.1.0-SNAPSHOT",
    homepage := Some(url("https://github.com/viperproject/gobra")),
    licenses := Seq("MPL-2.0 License" -> url("https://opensource.org/licenses/MPL-2.0")),

    // Compilation settings
    Compile / unmanagedResourceDirectories += baseDirectory.value / "conf",

    libraryDependencies +=
      ("org.bitbucket.inkytonik.kiama" %% "kiama" % "2.3.0") // Parsing
        .exclude("com.google.guava", "guava"),
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2", // Logging Frontend
    libraryDependencies += "org.fusesource.jansi" % "jansi" % "1.17.1", // For colouring Logback output
    // libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0", // cats
    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.9", // for SystemUtils
    libraryDependencies += "org.apache.commons" % "commons-text" % "1.9", // for escaping strings in parser preprocessor
    libraryDependencies += "commons-codec" % "commons-codec" % "1.15", // for obtaining the hex encoding of a string
    libraryDependencies += "org.antlr" % "antlr4-runtime" % "4.13.0",
    libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.3.7", // used for EitherT

    scalacOptions ++= Seq(
      "-encoding", "UTF-8", // Enforce UTF-8, instead of relying on properly set locales
      "-Ypatmat-exhaust-depth", "40"
    ),

    javacOptions := Seq("-encoding", "UTF-8"),

    // overwrite `compile` task to depend on the `genParser` task such that the parser is generated first:
    Compile / compile := (Compile / compile).dependsOn(genParser).value,

    // Run settings
    run / javaOptions ++= Seq(
      "-Xss128m",
      "-Dfile.encoding=UTF-8"
    ),

    fork := true,
    cancelable in Global := true,


    // Test settings
    // [2020-10-12 MS]
    //   When assembling a fat test JAR (test:assembly), the files under
    //   src/test don't end up in the JAR if the next line is missing.
    //   I'm not sure why that is, or why exactly the next line helps.
    //   To be investigated.
    // [2021-03-22 LA]
    //  If the following line is missing, gobra-test.jar will not even
    //  be created.
    inConfig(Test)(baseAssemblySettings),
    Test / javaOptions ++= (run / javaOptions).value,
    Test / assembly / assemblyJarName := "gobra-test.jar",
    Test / assembly / test := {},
    // exclude .gobra test files as we currently do not fully support files stored inside a JAR file:
    // [2021-03-22 LA]
    //  The following lines have been commented out as they seem to influence running the GobraTests
    //  with `sbt test`: the .gobra files do not get registered as test cases and thus not executed.
    // Test / assembly / unmanagedResources / excludeFilter := {
    //   val regressions = ((resourceDirectory in Test).value / "regressions").getCanonicalPath
    //   val same_package = ((resourceDirectory in Test).value / "same_package").getCanonicalPath
    //   new SimpleFileFilter(p =>
    //     (p.getCanonicalPath startsWith regressions) || (p.getCanonicalPath startsWith same_package)
    //   )
    // },
    Test / assembly / assemblyMergeStrategy := {
      case LogbackConfigurationFilePattern() => MergeStrategy.discard
      case n if n.startsWith("LICENSE.txt") => MergeStrategy.discard
      case x =>
        val fallbackStrategy = (assembly / assemblyMergeStrategy).value
        fallbackStrategy(x)
    },

    // Assembly settings
    assembly / assemblyJarName := "gobra.jar",
    assembly / mainClass := Some("viper.gobra.GobraRunner"),
    // assembly / javaOptions += "-Xss128m", // TODO: does apparently nothing


    assembly / assemblyMergeStrategy := {
      case LogbackConfigurationFilePattern() => MergeStrategy.discard
      case x =>
        val fallbackStrategy = (assembly / assemblyMergeStrategy).value
        fallbackStrategy(x)
    },
    assembly / test := {}
  )
  .enablePlugins(BuildInfoPlugin)
  .settings(
    buildInfoKeys := Seq[BuildInfoKey](
      "projectName" -> name.value,
      "projectVersion" -> version.value,
      scalaVersion,
      sbtVersion,
      BuildInfoKey.action("git") {
        val revision = Try(Process("git rev-parse HEAD").!!.trim).getOrElse("<revision>")
        val branch = Try(Process("git rev-parse --abbrev-ref HEAD").!!.trim).getOrElse("<branch>")
        Map("revision" -> revision, "branch" -> branch)
      }
    ),
    buildInfoPackage := "viper.gobra"
  )

lazy val LogbackConfigurationFilePattern = """logback.*?\.xml""".r
