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


sealed trait PNode extends Product

object PNode {
  type PPkg = String

}

sealed trait PScope extends PNode
sealed trait PUnorderedScope extends PScope

sealed trait PUncheckedUse extends PNode

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




sealed trait PMember extends PNode

sealed trait PTopLevel extends PMember

sealed trait PCodeRoot extends PNode

case class PConstDecl(typ: Option[PType], right: Vector[PExpression], left: Vector[PIdnDef]) extends PMember with PStatement

case class PVarDecl(typ: Option[PType], right: Vector[PExpression], left: Vector[PIdnDef]) extends PMember with PStatement

case class PFunctionDecl(
                          id: PIdnDef,
                          args: Vector[PParameter],
                          result: PResult,
                          body: Option[PBlock]
                        ) extends PMember with PTopLevel with PScope with PCodeRoot

case class PMethodDecl(
                        id: PIdnDef,
                        receiver: PReceiver,
                        args: Vector[PParameter],
                        result: PResult,
                        body: Option[PBlock]
                      ) extends PMember with PTopLevel with PScope with PCodeRoot

sealed trait PTypeDecl extends PMember with PStatement {

  def left: PIdnDef

  def right: PType
}

case class PTypeDef(right: PType, left: PIdnDef) extends PTypeDecl

case class PTypeAlias(right: PType, left: PIdnDef) extends PTypeDecl


/**
  * Statements
  */

sealed trait PStatement extends PNode

case class PLabeledStmt(label: PIdnDef, stmt: PStatement) extends PStatement


sealed trait PSimpleStmt extends PStatement

case class PEmptyStmt() extends PSimpleStmt

case class PExpressionStmt(exp: PExpression) extends PSimpleStmt

case class PSendStmt(channel: PExpression, msg: PExpression) extends PSimpleStmt

case class PAssignment(left: Vector[PAssignee], right: Vector[PExpression]) extends PSimpleStmt

/* Careful: left is only evaluated once */
case class PAssignmentWithOp(left: PAssignee, op: PAssOp, right: PExpression) extends PSimpleStmt

sealed trait PAssOp extends PNode

case class PAddOp() extends PAssOp

case class PSubOp() extends PAssOp

case class PMulOp() extends PAssOp

case class PDivOp() extends PAssOp

case class PModOp() extends PAssOp

case class PShortVarDecl(right: Vector[PExpression], left: Vector[PIdnUnk]) extends PSimpleStmt

case class PIfStmt(ifs: Vector[PIfClause], els: Option[PBlock]) extends PStatement with PScope

case class PIfClause(pre: Option[PSimpleStmt], condition: PExpression, body: PBlock) extends PNode

case class PExprSwitchStmt(pre: Option[PSimpleStmt], exp: PExpression, cases: Vector[PExprSwitchCase], dflt: Vector[PBlock]) extends PStatement with PScope

sealed trait PExprSwitchClause extends PNode

case class PExprSwitchDflt(body: PBlock) extends PExprSwitchClause

case class PExprSwitchCase(left: Vector[PExpression], body: PBlock) extends PExprSwitchClause

case class PTypeSwitchStmt(pre: Option[PSimpleStmt], exp: PExpression, binder: Option[PIdnDef], cases: Vector[PTypeSwitchCase], dflt: Vector[PBlock]) extends PStatement with PScope

sealed trait PTypeSwitchClause extends PNode

case class PTypeSwitchDflt(body: PBlock) extends PTypeSwitchClause

case class PTypeSwitchCase(left: Vector[PType], body: PBlock) extends PTypeSwitchClause

case class PForStmt(pre: Option[PSimpleStmt], cond: PExpression, post: Option[PSimpleStmt], body: PBlock) extends PStatement with PScope

case class PAssForRange(range: PRange, ass: Vector[PAssignee], body: PBlock) extends PStatement with PScope

case class PShortForRange(range: PRange, shorts: Vector[PIdnUnk], body: PBlock) extends PStatement with PScope

