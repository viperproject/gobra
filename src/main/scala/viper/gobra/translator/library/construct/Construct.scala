// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.library.construct

import viper.gobra.translator.library.Generator
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}
import viper.gobra.translator.util.ViperWriter.MemberLevel._
import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.gobra.reporting.Source

trait Construct extends Generator {

 /*
  * Generates function with name '${ctor.id.name}_$GEN_$DEREF'.
  * This function takes as argument the constructor 'ctor'.
  */
  def dereference(ctor: in.Constructor)(ctx: Context): Writer[Option[vpr.Function]]

 /*
  * Generates method with name '${ctor.id.name}_$GEN_$ASSIGN'.
  * This function takes as argument the constructor 'ctor'.
  */
  def assignments(ctor: in.Constructor)(ctx: Context): Writer[Option[vpr.Method]]

 /*
  * Gives an error if permission is not enough when calling the (generated) DEREF function.
  */
  def permissionDerefError(generated: Boolean): ErrorTransformer

 /*
  * Gives an error if permission is not enough when calling the (generated) ASSIGN method.
  */
  def permissionAssignError(generated: Boolean): ErrorTransformer

 /*
  * Gives an error if there is not enough permission to deref inside the DEREF function.
  */
  def derefWellFormedError(src: Source.Parser.Info, res: vpr.Exp): ErrorTransformer

 /*
  * Gives an error if there is not enough permission to assign inside the ASSIGN method.
  */
  def assignWellFormedError(src: Source.Parser.Info, res: vpr.Stmt): ErrorTransformer

 /*
  * Gives an error if the CONSTRUCTOR is not well formed.
  */
  def constructWellFormedError(res: vpr.Stmt): ErrorTransformer
  
}
