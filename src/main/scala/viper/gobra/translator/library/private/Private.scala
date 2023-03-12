// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.library.privates

import viper.gobra.translator.library.Generator
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.MemberLevel._
import viper.silver.{ast => vpr}

trait Private extends Generator {

  /*
   * Generates method with name '${x.name.name}_$Public'. That method is the private proof vpr.Method of 'x'
   * This method takes as argument the method 'x'.
   */
  def privateProofMethod(x: in.Method)(ctx: Context): Writer[Option[vpr.Method]]

  /*
   * Generates method with name '${x.name.name}_$Public'. That method is the private proof vpr.Method of 'x'
   * This method takes as argument the method 'x'.
   */
  def privateProofFunction(x: in.Function)(ctx: Context): Writer[Option[vpr.Method]]
  
}
