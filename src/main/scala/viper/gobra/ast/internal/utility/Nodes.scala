// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.utility

import viper.gobra.ast.internal.Node
import viper.gobra.ast.internal._

object Nodes {

  /** Returns a list of all direct sub-nodes of this node. The type of nodes is
    * included in this list only for declarations (but not for expressions, for
    * instance).
    *
    * Furthermore, pointers to declarations are not included either (e.g., the
    * `field` (which is of type `Node`) of a `FieldAccess`), and neither are
    * non-`Node` children (e.g., the `predicateName` (a `String`) of a
    * `PredicateAccess`).
    *
    * As a consequence, it is not sufficient to compare the subnodes of two
    * nodes for equality if one has to compare those two nodes for equality.
    */
  def subnodes(n: Node): Seq[Node] = { // TODO: maybe can be solved generally
    val subnodesWithoutType: Seq[Node] = n match {
      case Program(_, members, _) => members
      case Method(receiver, name, args, results, pres, posts, body) => Seq(receiver, name) ++ args ++ results ++ pres ++ posts ++ body
      case PureMethod(receiver, name, args, results, pres, posts, body) => Seq(receiver, name) ++ args ++ results ++ pres ++ posts ++ body
      case Function(name, args, results, pres, posts, body) => Seq(name) ++ args ++ results ++ pres ++ posts ++ body
      case PureFunction(name, args, results, pres, posts, body) => Seq(name) ++ args ++ results ++ pres ++ posts ++ body
      case FPredicate(name, args, body) => Seq(name) ++ args ++ body
      case MPredicate(recv, name, args, body) => Seq(recv, name) ++ args ++ body
      case MethodSubtypeProof(subProxy, _, superProxy, rec, args, res, b) => Seq(subProxy, superProxy, rec) ++ args ++ res ++ b
      case PureMethodSubtypeProof(subProxy, _, superProxy, rec, args, res, b) => Seq(subProxy, superProxy, rec) ++ args ++ res ++ b
      case Field(_, _, _) => Seq.empty
      case DomainDefinition(_, funcs, axioms) => funcs ++ axioms
      case DomainFunc(_, args, results) => args ++ Seq(results)
      case DomainAxiom(expr) => Seq(expr)
      case s: Stmt => s match {
        case Block(decls, stmts) => decls ++ stmts
        case Seqn(stmts) => stmts
        case Label(label) => Seq(label)
        case If(cond, thn, els) => Seq(cond, thn, els)
        case While(cond, invs, body) => Seq(cond) ++ invs ++ Seq(body)
        case New(target, typ) => Seq(target, typ)
        case MakeSlice(target, _, lenArg, capArg) => Seq(target, lenArg) ++ capArg.toSeq
        case MakeChannel(target, _, bufferSizeArg, _, _) => target +: bufferSizeArg.toSeq
        case MakeMap(target, _, initialSpaceArg) => target +: initialSpaceArg.toSeq
        case SafeMapLookup(resTarget, successTarget, mapLookup) => Vector(resTarget, successTarget, mapLookup)
        case Initialization(left) => Seq(left)
        case SingleAss(left, right) => Seq(left, right)
        case FunctionCall(targets, func, args) => targets ++ Seq(func) ++ args
        case MethodCall(targets, recv, meth, args) => targets ++ Seq(recv, meth) ++ args
        case Return() => Seq.empty
        case Assert(ass) => Seq(ass)
        case Exhale(ass) => Seq(ass)
        case Inhale(ass) => Seq(ass)
        case Assume(ass) => Seq(ass)
        case Fold(acc) => Seq(acc)
        case Unfold(acc) => Seq(acc)
        case PredExprFold(base, args, p) => Seq(base) ++ args ++ Seq(p)
        case PredExprUnfold(base, args, p) => Seq(base) ++ args ++ Seq(p)
        case SafeTypeAssertion(resTarget, successTarget, expr, _) => Seq(resTarget, successTarget, expr)
        case GoFunctionCall(func, args) => Seq(func) ++ args
        case GoMethodCall(recv, meth, args) => Seq(recv, meth) ++ args
        case SafeReceive(resTarget, successTarget, channel, recvChannel, recvGivenPerm, recvGotPerm, closed) =>
          Seq(resTarget, successTarget, channel, recvChannel, recvGivenPerm, recvGotPerm, closed)
        case Send(channel, expr, sendChannel, sendGivenPerm, sendGotPerm) =>
          Seq(channel, expr, sendChannel, sendGivenPerm, sendGotPerm)
      }
      case a: Assignee => Seq(a.op)
      case a: Assertion => a match {
        case SepAnd(left, right) => Seq(left, right)
        case SepForall(vars, triggers, body) => vars ++ triggers ++ Seq(body)
        case ExprAssertion(exp) => Seq(exp)
        case Implication(left, right) => Seq(left, right)
        case Access(e, p) => Seq(e, p)
      }
      case a: Accessible => Seq(a.op)
      case p: PredicateAccess => p match {
        case FPredicateAccess(pred, args) => Seq(pred) ++ args
        case MPredicateAccess(recv, pred, args) => Seq(recv, pred) ++ args
        case MemoryPredicateAccess(arg) => Seq(arg)
      }
      case PredExprInstance(base, args) => Seq(base) ++ args
      case e: Expr => e match {
        case Unfolding(acc, op) => Seq(acc, op)
        case PureFunctionCall(func, args, _) => Seq(func) ++ args
        case PureMethodCall(recv, meth, args, _) => Seq(recv, meth) ++ args
        case DomainFunctionCall(func, args, _) => Seq(func) ++ args
        case Conversion(_, expr) => Seq(expr)
        case DfltVal(_) => Seq.empty
        case Tuple(args) => args
        case Deref(exp, _) => Seq(exp)
        case Ref(ref, _) => Seq(ref)
        case FieldRef(recv, field) => Seq(recv, field)
        case StructUpdate(base, field, newVal) => Seq(base, field, newVal)
        case TypeAssertion(exp, _) => Seq(exp)
        case TypeOf(exp) => Seq(exp)
        case IsComparableType(exp) => Seq(exp)
        case IsComparableInterface(exp) => Seq(exp)
        case ToInterface(exp, _) => Seq(exp)
        case IsBehaviouralSubtype(left, right) => Seq(left, right)
        case BoolTExpr() => Seq.empty
        case StringTExpr() => Seq.empty
        case IntTExpr(_) => Seq.empty
        case PermTExpr() => Seq.empty
        case PointerTExpr(elem) => Seq(elem)
        case StructTExpr(_) => Seq.empty
        case ArrayTExpr(len, elem) => Seq(len, elem)
        case SliceTExpr(elem) => Seq(elem)
        case MapTExpr(key, elem) => Seq(key, elem)
        case SequenceTExpr(elem) => Seq(elem)
        case SetTExpr(elem) => Seq(elem)
        case MultisetTExpr(elem) => Seq(elem)
        case MathMapTExpr(key, elem) => Seq(key, elem)
        case OptionTExpr(elem) => Seq(elem)
        case TupleTExpr(elem) => elem
        case DefinedTExpr(_) => Seq.empty
        case PredicateConstructor(pred, _, args) => Seq(pred) ++ args.flatten
        case IndexedExp(base, idx) => Seq(base, idx)
        case ArrayUpdate(base, left, right) => Seq(base, left, right)
        case Slice(base, low, high, max) => Seq(base, low, high) ++ max
        case RangeSequence(low, high) => Seq(low, high)
        case GhostCollectionUpdate(base, left, right) => Seq(base, left, right)
        case SequenceDrop(left, right) => Seq(left, right)
        case SequenceTake(left, right) => Seq(left, right)
        case SequenceConversion(expr) => Seq(expr)
        case Cardinality(exp) => Seq(exp)
        case SetConversion(expr) => Seq(expr)
        case MultisetConversion(expr) => Seq(expr)
        case MapKeys(expr) => Seq(expr)
        case MapValues(expr) => Seq(expr)
        case Length(expr) => Seq(expr)
        case Capacity(expr) => Seq(expr)
        case OptionNone(_) => Seq.empty
        case OptionSome(exp) => Seq(exp)
        case OptionGet(exp) => Seq(exp)
        case Negation(operand) => Seq(operand)
        case Receive(channel, recvChannel, recvGivenPerm, recvGotPerm) => Seq(channel, recvChannel, recvGivenPerm, recvGotPerm)
        case BinaryExpr(left, _, right, _) => Seq(left, right)
        case Old(op, _) => Seq(op)
        case LabeledOld(label, operand) => Seq(label, operand)
        case Conditional(cond, thn, els, _) => Seq(cond, thn, els)
        case PureForall(vars, triggers, body) => vars ++ triggers ++ Seq(body)
        case Exists(vars, triggers, body) => vars ++ triggers ++ Seq(body)
        case p: Permission => p match {
          case _: FullPerm => Seq.empty
          case _: NoPerm => Seq.empty
          case FractionalPerm(left, right) => Seq(left, right)
          case _: WildcardPerm => Seq.empty
          case c: CurrentPerm => Seq(c.acc)
          case PermMinus(exp) => Seq(exp)
          case BinaryExpr(left, _, right, _) => Seq(left, right)
        }
        case l: Lit => l match {
          case IntLit(_, _) => Seq.empty
          case BoolLit(_) => Seq.empty
          case StringLit(_) => Seq.empty
          case NilLit(_) => Seq.empty
          case ArrayLit(_, _, elems) => elems.values.toSeq
          case SliceLit(_, elems) => elems.values.toSeq
          case MapLit(_, _, entries) => entries flatMap { case (x, y) => Seq(x, y) }
          case StructLit(_, args) => args
          case SequenceLit(_, _, args) => args.values.toSeq
          case SetLit(_, args) => args
          case MultisetLit(_, args) => args
          case MathMapLit(_, _, entries) => entries flatMap { case (x, y) => Seq(x, y) }
        }
        case Parameter.In(_, _) => Seq.empty
        case Parameter.Out(_, _) => Seq.empty
        case LocalVar(_, _) => Seq.empty
        case GlobalConst.Val(_, _) => Seq.empty
        case _: BoundVar => Seq.empty
      }
      case Trigger(exprs) => exprs
      case a: Addressable => Seq(a.op)
      case p: Proxy => p match {
        case FunctionProxy(_) => Seq.empty
        case MethodProxy(_, _) => Seq.empty
        case FPredicateProxy(_) => Seq.empty
        case MPredicateProxy(_, _) => Seq.empty
        case _: DomainFuncProxy => Seq.empty
        case _: LabelProxy => Seq.empty
      }
    }
//    n match {
//      case t: Typed => subnodesWithType ++ Seq(t.typ)
//      case _ => subnodesWithType
//    }
    subnodesWithoutType
  }

}
