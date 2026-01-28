// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.backend.ViperBackends.{CarbonBackend => Carbon}
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.gobra.reporting.{BackTranslator, BacktranslatingReporter, ChoppedProgressMessage}
import viper.gobra.util.{ChopperUtil, GobraExecutionContext}
import viper.silver
import viper.silver.verifier.VerificationResult
import viper.silver.{ast => vpr}

import scala.concurrent.Future

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

  def verify(task: Task, pkgInfo: PackageInfo)(config: Config)(implicit executor: GobraExecutionContext): Future[Result] = {

    var exePaths: Vector[String] = Vector.empty

    config.z3Exe match {
      case Some(z3Exe) =>
        exePaths ++= Vector("--z3Exe", z3Exe)
      case _ =>
    }

    (config.backendOrDefault, config.boogieExe) match {
      case (Carbon, Some(boogieExe)) =>
        exePaths ++= Vector("--boogieExe", boogieExe)
      case _ =>
    }

    val verificationResults: Future[VerificationResult] =  {
      val verifier = config.backendOrDefault.create(exePaths, config, pkgInfo)
      val reporter = BacktranslatingReporter(config.reporter, task.backtrack, config)

      if (!config.shouldChop) {
        verifier.verify(config.taskName, reporter, task.program)(executor)
      } else {

        val programs = ChopperUtil.computeChoppedPrograms(task, pkgInfo)(config)
        val num = programs.size
        var counter = 0 // verification progress counter

        // Starts verifying all chopped programs in parallel
        val partialVerificationResults = Future.traverse(programs.zipWithIndex) { case (program, idx) =>
          val programID = s"${config.taskName}_$idx"
          verifier.verify(programID, reporter, program)(executor).andThen { _ =>
            // this block ensures that progress messages are printed in order
            this.synchronized { counter += 1; config.reporter report ChoppedProgressMessage(counter, num, idx) }
          }
        }

        partialVerificationResults map { partialRes =>
          partialRes.foldLeft[VerificationResult](silver.verifier.Success) {
            case (acc, silver.verifier.Success) => acc
            case (silver.verifier.Success, res) => res
            case (silver.verifier.Failure(l), silver.verifier.Failure(r)) => silver.verifier.Failure(l ++ r)
          }
        }
      }
    }

    verificationResults.map(convertVerificationResult(_, task.backtrack))
  }

  /**
    * Takes a Viper VerificationResult and converts it to a Gobra Result using the provided backtracking information
    */
  def convertVerificationResult(result: VerificationResult, backTrackInfo: BackTrackInfo): Result =
    result match {
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