case class PGoStmt(exp: PExpression) extends PStatement

case class PSelectStmt(send: Vector[PSelectSend], rec: Vector[PSelectRecv], aRec: Vector[PSelectAssRecv], sRec: Vector[PSelectShortRecv], dflt: Vector[PSelectDflt]) extends PStatement with PScope

sealed trait PSelectClause extends PNode

case class PSelectDflt(body: PBlock) extends PSelectClause

case class PSelectSend(send: PSendStmt, body: PBlock) extends PSelectClause

case class PSelectRecv(recv: PReceive, body: PBlock) extends PSelectClause

case class PSelectAssRecv(ass: Vector[PAssignee], recv: PReceive, body: PBlock) extends PSelectClause

case class PSelectShortRecv(recv: PReceive, shorts: Vector[PIdnUnk], body: PBlock) extends PSelectClause

case class PReturn(exps: Vector[PExpression]) extends PStatement

case class PBreak(label: Option[PLabelUse]) extends PStatement

case class PContinue(label: Option[PLabelUse]) extends PStatement

case class PGoto(label: PLabelDef) extends PStatement

case class PDeferStmt(exp: PExpression) extends PStatement

// case class PFallThrough() extends PStatement


case class PBlock(stmts: Vector[PStatement]) extends PStatement with PScope

case class PSeq(stmts: Vector[PStatement]) extends PStatement

/**
  * Expressions
  */


sealed trait PExpression extends PNode

sealed trait PBuildIn extends PExpression

sealed trait PAssignee extends PExpression

sealed trait PUnaryExp extends PExpression {
  def operand: PExpression
}

case class PNamedOperand(id: PIdnUse) extends PExpression with PAssignee

sealed trait PLiteral extends PExpression

sealed trait PBasicLiteral extends PLiteral

case class PBoolLit(lit: Boolean) extends PBasicLiteral

case class PIntLit(lit: BigInt) extends PBasicLiteral

case class PNilLit() extends PBasicLiteral

// TODO: add other literals

case class PCompositeLit(typ: PLiteralType, lit: PLiteralValue) extends PLiteral

case class PLiteralValue(elems: Vector[PKeyedElement]) extends PNode

case class PKeyedElement(key: Option[PCompositeKey], exp: PCompositeVal) extends PNode

sealed trait PCompositeKey extends PNode

case class PIdentifierKey(id: PIdnUse) extends PCompositeKey with PUncheckedUse

sealed trait PCompositeVal extends PCompositeKey

case class PExpCompositeVal(exp: PExpression) extends PCompositeVal // exp is never a named operand

case class PLitCompositeVal(lit: PLiteralValue) extends PCompositeVal

case class PFunctionLit(args: Vector[PParameter], result: PResult, body: PBlock) extends PLiteral with PCodeRoot with PScope

case class PConversionOrUnaryCall(base: PIdnUse, arg: PExpression) extends PExpression

case class PConversion(typ: PType, arg: PExpression) extends PExpression

case class PCall(callee: PExpression, args: Vector[PExpression]) extends PExpression

// TODO: Check Arguments in language specification, also allows preceding type

case class PSelectionOrMethodExpr(base: PIdnUse, id: PIdnUse) extends PExpression with PAssignee

case class PMethodExpr(base: PMethodRecvType, id: PIdnUse) extends PExpression

case class PSelection(base: PExpression, id: PIdnUse) extends PExpression with PAssignee

case class PIndexedExp(base: PExpression, index: PExpression) extends PExpression with PAssignee

case class PSliceExp(base: PExpression, low: PExpression, high: PExpression, cap: Option[PExpression] = None) extends PExpression

case class PTypeAssertion(base: PExpression, typ: PType) extends PExpression

case class PReceive(operand: PExpression) extends PUnaryExp

case class PReference(operand: PExpression) extends PUnaryExp

case class PDereference(operand: PExpression) extends PUnaryExp with PAssignee with PAccessible

case class PNegation(operand: PExpression) extends PUnaryExp

