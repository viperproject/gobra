// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PActualEquality, PEquals, PUnequals}
import viper.gobra.frontend.info.base.Type.{StructClauseT, StructT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Equality extends BaseProperty { this: TypeInfoImpl =>

  private def isActualClause(clause: (String, StructClauseT)): Boolean = !clause._2.isGhost

  /** this property expresses that the equality comparison is non-trivial. */
  lazy val nontrivialGhostTypeEquality: Property[PActualEquality] = createFlatProperty[PActualEquality] {
    case e: PEquals => s"$e evaluates trivially to true as only non-ghost data is taken into account. Did you mean '==='?"
    case e: PUnequals => s"$e evaluates trivially to false as only non-ghost data is taken into account. Did you mean '!=='?"
  } (e => (underlyingType(exprOrTypeType(e.left)), underlyingType(exprOrTypeType(e.right))) match {
    case (l: StructT, r: StructT) =>l.clauses.exists(isActualClause) || r.clauses.exists(isActualClause)
    case _ => true
  })
}
