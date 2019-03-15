/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.parser

import java.nio.file.Paths

import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import org.bitbucket.inkytonik.kiama.util._
import viper.gobra.ast.parser.PNode.PPkg
import viper.gobra.reporting.VerifierError
import viper.silver.ast.{LineColumnPosition, SourcePosition}


sealed trait PNode extends Product

object PNode {
  type PPkg = PIdnPackage

}

sealed trait PScope extends PNode

case class PProgram(
                     packageClause: PPackageClause,
                     imports: Vector[PImportDecl],
                     declarations: Vector[PMember],
                     positions: PositionManager
                   ) extends PNode with PScope


class PositionManager extends PositionStore with Messaging {

  def translate[E <: VerifierError](
                                     messages: Messages,
                                     errorFactory: (String, SourcePosition) => E
                                   ): Vector[VerifierError] = {
    messages.sorted map { m =>
      errorFactory(formatMessage(m), translate(positions.getStart(m.value).get))
    }
  }

  def translate(position: Position): SourcePosition = {
    val filename = position.source.asInstanceOf[FileSource].filename
    new SourcePosition(
      Paths.get(filename),
      LineColumnPosition(position.line, position.column),
      None
    )
  }
}

case class PPackageClause(id: PIdnDef) extends PNode


sealed trait PImportDecl extends PNode {
  def pkg: PPkg
}

case class PQualifiedImport(qualifier: PIdnDef, pkg: PPkg) extends PImportDecl

case class PUnqualifiedImport(pkg: PPkg) extends PImportDecl


sealed trait PMember extends PNode

sealed trait PLocalDeclaration extends PNode

sealed trait NonExpression extends PNode

case class PConstDecl(left: Vector[PIdnDef], typ: Option[PType], right: Vector[PExpression]) extends PMember with PStatement with PLocalDeclaration

case class PVarDecl(left: Vector[PIdnDef], typ: Option[PType], right: Vector[PExpression]) extends PMember with PStatement with PLocalDeclaration

case class PFunctionDecl(
                          id: PIdnDef,
                          args: Vector[PParameter],
                          result: PResult,
                          body: Option[PBlock]
                        ) extends PMember with PScope with PLocalDeclaration

case class PMethodDecl(
                        id: PIdnDef,
                        receiver: PReceiver,
                        args: Vector[PParameter],
                        result: PResult,
                        body: Option[PBlock]
                      ) extends PMember with PScope

sealed trait PTypeDecl extends PMember with PStatement with PLocalDeclaration {

  def left: PIdnDef

  def right: PType
}

case class PTypeDef(left: PIdnDef, right: PType) extends PTypeDecl

case class PTypeAlias(left: PIdnDef, right: PType) extends PTypeDecl


sealed trait PParameter extends PNode with NonExpression {
  def typ: PType
}

case class PNamedParameter(id: PIdnDef, typ: PType) extends PParameter

case class PUnnamedParameter(typ: PType) extends PParameter

sealed trait PReceiver extends PNode with NonExpression {
  def typ: PMethodRecvType
}

case class PNamedReceiver(id: PIdnDef, typ: PMethodRecvType) extends PReceiver

case class PUnnamedReceiver(typ: PMethodRecvType) extends PReceiver


sealed trait PResult extends PNode with NonExpression

case class PVoidResult() extends PResult

case class PResultClause(outs: Vector[PParameter]) extends PResult

/**
  * Statements
  */

sealed trait PStatement extends PNode

case class PLabeledStmt(label: PIdnDef, stmt: PStatement)


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

case class PShortVarDecl(left: Vector[PIdnUnknown], right: Vector[PExpression]) extends PSimpleStmt

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

case class PAssForRange(ass: Vector[PAssignee], range: PExpression, body: PBlock) extends PStatement with PScope

case class PShortForRange(shorts: Vector[PIdnUnknown], range: PExpression, body: PBlock) extends PStatement with PScope

case class PGoStmt(exp: PExpression) extends PStatement

case class PSelectStmt(send: Vector[PSelectSend], rec: Vector[PSelectRecv], aRec: Vector[PSelectAssRecv], sRec: Vector[PSelectShortRecv], dflt: Vector[PSelectDflt]) extends PStatement with PScope

sealed trait PSelectClause extends PNode

case class PSelectDflt(body: PBlock) extends PSelectClause

case class PSelectSend(send: PSendStmt, body: PBlock) extends PSelectClause

case class PSelectRecv(recv: PReceive, body: PBlock) extends PSelectClause

case class PSelectAssRecv(ass: Vector[PAssignee], recv: PReceive, body: PBlock) extends PSelectClause

case class PSelectShortRecv(shorts: Vector[PIdnUnknown], recv: PReceive, body: PBlock) extends PSelectClause

