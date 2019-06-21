package viper.gobra.reporting

import viper.silver
import viper.silver.verifier.{errors => vprerr, reasons => vprrea}

class DefaultErrorBackTranslator(
                                  errorTs: Seq[BackTranslator.ErrorTransformer],
                                  reasonTs: Seq[BackTranslator.ReasonTransformer]
                                ) extends BackTranslator.ErrorBackTranslator {

  protected val defaultErrorTransformer: BackTranslator.ErrorTransformer = {
    case vprerr.AssignmentFailed(Source(sourceNode: PHeapRead), reason, _) =>
      AssignmentError(sourceNode, translate(reason))
    case vprerr.AssignmentFailed(Source(sourceNode: PHeapWrite), reason, _) =>
      AssignmentError(sourceNode, translate(reason))
    case vprerr.AssignmentFailed(Source(sourceNode: PAssign), reason, _) =>
      AssignmentError(sourceNode, translate(reason))
    case vprerr.PostconditionViolated(Source(sourceNode: PExpression), _, reason, _) =>
      PostconditionError(sourceNode, translate(reason))
    case vprerr.PreconditionInCallFalse(Source(sourceNode: PProcedureCall), reason, _) =>
      PreconditionError(sourceNode, translate(reason))
    case vprerr.AssertFailed(Source(sourceNode: PAssert), reason, _) =>
      AssertError(sourceNode, translate(reason))
    case vprerr.ExhaleFailed(Source(sourceNode: PExhale), reason, _) =>
//      ExhaleError(sourceNode, translate(reason))
//    case vprerr.FoldFailed(Source(sourceNode: PFold), reason, _) =>
//      FoldError(sourceNode, translate(reason))
//    case vprerr.UnfoldFailed(Source(sourceNode: PUnfold), reason, _) =>
//      UnfoldError(sourceNode, translate(reason))
//    case vprerr.LoopInvariantNotEstablished(Source(sourceNode: PInvariantClause), reason, _) =>
//      LoopInvariantEstablishmentError(sourceNode, translate(reason))
//    case vprerr.LoopInvariantNotPreserved(Source(sourceNode: PInvariantClause), reason, _) =>
//      LoopInvariantPreservationError(sourceNode, translate(reason))
  }

  protected val defaultReasonTransformer: BackTranslator.ReasonTransformer = {
    case vprrea.InsufficientPermission(Source(sourceNode: PAstNode)) =>
      InsufficientPermissionError(sourceNode)
    case vprrea.AssertionFalse(Source(sourceNode: PExpression)) =>
      AssertionError(sourceNode)
    case vprrea.AssertionFalse(Source(sourceNode: PInvariantClause)) =>
      AssertionError(sourceNode.assertion)
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

  private val errorTransformer = errorTs.foldLeft(defaultErrorTransformer){
    case (l, r) => l orElse r
  }
  private val reasonTransformer = reasonTs.foldLeft(defaultReasonTransformer){
    case (l, r) => l orElse r
  }

  override def translate(viperError: viper.silver.verifier.VerificationError): VerifierError = {
    errorTransformer.lift.apply(viperError).getOrElse{
      var messages: Vector[String] = Vector.empty
      messages += "Found non-verification-failures"
      messages += "Failed to back-translate a Viper error"
      messages += s"  ${viperError.readableMessage}"
      messages += s"    error is ${viperError.getClass.getSimpleName}"
      messages += s"    error off. node = ${viperError.offendingNode}"
      messages += s"    error off. node src = ${Source.unapply(viperError.offendingNode)}"
      messages += s"    reason is ${viperError.reason.getClass.getSimpleName}"
      messages += s"    reason off. node = ${viperError.reason.offendingNode}"
      messages += s"    reason off. node src = ${Source.unapply(viperError.reason.offendingNode)}"

      val completeMessage = messages.mkString("\n")
      throw new java.lang.IllegalStateException(completeMessage)
    }
  }

  override def translate(reason: silver.verifier.ErrorReason): VerifierError = {
    val errorOpt = reasonTransformer.lift.apply(reason)
  }
}
