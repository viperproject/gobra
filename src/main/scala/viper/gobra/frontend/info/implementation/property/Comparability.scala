// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.SymbolTable.{Embbed, Field}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Comparability extends BaseProperty { this: TypeInfoImpl =>

  lazy val comparableTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not comparable with $right"
  } {
    case (Single(left), Single(right)) =>
      (assignableTo(left, right) || assignableTo(right, left)) && ((left, right) match {
        case (l, r) if comparableType(l) && comparableType(r) => true
        case (NilType, r) if isPointerType(r) => true
        case (l, NilType) if isPointerType(l) => true
        case _ => false
      })
    case _ => false
  }

  lazy val comparableType: Property[Type] = createBinaryProperty("comparable") {
    case Single(st) => underlyingType(st) match {
      case t: StructT =>
        structMemberSet(t).collect {
          case (_, f: Field) => typeSymbType(f.decl.typ)
          case (_, e: Embbed) => miscType(e.decl.typ)
        }.forall(comparableType)

      case _: SliceT | _: GhostSliceT | _: MapT | _: FunctionT => false
      case _ => true
    }
    case _ => false
  }
}
