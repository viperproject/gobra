// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Tuples extends Generator {

  def typ(args: Vector[vpr.Type]): vpr.DomainType
  def create(args: Vector[vpr.Exp]): vpr.DomainFuncApp
  def get(arg: vpr.Exp, index: Int, arity: Int): vpr.DomainFuncApp
}