case class PReturn(exps: Vector[PExpression]) extends PStatement

case class PBreak(label: Option[PIdnUse]) extends PStatement

case class PContinue(label: Option[PIdnUse]) extends PStatement

case class PGoto(label: PIdnUse) extends PStatement

case class PDeferStmt(exp: PExpression) extends PStatement

// case class PFallThrough() extends PStatement


case class PBlock(stmts: Vector[PStatement]) extends PStatement with PScope

case class PSeq(stmts: Vector[PStatement]) extends PStatement

/**
  * Expressions
  */


sealed trait PExpression extends PNode with Typable

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

case class PLiteralValue(elems: Vector[(PKeyedElement)]) extends PNode

case class PKeyedElement(key: Option[PCompositeVal], exp: PCompositeVal) extends PNode

sealed trait PCompositeVal extends PNode

case class PExpCompositeVal(exp: PExpression) extends PCompositeVal

case class PLitCompositeVal(lit: PLiteralValue) extends PCompositeVal

case class PFunctionLit(args: Vector[PParameter], result: PResult, body: PBlock) extends PLiteral

case class PConversionOrUnaryCall(base: PIdnUse, arg: PExpression) extends PExpression

case class PConversion(typ: PType, arg: PExpression) extends PExpression

case class PCall(callee: PExpression, args: Vector[PExpression]) extends PExpression

// TODO: Check Arguments in language specification, also allows preceding type

case class PSelectionOrMethodExpr(base: PIdnUse, id: PIdnUnqualifiedUse) extends PExpression with PAssignee

case class PMethodExpr(base: PMethodRecvType, id: PIdnUnqualifiedUse) extends PExpression

case class PSelection(base: PExpression, id: PIdnUnqualifiedUse) extends PExpression with PAssignee

case class PIndexedExp(base: PExpression, index: PExpression) extends PExpression with PAssignee

case class PSliceExp(base: PExpression, low: PExpression, high: PExpression, cap: Option[PExpression] = None) extends PExpression

case class PTypeAssertion(base: PExpression, typ: PType) extends PExpression

case class PReceive(operand: PExpression) extends PUnaryExp

case class PReference(operand: PExpression) extends PUnaryExp

case class PDereference(operand: PExpression) extends PUnaryExp with PAssignee

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

sealed trait PType extends PNode with Typable

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



case class PStructType(embedded: Vector[PEmbeddedDecl], fields: Vector[PFieldDecl]) extends PTypeLit with PLiteralType with PScope

sealed trait PStructClause extends PNode

case class PFieldDecls(fields: Vector[PFieldDecl]) extends PStructClause

case class PFieldDecl(id: PIdnDef, typ: PType) extends PNode

case class PEmbeddedDecl(typ: PEmbeddedType) extends PStructClause

sealed trait PEmbeddedType extends PNode with PDefLike with NonExpression {
  def typ: PNamedType
  def name: String = typ.name
}

case class PEmbeddedName(typ: PNamedType) extends PEmbeddedType

case class PEmbeddedPointer(typ: PNamedType) extends PEmbeddedType

sealed trait PMethodRecvType extends PType {
  def typ: PDeclaredType
}

case class PMethodReceiveName(typ: PDeclaredType) extends PMethodRecvType

case class PMethodReceivePointer(typ: PDeclaredType) extends PMethodRecvType

// TODO: Named type is not allowed to be an interface


case class PFunctionType(args: Vector[PParameter], result: PResult) extends PTypeLit with PScope

case class PInterfaceType(embedded: Vector[PInterfaceName], specs: Vector[PMethodSpec]) extends PTypeLit with PScope

sealed trait PInterfaceClause extends PNode

case class PInterfaceName(typ: PDeclaredType) extends PInterfaceClause

case class PMethodSpec(id: PIdnDef, args: Vector[PParameter], result: PResult) extends PInterfaceClause with PScope


/**
  * Identifiers
  */

sealed trait PIdnNode extends PNode with Typable {
  def name: String
}

object PIdnNode {
  def isWildcard(id: PIdnNode): Boolean = id.name.equals("_")
}

trait PDefLike extends PIdnNode

trait PUseLike extends PIdnNode

case class PIdnUnknown(name: String) extends PIdnNode with PDefLike with PUseLike

case class PIdnDef(name: String) extends PIdnNode with PDefLike

sealed trait PIdnUse extends PIdnNode with PUseLike

case class PIdnQualifiedUse(name: String, pkg: PPkg) extends PIdnUse

case class PIdnUnqualifiedUse(name: String) extends PIdnUse

case class PIdnPackage(name: String) extends PIdnNode {
  def ref: String = ???
}

/**
  * Util
  */

case class PPos[T](get: T) extends PNode

sealed trait Typable extends PNode
