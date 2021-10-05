// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.ast.frontend.{PFunctionDecl, PMethodDecl}
import viper.gobra.backend.ViperBackends.{CarbonBackend => Carbon}
import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.gobra.reporting.{BackTranslator, BacktranslatingReporter, ChoppedViperMessage, Source}
import viper.gobra.util.GobraExecutionContext
import viper.silver
import viper.silver.verifier.VerificationResult
import viper.silver.{ast => vpr}

import scala.concurrent.Future
import viper.gobra.util.ViperChopper
import viper.silver.ast.SourcePosition

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

  def verify(task: Task)(config: Config)(implicit executor: GobraExecutionContext): Future[Result] = {

    var exePaths: Vector[String] = Vector.empty

    config.z3Exe match {
      case Some(z3Exe) =>
        exePaths ++= Vector("--z3Exe", z3Exe)
      case _ =>
    }

    (config.backend, config.boogieExe) match {
      case (Carbon, Some(boogieExe)) =>
        exePaths ++= Vector("--boogieExe", boogieExe)
      case _ =>
    }

    val verificationResults =  if (config.shouldChop) {
      println("Maps: " + config.isolate)
      val isolate = {
        def hit(x: SourcePosition, target: SourcePosition): Boolean = {
          (target.end match {
            case None => x.start.line == target.start.line
            case Some(pos) => target.start.line <= x.start.line && x.start.line <= pos.line
          }) && x.file.getFileName == target.file.getFileName
        }

        config.isolate.map { names => (m: vpr.Method) => m match {
          case Source(Source.Verifier.Info(_: PFunctionDecl, _, origin, _)) => names.exists(hit(_, origin.pos))
          case Source(Source.Verifier.Info(_: PMethodDecl, _, origin, _)) => names.exists(hit(_, origin.pos))
          case _ => false
        }}
      }
      val programs: Vector[vpr.Program] = if (isolate.isDefined) ViperChopper.chop(task.program)(isolate = isolate) else Vector(task.program)
      programs.zipWithIndex.foreach{ case (chopped, idx) =>
        config.reporter report ChoppedViperMessage(config.inputFiles.head, idx, () => chopped, () => task.backtrack)
      }

      val verifier = config.backend.create(exePaths)

      // val verificationResults = Future.traverse(programs.zipWithIndex) { case (program, idx) =>
      //   val programID = s"_programID_${config.inputFiles.head.getFileName}_$idx"
      //   verifier.verify(programID, config.backendConfig, BacktranslatingReporter(config.reporter, task.backtrack, config), program)(executor)
      // }

      programs.zipWithIndex.foldLeft(Future.successful(Vector(silver.verifier.Success)): Future[Vector[VerificationResult]]){ case (res, (program, idx)) =>
        val programID = s"_programID_${config.inputFiles.head.getFileName}_$idx"
        for {
          acc <- res
          next <- verifier.verify(programID, config.backendConfig, BacktranslatingReporter(config.reporter, task.backtrack, config), program)(executor)
        } yield acc :+ next
      }
    } else {
      val verifier = config.backend.create(exePaths)

      val programID = s"_programID_${config.inputFiles.head}"

      verifier.verify(programID, config.backendConfig, BacktranslatingReporter(config.reporter, task.backtrack, config), task.program)(executor).map(Vector(_))
    }

    
    verificationResults.map{ results =>
      val result = results.foldLeft(silver.verifier.Success: VerificationResult){
        case (acc, silver.verifier.Success) => acc
        case (silver.verifier.Success, res) => res
        case (l: silver.verifier.Failure, r: silver.verifier.Failure) => silver.verifier.Failure(l.errors ++ r.errors)
      }
      convertVerificationResult(result, task.backtrack)
    }
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
