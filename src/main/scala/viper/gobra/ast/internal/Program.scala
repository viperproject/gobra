// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal

/**
  * When adding a new node:
  * - extend @see [[DefaultPrettyPrinter.show]]
  * - extend desugar
  * - extend translator
  */

import viper.gobra.frontend.info.base.BuiltInMemberTag.{BuiltInFPredicateTag, BuiltInFunctionTag, BuiltInMPredicateTag, BuiltInMemberTag, BuiltInMethodTag}
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.Parser
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.util.{BackendAnnotation, Decimal, NumBase, TypeBounds, Violation}
import viper.gobra.util.TypeBounds.{IntegerKind, UnboundedInteger}
import viper.gobra.util.Violation.violation

import scala.collection.SortedSet

case class Program(
                    types: Vector[TopType], members: Vector[Member], table: LookupTable
                  )(val info: Source.Parser.Info) extends Node {

}

class LookupTable(
                   private[internal] val definedTypes: Map[(String, Addressability), Type] = Map.empty,
                   private[internal] val definedMethods: Map[MethodProxy, MethodLikeMember] = Map.empty,
                   private[internal] val definedFunctions: Map[FunctionProxy, FunctionLikeMember] = Map.empty,
                   private[internal] val definedMPredicates: Map[MPredicateProxy, MPredicateLikeMember] = Map.empty,
                   private[internal] val definedFPredicates: Map[FPredicateProxy, FPredicateLikeMember] = Map.empty,
                   private[internal] val definedFuncLiterals: Map[FunctionLitProxy, FunctionLitLike] = Map.empty,

                   /**
                   * only has to be defined on types that implement an interface // might change depending on how embedding support changes
                   * SortedSet is used to achieve a consistent ordering of members across runs of Gobra
                   */
                   private[internal] val directMemberProxies: Map[Type, SortedSet[MemberProxy]] = Map.empty,
                   /**
                   * empty interface does not have to be included
                   * SortedSet is used to achieve a consistent ordering of members across runs of Gobra
                   */
                   private[internal] val directInterfaceImplementations: Map[InterfaceT, SortedSet[Type]] = Map.empty,
                   private[internal] val implementationProofPredicateAliases: Map[(Type, InterfaceT, String), FPredicateProxy] = Map.empty,
                 ) {
  def lookup(t: DefinedT): Type = definedTypes(t.name, t.addressability)
  def lookup(m: MethodProxy): MethodLikeMember = definedMethods(m)
  def lookup(f: FunctionProxy): FunctionLikeMember = definedFunctions(f)
  def lookup(m: MPredicateProxy): MPredicateLikeMember = definedMPredicates(m)
  def lookup(f: FPredicateProxy): FPredicateLikeMember = definedFPredicates(f)
  def lookup(l: FunctionLitProxy): FunctionLitLike = definedFuncLiterals(l)

  def getMethods: Iterable[MethodLikeMember] = definedMethods.values
  def getFunctions: Iterable[FunctionLikeMember] = definedFunctions.values
  def getMPredicates: Iterable[MPredicateLikeMember] = definedMPredicates.values
  def getFPredicates: Iterable[FPredicateLikeMember] = definedFPredicates.values

  def lookupImplementations(t: InterfaceT): SortedSet[Type] = getImplementations.getOrElse(t.withAddressability(Addressability.Exclusive), SortedSet.empty)
  def lookupNonInterfaceImplementations(t: InterfaceT): SortedSet[Type] = lookupImplementations(t).filterNot(_.isInstanceOf[InterfaceT])
  def lookupMembers(t: Type): SortedSet[MemberProxy] = getMembers.getOrElse(t.withAddressability(Addressability.Exclusive), SortedSet.empty)
  def lookup(t: Type, name: String): Option[MemberProxy] = lookupMembers(t).find(_.name == name)

  def lookupImplementationPredicate(impl: Type, itf: InterfaceT, name: String): Option[PredicateProxy] = {
    lookup(impl, name).collect{ case m: MPredicateProxy => m }.orElse{
      implementationProofPredicateAliases.get(impl, itf, name)
    }
  }

  def getImplementations: Map[InterfaceT, SortedSet[Type]] = transitiveInterfaceImplementations
  def getMembers: Map[Type, SortedSet[MemberProxy]] = transitiveMemberProxies

  def merge(other: LookupTable): LookupTable = new LookupTable(
    definedTypes ++ other.definedTypes,
    definedMethods ++ other.definedMethods,
    definedFunctions ++ other.definedFunctions,
    definedMPredicates ++ other.definedMPredicates,
    definedFPredicates ++ other.definedFPredicates,
    definedFuncLiterals ++ other.definedFuncLiterals,
    directMemberProxies ++ other.directMemberProxies,
    directInterfaceImplementations ++ other.directInterfaceImplementations,
    implementationProofPredicateAliases ++ other.implementationProofPredicateAliases,
  )

  private lazy val (transitiveInterfaceImplementations, transitiveMemberProxies) = {
    var res = directInterfaceImplementations
    var resMemberProxies = directMemberProxies

    for ((_, values) <- res; t <- values) t match {
      case t: InterfaceT if !res.contains(t) => res += (t -> SortedSet.empty)
      case _ =>
    }

    var change = false
    var temp = res

    def mergeProxies(l: Option[SortedSet[MemberProxy]], r: Option[SortedSet[MemberProxy]]): SortedSet[MemberProxy] = {
      (l.getOrElse(SortedSet.empty[MemberProxy]) ++ r.getOrElse(SortedSet.empty[MemberProxy])).foldLeft((SortedSet.empty[MemberProxy], SortedSet.empty[String])){
        case ((res, set), x) if set.contains(x.name) =>
          // method redeclarations are currently rejected
          Violation.violation(x.isInstanceOf[PredicateProxy], s"Found re-declaration or override of $x, which is currently not supported")
          (res, set) // always take first in sorted set
        case ((res, set), x) => (res ++ SortedSet(x), set ++ SortedSet(x.name))
      }._1
    }
    val mapsTo = temp.compose[Type]{ case t: InterfaceT => t }
    def trans(key: InterfaceT, t: Type): SortedSet[Type] = t match {
      case mapsTo(set) =>
        change = true
        res += (key -> (res(key) ++ set))
        resMemberProxies += (t -> mergeProxies(resMemberProxies.get(t), resMemberProxies.get(key)))
        set

      case _ => SortedSet.empty
    }

    do {
      change = false
      temp = temp.map{ case (key, values) => (key, values.flatMap(trans(key, _))) }
    } while (change)

    (res, resMemberProxies)
  }
}

sealed trait Member extends Node
sealed trait BuiltInMember extends Member {
  def tag: BuiltInMemberTag
  def name: Proxy
  def argsT: Vector[Type]
}

sealed trait MethodLikeMember extends Member {
  def name: MethodProxy
}

sealed trait MethodMember extends MethodLikeMember {
  def receiver: Parameter.In
  def args: Vector[Parameter.In]
  def results: Vector[Parameter.Out]
  def pres: Vector[Assertion]
  def posts: Vector[Assertion]
  def terminationMeasures: Vector[TerminationMeasure]
  def backendAnnotations: Vector[BackendAnnotation]
}

sealed trait FunctionLikeMember extends Member {
  def name: FunctionProxy
}

sealed trait FunctionLikeMemberOrLit extends Node {
  def args: Vector[Parameter.In]
  def results: Vector[Parameter.Out]
  def pres: Vector[Assertion]
  def posts: Vector[Assertion]
  def terminationMeasures: Vector[TerminationMeasure]
  def backendAnnotations: Vector[BackendAnnotation]
}

sealed trait FunctionMember extends FunctionLikeMember with FunctionLikeMemberOrLit

