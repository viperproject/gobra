// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.ast.{internal => in}

import scala.annotation.unused

object Core {
  def unit[R](x: R): Core[R] = Core { args => (x, args) }

  def vector[R](x: Vector[Core[R]]): Core[Vector[R]] =
    x.foldRight(unit(Vector.empty[R])){ case (l, r)  => for (x <- l; xs <- r) yield x +: xs }

  def option[R](x: Option[Core[R]]): Core[Option[R]] = x.fold[Core[Option[R]]](unit(None))(_.map(Some(_)))

  /**
    * For a statements s, the result is a function from variables to s where all sub-expressions are substituted with the argument variables.
    * The result satisfies `core(s).run(coreArgs(s)) == s`
    * */
  def core(x: in.Deferrable): Core[in.Deferrable] = x match {
    case x: in.FunctionCall => for (args <- vector(x.args map core)) yield x.copy(args = args)(x.info)
    case x: in.MethodCall => for (recv <- core(x.recv); args <- vector(x.args map core)) yield x.copy(recv = recv, args = args)(x.info)
    case x: in.ClosureCall => for (closure <- core(x.closure); args <- vector(x.args map core); spec <- deepCore(x.spec)) yield x.copy(closure = closure, args = args, spec = spec)(x.info)
    case x: in.Fold => for (acc <- core(x.acc)) yield x.copy(acc = acc)(x.info)
    case x: in.Unfold => for (acc <- core(x.acc)) yield x.copy(acc = acc)(x.info)
    case x: in.PredExprFold => for (base <- deepCore(x.base); args <- vector(x.args map core)) yield x.copy(base = base, args = args)(x.info)
    case x: in.PredExprUnfold => for (base <- deepCore(x.base); args <- vector(x.args map core)) yield x.copy(base = base, args = args)(x.info)
  }

  def core(x: in.Access): Core[in.Access] =
    for (e <- core(x.e); p <- core(x.p)) yield x.copy(e = e, p = p)(x.info)

  def core(x: in.Accessible): Core[in.Accessible] = x match {
    case x: in.Accessible.Predicate => for (op <- core(x.op)) yield x.copy(op = op)
    case x: in.Accessible.ExprAccess => for (op <- core(x.op)) yield x.copy(op = op)
    case x: in.Accessible.Address => for (op <- core(x.op)) yield x.copy(op = op)
    case x: in.Accessible.PredExpr => for (op <- core(x.op)) yield x.copy(op = op)
  }

  def core(x: in.PredicateAccess): Core[in.PredicateAccess] = x match {
    case x: in.FPredicateAccess =>
      for (args <- vector(x.args map core)) yield x.copy(args = args)(x.info)
    case x: in.MPredicateAccess =>
      for (recv <- core(x.recv); args <- vector(x.args map core)) yield x.copy(args = args, recv = recv)(x.info)
    case x: in.MemoryPredicateAccess =>
      for (arg <- core(x.arg)) yield x.copy(arg = arg)(x.info)
  }

  def core(x: in.PredExprInstance): Core[in.PredExprInstance] = {
    for (base <- core(x.base); args <- vector(x.args map core)) yield x.copy(base = base, args = args)(x.info)
  }

  def core(@unused x: in.Expr): Core[in.LocalVar] = Core { args => (args.head, args.tail) }

  def deepCore(x: in.PredicateConstructor): Core[in.PredicateConstructor] = {
    for {
      args <- vector(x.args.map(y => option(y.map(core))))
    } yield x.copy(args = args)(x.info)
  }

  def deepCore(x: in.ClosureSpec): Core[in.ClosureSpec] = {
    for {
      paramValues <- vector(x.paramValues.map(y => option(y.map(core))))
      params = paramValues.zipWithIndex.collect{ case (Some(exp), idx) => idx+1 -> exp }.toMap
    } yield x.copy(params = params)(x.info)
  }


  /** Returns all expressions of a statement such that `core(s).run(coreArgs(s)) == s` */
  def coreArgs(x: in.Deferrable): Vector[in.Expr] = x match {
    case x: in.FunctionCall => x.args
    case x: in.MethodCall => x.recv +: x.args
    case x: in.ClosureCall => (x.closure +: x.args) ++ deepCoreArgs(x.spec)
    case x: in.Fold => coreArgs(x.acc)
    case x: in.Unfold => coreArgs(x.acc)
    case x: in.PredExprFold => deepCoreArgs(x.base) ++ x.args.flatMap(coreArgs)
    case x: in.PredExprUnfold => deepCoreArgs(x.base) ++ x.args.flatMap(coreArgs)
  }

  def coreArgs(x: in.Access): Vector[in.Expr] = coreArgs(x.e) :+ x.p

  def coreArgs(x: in.Accessible): Vector[in.Expr] = x match {
    case x: in.Accessible.Predicate => coreArgs(x.op)
    case x: in.Accessible.ExprAccess => coreArgs(x.op)
    case x: in.Accessible.Address => coreArgs(x.op)
    case x: in.Accessible.PredExpr => coreArgs(x.op)
  }

  def coreArgs(x: in.PredicateAccess): Vector[in.Expr] = x match {
    case x: in.FPredicateAccess => x.args
    case x: in.MPredicateAccess => x.recv +: x.args
    case x: in.MemoryPredicateAccess => Vector(x.arg)
  }

  def coreArgs(x: in.PredExprInstance): Vector[in.Expr] = x.base +: x.args

  def coreArgs(x: in.Expr): Vector[in.Expr] = Vector(x)

  def deepCoreArgs(x: in.PredicateConstructor): Vector[in.Expr] = x.args.flatten

  def deepCoreArgs(x: in.ClosureSpec): Vector[in.Expr] = x.paramValues.flatten
}

case class Core[+R](gen: Vector[in.LocalVar] => (R, Vector[in.LocalVar])) {

  def run(args: Vector[in.LocalVar]): R = gen(args)._1

  def flatMap[Q](f: R => Core[Q]): Core[Q] = Core{ v1 =>
    val (r1, v2) = gen(v1); f(r1).gen(v2)
  }

  def map[Q](f: R => Q): Core[Q] =  Core{ v1 =>
    val (r1, v2) = gen(v1); (f(r1), v2)
  }
}