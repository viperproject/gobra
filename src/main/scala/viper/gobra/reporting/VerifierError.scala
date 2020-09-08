/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.reporting

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

case class AssignmentError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "assignment_error"
  override def localMessage: String = "Assignment might fail"
}

case class PostconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "postcondition_error"
  override def localMessage: String = "Postcondition might not hold"
}

case class PreconditionError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "precondition_error"
  override def localMessage: String = "Precondition of call might not hold"
}

case class AssertError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "assert_error"
  override def localMessage: String = "Assert might fail"
}

case class ExhaleError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "exhale_error"
  override def localMessage: String = "Exhale might fail"
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

case class IfError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "conditional_error"
  override def localMessage: String = "Conditional statement might fail"
}

case class ForLoopError(info: Source.Verifier.Info) extends VerificationError {
  override def localId: String = "for_loop_error"
  override def localMessage: String = "For loop statement might fail"
}

sealed trait VerificationErrorReason {
  def id: String
  def message: String
  override def toString: String = message
}

case class InsufficientPermissionError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "permission_error"
  override def message: String = s"permission to ${info.origin.tag} might not suffice"
}

case class AssertionFalseError(info: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "assertion_error"
  override def message: String = s"Assertion ${info.origin.tag} might not hold"
}

case class SeqIndexExceedsLengthError(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "seq_index_exceeds_length_error"
  override def message: String = s"Index ${index.origin.tag.trim} into ${node.origin.tag.trim} might exceed sequence length"
}

case class SeqIndexNegativeError(node: Source.Verifier.Info, index: Source.Verifier.Info) extends VerificationErrorReason {
  override def id: String = "seq_index_negative_error"
  override def message: String = s"Index ${index.origin.tag.trim} into ${node.origin.tag.trim} might be negative"
}

sealed trait VerificationErrorClarification {
  def message: String
  override def toString: String = message
}

