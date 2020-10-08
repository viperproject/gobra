// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.utility

/** Code was taken from @see [[viper.silver.ast.utility.ViperStrategy]] */

import viper.gobra.ast.internal._

/**
  * Factory for standard rewriting configurations
  */
object GobraStrategy {

  def gobraDuplicatorStrategy: PartialFunction[(Node, Seq[AnyRef], Node.Meta), Node] = {
    case (n, args, info) => gobraDuplicator(n, args, info)
  }

  def gobraDuplicator[N <: Node](x: N, args: Seq[AnyRef], meta: Node.Meta): N = {
    val node: Node = (x, args) match {
        // Members
      case (p: Program, Seq(t: Vector[TopType@unchecked], m: Vector[Member@unchecked])) => Program(t, m, p.table)(meta)
      case (m: Method, Seq(rec: Parameter.In, name: MethodProxy, arg: Vector[Parameter.In@unchecked], res: Vector[Parameter.Out@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Block@unchecked])) => Method(rec, name, arg, res, pre, post, b)(meta)
      case (m: PureMethod, Seq(rec: Parameter.In, name: MethodProxy, arg: Vector[Parameter.In@unchecked], res: Vector[Parameter.Out@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Expr@unchecked])) => PureMethod(rec, name, arg, res, pre, post, b)(meta)
      case (f: Function, Seq(name: FunctionProxy, arg: Vector[Parameter.In@unchecked], res: Vector[Parameter.Out@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Block@unchecked])) => Function(name, arg, res, pre, post, b)(meta)
      case (f: PureFunction, Seq(name: FunctionProxy, arg: Vector[Parameter.In@unchecked], res: Vector[Parameter.Out@unchecked], pre: Vector[Assertion@unchecked], post: Vector[Assertion@unchecked], b: Option[Expr@unchecked])) => PureFunction(name, arg, res, pre, post, b)(meta)
      case (p: MPredicate, Seq(recv: Parameter.In, name: MPredicateProxy, args: Vector[Parameter.In@unchecked], b: Option[Assertion@unchecked])) => MPredicate(recv, name, args, b)(meta)
      case (p: FPredicate, Seq(name: FPredicateProxy, args: Vector[Parameter.In@unchecked], b: Option[Assertion@unchecked])) => FPredicate(name, args, b)(meta)
      case (f: Field, Seq()) => Field(f.name, f.typ, f.ghost)(meta)
        // Statements
      case (b: Block, Seq(v: Vector[BlockDeclaration@unchecked], s: Vector[Stmt@unchecked])) => Block(v, s)(meta)
      case (s: Seqn, Seq(stmts: Vector[Stmt@unchecked])) => Seqn(stmts)(meta)
      case (i: If, Seq(cond: Expr, thn: Stmt, els: Stmt)) => If(cond, thn, els)(meta)
      case (w: While, Seq(cond: Expr, invs: Vector[Assertion@unchecked], body: Stmt)) => While(cond, invs, body)(meta)
      case (n: Make, Seq(target: LocalVar, co: CompositeObject)) => Make(target, co)(meta)
      case (s: SingleAss, Seq(l: Assignee, r: Expr)) => SingleAss(l, r)(meta)
      case (a: Assignee.Var, Seq(v: AssignableVar)) => Assignee.Var(v)
      case (a: Assignee.Pointer, Seq(e: Deref)) => Assignee.Pointer(e)
      case (a: Assignee.Field, Seq(e: FieldRef)) => Assignee.Field(e)
      case (a: Assignee.Index, Seq(e: IndexedExp)) => Assignee.Index(e)
      case (f: FunctionCall, Seq(targets: Vector[LocalVar@unchecked], func: FunctionProxy, args: Vector[Expr@unchecked])) => FunctionCall(targets, func, args)(meta)
      case (m: MethodCall, Seq(targets: Vector[LocalVar@unchecked], recv: Expr, meth: MethodProxy, args: Vector[Expr@unchecked])) => MethodCall(targets, recv, meth, args)(meta)
      case (r: Return, Seq()) => Return()(meta)
      case (a: Assert, Seq(ass: Assertion)) => Assert(ass)(meta)
      case (a: Assume, Seq(ass: Assertion)) => Assume(ass)(meta)
      case (i: Inhale, Seq(ass: Assertion)) => Inhale(ass)(meta)
      case (e: Exhale, Seq(ass: Assertion)) => Exhale(ass)(meta)
      case (f: Fold, Seq(acc: Access)) => Fold(acc)(meta)
      case (u: Unfold, Seq(acc: Access)) => Unfold(acc)(meta)
        // Assertions
      case (s: SepAnd, Seq(l: Assertion, r: Assertion)) => SepAnd(l, r)(meta)
      case (e: ExprAssertion, Seq(exp: Expr)) => ExprAssertion(exp)(meta)
      case (i: Implication, Seq(l: Expr, r: Assertion)) => Implication(l, r)(meta)
      case (a: Access, Seq(acc: Accessible)) => Access(acc)(meta)
      case (a: Accessible.Address, Seq(d: Deref)) => Accessible.Address(d)
      case (a: Accessible.Predicate, Seq(p: PredicateAccess)) => Accessible.Predicate(p)
      case (p: FPredicateAccess, Seq(pred: FPredicateProxy, args: Vector[Expr@unchecked])) => FPredicateAccess(pred, args)(meta)
      case (p: MPredicateAccess, Seq(recv: Expr, pred: MPredicateProxy, args: Vector[Expr@unchecked])) => MPredicateAccess(recv, pred, args)(meta)
      case (p: MemoryPredicateAccess, Seq(arg: Expr)) => MemoryPredicateAccess(arg)(meta)
        // Expressions
      case (u: Unfolding, Seq(acc: Access, e: Expr)) => Unfolding(acc, e)(meta)
      case (f: PureFunctionCall, Seq(func: FunctionProxy, args: Vector[Expr@unchecked])) => PureFunctionCall(func, args, f.typ)(meta)
      case (m: PureMethodCall, Seq(recv: Expr, meth: MethodProxy, args: Vector[Expr@unchecked])) => PureMethodCall(recv, meth, args, m.typ)(meta)
      case (d: DfltVal, Seq()) => DfltVal(d.typ)(meta)
      case (t: Tuple, Seq(args: Vector[Expr@unchecked])) => Tuple(args)(meta)
      case (d: Deref, Seq(e: Expr)) => Deref(e, d.typ)(meta)
      case (r: Ref, Seq(ref: Addressable, t: PointerT)) => Ref(ref, t)(meta)
      case (f: FieldRef, Seq(recv: Expr, field: Field)) => FieldRef(recv, field)(meta)
      case (i: IndexedExp, Seq(base: Expr, idx: Expr)) => IndexedExp(base, idx)(meta)
      case (u: ArrayUpdate, Seq(base: Expr, left: Expr, right: Expr)) => ArrayUpdate(base, left, right)(meta)
      case (u: StructUpdate, Seq(base: Expr, left: Field, right: Expr)) => StructUpdate(base, left, right)(meta)
      case (n: Negation, Seq(op: Expr)) => Negation(op)(meta)
      case (e: EqCmp, Seq(l: Expr, r: Expr)) => EqCmp(l, r)(meta)
      case (e: UneqCmp, Seq(l: Expr, r: Expr)) => UneqCmp(l, r)(meta)
      case (e: LessCmp, Seq(l: Expr, r: Expr)) => LessCmp(l, r)(meta)
      case (e: AtMostCmp, Seq(l: Expr, r: Expr)) => AtMostCmp(l, r)(meta)
      case (e: GreaterCmp, Seq(l: Expr, r: Expr)) => GreaterCmp(l, r)(meta)
      case (e: AtLeastCmp, Seq(l: Expr, r: Expr)) => AtLeastCmp(l, r)(meta)
      case (e: And, Seq(l: Expr, r: Expr)) => And(l, r)(meta)
      case (e: Or, Seq(l: Expr, r: Expr)) => Or(l, r)(meta)
      case (e: And, Seq(l: Expr, r: Expr)) => And(l, r)(meta)
      case (e: Sub, Seq(l: Expr, r: Expr)) => Sub(l, r)(meta)
      case (e: Mul, Seq(l: Expr, r: Expr)) => Mul(l, r)(meta)
      case (e: Mod, Seq(l: Expr, r: Expr)) => Mod(l, r)(meta)
      case (e: Div, Seq(l: Expr, r: Expr)) => Div(l, r)(meta)
      case (n: Multiplicity, Seq(left: Expr, right: Expr)) => Multiplicity(left, right)(meta)
      case (n: Length, Seq(exp: Expr)) => Length(exp)(meta)
      case (n: RangeSequence, Seq(low: Expr, high: Expr)) => RangeSequence(low, high)(meta)
      case (n: SequenceAppend, Seq(l: Expr, r: Expr)) => SequenceAppend(l, r)(meta)
      case (n: SequenceUpdate, Seq(base: Expr, left: Expr, right: Expr)) => SequenceUpdate(base, left, right)(meta)
      case (n: SequenceDrop, Seq(l: Expr, r: Expr)) => SequenceDrop(l, r)(meta)
      case (n: SequenceTake, Seq(l: Expr, r: Expr)) => SequenceTake(l, r)(meta)
      case (n: SequenceConversion, Seq(arg: Expr)) => SequenceConversion(arg)(meta)
      case (n: Union, Seq(l: Expr, r: Expr)) => Union(l, r)(meta)
      case (n: Intersection, Seq(l: Expr, r: Expr)) => Intersection(l, r)(meta)
      case (n: SetMinus, Seq(l: Expr, r: Expr)) => SetMinus(l, r)(meta)
      case (n: Subset, Seq(l: Expr, r: Expr)) => Subset(l ,r)(meta)
      case (n: Cardinality, Seq(arg: Expr)) => Cardinality(arg)(meta)
      case (n: Contains, Seq(l: Expr, r: Expr)) => Contains(l, r)(meta)
      case (n: SetConversion, Seq(arg: Expr)) => SetConversion(arg)(meta)
      case (n: MultisetConversion, Seq(arg: Expr)) => MultisetConversion(arg)(meta)
      case (e: Old, Seq(op: Expr)) => Old(op, e.typ)(meta)
      case (c: Conditional, Seq(cond: Expr, thn: Expr, els: Expr)) => Conditional(cond, thn, els, c.typ)(meta)
      case (i: IntLit, Seq()) => IntLit(i.v)(meta)
      case (b: BoolLit, Seq()) => BoolLit(b.b)(meta)
      case (n: NilLit, Seq()) => NilLit(n.typ)(meta)
      case (s: StructLit, Seq(args: Vector[Expr@unchecked])) => StructLit(s.typ, args)(meta)
      case (n: ArrayLit, Seq(t: Type, args: Vector[Expr@unchecked])) => ArrayLit(t, args)(meta)
      case (n: SequenceLit, Seq(t: Type, args: Vector[Expr@unchecked])) => SequenceLit(t, args)(meta)
      case (n: SetLit, Seq(t: Type, args: Vector[Expr@unchecked])) => SetLit(t, args)(meta)
      case (n: MultisetLit, Seq(t: Type, args: Vector[Expr@unchecked])) => MultisetLit(t, args)(meta)
      case (p: Parameter.In, Seq()) => Parameter.In(p.id, p.typ)(meta)
      case (p: Parameter.Out, Seq()) => Parameter.Out(p.id, p.typ)(meta)
      case (l: LocalVar, Seq()) => LocalVar(l.id, l.typ)(meta)
      case (a: Addressable.Var, Seq(v: LocalVar)) => Addressable.Var(v)
      case (a: Addressable.Pointer, Seq(v: Deref)) => Addressable.Pointer(v)
      case (a: Addressable.Field, Seq(v: FieldRef)) => Addressable.Field(v)
      case (a: Addressable.Index, Seq(v: IndexedExp)) => Addressable.Index(v)
        // Proxy
      case (f: FunctionProxy, Seq()) => FunctionProxy(f.name)(meta)
      case (m: MethodProxy, Seq()) => MethodProxy(m.name, m.uniqueName)(meta)
      case (f: FPredicateProxy, Seq()) => FPredicateProxy(f.name)(meta)
      case (m: MPredicateProxy, Seq()) => MPredicateProxy(m.name, m.uniqueName)(meta)
    }

    node.asInstanceOf[N]
  }
}
