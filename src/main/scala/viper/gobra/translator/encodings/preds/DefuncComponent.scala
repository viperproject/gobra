// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.preds

import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}

trait DefuncComponent extends Generator {

  /** Returns the predicate type with argument types 'ts'. */
  def typ(ts: Vector[in.Type])(ctx: Context): vpr.Type

  /** Returns the default value of the predicate type with arguments 'ts' */
  def default(ts: Vector[in.Type])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Returns the predicate expression resulting from the construction on 'predicate' with arguments 'args'. */
  def construct(predicate: in.PredicateProxy, predTs: Vector[in.Type], args: Vector[Option[vpr.Exp]])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    val pattern = computePattern(args)
    val pId = id(predicate, predTs, pattern)(ctx)
    val resTs = computeConstructTs(predTs, pattern)
    make(pId, args.flatten, resTs)(pos, info, errT)(ctx)
  }

  /** Computes the pattern of applied arguments. */
  def computePattern[A](args: Vector[Option[A]]): Vector[Boolean] =
    args map (_.isEmpty)

  /** Computes the argument types of a predicate constructors with a base of type pred('baseTs') and a pattern 'pattern'. */
  def computeConstructTs(baseTs: Vector[in.Type], pattern: Vector[Boolean]): Vector[in.Type] =
    (baseTs zip pattern) collect { case (t, true) => t }

  /** Computes the types of the applied arguments for a predicate constructors with a base of type pred('baseTs') and a pattern 'pattern'. */
  def computeAppliedTs(baseTs: Vector[in.Type], pattern: Vector[Boolean]): Vector[in.Type] =
    (baseTs zip pattern) collect { case (t, false) => t }

  /**
    * Returns the predicate identifier associated to a predicate 'predicate' with a combination of applied arguments 'pattern' where the predicate has type pred('predTs').
    * 'pattern' has value 'true' at index i iff the i-th argument of 'predicate' is not partially applied with a concrete value.
    * */
  def id(predicate: in.PredicateProxy, predTs: Vector[in.Type], pattern: Vector[Boolean])(ctx: Context): BigInt

  /** Returns the predicate expression of type pred('resTs') resulting from the construction on 'id' with arguments 'args'. */
  def make(id: BigInt, args: Vector[vpr.Exp], resTs: Vector[in.Type])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Returns the predicate instance of predicate expression 'base' with type pred('baseTs') with arguments 'args'. */
  def instance(base: vpr.Exp, baseTs: Vector[in.Type], args: Vector[vpr.Exp])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.PredicateAccess
}
