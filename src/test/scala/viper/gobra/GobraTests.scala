// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.Path
import ch.qos.logback.classic.Level
import org.bitbucket.inkytonik.kiama.util.Source
import org.scalatest.{Args, BeforeAndAfterAll, Status}
import scalaz.Scalaz.futureInstance
import viper.gobra.frontend.PackageResolver.RegularPackage
import viper.gobra.frontend.Source.FromFileSource
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Config, PackageResolver, Parser, Source}
import viper.gobra.reporting.VerifierResult.{Failure, Success}
import viper.gobra.reporting.{GobraMessage, GobraReporter, VerifierError}
import viper.gobra.tags.GobraTestsTag
import viper.silver.testing.{AbstractOutput, AnnotatedTestInput, ProjectInfo, SystemUnderTest}
import viper.silver.utility.TimingUtils
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}

import java.util.concurrent.atomic.AtomicReference
import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.Duration


// tag this class with `GobraTestsTag` such that we can run all except this test class
// (if necessary) by executing `sbt "testOnly * -- -l viper.gobra.GobraTestsTag"`
@GobraTestsTag
class GobraTests extends AbstractGobraTests with BeforeAndAfterAll {

  val regressionsPropertyName = "GOBRATESTS_REGRESSIONS_DIR"

  val regressionsDir: String = System.getProperty(regressionsPropertyName, "regressions")
  val testDirectories: Seq[String] = Vector(regressionsDir)
  override val defaultTestPattern: String = PackageResolver.inputFilePattern

  var gobraInstance: Gobra = _
  var executor: GobraExecutionContext = _
  var inputs: Vector[Source] = Vector.empty
  val shouldPreParseAndTypeCheck = true
  val preParseAndTypeCheckResult: AtomicReference[Option[Future[Vector[Either[Vector[VerifierError], TypeInfo]]]]] = new AtomicReference(None)

  override def beforeAll(): Unit = {
    executor = new DefaultGobraExecutionContext()
    gobraInstance = new Gobra()
  }

  override def registerTest(input: AnnotatedTestInput): Unit = {
    super.registerTest(input)
    val source = FromFileSource(input.file)
    inputs = inputs :+ source
  }

  protected def getConfig(source: Source): Config =
    Config(
      logLevel = Level.INFO,
      reporter = StringifyReporter,
      packageInfoInputMap = Map(Source.getPackageInfoOrCrash(source, Path.of("")) -> Vector(source)),
      checkConsistency = true,
      cacheParserAndTypeChecker = shouldPreParseAndTypeCheck,
      z3Exe = z3Exe,
      // termination checks in functions are currently disabled in the tests. This can be enabled in the future,
      // but requires some work to add termination measures all over the test suite.
      disableCheckTerminationPureFns = true,
    )

  // The testing framework calls first `runTests` once, followed by multiple calls to `runTest`.
  // Even if we skip `GobraTests` by instructing ScalaTest to ignore a certain tag, `runTests` is still called.
  // Thus, we start the pre-parsing and pre-typeChecking at the first invocation of `runTest`.
  override def runTest(testName: String, args: Args): Status = {
    if (shouldPreParseAndTypeCheck) {
      val result: Promise[Vector[Either[Vector[VerifierError], TypeInfo]]] = Promise()
      val prevValue = preParseAndTypeCheckResult.getAndUpdate {
        case None => Some(result.future)
        case v => v // leave unchanged
      }
      // We start the pre-parsing and pre-typechecking only if we just updated the atomic reference, i.e., the
      // previous value was `None`.
      val fut = prevValue match {
        case Some(f) => f // atomic reference was already set by another thread
        case None =>
          implicit val execContext: GobraExecutionContext = executor
          val futs = inputs.map(source => {
            val config = getConfig(source)
            val pkgInfo = config.packageInfoInputMap.keys.head
            val fut = for {
              parseResult <- Parser.parse(config, pkgInfo)
              pkg = RegularPackage(pkgInfo.id)
              typeCheckResult <- Info.check(config, pkg, parseResult)
            } yield typeCheckResult
            fut.toEither
          })
          Future.sequence(futs)
            .andThen(res => {
              result.complete(res) // set the result to the promise
            })
          result.future // return the promise's future
      }
      // block until the pre-parsing and pre-typechecking is completed:
      Await.result(fut, Duration.Inf)
    }

    super.runTest(testName, args)
  }

  override def afterAll(): Unit = {
    executor.terminateAndAssertInexistanceOfTimeout()
    gobraInstance = null
  }

  val gobraInstanceUnderTest: SystemUnderTest =
    new SystemUnderTest with TimingUtils {
      /** For filtering test annotations. Does not need to be unique. */
      override val projectInfo: ProjectInfo = new ProjectInfo(List("Gobra"))

      override def run(input: AnnotatedTestInput): Seq[AbstractOutput] = {

        val source = FromFileSource(input.file)
        val config = getConfig(source)
        val pkgInfo = config.packageInfoInputMap.keys.head
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
