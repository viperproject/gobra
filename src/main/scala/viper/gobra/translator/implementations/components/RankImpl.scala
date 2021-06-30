// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Rank
import viper.silver.ast.{ErrorTrafo, Exp, Info, Position}
import viper.silver.{ast => vpr}

class RankImpl extends Rank {

  private var isUsed: Boolean = false

  override def finalize(col: Collector): Unit = {
    if(isUsed) {
      col.addMember(domain)
    }
  }

  private val (domain: vpr.Domain, rankFunc: vpr.DomainFunc, typeVar: vpr.TypeVar) = {
    val domainName = Names.rankDomain

    val typeVar = vpr.TypeVar("T")

    val aDecl = vpr.LocalVarDecl("a", typeVar)()

    val rankFunc = vpr.DomainFunc(Names.rankFunc, Seq(aDecl), vpr.Int)(domainName = domainName)

    val domain = vpr.Domain(
      name = domainName,
      functions = Seq(rankFunc),
      axioms = Seq(),
      typVars = Seq(typeVar)
    )()

    (domain, rankFunc, typeVar)
  }

  override def rank(a: Exp)(pos: Position, info: Info, errT: ErrorTrafo): Exp = {
    isUsed = true
    val typeVarMap = Map(typeVar -> a.typ)
    vpr.DomainFuncApp(
      rankFunc,
      Seq(a),
      typeVarMap
    )(pos, info, errT)
  }
}
