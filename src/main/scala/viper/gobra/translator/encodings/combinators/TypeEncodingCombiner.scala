// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.ast.Exp
import viper.silver.{ast => vpr}

/**
  * Combines a vector of type encodings.
  */
abstract class TypeEncodingCombiner(encodings: Vector[TypeEncoding], defaults: Vector[TypeEncoding]) extends TypeEncoding {

  /**
    * Combines partial functions selected by 'get' into a single partial function
    * @param get function selecting a partial function of the type encoding.
    * @return combined partial function.
    */
  protected[combinators] def combiner[X, Y](get: TypeEncoding => (X ==> Y)): X ==> Y

  protected[combinators] def extender[X, Y](get: TypeEncoding => (X ==> (Y => Y))): X ==> (Y => Y) = {
    case x =>
      val functions = encodings.flatMap(get(_).unapply(x))
      (y: Y) => functions.foldLeft(y){ case (r,f) => f(r) }
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    encodings.foreach(_.finalize(addMemberFn))
    defaults.foreach(_.finalize(addMemberFn))
  }

  override def typ(ctx: Context): in.Type ==> vpr.Type = combiner(_.typ(ctx))
  override def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = combiner(_.variable(ctx))
  override def method(ctx: Context): in.Member ==> MemberWriter[vpr.Method] = combiner(_.method(ctx))
  override def function(ctx: Context): in.Member ==> MemberWriter[vpr.Function] = combiner(_.function(ctx))
  override def predicate(ctx: Context): in.Member ==> MemberWriter[vpr.Predicate] = combiner(_.predicate(ctx))
  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = combiner(_.member(ctx))
  override def varPrecondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = combiner(_.varPrecondition(ctx))
  override def varPostcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = combiner(_.varPostcondition(ctx))
  override def initialization(ctx: Context): in.Location ==> CodeWriter[vpr.Stmt] = combiner(_.initialization(ctx))
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = combiner(_.assignment(ctx))
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = combiner(_.equal(ctx))
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = combiner(_.goEqual(ctx))
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = combiner(_.expression(ctx))
  override def triggerExpr(ctx: Context): in.TriggerExpr ==> CodeWriter[vpr.Exp] = combiner(_.triggerExpr(ctx))
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = combiner(_.assertion(ctx))
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = combiner(_.reference(ctx))
  override def addressFootprint(ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = combiner(_.addressFootprint(ctx))
  override def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[Exp]] = combiner(_.isComparable(ctx))
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = combiner(_.statement(ctx))

  override def builtInMethod(ctx: Context): in.BuiltInMethod ==> in.MethodMember = combiner(_.builtInMethod(ctx))
  override def builtInFunction(ctx: Context): in.BuiltInFunction ==> in.FunctionMember = combiner(_.builtInFunction(ctx))
  override def builtInFPredicate(ctx: Context): in.BuiltInFPredicate ==> in.FPredicate = combiner(_.builtInFPredicate(ctx))
  override def builtInMPredicate(ctx: Context): in.BuiltInMPredicate ==> in.MPredicate = combiner(_.builtInMPredicate(ctx))

  override def extendMethod(ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Method]] = extender(_.extendMethod(ctx))
  override def extendFunction(ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Function]] = extender(_.extendFunction(ctx))
  override def extendPredicate(ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Predicate]] = extender(_.extendPredicate(ctx))
  override def extendExpression(ctx: Context): in.Expr ==> Extension[CodeWriter[vpr.Exp]] = extender(_.extendExpression(ctx))
  override def extendAssertion(ctx: Context): in.Assertion ==> Extension[CodeWriter[vpr.Exp]] = extender(_.extendAssertion(ctx))
  override def extendStatement(ctx: Context): in.Stmt ==> Extension[CodeWriter[vpr.Stmt]] = extender(_.extendStatement(ctx))
}
