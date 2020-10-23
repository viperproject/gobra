// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

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
import viper.gobra.theory.Addressability
import viper.gobra.util.TypeBounds.{IntegerKind, UnboundedInteger}
import viper.gobra.util.Violation

case class Program(
                    types: Vector[TopType], members: Vector[Member], table: LookupTable
                  )(val info: Source.Parser.Info) extends Node {

}

class LookupTable(
                 definedTypes: Map[(String, Addressability), Type]
                  ) {
  def lookup(t: DefinedT): Type = definedTypes(t.name, t.addressability)
}

sealed trait Member extends Node

sealed trait Location extends Expr


sealed trait GlobalVarDecl extends Member

case class GlobalConstDecl(left: GlobalConst, right: Lit)(val info: Source.Parser.Info) extends Member

case class Field(name: String, typ: Type, ghost: Boolean)(val info: Source.Parser.Info) extends Node



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
                       posts: Vector[Assertion],
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
                         posts: Vector[Assertion],
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
                     receiver: Parameter.In,
                     name: MPredicateProxy,
                     args: Vector[Parameter.In],
                     body: Option[Assertion]
                     )(val info: Source.Parser.Info) extends Member




sealed trait Stmt extends Node

case class Block(
                  decls: Vector[BlockDeclaration],
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
  def unapply(x: Assignee): Option[Expr] = Some(x.op)
  def apply(op: Expr): Assignee = op match {
    case op: AssignableVar => Var(op)
    case op: Deref => Pointer(op)
    case op: FieldRef => Field(op)
    case op: IndexedExp => Index(op)
    case _ => Violation.violation(s"expected variables, dereference, field access, or index expression, but got $op")
  }

  case class Var(op: AssignableVar) extends Assignee
  case class Pointer(op: Deref) extends Assignee
  case class Field(op: FieldRef) extends Assignee
  case class Index(op : IndexedExp) extends Assignee
}

case class Make(target: LocalVar, typ: CompositeObject)(val info: Source.Parser.Info) extends Stmt

sealed trait CompositeObject extends Node {
  def op: CompositeLit
  override def info: Parser.Info = op.info
}

object CompositeObject {
  def unapply(arg: CompositeObject): Option[CompositeLit] = Some(arg.op)

  case class Array(op : ArrayLit) extends CompositeObject
  case class Struct(op : StructLit) extends CompositeObject
  case class Sequence(op : SequenceLit) extends CompositeObject
  case class Set(op : SetLit) extends CompositeObject
  case class Multiset(op : MultisetLit) extends CompositeObject
}

