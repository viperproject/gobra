/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.backend

import viper.gobra.frontend.Config
import viper.gobra.reporting.{BackTranslator, BacktranslatingReporter, GeneratedViperMessage}
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.silver
import viper.silver.{ast => vpr}
import viper.silver.verifier.VerificationResult

import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.actor.ActorSystem

object BackendVerifier {

  case class Task(
                   program: vpr.Program,
                   backtrack: BackTranslator.BackTrackInfo
                 )

  sealed trait Result
  case object Success extends Result
  case class Failure(
                    errors: Vector[silver.verifier.VerificationError],
                    backtrack: BackTranslator.BackTrackInfo
                    ) extends Result

  def verify(task: Task)(config: Config)
            (implicit executionContext: ExecutionContextExecutor): Future[Result] = {
/*
    config.reporter report GeneratedViperMessage(config.inputFile, () => task.program)

    val verifier = config.backend.create
    verifier.start(BacktranslatingReporter(config.reporter, task.backtrack, config))
    val verificationResult = verifier.handle(task.program)
    verifier.stop()

    convertVerificationResult(verificationResult, task.backtrack)
*/

    config.reporter report GeneratedViperMessage(config.inputFile, () => task.program)

    val verifier = config.backend.create

    val programID = "_programID_" + config.inputFile.getName()

    val verificationResult = verifier.verify(programID, config.backendConfig, BacktranslatingReporter(config.reporter, task.backtrack, config), task.program)
    

    verificationResult.map(
      result => {
        convertVerificationResult(result, task.backtrack)
      })

  }

  /**
    * Takes a Viper VerificationResult and converts it to a Gobra Result using the provided backtracking information
    */
  def convertVerificationResult(result: VerificationResult, backTrackInfo: BackTrackInfo): Result = result match {
    case silver.verifier.Success => Success
    case failure: silver.verifier.Failure =>

      val (verificationError, otherError) = failure.errors
        .partition(_.isInstanceOf[silver.verifier.VerificationError])
        .asInstanceOf[(Seq[silver.verifier.VerificationError], Seq[silver.verifier.AbstractError])]

      checkAbstractViperErrors(otherError)

      Failure(verificationError.toVector, backTrackInfo)
  }

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  private def checkAbstractViperErrors(errors: Seq[silver.verifier.AbstractError]): Unit = {
    if (errors.nonEmpty) {
      var messages: Vector[String] = Vector.empty
      messages ++= Vector("Found non-verification-failures")
      messages ++= errors map (_.readableMessage)

      val completeMessage = messages.mkString("\n")
      throw new java.lang.IllegalStateException(completeMessage)
    }
  }

}
