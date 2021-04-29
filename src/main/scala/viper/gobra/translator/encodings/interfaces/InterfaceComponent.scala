// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}

trait InterfaceComponent {

  /** Returns type of an interface */
  def typ(polyType: vpr.Type, dynTypeType: vpr.Type)(ctx: Context): vpr.Type

  /** Creates an interface */
  def create(polyVal: vpr.Exp, dynType: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Returns dynamic type of an interface */
  def dynTypeOf(itf: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Return polymorphic value of an interface */
  def polyValOf(itf: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp
}
