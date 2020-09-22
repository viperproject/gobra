// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}
import StructEncoding.ComponentParameter

trait ExclusiveStructComponent extends Generator {

  /** Returns type of exclusive-struct domain. */
  def typ(t: ComponentParameter)(ctx: Context): vpr.Type

  /** Constructor of exclusive-struct domain. */
  def create(args: Vector[vpr.Exp], t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Getter of exclusive-struct domain. */
  def get(base: vpr.Exp, idx: Int, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /**
    * Update function of exclusive-struct domain.
    *
    * The default implementation is:
    * update(e, i, v, n) -> create( get(e, 0, n), ..., get(e, i-1, n), v, get(e, i+1, n), ..., get(e, n-1, n) )
    * */
  def update(base: vpr.Exp, idx: Int, newVal: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp = {
    val lowerArgs = (0 until idx).map(l => get(base, l, t)(src)(ctx)).toVector
    val higherArgs = ((idx + 1) until t.size).map(l => get(base, l, t)(src)(ctx)).toVector
    create(lowerArgs ++ (newVal +: higherArgs), t)(src)(ctx)
  }
}
