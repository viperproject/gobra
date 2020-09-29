// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.UnknownValues
import viper.silver.{ast => vpr}

class UnknownValuesImpl extends UnknownValues {

  override def finalize(col: Collector): Unit = {
    if (genFunctions.nonEmpty) {
      val domain = vpr.Domain(
        name = domainName,
        typVars = Seq.empty,
        functions = genFunctions,
        axioms = Seq.empty
      )()

      col.addMember(domain)
    }
  }

  private val domainName: String = Names.unknownValuesDomain
  private var genFunctions: List[vpr.DomainFunc] = List.empty
  private var counter = 0
  private def funcName: String = s"${domainName}_$counter"

  private def genFunction(t: vpr.Type): vpr.DomainFunc = {
    val newFunc = vpr.DomainFunc(
      name = funcName,
      formalArgs = Seq.empty,
      typ = t
    )(domainName = domainName)
    genFunctions ::= newFunc
    counter += 1
    newFunc
  }

  /** Returns an unknown value. */
  def unkownValue(t: vpr.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.Exp = {
    val function = genFunction(t)
    vpr.DomainFuncApp(
      func = function,
      args = Seq.empty,
      typVarMap = Map.empty
    )(pos, info, errT)
  }

}
