// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.fields

import viper.gobra.translator.context.Context
import viper.gobra.translator.library.Generator
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}

trait Fields extends Generator {

  def field(t: in.Type)(ctx: Context): vpr.Field
}
