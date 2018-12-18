/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.parser

import org.bitbucket.inkytonik.kiama.util.Positions
import viper.gobra.ast.parser.PNode.PPkg


sealed trait PNode extends Product

object PNode {
  type PPkg = String

}


case class PProgram(
                     packageClause: PPackageClause,
                     imports: Vector[PImportDecl],
                     declarations: Vector[PMember],
                     positions: Positions
                   ) extends PNode


case class PPackageClause() extends PNode


sealed trait PImportDecl extends PNode {
  def pkg: PPkg
}

case class PQualifiedImport(qualifier: PIdnDef, pkg: PPkg)

case class PUnqualifiedImport(pkg: PPkg)


sealed trait PMember extends PNode

case class PConstDecl(id: PIdnDef, typ: Option[PType], exp: PExpression) extends PMember with PStatement

case class PVarDecl(id: PIdnDef, typ: Option[PType], exp: PExpression) extends PMember with PStatement

case class PFunctionDecl(
                          id: PIdnDef,
                          args: Vector[PParameter],
                          result: PResult,
                          body: Option[PBlock]
                        ) extends PMember

case class PMethodDecl(
                        id: PIdnDef,
                        receiver: PParameter,
                        args: Vector[PParameter],
                        result: PResult,
                        body: Option[PBlock]
                      ) extends PMember

sealed trait PTypeDecl extends PMember with PStatement {

  def id: PIdnDef

  def typ: PType
}

case class PTypeDef(id: PIdnDef, typ: PType) extends PTypeDecl

case class PTypeAlias(id: PIdnDef, typ: PType) extends PTypeDecl


sealed trait PParameter extends PNode

case class PNamedParameter(id: PIdnDef, typ: PType) extends PParameter

case class PUnnamedParameter(typ: PType) extends PParameter


sealed trait PResult extends PNode

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

case class PIfStmt(ifs: Vector[PIfClause], els: Option[PBlock]) extends PStatement

case class PIfClause(pre: Option[PSimpleStmt], condition: PExpression, body: PBlock) extends PNode

case class PExprSwitchStmt(pre: Option[PSimpleStmt], exp: PExpression, cases: Vector[PExprSwitchCase], dflt: Vector[PBlock]) extends PStatement

sealed trait PExprSwitchClause extends PNode

case class PExprSwitchDflt(body: PBlock) extends PExprSwitchClause

case class PExprSwitchCase(left: Vector[PExpression], body: PBlock) extends PExprSwitchClause

case class PTypeSwitchStmt(pre: Option[PSimpleStmt], exp: PExpression, binder: Option[PIdnDef], cases: Vector[PTypeSwitchCase], dflt: Vector[PBlock]) extends PStatement

sealed trait PTypeSwitchClause extends PNode

case class PTypeSwitchDflt(body: PBlock) extends PTypeSwitchClause

case class PTypeSwitchCase(left: Vector[PType], body: PBlock) extends PTypeSwitchClause

case class PWhileStmt(condition: PExpression, body: PBlock) extends PStatement

case class PForStmt(pre: Option[PSimpleStmt], cond: PExpression, post: PSimpleStmt, body: PBlock) extends PStatement

case class PAssForRange(ass: Vector[PAssignee], range: PExpression, body: PBlock) extends PStatement

case class PShortForRange(shorts: Vector[PIdnUnknown], range: PExpression, body: PBlock) extends PStatement

case class PGoStmt(exp: PExpression) extends PStatement

case class PSelectStmt(send: Vector[PSelectSend], aRec: Vector[PSelectAssRecv], sRec: Vector[PSelectShortRecv], dflt: Vector[PSelectDflt]) extends PStatement

sealed trait PSelectClause extends PNode

case class PSelectDflt(body: PBlock) extends PSelectClause

case class PSelectSend(send: PSendStmt, body: PBlock) extends PSelectClause

case class PSelectAssRecv(ass: Vector[PAssignee], recv: PReceive, body: PBlock) extends PSelectClause

case class PSelectShortRecv(shorts: Vector[PIdnUnknown], recv: PReceive, body: PBlock) extends PSelectClause

case class PReturn(exps: Vector[PExpression]) extends PStatement

case class PBreak(label: Option[PIdnUse]) extends PStatement

case class PContinue(label: Option[PIdnUse]) extends PStatement

case class PGoto(label: PIdnUse) extends PStatement

case class PDeferStmt(exp: PExpression) extends PStatement

// case class PFallThrough() extends PStatement


case class PBlock(stmts: Vector[PStatement]) extends PStatement

/**
  * Expressions
  */


sealed trait PExpression extends PNode

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

case class PMethodExpr(base: PMethodRecv, id: PIdnUnqualifiedUse) extends PExpression

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

sealed trait PType extends PNode

sealed trait PLiteralType extends PNode

sealed trait PNamedType extends PType

case class PDeclaredType(id: PIdnUse) extends PNamedType with PLiteralType

sealed trait PPredeclaredType extends PNamedType

case class PBoolType() extends PPredeclaredType

case class PIntType() extends PPredeclaredType

// TODO: add more types

// TODO: ellipsis type

sealed trait PTypeLit extends PType

case class PArrayType(len: PExpression, elem: PType) extends PTypeLit with PLiteralType

case class PImplicitSizeArrayType(elem: PType) extends PLiteralType

case class PSliceType(elem: PType) extends PTypeLit with PLiteralType

case class PMapType(elem: PType, key: PType) extends PTypeLit with PLiteralType

case class PPointerType(base: PType) extends PTypeLit

sealed trait PChannelType extends PTypeLit {
  def elem: PType
}

case class PBiChannelType(elem: PType) extends PChannelType

case class PSendChannelType(elem: PType) extends PChannelType

case class PRecvChannelType(elem: PType) extends PChannelType


case class PStructType(embedded: Vector[PEmbeddedDecl], fields: Vector[PFieldDecl]) extends PTypeLit with PLiteralType

sealed trait PStructClause extends PNode

case class PFieldDecls(fields: Vector[PFieldDecl]) extends PStructClause

case class PFieldDecl(id: PIdnDef, typ: PType) extends PNode

sealed trait PMethodRecv extends PNode

sealed trait PEmbeddedDecl extends PStructClause with PMethodRecv

case class PEmbeddedName(typ: PNamedType) extends PEmbeddedDecl

case class PEmbeddedPointer(base: PNamedType) extends PEmbeddedDecl

// TODO: Named type is not allowed to be an interface


case class PFunctionType(args: Vector[PParameter], result: PResult) extends PTypeLit

case class PInterfaceType(embedded: Vector[PInterfaceName], specs: Vector[PMethodSpec]) extends PTypeLit

sealed trait PInterfaceClause extends PNode

case class PInterfaceName(typ: PNamedType) extends PInterfaceClause

case class PMethodSpec(id: PIdnDef, args: Vector[PParameter], result: PResult) extends PInterfaceClause


/**
  * Identifiers
  */

sealed trait PIdnNode extends PNode {
  def name: String
}

object PIdnNode {
  def isWildcard(id: PIdnNode): Boolean = id.name.equals("_")
}

case class PIdnUnknown(name: String) extends PIdnNode

case class PIdnDef(name: String) extends PIdnNode

sealed trait PIdnUse extends PIdnNode

case class PIdnQualifiedUse(name: String, pkg: PPkg) extends PIdnUse

case class PIdnUnqualifiedUse(name: String) extends PIdnUse

/**
  * Util
  */

case class PPos[T](get: T) extends PNode
