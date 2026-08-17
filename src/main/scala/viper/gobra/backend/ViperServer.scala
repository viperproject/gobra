// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.silver.ast.Program
import viper.silver.reporter.{ExceptionReport, Message, OverallFailureMessage, OverallSuccessMessage, Reporter}
import viper.silver.verifier.{Success, VerificationResult}
import akka.actor.{Actor, Props, Status}
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.reporting.{IntermediateVerifierResult, VerifierResult}
import viper.gobra.reporting.IntermediateVerifierResult.IntermediateVerifierResult

import scala.concurrent.{Await, Future, Promise}
import viper.gobra.util.{AbortSignal, GobraExecutionContext}
import viper.server.core.{CarbonConfig, SiliconConfig, VerificationExecutionContext, ViperBackendConfig, ViperCoreServer, ViperServerBackendNotFoundException}
import viper.server.frontends.lsp.ViperServerService

import scala.concurrent.duration.Duration

object ViperServer {

  class GlueActor(reporter: Reporter, verificationPromise: Promise[VerificationResult]) extends Actor {
    override def receive: Receive = {

      case msg: Message =>
        try {
          reporter.report(msg)

          msg match {
            case msg: OverallFailureMessage => verificationPromise trySuccess msg.result
            case _: OverallSuccessMessage => verificationPromise trySuccess Success
            case ExceptionReport(e)         => verificationPromise tryFailure e
            case _ =>
          }
        } catch {
          case e: Throwable => verificationPromise tryFailure e
        }

      case Status.Success =>
        // this message is the last one to be received meaning that all messages belonging to the current verification
        // should be received by now. To make sure that the promise is eventually completed (even if no
        // OverallSuccessMessage or OverallFailureMessage was received), we let the promise fail. This failure is
        // ignored if the promise has already been completed before:
        verificationPromise tryFailure new RuntimeException("no overall success or failure message has been received")
      case Status.Failure(cause) => verificationPromise tryFailure cause

      case e: Throwable => verificationPromise tryFailure e
    }
  }
}

trait ViperVerifierConfig {
  val partialCommandLine: List[String]
}
trait ViperServerWithSilicon extends ViperVerifierConfig
trait ViperServerWithCarbon extends ViperVerifierConfig

object ViperServerConfig {
  object EmptyConfigWithSilicon extends ViperServerWithSilicon {val partialCommandLine: List[String] = Nil}
  object EmptyConfigWithCarbon extends ViperServerWithCarbon {val partialCommandLine: List[String] = Nil}
  case class ConfigWithSilicon(partialCommandLine: List[String]) extends ViperServerWithSilicon
  case class ConfigWithCarbon(partialCommandLine: List[String]) extends ViperServerWithCarbon
}

class ViperServer(server: ViperCoreServer, backendConfig: ViperVerifierConfig, abortSignal: AbortSignal)(implicit executor: VerificationExecutionContext) extends ViperVerifier {
  import ViperServer._

  override def verify(programID: String, reporter: Reporter, program: Program)(_ctx: GobraExecutionContext): IntermediateVerifierResult[VerificationResult] = {
    // convert ViperVerifierConfig to ViperBackendConfig:

    if(!server.isRunning) {
      Await.ready(server.start(), Duration.Inf)
    }

    val serverConfig: ViperBackendConfig = backendConfig match {
      case _: ViperServerWithSilicon => SiliconConfig(backendConfig.partialCommandLine)
      case _: ViperServerWithCarbon => CarbonConfig(backendConfig.partialCommandLine)
      case c => throw ViperServerBackendNotFoundException(s"unknown backend config $c")
    }
    if (abortSignal.isAborted) {
      // the verification has been aborted before this backend verification was submitted:
      return IntermediateVerifierResult(VerifierResult.Aborted)
    }
    val handle = server.verify(programID, serverConfig, program)
    if (handle.id < 0) {
      // the server rejected the job, e.g. because the maximum number of concurrently active jobs
      // has been reached. Fail fast: there is no job whose messages could be streamed, i.e. the
      // returned future would otherwise never complete:
      return EitherT.rightT(Future.failed[VerificationResult](new RuntimeException(
        s"ViperServer rejected the verification job for $programID, e.g. because the maximum number of active jobs has been reached")))
    }
    // we have to create our own GlueActor and replicate parts of `ViperCoreServerUtils.getResultsFuture(...)` because
    // we do not only need to return a future but also forward all messages to the reporter
    val promise: Promise[VerificationResult] = Promise()
    val clientActor = executor.actorSystem.actorOf(Props(new GlueActor(reporter, promise)))
    // We do not perform an AST job before verification. Thus, we pass "false" as the value of include_ast.
    val streamed = server.streamMessages(handle, clientActor, include_ast = false)
    if (streamed.isEmpty) {
      // the job is unknown to the server, i.e. it completed and was cleaned up between `verify`
      // and `streamMessages`. Without an attached stream, `promise` would never complete:
      promise.tryFailure(new RuntimeException(
        s"the verification job for $programID completed before its messages could be streamed"))
    }
    // interrupt this backend verification as soon as the verification is aborted. The listener is
    // registered only after the message stream has been attached. Note that the listener is invoked
    // immediately in case the verification was aborted since the check above:
    abortSignal.onAbort(() => server match {
      case service: ViperServerService => service.stopVerification(handle)
      case _ =>
        // the underlying server does not expose an API to interrupt single jobs -- the job keeps
        // running but its result is suppressed by the caller
    })
    EitherT.rightT(promise.future)
  }
}
