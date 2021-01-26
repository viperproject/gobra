package viper.gobra

import java.nio.file.Path
import java.util.concurrent.TimeUnit

import org.scalatest.DoNotDiscover
import viper.gobra.ast.frontend.PPackage
import viper.gobra.ast.internal.Program
import viper.gobra.ast.internal.transform.OverflowChecksTransform
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, Desugar, PackageResolver, Parser}
import viper.gobra.reporting.{AppliedInternalTransformsMessage, BackTranslator, VerifierError, VerifierResult}
import viper.gobra.translator.Translator
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.silver.ast.{NoPosition, Position}
import viper.silver.frontend.Frontend
import viper.silver.testing.StatisticalTestSuite
import viper.silver.verifier.{NoVerifier, Verifier}
import viper.silver.{verifier => vpr}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Tool for benchmarking Gobra's performance (wrapped as a ScalaTest).
  * This tool is heavily inspired by PortableSiliconTests (credits go to Felix and Arshavir).
  * Example run command:
  * ```
  * Z3_EXE=z3.exe
  * sbt "test:runMain
  *   -DGOBRATESTS_TARGET=./target
  *   -DGOBRATESTS_WARMUP=./warmup
  *   -DGOBRATESTS_REPETITIONS=5
  *   -DGOBRATESTS_CSV=data.csv
  *   org.scalatest.tools.Runner
  *   -o -s
  *   viper.gobra.BenchmarkTests"
  * ```
  *
  * The command above will:
  * 1. Warm-up the JVM by verifying all .gobra files in ./warmup
  * 2. Measure time of 5 runs of each .gobra file in ./target
  * 3. Discard ("trim") the slowest and the fastest runs and compute
  *   - the mean
  *   - absolute and relative standard deviation
  *   - the best
  *   - the median
  *   - the worst
  *   run times of all these tests, and
  * 4. Print the timing info (per phase) into STDOUT, and write mean and standard deviation
  *    to file data.csv
  * 5. Create JAR files (e.g., target/scala-2.13/gobra_2.123-0.1.0-SNAPSHOT.jar,
  *                            target/scala-2.13/gobra_2.13-0.1.0-SNAPSHOT-tests.jar)
  *    that can be used to run tests with SBT without the need to distribute/ recompile
  *    the Gobra sources. To run the test without recompiling the sources, these
  *    JAR files should be put into a directory test-location/lib/
  *    where test-location/ is the directory where you invoke SBT via:
  *    ```
  *    sbt "set trapExit := false" \
  *    "test:runMain org.scalatest.tools.Runner -o -s viper.gobra.BenchmarkTests"
  *    ```
  *    Note that this command takes the same JVM property arguments as used above.
  *
  * The warmup and the target must be disjoint (not in a sub-directory relation).
  *
  * The following JVM properties are available:
  *   - GOBRATESTS_TARGET = path/to/target/files/    // Mandatory
  *   - GOBRATESTS_WARMUP = path/to/warmup/files/    // Optional. If not specified, skip JVM warmup phase.
  *   - GOBRATESTS_REPETITIONS = n // Optional, defaults to 1. If less than 4, no "trimming" will happen.
  *   - GOBRATESTS_CSV = path/to/file.csv // Optional. If provided, mean & stddev are written to CSV file.
  *   - GOBRATESTS_TIMEOUT = 180 // timeout in seconds, by default 180s
  */
@DoNotDiscover
class BenchmarkTests extends StatisticalTestSuite {
  /** Following a hyphenation-based naming scheme is important for handling project-specific annotations.
    * See comment for [[viper.silver.testing.TestAnnotations.projectNameMatches()]].
    */
  override def name = "Gobra-Benchmarks"

  override val repetitionsPropertyName = "GOBRATESTS_REPETITIONS"
  override val warmupLocationPropertyName = "GOBRATESTS_WARMUP"
  override val targetLocationPropertyName = "GOBRATESTS_TARGET"
  override val csvFilePropertyName = "GOBRATESTS_CSV"
  override val inclusionFilePropertyName = "GOBRATESTS_INCL_FILE"
  val timeoutPropertyName = "GOBRATESTS_TIMEOUT"

  val timeoutSec: Int = System.getProperty(timeoutPropertyName, "180").toInt /* timeout in seconds */

  override val defaultTestPattern: String = s".*\\.${PackageResolver.extension}"

  // the frontend will internally use Gobra as verifier
  val verifier: Verifier = new NoVerifier

  override def frontend(verifier: Verifier, files: Seq[Path]): GobraFrontendForTesting = {
    val fe = gobraFrontend
    fe.init(verifier)
    fe.reset(files)
    fe
  }

