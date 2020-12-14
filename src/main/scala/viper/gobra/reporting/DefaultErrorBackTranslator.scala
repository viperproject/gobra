// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.gobra.ast.internal.transform.OverflowChecksTransform.OverflowCheckAnnotation
import viper.gobra.reporting.Source.{AnnotatedOrigin, Synthesized}
import viper.gobra.util.Violation.violation
import viper.silver
import viper.silver.ast.Not
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
    val defaultReasonTransformerAux: BackTranslator.ReasonTransformer = {
      case vprrea.InsufficientPermission(Source(info)) =>
        InsufficientPermissionError(info)
      case vprrea.AssertionFalse(Source(info)) =>
        AssertionFalseError(info)
      case vprrea.AssertionFalse(Synthesized(info)) =>
        SynthesizedAssertionFalseError(info)
      case vprrea.SeqIndexExceedsLength(Source(node), Source(index)) =>
        SeqIndexExceedsLengthError(node, index)
      case vprrea.SeqIndexNegative(Source(node), Source(index)) =>
        SeqIndexNegativeError(node, index)
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
    }

    val transformVerificationErrorReason: VerificationErrorReason => VerificationErrorReason = {
      case a@AssertionFalseError(info) if info.origin.isInstanceOf[AnnotatedOrigin] =>
        info.origin.asInstanceOf[AnnotatedOrigin].annotation match {
          case OverflowCheckAnnotation => OverflowErrorReason(info)
          case _ => a
        }
      case x => x
    }

    { case x => transformVerificationErrorReason(defaultReasonTransformerAux(x)) }
  }
}

class DefaultErrorBackTranslator(
                                  backtrack: BackTranslator.BackTrackInfo
                                ) extends BackTranslator.ErrorBackTranslator {

  protected val defaultErrorTransformer: BackTranslator.ErrorTransformer = {
    // same order as they are declared in VerificationError.scala
    // errors regarding wellformedness, termination, magic wands, and heuristics are currently not transformed
    val errorMapper: BackTranslator.ErrorTransformer = {
      case vprerr.AssignmentFailed(Source(info), reason, _) =>
        AssignmentError(info) dueTo translate(reason)
      case vprerr.CallFailed(Source(info), reason, _) =>
        CallError(info) dueTo translate(reason)
      case vprerr.PreconditionInCallFalse(Source(info), reason, _) =>
        PreconditionError(info) dueTo translate(reason)
      case vprerr.PreconditionInAppFalse(Source(info), reason, _) =>
        PreconditionError(info) dueTo translate(reason)
      case vprerr.ExhaleFailed(Source(info), reason, _) =>
        ExhaleError(info) dueTo translate(reason)
      case vprerr.InhaleFailed(Source(info), reason, _) =>
        InhaleError(info) dueTo translate(reason)
      case vprerr.IfFailed(Source(info), reason, _) =>
        IfError(info) dueTo translate(reason)
      case vprerr.WhileFailed(Source(info), reason, _) =>
        ForLoopError(info) dueTo translate(reason)
      case vprerr.AssertFailed(Source(info), reason, _) =>
        AssertError(info) dueTo translate(reason)
      case vprerr.PostconditionViolated(Source(info), _, reason, _) =>
        PostconditionError(info) dueTo translate(reason)
      case vprerr.FoldFailed(Source(info), reason, _) =>
        FoldError(info) dueTo translate(reason)
      case vprerr.UnfoldFailed(Source(info), reason, _) =>
        UnfoldError(info) dueTo translate(reason)
      case vprerr.LoopInvariantNotPreserved(Source(info), reason, _) =>
        LoopInvariantPreservationError(info) dueTo translate(reason)
      case vprerr.LoopInvariantNotEstablished(Source(info), reason, _) =>
        LoopInvariantEstablishmentError(info) dueTo translate(reason)

      // Wytse (2020-05-22):
      // It appears that Viper sometimes negates conditions
      // during the translation of if-statements.
      // However, these generated negated conditions
      // don't appear to preserve any source information,
      // meaning that the above case for `IfFailed` doesn't catch all errors...
      // This extra case provides a workaround for this issue.
      // Nevertheless, this should eventually be solved on the Viper level I think.
      case vprerr.IfFailed(Not(Source(info)), reason, _) =>
        IfError(info) dueTo translate(reason)
    }

    val transformAnnotatedError: VerificationError => VerificationError = x => {
      x.info.origin match {
        case origin: AnnotatedOrigin =>
          // errorMapper assigns a reason to every error. as such, at this point, every error should have one error reason
          violation(x.reasons.size == 1, "Error expected to have one and only one reason.")
          origin.annotation match {
            case OverflowCheckAnnotation =>
              OverflowError(x.info) dueTo x.reasons.head
            case _ => ???
          }
        case _ => x
      }
    }

    { case x => transformAnnotatedError(errorMapper(x)) }
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
