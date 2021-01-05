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
      case Field(_, _, _) => Seq()
      case s: Stmt => s match {
        case Block(decls, stmts) => decls ++ stmts
        case Seqn(stmts) => stmts
        case If(cond, thn, els) => Seq(cond, thn, els)
        case While(cond, invs, body) => Seq(cond) ++ invs ++ Seq(body)
        case Make(target, typ) => Seq(target, typ)
        case SingleAss(left, right) => Seq(left, right)
        case FunctionCall(targets, func, args) => targets ++ Seq(func) ++ args
        case MethodCall(targets, recv, meth, args) => targets ++ Seq(recv, meth) ++ args
        case Return() => Seq()
        case Assert(ass) => Seq(ass)
        case Exhale(ass) => Seq(ass)
        case Inhale(ass) => Seq(ass)
        case Assume(ass) => Seq(ass)
        case Fold(acc) => Seq(acc)
        case Unfold(acc) => Seq(acc)
        case SafeTypeAssertion(resTarget, successTarget, expr, _) => Seq(resTarget, successTarget, expr)
      }
      case a: Assignee => Seq(a.op)
      case a: Assertion => a match {
        case SepAnd(left, right) => Seq(left, right)
        case SepForall(vars, triggers, body) => vars ++ triggers ++ Seq(body)
        case ExprAssertion(exp) => Seq(exp)
        case Implication(left, right) => Seq(left, right)
        case Access(e) => Seq(e)
      }
      case a: Accessible => Seq(a.op)
      case p: PredicateAccess => p match {
        case FPredicateAccess(pred, args) => Seq(pred) ++ args
        case MPredicateAccess(recv, pred, args) => Seq(recv, pred) ++ args
        case MemoryPredicateAccess(arg) => Seq(arg)
        case PredExprInstance(base, args) => Seq(base) ++ args
      }
      case e: Expr => e match {
        case Unfolding(acc, op) => Seq(acc, op)
        case PureFunctionCall(func, args, _) => Seq(func) ++ args
        case PureMethodCall(recv, meth, args, _) => Seq(recv, meth) ++ args
        case Conversion(_, expr) => Seq(expr)
        case DfltVal(_) => Seq()
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
        case BoolTExpr() => Seq()
        case IntTExpr(_) => Seq()
        case PermTExpr() => Seq()
        case PointerTExpr(elem) => Seq(elem)
        case StructTExpr(_) => Seq()
        case ArrayTExpr(len, elem) => Seq(len, elem)
        case SliceTExpr(elem) => Seq(elem)
        case SequenceTExpr(elem) => Seq(elem)
        case SetTExpr(elem) => Seq(elem)
        case MultisetTExpr(elem) => Seq(elem)
        case OptionTExpr(elem) => Seq(elem)
        case TupleTExpr(elem) => elem
        case DefinedTExpr(_) => Seq()
        case PredicateConstructor(pred, _, args) => Seq(pred) ++ args.flatten
        case IndexedExp(base, idx) => Seq(base, idx)
        case ArrayUpdate(base, left, right) => Seq(base, left, right)
        case Slice(base, low, high, max) => Seq(base, low, high) ++ max
        case RangeSequence(low, high) => Seq(low, high)
        case SequenceUpdate(base, left, right) => Seq(base, left, right)
        case SequenceDrop(left, right) => Seq(left, right)
        case SequenceTake(left, right) => Seq(left, right)
        case SequenceConversion(expr) => Seq(expr)
        case Cardinality(exp) => Seq(exp)
        case SetConversion(expr) => Seq(expr)
        case MultisetConversion(expr) => Seq(expr)
        case Length(expr) => Seq(expr)
        case Capacity(expr) => Seq(expr)
        case OptionNone(_) => Seq()
        case OptionSome(exp) => Seq(exp)
        case OptionGet(exp) => Seq(exp)
        case Negation(operand) => Seq(operand)
        case BinaryExpr(left, _, right, _) => Seq(left, right)
        case EqCmp(l, r) => Seq(l, r)
        case Old(op, _) => Seq(op)
        case Conditional(cond, thn, els, _) => Seq(cond, thn, els)
        case l: Lit => l match {
          case IntLit(_) => Seq()
          case BoolLit(_) => Seq()
          case NilLit(_) => Seq()
          case ArrayLit(_, _, elems) => elems.values.toSeq
          case SliceLit(_, elems) => elems.values.toSeq
          case StructLit(_, args) => args
          case SequenceLit(_, _, args) => args.values.toSeq
          case SetLit(_, args) => args
          case MultisetLit(_, args) => args
        }
        case Parameter.In(_, _) => Seq()
        case Parameter.Out(_, _) => Seq()
        case LocalVar(_, _) => Seq()
      }
      case a: Addressable => Seq(a.op)
      case p: Proxy => p match {
        case FunctionProxy(_) => Seq()
        case MethodProxy(_, _) => Seq()
        case FPredicateProxy(_) => Seq()
        case MPredicateProxy(_, _) => Seq()
      }
    }
//    n match {
//      case t: Typed => subnodesWithType ++ Seq(t.typ)
//      case _ => subnodesWithType
//    }
    subnodesWithoutType
  }

}
