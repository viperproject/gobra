// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.arrays

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}

trait SharedArrayEmbedding {

  /** Boxing in the context of the shared-array domain. */
  def box(arg : vpr.Exp, typ : in.ArrayT)(src: in.Node)(ctx: Context): vpr.Exp

  /** Unboxing in the context of the shared-array domain. */
  def unbox(arg : vpr.Exp, typ : in.ArrayT)(src: in.Node)(ctx: Context): vpr.Exp
}
