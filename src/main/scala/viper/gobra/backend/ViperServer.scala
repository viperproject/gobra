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

import scala.concurrent.{Await, Future, Promise}
import viper.gobra.util.GobraExecutionContext
import viper.server.core.{CarbonConfig, SiliconConfig, VerificationExecutionContext, ViperBackendConfig, ViperCoreServer, ViperServerBackendNotFoundException}

import scala.concurrent.duration.Duration

object ViperServer {

  case object Result

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

object ViperServerConfig {
  object EmptyConfigWithSilicon extends ViperServerWithSilicon {val partialCommandLine: List[String] = Nil}
  object EmptyConfigWithCarbon extends ViperServerWithCarbon {val partialCommandLine: List[String] = Nil}
  case class ConfigWithSilicon(partialCommandLine: List[String]) extends ViperServerWithSilicon
  case class ConfigWithCarbon(partialCommandLine: List[String]) extends ViperServerWithCarbon
}
trait ViperServerWithSilicon extends ViperVerifierConfig
trait ViperServerWithCarbon extends ViperVerifierConfig

class ViperServer(server: ViperCoreServer, backendConfig: ViperVerifierConfig)(implicit executor: VerificationExecutionContext) extends ViperVerifier {
  import ViperServer._

  override def verify(programID: String, config: ViperVerifierConfig, reporter: Reporter, program: Program)(_ctx: GobraExecutionContext): Future[VerificationResult] = {
    // convert ViperVerifierConfig to ViperBackendConfig:

    if(!server.isRunning) {
      Await.ready(server.start(), Duration.Inf)
    }

    val serverConfig: ViperBackendConfig = backendConfig match {
      case _: ViperServerWithSilicon => SiliconConfig(backendConfig.partialCommandLine)
      case _: ViperServerWithCarbon => CarbonConfig(backendConfig.partialCommandLine)
      case c => throw ViperServerBackendNotFoundException(s"unknown backend config $c")
    }
    val handle = server.verify(programID, serverConfig, program)
    // we have to create our own GlueActor and replicate parts of `ViperCoreServerUtils.getResultsFuture(...)` because
    // we do not only need to return a future but also forward all messages to the reporter
    val promise: Promise[VerificationResult] = Promise()
    val clientActor = executor.actorSystem.actorOf(Props(new GlueActor(reporter, promise)))
    server.streamMessages(handle, clientActor)
    promise.future
  }
}