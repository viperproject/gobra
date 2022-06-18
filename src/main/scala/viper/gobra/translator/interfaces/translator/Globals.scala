// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.silver.{ast => vpr}

abstract class Globals extends Generator {

  def globalVarDecl(decl: in.GlobalVarDecl)(ctx: Context): MemberWriter[Vector[vpr.Function]]

}
