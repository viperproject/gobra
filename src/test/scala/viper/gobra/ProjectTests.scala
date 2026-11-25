// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra

import java.nio.file.{Files, Path, Paths}

import org.rogach.scallop.throwError
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import viper.gobra.frontend.ScallopGobraConfig
import viper.gobra.reporting.NoopReporter
import viper.gobra.reporting.VerifierResult.{Aborted, Failure, Skipped, Success}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.silver.utility.TimingUtils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Verifies (a subset of the) packages of a real-world, already verified project that is no longer actively
  * maintained, such that changes to Gobra (e.g., optimizations or new well-formedness checks) are exercised against
  * a sizable code base. Every package is expected to verify without errors.
  *
  * Projects are expected to be configured via Gobra's JSON config files (i.e., a `gobra-mod.json` in the project's
  * root and optional `gobra.json` files next to packages). Each package is verified exactly as `gobra --config <pkg>`
  * would do; the only settings overridden are the Z3 executable and the reporter.
  *
  * If the project directory does not exist (e.g., because it was removed), all tests are
  * canceled instead of failing.
  */
abstract class ProjectTests extends AnyFunSuite with BeforeAndAfterAll with TimingUtils {

  /** Name of the project, used as a prefix for the test names. */
  def projectName: String

  /** Root directory of the project. */
  def projectRoot: Path

  /** Directories (relative to `projectRoot`) of the packages that should be verified. */
  def packages: Seq[String]

  val z3Exe: Option[String] = Option(System.getProperty("GOBRATESTS_Z3_EXE"))

  private var executor: GobraExecutionContext = _
  private var gobraInstance: Gobra = _

  override def beforeAll(): Unit = {
    executor = new DefaultGobraExecutionContext()
    gobraInstance = new Gobra()
  }

  override def afterAll(): Unit = {
    executor.terminateAndAssertInexistanceOfTimeout()
    gobraInstance = null
  }

  /** Parses a comma-separated list of packages from a system property, if set. */
  protected def packagesFromProperty(propertyName: String): Option[Seq[String]] =
    Option(System.getProperty(propertyName)).map(_.split(",").toSeq.map(_.trim).filter(_.nonEmpty))

  packages.foreach { pkg =>
    test(s"$projectName/$pkg") {
      val pkgDir = projectRoot.resolve(pkg)
      assume(Files.isDirectory(projectRoot), s"Project directory $projectRoot does not exist")
      assert(Files.isDirectory(pkgDir), s"Package directory $pkgDir does not exist")

      throwError.value = true
      val config = new ScallopGobraConfig(Seq("--config", pkgDir.toString)).config match {
        case Right(c) => c.copy(reporter = NoopReporter, z3Exe = z3Exe.orElse(c.z3Exe))
        case Left(errors) => fail(s"Invalid configuration for $pkgDir:\n${errors.map(_.formattedMessage).mkString("\n")}")
      }
      assert(config.packageInfoInputMap.nonEmpty, s"No input files found for $pkgDir")

      config.packageInfoInputMap.keys.toSeq.sortBy(_.id).foreach { pkgInfo =>
        val (result, elapsedMillis) = time(() =>
          Await.result(gobraInstance.verify(pkgInfo, config.copy(taskName = pkgInfo.id))(executor), Duration.Inf))
        info(s"${pkgInfo.id}: $elapsedMillis ms")
        result match {
          case Success =>
          case Failure(errors) =>
            fail(s"Verification of ${pkgInfo.id} failed with ${errors.size} error(s):\n${errors.map(_.formattedMessage).mkString("\n")}")
          case Aborted => fail(s"Verification of ${pkgInfo.id} has unexpectedly been aborted")
          case Skipped => fail(s"Verification of ${pkgInfo.id} has unexpectedly been skipped")
        }
      }
    }
  }
}

/**
  * Verifies a subset of the packages of VerifiedSCION (https://github.com/viperproject/VerifiedSCION), which is
  * vendored (as a subset) in `src/test/resources/projects/VerifiedSCION`. The set of packages can be overridden by setting the
  * system property `GOBRATESTS_SCION_PACKAGES` to a comma-separated list of package directories.
  */
class ScionTests extends ProjectTests {
  override def projectName: String = "VerifiedSCION"

  override def projectRoot: Path =
    Option(System.getProperty("GOBRATESTS_SCION_DIR")).map(Paths.get(_))
      .orElse(Option(getClass.getClassLoader.getResource("projects/VerifiedSCION")).map(viper.silver.utility.Paths.pathFromResource))
      .getOrElse(Paths.get("src/test/resources/projects/VerifiedSCION"))
      .toAbsolutePath.normalize()

  override def packages: Seq[String] = packagesFromProperty("GOBRATESTS_SCION_PACKAGES").getOrElse(Seq(
    "pkg/slayers/path",
    "pkg/slayers/path/empty",
    "pkg/slayers/path/onehop",
    "pkg/slayers/path/epic",
  ))
}
