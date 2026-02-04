// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting

import viper.gobra.ast.frontend
import viper.gobra.ast.frontend.{PReceive, PSendStmt}
import viper.gobra.reporting.Source.Verifier
import viper.gobra.util.Constants
import viper.gobra.util.Violation.violation
import viper.silver.ast.{SourcePosition}

sealed trait VerifierError {
  def position: Option[SourcePosition]
  def message: String
  def id: String

  def formattedMessage: String = position match {
    case Some(pos) => s"<${pos.file.toString}:${pos.line}:${pos.column}> $message"
    case _ => message
  }

  override def toString: String = formattedMessage

  var cached: Boolean = false
}

case class NotFoundError(message: String) extends VerifierError {
  val position: Option[SourcePosition] = None
  val id = "not_found_error"
}

case class ConfigError(message: String) extends VerifierError {
  val position: Option[SourcePosition] = None
  val id = "config_error"
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

case class TimeoutError(message: String) extends VerifierError {
  val position: Option[SourcePosition] = None
  val id = "timeout_error"
}

case class ConsistencyError(message: String, position: Option[SourcePosition]) extends VerifierError {
  val id = "consistency_error"
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

case class LoadError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "load_error"
  override def localMessage: String = "Reading might fail"
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

case class RefuteError(info: Source.Verifier.Info) extends VerificationError {

  override def localId: String = "refute_error"

  override def localMessage: String = "Refute statement failed. Assertion is either unreachable or it always holds"
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

case class MagicWandNotWellformedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "wand_not_wellformed"
  override def localMessage: String = "Magic wand might not be well-formed."
}

case class MethodContractNotWellFormedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "contract_not_well_formed"
  override def localMessage: String = "Method contract is not well-formed"
}

case class PackageFailedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "package_failed"
  override def localMessage: String = "Packaging wand might fail"
}

case class ApplyFailed(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "apply_failed"
  override def localMessage: String = "Applying wand might fail"
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
  override def localMessage: String = "Contract is not well-formed"
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

case class MainPreconditionNotEstablished(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "main_pre_error"
  override def localMessage: String =
    s"The precondition of the function ${Constants.MAIN_FUNC_NAME} might not be established by the initialization code"
}

case class ImportPreconditionNotEstablished(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "import_pre_error"
  override def localMessage: String =
    s"The import precondition might not be established by the initialization code of the imported package"
}

case class ArrayMakePreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "make_precondition_error"
  override def localMessage: String = s"The provided length might not be smaller or equals to the provided capacity, or length and capacity might not be non-negative"
}

case class ChannelMakePreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "make_precondition_error"
  override def localMessage: String = s"The provided length to ${info.origin.tag.trim} might be negative"
}

case class MatchError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "match_error"
  override def localMessage: String = s"The patterns might not match the expression"
}

case class RangeVariableMightNotExistError(info: Source.Verifier.Info)(rangeExpr: String) extends VerificationError {
  override def localId: String = "range_variable_might_not_exist"
  override def localMessage: String = s"Length of range expression '$rangeExpr' might be 0"
}

case class NoPermissionToRangeExpressionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "no_permission_to_range_expression"
  override def localMessage: String = s"Might not have read permission to range expression"
}

case class InsufficientPermissionToRangeExpressionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "insufficient_permission_to_range_expression"
  override def localMessage: String = s"Range expression should be immutable inside the loop body"
}

case class MapMakePreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "make_precondition_error"
  override def localMessage: String = s"The provided length to ${info.origin.tag.trim} might be negative"
}

case class ShiftPreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "shift_precondition_error"
  override def localMessage: String = s"The shift count in ${info.origin.tag.trim} might be negative"
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

case class MethodObjectGetterPreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "method_object_nil_error"
  override def localMessage: String = s"The receiver of ${info.origin.tag} might be nil"
}

case class SpecImplementationPostconditionError(info: Source.Verifier.Info, specName: String) extends VerificationError {
  override def localId: String = "spec_implementation_post_error"
  override def localMessage: String = s"Postcondition of spec $specName might not hold"
}

case class ChannelReceiveError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "receive_error"
  override def localMessage: String = s"The receive expression ${info.trySrc[PReceive](" ")}might fail"
}

case class ChannelSendError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "send_error"
  override def localMessage: String = s"The send expression ${info.trySrc[PSendStmt](" ")}might fail"
}

case class PredicateInstanceNoAccessError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "predicate_instance_no_access_error"
  override def localMessage: String = "Accessing predicate instance might fail"
}

case class FunctionTerminationError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "pure_function_termination_error"
  override def localMessage: String = s"Pure function might not terminate"
}

case class MethodTerminationError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "function_termination_error"
  override def localMessage: String = s"Function might not terminate"
}

case class LoopTerminationError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "loop_termination_error"
  override def localMessage: String = s"The loop ${info.origin.tag.trim} might not terminate"
}

case class SIFGotoError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "sif_goto_error"
  override def localMessage: String = s"The side conditions for the goto statement ${info.origin.tag.trim} caused by verifying hyper properties might not hold"
}

case class IsInvariantFailedError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "is_invariant_failed"
  override def localMessage: String = s"${info.origin.tag.trim} might not be an invariant"
}

