/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.ast.internal

/**
  * When adding a new node:
  * - extend @see [[viper.gobra.ast.internal.utility.Nodes.subnodes]]
  * - extend @see [[viper.gobra.ast.internal.utility.GobraStrategy.gobraDuplicator]]
  * - extend @see [[DefaultPrettyPrinter.show]]
  * - extend desugar
  * - extend translator
  */

import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.Parser

case class Program(
                    types: Vector[TopType],
                    variables: Vector[GlobalVarDecl],
                    constants: Vector[GlobalConst],
                    methods: Vector[Method],
                    functions: Vector[Function]
                  )(val info: Source.Parser.Info) extends Node {

}

sealed trait GlobalVarDecl extends Node

//case class SingleGlobalVarDecl(left: GlobalVar, right: Expr)

//case class MultiGlobalVarDecl(lefts: Vector[GlobalVar], right: Expr)

sealed trait GlobalConst extends Node


case class Field(name: String, typ: Type, isEmbedding: Boolean)(val info: Source.Parser.Info) extends Node



case class Method(
                 receiver: Parameter,
                 name: String,
                 args: Vector[Parameter],
                 results: Vector[LocalVar.Val],
                 pres: Vector[Assertion],
                 posts: Vector[Assertion],
                 body: Option[Block]
                 )(val info: Source.Parser.Info) extends Node

case class Function(
                     name: String,
                     args: Vector[Parameter],
                     results: Vector[LocalVar.Val],
                     pres: Vector[Assertion],
                     posts: Vector[Assertion],
                     body: Option[Block]
                   )(val info: Source.Parser.Info) extends Node


sealed trait Stmt extends Node

sealed trait Declaration extends Node

sealed trait TopDeclaration extends Declaration

sealed trait BottomDeclaration extends Declaration

case class Block(
                  decls: Vector[BottomDeclaration],
                  stmts: Vector[Stmt]
                )(val info: Source.Parser.Info) extends Stmt

case class Seqn(stmts: Vector[Stmt])(val info: Source.Parser.Info) extends Stmt

case class If(cond: Expr, thn: Stmt, els: Stmt)(val info: Source.Parser.Info) extends Stmt

case class While(cond: Expr, invs: Vector[Assertion], body: Stmt)(val info: Source.Parser.Info) extends Stmt

sealed trait Assignment extends Stmt

case class NewComposite(target: LocalVar.Val, typ: Composite)(val info: Source.Parser.Info) extends Stmt

sealed trait Composite

object Composite {
  case class RefStruct(op: RefStructT) extends Composite
  case class ValStruct(op: ValStructT) extends Composite
  case class Defined(name: String, op: Composite) extends Composite {

  }
}

case class SingleAss(left: Assignee, right: Expr)(val info: Source.Parser.Info) extends Assignment

sealed trait Assignee extends Node {
  def op: Expr
  override def info: Parser.Info = op.info
}

object Assignee {
  case class Var(op: BodyVar) extends Assignee
  case class Pointer(op: Deref) extends Assignee
  case class Field(op: FieldRef) extends Assignee
  // TODO: Index
}

