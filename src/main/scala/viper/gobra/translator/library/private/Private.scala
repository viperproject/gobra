// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.privates

import viper.gobra.translator.library.Generator
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.gobra.reporting.BackTranslator.ErrorTransformer

trait Private extends Generator {

  /*
   * Generates method with name '${x.name.name}_public' the private proof vpr.Method of 'x'
   * The generated method takes as argument the method 'x'.
   */
  def privateProofMethod(x: in.Method)(ctx: Context): Unit

  /*
   * Generates method with name '${x.name.name}_public' and the public specifications of 'x'
   * The generated method takes as argument the function 'x'.
   */
  def privateProofFunction(x: in.Function)(ctx: Context): Unit


  def privateProofError(funcId: String): ErrorTransformer
  
}
