// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.Expr
import viper.gobra.translator.encodings.TypeEncoding
import viper.gobra.util.Violation
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
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

  override def finalize(col: Collector): Unit = te.finalize(col)
  override def typ(ctx: Context): in.Type ==> vpr.Type = te.typ(ctx) orElse expectedMatch("typ")
  override def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = te.variable(ctx) orElse expectedMatch("variable")
  override def globalVar(ctx: Context): in.GlobalVar ==> CodeWriter[vpr.Exp] = te.globalVar(ctx) orElse expectedMatch("globalVar")
  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = te.member(ctx)
  override def precondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = te.precondition(ctx)
  override def postcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = te.postcondition(ctx)
  override def initialization(ctx: Context): in.Location ==> CodeWriter[vpr.Stmt] = te.initialization(ctx) orElse expectedMatch("initialization")
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = te.assignment(ctx) orElse expectedMatch("assignment")
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = te.equal(ctx) orElse expectedMatch("equal")
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = te.goEqual(ctx) orElse expectedMatch("equal")
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = te.expr(ctx)
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = te.assertion(ctx)
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = te.reference(ctx) orElse expectedMatch("reference")
  override def addressFootprint(ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = te.addressFootprint(ctx) orElse expectedMatch("addressFootprint")
  override def isComparable(ctx: Context): Expr ==> Either[Boolean, CodeWriter[Exp]] = te.isComparable(ctx) orElse expectedMatch("isComparable")
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = te.statement(ctx)
}