sealed trait PBinaryExp extends PExpression {
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


/**
  * Types
  */

sealed trait PType extends PNode

sealed trait PLiteralType extends PNode

sealed trait PNamedType extends PType {
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

sealed trait PTypeLit extends PType

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
  lazy val embedded: Vector[PEmbeddedDecl] = clauses.collect{ case x: PEmbeddedDecl => x }
  def fields: Vector[PFieldDecl]=  clauses.collect{ case x: PFieldDecls => x.fields }.flatten
}

sealed trait PStructClause extends PNode

case class PFieldDecls(fields: Vector[PFieldDecl]) extends PStructClause

case class PFieldDecl(id: PIdnDef, typ: PType) extends PNode

case class PEmbeddedDecl(typ: PEmbeddedType, id: PIdnDef) extends PStructClause {
  require(id.name == typ.name)
}

sealed trait PMethodRecvType extends PType {
  def typ: PDeclaredType
}

case class PMethodReceiveName(typ: PDeclaredType) extends PMethodRecvType

case class PMethodReceivePointer(typ: PDeclaredType) extends PMethodRecvType

// TODO: Named type is not allowed to be an interface


case class PFunctionType(args: Vector[PParameter], result: PResult) extends PTypeLit with PScope

case class PInterfaceType(embedded: Vector[PInterfaceName], specs: Vector[PMethodSig]) extends PTypeLit with PUnorderedScope

sealed trait PInterfaceClause extends PNode

case class PInterfaceName(typ: PDeclaredType) extends PInterfaceClause

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

case class PRange(exp: PExpression) extends PMisc

sealed trait PParameter extends PNode with PMisc {
  def typ: PType
}

case class PNamedParameter(id: PIdnDef, typ: PType) extends PParameter

case class PUnnamedParameter(typ: PType) extends PParameter

sealed trait PReceiver extends PNode with PMisc {
  def typ: PMethodRecvType
}

case class PNamedReceiver(id: PIdnDef, typ: PMethodRecvType) extends PReceiver

case class PUnnamedReceiver(typ: PMethodRecvType) extends PReceiver


sealed trait PResult extends PNode with PMisc

case class PVoidResult() extends PResult

case class PResultClause(outs: Vector[PParameter]) extends PResult

sealed trait PEmbeddedType extends PNode with PMisc {
  def typ: PNamedType
  def name: String = typ.name
}

case class PEmbeddedName(typ: PNamedType) extends PEmbeddedType

case class PEmbeddedPointer(typ: PNamedType) extends PEmbeddedType

/**
  * Ghost
  */

sealed trait PGhostNode extends PNode



/**
  * Specification
  */

sealed trait PSpecification extends PGhostNode

case class PMethodSpec() extends PSpecification

case class PFunctionSpec() extends PSpecification


/**
  * Ghost Member
  */

sealed trait PGhostMember extends PMember with PGhostNode

/**
  * Ghost Statement
  */

sealed trait PGhostStatement extends PStatement with PGhostNode

case class PAssert(exp: PAssertion) extends PGhostStatement

case class PAssume(exp: PAssertion) extends PGhostStatement

case class PExhale(exp: PAssertion) extends PGhostStatement

case class PInhale(exp: PAssertion) extends PGhostStatement

/**
  * Ghost Expression
  */

sealed trait PGhostExpression extends PExpression with PGhostNode

sealed trait PPermission extends PGhostExpression

case class PFullPerm() extends PPermission

case class PNoPerm() extends PPermission

/**
  * Assertions
  */


sealed trait PAssertion extends PGhostNode

case class PStar(left: PAssertion, right: PAssertion) extends PAssertion

case class PExprAssertion(exp: PExpression) extends PAssertion

case class PImplication(left: PExpression, right: PAssertion) extends PAssertion



case class PAccess(exp: PAccessible) extends PAssertion

sealed trait PAccessible extends PGhostNode



case class PPos[T](get: T) extends PNode
