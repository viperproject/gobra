// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.gobra.ast.frontend
import viper.gobra.ast.frontend.{PReceive, PSendStmt}
import viper.gobra.util.Violation.violation
import viper.silver.ast.SourcePosition

sealed trait VerifierError {
  def position: Option[SourcePosition]
  def message: String
  def id: String

  def formattedMessage: String = position match {
    case Some(pos) => s"<${pos.line}:${pos.column}> $message"
    case _ => message
  }

  override def toString: String = formattedMessage

  var cached: Boolean = false
}

case class NotFoundError(message: String) extends VerifierError {
  val position: Option[SourcePosition] = None
  val id = "not_found_error"
}

case class ParserError(message: String, position: Option[SourcePosition]) extends VerifierError {
  val id = "parser_error"
}

case class TypeError(message: String, position: Option[SourcePosition]) extends VerifierError {
  val id = "type_error"
}

case class CyclicImportError(message: String) extends VerifierError {
  val position: Option[SourcePosition] = None
  val id = "cyclic_import_error"
}

case class DiamondError(message: String) extends VerifierError {
  val position: Option[SourcePosition] = None
  val id = "diamond_error"
}


sealed trait VerificationError extends VerifierError {

  def info: Source.Verifier.Info

  override def position: Option[SourcePosition] = Some(info.origin.pos)

  def localId: String
  def localMessage: String

  override def id: String = (localId :: reasons.map(_.id)).mkString(":")

  override def message: String = {
    val reasonsMsg = if (reasons.nonEmpty) s"\n${reasons.mkString("\n")}" else ""
    val detailsMsg = if (details.nonEmpty) s"\n${details.mkString("\n")}" else ""

    s"$localMessage. $reasonsMsg$detailsMsg"
  }

  protected var _reasons: List[VerificationErrorReason] = List.empty

  def reasons: List[VerificationErrorReason] = _reasons

  def dueTo(reasonToAppend: VerificationErrorReason): VerificationError = {
    _reasons ::= reasonToAppend
    this
  }

  def dueTo(reasonToAppend: Option[VerificationErrorReason]): VerificationError = reasonToAppend match {
    case Some(reason) => dueTo(reason)
    case None => this
  }

  protected var _details: List[VerificationErrorClarification] = List.empty

  def details: List[VerificationErrorClarification] = _details

  def withDetail(detailToAppend: VerificationErrorClarification): VerificationError = {
    _details ::= detailToAppend
    this
  }

  def withDetail(detailToAppend: Option[VerificationErrorClarification]): VerificationError = detailToAppend match {
    case Some(detail) => withDetail(detail)
    case None => this
  }
}

abstract class ErrorExtension(error: VerificationError) extends VerificationError {
  def extensionMessage: String
  def extensionId: String

  override def localId: String = s"$extensionId:${error.localId}"
  override def localMessage: String = s"$extensionMessage. ${error.localMessage}"
  override def info: Source.Verifier.Info = error.info
  override def reasons: List[VerificationErrorReason] = super.reasons ::: error.reasons
  override def details: List[VerificationErrorClarification] = super.details ::: error.details
}

case class UncaughtError(viperError: viper.silver.verifier.VerificationError) extends VerificationError {
  val infoOpt: Option[Source.Verifier.Info] = Source.unapply(viperError.offendingNode)
  override def info: Source.Verifier.Info = violation("Uncaught errors do not have a Gobra source.")
  override def position: Option[SourcePosition] = infoOpt.map(_.origin.pos)
  override def localId: String = "uncaught_error"
  override def localMessage: String =
    s"""
       |Encountered an unexpected Viper error. This is a bug. The following information is for debugging purposes:
       |  ${viperError.readableMessage}
       |    error is ${viperError.getClass.getSimpleName}
       |    error offending node = ${viperError.offendingNode}
       |    error offending node src = ${Source.unapply(viperError.offendingNode)}
       |    reason is ${viperError.reason.getClass.getSimpleName}
       |    reason offending node = ${viperError.reason.offendingNode}
       |    reason offending node src = ${Source.unapply(viperError.reason.offendingNode)}
        """.stripMargin
}

