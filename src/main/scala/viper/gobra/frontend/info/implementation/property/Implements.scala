// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.PExplicitGhostStructClause
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.base.Type.{GhostCollectionType, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Implements { this: TypeInfoImpl =>

  def implements(l: Type, r: Type): Boolean = underlyingType(r) match {
    case t: Type.InterfaceT if t.isEmpty => supportedSortForInterfaces(l)
    case _ => false
  }


  /** Returns true if the type is supported for interfaces. All finite types are supported. */
  def supportedSortForInterfaces(t: Type): Boolean = {
    isIdentityPreservingType(t)
  }

  /** Returns whether values of type 't' satisfy that [x] == [y] in Viper implies x == y in Gobra. */
  private def isIdentityPreservingType(t: Type, encounteredTypes: Set[Type] = Set.empty): Boolean = {
    if (encounteredTypes contains t) {
      true
    } else {
      def go(t: Type): Boolean = isIdentityPreservingType(t, encounteredTypes + t)
      underlyingType(t) match {
        case Type.NilType | Type.BooleanT | _: Type.IntT => true
        case ut: Type.PointerT => go(ut.elem)
        case ut: Type.StructT =>
          ut.decl.clauses.forall(!_.isInstanceOf[PExplicitGhostStructClause]) &&
            ut.clauses.forall{ case (_, (_, fieldType)) => go(fieldType) }
        case ut: Type.ArrayT => go(ut.elem)
        case _: Type.SliceT => true
        case ut: Type.OptionT => go(ut.elem)
        case ut: GhostCollectionType => go(ut.elem)
        case _ => false
      }
    }

  }

}
