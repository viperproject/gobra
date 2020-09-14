// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.util.Entity
import viper.gobra.ast.frontend.PIdnNode
import viper.gobra.frontend.info.base.SymbolTable.{DataEntity, TypeEntity}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait PointsTo extends BaseProperty { this: TypeInfoImpl =>

  private def satisfies(n: PIdnNode)(f: Entity ==> Boolean): Boolean = f.lift(entity(n)).getOrElse(false)

  lazy val pointsToData: Property[PIdnNode] = createBinaryProperty("use of a variable or constant"){
    n => satisfies(n){ case _: DataEntity => true}
  }

  lazy val pointsToType: Property[PIdnNode] = createBinaryProperty("use of a Type"){
    n => satisfies(n){ case _: TypeEntity => true}
  }
}
