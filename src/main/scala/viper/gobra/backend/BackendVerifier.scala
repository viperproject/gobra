/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.backend

import java.nio.charset.StandardCharsets.UTF_8

import org.apache.commons.io.FileUtils
import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator
import viper.gobra.util.OutputUtil
import viper.silver
import viper.silver.{ast => vpr}

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

  def verify(task: Task)(config: Config): Result = {


    // print generated viper file if set in config
    if (config.printVpr()) {
      println("Hallo aus der Nachbarschaft")
      val outputFile = OutputUtil.postfixFile(config.inputFile(), "vpr")
      FileUtils.writeStringToFile(
        outputFile,
        silver.ast.pretty.FastPrettyPrinter.pretty(task.program),
        UTF_8
      )
    }

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

        checkAbstractViperErrors(otherError)

        Failure(verificationError.toVector, task.backtrack)
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