case class AssignmentError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "assignment_error"
  override def localMessage: String = "Assignment might fail"
}

case class TypeAssertionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "type_assertion_error"
  override def localMessage: String = "Type assertion might fail"
}

case class ComparisonError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "comparison_error"
  override def localMessage: String = "Comparison might panic"
}

case class CallError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "call_error"
  override def localMessage: String = "Call might fail"
}

case class PostconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "postcondition_error"
  override def localMessage: String = "Postcondition might not hold"
}

case class PreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "precondition_error"
  override def localMessage: String = s"Precondition of call ${info.trySrc[frontend.PInvoke](" ")}might not hold"
}

case class AssertError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "assert_error"
  override def localMessage: String = "Assert might fail"
}

case class ExhaleError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "exhale_error"
  override def localMessage: String = "Exhale might fail"
}

case class InhaleError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "inhale_error"
  override def localMessage: String = "Inhale might fail"
}

case class FoldError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "fold_error"
  override def localMessage: String = "Fold might fail"
}

case class UnfoldError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "unfold_error"
  override def localMessage: String = "Unfold might fail"
}

case class LoopInvariantPreservationError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "invariant_preservation_error"
  override def localMessage: String = "Loop invariant might not be preserved"
}

case class LoopInvariantEstablishmentError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "invariant_establishment_error"
  override def localMessage: String = "Loop invariant might not be established"
}

case class LoopInvariantNotWellFormedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "invariant_not_well_formed"
  override def localMessage: String = "Loop invariant is not well-formed"
}

case class MethodContractNotWellFormedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "contract_not_well_formed"
  override def localMessage: String = "Method contract is not well-formed"
}

case class PredicateNotWellFormedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "predicate_not_well_defined"
  override def localMessage: String = "Predicate body is not well-formed"
}

case class PureFunctionNotWellFormedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "pure_function_not_well_defined"
  override def localMessage: String = "The pure function is not well-formed"
}

case class ImpreciseContractNotWellFormedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "imprecise_contract_not_well_formed"
  override def localMessage: String  ="Contract is not well-formed"
}

case class IfError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "conditional_error"
  override def localMessage: String = "Conditional statement might fail"
}

case class ForLoopError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "for_loop_error"
  override def localMessage: String = "For loop statement might fail"
}

case class OverflowError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "integer_overflow_error"
  override def localMessage: String = "Expression may cause integer overflow"
}

case class ArrayMakePreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "make_precondition_error"
  override def localMessage: String = s"The provided length might not be smaller or equals to the provided capacity, or length and capacity might not be non-negative"
}

case class ChannelMakePreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "make_precondition_error"
  override def localMessage: String = s"The provided length to ${info.origin.tag.trim} might be negative"
}

case class MapMakePreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "make_precondition_error"
  override def localMessage: String = s"The provided length to ${info.origin.tag.trim} might be negative"
}

case class GeneratedImplementationProofError(subT: String, superT: String, error: VerificationError) extends ErrorExtension(error) {
  override def extensionId: String = "generated_implementation_proof"
  override def extensionMessage: String = s"Generated implementation proof ($subT implements $superT) failed"
  override def localMessage: String = error match {
    case _: PreconditionError => s"$extensionMessage. Precondition of call to implementation method might not hold"
    case _: PostconditionError => s"$extensionMessage. Postcondition of interface method might not hold"
    case _ => super.localMessage
  }
}

case class ChannelReceiveError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "receive_error"
  override def localMessage: String = s"The receive expression ${info.trySrc[PReceive](" ")}might fail"
}

case class ChannelSendError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "send_error"
  override def localMessage: String = s"The receive expression ${info.trySrc[PSendStmt](" ")}might fail"
}



