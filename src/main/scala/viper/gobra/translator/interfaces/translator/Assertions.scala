// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.{ast => vpr}

trait Assertions
  extends BaseTranslator[in.Assertion, CodeWriter[vpr.Exp]] {

  def invariant(x: in.Assertion)(ctx: Context): (CodeWriter[Unit], vpr.Exp)
  def precondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp]
  def postcondition(x: in.Assertion)(ctx: Context): MemberWriter[vpr.Exp]
}