sealed trait Location extends Expr

case class GlobalVarDecl(left: Vector[GlobalVar],
                         // statements involved in declaring the variables on [left]; should include an assignment
                         // to every variable on the left
                         declStmts: Vector[Stmt]
                        )(val info: Source.Parser.Info) extends Member {
  require(declStmts.nonEmpty)
}

case class GlobalConstDecl(left: GlobalConst, right: Lit)(val info: Source.Parser.Info) extends Member

case class Field(name: String, typ: Type, ghost: Boolean)(val info: Source.Parser.Info) extends Node

case class Method(
                 override val receiver: Parameter.In,
                 override val name: MethodProxy,
                 override val args: Vector[Parameter.In],
                 override val results: Vector[Parameter.Out],
                 override val pres: Vector[Assertion],
                 override val posts: Vector[Assertion],
                 override val terminationMeasures: Vector[TerminationMeasure],
                 override val backendAnnotations: Vector[BackendAnnotation],
                 body: Option[MethodBody]
                 )(val info: Source.Parser.Info) extends Member with MethodMember

case class PureMethod(
                       override val receiver: Parameter.In,
                       override val name: MethodProxy,
                       override val args: Vector[Parameter.In],
                       override val results: Vector[Parameter.Out],
                       override val pres: Vector[Assertion],
                       override val posts: Vector[Assertion],
                       override val terminationMeasures: Vector[TerminationMeasure],
                       override val backendAnnotations: Vector[BackendAnnotation],
                       body: Option[Expr],
                       isOpaque: Boolean
                     )(val info: Source.Parser.Info) extends Member with MethodMember {
  require(results.size <= 1)
}

case class BuiltInMethod(
                          receiverT: Type,
                          override val tag: BuiltInMethodTag,
                          override val name: MethodProxy,
                          override val argsT: Vector[Type]
                        )(val info: Source.Parser.Info) extends BuiltInMember with MethodLikeMember {
  require(receiverT.addressability == Addressability.Exclusive)
  require(argsT.forall(_.addressability == Addressability.Exclusive))
}

case class MethodSubtypeProof(
                               subProxy: MethodProxy,
                               superT: InterfaceT,
                               superProxy: MethodProxy,
                               receiver: Parameter.In,
                               args: Vector[Parameter.In],
                               results: Vector[Parameter.Out],
                               body: Option[Block] // empty if it is generated
                             )(val info: Source.Parser.Info) extends Member

case class PureMethodSubtypeProof(
                               subProxy: MethodProxy,
                               superT: InterfaceT,
                               superProxy: MethodProxy,
                               receiver: Parameter.In,
                               args: Vector[Parameter.In],
                               results: Vector[Parameter.Out],
                               body: Option[Expr] // empty if it is generated
                             )(val info: Source.Parser.Info) extends Member {
  require(results.size <= 1)
}


case class Function(
                     override val name: FunctionProxy,
                     override val args: Vector[Parameter.In],
                     override val results: Vector[Parameter.Out],
                     override val pres: Vector[Assertion],
                     override val posts: Vector[Assertion],
                     override val terminationMeasures: Vector[TerminationMeasure],
                     override val backendAnnotations: Vector[BackendAnnotation],
                     body: Option[MethodBody]
                   )(val info: Source.Parser.Info) extends Member with FunctionMember

case class PureFunction(
                         override val name: FunctionProxy,
                         override val args: Vector[Parameter.In],
                         override val results: Vector[Parameter.Out],
                         override val pres: Vector[Assertion],
                         override val posts: Vector[Assertion],
                         override val terminationMeasures: Vector[TerminationMeasure],
                         override val backendAnnotations: Vector[BackendAnnotation],
                         body: Option[Expr],
                         isOpaque: Boolean
                       )(val info: Source.Parser.Info) extends Member with FunctionMember {
  require(results.size <= 1)
}

case class BuiltInFunction(
                          override val tag: BuiltInFunctionTag,
                          override val name: FunctionProxy,
                          override val argsT: Vector[Type]
                        )(val info: Source.Parser.Info) extends BuiltInMember with FunctionLikeMember {
  require(argsT.forall(_.addressability == Addressability.Exclusive))
}

sealed trait PredicateMember extends Member {
  def name: PredicateProxy
}

sealed trait FPredicateLikeMember extends Member {
  def name: FPredicateProxy
}

case class FPredicate(
                       override val name: FPredicateProxy,
                       args: Vector[Parameter.In],
                       body: Option[Assertion]
                     )(val info: Source.Parser.Info) extends FPredicateLikeMember with PredicateMember

case class BuiltInFPredicate(
                              override val tag: BuiltInFPredicateTag,
                              override val name: FPredicateProxy,
                              override val argsT: Vector[Type]
                            )(val info: Source.Parser.Info) extends BuiltInMember with FPredicateLikeMember {
  require(argsT.forall(_.addressability == Addressability.Exclusive))
}

sealed trait MPredicateLikeMember extends Member {
  def name: MPredicateProxy
}

case class MPredicate(
                     receiver: Parameter.In,
                     override val name: MPredicateProxy,
                     args: Vector[Parameter.In],
                     body: Option[Assertion]
                     )(val info: Source.Parser.Info) extends MPredicateLikeMember with PredicateMember

case class BuiltInMPredicate(
                            receiverT: Type,
                            override val tag: BuiltInMPredicateTag,
                            override val name: MPredicateProxy,
                            override val argsT: Vector[Type]
                            )(val info: Source.Parser.Info) extends BuiltInMember with MPredicateLikeMember {
  require(receiverT.addressability == Addressability.Exclusive)
  require(argsT.forall(_.addressability == Addressability.Exclusive))
}

case class DomainDefinition(name: String, funcs: Vector[DomainFunc], axioms: Vector[DomainAxiom])(val info: Source.Parser.Info) extends Member
case class DomainAxiom(expr: Expr)(val info: Source.Parser.Info) extends Node
case class DomainFunc(
                       name: DomainFuncProxy,
                       args: Vector[Parameter.In],
                       results: Parameter.Out
                     )(val info: Source.Parser.Info) extends Node

case class AdtDefinition(name: String, clauses: Vector[AdtClause])(val info: Source.Parser.Info) extends Member
case class AdtClause(name: AdtClauseProxy, args: Vector[Field])(val info: Source.Parser.Info) extends Node

sealed trait Stmt extends Node

/** This node serves as a target of encoding extensions. See [[viper.gobra.translator.encodings.combinators.TypeEncoding.Extension]]*/
case class MethodBody(
                       decls: Vector[BlockDeclaration],
                       seqn: MethodBodySeqn,
                       postprocessing: Vector[Stmt] = Vector.empty,
                     )(val info: Source.Parser.Info) extends Stmt

object MethodBody {
  def empty(info: Source.Parser.Info): MethodBody = {
    MethodBody(Vector.empty, MethodBodySeqn(Vector.empty)(info), Vector.empty)(info)
  }
}

/**
  * This node serves as a target of encoding extensions. See [[viper.gobra.translator.encodings.combinators.TypeEncoding.Extension]]
  * Return statements jump exactly to the end of the encoding of this statement.
  * */
case class MethodBodySeqn(stmts: Vector[Stmt])(val info: Source.Parser.Info) extends Stmt

case class Block(
                  decls: Vector[BlockDeclaration],
                  stmts: Vector[Stmt]
                )(val info: Source.Parser.Info) extends Stmt {
  def toMethodBody: MethodBody = MethodBody(decls, MethodBodySeqn(stmts)(info))(info)
}

case class Seqn(stmts: Vector[Stmt])(val info: Source.Parser.Info) extends Stmt

case class Label(id: LabelProxy)(val info: Source.Parser.Info) extends Stmt

