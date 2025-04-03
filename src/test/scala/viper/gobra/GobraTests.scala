// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.Path
import ch.qos.logback.classic.Level
import org.bitbucket.inkytonik.kiama.util.Source
import viper.gobra.frontend.Source.FromFileSource
import viper.gobra.frontend.{Config, PackageResolver, Source}
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.gobra.reporting.{GobraMessage, GobraReporter, VerifierError}
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils
import viper.gobra.util.Violation

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class GobraTests extends AbstractGobraTests /* with ParallelTestExecution */ {

  // Note that caching parse and type-check results is incompatible with ParallelTestExecution as each test case is run
  // in its own instance of this class.
  // Thus, we also cannot share a single Gobra instance or Gobra executor among all tests (by creating them in beforeAll
  // and cleaning them up in afterAll).

  val regressionsPropertyName = "GOBRATESTS_REGRESSIONS_DIR"

  val regressionsDir: String = System.getProperty(regressionsPropertyName, "regressions")
  val testDirectories: Seq[String] = Vector(regressionsDir)
  override val defaultTestPattern: String = PackageResolver.inputFilePattern


  protected def getConfig(source: Source): Config =
    Config(
      logLevel = Level.INFO,
      reporter = StringifyReporter,
      packageInfoInputMap = Map(Source.getPackageInfoOrCrash(source, Path.of("")) -> Vector(source)),
      checkConsistency = true,
      cacheParserAndTypeChecker = false,
      z3Exe = z3Exe,
      // termination checks in functions are currently disabled in the tests. This can be enabled in the future,
      // but requires some work to add termination measures all over the test suite.
      disableCheckTerminationPureFns = true,
    )
  
  val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {
        input match {
          case i: GobraAnnotatedTestInput => run(i)
          case _ => Violation.violation("unexpected test input type")
        }
      }

      def run(input: GobraAnnotatedTestInput): Seq[AbstractOutput] = {

        val source = FromFileSource(input.file)
        val config = getConfig(source)
        val pkgInfo = config.packageInfoInputMap.keys.head
        val executor = input.prerequisites._2
        val gobraInstance = input.prerequisites._1
        val (result, elapsedMilis) = time(() => Await.result(gobraInstance.verify(pkgInfo, config)(executor), Duration.Inf))

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

  case object StringifyReporter extends GobraReporter {
    override val name: String = "StringifyReporter"

    override def report(msg: GobraMessage): Unit = {
      // by invoking `toString`, we check that messages are printable, which includes pretty-printing AST nodes:
      msg.toString
    }
  }
}
