// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.arrays

import viper.gobra.translator.library.Generator
import viper.silver.{ast => vpr}

trait Arrays extends Generator {
  /**
    * Upper bound for array lengths, set to the configured `int` kind's maximum under bounded
    * integer semantics (`None` under `--unboundedIntegers`). Go guarantees that the number of
    * elements of any array, slice, or string fits in `int`, so `len(a) <= MaxInt` is sound and
    * needed for bounded-integer quantifiers to entail internally generated footprints over the
    * unbounded length. Must be set before the first domain is generated.
    */
  var intUpperBound: Option[BigInt] = None

  def len(a: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.Exp

  def loc(a: vpr.Exp, i: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.Exp

  def typ(t: vpr.Type): vpr.Type
}
