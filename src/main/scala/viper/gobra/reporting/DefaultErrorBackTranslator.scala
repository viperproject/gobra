// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.gobra.reporting.Source.{AutoImplProofAnnotation, CertainSource, CertainSynthesized, FailedLinterCheck, ImportPreNotEstablished, InsufficientPermissionToRangeExpressionAnnotation, InvalidImplTermMeasureAnnotation, LoopInvariantNotEstablishedAnnotation, MainPreNotEstablished, OverflowCheckAnnotation, OverwriteErrorAnnotation, ReceiverNotNilCheckAnnotation}
import viper.gobra.reporting.Source.Verifier./
import viper.silver
import viper.silver.ast.Not
import viper.silver.verifier.{AbstractVerificationError, errors => vprerr, reasons => vprrea}
import viper.silver.plugin.standard.predicateinstance
import viper.silver.plugin.standard.termination
import viper.silver.plugin.standard.{refute => vprrefute}
import viper.silver.plugin.sif

object DefaultErrorBackTranslator {

  def translateWithTransformer(
                                viperError: viper.silver.verifier.VerificationError,
                                transformer: BackTranslator.ErrorTransformer
                              ): VerificationError = {
    val gobraError = transformer.lift.apply(viperError).getOrElse {
      UncaughtError(viperError)
    }
    if (viperError.cached) gobraError.cached = true
    gobraError
  }

  def translateWithTransformer(
                                viperReason: silver.verifier.ErrorReason,
                                transformer: BackTranslator.ReasonTransformer
                              ): VerificationErrorReason = {
    transformer.lift.apply(viperReason).getOrElse {
      UncaughtReason(viperReason)
    }
  }

  def defaultTranslate(viperReason: silver.verifier.ErrorReason): VerificationErrorReason =
    translateWithTransformer(viperReason, defaultReasonTransformer)