/**
  * 'label' corresponds to the loop label we want to continue. In case it is a normal
  * continue node, it is None.
  * The continue node will be replaced by a goto statement to 'escLabel' which has
  * been placed properly while desugaring for loops.
  */
case class Continue(label: Option[String], escLabel: String)(val info: Source.Parser.Info) extends Stmt

/**
  * 'label' corresponds to the loop label we want to break out of. In case it is a normal
  * break node, it is 'None'.
  * The break node will be replaced by a goto statement to 'escLabel'.
  */
case class Break(label: Option[String], escLabel: String)(val info: Source.Parser.Info) extends Stmt

case class If(cond: Expr, thn: Stmt, els: Stmt)(val info: Source.Parser.Info) extends Stmt

case class While(cond: Expr, invs: Vector[Assertion], terminationMeasure: Option[TerminationMeasure], body: Stmt)(val info: Source.Parser.Info) extends Stmt

case class Initialization(left: AssignableVar)(val info: Source.Parser.Info) extends Stmt

sealed trait Assignment extends Stmt

case class SingleAss(left: Assignee, right: Expr)(val info: Source.Parser.Info) extends Assignment

sealed trait Assignee extends Node {
  def op: Expr
  override def info: Parser.Info = op.info
}

object Assignee {
  def unapply(x: Assignee): Some[Expr] = Some(x.op)
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

sealed trait MakeStmt extends Stmt {
  val target: LocalVar
  val typeParam: Type
}

case class MakeSlice(override val target: LocalVar, override val typeParam: SliceT, lenArg: Expr, capArg: Option[Expr])(val info: Source.Parser.Info) extends MakeStmt
case class MakeChannel(override val target: LocalVar, override val typeParam: ChannelT, bufferSizeArg: Option[Expr], isChannel: MPredicateProxy, bufferSize: MethodProxy)(val info: Source.Parser.Info) extends MakeStmt
case class MakeMap(override val target: LocalVar, override val typeParam: MapT, initialSpaceArg: Option[Expr])(val info: Source.Parser.Info) extends MakeStmt

case class New(target: LocalVar, expr: Expr)(val info: Source.Parser.Info) extends Stmt

/**
  * Type assertion that does not fail.
  * 'successTarget' gets assigned a boolean, indicating whether the cast is valid or not.
  * If the cast is not valid, 'resTarget' gets assigned the default value of 'typ'.
  * The statement does not have side-effects.
  * */
case class SafeTypeAssertion(resTarget: LocalVar, successTarget: LocalVar, expr: Expr, typ: Type)(val info: Source.Parser.Info) extends Stmt


case class FunctionCall(targets: Vector[LocalVar], func: FunctionProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt with Deferrable
case class MethodCall(targets: Vector[LocalVar], recv: Expr, meth: MethodProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt with Deferrable
case class ClosureCall(targets: Vector[LocalVar], closure: Expr, args: Vector[Expr], spec: ClosureSpec)(val info: Source.Parser.Info) extends Stmt with Deferrable

case class GoFunctionCall(func: FunctionProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt
case class GoMethodCall(recv: Expr, meth: MethodProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends Stmt
case class GoClosureCall(closure: Expr, args: Vector[Expr], spec: ClosureSpec)(val info: Source.Parser.Info) extends Stmt

sealed trait Deferrable extends Stmt
case class Defer(stmt: Deferrable)(val info: Source.Parser.Info) extends Stmt

case class Return()(val info: Source.Parser.Info) extends Stmt

case class Assert(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Refute(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Assume(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Inhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt
case class Exhale(ass: Assertion)(val info: Source.Parser.Info) extends Stmt

case class Fold(acc: Access)(val info: Source.Parser.Info) extends Stmt with Deferrable {
  require(acc.e.isInstanceOf[Accessible.Predicate])
  lazy val op: PredicateAccess = acc.e.asInstanceOf[Accessible.Predicate].op
}



case class Unfold(acc: Access)(val info: Source.Parser.Info) extends Stmt with Deferrable {
  require(acc.e.isInstanceOf[Accessible.Predicate])
  lazy val op: PredicateAccess = acc.e.asInstanceOf[Accessible.Predicate].op
}

case class PackageWand(wand: MagicWand, block: Option[Stmt])(val info: Source.Parser.Info) extends Stmt

case class ApplyWand(wand: MagicWand)(val info: Source.Parser.Info) extends Stmt

case class Outline(
                    name: String,
                    pres: Vector[Assertion],
                    posts: Vector[Assertion],
                    terminationMeasures: Vector[TerminationMeasure],
                    val backendAnnotations: Vector[BackendAnnotation],
                    body: Stmt,
                    trusted: Boolean,
                  )(val info: Source.Parser.Info) extends Stmt

case class Send(channel: Expr, expr: Expr, sendChannel: MPredicateProxy, sendGivenPerm: MethodProxy, sendGotPerm: MethodProxy)(val info: Source.Parser.Info) extends Stmt

/**
  * Channel receive operation that does not only return the received message but also a boolean result whether
  * receive operation was successful. Thus, receiving a zero value from a closed or empty channel can be
  * distinguished from a zero value sent with a successful channel send operation
  */
case class SafeReceive(resTarget: LocalVar, successTarget: LocalVar, channel: Expr, recvChannel: MPredicateProxy, recvGivenPerm: MethodProxy, recvGotPerm: MethodProxy, closed: MPredicateProxy)(val info: Source.Parser.Info) extends Stmt

/**
  * Map lookup operation that returns the result of the lookup or default value if the key is not in the map,
  * and a boolean values stating whether the key is in the map.
  */
case class SafeMapLookup(resTarget: LocalVar, successTarget: LocalVar, mapLookup: IndexedExp)(val info: Source.Parser.Info) extends Stmt

case class PatternMatchExp(exp: Expr, typ: Type, cases: Vector[PatternMatchCaseExp], default: Option[Expr])(val info: Source.Parser.Info) extends Expr

case class PatternMatchCaseExp(mExp: MatchPattern, exp: Expr)(val info: Source.Parser.Info) extends Node

case class PatternMatchAss(exp: Expr, cases: Vector[PatternMatchCaseAss], default: Option[Assertion])(val info: Source.Parser.Info) extends Assertion

case class PatternMatchCaseAss(mExp: MatchPattern, ass: Assertion)(val info: Source.Parser.Info) extends Node

case class PatternMatchStmt(exp: Expr, cases: Vector[PatternMatchCaseStmt], strict: Boolean)(val info: Source.Parser.Info) extends Stmt

case class PatternMatchCaseStmt(mExp: MatchPattern, body: Stmt)(val info: Source.Parser.Info) extends Node

sealed trait MatchPattern extends Node

case class MatchValue(exp: Expr)(val info: Source.Parser.Info) extends MatchPattern

case class MatchBindVar(name: String, typ: Type)(val info: Source.Parser.Info) extends MatchPattern

case class MatchAdt(clause: AdtClauseT, expr: Vector[MatchPattern])(val info: Source.Parser.Info) extends MatchPattern

case class MatchWildcard()(val info: Source.Parser.Info) extends MatchPattern

sealed trait Assertion extends Node

case class SepAnd(left: Assertion, right: Assertion)(val info: Source.Parser.Info) extends Assertion

case class ExprAssertion(exp: Expr)(val info: Source.Parser.Info) extends Assertion

case class Implication(left: Expr, right: Assertion)(val info: Source.Parser.Info) extends Assertion

case class Access(e: Accessible, p: Expr)(val info: Source.Parser.Info) extends Assertion {
  require(p.typ.isInstanceOf[PermissionT], s"expected an expression of permission type but got $p.typ")
}

sealed trait TerminationMeasure extends Assertion {
  val cond: Option[Expr]
  val info: Source.Parser.Info
}

sealed trait ItfMethodMeasure extends TerminationMeasure
sealed trait NonItfMethodMeasure extends TerminationMeasure
sealed trait WildcardMeasure extends TerminationMeasure
sealed trait TupleTerminationMeasure extends TerminationMeasure {
  val tuple: Vector[Node]
  require(tuple.forall(x => x.isInstanceOf[Expr] || x.isInstanceOf[Access]), s"Unexpected tuple $tuple")
}

case class ItfMethodWildcardMeasure(cond: Option[Expr])(val info: Source.Parser.Info)
  extends ItfMethodMeasure with WildcardMeasure
case class NonItfMethodWildcardMeasure(cond: Option[Expr])(val info: Source.Parser.Info)
  extends NonItfMethodMeasure with WildcardMeasure
case class ItfTupleTerminationMeasure(tuple: Vector[Node], cond: Option[Expr])(val info: Source.Parser.Info)
  extends ItfMethodMeasure with TupleTerminationMeasure
case class NonItfTupleTerminationMeasure(tuple: Vector[Node], cond: Option[Expr])(val info: Source.Parser.Info)
  extends NonItfMethodMeasure with TupleTerminationMeasure

sealed trait Accessible extends Node {
  def op: Node
  override def info: Parser.Info = op.info
}

object Accessible {
  case class Predicate(op: PredicateAccess) extends Accessible with TriggerExpr
  case class ExprAccess(op: Expr) extends Accessible
  case class Address(op: Location) extends Accessible {
    require(op.typ.addressability == Addressability.Shared, s"expected shared location, but got $op :: ${op.typ}")
  }
  case class PredExpr(op: PredExprInstance) extends Accessible
}

sealed trait PredicateAccess extends Node

case class FPredicateAccess(pred: FPredicateProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends PredicateAccess
case class MPredicateAccess(recv: Expr, pred: MPredicateProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends PredicateAccess
case class MemoryPredicateAccess(arg: Expr)(val info: Source.Parser.Info) extends PredicateAccess

sealed trait Expr extends Node with Typed with TriggerExpr

object Expr {
  def getSubExpressions(x: Expr): Set[Expr] = {
    def aux(x: Expr): Set[Expr] = x.subnodes.collect{ case e: Expr => e }.toSet
    def auxClosed(x: Expr): Set[Expr] = aux(x).flatMap(auxClosed) + x
    aux(x).flatMap(auxClosed) + x
  }
}

case class Unfolding(acc: Access, in: Expr)(val info: Source.Parser.Info) extends Expr {
  require(acc.e.isInstanceOf[Accessible.Predicate])
  lazy val op: PredicateAccess = acc.e.asInstanceOf[Accessible.Predicate].op
  override def typ: Type = in.typ
  require(typ.addressability == Addressability.unfolding(in.typ.addressability))
}

case class PureLet(left: LocalVar, right: Expr, in: Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = in.typ
}

case class Let(left: LocalVar, right: Expr, in: Assertion)(val info: Source.Parser.Info) extends Assertion

case class Old(operand: Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = operand.typ.withAddressability(Addressability.rValue)
}

case class LabeledOld(label: LabelProxy, operand: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = operand.typ.withAddressability(Addressability.rValue)
}

case class Conditional(cond: Expr, thn: Expr, els: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr

trait TriggerExpr extends Node
case class Trigger(exprs: Vector[TriggerExpr])(val info: Source.Parser.Info) extends Node

case class PureForall(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = BoolT(Addressability.rValue)
}

case class SepForall(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Assertion)(val info: Source.Parser.Info) extends Assertion

case class MagicWand(left: Assertion, right: Assertion)(val info: Source.Parser.Info) extends Assertion

case class Exists(vars: Vector[BoundVar], triggers: Vector[Trigger], body: Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = BoolT(Addressability.rValue)
}

sealed trait Permission extends Expr {
  override def typ: Type = PermissionT(Addressability.rValue)
}

case class FullPerm(info: Source.Parser.Info) extends Permission
case class NoPerm(info: Source.Parser.Info) extends Permission
case class FractionalPerm(left: Expr, right: Expr)(val info: Source.Parser.Info) extends Permission
case class WildcardPerm(info: Source.Parser.Info) extends Permission
case class CurrentPerm(acc: Accessible)(val info: Source.Parser.Info) extends Permission
case class PermMinus(exp: Expr)(val info: Source.Parser.Info) extends Permission
case class PermAdd(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("+") with Permission
case class PermSub(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("-") with Permission
case class PermMul(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("*") with Permission
case class PermDiv(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("/") with Permission
// Comparison expressions
case class PermLtCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("<") with BoolOperation
case class PermLeCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("<=") with BoolOperation
case class PermGtCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">") with BoolOperation
case class PermGeCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">=") with BoolOperation

/* ** Type related expressions */

case class TypeAssertion(exp: Expr, arg: Type)(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = arg.withAddressability(Addressability.rValue)
}

case class TypeOf(exp: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = SortT
}

case class IsComparableType(exp: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = BoolT(Addressability.rValue)
}

case class IsComparableInterface(exp: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = BoolT(Addressability.rValue)
}

case class IsBehaviouralSubtype(subtype: Expr, supertype: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = BoolT(Addressability.rValue)
}

/** Boxes an expression into an interface. */
case class ToInterface(exp: Expr, typ: Type)(val info: Source.Parser.Info) extends Expr

sealed trait TypeExpr extends Expr {
  override val typ: Type = SortT
}

case class PointerTExpr(elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class DefinedTExpr(name: String)(val info: Source.Parser.Info) extends TypeExpr


case class BoolTExpr()(val info: Source.Parser.Info) extends TypeExpr
case class StringTExpr()(val info: Source.Parser.Info) extends TypeExpr
case class Float32TExpr()(val info: Source.Parser.Info) extends TypeExpr
case class Float64TExpr()(val info: Source.Parser.Info) extends TypeExpr
case class IntTExpr(kind: IntegerKind)(val info: Source.Parser.Info) extends TypeExpr
case class StructTExpr(fields: Vector[(String, Expr, Boolean)])(val info: Source.Parser.Info) extends TypeExpr
case class ArrayTExpr(length: Expr, elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class SliceTExpr(elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class MapTExpr(keys: Expr, elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class PermTExpr()(val info: Source.Parser.Info) extends TypeExpr
case class SequenceTExpr(elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class SetTExpr(elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class MultisetTExpr(elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class MathMapTExpr(keys: Expr, elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class OptionTExpr(elems: Expr)(val info: Source.Parser.Info) extends TypeExpr
case class TupleTExpr(elems: Vector[Expr])(val info: Source.Parser.Info) extends TypeExpr

/* ** Information Flow  */

case class Low(exp: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = BoolT(Addressability.rValue)
}

case class LowContext()(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = BoolT(Addressability.rValue)
}

/* ** Higher-order predicate expressions */

case class PredicateConstructor(proxy: PredicateProxy, proxyT: PredT, args: Vector[Option[Expr]])(val info: Source.Parser.Info) extends Expr {
  override val typ: Type = PredT(proxyT.args.zip(args).filter(_._2.isEmpty).map(_._1), Addressability.rValue)
}

case class PredExprInstance(base: Expr, args: Vector[Expr])(val info: Source.Parser.Info) extends Node

case class PredExprFold(base: PredicateConstructor, args: Vector[Expr], p: Expr)(val info: Source.Parser.Info) extends Stmt with Deferrable
case class PredExprUnfold(base: PredicateConstructor, args: Vector[Expr], p: Expr)(val info: Source.Parser.Info) extends Stmt with Deferrable


/* ** Option type expressions */

/**
  * The 'none' constructor for option types of type `elem`.
  */
case class OptionNone(elem : Type)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = OptionT(elem, Addressability.rValue)
}

/**
  * The 'some(`exp`)' constructor for option types.
  */
case class OptionSome(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = OptionT(exp.typ, Addressability.rValue)
}

/**
  * The 'option(`exp`)' projection function, where `exp` is expected to be an option type.
  */
case class OptionGet(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = exp.typ match {
    case OptionT(t, _) => t
    case t => Violation.violation(s"expected an option type, but got $t")
  }
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
  * of an array type or a sequence type or a set.
  */
case class Length(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = IntT(Addressability.rValue)
}

/**
  * Represents the "cap(`exp`)" in Go, which gives
  * the capacity of `exp` according to its type.
  */
case class Capacity(exp : Expr)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = IntT(Addressability.rValue)
}

/**
  * Represents indexing into an array "`base`[`index`]",
  * where `base` is expected to be of an array or sequence type
  * and `index` of an integer type. `baseUnderlyingType` is the underlyingType of `base`'s type.
  */
case class IndexedExp(base : Expr, index : Expr, baseUnderlyingType: Type)(val info : Source.Parser.Info) extends Expr with Location {
  override val typ : Type = baseUnderlyingType match {
    case t: ArrayT => t.elems
    case PointerT(t: ArrayT, _) => t.elems
    case t: SequenceT => t.t
    case t: SliceT => t.elems
    case t: MapT => t.values
    case t: MathMapT => t.values
    case _: StringT => IntT(Addressability.Exclusive, TypeBounds.Byte)
    case t => Violation.violation(s"expected an array, map or sequence type, but got $t")
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
  * A (mathematical) sequence literal "seq[`memberType`] { k_0:e_0, ..., k_n:e_n }",
  * where `elems` is the map of key-value pairs that constitutes the members
  * of the literal. Every "e_i" is expected to be of type `memberType`.
  */
case class SequenceLit(length : BigInt, memberType : Type, elems : Map[BigInt, Expr])(val info : Source.Parser.Info) extends CompositeLit {
  require(elems.forall(e => 0 <= e._1 && e._1 < length), "All elements should be within bounds")
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
case class SequenceAppend(left : Expr, right : Expr, typ: Type)(val info: Source.Parser.Info) extends BinaryExpr("++")

/**
  * Denotes a ghost collection update "`col`[`left` = `right`]", which results in a
  * collection equal to `col` but 'updated' to have `right` at the `left` position.
  * `baseUnderlyingType` is the underlyingType of `base`'s type
  */
case class GhostCollectionUpdate(base : Expr, left : Expr, right : Expr, baseUnderlyingType: Type)(val info: Source.Parser.Info) extends Expr {
  override val typ : Type = base.typ.withAddressability(Addressability.rValue)
}

/**
  * Represents a _sequence drop expression_ roughly of
  * the form "`left`[`right`:]".
  * Here `left` is the base sequence and `right` an integer
  * denoting the number of elements to drop from `left`.
  */
case class SequenceDrop(left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `left`. */
  override val typ : Type = left.typ.withAddressability(Addressability.rValue)
}

/**
  * Represents a _sequence take operation_ roughly of
  * the form "`left`[:`right`]", where `left` is the base sequence
  * and `right` an integer denoting the number of elements to
  * take from `left`.
  */
case class SequenceTake(left : Expr, right : Expr)(val info: Source.Parser.Info) extends Expr {
  /** Is equal to the type of `left`. */
  override val typ : Type = left.typ.withAddressability(Addressability.rValue)
}

/**
  * Represents the conversion of a collection of type 't',
  * represented by `expr`, to a (mathematical) sequence of type 't'.
  * Here `expr` is assumed to be either a sequence or an exclusive array.
  */
case class SequenceConversion(expr: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ : Type = expr.typ match {
    case t: SequenceT => t
    case t: ArrayT => t.sequence
    case OptionT(t, addr) => SequenceT(t, addr)
    case t => Violation.violation(s"expected a sequence or exclusive array type. but got $t")
  }
}


/* ** Unordered collection expressions */

/**
  * Represents a (multi)set union "`left` union `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class Union(left : Expr, right : Expr, typ: Type)(val info : Source.Parser.Info) extends BinaryExpr("union")

/**
  * Represents a (multi)set intersection "`left` intersection `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class Intersection(left : Expr, right : Expr, typ: Type)(val info : Source.Parser.Info) extends BinaryExpr("intersection")

/**
  * Represents a (multi)set difference "`left` setminus `right`",
  * where `left` and `right` should be (multi)sets of identical types.
  */
case class SetMinus(left : Expr, right : Expr, typ: Type)(val info : Source.Parser.Info) extends BinaryExpr("setminus")

/**
  * Represents a subset relation "`left` subset `right`", where
  * `left` and `right` are assumed to be sets of comparable types.
  */
case class Subset(left : Expr, right : Expr)(val info : Source.Parser.Info) extends BinaryExpr("subset") {
  override val typ : Type = BoolT(Addressability.rValue)
}

/**
  * Represents a membership expression "`left` elem `right`".
  * Here `right` should be a ghost collection (that is,
  * a sequence, set, or multiset) of a type that is compatible
  * with the one of `left`.
  */
case class Contains(left : Expr, right : Expr)(val info: Source.Parser.Info) extends BinaryExpr("elem") {
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

/* ** Mathematical Map expressions */

/**
  * Represents a mathematical map literal "mmap[`keys`]`values` { k_0: e_0, ..., k_n: e_n }",
  * where `entries` constitutes the sequence of entries of the map "{(k_0: e_0), ..., (k_n: e_n)}". The expressions `k_i` should have type `keys`
  * and the expressions `e_i` should have type `values`.
  */
case class MathMapLit(keys : Type, values : Type, entries : Seq[(Expr, Expr)])(val info : Source.Parser.Info) extends CompositeLit {
  override val typ : Type = MathMapT(keys, values, Addressability.literal)
}

case class MapKeys(exp : Expr, expUnderlyingType: Type)(val info : Source.Parser.Info) extends Expr {
  override val typ : Type = expUnderlyingType match {
    case t: MathMapT => SetT(t.keys, Addressability.mathDataStructureElement)
    case t: MapT => SetT(t.keys, Addressability.rValue)
    case _ => violation(s"unexpected type ${exp.typ}")
  }
}

case class MapValues(exp : Expr, expUnderlyingType: Type)(val info : Source.Parser.Info) extends Expr {
  override val typ : Type = expUnderlyingType match {
    case t: MathMapT => SetT(t.keys, Addressability.mathDataStructureElement)
    case t: MapT => SetT(t.keys, Addressability.rValue)
    case _ => violation(s"unexpected type ${exp.typ}")
  }
}

/**
 * Represents the conversion of a value of type 'map[k]v' to a mathematical map with type 'dict[k]v'
 */
case class MapConversion(expr: Expr)(val info: Source.Parser.Info) extends Expr {
  override val typ : Type = expr.typ match {
    case MapT(k, v, _) => MathMapT(k, v, Addressability.conversionResult)
    case t => Violation.violation(s"expected a map but got $t")
  }
}


case class PureFunctionCall(func: FunctionProxy, args: Vector[Expr], typ: Type, reveal: Boolean = false)(val info: Source.Parser.Info) extends Expr
case class PureMethodCall(recv: Expr, meth: MethodProxy, args: Vector[Expr], typ: Type, reveal: Boolean = false)(val info: Source.Parser.Info) extends Expr
case class PureClosureCall(closure: Expr, args: Vector[Expr], spec: ClosureSpec, typ: Type)(val info: Source.Parser.Info) extends Expr
case class DomainFunctionCall(func: DomainFuncProxy, args: Vector[Expr], typ: Type)(val info: Source.Parser.Info) extends Expr

case class Deref(exp: Expr, underlyingTypeExpr: Type)(val info: Source.Parser.Info) extends Expr with Location {
  require(underlyingTypeExpr.isInstanceOf[PointerT])
  override val typ: Type = underlyingTypeExpr.asInstanceOf[PointerT].t
}

case class Ref(ref: Addressable, typ: PointerT)(val info: Source.Parser.Info) extends Expr with Location

object Ref {
  def apply(ref: Expr)(info: Source.Parser.Info): Ref = {
    require(ref.typ.addressability == Addressability.Shared)

    val pointerT = PointerT(ref.typ, Addressability.reference)
    ref match {
      case x: LocalVar     => Ref(Addressable.Var(x), pointerT)(info)
      case x: GlobalVar    => Ref(Addressable.GlobalVar(x), pointerT)(info)
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
  import viper.gobra.ast.{internal => in}

  case class Var(op: LocalVar) extends Addressable
  case class GlobalVar(op: in.GlobalVar) extends Addressable
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

case class AdtDestructor(base: Expr, field: Field)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = field.typ
}

case class AdtDiscriminator(base: Expr, clause: AdtClauseProxy)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = BoolT(Addressability.literal)
}

sealed trait BoolOperation extends Expr {
  override val typ: Type = BoolT(Addressability.rValue)
}

sealed trait IntOperation extends Expr {
  override def typ: Type = IntT(Addressability.rValue)
}

case class Negation(operand: Expr)(val info: Source.Parser.Info) extends BoolOperation

sealed abstract class BinaryExpr(val operator: String) extends Expr {
  def left: Expr
  def right: Expr
}

sealed abstract class BinaryIntExpr(override val operator: String) extends BinaryExpr(operator) with IntOperation {
  override def typ: Type = (left.typ, right.typ) match {
    // should always produce an exclusive val. from the go spec:
    // (...) must be addressable, that is, either a variable, pointer indirection, or slice indexing operation;
    // or a field selector of an addressable struct operand; or an array indexing operation of an addressable array.
    // As an exception to the addressability requirement, x may also be a (possibly parenthesized) composite literal.
    case (IntT(_, kind1), IntT(_, kind2)) => IntT(Addressability.Exclusive, TypeBounds.merge(kind1, kind2))

    // A binary expression may have one operand of a defined type T and another operand that is an unbounded integer.
    // If T's underlying type is an integer type, then the result of the expression should be of type T.
    // Here, the underlying type of a defined type is not checked, as the information is not available at this point.
    // However, this should not pose a problem assuming that the original program has been type-checked before the
    // translation to the internal language.
    case (x, IntT(_, UnboundedInteger)) if x.isInstanceOf[DefinedT] => x.withAddressability(Addressability.Exclusive)
    case (IntT(_, UnboundedInteger), y) if y.isInstanceOf[DefinedT] => y.withAddressability(Addressability.Exclusive)
    case (x, y) if x.equalsWithoutMod(y) => x.withAddressability(Addressability.Exclusive)
    case (l, r) => violation(s"cannot merge types $l and $r")

  }
}

object BinaryExpr {
  def unapply(arg: BinaryExpr): Some[(Expr, String, Expr, Type)] =
    Some((arg.left, arg.operator, arg.right, arg.typ))
}

case class EqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)      extends BinaryExpr("==") with BoolOperation
case class UneqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)    extends BinaryExpr("!=") with BoolOperation
case class GhostEqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)   extends BinaryExpr("===") with BoolOperation
case class GhostUneqCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("!==") with BoolOperation
case class LessCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)    extends BinaryExpr("<" ) with BoolOperation
case class AtMostCmp(left: Expr, right: Expr)(val info: Source.Parser.Info)  extends BinaryExpr("<=") with BoolOperation
case class GreaterCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">" ) with BoolOperation
case class AtLeastCmp(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr(">=") with BoolOperation

case class And(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("&&") with BoolOperation
case class Or(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryExpr("||") with BoolOperation

case class Add(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("+")
case class Sub(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("-")
case class Mul(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("*")
case class Mod(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("%")
case class Div(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("/")

/* Bitwise Operators */
case class BitAnd(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("&")
case class BitOr(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("|")
case class BitXor(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("^")
case class BitClear(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("&^")
case class ShiftLeft(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr("<<") {
  override val typ: Type = left.typ
}
case class ShiftRight(left: Expr, right: Expr)(val info: Source.Parser.Info) extends BinaryIntExpr(">>") {
  override val typ: Type = left.typ
}
case class BitNeg(op: Expr)(val info: Source.Parser.Info) extends IntOperation

/*
 * Convert 'expr' to non-interface type 'newType'. If 'newType' is
 * an interface type, then 'ToInterface' should be used instead.
 */
 // TODO: maybe unify with ToInterface at some point
case class Conversion(newType: Type, expr: Expr)(val info: Source.Parser.Info) extends Expr {
  override def typ: Type = newType
}

case class EffectfulConversion(target: LocalVar, newType: Type, expr: Expr)(val info: Source.Parser.Info) extends Stmt

case class Receive(channel: Expr, recvChannel: MPredicateProxy, recvGivenPerm: MethodProxy, recvGotPerm: MethodProxy)(val info: Source.Parser.Info) extends Expr {
  require(channel.typ.isInstanceOf[ChannelT])
  override def typ: Type = channel.typ.asInstanceOf[ChannelT].elem
}

sealed trait Lit extends Expr

case class DfltVal(typ: Type)(val info: Source.Parser.Info) extends Expr

case class IntLit(v: BigInt, kind: IntegerKind = UnboundedInteger, base: NumBase = Decimal)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = IntT(Addressability.literal, kind)
}

case class PermLit(dividend: BigInt, divisor: BigInt)(val info: Source.Parser.Info) extends Lit with Permission {
  require(divisor != 0)
  override def typ: Type = PermissionT(Addressability.literal)
}

case class BoolLit(b: Boolean)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = BoolT(Addressability.literal)
}

case class StringLit(s: String)(val info: Source.Parser.Info) extends Lit {
  override def typ: Type = StringT(Addressability.literal)
}

case class NilLit(typ: Type)(val info: Source.Parser.Info) extends Lit

/* ** Closures */
sealed trait FunctionLitLike extends Lit with FunctionLikeMemberOrLit  {
  def name: FunctionLitProxy
  def captured: Vector[(Expr, Parameter.In)]
}

case class FunctionLit(
                     override val name: FunctionLitProxy,
                     override val args: Vector[Parameter.In],
                     override val captured: Vector[(Expr, Parameter.In)],
                     override val results: Vector[Parameter.Out],
                     override val pres: Vector[Assertion],
                     override val posts: Vector[Assertion],
                     override val terminationMeasures: Vector[TerminationMeasure],
                     override val backendAnnotations: Vector[BackendAnnotation],
                     body: Option[MethodBody]
                   )(val info: Source.Parser.Info) extends FunctionLitLike {
  override def typ: Type = FunctionT(args.map(_.typ), results.map(_.typ), Addressability.literal)
}

case class PureFunctionLit(
                         override val name: FunctionLitProxy,
                         override val args: Vector[Parameter.In],
                         override val captured: Vector[(Expr, Parameter.In)],
                         override val results: Vector[Parameter.Out],
                         override val pres: Vector[Assertion],
                         override val posts: Vector[Assertion],
                         override val terminationMeasures: Vector[TerminationMeasure],
                         override val backendAnnotations: Vector[BackendAnnotation],
                         body: Option[Expr]
                       )(val info: Source.Parser.Info) extends FunctionLitLike {
  override def typ: Type = FunctionT(args.map(_.typ), results.map(_.typ), Addressability.literal)
  require(results.size <= 1)
}

case class ClosureImplements(closure: Expr, spec: ClosureSpec)(override val info: Source.Parser.Info) extends Expr {
  override def typ: Type = BoolT(Addressability.rValue)
}

case class ClosureSpec(func: FunctionMemberOrLitProxy, params: Map[Int, Expr])(override val info: Source.Parser.Info) extends Node {
  lazy val paramValues: Vector[Option[Expr]] =
    if (params.isEmpty) Vector.empty else (1 to params.keySet.max).map(idx => params.get(idx)).toVector
}

case class SpecImplementationProof(closure: Expr, spec: ClosureSpec, body: Block, pres: Vector[Assertion], posts: Vector[Assertion])
                                  (override val info: Source.Parser.Info) extends Stmt

case class ClosureObject(func: FunctionLitProxy, override val typ: Type)(override val info: Source.Parser.Info) extends Expr

case class FunctionObject(func: FunctionProxy, override val typ: Type)(override val info: Source.Parser.Info) extends Expr

case class MethodObject(recv: Expr, meth: MethodProxy, override val typ: Type)(override val info: Source.Parser.Info) extends Expr


/**
  * Represents (full) slice expressions "`base`[`low`:`high`:`max`]".
  * Only the `max` component is optional at this point.
  * Any slicing expression "a[:j]" is assumed to be desugared into "a[0:j]",
  * and any expression "a[i:]" is assumed to be desugared into "a[i:len(a)]".
  * `baseUnderlyingType` is the underlyingType of `base`'s type.
  */
case class Slice(base : Expr, low : Expr, high : Expr, max : Option[Expr], baseUnderlyingType: Type)(val info : Source.Parser.Info) extends Expr {
  override def typ : Type = baseUnderlyingType match {
    case t: ArrayT => SliceT(t.elems, Addressability.sliceElement)
    case _: SliceT | _: StringT => base.typ
    case PointerT(t: ArrayT, _) => SliceT(t.elems, Addressability.sliceElement)
    case t => Violation.violation(s"expected an array, slice or string type, but got $t")
  }
}

case class Tuple(args: Vector[Expr])(val info: Source.Parser.Info) extends Expr {
  lazy val typ: Type = TupleT(args map (_.typ), Addressability.literal) // TODO: remove redundant typ information of other nodes
}

sealed trait CompositeLit extends Lit

/** An array literal of type '[`length`]`memberType`' consisting of `elems`. */
case class ArrayLit(length : BigInt, memberType : Type, elems : Map[BigInt, Expr])(val info : Source.Parser.Info) extends CompositeLit {
  override val typ : Type = ArrayT(length, memberType, Addressability.literal)
}

/** A slice literal of type '[]`memberType`' consisting of `elems`. */
case class NewSliceLit(target: LocalVar, memberType: Type, elems: Map[BigInt, Expr])(val info: Source.Parser.Info) extends Stmt {
  lazy val length: BigInt = if (elems.isEmpty) 0 else elems.maxBy(_._1)._1 + 1
  lazy val asArrayLit: ArrayLit = ArrayLit(length, memberType.withAddressability(Addressability.rValue), elems)(info)
  val typ: Type = SliceT(memberType, Addressability.literal)
}

case class StructLit(typ: Type, args: Vector[Expr])(val info: Source.Parser.Info) extends CompositeLit

case class NewMapLit(target: LocalVar, keys: Type, values: Type, entries: Seq[(Expr, Expr)])(val info: Source.Parser.Info) extends Stmt {
  val typ : Type = MapT(keys, values, Addressability.literal)
}

case class AdtConstructorLit(typ: Type, clause: AdtClauseProxy, args: Vector[Expr])(val info: Source.Parser.Info) extends CompositeLit

sealed trait Declaration extends Node

/** Everything that is defined with the scope of a code block. */
sealed trait BlockDeclaration extends Declaration

/** Any Gobra variable. */
sealed trait Var extends Expr with Location {
  def id: String

  def unapply(arg: Var): Some[(String, Type)] =
    Some((arg.id, arg.typ))
}

/**
  * Any variable that has a global scope.
  * */
sealed trait Global extends Var

/**
  * Any variable whose scope is the body of a method, function, or predicate.
  * Effectively, every variable that does not have a global scope.
  * */
sealed trait BodyVar extends Var

/** Any variable that is assignable in the intermediate representation. */
sealed trait AssignableVar extends Var


sealed trait Parameter extends BodyVar {
  def unapply(arg: Parameter): Some[(String, Type)] =
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

case class GlobalVar(name: GlobalVarProxy, typ: Type)(val info: Source.Parser.Info) extends AssignableVar with Global {
  override def id: String = name.name
}

sealed trait GlobalConst extends Global {
  def unapply(arg: GlobalConst): Some[(String, Type)] =
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

sealed trait Type extends Ordered[Type] {
  def addressability: Addressability

  /** Returns whether 'this' is equals to 't' without considering the addressability modifier of the types. */
  def equalsWithoutMod(t: Type): Boolean

  def withAddressability(newAddressability: Addressability): Type

  override def compare(that: Type): Int = Names.serializeType(this).compare(Names.serializeType(that))
}

sealed abstract class PrettyType(pretty: => String) extends Type {
  override lazy val toString: String = s"$pretty${addressability.pretty}"
}

case class BoolT(addressability: Addressability) extends PrettyType("bool") {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[BoolT]
  override def withAddressability(newAddressability: Addressability): BoolT = BoolT(newAddressability)
}

case class IntT(addressability: Addressability, kind: IntegerKind = UnboundedInteger) extends PrettyType(kind.name) {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[IntT] && t.asInstanceOf[IntT].kind == kind
  override def withAddressability(newAddressability: Addressability): IntT = IntT(newAddressability, kind)
}

case class Float32T(addressability: Addressability) extends PrettyType("float32") {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[Float32T]
  override def withAddressability(newAddressability: Addressability): Float32T = Float32T(newAddressability)
}

case class Float64T(addressability: Addressability) extends PrettyType("float64") {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[Float64T]
  override def withAddressability(newAddressability: Addressability): Float64T = Float64T(newAddressability)
}

case class StringT(addressability: Addressability) extends PrettyType("string") {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[StringT]
  override def withAddressability(newAddressability: Addressability): StringT = StringT(newAddressability)
}

case object VoidT extends PrettyType("void") {
  override val addressability: Addressability = Addressability.unit
  override def equalsWithoutMod(t: Type): Boolean = t == VoidT
  override def withAddressability(newAddressability: Addressability): VoidT.type = VoidT
}

case class FunctionT(args: Vector[Type], res: Vector[Type], addressability: Addressability) extends PrettyType(f"func${args.mkString("(", ", ", ")")}${res.mkString("(", ", ", ")")}") {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case FunctionT(otherArgs, otherRes, _) => otherArgs.length == args.length &&
      (otherArgs zip args).forall{ t => t._1.equalsWithoutMod(t._2)} &&
      (otherRes zip res).forall{ t => t._1.equalsWithoutMod(t._2)}
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): FunctionT = FunctionT(args, res, newAddressability)
}

case class PermissionT(addressability: Addressability) extends PrettyType("perm") {
  override def equalsWithoutMod(t: Type): Boolean = t.isInstanceOf[PermissionT]
  override def withAddressability(newAddressability: Addressability): PermissionT = PermissionT(newAddressability)
}

/** The type of types. For now, we have a single sort. */
case object SortT extends PrettyType("sort") {
  override val addressability: Addressability = Addressability.mathDataStructureElement
  override def equalsWithoutMod(t: Type): Boolean = t == SortT
  override def withAddressability(newAddressability: Addressability): SortT.type = SortT
}

/**
  * The type of `length`-sized arrays of elements of type `typ`.
  */
case class ArrayT(length: BigInt, elems: Type, addressability: Addressability) extends PrettyType(s"[$length]$elems") {
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
  * The (composite) type of slices of type `elems`.
  */
case class SliceT(elems : Type, addressability: Addressability) extends PrettyType(s"[]$elems") {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case SliceT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): SliceT =
    SliceT(elems.withAddressability(Addressability.sliceElement), newAddressability)
}

/**
  * The (composite) type of maps from type `keys` to type `values`.
  */
case class MapT(keys: Type, values: Type, addressability: Addressability) extends PrettyType(s"map[$keys]$values") {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case MapT(otherKeys, otherValues, _) => keys.equalsWithoutMod(otherKeys) && values.equalsWithoutMod(otherValues)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): MapT =
    MapT(keys.withAddressability(Addressability.mapKey), values.withAddressability(Addressability.mapValue), newAddressability)
}

/**
  * The type of mathematical sequences with elements of type `t`.
  */
case class SequenceT(t : Type, addressability: Addressability) extends PrettyType(s"seq[$t]")  {
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
case class SetT(t : Type, addressability: Addressability) extends PrettyType(s"set[$t]") {
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
case class MultisetT(t : Type, addressability: Addressability) extends PrettyType(s"mset[$t]") {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case MultisetT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): MultisetT =
    MultisetT(t.withAddressability(Addressability.mathDataStructureElement), newAddressability)
}

/**
  * The type of mathematical maps from `keys` to `values`
  */
case class MathMapT(keys: Type, values: Type, addressability: Addressability) extends PrettyType(s"dict[$keys]$values") {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case MathMapT(otherKeys, otherValues, _) => keys.equalsWithoutMod(otherKeys) && values.equalsWithoutMod(otherValues)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): MathMapT =
    MathMapT(keys.withAddressability(Addressability.mathDataStructureElement), values.withAddressability(Addressability.mathDataStructureElement), newAddressability)
}

/**
  * The (mathematical) type encapsulating an optional value of type `t`.
  */
case class OptionT(t : Type, addressability: Addressability) extends PrettyType(s"option[$t]") {
  override def equalsWithoutMod(t : Type): Boolean = t match {
    case OptionT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability) : OptionT =
    OptionT(t.withAddressability(Addressability.mathDataStructureElement), newAddressability)
}

case class DefinedT(name: String, addressability: Addressability) extends PrettyType(name) with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case DefinedT(otherName, _) => name == otherName
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): DefinedT =
    DefinedT(name, newAddressability)
}

case class PointerT(t: Type, addressability: Addressability) extends PrettyType(s"*$t") with TopType {
  require(t.addressability.isShared)

  override def equalsWithoutMod(t: Type): Boolean = t match {
    case PointerT(otherT, _) => t.equalsWithoutMod(otherT)
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): PointerT =
    PointerT(t.withAddressability(Addressability.pointerBase), newAddressability)
}

case class TupleT(ts: Vector[Type], addressability: Addressability) extends PrettyType(ts.mkString("(", ", ", ")")) with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case TupleT(otherTs, _) => ts.zip(otherTs).forall{ case (l,r) => l.equalsWithoutMod(r) }
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): TupleT =
    TupleT(ts.map(_.withAddressability(Addressability.mathDataStructureElement)), newAddressability)
}

case class PredT(args: Vector[Type], addressability: Addressability) extends PrettyType(args.mkString("pred(", ", ", ")")) with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case PredT(otherTs, _) => args.zip(otherTs).forall{ case (l,r) => l.equalsWithoutMod(r) }
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): PredT =
    PredT(args.map(_.withAddressability(Addressability.mathDataStructureElement)), newAddressability)
}


// StructT does not have a name because equality of two StructT does not depend at all on their declaration site but
// only on their structure, i.e. whether the fields (and addressability) are equal
case class StructT(fields: Vector[Field], ghost: Boolean, addressability: Addressability) extends PrettyType(fields.mkString(if (ghost) "ghost " else "" + "struct{", ", ", "}")) with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case StructT(otherFields, otherGhost, _) => ghost == otherGhost && fields.zip(otherFields).forall{ case (l, r) => l.name == r.name && l.ghost == r.ghost && l.typ.equalsWithoutMod(r.typ) }
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): StructT =
    StructT(fields.map(f => Field(f.name, f.typ.withAddressability(Addressability.field(newAddressability)), f.ghost)(f.info)), ghost = ghost, newAddressability)
}

case class InterfaceT(name: String, addressability: Addressability) extends PrettyType(s"interface{ name is $name }") with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case o: InterfaceT => name == o.name
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): InterfaceT =
    InterfaceT(name, newAddressability)

  def isEmpty: Boolean = name == Names.emptyInterface
}

case class DomainT(name: String, addressability: Addressability) extends PrettyType(s"domain{ name is $name }") with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case o: DomainT => name == o.name
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): DomainT =
    DomainT(name, newAddressability)
}

case class AdtT(name: String, definedName: String, addressability: Addressability) extends PrettyType(s"adt{ name is $name }") with TopType {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case o: AdtT => name == o.name
    case _ => false
  }

  val definedType: DefinedT = DefinedT(definedName, addressability)

  override def withAddressability(newAddressability: Addressability): Type =
    AdtT(name, definedName, newAddressability)
}

case class AdtClauseT(name: String, adtT: AdtT, fields: Vector[Field], addressability: Addressability) extends PrettyType(fields.mkString(s"$name{", ", ", "}")) {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case o: AdtClauseT => name == o.name && adtT == o.adtT
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): Type =
    AdtClauseT(name, adtT, fields, newAddressability)
}

case class ChannelT(elem: Type, addressability: Addressability) extends PrettyType(s"chan $elem") {
  override def equalsWithoutMod(t: Type): Boolean = t match {
    case o: ChannelT => elem == o.elem
    case _ => false
  }

  override def withAddressability(newAddressability: Addressability): ChannelT =
    ChannelT(elem, newAddressability)
}



sealed trait Proxy extends Node with Ordered[Proxy] {
  def name: String
  override def compare(that: Proxy): Int = name.compare(that.name)
}
sealed trait MemberProxy extends Proxy {
  def uniqueName: String

  override def compare(that: Proxy): Int = that match {
    case m: MemberProxy => uniqueName.compare(m.uniqueName)
    case _ => super.compare(that)
  }
}
sealed trait CallProxy extends Proxy

sealed trait FunctionMemberOrLitProxy extends Proxy {
  def name: String
}

case class FunctionProxy(override val name: String)(val info: Source.Parser.Info) extends FunctionMemberOrLitProxy with CallProxy
case class MethodProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends MemberProxy with CallProxy
case class DomainFuncProxy(name: String, domainName: String)(val info: Source.Parser.Info) extends Proxy

case class AdtClauseProxy(name: String, adtName: String)(val info: Source.Parser.Info) extends Proxy

case class FunctionLitProxy(override val name: String)(val info: Source.Parser.Info) extends FunctionMemberOrLitProxy

sealed trait PredicateProxy extends Proxy
case class FPredicateProxy(name: String)(val info: Source.Parser.Info) extends PredicateProxy
case class MPredicateProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends PredicateProxy with MemberProxy

case class LabelProxy(name: String)(val info: Source.Parser.Info) extends Proxy with BlockDeclaration

case class GlobalVarProxy(name: String, uniqueName: String)(val info: Source.Parser.Info) extends Proxy

