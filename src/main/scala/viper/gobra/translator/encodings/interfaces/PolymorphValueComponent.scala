// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}

/** Polymorphic value that can fit all countable types. */
trait PolymorphValueComponent extends Generator {

  /** Type of polymorphic value. */
  def typ()(ctx: Context): vpr.Type

  /** Puts the expression into an polymorphic value. */
  def box(arg: vpr.Exp)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp

  /** Extracts an expression from the polymorphic value. */
  def unbox(arg: vpr.Exp, typ: in.Type)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp
}
