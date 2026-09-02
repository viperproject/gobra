// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra

import ch.qos.logback.classic.Level
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.backend.{ViperBackend, ViperBackends, ViperVerifier}
import viper.gobra.frontend.Source.FromFileSource
import viper.gobra.frontend.{Config, PackageInfo, Source}
import viper.gobra.reporting.IntermediateVerifierResult.IntermediateVerifierResult
import viper.gobra.reporting.{IntermediateVerifierResult, NegativeVerifierResult, NoopReporter, VerifierResult}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
import viper.server.ViperConfig
import viper.server.core.ViperCoreServer
import viper.server.vsi.DefaultVerificationServerStart
import viper.silver.reporter.{Reporter, NoopReporter => SilverNoopReporter}
import viper.silver.verifier.VerificationResult
import viper.silver.{ast => vpr}

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}

/**
  * Tests that cancelling a verification via [[GoVerifier.verifyCancellable]] correctly aborts:
  * - before reaching the backend (Gobra aborts at the next stage boundary),
  * - while the backend is verifying and completes its result gracefully with an aborted result, and
  * - while the backend is verifying and fails its result future upon being interrupted (as
  *   ViperServer does when a running job is stopped and the message stream ends without an
  *   overall result).
  * The backend behaviors are modeled with a stub backend such that the tests are deterministic.
  * Additionally, ViperServer's handling of verifications that are aborted before job submission
  * is tested against an actual ViperServer instance.
  */
class AbortTests extends AnyFunSuite with BeforeAndAfterAll {
  val timeoutSec: Int = 60

  var executor: GobraExecutionContext = _
  var gobraInstance: Gobra = _

  override def beforeAll(): Unit = {
    executor = new DefaultGobraExecutionContext()
    gobraInstance = new Gobra()
  }

  override def afterAll(): Unit = {
    executor.terminateAndAssertInexistanceOfTimeout()
    gobraInstance = null
  }

  private val programUnderTest: String =
    """package main
      |
      |requires a >= 0 && b >= 0
      |ensures res == a + b
      |func add(a int, b int) (res int) {
      |  return a + b
      |}
      |""".stripMargin

  /** the program under test, written to a temporary file since sources must be backed by a file path */
  private lazy val sourceFile: Path = {
    val tmpDir = Files.createTempDirectory("gobra-abort-tests")
    tmpDir.toFile.deleteOnExit()
    val file = tmpDir.resolve("abort-test.gobra")
    Files.write(file, programUnderTest.getBytes(StandardCharsets.UTF_8))
    file.toFile.deleteOnExit()
    file
  }

  private def getConfig(backend: ViperBackend): Config = {
    val source = FromFileSource(sourceFile)
    Config(
      logLevel = Level.INFO,
      reporter = NoopReporter,
      packageInfoInputMap = Map(Source.getPackageInfoOrCrash(source, Path.of("")) -> Vector(source)),
      backend = Some(backend),
      z3Exe = Option(System.getProperty("GOBRATESTS_Z3_EXE")),
    )
  }

  type StubBackendResultFn = (Config, PackageInfo, String, Reporter, vpr.Program, GobraExecutionContext) => IntermediateVerifierResult[VerificationResult]

  /** stub backend with which tests can observe whether Gobra reaches the backend and control the backend's result */
  private class StubBackend(result: StubBackendResultFn) extends ViperBackend {
    override val value: String = "ABORTTESTSTUB"
    /** completed as soon as Gobra submits a verification to this backend */
    val backendReached: Promise[Unit] = Promise()

    override def create(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo)(implicit executor: GobraExecutionContext): ViperVerifier =
      new ViperVerifier {
        override def verify(id: String, reporter: Reporter, program: vpr.Program)(ctx: GobraExecutionContext): IntermediateVerifierResult[VerificationResult] = {
          backendReached.trySuccess(())
          result(config, pkgInfo, id, reporter, program, ctx)
        }
      }
  }

