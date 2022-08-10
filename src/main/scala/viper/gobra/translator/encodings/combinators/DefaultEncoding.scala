// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.combinators

import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}

trait DefaultEncoding {

  def method(x: in.Method)(ctx: Context): MemberWriter[vpr.Method]

  def function(x: in.Function)(ctx: Context): MemberWriter[vpr.Method]

  def pureMethod(x: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function]

  def pureFunction(x: in.PureFunction)(ctx: Context): MemberWriter[vpr.Function]

  def mpredicate(x: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate]

  def fpredicate(x: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate]

  def globalVarDeclaration(x: in.GlobalVarDecl)(ctx: Context): MemberWriter[Vector[vpr.Function]]
}