case class FunctionCall(targets: Vector[LocalVar], func: FunctionProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt
case class MethodCall(targets: Vector[LocalVar], recv: Expr, meth: MethodProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt

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
  case class Predicate(op: PredicateAccess) extends Accessible
  case class Address(op: Location) extends Accessible {
    require(op.typ.addressability == Addressability.Shared, s"expected shared location, but got $op :: ${op.typ}")
  }
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
  require(typ.addressability == Addressability.unfolding(in.typ.addressability))
}

case class Old(operand: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr

case class Conditional(cond: Expr, thn: Expr, els: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr

case class Trigger(exprs: Vector[Expr])(val info: Source.Parser.Info) extends Node

case class PureForall(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Expr)(val info: Source.Parser.Info) extends Expr {
override def typ: Type = BoolT(Addressability.rValue)
}

case class SepForall(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Assertion)(val info: Source.Parser.Info) extends Assertion

case class Exists(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Expr)(val info: Source.Parser.Info) extends Expr {
override def typ: Type = BoolT(Addressability.rValue)
}


/* ** Collection expressions */

/**
  * Denotes the multiplicity operator "`left` # `right`", with `right`
  * a sequence or (multi)set and `left` an expression of a matching type.
  */
case class Multiplicity(left : Expr, right : Expr)(val info: Source.Parser.Info) extends BinaryExpr("#") {
  override def typ : Type = IntT(Addressability.rValue)
}

/**
  * Denotes the length of `exp`, which is expected to be either
  * of an array type or a sequence type.
  */
case class Length(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = IntT(Addressability.rValue)
}

/**
  * Represents indexing into an array "`base`[`index`]",
  * where `base` is expected to be of an array or sequence type
  * and `index` of an integer type.
  */
case class IndexedExp(base : Expr, index : Expr)(val info : Source.Parser.Info) extends Expr with Location {
  override val typ : Type = base.typ match {
    case t: ArrayT => t.elems
    case t: SequenceT => t.t
    case t => Violation.violation(s"expected an array or sequence type, but got $t")
  }
}

/**
  * Denotes an array update "`base`[`left` = `right`]", which results in an
  * array equal to `base` but 'updated' to have `right` at the `left` position.
  */
case class ArrayUpdate(base: Expr, left: Expr, right: Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `base`. */
  require(base.typ.addressability == Addressability.Exclusive)
  override val typ : Type = base.typ
}


/* ** Sequence expressions */

/**
  * A (mathematical) sequence literal "seq[`memberType`] { e_0, ..., e_n }",
  * where `exprs` constitutes the vector "e_0, ..., e_n" of members,
  * which should all be of type `memberType`.
  */
case class SequenceLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  lazy val length: BigInt = exprs.length
  override val typ : Type = SequenceT(memberType, Addressability.literal)
}

/**
  * Denotes the range of integers from `low` to `high`
  * (both of which should be integers), not including `high` but including `low`.
  */
case class RangeSequence(low : Expr, high : Expr)(val info : Source.Parser.Info) extends Expr {
  override val typ : Type = SequenceT(IntT(Addressability.mathDataStructureLookup), Addressability.rValue)
}

/**
  * The appending of two sequences represented by `left` and `right`
  * (which should be of identical types as result of type checking).
  */
case class SequenceAppend(left : Expr, right : Expr)(val info: Source.Parser.Info) extends BinaryExpr("++") {
  /** Should be identical to `right.typ`. */
  require(left.typ.isInstanceOf[SequenceT] && right.typ.isInstanceOf[SequenceT], s"expected two sequences, but got ${left.typ} and ${right.typ} (${info.origin})")
  override val typ : Type = SequenceT(left.typ.asInstanceOf[SequenceT].t, Addressability.rValue)
}

/**
  * Denotes a sequence update "`seq`[`left` = `right`]", which results in a
  * sequence equal to `seq` but 'updated' to have `right` at the `left` position.
  */
case class SequenceUpdate(base : Expr, left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `base`. */
  require(base.typ.isInstanceOf[SequenceT], s"expected sequence, but got ${base.typ} (${info.origin})")
  override val typ : Type = SequenceT(base.typ.asInstanceOf[SequenceT].t, Addressability.rValue)
}

/**
  * Represents a _sequence drop expression_ roughly of
  * the form "`left`[`right`:]".
  * Here `left` is the base sequence and `right` an integer
  * denoting the number of elements to drop from `left`.
  */
case class SequenceDrop(left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `left`. */
  require(left.typ.isInstanceOf[SequenceT])
  override val typ : Type = SequenceT(left.typ.asInstanceOf[SequenceT].t, Addressability.rValue)
}

/**
  * Represents a _sequence take operation_ roughly of
  * the form "`left`[:`right`]", where `left` is the base sequence
  * and `right` an integer denoting the number of elements to
  * take from `left`.
  */
case class SequenceTake(left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `left`. */
  require(left.typ.isInstanceOf[SequenceT])
  override val typ : Type = SequenceT(left.typ.asInstanceOf[SequenceT].t, Addressability.rValue)
}

/**
  * Represents the conversion of a collection of type 't',
  * represented by `expr`, to a (mathematical) sequence of type 't'.
  * Here `expr` is assumed to be either a sequence or an exclusive array.
  */
case class SequenceConversion(expr : Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ : Type = expr.typ match {
    case t: SequenceT => t
    case t: ArrayT => t.sequence
    case t => Violation.violation(s"expected a sequence or exclusive array type. but got $t")
  }
}


/* ** Unordered collection expressions */

/**
  * Represents a (multi)set union "`left` union `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class Union(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("union") {
  /** `left.typ` is expected to be identical to `right.typ`. */
  require(
    (left.typ.isInstanceOf[SetT] && right.typ.isInstanceOf[SetT])
      || (left.typ.isInstanceOf[MultisetT] && right.typ.isInstanceOf[MultisetT]),
    s"expected set or multiset, but got ${left.typ} and ${right.typ} (${info.origin})"
  )
  override val typ : Type = left.typ match {
    case t: SetT => SetT(t.t, Addressability.rValue)
    case t: MultisetT => MultisetT(t.t, Addressability.rValue)
    case _ => Violation.violation("expected set or type")
  }
}

/**
  * Represents a (multi)set intersection "`left` intersection `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class Intersection(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("intersection") {
  /** `left.typ` is expected to be identical to `right.typ`. */
  require(
    (left.typ.isInstanceOf[SetT] && right.typ.isInstanceOf[SetT])
      || (left.typ.isInstanceOf[MultisetT] && right.typ.isInstanceOf[MultisetT]),
    s"expected set or multiset, but got ${left.typ} and ${right.typ} (${info.origin})"
  )
  override val typ : Type = left.typ match {
    case t: SetT => SetT(t.t, Addressability.rValue)
    case t: MultisetT => MultisetT(t.t, Addressability.rValue)
    case _ => Violation.violation("expected set or type")
  }
}

