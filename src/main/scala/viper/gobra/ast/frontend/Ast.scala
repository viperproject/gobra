/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.frontend

import java.nio.file.Paths

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import org.bitbucket.inkytonik.kiama.util._
import viper.gobra.ast.frontend.PNode.PPkg
import viper.gobra.reporting.VerifierError
import viper.silver.ast.{LineColumnPosition, SourcePosition}

// TODO: comment describing identifier positions (resolution)


sealed trait PNode extends Product {

  def pretty(prettyPrinter: PrettyPrinter = PNode.defaultPrettyPrinter): String = prettyPrinter.format(this)

  lazy val formatted: String = pretty()

  override def toString: PPkg = formatted
}

object PNode {
  type PPkg = String
  val defaultPrettyPrinter = new DefaultPrettyPrinter
}

sealed trait PScope extends PNode
sealed trait PUnorderedScope extends PScope

case class PProgram(
                     packageClause: PPackageClause,
                     imports: Vector[PImportDecl],
                     declarations: Vector[PMember],
                     positions: PositionManager
                   ) extends PNode with PUnorderedScope


class PositionManager extends PositionStore with Messaging {

  def translate[E <: VerifierError](
                                     messages: Messages,
                                     errorFactory: (String, SourcePosition) => E
                                   ): Vector[VerifierError] = {
    messages.sorted map { m =>
      errorFactory(
        formatMessage(m),
        translate(positions.getStart(m.value).get, positions.getFinish(m.value).get)
      )
    }
  }

  def translate(start: Position, end: Position): SourcePosition = {
    val filename = start.source.asInstanceOf[FileSource].filename
    new SourcePosition(
      Paths.get(filename),
      LineColumnPosition(start.line, start.column),
      Some(LineColumnPosition(end.line, end.column))
    )
  }
}

case class PPackageClause(id: PPkgDef) extends PNode


sealed trait PImportDecl extends PNode {
  def pkg: PPkg
}

case class PQualifiedImport(qualifier: PIdnDef, pkg: PPkg) extends PImportDecl

case class PUnqualifiedImport(pkg: PPkg) extends PImportDecl


sealed trait PGhostifiable extends PNode

sealed trait PMember extends PNode

sealed trait PActualMember extends PMember

sealed trait PGhostifiableMember extends PActualMember with PGhostifiable

sealed trait PCodeRoot extends PNode

sealed trait PCodeRootWithResult extends PCodeRoot {
  def result: PResult
}

case class PConstDecl(typ: Option[PType], right: Vector[PExpression], left: Vector[PIdnDef]) extends PActualMember with PActualStatement with PGhostifiableStatement with PGhostifiableMember

case class PVarDecl(typ: Option[PType], right: Vector[PExpression], left: Vector[PIdnDef], addressable: Vector[Boolean]) extends PActualMember with PActualStatement with PGhostifiableStatement with PGhostifiableMember

case class PFunctionDecl(
                          id: PIdnDef,
                          args: Vector[PParameter],
                          result: PResult,
                          spec: PFunctionSpec,
                          body: Option[PBlock]
                        ) extends PActualMember with PScope with PCodeRootWithResult with PGhostifiableMember

case class PMethodDecl(
                        id: PIdnDef,
                        receiver: PReceiver,
                        args: Vector[PParameter],
                        result: PResult,
                        spec: PFunctionSpec,
                        body: Option[PBlock]
                      ) extends PActualMember with PScope with PCodeRootWithResult with PGhostifiableMember

sealed trait PTypeDecl extends PActualMember with PActualStatement with PGhostifiableStatement with PGhostifiableMember {

  def left: PIdnDef

  def right: PType
}

case class PTypeDef(right: PType, left: PIdnDef) extends PTypeDecl

case class PTypeAlias(right: PType, left: PIdnDef) extends PTypeDecl


/**
  * Statements
  */

sealed trait PStatement extends PNode

sealed trait PActualStatement extends PStatement

sealed trait PGhostifiableStatement extends PActualStatement with PGhostifiable

case class PLabeledStmt(label: PIdnDef, stmt: PStatement) extends PActualStatement


sealed trait PSimpleStmt extends PActualStatement

case class PEmptyStmt() extends PSimpleStmt with PGhostifiableStatement

case class PExpressionStmt(exp: PExpression) extends PSimpleStmt with PGhostifiableStatement

case class PSendStmt(channel: PExpression, msg: PExpression) extends PSimpleStmt

