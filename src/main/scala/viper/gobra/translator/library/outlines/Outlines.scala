// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.outlines

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait Outlines extends Generator {

  /**
    * Moves code `body` into a generated method with name `name`, preconditions `pres`, and postconditions `pros`.
    * Returns a call to the generated method with the appropriate arguments and results.
    */
  def outline(
               name: String,
               pres: Vector[vpr.Exp],
               posts: Vector[vpr.Exp],
               body: vpr.Stmt,
               trusted: Boolean,
             )(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.Stmt

  /**
    * Generates method with name `name`, preconditions `pres`, and postconditions `pros`.
    * The generated method takes as arguments `arguments`. `modifies` specifies which of the arguments are modified.
    * Returns a call to the generated method with the appropriate arguments and results.
    */
  def outlineWithoutBody(
               name: String,
               pres: Vector[vpr.Exp],
               posts: Vector[vpr.Exp],
               arguments: Vector[vpr.LocalVar],
               modifies:  Vector[vpr.LocalVar],
             )(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.Stmt
}
