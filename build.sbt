
// Import general settings from Silver, Silicon and Carbon
lazy val silver = project in file("silver")
lazy val silicon = project in file("silicon")
lazy val carbon = project in file("carbon")

// Gobra specific project settings
lazy val server = (project in file("."))
  .dependsOn(silver % "compile->compile;test->test")
  .dependsOn(silicon % "compile->compile;test->test")
  .dependsOn(carbon % "compile->compile;test->test")
  .settings(
    // General settings
    name := "Gobra",
    organization := "viper",
    version := "0.1.0-SNAPSHOT",
    homepage := Some(url("https://bitbucket.org/mschwerhoff/voila")), // TODO: update link to correct repository
    licenses := Seq("MPL-2.0 License" -> url("https://opensource.org/licenses/MPL-2.0")),

    // Compilation settings
    silicon / excludeFilter := "logback.xml", /* Ignore Silicon's Logback configuration */
    carbon / excludeFilter := "logback.xml", /* Ignore Carbon's Logback configuration */
    Compile / unmanagedResourceDirectories += baseDirectory.value / "conf",

    libraryDependencies += "org.bitbucket.inkytonik.kiama" %% "kiama" % "2.2.0", // Parsing
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0", // Logging Frontend
    // libraryDependencies += "org.fusesource.jansi" % "jansi" % "1.17.1", // For colouring Logback output

    scalacOptions ++= Seq(
      "-Ypartial-unification",
      "-Ypatmat-exhaust-depth", "40"
    ),
    
    // Run settings
    run / javaOptions += "-Xss128m",

    fork := true,

    // Test settings
    Test / javaOptions ++= (run / javaOptions).value,

    // Assembly settings
    assembly / assemblyJarName := "gobra.jar",
    assembly / mainClass := Some("viper.gobra.GobraRunner"),


    assembly / assemblyMergeStrategy := {
      case LogbackConfigurationFilePattern() => MergeStrategy.discard
      case x =>
        val fallbackStrategy = (assembly / assemblyMergeStrategy).value
        fallbackStrategy(x)
    },
    assembly / test := {}
  )

val LogbackConfigurationFilePattern = """logback.*?\.xml""".r