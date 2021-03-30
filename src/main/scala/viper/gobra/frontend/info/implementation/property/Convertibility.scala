// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.{DeclaredT, IntT, PermissionT, PointerT, Single, SliceT, StringT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Convertibility extends BaseProperty { this: TypeInfoImpl =>

  // TODO: check where convertibility and where assignability is required.

  lazy val convertibleTo: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not convertible to $right"
  } {
    case (Single(lst), Single(rst)) => (lst, rst) match {
      case (left, right) if assignableTo(left, right) => true
      case (IntT(_), IntT(_)) => true
      case (SliceT(IntT(config.typeBounds.Byte)), StringT) => true
      case (StringT, SliceT(IntT(config.typeBounds.Byte))) => true
      case (IntT(_), PermissionT) => true
      case (left, right) => (underlyingType(left), underlyingType(right)) match {
        case (l, r) if identicalTypes(l, r) => true
        case (IntT(_), IntT(_)) => true
        case (PointerT(l), PointerT(r)) if identicalTypes(underlyingType(l), underlyingType(r)) &&
          !(left.isInstanceOf[DeclaredT] && right.isInstanceOf[DeclaredT]) => true
        case _ => false
      }
    }
    case _ => false
  }
}