sealed trait VerificationErrorReason {
  def id: String
  def message: String
  override def toString: String = message
}

case class UncaughtReason(viperReason: viper.silver.verifier.ErrorReason) extends VerificationErrorReason {
  override def id: String = "uncaught_reason"
  override def message: String =
    s"""
       |Encountered an unexpected Viper reason. This is a bug. The following information is for debugging purposes:
       |  ${viperReason.readableMessage}
       |    error is ${viperReason.getClass.getSimpleName}
       |    error offending node = ${viperReason.offendingNode}
       |    error offending node src = ${Source.unapply(viperReason.offendingNode)}
        """.stripMargin
}

case class InsufficientPermissionError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "permission_error"
  override def message: String = s"permission to ${info.origin.tag.trim} might not suffice"
}

case class InsufficientPermissionFromTagError(tag: String) extends VerificationErrorReason {
  override def id: String = "permission_error"
  override def message: String = s"permission to $tag might not suffice"
}

case class AssertionFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "assertion_error"
  override def message: String = s"Assertion ${info.origin.tag.trim} might not hold"
}

case class SeqIndexExceedsLengthError(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "seq_index_exceeds_length_error"
  override def message: String = s"Index ${index.origin.tag.trim} into ${node.origin.tag.trim} might exceed sequence length"
}

case class SeqIndexNegativeError(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "seq_index_negative_error"
  override def message: String = s"Index ${index.origin.tag.trim} into ${node.origin.tag.trim} might be negative"
}

case class MapKeyNotContained(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "map_key_not_contained"
  override def message: String = s"Key ${index.origin.tag.trim} might not be contained in ${node.origin.tag.trim}"
}

case class KeyNotComparableReason(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "key_not_comparable_reason"
  override def message: String = s"Key in ${info.origin.tag.trim} is not comparable"
}

case class RepeatedMapKeyReason(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "repeated_map_key_reason"
  override def message: String = s"Map literal ${info.origin.tag.trim} might contain the key twice"
}

// João, 06/03/2021: unlike the other subtypes of VerificationErrorReason, DivisionByZeroReason has an Optional argument.
// This has to do with the fact that, in our tests, there are cases where a division by zero occurs but we cannot retrieve
// a corresponding Source.Verifier.Info. E.g. src/test/resources/regressions/features/fractional_permissions/fields/fail3.gobra
case class DivisionByZeroReason(node: Option[Source.Verifier.Info]) extends VerificationErrorReason {
  override def id: String = "division_by_zero"
  override def message: String = s"Divisor ${node.map(_.origin.tag.trim).getOrElse("expression")} might be zero"
}

case class OverflowErrorReason(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "integer_overflow_error"
  override def message: String = s"Expression ${node.origin.tag.trim} might cause integer overflow"
}

case class DynamicValueNotASubtypeReason(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "failed_type_assertion"
  override def message: String = s"Dynamic value might not be a subtype of the target type."
}

case class SafeTypeAssertionsToInterfaceNotSucceedingReason(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "failed_safe_type_assertion"
  override def message: String = s"The type assertion ${node.origin.tag.trim} might fail. Safe type assertions to interfaces must succeed."
}

case class ComparisonOnIncomparableInterfaces(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "incomparable_error"
  override def message: String = s"Both operands of ${node.origin.tag.trim} might not have comparable values."
}

case class SynthesizedAssertionFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "assertion_error"
  override def message: String = info.comment.reduce[String] { case (l, r) => s"$l; $r" }
}

case class NegativePermissionError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "negative_permission_error"
  override def message: String = s"Expression ${info.origin.tag.trim} might be negative."
}

case class GoCallPreconditionError(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "go_call_precondition_error"
  override def message: String = s"${node.origin.tag.trim} might not satisfy the precondition of the callee."
}

sealed trait VerificationErrorClarification {
  def message: String
  override def toString: String = message
}