case class PAssignment(right: Vector[PExpression], left: Vector[PAssignee]) extends PSimpleStmt with PGhostifiableStatement

/* Careful: left is only evaluated once */
case class PAssignmentWithOp(right: PExpression, op: PAssOp, left: PAssignee) extends PSimpleStmt with PGhostifiableStatement

sealed trait PAssOp extends PNode

case class PAddOp() extends PAssOp

case class PSubOp() extends PAssOp

case class PMulOp() extends PAssOp

case class PDivOp() extends PAssOp

case class PModOp() extends PAssOp

case class PShortVarDecl(right: Vector[PExpression], left: Vector[PIdnUnk], addressable: Vector[Boolean]) extends PSimpleStmt with PGhostifiableStatement

case class PIfStmt(ifs: Vector[PIfClause], els: Option[PBlock]) extends PActualStatement with PScope with PGhostifiableStatement

case class PIfClause(pre: Option[PSimpleStmt], condition: PExpression, body: PBlock) extends PNode

case class PExprSwitchStmt(pre: Option[PSimpleStmt], exp: PExpression, cases: Vector[PExprSwitchCase], dflt: Vector[PBlock]) extends PActualStatement with PScope with PGhostifiableStatement

sealed trait PExprSwitchClause extends PNode

case class PExprSwitchDflt(body: PBlock) extends PExprSwitchClause

case class PExprSwitchCase(left: Vector[PExpression], body: PBlock) extends PExprSwitchClause

case class PTypeSwitchStmt(pre: Option[PSimpleStmt], exp: PExpression, binder: Option[PIdnDef], cases: Vector[PTypeSwitchCase], dflt: Vector[PBlock]) extends PActualStatement with PScope with PGhostifiableStatement

sealed trait PTypeSwitchClause extends PNode

case class PTypeSwitchDflt(body: PBlock) extends PTypeSwitchClause

case class PTypeSwitchCase(left: Vector[PType], body: PBlock) extends PTypeSwitchClause

case class PForStmt(pre: Option[PSimpleStmt], cond: PExpression, post: Option[PSimpleStmt], spec: PLoopSpec, body: PBlock) extends PActualStatement with PScope with PGhostifiableStatement

case class PAssForRange(range: PRange, ass: Vector[PAssignee], body: PBlock) extends PActualStatement with PScope with PGhostifiableStatement

case class PShortForRange(range: PRange, shorts: Vector[PIdnUnk], body: PBlock) extends PActualStatement with PScope with PGhostifiableStatement

case class PGoStmt(exp: PExpression) extends PActualStatement

case class PSelectStmt(send: Vector[PSelectSend], rec: Vector[PSelectRecv], aRec: Vector[PSelectAssRecv], sRec: Vector[PSelectShortRecv], dflt: Vector[PSelectDflt]) extends PActualStatement with PScope

sealed trait PSelectClause extends PNode

case class PSelectDflt(body: PBlock) extends PSelectClause

case class PSelectSend(send: PSendStmt, body: PBlock) extends PSelectClause

case class PSelectRecv(recv: PReceive, body: PBlock) extends PSelectClause

case class PSelectAssRecv(recv: PReceive, ass: Vector[PAssignee], body: PBlock) extends PSelectClause

case class PSelectShortRecv(recv: PReceive, shorts: Vector[PIdnUnk], body: PBlock) extends PSelectClause

case class PReturn(exps: Vector[PExpression]) extends PActualStatement

case class PBreak(label: Option[PLabelUse]) extends PActualStatement

case class PContinue(label: Option[PLabelUse]) extends PActualStatement

case class PGoto(label: PLabelDef) extends PActualStatement

case class PDeferStmt(exp: PExpression) extends PActualStatement

// case class PFallThrough() extends PStatement


case class PBlock(stmts: Vector[PStatement]) extends PActualStatement with PScope with PGhostifiableStatement

case class PSeq(stmts: Vector[PStatement]) extends PActualStatement with PGhostifiableStatement

/**
  * Expressions
  */


sealed trait PExpression extends PNode

sealed trait PActualExpression extends PExpression




sealed trait PBuildIn extends PActualExpression

sealed trait PAssignee extends PActualExpression

sealed trait PUnaryExp extends PActualExpression {
  def operand: PExpression
}

case class PNamedOperand(id: PIdnUse) extends PActualExpression with PAssignee

sealed trait PLiteral extends PActualExpression

sealed trait PBasicLiteral extends PLiteral

case class PBoolLit(lit: Boolean) extends PBasicLiteral

