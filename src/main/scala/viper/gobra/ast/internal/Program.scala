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
import viper.gobra.util.Violation

case class Program(
                    types: Vector[TopType], members: Vector[Member], table: LookupTable
                  )(val info: Source.Parser.Info) extends Node {

}

class LookupTable(
                 definedTypes: Map[String, Type]
                  ) {
  def lookup(t: DefinedT): Type = definedTypes(t.name)
}

sealed trait Member extends Node

sealed trait Location extends Expr


sealed trait GlobalVarDecl extends Member

//case class SingleGlobalVarDecl(left: GlobalVar, right: Expr)

//case class MultiGlobalVarDecl(lefts: Vector[GlobalVar], right: Expr)

sealed trait GlobalConstDecl extends Member

case class SingleGlobalConstDecl(left: GlobalConst, right: Expr)(val info: Source.Parser.Info) extends GlobalConstDecl

sealed trait Field extends Node {
  def name: String
  def typ: Type
}

object Field {
  def unapply(arg: Field): Option[(String, Type)] = Some((arg.name, arg.typ))

  case class Ref(name: String, typ: Type)(val info: Source.Parser.Info) extends Field
  case class Val(name: String, typ: Type)(val info: Source.Parser.Info) extends Field
}



case class Method(
                 receiver: Parameter.In,
                 name: MethodProxy,
                 args: Vector[Parameter.In],
                 results: Vector[Parameter.Out],
                 pres: Vector[Assertion],
                 posts: Vector[Assertion],
                 body: Option[Block]
                 )(val info: Source.Parser.Info) extends Member

case class PureMethod(
                       receiver: Parameter.In,
                       name: MethodProxy,
                       args: Vector[Parameter.In],
                       results: Vector[Parameter.Out],
                       pres: Vector[Assertion],
                       body: Option[Expr]
                     )(val info: Source.Parser.Info) extends Member {
  require(results.size <= 1)
}

case class Function(
                     name: FunctionProxy,
                     args: Vector[Parameter.In],
                     results: Vector[Parameter.Out],
                     pres: Vector[Assertion],
                     posts: Vector[Assertion],
                     body: Option[Block]
                   )(val info: Source.Parser.Info) extends Member

case class PureFunction(
                         name: FunctionProxy,
                         args: Vector[Parameter.In],
                         results: Vector[Parameter.Out],
                         pres: Vector[Assertion],
                         body: Option[Expr]
                       )(val info: Source.Parser.Info) extends Member {
  require(results.size <= 1)
}

case class FPredicate(
                     name: FPredicateProxy,
                     args: Vector[Parameter.In],
                     body: Option[Assertion]
                     )(val info: Source.Parser.Info) extends Member

case class MPredicate(
                     receiver: Parameter,
                     name: MPredicateProxy,
                     args: Vector[Parameter.In],
                     body: Option[Assertion]
                     )(val info: Source.Parser.Info) extends Member




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

case class SingleAss(left: Assignee, right: Expr)(val info: Source.Parser.Info) extends Assignment

sealed trait Assignee extends Node {
  def op: Expr
  override def info: Parser.Info = op.info
}

object Assignee {
  case class Var(op: AssignableVar) extends Assignee
  case class Pointer(op: Deref) extends Assignee
  case class Field(op: FieldRef) extends Assignee
  // TODO: Index
}

case class Make(target: LocalVar.Val, typ: CompositeObject)(val info: Source.Parser.Info) extends Stmt

sealed trait CompositeObject extends Node {
  def op: CompositeLit
  override def info: Parser.Info = op.info
}

object CompositeObject {
  case class Struct(op: StructLit) extends CompositeObject
}