/**
  * Represents a (multi)set difference "`left` setminus `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class SetMinus(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("setminus") {
  /** `left.typ` is expected to be identical to `right.typ`. */
  require(
    (left.typ.isInstanceOf[SetT] && right.typ.isInstanceOf[SetT])
      || (left.typ.isInstanceOf[MultisetT] && right.typ.isInstanceOf[MultisetT]),
    s"expected set or multiset, but got ${left.typ} and ${right.typ} (${info.origin})"
  )
  override val typ : Type = left.typ match {
    case t: SetT => SetT(t.t, Addressability.rValue)
    case t: MultisetT => MultisetT(t.t, Addressability.rValue)
    case _ => Violation.violation("expected set or type")
  }
}

/**
  * Represents a subset relation "`left` subset `right`", where
  * `left` and `right` are assumed to be sets of comparable types.
  */
case class Subset(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("subset") {
  override val typ : Type = BoolT(Addressability.rValue)
}

/**
  * Represents the cardinality of `exp`, which is assumed
  * to be either a set or a multiset.
  */
case class Cardinality(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override val typ : Type = IntT(Addressability.rValue)
}

/**
  * Represents a membership expression "`left` in `right`".
  * Here `right` should be a ghost collection (that is,
  * a sequence, set, or multiset) of a type that is compatible
  * with the one of `left`.
  */
case class Contains(left : Expr, right : Expr)(val info: Source.Parser.Info) extends BinaryExpr("in") {
  override val typ : Type = BoolT(Addressability.rValue)
}


/* ** Set expressions */

/**
  * Represents a (mathematical) set literal "set[`memberType`] { e_0, ..., e_n }",
  * where `exprs` constitutes the vector "e_0, ..., e_n" of members,
  * which should all be of type `memberType`.
  */
case class SetLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  override val typ : Type = SetT(memberType, Addressability.literal)
}

/**
  * Represents the conversion of a collection of type 't', represented by `exp`,
  * to a (mathematical) set of type 't'.
  */
case class SetConversion(expr : Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ : Type = expr.typ match {
    case SequenceT(t, _) => SetT(t, Addressability.conversionResult)
    case SetT(t, _) => SetT(t, Addressability.conversionResult)
    case t => Violation.violation(s"expected a sequence or set type but got $t")
  }
}


/* ** Multiset expressions */

/**
  * Represents a multiset literal "mset[`memberType`] { e_0, ..., e_n }",
  * where `exprs` constitutes the vector "e_0, ..., e_n" of members,
  * which should all be of type `memberType`.
  */
case class MultisetLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  override val typ : Type = MultisetT(memberType, Addressability.literal)
}

/**
  * Represents the conversion of `exp` to a (mathematical) multiset of
  * a matching type, where `exp` should be a collection, i.e., a sequence or (multi)set.
  */
case class MultisetConversion(expr : Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ : Type = expr.typ match {
    case SequenceT(t, _) => MultisetT(t, Addressability.conversionResult)
    case MultisetT(t, _) => MultisetT(t, Addressability.conversionResult)
    case t => Violation.violation(s"expected a sequence or multiset type but got $t")
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
    require(ref.typ.addressability == Addressability.Shared)

    val pointerT = PointerT(ref.typ, Addressability.reference)
    ref match {
      case x: LocalVar     => Ref(Addressable.Var(x), pointerT)(info)
      case x: Deref        => Ref(Addressable.Pointer(x), pointerT)(info)
      case x: FieldRef     => Ref(Addressable.Field(x), pointerT)(info)
      case x: IndexedExp   => Ref(Addressable.Index(x), pointerT)(info)
      case _ => Violation.violation(s"encountered unexpected addressable expression $ref")
    }
  }
}


sealed trait Addressable extends Node {
  def op: Location
  override def info: Parser.Info = op.info
}

object Addressable {

  case class Var(op: LocalVar) extends Addressable
  case class Pointer(op: Deref) extends Addressable
  case class Field(op: FieldRef) extends Addressable
  case class Index(op: IndexedExp) extends Addressable
}

