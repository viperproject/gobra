// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.


package viper.gobra.reporting

import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import scala.concurrent.{ExecutionContext, Future}

sealed trait VerifierResult

object VerifierResult {

  case object Success extends VerifierResult
  case class Failure(errors: Vector[VerifierError]) extends VerifierResult {
    require(errors.nonEmpty, "got empty list during construction of VerifierResult.Failure")
  }
  /** the verification has been aborted, e.g., via [[viper.gobra.util.AbortSignal]] */
  case object Aborted extends VerifierResult
}

object IntermediateVerifierResult {
  /** Result of a single step of Gobra's pipeline: either the step's output (right) or a
    * [[LeftIntermediateVerifierResult]] short-circuiting the remaining steps (left).
    **/
  type IntermediateVerifierResult[R] = EitherT[LeftIntermediateVerifierResult, Future, R]

  /** Construct a left intermediate verifier result that short-circuits all remaining steps */
  def apply[R](l: LeftIntermediateVerifierResult)(implicit ec: ExecutionContext): IntermediateVerifierResult[R] = {
    EitherT.left[LeftIntermediateVerifierResult, Future, R](l)
  }

  /** Construct a step's result */
  def apply[R](r: R)(implicit ec: ExecutionContext): IntermediateVerifierResult[R] = {
    EitherT.right[LeftIntermediateVerifierResult, Future, R](r)
  }

  /** outcome of a step that does not produce an output for the subsequent step */
  sealed trait LeftIntermediateVerifierResult {
    /** the overall verification result that this step outcome amounts to */
    def toVerifierResult: VerifierResult
  }

  /** the verification has been aborted, e.g., via [[viper.gobra.util.AbortSignal]] */
  case object Aborted extends LeftIntermediateVerifierResult {
    override def toVerifierResult: VerifierResult = VerifierResult.Aborted
  }
  /** the step has been skipped due to a configuration option, which ends the pipeline successfully */
  case object Skipped extends LeftIntermediateVerifierResult {
    override def toVerifierResult: VerifierResult = VerifierResult.Success
  }
  /** the step produced errors */
  case class Errored(errors: Vector[VerifierError]) extends LeftIntermediateVerifierResult {
    require(errors.nonEmpty, "got empty list during construction of IntermediateVerifierResult.Errored")
    override def toVerifierResult: VerifierResult = VerifierResult.Failure(errors)
  }
}
