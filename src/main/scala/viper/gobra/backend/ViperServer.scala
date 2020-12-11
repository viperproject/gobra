// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.silver.ast.Program
import viper.silver.reporter.{Message, OverallFailureMessage, OverallSuccessMessage, Reporter}
import viper.silver.verifier.{Success, VerificationResult}
import akka.actor.{Actor, Props}

import scala.concurrent.{Future, Promise}
import viper.gobra.util.GobraExecutionContext

import viper.server.core.{ViperBackendConfig, ViperCoreServer}


object ViperServer {

  case object Result

  class GlueActor(reporter: Reporter, verificationPromise: Promise[VerificationResult]) extends Actor {
    override def receive: Receive = {

      case msg: Message =>
        try {
          reporter.report(msg)

          msg match {
            case msg: OverallFailureMessage => verificationPromise trySuccess msg.result
            case _: OverallSuccessMessage   => verificationPromise trySuccess Success
            case _ =>
          }
        } catch {
          case e: Throwable => verificationPromise tryFailure e
        }

      case e: Throwable => verificationPromise tryFailure e
    }
  }
}

class ViperServer(server: ViperCoreServer) extends ViperVerifier {

  import ViperServer._

  def verify(programID: String, config: ViperBackendConfig, reporter: Reporter, program: Program)(executor: GobraExecutionContext): Future[VerificationResult] = {
    // directly declaring the parameter implicit somehow does not work as the compiler is unable to spot the inheritance
    implicit val _executor: GobraExecutionContext = executor

    val handle = server.verify(programID, config, program)
    val promise: Promise[VerificationResult] = Promise()
    val clientActor = executor.actorSystem.actorOf(Props(new GlueActor(reporter, promise)))
    server.streamMessages(handle, clientActor)
    promise.future
  }

}