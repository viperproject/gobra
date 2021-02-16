// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.arrays

import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import ArrayEncoding.ComponentParameter

trait SharedArrayComponent extends Generator {

  /** Returns type of exclusive-array domain. */
  def typ(t: ComponentParameter)(ctx: Context): vpr.Type

  /** Getter of shared-array domain. */
  def get(base: vpr.Exp, idx: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Nil of shared-struct domain */
  def nil(t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Length of shared-array domain. */
  def length(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Boxing in the context of the shared-array domain. */
  def box(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Unboxing in the context of the shared-array domain. */
  def unbox(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp
}
