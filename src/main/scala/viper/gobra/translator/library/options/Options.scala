// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.options

import viper.gobra.translator.library.Generator
import viper.silver.{ast => vpr}

trait Options extends Generator {
  def get(exp: vpr.Exp, t: vpr.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp

  def isNone(exp: vpr.Exp, t: vpr.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp

  def none(t: vpr.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp

  def some(exp: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp

  def typ(t: vpr.Type): vpr.DomainType
}