case class InvariantMightBeOpenError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "invariant_already_open"
  override def localMessage: String = s"Invariant ${info.origin.tag.trim} might already be open"
}

case class InvariantNotRestoredError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "invariant_not_restored"
  override def localMessage: String = s"Invariant ${info.origin.tag.trim} might not have been restored"
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
  override def message: String = s"Permission to ${info.origin.tag.trim} might not suffice."
}

case class InsufficientPermissionFromTagError(tag: String) extends VerificationErrorReason {
  override def id: String = "permission_error"
  override def message: String = s"Permission to $tag might not suffice."
}

case class AssertionFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "assertion_error"
  override def message: String = s"Assertion ${info.origin.tag.trim} might not hold."
}

case class RefutationTrueError(info: Source.Verifier.Info) extends VerificationErrorReason {

  override def id: String = "refutation_true_error"

  override def message: String = s"Assertion ${info.origin.tag.trim} definitely holds."
}

case class SeqIndexExceedsLengthError(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "seq_index_exceeds_length_error"
  override def message: String = s"Index ${index.origin.tag.trim} into ${node.origin.tag.trim} might exceed sequence length."
}

case class SeqIndexNegativeError(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "seq_index_negative_error"
  override def message: String = s"Index ${index.origin.tag.trim} into ${node.origin.tag.trim} might be negative."
}

case class MapKeyNotContained(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "map_key_not_contained"
  override def message: String = s"Key ${index.origin.tag.trim} might not be contained in ${node.origin.tag.trim}."
}

case class KeyNotComparableReason(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "key_not_comparable_reason"
  override def message: String = s"Key in ${info.origin.tag.trim} is not comparable."
}

case class RepeatedMapKeyReason(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "repeated_map_key_reason"
  override def message: String = s"Map literal ${info.origin.tag.trim} might contain the key twice."
}

// João, 06/03/2021: unlike the other subtypes of VerificationErrorReason, DivisionByZeroReason has an Optional argument.
// This has to do with the fact that, in our tests, there are cases where a division by zero occurs but we cannot retrieve
// a corresponding Source.Verifier.Info. E.g. src/test/resources/regressions/features/fractional_permissions/fields/fail3.gobra
case class DivisionByZeroReason(node: Option[Source.Verifier.Info]) extends VerificationErrorReason {
  override def id: String = "division_by_zero"
  override def message: String = s"Divisor ${node.map(_.origin.tag.trim).getOrElse("expression")} might be zero."
}

case class OverflowErrorReason(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "integer_overflow_error"
  override def message: String = s"Expression ${node.origin.tag.trim} might cause integer overflow."
}

case class InterfaceReceiverIsNilReason(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "receiver_is_nil_error"
  override def message: String = s"The receiver might be nil"
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

case class SynthesizedAssertionFalseReason(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "assertion_error"
  override def message: String = info.comment.reduce[String] { case (l, r) => s"$l; $r" }
}

case class MagicWandChunkNotFound(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "wand.not.found"
  override def message: String = "Magic wand instance not found."
}

case class NegativePermissionReason(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "negative_permission_error"
  override def message: String = s"Expression ${info.origin.tag.trim} might be negative."
}

case class QPAssertionNotInjective(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "qp_assertion_not_injective"
  override def message: String = s"Quantified resource ${info.origin.tag.trim} might not be injective."
}

case class GoCallPreconditionReason(node: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "go_call_precondition_error"
  override def message: String = s"${node.origin.tag.trim} might not satisfy the precondition of the callee."
}

case class TerminationConditionFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "termination_condition_false_error"
  override def message: String = s"Required termination condition might not hold."
}

case class ImplMeasureHigherThanInterfaceReason(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "term_measure_impl_higher_than_interface"
  override def message: String = s"The termination measure of this method might exceed the termination measure of the corresponding interface method."
}

case class TupleConditionFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "tuple_condition_false_error"
  override def message: String = s"Required tuple condition might not hold."
}

case class TupleSimpleFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "tuple_simple_false_error"
  override def message: String = s"Termination measure might not decrease or might not be bounded."
}

case class TupleDecreasesFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "tuple_decreases_false_error"
  override def message: String = s"Termination measure might not decrease."
}

case class TupleBoundedFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "tuple_bounded_false_error"
  override def message: String = s"Termination measure might not be bounded."
}

case class LabelledStateNotReached(info: Source.Verifier.Info) extends VerificationErrorReason  {
  override def id: String = "labelled_state_not_reached"
  override def message: String = s"Did not reach labelled state required to evaluate ${info.origin.tag.trim}"
}

case class SpecNotImplementedByClosure(info: Verifier.Info, closure: String, spec: String) extends VerificationErrorReason {
  override def id = "spec_not_implemented"
  override def message: String = s"$closure might not implement $spec."
}

case class SIFGotoNotLowEvent(info: Verifier.Info) extends VerificationErrorReason {
  override def id: String = "sif_goto_not_low_event"
  override def message: String = s"${info.origin.tag.trim} might not be executed by both executions."
}

sealed trait VerificationErrorClarification {
  def message: String
  override def toString: String = message
}
