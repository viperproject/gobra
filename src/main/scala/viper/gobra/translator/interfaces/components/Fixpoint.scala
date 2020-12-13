// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.components

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Fixpoint extends Generator {

  def create(gc: in.GlobalConstDecl)(ctx: Context): Unit
  def get(gc: in.GlobalConst)(ctx: Context): vpr.DomainFuncApp
}
