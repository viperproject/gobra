// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.translator.library.Generator
import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}

trait MethodGenerator[T] extends Generator {

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = generatedMember foreach addMemberFn

  private var generatedMember: List[vpr.Method] = List.empty
  private var genMap: Map[T, vpr.Method] = Map.empty

  def genMethod(x: T)(ctx: Context): vpr.Method

  def getMethod(x: T)(ctx: Context): vpr.Method = {
    genMap.getOrElse(x, {
      val newMethod = genMethod(x)(ctx)
      genMap += x -> newMethod
      generatedMember ::= newMethod
      newMethod
    })
  }

  def apply(args: Vector[vpr.Exp], targets: Seq[vpr.LocalVar], x: T)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.MethodCall = {
    val method = getMethod(x)(ctx)
    vpr.MethodCall(method, args, targets)(pos, info, errT)
  }
}