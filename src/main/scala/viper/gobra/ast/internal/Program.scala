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

case class GlobalConstDecl(left: GlobalConst, right: Lit)(val info: Source.Parser.Info) extends Member

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
  case class Index(op : IndexedExp) extends Assignee
}

case class Make(target: LocalVar.Val, typ: CompositeObject)(val info: Source.Parser.Info) extends Stmt

sealed trait CompositeObject extends Node {
  def op: CompositeLit
  override def info: Parser.Info = op.info
}

object CompositeObject {
  case class Array(op : ArrayLit) extends CompositeObject
  case class Struct(op : StructLit) extends CompositeObject
  case class Sequence(op : SequenceLit) extends CompositeObject
  case class Set(op : SetLit) extends CompositeObject
  case class Multiset(op : MultisetLit) extends CompositeObject
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
  case class Index(op : IndexedExp) extends Accessible
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

case class Trigger(exprs: Vector[Expr])(val info: Source.Parser.Info) extends Node

case class PureForall(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Expr)(val info: Source.Parser.Info) extends Expr {
override def typ: Type = BoolT
}

case class SepForall(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Assertion)(val info: Source.Parser.Info) extends Assertion

case class Exists(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Expr)(val info: Source.Parser.Info) extends Expr {
override def typ: Type = BoolT
}


/* ** Collection expressions */

/**
  * Denotes the multiplicity operator "`left` # `right`", with `right`
  * a sequence or (multi)set and `left` an expression of a matching type.
  */
case class Multiplicity(left : Expr, right : Expr)(val info: Source.Parser.Info) extends BinaryExpr("#") {
  override def typ : Type = IntT
}

/**
  * Denotes the length of `exp`, which should be
  * of type `ArrayT` or `SequenceT`.
  */
case class Length(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = IntT
}

/**
  * Represents indexing into an array "`base`[`index`]",
  * where `base` is expected to be of an array or sequence type
  * and `index` of an integer type.
  */
case class IndexedExp(base : Expr, index : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = base.typ match {
    case ArrayT(_, t) => t
    case SequenceT(t) => t
    case t => Violation.violation(s"expected an array or sequence type, but got $t")
  }
}




/* ** Sequence expressions */

/**
  * A (mathematical) sequence literal "seq[`memberType`] { e_0, ..., e_n }",
  * where `exprs` constitutes the vector "e_0, ..., e_n" of members,
  * which should all be of type `memberType`.
  */
case class SequenceLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  lazy val length = exprs.length
  override def typ : Type = SequenceT(memberType)
}

/**
  * Denotes the range of integers from `low` to `high`
  * (both of which should be integers), not including `high` but including `low`.
  */
case class RangeSequence(low : Expr, high : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = SequenceT(IntT)
}

/**
  * The appending of two sequences represented by `left` and `right`
  * (which should be of identical types as result of type checking).
  */
case class SequenceAppend(left : Expr, right : Expr)(val info: Source.Parser.Info) extends BinaryExpr("++") {
  /** Should be identical to `right.typ`. */
  override def typ : Type = left.typ
}

/**
  * Denotes a sequence update "`seq`[`left` = `right`]", which results in a
  * sequence equal to `seq` but 'updated' to have `right` at the `left` position.
  */
case class SequenceUpdate(base : Expr, left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `base`. */
  override def typ : Type = base.typ
}

/**
  * Represents a _sequence drop expression_ roughly of
  * the form "`left`[`right`:]".
  * Here `left` is the base sequence and `right` an integer
  * denoting the number of elements to drop from `left`.
  */
case class SequenceDrop(left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `left`. */
  override def typ : Type = left.typ
}

/**
  * Represents a _sequence take operation_ roughly of
  * the form "`left`[:`right`]", where `left` is the base sequence
  * and `right` an integer denoting the number of elements to
  * take from `left`.
  */
case class SequenceTake(left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `left`. */
  override def typ : Type = left.typ
}


/* ** Unordered collection expressions */

/**
  * Represents a (multi)set union "`left` union `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class Union(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("union") {
  /** `left.typ` is expected to be identical to `right.typ`. */
  override def typ : Type = left.typ
}

