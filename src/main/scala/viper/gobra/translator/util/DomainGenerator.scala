// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.translator.library.Generator
import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}

trait DomainGenerator[T] extends Generator {

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = generatedMember foreach addMemberFn

  private var generatedMember: List[vpr.Domain] = List.empty
  private var genMap: Map[T, vpr.Domain] = Map.empty

  def genDomain(x: T)(ctx: Context): vpr.Domain

  def apply(args: Vector[vpr.Type], x: T)(ctx: Context): vpr.DomainType = {
    val domain = genMap.getOrElse(x, {
      val newDomain = genDomain(x)(ctx)
      genMap += x -> newDomain
      generatedMember ::= newDomain
      newDomain
    })
    vpr.DomainType(domain, domain.typVars.zip(args).toMap)
  }
}

trait DomainGeneratorWithoutContext[T] extends Generator {
  override def finalize(addMemberFn: vpr.Member => Unit): Unit = generatedMember foreach addMemberFn

  private var generatedMember: List[vpr.Domain] = List.empty
  private var genMap: Map[T, vpr.Domain] = Map.empty

  def genDomain(x: T): vpr.Domain

  def apply(args: Vector[vpr.Type], x: T): vpr.DomainType = {
    val domain = genMap.getOrElse(x, {
      val newDomain = genDomain(x)
      genMap += x -> newDomain
      generatedMember ::= newDomain
      newDomain
    })
    vpr.DomainType(domain, domain.typVars.zip(args).toMap)
  }
}