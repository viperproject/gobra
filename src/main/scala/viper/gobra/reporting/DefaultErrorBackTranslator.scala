// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.gobra.reporting.Source.{AutoImplProofAnnotation, CertainSource, CertainSynthesized, OverflowCheckAnnotation}
import viper.gobra.reporting.Source.Verifier./
import viper.silver
import viper.silver.ast.Not
import viper.silver.verifier.{errors => vprerr, reasons => vprrea}

object DefaultErrorBackTranslator {

  def translateWithTransformer(
                                viperError: viper.silver.verifier.VerificationError,
                                transformer: BackTranslator.ErrorTransformer
                              ): VerificationError = {
    val gobraError = transformer.lift.apply(viperError).getOrElse{ UncaughtError(viperError) }
    if (viperError.cached) gobraError.cached = true
    gobraError
  }

  def translateWithTransformer(
                                viperReason: silver.verifier.ErrorReason,
                                transformer: BackTranslator.ReasonTransformer
                              ): VerificationErrorReason = {
    transformer.lift.apply(viperReason).getOrElse{ UncaughtReason(viperReason) }
  }

  def defaultTranslate(viperReason: silver.verifier.ErrorReason): VerificationErrorReason =
    translateWithTransformer(viperReason, defaultReasonTransformer)

  val defaultReasonTransformer: BackTranslator.ReasonTransformer = {
    val defaultReasonTransformerAux: BackTranslator.ReasonTransformer = {
      case vprrea.InsufficientPermission(CertainSource(info)) =>
        InsufficientPermissionError(info)
      case vprrea.AssertionFalse(CertainSource(info)) =>
        AssertionFalseError(info)
      case vprrea.AssertionFalse(CertainSynthesized(info)) =>
        SynthesizedAssertionFalseError(info)
      case vprrea.SeqIndexExceedsLength(CertainSource(node), CertainSource(index)) =>
        SeqIndexExceedsLengthError(node, index)
      case vprrea.SeqIndexNegative(CertainSource(node), CertainSource(index)) =>
        SeqIndexNegativeError(node, index)
      case vprrea.DivisionByZero(info) =>
        DivisionByZeroReason(CertainSource.unapply(info))
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
      case vprrea.NegativePermission(CertainSource(info)) =>
        NegativePermissionError(info)
      //      case vprrea.InvalidPermMultiplication(offendingNode) =>
      //      case vprrea.MagicWandChunkNotFound(offendingNode) =>
      //      case vprrea.NamedMagicWandChunkNotFound(offendingNode) =>
      //      case vprrea.MagicWandChunkOutdated(offendingNode) =>
      //      case vprrea.ReceiverNotInjective(offendingNode) =>
      //      case vprrea.LabelledStateNotReached(offendingNode) =>
    }

    val transformVerificationErrorReason: VerificationErrorReason => VerificationErrorReason = {
      case AssertionFalseError(info / OverflowCheckAnnotation) => OverflowErrorReason(info)
      case x => x
    }

    defaultReasonTransformerAux.andThen(transformVerificationErrorReason)
  }
}

class DefaultErrorBackTranslator(
                                  backtrack: BackTranslator.BackTrackInfo
                                ) extends BackTranslator.ErrorBackTranslator {

  protected val defaultErrorTransformer: BackTranslator.ErrorTransformer = {
    // same order as they are declared in VerificationError.scala
    // errors regarding wellformedness, termination, magic wands, and heuristics are currently not transformed
    val errorMapper: BackTranslator.ErrorTransformer = {
      case vprerr.AssignmentFailed(CertainSource(info), reason, _) =>
        AssignmentError(info) dueTo translate(reason)
      case vprerr.CallFailed(CertainSource(info), reason, _) =>
        CallError(info) dueTo translate(reason)
      case vprerr.PreconditionInCallFalse(CertainSource(info), reason, _) =>
        PreconditionError(info) dueTo translate(reason)
      case vprerr.PreconditionInAppFalse(CertainSource(info), reason, _) =>
        PreconditionError(info) dueTo translate(reason)
      case vprerr.PredicateNotWellformed(CertainSource(info), reason, _) =>
        PredicateNotWellFormedError(info) dueTo translate(reason)
      case vprerr.ContractNotWellformed(CertainSource(info), reason, _) =>
        ImpreciseContractNotWellFormedError(info) dueTo translate(reason)
      case vprerr.ExhaleFailed(CertainSource(info), reason, _) =>
        ExhaleError(info) dueTo translate(reason)
      case vprerr.InhaleFailed(CertainSource(info), reason, _) =>
        InhaleError(info) dueTo translate(reason)
      case vprerr.WhileFailed(CertainSource(info), reason, _) =>
        ForLoopError(info) dueTo translate(reason)
      case vprerr.AssertFailed(CertainSource(info), reason, _) =>
        AssertError(info) dueTo translate(reason)
      case vprerr.PostconditionViolated(CertainSource(info), _, reason, _) =>
        PostconditionError(info) dueTo translate(reason)
      case vprerr.FoldFailed(CertainSource(info), reason, _) =>
        FoldError(info) dueTo translate(reason)
      case vprerr.UnfoldFailed(CertainSource(info), reason, _) =>
        UnfoldError(info) dueTo translate(reason)
      case vprerr.LoopInvariantNotPreserved(CertainSource(info), reason, _) =>
        LoopInvariantPreservationError(info) dueTo translate(reason)
      case vprerr.LoopInvariantNotEstablished(CertainSource(info), reason, _) =>
        LoopInvariantEstablishmentError(info) dueTo translate(reason)

      // Wytse (2020-05-22):
      // It appears that Viper sometimes negates conditions
      // during the translation of if-statements.
      // However, these generated negated conditions
      // don't appear to preserve any source information,
      // meaning that the above case for `IfFailed` doesn't catch all errors...
      // This extra case provides a workaround for this issue.
      // Nevertheless, this should eventually be solved on the Viper level I think.
      case vprerr.IfFailed(Source(info), reason, _) =>
        IfError(info) dueTo translate(reason)
      case vprerr.IfFailed(Not(Source(info)), reason, _) =>
        IfError(info) dueTo translate(reason)
      case vprerr.IfFailed(CertainSource(info), reason, _) =>
        IfError(info) dueTo translate(reason)
    }

    val transformAnnotatedError: VerificationError => VerificationError = x => x.info match {
      case _ / OverflowCheckAnnotation =>
        x.reasons.foldLeft(OverflowError(x.info): VerificationError){ case (err, reason) => err dueTo reason }

      case _ / AutoImplProofAnnotation(subT, superT) =>
        GeneratedImplementationProofError(subT, superT, x)

      case _ => x
    }

    errorMapper.andThen(transformAnnotatedError)
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
