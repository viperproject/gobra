// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.Expr
import viper.gobra.util.Violation
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.ast.Exp
import viper.silver.{ast => vpr}

/** Throws an error if a partial function that is expected to be defined on all inputs is not defined on an input. */
class FinalTypeEncoding(te: TypeEncoding) extends TypeEncoding {

  /** Runtime error if a match was expected. */
  private def expectedMatch(name: String): Any ==> Nothing = {
    case x: in.Expr => Violation.violation(s"Node $x (${x.getClass}) :: ${x.typ} did not match with any implemented case of $name. ")
    case (x: in.Assignee, y, z) => Violation.violation(s"Node ($x (${x.getClass}) :: ${x.op.typ}, $y, $z) did not match with any implemented case of $name. ")
    case (x: in.Expr, y, z) => Violation.violation(s"Node ($x (${x.getClass}) :: ${x.typ}, $y, $z) did not match with any implemented case of $name. ")
    case n => Violation.violation(s"Node $n (${n.getClass}) did not match with any implemented case of $name. ")
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = te.finalize(addMemberFn)

  /* A result is not guaranteed */
  override def varPrecondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = te.varPrecondition(ctx)
  override def varPostcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = te.varPostcondition(ctx)

  /* A result is guaranteed */
  override def typ(ctx: Context): in.Type ==> vpr.Type = te.typ(ctx) orElse expectedMatch("typ")
  override def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = te.variable(ctx) orElse expectedMatch("variable")
  override def globalVar(ctx: Context): in.GlobalVar ==> CodeWriter[vpr.Exp] = te.globalVar(ctx) orElse expectedMatch("globalVar")
  override def method(ctx: Context): in.Member ==> MemberWriter[vpr.Method] = te.method(ctx) orElse expectedMatch("method")
  override def function(ctx: Context): in.Member ==> MemberWriter[vpr.Function] = te.function(ctx) orElse expectedMatch("function")
  override def predicate(ctx: Context): in.Member ==> MemberWriter[vpr.Predicate] = te.predicate(ctx) orElse expectedMatch("predicate")
  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = te.member(ctx) orElse expectedMatch("member")
  override def initialization(ctx: Context): in.Location ==> CodeWriter[vpr.Stmt] = te.initialization(ctx) orElse expectedMatch("initialization")
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = te.assignment(ctx) orElse expectedMatch("assignment")
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = te.equal(ctx) orElse expectedMatch("equal")
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = te.goEqual(ctx) orElse expectedMatch("equal")
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = te.expression(ctx) orElse expectedMatch("expression")
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = te.assertion(ctx) orElse expectedMatch("assertion")
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = te.reference(ctx) orElse expectedMatch("reference")
  override def addressFootprint(ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = te.addressFootprint(ctx) orElse expectedMatch("addressFootprint")
  override def isComparable(ctx: Context): Expr ==> Either[Boolean, CodeWriter[Exp]] = te.isComparable(ctx) orElse expectedMatch("isComparable")
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = te.statement(ctx) orElse expectedMatch("statement")

  override def builtInMethod(ctx: Context): in.BuiltInMethod ==> in.MethodMember = te.builtInMethod(ctx) orElse expectedMatch("built-in method")
  override def builtInFunction(ctx: Context): in.BuiltInFunction ==> in.FunctionMember = te.builtInFunction(ctx) orElse expectedMatch("built-in method")
  override def builtInFPredicate(ctx: Context): in.BuiltInFPredicate ==> in.FPredicate = te.builtInFPredicate(ctx) orElse expectedMatch("built-in method")
  override def builtInMPredicate(ctx: Context): in.BuiltInMPredicate ==> in.MPredicate = te.builtInMPredicate(ctx) orElse expectedMatch("built-in method")

  override def extendMethod(ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Method]] = te.extendMethod(ctx) orElse { _ => identity }
  override def extendFunction(ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Function]] = te.extendFunction(ctx) orElse { _ => identity }
  override def extendPredicate(ctx: Context): in.Member ==> Extension[MemberWriter[vpr.Predicate]] = te.extendPredicate(ctx) orElse { _ => identity }
  override def extendExpression(ctx: Context): in.Expr ==> Extension[CodeWriter[vpr.Exp]] = te.extendExpression(ctx) orElse { _ => identity }
  override def extendAssertion(ctx: Context): in.Assertion ==> Extension[CodeWriter[vpr.Exp]] = te.extendAssertion(ctx) orElse { _ => identity }
  override def extendStatement(ctx: Context): in.Stmt ==> Extension[CodeWriter[vpr.Stmt]] = te.extendStatement(ctx) orElse { _ => identity }
}