case class FunctionCall(targets: Vector[LocalVar.Val], func: FunctionProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt
case class MethodCall(targets: Vector[LocalVar.Val], recv: Expr, meth: MethodProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt

case class Return()(val info: Source.Parser.Info) extends Stmt

case class Assert(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Assume(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Inhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Exhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt

case class Fold(acc: Access)(val info: Source.Parser.Info) extends Stmt {
  require(acc.e.isInstanceOf[Accessible.Predicate])
  lazy val op: PredicateAccess = acc.e.asInstanceOf[Accessible.Predicate].op
}

case class Unfold(acc: Access)(val info: Source.Parser.Info) extends Stmt {
  require(acc.e.isInstanceOf[Accessible.Predicate])
  lazy val op: PredicateAccess = acc.e.asInstanceOf[Accessible.Predicate].op
}


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
  case class Pointer(op: Deref) extends Accessible
  case class Field(op: FieldRef) extends Accessible
  case class Predicate(op: PredicateAccess) extends Accessible
}

sealed trait PredicateAccess extends Node

case class FPredicateAccess(pred: FPredicateProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends PredicateAccess
case class MPredicateAccess(recv: Expr, pred: MPredicateProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends PredicateAccess
case class MemoryPredicateAccess(arg: Expr)(val info: Source.Parser.Info) extends PredicateAccess



sealed trait Expr extends Node with Typed

case class Unfolding(acc: Access, in: Expr)(val info: Source.Parser.Info) extends Expr {
  require(acc.e.isInstanceOf[Accessible.Predicate])
  lazy val op: PredicateAccess = acc.e.asInstanceOf[Accessible.Predicate].op
  override def typ: Type = in.typ
}

case class Old(operand: Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = operand.typ
}

case class Conditional(cond: Expr, thn: Expr, els: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr

case class PureFunctionCall(func: FunctionProxy, args: Vector[Expr], typ: Type)(val info: Source.Parser.Info) extends Expr
case class PureMethodCall(recv: Expr, meth: MethodProxy, args: Vector[Expr], typ: Type)(val info: Source.Parser.Info) extends Expr

case class Deref(exp: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr with Location {
  require(exp.typ.isInstanceOf[PointerT])
}

object Deref {
  def apply(exp: Expr)(info: Source.Parser.Info): Deref = {
    require(exp.typ.isInstanceOf[PointerT])
    Deref(exp, exp.typ.asInstanceOf[PointerT].t)(info)
  }

}

case class Ref(ref: Addressable, typ: PointerT)(val info: Source.Parser.Info) extends Expr with Location

object Ref {
  def apply(ref: Expr)(info: Source.Parser.Info): Ref = {
    require(Addressable.isAddressable(ref))

    val pointerT = PointerT(ref.typ)
    ref match {
      case x: LocalVar.Ref => Ref(Addressable.Var(x), pointerT)(info)
      case x: Deref        => Ref(Addressable.Pointer(x), pointerT)(info)
      case x: FieldRef     => Ref(Addressable.Field(x), pointerT)(info)
      case _ => Violation.violation(s"encountered unexpected addressable expression $ref")
    }
  }
}

case class FieldRef(recv: Expr, field: Field)(val info: Source.Parser.Info) extends Expr with Location {
  override lazy val typ: Type = field.typ
}


sealed trait Addressable extends Node {
  def op: Location
  override def info: Parser.Info = op.info
}

object Addressable {
  case class Var(op: LocalVar.Ref) extends Addressable
  case class Pointer(op: Deref) extends Addressable
  case class Field(op: FieldRef) extends Addressable
  // TODO: Global

  import viper.gobra.ast.internal.{Field => Field2}

  def isAddressable(x: Field2): Boolean = x match {
    case _: Field2.Ref => true
    case _: Field2.Val => false
  }

  def isNonAddressable(x: Expr): Boolean = {
    x match {
      case _: LocalVar.Inter => true
      case _: Parameter => true
      case _: Lit | _: DfltVal => true
      case f: FieldRef => isNonAddressable(f.recv)
      case _ => false
    }
  }

  def isAddressable(x: Expr): Boolean = {
    x match {
      case _: LocalVar.Ref => true
      case _: Deref => true
      case f: FieldRef => isAddressable(f.field) && !isNonAddressable(f)
      case _ => false
    }
  }
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

case class DfltVal(typ: Type)(val info: Source.Parser.Info) extends Expr

case class IntLit(v: BigInt)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = IntT
}

case class BoolLit(b: Boolean)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = BoolT
}

case class NilLit()(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = NilT
}

case class Tuple(args: Vector[Expr])(val info: Source.Parser.Info) extends Expr {
  lazy val typ = TupleT(args map (_.typ)) // TODO: remove redundant typ information of other nodes
}

sealed trait CompositeLit extends Lit

case class StructLit(typ: Type, args: Vector[Expr])(val info: Source.Parser.Info) extends CompositeLit

sealed trait Var extends Expr with Location {
  def id: String

  def unapply(arg: Var): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

sealed trait AssignableVar extends Var with BottomDeclaration

sealed trait Parameter extends Var with TopDeclaration {
  def unapply(arg: Parameter): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

object Parameter {
  case class In(id: String, typ: Type)(val info: Source.Parser.Info) extends Parameter
  case class Out(id: String, typ: Type)(val info: Source.Parser.Info) extends Parameter with AssignableVar
}


sealed trait BodyVar extends Var

sealed trait LocalVar extends BodyVar with AssignableVar {
  def unapply(arg: LocalVar): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

object LocalVar {
  case class Ref(id: String, typ: Type)(val info: Source.Parser.Info) extends LocalVar
  case class Val(id: String, typ: Type)(val info: Source.Parser.Info) extends LocalVar with TopDeclaration
  case class Inter(id: String, typ: Type)(val info: Source.Parser.Info) extends LocalVar

}

sealed trait GlobalConst extends Var {
  def unapply(arg: GlobalConst): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

object GlobalConst {
  case class Val(id: String, typ: Type)(val info: Source.Parser.Info) extends GlobalConst
}





sealed trait Typed {
  def typ: Type
}

sealed trait TopType

sealed trait Type

case object BoolT extends Type

case object IntT extends Type

case object VoidT extends Type

case object NilT extends Type

case object PermissionT extends Type

case class DefinedT(name: String) extends Type with TopType

case class PointerT(t: Type) extends Type with TopType

case class TupleT(ts: Vector[Type]) extends Type with TopType

case class StructT(name: String, fields: Vector[Field]) extends Type with TopType





sealed trait Proxy extends Node
case class FunctionProxy(name: String)(val info: Source.Parser.Info) extends Proxy
case class MethodProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends Proxy
case class FPredicateProxy(name: String)(val info: Source.Parser.Info) extends Proxy
case class MPredicateProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends Proxy





