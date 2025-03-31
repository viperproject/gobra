// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.io.File
import java.nio.file.Path
import ch.qos.logback.classic.Level
import org.bitbucket.inkytonik.kiama.util.Source
import org.rogach.scallop.exceptions.ValidationFailure
import org.rogach.scallop.throwError
import viper.gobra.frontend.Source.FromFileSource
import viper.gobra.frontend.{Config, PackageInfo, ScallopGobraConfig, Source}
import viper.gobra.reporting.{NoopReporter, ParserError}
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.gobra.util.DefaultGobraExecutionContext
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, DefaultAnnotatedTestInput, DefaultTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GobraPackageTests extends GobraTests {
  val samePackagePropertyName    = "GOBRATESTS_SAME_PACKAGE_DIR"
  val builtinPackagePropertyName = "GOBRATESTS_BUILTIN_PACKAGES_DIR"
  val stubPackagesPropertyName   = "GOBRATESTS_STUB_PACKAGES_DIR"

  val samePackageDir: String  = System.getProperty(samePackagePropertyName, "same_package")
  val builtinStubDir: String  = System.getProperty(builtinPackagePropertyName, "builtin")
  val stubPackagesDir: String = System.getProperty(stubPackagesPropertyName, "stubs")

  override val testDirectories: Seq[String] = Vector(samePackageDir, builtinStubDir, stubPackagesDir)

  override def buildTestInput(file: Path, prefix: String): DefaultAnnotatedTestInput = {
    // get package clause of file and collect all other files belonging to this package:
    val input = for {
      pkgName <- getPackageClause(file.toFile)
      currentDir = file.getParent.toFile
      samePkgFiles = currentDir.listFiles
        .filter(_.isFile)
        .map(f => (f, getPackageClause(f)))
        .filter { case (_, Some(clause)) if clause == pkgName => true }
        .map { case (f, _) => f.toPath }
        .sortBy(_.toString)
        .toSeq
    } yield DefaultTestInput(s"$prefix/$pkgName (${file.getFileName.toString})", prefix, samePkgFiles, Seq())
    GobraAnnotatedTestInput(input.get)
  }

  override val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {
        // test scallop parsing by giving package name and testing whether the same set of files is created
        val currentDir = input.file.getParent
        val parsedConfig = for {
          config <- createConfig(Array(
            "--logLevel", "INFO",
            "--directory", currentDir.toFile.getPath,
            "--projectRoot", currentDir.toFile.getPath, // otherwise, it assumes Gobra's root directory as the project root
            "-I", currentDir.toFile.getPath,
            // termination checks in functions are currently disabled in the tests. This can be enabled in the future,
            // but requires some work to add termination measures all over the test suite.
            "--disablePureFunctsTerminationRequirement",
          ))
        } yield config

        val pkgInfo = Source.getPackageInfoOrCrash(FromFileSource(input.files.head), currentDir)

        val config = Config(
          logLevel = Level.INFO,
          reporter = NoopReporter,
          packageInfoInputMap = Map(pkgInfo -> input.files.toVector.map(FromFileSource(_))),
          includeDirs = Vector(currentDir),
          checkConsistency = true,
          z3Exe = z3Exe
        )

        val executor = new DefaultGobraExecutionContext()
        val gobraInstance = new Gobra()
        val (result, elapsedMilis) = time(() => Await.result(gobraInstance.verify(pkgInfo, config)(executor), Duration.Inf))

        info(s"Time required: $elapsedMilis ms")

        val res = equalConfigs(parsedConfig.get, config) ++ (result match {
          case Success => Vector.empty
          case Failure(errors) => errors map GobraTestOuput
        })

        executor.terminateAndAssertInexistanceOfTimeout()

        res
      }
    }

  private lazy val pkgClauseRegex = """(?:\/\/.*|\/\*(?:.|\n)*\*\/|package(?:\s|\n)+([a-zA-Z_][a-zA-Z0-9_]*))""".r

  private def getPackageClause(file: File): Option[String] = {
    val bufferedSource = scala.io.Source.fromFile(file)
    val content = bufferedSource.mkString
    bufferedSource.close()
    // TODO is there a way to perform the regex lazily on the file's content?
    pkgClauseRegex
      .findAllMatchIn(content)
      .collectFirst { case m if m.group(1) != null => m.group(1) }
  }

  private def createConfig(args: Array[String]): Option[Config] = {
    try {
      // set throwError to true: Scallop will throw an exception instead of terminating the program in case an
      // exception occurs (e.g. a validation failure)
      throwError.value = true

      new ScallopGobraConfig(args.toSeq).config.toOption
    } catch {
      case _: ValidationFailure => None
      case other: Throwable => throw other
    }
  }

  private def equalConfigs(config1: Config, config2: Config): Vector[GobraTestOuput] = {
    def equalFiles(v1: Vector[Path], v2: Vector[Path]): Boolean = v1.sortBy(_.toString).equals(v2.sortBy(_.toString))

    /**
     * Checks that two given maps contain the same PPackageInfo source mapping
     */
    def equalPkgInfoSourceMaps(v1: Map[PackageInfo, Vector[Source]], v2: Map[PackageInfo, Vector[Source]]): Boolean = {
      val keys = (v1.keys ++ v2.keys).toSet
      keys.forall(pkgInfo =>
        v1.contains(pkgInfo) && v2.contains(pkgInfo) && v1(pkgInfo).map(_.name).sorted.equals(v2(pkgInfo).map(_.name).sorted)
      )
    }

    val equal = (equalPkgInfoSourceMaps(config1.packageInfoInputMap, config2.packageInfoInputMap)
      && equalFiles(config1.includeDirs, config2.includeDirs)
      && config1.logLevel == config2.logLevel)
    if (equal) Vector()
    else Vector(GobraTestOuput(ParserError("unexpected results for conversion of package clause to input files", None)))
  }
}