case class FunctionCall(targets: Vector[LocalVar.Val], func: FunctionProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt
case class MethodCall(targets: Vector[LocalVar.Val], recv: Expr, func: FunctionProxy, args: Vector[Expr], path: MemberPath)(val info: Source.Parser.Info) extends Stmt

case class Return()(val info: Source.Parser.Info) extends Stmt

case class Assert(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Assume(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Inhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Exhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt


sealed trait Assertion extends Node

case class SepAnd(left: Assertion, right: Assertion)(val info: Source.Parser.Info) extends Assertion

case class ExprAssertion(exp: Expr)(val info: Source.Parser.Info) extends Assertion

case class Implication(left: Expr, right: Assertion)(val info: Source.Parser.Info) extends Assertion

case class Access(e: Accessible)(val info: Source.Parser.Info) extends Assertion

sealed trait Accessible extends Node {
  def op: Node
  override def info: Parser.Info = op.info
}

object Accessible {
  case class Ref(op: Deref) extends Accessible
  case class Field(op: FieldRef) extends Accessible
}

sealed trait Expr extends Node with Typed

// case class FieldAccess() extends Expr

case class DfltVal(typ: Type)(val info: Source.Parser.Info) extends Expr

case class Tuple(args: Vector[Expr])(val info: Source.Parser.Info) extends Expr {
  lazy val typ = TupleT(args map (_.typ)) // TODO: remove redundant typ information of other nodes
}

case class Deref(exp: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr {
  require(exp.typ.isInstanceOf[PointerT])
}

case class Ref(ref: Addressable, typ: PointerT)(val info: Source.Parser.Info) extends Expr

case class FieldRef(recv: Expr, field: Field, path: MemberPath)(val info: Source.Parser.Info) extends Expr {
  override lazy val typ: Type = field.typ
}


sealed trait Addressable extends Node {
  def op: Expr
  override def info: Parser.Info = op.info
}

object Addressable {
  case class Var(op: LocalVar.Ref) extends Addressable
  case class Field(op: FieldRef) extends Addressable
  // TODO: Global
}

sealed trait BoolExpr extends Expr {
  override val typ: Type = BoolT
}

sealed trait IntExpr extends Expr {
  override val typ: Type = IntT
}

case class Negation(operand: Expr)(val info: Source.Parser.Info) extends BoolExpr

sealed abstract class BinaryExpr(val operator: String) extends Expr {
  def left: Expr
  def right: Expr
}

object BinaryExpr {
  def unapply(arg: BinaryExpr): Option[(Expr, String, Expr, Type)] =
    Some((arg.left, arg.operator, arg.right, arg.typ))
}

case class EqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)      extends BinaryExpr("==") with BoolExpr
case class UneqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)    extends BinaryExpr("!=") with BoolExpr
case class LessCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)    extends BinaryExpr("<" ) with BoolExpr
case class AtMostCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)  extends BinaryExpr("<=") with BoolExpr
case class GreaterCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">" ) with BoolExpr
case class AtLeastCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">=") with BoolExpr

case class And(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("&&") with BoolExpr
case class Or(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("||") with BoolExpr


case class Add(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("+") with IntExpr
case class Sub(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("-") with IntExpr
case class Mul(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("*") with IntExpr
case class Mod(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("%") with IntExpr
case class Div(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("/") with IntExpr



sealed trait Lit extends Expr

case class IntLit(v: BigInt)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = IntT
}

case class BoolLit(b: Boolean)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = BoolT
}




sealed trait Var extends Expr {
  def id: String
}

case class Parameter(id: String, typ: Type)(val info: Source.Parser.Info) extends Var with TopDeclaration

sealed trait BodyVar extends Var

sealed trait LocalVar extends BodyVar with TopDeclaration with BottomDeclaration {
  def unapply(arg: LocalVar): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

object LocalVar {
  case class Ref(id: String, typ: Type)(val info: Source.Parser.Info) extends LocalVar
  case class Val(id: String, typ: Type)(val info: Source.Parser.Info) extends LocalVar
}

//sealed trait GlobalVar extends Var {
//  def unapply(arg: LocalVar): Option[(String, Type)] =
//    Some((arg.id, arg.typ))
//}

//object GlobalVar {
//  case class Var(id: String, typ: Type)(val src: Source) extends LocalVar
//  case class Val(id: String, typ: Type)(val src: Source) extends LocalVar
//}


sealed trait Typed {
  def typ: Type
}

sealed trait TopType extends Type

sealed trait Type

case object BoolT extends Type

case object IntT extends Type

case object VoidT extends Type

case object NilT extends Type

case object PermissionT extends Type

case class DefinedT(name: String, right: Type) extends TopType

case class PointerT(t: Type) extends TopType

case class TupleT(ts: Vector[Type]) extends TopType

sealed trait StructT extends TopType {
  def name: String
  def fields: Vector[Field]
}


case class RefStructT(name: String, fields: Vector[Field]) extends StructT
case class ValStructT(name: String, fields: Vector[Field]) extends StructT





sealed trait Proxy extends Node
case class FunctionProxy(name: String)(val info: Source.Parser.Info) extends Proxy


object MemberPath {
  sealed trait Step

  case object Underlying extends Step
  case object Deref extends Step
  case object Ref extends Step
  case class  Next(e: Field) extends Step
}

case class MemberPath(path: Vector[MemberPath.Step])



