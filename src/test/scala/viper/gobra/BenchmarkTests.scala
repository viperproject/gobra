// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import org.rogach.scallop.throwError

import java.nio.file.Path
import org.scalatest.{ConfigMap, DoNotDiscover}
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.frontend.{Config, PackageResolver, ScallopGobraConfig}
import viper.gobra.reporting.{NoopReporter, VerifierError, VerifierResult}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext, Violation}
import viper.silver.ast.{NoPosition, Position}
import viper.silver.frontend.Frontend
import viper.silver.testing.StatisticalTestSuite
import viper.silver.verifier.{NoVerifier, Verifier}
import viper.silver.{verifier => vpr}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

@DoNotDiscover
trait BenchmarkTests extends StatisticalTestSuite {

  override val repetitionsPropertyName = "GOBRATESTS_REPETITIONS"
  override val warmupLocationPropertyName = "GOBRATESTS_WARMUP"
  override val targetLocationPropertyName = "GOBRATESTS_TARGET"
  override val csvFilePropertyName = "GOBRATESTS_CSV"
  override val inclusionFilePropertyName = "GOBRATESTS_INCL_FILE"
  val timeoutPropertyName = "GOBRATESTS_TIMEOUT"

  val timeoutSec: Int = System.getProperty(timeoutPropertyName, "600").toInt /* timeout in seconds */

  override val defaultTestPattern: String = PackageResolver.inputFilePattern

  // the frontend will internally use Gobra as verifier
  val verifier: Verifier = new NoVerifier

  // reuse the same frontend for all test files thus it is a val
  val gobraFrontend: GobraFrontendForTesting

  override def frontend(verifier: Verifier, files: Seq[Path]): GobraFrontendForTesting = {
    val fe = gobraFrontend
    fe.init(verifier)
    fe.reset(files)
    fe
  }

  override def afterAll(configMap: ConfigMap): Unit = {
    super.afterAll(configMap)
    gobraFrontend.terminate()
  }

  trait GobraFrontendForTesting extends Frontend {
    val z3PropertyName = "GOBRATESTS_Z3_EXE"
    val z3Exe: Option[String] = Option(System.getProperty(z3PropertyName))

    protected implicit val executor: GobraExecutionContext = new DefaultGobraExecutionContext()
    protected var config: Option[Config] = None

    /** Initialize this translator with a given verifier. Only meant to be called once. */
    override def init(verifier: Verifier): Unit = () // ignore verifier argument as we reuse the Gobra / Parser / TypeChecker / etc. instances for all tests

    override def reset(files: Seq[Path]): Unit =
      createConfig(Array("-i", files.toVector.mkString(" ")))


    private def createConfig(args: Array[String]): Config = {
      // set throwError to true: Scallop will throw an exception instead of terminating the program in case an
      // exception occurs (e.g. a validation failure)
      throwError.value = true
      // Simulate pick of package, Gobra normally does
      val config = new ScallopGobraConfig(args.toSeq).config
      Violation.violation(config.isRight, "creating the config has failed")
      config.toOption.get.copy(reporter = NoopReporter, z3Exe = z3Exe)
    }

    def gobraResult: VerifierResult

    /**
      * The result of the verification attempt (only available after parse, typecheck, translate and
      * verify have been called).
      */
    override def result: vpr.VerificationResult = {
      // transform Gobra errors back to vpr.AbstractError such that the benchmarking framework automatically handles them
      gobraResult match {
        case VerifierResult.Success => vpr.Success
        case VerifierResult.Failure(errors) => vpr.Failure(errors.map(GobraTestError))
      }
    }

    def terminate(): Unit = {
      executor.terminateAndAssertInexistanceOfTimeout()
    }

    /**
      * Represents a phase in Gobra producing a result of type Either[E, O].
      * As Phase is a case class defined in Frontend, this trait has to be defined here (instead of external to the Gobra frontend)
      */
    trait Step[O, E] {
      def res: Option[Either[E, O]]
      def phase: Phase
      def phases: Seq[Phase]
      def reset(): Unit
    }

    case class InitialStep[E, O](name: String, fn: () => Either[E, O]) extends Step[O, E] {
      var res: Option[Either[E, O]] = None
      override val phase: Phase = Phase(name, () => {
        res = Some(fn())
      })
      override def phases: Seq[Phase] = Seq(phase)
      override def reset(): Unit =
        res = None
    }

    case class InitialStepEitherT[E, O](name: String, fn: () => EitherT[E, Future, O]) extends Step[O, E] {
      var res: Option[Either[E, O]] = None
      override val phase: Phase = Phase(name, () => {
        res = Some(Await.result(fn().toEither, Duration(timeoutSec, TimeUnit.SECONDS)))
      })
      override def phases: Seq[Phase] = Seq(phase)
      override def reset(): Unit =
        res = None
    }

    case class NextStep[I, E, O](name: String, prevStep: Step[I, E], fn: I => Either[E, O]) extends Step[O, E] {
      var res: Option[Either[E, O]] = None
      override val phase: Phase = Phase(name, () => {
        assert(prevStep.res.isDefined)
        res = prevStep.res match {
          case Some(Right(output)) => Some(fn(output))
          case Some(Left(errs)) => Some(Left(errs)) // propagate errors
        }
      })
      override def phases: Seq[Phase] = prevStep.phases :+ phase
      override def reset(): Unit =
        prevStep.reset()
      res = None
    }

    case class NextStepEitherT[I, E, O](name: String, prevStep: Step[I, E], fn: I => EitherT[E, Future, O]) extends Step[O, E] {
      var res: Option[Either[E, O]] = None
      override val phase: Phase = Phase(name, () => {
        assert(prevStep.res.isDefined)
        res = prevStep.res match {
          case Some(Right(output)) => Some(Await.result(fn(output).toEither, Duration(timeoutSec, TimeUnit.SECONDS)))
          case Some(Left(errs)) => Some(Left(errs)) // propagate errors
        }
      })
      override def phases: Seq[Phase] = prevStep.phases :+ phase
      override def reset(): Unit =
        prevStep.reset()
      res = None
    }

    case class GobraTestError(error: VerifierError) extends vpr.AbstractError {
      /** The position where the error occured. */
      override def pos: Position = error.position.getOrElse(NoPosition)

      /** A short and unique identifier for this error. */
      override def fullId: String = error.id

      /** A readable message describing the error. */
      override def readableMessage: String = error.formattedMessage
    }
  }
}
