package viper.gobra.backend

import viper.silver.ast.Program
import viper.silver.verifier.VerificationResult
import viper.silver.reporter.Reporter
// import viper.silver.reporter.{Message, OverallFailureMessage, OverallSuccessMessage, Reporter}
// import viper.silver.verifier.{Success, VerificationResult}

import scala.concurrent.Future
// import akka.actor.{Actor, ActorSystem, Props}
// import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, Promise}

import viper.server.ViperCoreServer
import viper.server.ViperBackendConfig


// /* FIXME: Code for the master branch of viperserver */
//object ViperServer {
//  implicit val actor_system: ActorSystem = ActorSystem("Gobra_Actor_System")
//  implicit private val executionContext: ExecutionContextExecutor = ExecutionContext.global
//
//  case object Result
//
//  class GlueActor(reporter: Reporter) extends Actor {
//    val verificationPromise: Promise[VerificationResult] = Promise()
//
//    override def receive: Receive = {
//
//      case Result =>
//        sender ! verificationPromise
//
//      case msg: Message =>
//        reporter.report(msg)
//
//        msg match {
//          case msg: OverallFailureMessage => verificationPromise success msg.result
//          case _: OverallSuccessMessage   => verificationPromise success Success
//        }
//
//      case e: Throwable => verificationPromise failure e
//    }
//  }
//}

class ViperServer(server: ViperCoreServer) extends ViperVerifier {
  def verify(programID: String, config: ViperBackendConfig, reporter: Reporter, program: Program): Future[VerificationResult] = {

    val handler = server.verify(programID, config, reporter, program)
    server.getFuture(handler.id)

// /* FIXME: Code for the master branch of viperserver */
//    val handle = server.verify(programID, config, program)
//    val clientActor = actor_system.actorOf(Props(new GlueActor(reporter)))
//    server.streamMessages(handle.id, clientActor)
//
//    implicit val askTimeout: Timeout = Timeout(server.config.actorCommunicationTimeout() milliseconds)
//    val query = (clientActor ? Result).mapTo[Promise[VerificationResult]] // .asInstanceOf[Future[Promise[VerificationResult]]]
//    val res = query.flatMap(_.future)
  }

}