  val defaultReasonTransformer: BackTranslator.ReasonTransformer = {
    val defaultReasonTransformerAux: BackTranslator.ReasonTransformer = {
      case vprrea.InsufficientPermission(CertainSource(info)) =>
        InsufficientPermissionError(info)
      case vprrea.AssertionFalse(CertainSource(info)) =>
        AssertionFalseError(info)
      case vprrefute.RefutationTrue(CertainSource(info)) =>
        RefutationTrueError(info)
      case vprrea.AssertionFalse(CertainSynthesized(info)) =>
        SynthesizedAssertionFalseReason(info)
      case vprrea.SeqIndexExceedsLength(CertainSource(node), CertainSource(index)) =>
        SeqIndexExceedsLengthError(node, index)
      case vprrea.SeqIndexNegative(CertainSource(node), CertainSource(index)) =>
        SeqIndexNegativeError(node, index)
      case vprrea.DivisionByZero(info) =>
        DivisionByZeroReason(CertainSource.unapply(info))
      case vprrea.MapKeyNotContained(CertainSource(node), CertainSource(index)) =>
        MapKeyNotContained(node, index)
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
        NegativePermissionReason(info)
      //      case vprrea.InvalidPermMultiplication(offendingNode) =>
      case vprrea.MagicWandChunkNotFound(CertainSource(info)) =>
        MagicWandChunkNotFound(info)
      case vprrea.QPAssertionNotInjective(CertainSource(info)) =>
        QPAssertionNotInjective(info)
      case vprrea.LabelledStateNotReached(CertainSource(info)) =>
        LabelledStateNotReached(info)
      case termination.TerminationConditionFalse(CertainSource(info)) =>
        TerminationConditionFalseError(info)
      case termination.TupleConditionFalse(CertainSource(info)) =>
        TupleConditionFalseError(info)
      case termination.TupleSimpleFalse(CertainSource(info)) =>
        TupleSimpleFalseError(info)
      case termination.TupleDecreasesFalse(CertainSource(info)) =>
        TupleDecreasesFalseError(info)
      case termination.TupleBoundedFalse(CertainSource(info)) =>
        TupleBoundedFalseError(info)
      case sif.SIFGotoNotLowEvent(CertainSource(info)) =>
        SIFGotoNotLowEvent(info)
    }

    val transformVerificationErrorReason: VerificationErrorReason => VerificationErrorReason = {
      case AssertionFalseError(info / OverflowCheckAnnotation) => OverflowErrorReason(info)
      case AssertionFalseError(info / FailedLinterCheck(checkType)) => LinterCheckReason(info, checkType)
      case RefutationTrueError(info / FailedLinterCheck(checkType)) => LinterCheckReason(info, checkType)
      case AssertionFalseError(info / ReceiverNotNilCheckAnnotation) => InterfaceReceiverIsNilReason(info)
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
      case vprerr.FunctionNotWellformed(CertainSource(info), reason, _) =>
        PureFunctionNotWellFormedError(info) dueTo translate(reason)
      case vprerr.ExhaleFailed(CertainSource(info), reason, _) =>
        ExhaleError(info) dueTo translate(reason)
      case vprerr.InhaleFailed(CertainSource(info), reason, _) =>
        InhaleError(info) dueTo translate(reason)
      case vprerr.WhileFailed(CertainSource(info), reason, _) =>
        ForLoopError(info) dueTo translate(reason)
      case vprerr.AssertFailed(CertainSource(info), reason, _) =>
        AssertError(info) dueTo translate(reason)
      case vprrefute.RefuteFailed(CertainSource(info), reason, _) =>
        RefuteError(info) dueTo translate(reason)
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
      case vprerr.MagicWandNotWellformed(CertainSource(info), reason, _) =>
        MagicWandNotWellformedError(info) dueTo translate(reason)
      case vprerr.PackageFailed(CertainSource(info), reason, _) =>
        PackageFailedError(info) dueTo translate(reason)
      case vprerr.ApplyFailed(CertainSource(info), reason, _) =>
        ApplyFailed(info) dueTo translate(reason)

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
      case predicateinstance.PredicateInstanceNoAccess(Source(info), reason, _) =>
        PredicateInstanceNoAccessError(info) dueTo translate(reason)
      case termination.FunctionTerminationError(Source(info), reason, _) =>
        FunctionTerminationError(info) dueTo translate(reason)
      case termination.MethodTerminationError(Source(info), reason, _) =>
        MethodTerminationError(info) dueTo translate(reason)
      case termination.LoopTerminationError(Source(info), reason, _) =>
        LoopTerminationError(info) dueTo translate(reason)
      case sif.SIFGotoCheckFailed(Source(info), reason, _) =>
        SIFGotoError(info) dueTo translate(reason)
    }

    val transformAnnotatedError: VerificationError => VerificationError = x => x.info match {
      case _ / (an: OverwriteErrorAnnotation) => an(x)

      case _ / OverflowCheckAnnotation =>
        x.reasons.foldLeft(OverflowError(x.info): VerificationError) { case (err, reason) => err dueTo reason }

      case _ / FailedLinterCheck(reason) =>
        x.reasons.foldLeft(LinterError(x.info): VerificationError) { case (err, reason) => err dueTo reason }

      case _ / AutoImplProofAnnotation(subT, superT) =>
        GeneratedImplementationProofError(subT, superT, x)

      case _ / MainPreNotEstablished =>
        x.reasons.foldLeft(MainPreconditionNotEstablished(x.info): VerificationError) {
          case (err, reason) => err dueTo reason
        }

      case _ / ImportPreNotEstablished =>
        x.reasons.foldLeft(ImportPreconditionNotEstablished(x.info): VerificationError) {
          case (err, reason) => err dueTo reason
        }

      case _ / InsufficientPermissionToRangeExpressionAnnotation() =>
        x.reasons.foldLeft(InsufficientPermissionToRangeExpressionError(x.info): VerificationError) { case (err, reason) => err dueTo reason }

      case _ / LoopInvariantNotEstablishedAnnotation =>
        x.reasons.foldLeft(LoopInvariantEstablishmentError(x.info): VerificationError) { case (err, reason) => err dueTo reason }

      case _ / InvalidImplTermMeasureAnnotation() =>
        x.reasons.foldLeft(MethodTerminationError(x.info): VerificationError) { case (err, _) => err dueTo ImplMeasureHigherThanInterfaceReason(x.info) }

      case _ => x
    }

    errorMapper.andThen(transformAnnotatedError)
  }

  private val errorTransformer = backtrack.errorT.foldRight(defaultErrorTransformer) {
    case (l, r) => l orElse r
  }

  private val reasonTransformer = backtrack.reasonT.foldRight(DefaultErrorBackTranslator.defaultReasonTransformer) {
    case (l, r) => l orElse r
  }

  override def translate(viperError: viper.silver.verifier.VerificationError): VerificationError = {
    val transformedViperError = viperError match {
      case err: AbstractVerificationError => err.transformedError()
      case err => err
    }
    DefaultErrorBackTranslator.translateWithTransformer(transformedViperError, errorTransformer)
  }

  override def translate(viperReason: silver.verifier.ErrorReason): VerificationErrorReason = {
    DefaultErrorBackTranslator.translateWithTransformer(viperReason, reasonTransformer)
  }
}
