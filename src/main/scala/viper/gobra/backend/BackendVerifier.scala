/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.backend

import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator
import viper.gobra.util.Violation
import viper.silver
import viper.silver.verifier.errors
import viper.silver.{ast => vpr}

object BackendVerifier {

  case class Task(
                   program: vpr.Program,
                   backtrack: BackTranslator.BackTrackInfo
                 )

  sealed trait Result
  case object Success extends Result
  case class Failure(
                    errors: Seq[silver.verifier.VerificationError],
                    backtrack: BackTranslator.BackTrackInfo
                    ) extends Result

  def verify(task: Task)(config: Config): Result = {
    val verifier = setupSilicon(config)
    verifier.start()
    val verificationResult = verifier.handle(task.program)
    verifier.stop()

    verificationResult match {
      case silver.verifier.Success => Success
      case failure: silver.verifier.Failure =>

        val (verificationError, otherError) = failure.errors
          .partition(_.isInstanceOf[silver.verifier.VerificationError])
          .asInstanceOf[(Seq[silver.verifier.VerificationError], Seq[silver.verifier.AbstractError])]

        Violation.checkAbstractViperErrors(otherError)

        Failure(verificationError, task.backtrack)
    }
  }

  private def setupSilicon(config: Config): ViperVerifier = {
    var options: Vector[String] = Vector.empty
    options ++= Vector("--logLevel", "ERROR")
    options ++= Vector("--disableCatchingExceptions")
    options ++= Vector("--enableMoreCompleteExhale")

    new Silicon(options)
  }

  private def setupCarbon(config: Config): ViperVerifier = {
    var options: Vector[String] = Vector.empty
    options ++= Vector("--logLevel", "ERROR")

    new Carbon(options)
  }

}