/**
  * Represents a (multi)set intersection "`left` intersection `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class Intersection(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("intersection") {
  /** `left.typ` is expected to be identical to `right.typ`. */
  override def typ : Type = left.typ
}

/**
  * Represents a (multi)set difference "`left` setminus `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class SetMinus(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("setminus") {
  /** `left.typ` is expected to be identical to `right.typ`. */
  override def typ : Type = left.typ
}

/**
  * Represents a subset relation "`left` subset `right`", where
  * `left` and `right` are assumed to be sets of comparable types.
  */
case class Subset(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("subset") {
  override def typ : Type = BoolT
}

/**
  * Represents the cardinality of `exp`, which is assumed
  * to be either a set or a multiset.
  */
case class Cardinality(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = IntT
}

/**
  * Represents a membership expression "`left` in `right`".
  * Here `right` should be a ghost collection (that is,
  * a sequence, set, or multiset) of a type that is compatible
  * with the one of `left`.
  */
case class Contains(left : Expr, right : Expr)(val info: Source.Parser.Info) extends BinaryExpr("in") {
  override def typ : Type = BoolT
}


/* ** Set expressions */

/**
  * Represents a (mathematical) set literal "set[`memberType`] { e_0, ..., e_n }",
  * where `exprs` constitutes the vector "e_0, ..., e_n" of members,
  * which should all be of type `memberType`.
  */
case class SetLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  override def typ : Type = SetT(memberType)
}

/**
  * Represents the conversion of a collection of type 't', represented by `exp`,
  * to a (mathematical) set of type 't'.
  */
case class SetConversion(expr : Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ : Type = expr.typ match {
    case SequenceT(t) => SetT(t)
    case t => Violation.violation(s"expected a sequence type but got $t")
  }
}


/* ** Multiset expressions */

/**
  * Represents a multiset literal "mset[`memberType`] { e_0, ..., e_n }",
  * where `exprs` constitutes the vector "e_0, ..., e_n" of members,
  * which should all be of type `memberType`.
  */
case class MultisetLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  override def typ : Type = MultisetT(memberType)
}

/**
  * Represents the conversion of `exp` to a (mathematical) multiset of
  * a matching type, where `exp` should be a collection, i.e., a sequence or (multi)set.
  */
case class MultisetConversion(expr : Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ : Type = expr.typ match {
    case SequenceT(t) => MultisetT(t)
    case t => Violation.violation(s"expected a sequence type but got $t")
  }
}


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
      case _: BoundVar => true
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
      case e: IndexedExp => isAddressable(e.base) && !isNonAddressable(e)
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

case class ArrayLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  lazy val length = exprs.length
  lazy val asSeqLit = SequenceLit(memberType, exprs)(info)
  override def typ : Type = ArrayT(exprs.length, memberType)
}

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

case class BoundVar(id: String, typ: Type)(val info: Source.Parser.Info) extends Var with BottomDeclaration

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

/**
  * The type of arrays of length `length` and type `t`.
  * Here `length` is assumed to be positive
  * (this is ensured by the type checker).
  */
case class ArrayT(length : BigInt, typ : Type) extends Type

/**
  * The type of mathematical sequences with elements of type `t`.
  * @param t The type of elements
  */
case class SequenceT(t : Type) extends Type

/**
  * The type of mathematical sets with elements of type `t`.
  */
case class SetT(t : Type) extends Type

/**
  * The type of mathematical multisets with elements of type `t`.
  */
case class MultisetT(t : Type) extends Type

case class DefinedT(name: String) extends Type with TopType

case class PointerT(t: Type) extends Type with TopType

case class TupleT(ts: Vector[Type]) extends Type with TopType

case class StructT(name: String, fields: Vector[Field]) extends Type with TopType




sealed trait Proxy extends Node
case class FunctionProxy(name: String)(val info: Source.Parser.Info) extends Proxy
case class MethodProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends Proxy
case class FPredicateProxy(name: String)(val info: Source.Parser.Info) extends Proxy
case class MPredicateProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends Proxy