case class PIntLit(lit: BigInt) extends PBasicLiteral

case class PNilLit() extends PBasicLiteral

// TODO: add other literals

case class PCompositeLit(typ: PLiteralType, lit: PLiteralValue) extends PLiteral

sealed trait PShortCircuitMisc extends PMisc

case class PLiteralValue(elems: Vector[PKeyedElement]) extends PShortCircuitMisc with PActualMisc

case class PKeyedElement(key: Option[PCompositeKey], exp: PCompositeVal) extends PShortCircuitMisc with PActualMisc

sealed trait PCompositeKey extends PNode

case class PIdentifierKey(id: PIdnUse) extends PCompositeKey

sealed trait PCompositeVal extends PCompositeKey with PShortCircuitMisc with PActualMisc

case class PExpCompositeVal(exp: PExpression) extends PCompositeVal // exp is never a named operand as a key

case class PLitCompositeVal(lit: PLiteralValue) extends PCompositeVal

case class PFunctionLit(args: Vector[PParameter], result: PResult, body: PBlock) extends PLiteral with PCodeRootWithResult with PScope

case class PConversionOrUnaryCall(base: PIdnUse, arg: PExpression) extends PActualExpression

case class PConversion(typ: PType, arg: PExpression) extends PActualExpression

case class PCall(callee: PExpression, args: Vector[PExpression]) extends PActualExpression

// TODO: Check Arguments in language specification, also allows preceding type

case class PSelectionOrMethodExpr(base: PIdnUse, id: PIdnUse) extends PActualExpression with PAssignee

case class PMethodExpr(base: PMethodRecvType, id: PIdnUse) extends PActualExpression

case class PSelection(base: PExpression, id: PIdnUse) extends PActualExpression with PAssignee with PAccessible

case class PIndexedExp(base: PExpression, index: PExpression) extends PActualExpression with PAssignee

case class PSliceExp(base: PExpression, low: PExpression, high: PExpression, cap: Option[PExpression] = None) extends PActualExpression

case class PTypeAssertion(base: PExpression, typ: PType) extends PActualExpression

case class PReceive(operand: PExpression) extends PUnaryExp

case class PReference(operand: PExpression) extends PUnaryExp with PAccessible

case class PDereference(operand: PExpression) extends PUnaryExp with PAssignee with PAccessible

case class PNegation(operand: PExpression) extends PUnaryExp

sealed trait PBinaryExp extends PActualExpression {
  def left: PExpression

  def right: PExpression
}

case class PEquals(left: PExpression, right: PExpression) extends PBinaryExp

case class PUnequals(left: PExpression, right: PExpression) extends PBinaryExp

case class PAnd(left: PExpression, right: PExpression) extends PBinaryExp

case class POr(left: PExpression, right: PExpression) extends PBinaryExp

case class PLess(left: PExpression, right: PExpression) extends PBinaryExp

case class PAtMost(left: PExpression, right: PExpression) extends PBinaryExp

case class PGreater(left: PExpression, right: PExpression) extends PBinaryExp

case class PAtLeast(left: PExpression, right: PExpression) extends PBinaryExp

case class PAdd(left: PExpression, right: PExpression) extends PBinaryExp

case class PSub(left: PExpression, right: PExpression) extends PBinaryExp

case class PMul(left: PExpression, right: PExpression) extends PBinaryExp

case class PMod(left: PExpression, right: PExpression) extends PBinaryExp

case class PDiv(left: PExpression, right: PExpression) extends PBinaryExp


sealed trait PActualExprProofAnnotation extends PActualExpression {
  def op: PExpression
}

case class PUnfolding(pred: PPredicateAccess, op: PExpression) extends PActualExprProofAnnotation

/**
  * Types
  */

sealed trait PType extends PNode

sealed trait PActualType extends PType

sealed trait PLiteralType extends PNode

sealed trait PNamedType extends PActualType {
  def name: String
}

case class PDeclaredType(id: PIdnUse) extends PNamedType with PLiteralType {
  override val name: String = id.name
}

sealed abstract class PPredeclaredType(override val name: String) extends PNamedType

case class PBoolType() extends PPredeclaredType("bool")

case class PIntType() extends PPredeclaredType("int")

// TODO: add more types

// TODO: ellipsis type

sealed trait PTypeLit extends PActualType

case class PArrayType(len: PExpression, elem: PType) extends PTypeLit with PLiteralType

case class PImplicitSizeArrayType(elem: PType) extends PLiteralType

