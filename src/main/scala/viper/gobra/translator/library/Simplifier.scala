// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.library

import viper.silver.{ast => vpr}

trait Simplifier {
  def simplify(node: vpr.Node): Option[vpr.Node]
}