  private def awaitResult(fut: Future[VerifierResult]): VerifierResult =
    Await.result(fut, Duration(timeoutSec, TimeUnit.SECONDS))

  test("cancelling before the verification starts aborts at a stage boundary without reaching the backend") {
    implicit val ctx: GobraExecutionContext = executor
    val backend = new StubBackend((_, _, _, _, _, ctx) => {
      IntermediateVerifierResult(viper.silver.verifier.Success: VerificationResult)(ctx)
    })
    val config = getConfig(backend)
    val pkgInfo = config.packageInfoInputMap.keys.head

    config.abortSignal.abort()
    val handle = gobraInstance.verifyCancellable(pkgInfo, config)

    assert(awaitResult(handle.result) == VerifierResult.Aborted)
    assert(!backend.backendReached.isCompleted, "the backend must not be reached for an already aborted verification")
  }

  test("cancelling during a backend verification that completes its result gracefully reports an aborted result") {
    implicit val ctx: GobraExecutionContext = executor
    val backend = new StubBackend((config, _, _, _, _, ctx) => {
      implicit val ec: GobraExecutionContext = ctx
      // models a backend that supports interruption and completes with an aborted result:
      val promise = Promise[Either[NegativeVerifierResult, VerificationResult]]()
      config.abortSignal.onAbort(() => promise.trySuccess(Left(VerifierResult.Aborted)))
      EitherT.fromEither(promise.future)
    })
    val config = getConfig(backend)
    val pkgInfo = config.packageInfoInputMap.keys.head

    val handle = gobraInstance.verifyCancellable(pkgInfo, config)
    // cancel as soon as the backend verification is running:
    Await.result(backend.backendReached.future, Duration(timeoutSec, TimeUnit.SECONDS))
    handle.cancel()

    assert(awaitResult(handle.result) == VerifierResult.Aborted)
  }

  test("cancelling during a backend verification that fails its result future reports an aborted result") {
    implicit val ctx: GobraExecutionContext = executor
    val backend = new StubBackend((config, _, _, _, _, ctx) => {
      implicit val ec: GobraExecutionContext = ctx
      // models ViperServer's behavior when a running job is stopped: the message stream completes
      // without an overall result and the result future fails (see ViperServer.GlueActor):
      val promise = Promise[VerificationResult]()
      config.abortSignal.onAbort(() => promise.tryFailure(new RuntimeException("no overall success or failure message has been received")))
      EitherT.rightT(promise.future)
    })
    val config = getConfig(backend)
    val pkgInfo = config.packageInfoInputMap.keys.head

    val handle = gobraInstance.verifyCancellable(pkgInfo, config)
    // cancel as soon as the backend verification is running:
    Await.result(backend.backendReached.future, Duration(timeoutSec, TimeUnit.SECONDS))
    handle.cancel()

    assert(awaitResult(handle.result) == VerifierResult.Aborted)
  }

  test("ViperServer reports an aborted result for verifications that are aborted before job submission") {
    implicit val ctx: GobraExecutionContext = executor
    val server = new ViperCoreServer(new ViperConfig(List("--logLevel", "ERROR")))(executor) with DefaultVerificationServerStart
    try {
      val backend = ViperBackends.ViperServerWithSilicon(Some(server))
      val config = getConfig(backend)
      val pkgInfo = config.packageInfoInputMap.keys.head
      val verifier = backend.create(Vector.empty, config, pkgInfo)

      config.abortSignal.abort()
      val emptyProgram = vpr.Program(Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq.empty, Seq.empty)()
      val res = Await.result(verifier.verify("abort-test", SilverNoopReporter, emptyProgram)(executor).toEither, Duration(timeoutSec, TimeUnit.SECONDS))

      assert(res == Left(VerifierResult.Aborted))
    } finally {
      Await.ready(server.stop(), Duration(timeoutSec, TimeUnit.SECONDS))
    }
  }
}
