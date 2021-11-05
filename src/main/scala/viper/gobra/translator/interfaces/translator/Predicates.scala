// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.translator

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.plugin.standard.predicateinstance

abstract class Predicates extends Generator {

  def mpredicate(pred: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate]
  def fpredicate(pred: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate]

  def predicateAccess(ctx: Context): (in.PredicateAccess, in.Expr) ==> CodeWriter[vpr.PredicateAccessPredicate]

  def predicate(ctx: Context): in.PredicateAccess ==> CodeWriter[predicateinstance.PredicateInstance]

  /** Returns proxy(args) */
  def proxyAccess(proxy: in.PredicateProxy, args: Vector[in.Expr], perm: in.Expr)(src: Source.Parser.Info)(ctx: Context): CodeWriter[vpr.PredicateAccessPredicate]
}
