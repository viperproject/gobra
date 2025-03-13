// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.{Files, Path}
import ch.qos.logback.classic.Level
import org.bitbucket.inkytonik.kiama.util.Source
import org.rogach.scallop.exceptions.ValidationFailure
import org.rogach.scallop.throwError
import viper.gobra.frontend.Source.FromFileSource
import viper.gobra.frontend.{Config, PackageInfo, ScallopGobraConfig, Source}
import viper.gobra.reporting.{NoopReporter, ParserError}
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, DefaultAnnotatedTestInput, DefaultTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters.IterableHasAsScala

class GobraPackageTests extends GobraTests {
  val samePackagePropertyName    = "GOBRATESTS_SAME_PACKAGE_DIR"
  val builtinPackagePropertyName = "GOBRATESTS_BUILTIN_PACKAGES_DIR"
  val stubPackagesPropertyName   = "GOBRATESTS_STUB_PACKAGES_DIR"

  val samePackageDir: String  = System.getProperty(samePackagePropertyName, "same_package")
  val builtinStubDir: String  = System.getProperty(builtinPackagePropertyName, "builtin")
  val stubPackagesDir: String = System.getProperty(stubPackagesPropertyName, "stubs")

  override val testDirectories: Seq[String] = Vector(samePackageDir, builtinStubDir, stubPackagesDir)

  override def buildTestInput(path: Path, prefix: String): DefaultAnnotatedTestInput = {
    def listDirectory(path: Path): Vector[Path] = Files.newDirectoryStream(path).asScala.toVector
    def isFile(path: Path): Boolean = Files.isRegularFile(path)

    // get package clause of file and collect all other files belonging to this package:
    val input = for {
      pkgName <- getPackageClause(path)
      currentDir = path.getParent
      samePkgFiles = listDirectory(currentDir)
        .filter(isFile)
        .map(p => (p, getPackageClause(p)))
        .filter { case (_, Some(clause)) if clause == pkgName => true }
        .map { case (p, _) => p }
        .sortBy(_.toString)
    } yield DefaultTestInput(s"$prefix/$pkgName (${path.getFileName.toString})", prefix, samePkgFiles, Seq())
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
            "--directory", currentDir.toString,
            "--projectRoot", currentDir.toString, // otherwise, it assumes Gobra's root directory as the project root
            "-I", currentDir.toString,
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

        val (result, elapsedMilis) = time(() => Await.result(gobraInstance.verify(pkgInfo, config)(executor), Duration.Inf))

        info(s"Time required: $elapsedMilis ms")

        equalConfigs(parsedConfig.get, config) ++ (result match {
          case Success => Vector.empty
          case Failure(errors) => errors map GobraTestOuput
        })
      }
    }

  private lazy val pkgClauseRegex = """(?:\/\/.*|\/\*(?:.|\n)*\*\/|package(?:\s|\n)+([a-zA-Z_][a-zA-Z0-9_]*))""".r

  private def getPackageClause(path: Path): Option[String] = {
    val input = Files.newInputStream(path)
    val bufferedSource = scala.io.Source.fromInputStream(input)
    val content = bufferedSource.mkString
    bufferedSource.close()
    input.close()
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