  // reuse the same frontend for all test files
  val gobraFrontend: GobraFrontendForTesting = {
    val fe = new GobraFrontendForTesting()
    fe
  }

  class GobraFrontendForTesting() extends Frontend {
    // we access Parser, TypeChecker, etc. directly and thus do not need an instance of Gobra
    private val executor: GobraExecutionContext = new DefaultGobraExecutionContext()
    private var config: Option[Config] = None

    /** Initialize this translator with a given verifier. Only meant to be called once. */
    override def init(verifier: Verifier): Unit = () // ignore verifier argument as we reuse the Gobra / Parser / TypeChecker / etc. instances for all tests

    override def reset(files: Seq[Path]): Unit =
      config = Some(Config(files.map(_.toFile).toVector))

    /**
      * Reset any messages recorded internally (errors from previous program translations, etc.)
      */
    override def resetMessages(): Unit =
      verifying.transitiveReset()

    private val parsing = InitialStep("parse", () => {
      assert(config.isDefined)
      val c = config.get
      Parser.parse(c.inputFiles.map(_.toPath))(c)
    })

    private val typeChecking: Continuation[PPackage, (PPackage, TypeInfo), Vector[VerifierError]] =
      Continuation("type-checking", parsing, (parsedPackage: PPackage) => {
        assert(config.isDefined)
        Info.check(parsedPackage)(config.get).map(typeInfo => (parsedPackage, typeInfo))
      })

    private val desugaring: Continuation[(PPackage, TypeInfo), Program, Vector[VerifierError]] =
      Continuation("desugaring", typeChecking, { case (parsedPackage: PPackage, typeInfo: TypeInfo) =>
        assert(config.isDefined)
        Right(Desugar.desugar(parsedPackage, typeInfo)(config.get))
      })

    private val internalTransforming = Continuation("internal transforming", desugaring, (program: Program) => {
      assert(config.isDefined)
      val c = config.get
      if (c.checkOverflows) {
        val result = OverflowChecksTransform.transform(program)
        c.reporter report AppliedInternalTransformsMessage(c.inputFiles.head, () => result)
        Right(result)
      } else {
        Right(program)
      }
    })

    private val encoding = Continuation("Viper encoding", internalTransforming, (program: Program) => {
      assert(config.isDefined)
      Right(Translator.translate(program)(config.get))
    })

    private val verifying = Continuation("Viper verification", encoding, (viperTask: BackendVerifier.Task) => {
      assert(config.isDefined)
      val c = config.get
      val resultFuture = BackendVerifier.verify(viperTask)(c)(executor)
        .map(BackTranslator.backTranslate(_)(c))(executor)
      Right(Await.result(resultFuture, Duration(timeoutSec, TimeUnit.SECONDS)))
    })

    private val lastStep = verifying

    /** Phases of the frontend which executes sequentially. */
    override val phases: Seq[Phase] = lastStep.transitivePhases

    /**
      * Represents a phase in Gobra producing a result of type Either[E, O].
      * As Phase is a case class defined in Frontend, this trait has to be defined here (instead of external to the Gobra frontent)
      */
    trait Step[O, E] {
      def res: Option[Either[E, O]]
      def phase: Phase
      def transitivePhases: Seq[Phase]
      def transitiveReset(): Unit
    }

    case class InitialStep[E, O](name: String, fn: () => Either[E, O]) extends Step[O, E] {
      var res: Option[Either[E, O]] = None
      override val phase: Phase = Phase(name, () => {
        res = Some(fn())
      })
      override def transitivePhases: Seq[Phase] = Seq(phase)
      override def transitiveReset(): Unit =
        res = None
    }

    case class Continuation[I, O, E](name: String, prevStep: Step[I, E], fn: I => Either[E, O]) extends Step[O, E] {
      var res: Option[Either[E, O]] = None
      override val phase: Phase = Phase(name, () => {
        assert(prevStep.res.isDefined)
        res = prevStep.res match {
          case Some(Right(output)) => Some(fn(output))
          case Some(Left(errs)) => Some(Left(errs)) // propagate errors
        }
      })
      override def transitivePhases: Seq[Phase] = prevStep.transitivePhases :+ phase
      override def transitiveReset(): Unit =
        prevStep.transitiveReset()
        res = None
    }

    /**
      * The result of the verification attempt (only available after parse, typecheck, translate and
      * verify have been called).
      */
    override def result: vpr.VerificationResult = lastStep.res match {
      case Some(Right(VerifierResult.Success)) => vpr.Success
      case Some(Right(VerifierResult.Failure(errors))) => vpr.Failure(errors.map(GobraTestError))
      case Some(Left(errors)) => vpr.Failure(errors.map(GobraTestError))
    }
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
