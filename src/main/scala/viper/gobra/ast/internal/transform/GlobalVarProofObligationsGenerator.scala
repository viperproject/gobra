// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.ast.internal.transform
import viper.gobra.ast.internal.Program

object GlobalVarProofObligationsGenerator extends InternalTransform {

  // TODO:
  // separate globals per file,
  // get spec per file
  // inline inits in initialization code per file and remove from the program
  // sort vars per decl order and dependency order

  override def name(): String = ???

  /**
    * Program-to-program transformation
    */
  override def transform(p: Program): Program = ???
}
