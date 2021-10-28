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

trait TerminationMeasures
  extends BaseTranslator[in.TerminationMeasure, CodeWriter[vpr.Exp]] {

  def decreases(measure: in.TerminationMeasure)(ctx: Context): MemberWriter[vpr.Exp]
}
