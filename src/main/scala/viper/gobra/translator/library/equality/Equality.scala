// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.equality

import viper.gobra.translator.library.Generator
import viper.silver.{ast => vpr}

trait Equality extends Generator {
  /** Return eq('l', 'r'), where eq(x,y) <==> x == y holds. */
  def eq(l: vpr.Exp, r: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.Exp
}
