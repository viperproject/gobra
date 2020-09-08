// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.silver
import viper.silver.verifier.{errors => vprerr, reasons => vprrea}

object DefaultErrorBackTranslator {

  def translateWithTransformer(
                                viperError: viper.silver.verifier.VerificationError,
                                transformer: BackTranslator.ErrorTransformer
                              ): VerificationError = {
    val gobraError = transformer.lift.apply(viperError).getOrElse{
      val message: String =
        s"""
           |Failed to back-translate a Viper error
           |  ${viperError.readableMessage}
           |    error is ${viperError.getClass.getSimpleName}
           |    error off. node = ${viperError.offendingNode}
           |    error off. node src = ${Source.unapply(viperError.offendingNode)}
           |    reason is ${viperError.reason.getClass.getSimpleName}
           |    reason off. node = ${viperError.reason.offendingNode}
           |    reason off. node src = ${Source.unapply(viperError.reason.offendingNode)}
        """.stripMargin

      throw new java.lang.IllegalStateException(message)
    }
    if (viperError.cached) gobraError.cached = true
    gobraError
  }

  def translateWithTransformer(
                                viperReason: silver.verifier.ErrorReason,
                                transformer: BackTranslator.ReasonTransformer
                              ): VerificationErrorReason = {
    transformer.lift.apply(viperReason).getOrElse{
      val message: String =
        s"""
           |Failed to back-translate a Viper reason
           |  ${viperReason.readableMessage}
           |    error is ${viperReason.getClass.getSimpleName}
           |    error off. node = ${viperReason.offendingNode}
           |    error off. node src = ${Source.unapply(viperReason.offendingNode)}
        """.stripMargin

      throw new java.lang.IllegalStateException(message)
    }
  }

  def defaultTranslate(viperReason: silver.verifier.ErrorReason): VerificationErrorReason =
    translateWithTransformer(viperReason, defaultReasonTransformer)

  val defaultReasonTransformer: BackTranslator.ReasonTransformer = {
    case vprrea.InsufficientPermission(Source(info)) =>
      InsufficientPermissionError(info)
    case vprrea.AssertionFalse(Source(info)) =>
      AssertionFalseError(info)
    case vprrea.AssertionFalse(Source(info)) =>
      AssertionFalseError(info)
    //      case vprrea.DummyReason =>
    //      case vprrea.InternalReason(offendingNode, explanation) =>
    //      case vprrea.FeatureUnsupported(offendingNode, explanation) =>
    //      case vprrea.UnexpectedNode(offendingNode, explanation, stackTrace) =>
    //      case vprrea.VariantNotDecreasing(offendingNode, decExp) =>
    //      case vprrea.TerminationNoBound(offendingNode, decExp) =>
    //      case vprrea.CallingNonTerminatingFunction(offendingNode, callee) =>
    //      case vprrea.NoDecClauseSpecified(offendingNode) =>
    //      case vprrea.EpsilonAsParam(offendingNode) =>
    //      case vprrea.ReceiverNull(offendingNode) =>
    //      case vprrea.DivisionByZero(offendingNode) =>
    //      case vprrea.NegativePermission(offendingNode) =>
    //      case vprrea.InvalidPermMultiplication(offendingNode) =>
    //      case vprrea.MagicWandChunkNotFound(offendingNode) =>
    //      case vprrea.NamedMagicWandChunkNotFound(offendingNode) =>
    //      case vprrea.MagicWandChunkOutdated(offendingNode) =>
    //      case vprrea.ReceiverNotInjective(offendingNode) =>
    //      case vprrea.LabelledStateNotReached(offendingNode) =>
    //      case vprrea.SeqIndexNegative(seq, offendingNode) =>
    //      case vprrea.SeqIndexExceedsLength(seq, offendingNode) =>
  }
}

class DefaultErrorBackTranslator(
                                  backtrack: BackTranslator.BackTrackInfo
                                ) extends BackTranslator.ErrorBackTranslator {

  protected val defaultErrorTransformer: BackTranslator.ErrorTransformer = {
    case vprerr.AssignmentFailed(Source(info), reason, _) =>
      AssignmentError(info) dueTo translate(reason)
    case vprerr.PostconditionViolated(Source(info), _, reason, _) =>
      PostconditionError(info) dueTo translate(reason)
    case vprerr.PreconditionInCallFalse(Source(info), reason, _) =>
      PreconditionError(info) dueTo translate(reason)
    case vprerr.AssertFailed(Source(info), reason, _) =>
      AssertError(info) dueTo translate(reason)
    case vprerr.ExhaleFailed(Source(info), reason, _) =>
      ExhaleError(info) dueTo translate(reason)
    case vprerr.FoldFailed(Source(info), reason, _) =>
      FoldError(info) dueTo translate(reason)
    case vprerr.UnfoldFailed(Source(info), reason, _) =>
      UnfoldError(info) dueTo translate(reason)
    case vprerr.LoopInvariantNotEstablished(Source(info), reason, _) =>
      LoopInvariantEstablishmentError(info) dueTo translate(reason)
    case vprerr.LoopInvariantNotPreserved(Source(info), reason, _) =>
      LoopInvariantPreservationError(info) dueTo translate(reason)
  }

  private val errorTransformer = backtrack.errorT.foldRight(defaultErrorTransformer){
    case (l, r) => l orElse r
  }
  private val reasonTransformer = backtrack.reasonT.foldRight(DefaultErrorBackTranslator.defaultReasonTransformer){
    case (l, r) => l orElse r
  }

  override def translate(viperError: viper.silver.verifier.VerificationError): VerificationError =
    DefaultErrorBackTranslator.translateWithTransformer(viperError, errorTransformer)


  override def translate(viperReason: silver.verifier.ErrorReason): VerificationErrorReason = {
    DefaultErrorBackTranslator.translateWithTransformer(viperReason, reasonTransformer)
  }
}