case class PSliceType(elem: PType) extends PTypeLit with PLiteralType

case class PMapType(key: PType, elem: PType) extends PTypeLit with PLiteralType

case class PPointerType(base: PType) extends PTypeLit

sealed trait PChannelType extends PTypeLit {
  def elem: PType
}

case class PBiChannelType(elem: PType) extends PChannelType

case class PSendChannelType(elem: PType) extends PChannelType

case class PRecvChannelType(elem: PType) extends PChannelType



case class PStructType(clauses: Vector[PStructClause]) extends PTypeLit with PLiteralType with PUnorderedScope {

  lazy val embedded: Vector[PEmbeddedDecl] = clauses.collect{
    case x: PEmbeddedDecl => x
    case PExplicitGhostStructClause(x: PEmbeddedDecl) => x
  }

  def fields: Vector[PFieldDecl]=  clauses.collect{
    case x: PFieldDecls => x.fields
    case PExplicitGhostStructClause(x: PFieldDecls) => x.fields
  }.flatten
}

sealed trait PStructClause extends PNode

sealed trait PActualStructClause extends PStructClause

// TODO: maybe change to misc
case class PFieldDecls(fields: Vector[PFieldDecl]) extends PActualStructClause

case class PFieldDecl(id: PIdnDef, typ: PType) extends PNode

case class PEmbeddedDecl(typ: PEmbeddedType, id: PIdnDef) extends PActualStructClause {
  require(id.name == typ.name)
}

sealed trait PMethodRecvType extends PActualType {
  def typ: PDeclaredType
}

case class PMethodReceiveName(typ: PDeclaredType) extends PMethodRecvType

case class PMethodReceivePointer(typ: PDeclaredType) extends PMethodRecvType

// TODO: Named type is not allowed to be an interface


case class PFunctionType(args: Vector[PParameter], result: PResult) extends PTypeLit with PScope

case class PInterfaceType(
                           embedded: Vector[PInterfaceName],
                           methSpecs: Vector[PMethodSig],
                           predSpec: Vector[PMPredicateSig]
                         ) extends PTypeLit with PUnorderedScope

sealed trait PInterfaceClause extends PNode

case class PInterfaceName(typ: PDeclaredType) extends PInterfaceClause

// TODO: maybe change to misc
case class PMethodSig(id: PIdnDef, args: Vector[PParameter], result: PResult) extends PInterfaceClause with PScope


/**
  * Identifiers
  */


sealed trait PIdnNode extends PNode {
  def name: String
}

trait PDefLikeId extends PIdnNode
trait PUseLikeId extends PIdnNode

case class PIdnDef(name: String) extends PDefLikeId
case class PIdnUse(name: String) extends PUseLikeId
case class PIdnUnk(name: String) extends PIdnNode


sealed trait PLabelNode extends PNode {
  def name: String
}

trait PDefLikeLabel extends PLabelNode
trait PUseLikeLabel extends PLabelNode

case class PLabelDef(name: String) extends PDefLikeLabel
case class PLabelUse(name: String) extends PUseLikeLabel


sealed trait PPackegeNode extends PNode {
  def name: String
}

trait PDefLikePkg extends PPackegeNode
trait PUseLikePkg extends PPackegeNode

case class PPkgDef(name: String) extends PDefLikePkg
case class PPkgUse(name: String) extends PUseLikePkg


case class PWildcard() extends PDefLikeId with PUseLikeId {
  override def name: String = "_"
}


/**
  * Miscellaneous
  */

sealed trait PMisc extends PNode

sealed trait PActualMisc extends PMisc

case class PRange(exp: PExpression) extends PActualMisc

sealed trait PParameter extends PMisc {
  def typ: PType
}

sealed trait PActualParameter extends PParameter with PActualMisc

case class PNamedParameter(id: PIdnDef, typ: PType, addressable: Boolean) extends PActualParameter

case class PUnnamedParameter(typ: PType) extends PActualParameter

sealed trait PReceiver extends PNode with PActualMisc {
  def typ: PMethodRecvType
}

case class PNamedReceiver(id: PIdnDef, typ: PMethodRecvType, addressable: Boolean) extends PReceiver

case class PUnnamedReceiver(typ: PMethodRecvType) extends PReceiver


sealed trait PResult extends PNode with PActualMisc

case class PVoidResult() extends PResult

case class PResultClause(outs: Vector[PParameter]) extends PResult

sealed trait PEmbeddedType extends PNode with PActualMisc {
  def typ: PNamedType
  def name: String = typ.name
}

