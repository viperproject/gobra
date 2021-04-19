// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait FunctionGenerator[T] extends Generator {

  override def finalize(col: Collector): Unit = generatedMember.foreach(col.addMember(_))

  private var generatedMember: List[vpr.Function] = List.empty
  private var genMap: Map[T, vpr.Function] = Map.empty

  def genFunction(x: T)(ctx: Context): vpr.Function

  def getFunction(x: T)(ctx: Context): vpr.Function = {
    genMap.getOrElse(x, {
      val newFunc = genFunction(x)(ctx)
      genMap += x -> newFunc
      generatedMember ::= newFunc
      newFunc
    })
  }

  def apply(args: Vector[vpr.Exp], x: T)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp = {
    val func = getFunction(x)(ctx)
    vpr.FuncApp(func, args)(pos, info, errT)
  }
}

trait FunctionGeneratorWithoutContext[T] extends Generator {

  override def finalize(col: Collector): Unit = generatedMember.foreach(col.addMember(_))

  private var generatedMember: List[vpr.Function] = List.empty
  private var genMap: Map[T, vpr.Function] = Map.empty

  def genFunction(x: T): vpr.Function

  def getFunction(x: T): vpr.Function = {
    genMap.getOrElse(x, {
      val newFunc = genFunction(x)
      genMap += x -> newFunc
      generatedMember ::= newFunc
      newFunc
    })
  }

  def apply(args: Vector[vpr.Exp], x: T)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.FuncApp = {
    val func = getFunction(x)
    vpr.FuncApp(func, args)(pos, info, errT)
  }
}
