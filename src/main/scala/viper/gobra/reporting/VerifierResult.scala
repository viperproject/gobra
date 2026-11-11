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
sealed trait NegativeVerifierResult extends VerifierResult

object VerifierResult {

  case object Success extends VerifierResult
  case class Failure(errors: Vector[VerifierError]) extends NegativeVerifierResult {
    require(errors.nonEmpty, "got empty list during construction of VerifierResult.Failure")
  }
  /** the verification has been aborted, e.g., via [[viper.gobra.util.AbortSignal]] */
  case object Aborted extends NegativeVerifierResult
  /** the verification has been skipped due to a configuration option */
  case object Skipped extends NegativeVerifierResult
}

object IntermediateVerifierResult {
  /** Result of a single step of Gobra's pipeline: either the step's output (right) or a final
    * [[VerifierResult]] short-circuiting the remaining steps (left).
    **/
  type IntermediateVerifierResult[R] = EitherT[NegativeVerifierResult, Future, R]

  /** Construct a final verifier result that short-circuits all remaining steps */
  def apply[R](l: NegativeVerifierResult)(implicit ec: ExecutionContext): IntermediateVerifierResult[R] = {
    EitherT.left(l)
  }

  /** Construct a step's result */
  def apply[R](r: R)(implicit ec: ExecutionContext): IntermediateVerifierResult[R] = {
    EitherT.right(r)
  }
}
