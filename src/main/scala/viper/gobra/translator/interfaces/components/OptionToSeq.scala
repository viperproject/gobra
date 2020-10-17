// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.components

import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait OptionToSeq extends Generator {

  /**
    * Gives a Viper domain function application 'opt2seq(`exp`)'
    * for converting the option type expression `exp` to a sequence
    * of type `typ`. Here `exp` is assumed to be of type 'option[`typ`]',
    */
  def create(exp : vpr.Exp, typ : vpr.Type)(pos : vpr.Position = vpr.NoPosition, info : vpr.Info = vpr.NoInfo, errT : vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp
}
