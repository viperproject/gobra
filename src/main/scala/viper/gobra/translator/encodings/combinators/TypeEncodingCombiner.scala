// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import viper.gobra.translator.encodings.TypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.Expr
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.ast.Exp
import viper.silver.{ast => vpr}

/**
  * Combines a vector of type encodings.
  */
abstract class TypeEncodingCombiner(encodings: Vector[TypeEncoding]) extends TypeEncoding {

  /**
    * Combines partial functions selected by 'get' into a single partial function
    * @param get function selecting a partial function of the type encoding.
    * @return combined partial function.
    */
  protected[combinators] def combiner[X, Y](get: TypeEncoding => (X ==> Y)): X ==> Y


  override def finalize(col: Collector): Unit = encodings.foreach(_.finalize(col))
  override def typ(ctx: Context): in.Type ==> vpr.Type = combiner(_.typ(ctx))
  override def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = combiner(_.variable(ctx))
  override def globalVar(ctx: Context): in.GlobalVar ==> CodeWriter[vpr.Exp] = combiner(_.globalVar(ctx))
  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = combiner(_.member(ctx))
  override def precondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = combiner(_.precondition(ctx))
  override def postcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = combiner(_.postcondition(ctx))
  override def initialization(ctx: Context): in.Location ==> CodeWriter[vpr.Stmt] = combiner(_.initialization(ctx))
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = combiner(_.assignment(ctx))
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = combiner(_.equal(ctx))
  override def goEqual(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = combiner(_.goEqual(ctx))
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = combiner(_.expr(ctx))
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = combiner(_.assertion(ctx))
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = combiner(_.reference(ctx))
  override def addressFootprint(ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = combiner(_.addressFootprint(ctx))
  override def isComparable(ctx: Context): Expr ==> Either[Boolean, CodeWriter[Exp]] = combiner(_.isComparable(ctx))
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = combiner(_.statement(ctx))
}