case class FieldRef(recv: Expr, field: Field)(val info: Source.Parser.Info) extends Expr with Location {
  override val typ: Type = field.typ
}

/** Updates struct 'base' at field 'field' with value 'newVal', i.e. base[field -> newVal]. */
case class StructUpdate(base: Expr, field: Field, newVal: Expr)(val info: Source.Parser.Info) extends Expr {
  require(base.typ.addressability == Addressability.Exclusive)
  override val typ: Type = base.typ
}

sealed trait BoolOperation extends Expr {
  override val typ: Type = BoolT(Addressability.rValue)
}

sealed trait IntOperation extends Expr {
  override val typ: Type = IntT(Addressability.rValue)
}

case class Negation(operand: Expr)(val info: Source.Parser.Info) extends BoolOperation

sealed abstract class BinaryExpr(val operator: String) extends Expr {
  def left: Expr
  def right: Expr
}

object BinaryExpr {
  def unapply(arg: BinaryExpr): Option[(Expr, String, Expr, Type)] =
    Some((arg.left, arg.operator, arg.right, arg.typ))
}

case class EqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)      extends BinaryExpr("==") with BoolOperation
case class UneqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)    extends BinaryExpr("!=") with BoolOperation
case class LessCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)    extends BinaryExpr("<" ) with BoolOperation
case class AtMostCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)  extends BinaryExpr("<=") with BoolOperation
case class GreaterCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">" ) with BoolOperation
case class AtLeastCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">=") with BoolOperation