case class PEmbeddedName(typ: PNamedType) extends PEmbeddedType

case class PEmbeddedPointer(typ: PNamedType) extends PEmbeddedType


/**
  * Ghost
  */

sealed trait PGhostNode extends PNode

sealed trait PGhostifier[T <: PNode] extends PGhostNode {
  def actual: T
}

object PGhostifier {
  def unapply[T <: PNode](arg: PGhostifier[T]): Option[T] = Some(arg.actual)
}


/**
  * Specification
  */

sealed trait PSpecification extends PGhostNode

case class PFunctionSpec(
                      pres: Vector[PAssertion],
                      posts: Vector[PAssertion],
                      isPure: Boolean = false,
                      ) extends PSpecification


case class PLoopSpec(
                    invariants: Vector[PAssertion]
                    ) extends PSpecification


/**
  * Ghost Member
  */

sealed trait PGhostMember extends PMember with PGhostNode

case class PExplicitGhostMember(actual: PGhostifiableMember) extends PGhostMember with PGhostifier[PGhostifiableMember]

case class PFPredicateDecl(
                         id: PIdnDef,
                         args: Vector[PParameter],
                         body: Option[PAssertion]
                         ) extends PGhostMember with PScope with PCodeRoot

case class PMPredicateDecl(
                          id: PIdnDef,
                          receiver: PReceiver,
                          args: Vector[PParameter],
                          body: Option[PAssertion]
                          ) extends PGhostMember with PScope with PCodeRoot

case class PMPredicateSig(id: PIdnDef, args: Vector[PParameter]) extends PInterfaceClause with PScope

/**
  * Ghost Statement
  */

sealed trait PGhostStatement extends PStatement with PGhostNode

case class PExplicitGhostStatement(actual: PStatement) extends PGhostStatement with PGhostifier[PStatement]

case class PAssert(exp: PAssertion) extends PGhostStatement

case class PAssume(exp: PAssertion) extends PGhostStatement

case class PExhale(exp: PAssertion) extends PGhostStatement

case class PInhale(exp: PAssertion) extends PGhostStatement

case class PFold(exp: PPredicateAccess) extends PGhostStatement

case class PUnfold(exp: PPredicateAccess) extends PGhostStatement

/**
  * Ghost Expression
  */

sealed trait PGhostExpression extends PExpression with PGhostNode

//sealed trait PPermission extends PGhostExpression
//
//case class PFullPerm() extends PPermission
//
//case class PNoPerm() extends PPermission

case class POld(operand: PExpression) extends PGhostExpression

case class PConditional(cond: PExpression, thn: PExpression, els: PExpression) extends PGhostExpression

/**
  * Assertions
  */


sealed trait PAssertion extends PGhostNode

case class PStar(left: PAssertion, right: PAssertion) extends PAssertion

case class PExprAssertion(exp: PExpression) extends PAssertion

sealed trait PPredicateCall extends PAssertion

case class PFPredOrBoolFuncCall(id: PIdnUse, args: Vector[PExpression]) extends PPredicateCall

case class PMPredOrBoolMethCall(recv: PExpression, id: PIdnUse, args: Vector[PExpression]) extends PPredicateCall

case class PMPredOrMethExprCall(base: PMethodRecvType, id: PIdnUse, args: Vector[PExpression]) extends PPredicateCall

case class PMPredOrMethRecvOrExprCall(base: PIdnUse, id: PIdnUse, args: Vector[PExpression]) extends PPredicateCall

case class PMemoryPredicateCall(arg: PExpression) extends PAssertion with PPredicateCall

case class PImplication(left: PExpression, right: PAssertion) extends PAssertion

case class PAccess(exp: PAccessible) extends PAssertion

sealed trait PAccessible extends PGhostNode

case class PPredicateAccess(pred: PPredicateCall) extends PAssertion

/**
  * Types
  */

sealed trait PGhostType extends PType with PGhostNode

/**
  * Miscellaneous
  */

sealed trait PGhostMisc extends PMisc with PGhostNode

case class PExplicitGhostParameter(actual: PActualParameter) extends PParameter with PGhostMisc with PGhostifier[PActualParameter] {
  override def typ: PType = actual.typ
}
// TODO: maybe change to misc
case class PExplicitGhostStructClause(actual: PActualStructClause) extends PStructClause with PGhostNode with PGhostifier[PActualStructClause]

/**
  * Required for parsing
  */

case class PPos[T](get: T) extends PNode
