// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Conditions
import viper.silver.{ast => vpr}

class ConditionsImpl extends Conditions {

  override def finalize(col: Collector): Unit = col.addMember(assertFunction)

  /**
    * Generates:
    * function assertArg(b: Boolean): Boolean
    *   requires b
    * { true }
    */
  private val assertFunction: vpr.Function = {
    val b = vpr.LocalVarDecl("b", vpr.Bool)()
    vpr.Function(
      name = Names.assertFunc,
      formalArgs = Seq(b),
      typ = vpr.Bool,
      pres = Seq(b.localVar),
      posts = Seq.empty,
      body = Some(vpr.TrueLit()())
    )()
  }

  /** Returns true, but asserts that the argument holds. */
  override def assert(x: vpr.Exp): vpr.Exp = vpr.FuncApp(assertFunction, Seq(x))(x.pos, x.info, x.errT)
}