case class And(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("&&") with BoolOperation
case class Or(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("||") with BoolOperation


case class Add(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("+") with IntOperation
case class Sub(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("-") with IntOperation
case class Mul(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("*") with IntOperation
case class Mod(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("%") with IntOperation
case class Div(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("/") with IntOperation



sealed trait Lit extends Expr

case class DfltVal(typ: Type)(val info: Source.Parser.Info) extends Expr

case class IntLit(v: BigInt)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = IntT(Addressability.literal)
}

case class BoolLit(b: Boolean)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = BoolT(Addressability.literal)
}

case class NilLit(typ: Type)(val info: Source.Parser.Info) extends Lit

case class Tuple(args: Vector[Expr])(val info: Source.Parser.Info) extends Expr {
  lazy val typ = TupleT(args map (_.typ), Addressability.literal) // TODO: remove redundant typ information of other nodes
}

sealed trait CompositeLit extends Lit

case class ArrayLit(memberType : Type, exprs : Vector[Expr])(val info : Source.Parser.Info) extends CompositeLit {
  lazy val length: BigInt = exprs.length
  override val typ : Type = ArrayT(exprs.length, memberType, Addressability.literal)
}

case class StructLit(typ: Type, args: Vector[Expr])(val info: Source.Parser.Info) extends CompositeLit



sealed trait Declaration extends Node

/** Everything that is defined with the scope of a code block. */
sealed trait BlockDeclaration extends Declaration

/** Any Gobra variable. */
sealed trait Var extends Expr with Location {
  def id: String

  def unapply(arg: Var): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

/**
  * Any variable that has a global scope.
  * */
sealed trait GlobalVar extends Var

/**
  * Any variable whose scope is the body of a method, function, or predicate.
  * Effectively, every variable that does not have a global scope.
  * */
sealed trait BodyVar extends Var

/** Any variable that is assignable in the intermediate representation. */
sealed trait AssignableVar extends Var


sealed trait Parameter extends BodyVar {
  def unapply(arg: Parameter): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

/** In- and out-parameters. */
object Parameter {
  case class In(id: String, typ: Type)(val info: Source.Parser.Info) extends Parameter {
    require(typ.addressability == Addressability.inParameter)
  }
  case class Out(id: String, typ: Type)(val info: Source.Parser.Info) extends Parameter with AssignableVar {
    require(typ.addressability == Addressability.outParameter)
  }
}

/** Variables that are bound by a quantifier. */
case class BoundVar(id: String, typ: Type)(val info: Source.Parser.Info) extends BodyVar {
  require(typ.addressability == Addressability.boundVariable)
}

/** Variables that can be defined in the body of a method or function. */
case class LocalVar(id: String, typ: Type)(val info: Source.Parser.Info) extends BodyVar with AssignableVar with BlockDeclaration

sealed trait GlobalConst extends GlobalVar {
  def unapply(arg: GlobalConst): Option[(String, Type)] =
    Some((arg.id, arg.typ))
}

object GlobalConst {
  case class Val(id: String, typ: Type)(val info: Source.Parser.Info) extends GlobalConst {
    require(typ.addressability == Addressability.constant)
  }
}






sealed trait Typed {
  def typ: Type
}

sealed trait TopType

/** When a type is added, then also add a pattern to [[viper.gobra.translator.util.TypePatterns]] */

sealed trait Type {
  def addressability: Addressability

  /** Returns whether 'this' is equals to 't' without considering the addressability modifier of the types. */
  def equalsWithoutMod(t: Type): Boolean

  def withAddressability(newAddressability: Addressability): Type
}

case class BoolT(addressability: Addressability) extends Type {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[BoolT]
  override def withAddressability(newAddressability: Addressability): BoolT = BoolT(newAddressability)
}

case class IntT(addressability: Addressability, kind: IntegerKind = UnboundedInteger) extends Type {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[IntT] && t.asInstanceOf[IntT].kind == kind
  override def withAddressability(newAddressability: Addressability): IntT = IntT(newAddressability, kind)
}

case object VoidT extends Type {
  override val addressability: Addressability = Addressability.unit
  override def equalsWithoutMod(t: Type): Boolean = t == VoidT
  override def withAddressability(newAddressability: Addressability): VoidT.type = VoidT
}

case class PermissionT(addressability: Addressability) extends Type {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[PermissionT]
  override def withAddressability(newAddressability: Addressability): PermissionT = PermissionT(newAddressability)
}

/**
  * The type of `length`-sized arrays
  * of elements of type `typ`.
  */
case class ArrayT(length: BigInt, elems: Type, addressability: Addressability) extends Type {
  /** (Deeply) converts the current type to a `SequenceT`. */
  lazy val sequence : SequenceT = SequenceT(elems match {
    case t: ArrayT => t.sequence
    case t => t
  }, addressability)

  override def equalsWithoutMod(t: Type): Boolean = t match {
    case ArrayT(otherLength, otherElems, _) => length == otherLength && elems.equalsWithoutMod(otherElems)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): ArrayT =
    ArrayT(length, elems.withAddressability(Addressability.arrayElement(newAddressability)), newAddressability)
}

/**
  * The type of mathematical sequences with elements of type `t`.
  */
case class SequenceT(t : Type, addressability: Addressability) extends Type {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case SequenceT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): SequenceT =
    SequenceT(t.withAddressability(Addressability.mathDataStructureElement), newAddressability)
}

/**
  * The type of mathematical sets with elements of type `t`.
  */
case class SetT(t : Type, addressability: Addressability) extends Type {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case SetT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): SetT =
    SetT(t.withAddressability(Addressability.mathDataStructureElement), newAddressability)
}
/**
  * The type of mathematical multisets with elements of type `t`.
  */
case class MultisetT(t : Type, addressability: Addressability) extends Type {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case MultisetT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): MultisetT =
    MultisetT(t.withAddressability(Addressability.mathDataStructureElement), newAddressability)
}

case class DefinedT(name: String, addressability: Addressability) extends Type with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case DefinedT(otherName, _) => name == otherName
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): DefinedT =
    DefinedT(name, newAddressability)
}

case class PointerT(t: Type, addressability: Addressability) extends Type with TopType {
  require(t.addressability.isShared)

  override def equalsWithoutMod(t: Type): Boolean = t match {
    case PointerT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): PointerT =
    PointerT(t.withAddressability(Addressability.pointerBase), newAddressability)
}

case class TupleT(ts: Vector[Type], addressability: Addressability) extends Type with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case TupleT(otherTs, _) => ts.zip(otherTs).forall{ case (l,r) => l.equalsWithoutMod(r) }
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): TupleT =
    TupleT(ts.map(_.withAddressability(Addressability.mathDataStructureElement)), newAddressability)
}


// TODO: Maybe remove name
case class StructT(name: String, fields: Vector[Field], addressability: Addressability) extends Type with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case StructT(_, otherFields, _) => fields.zip(otherFields).forall{ case (l, r) => l.typ.equalsWithoutMod(r.typ) }
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): StructT =
    StructT(name, fields.map(f => Field(f.name, f.typ.withAddressability(Addressability.field(newAddressability)), f.ghost)(f.info)), newAddressability)
}




sealed trait Proxy extends Node
case class FunctionProxy(name: String)(val info: Source.Parser.Info) extends Proxy
case class MethodProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends Proxy
case class FPredicateProxy(name: String)(val info: Source.Parser.Info) extends Proxy
case class MPredicateProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends Proxy





