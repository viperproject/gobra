// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.Path

import ch.qos.logback.classic.Level
import org.scalatest.BeforeAndAfterAll
import viper.gobra.frontend.{Config, PackageResolver}
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.gobra.reporting.{NoopReporter, VerifierError}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils

import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GobraTests extends AbstractGobraTests with BeforeAndAfterAll {

  val regressionsPropertyName = "GOBRATESTS_REGRESSIONS_DIR"

  val regressionsDir: String = System.getProperty(regressionsPropertyName, "regressions")
  val testDirectories: Seq[String] = Vector(regressionsDir)
  override val defaultTestPattern: String = s".*\\.${PackageResolver.extension}"

  var gobraInstance: Gobra = _

  override def beforeAll(): Unit = {
    gobraInstance = new Gobra()
  }

  override def afterAll(): Unit = {
    gobraInstance = null
  }

  val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {

        val config = Config(
          logLevel = Level.INFO,
          reporter = NoopReporter,
          inputFiles = Vector(input.file),
          // TODO: enable consistency checks as soon as inconsistencies have been fixed
          // checkConsistency = true,
          z3Exe = z3Exe
        )

        val executor: GobraExecutionContext = new DefaultGobraExecutionContext()
        val (result, elapsedMilis) = time(() => Await.result(gobraInstance.verify(config)(executor), Duration.Inf))

        info(s"Time required: $elapsedMilis ms")

        result match {
          case Success => Vector.empty
          case Failure(errors) => errors map GobraTestOuput
        }
      }
    }


  /**
    * The systems to test each input on.
    *
    * This method is not modeled as a constant field deliberately, such that
    * subclasses can instantiate a new [[viper.silver.testing.SystemUnderTest]]
    * for each test input.
    */
  override def systemsUnderTest: Seq[SystemUnderTest] = Vector(gobraInstanceUnderTest)

  case class GobraTestOuput(error: VerifierError) extends AbstractOutput {
    /** Whether the output belongs to the given line in the given file. */
    override def isSameLine(file: Path, lineNr: Int): Boolean = error.position.exists(_.line == lineNr)

    /** A short and unique identifier for this output. */
    override def fullId: String = error.id
  }
}
