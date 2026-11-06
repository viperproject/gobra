// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.{ActualPointerT, DeclaredT, Float32T, Float64T, FloatT, GhostPointerT, IntT, PermissionT, Single, SliceT, StringT, StructT, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Convertibility extends BaseProperty { this: TypeInfoImpl =>

  // TODO: check where convertibility and where assignability is required.

  /**
    * True iff the legality of converting `left` to `right` depends on the structure of a struct
    * type declared in another package. Such conversions are rejected, as they may depend on
    * non-exported fields, which importing packages cannot rely upon.
    */
  private def conversionDependsOnPrivateStructure(left: Type, right: Type): Boolean = {
    def stripPointer(t: Type): Type = t match {
      case ActualPointerT(e) => e
      case GhostPointerT(e) => e
      case _ => t
    }
    val (l, r) = (stripPointer(left), stripPointer(right))
    (isImportedContextualType(l) || isImportedContextualType(r)) &&
      underlyingType(l).isInstanceOf[StructT] && underlyingType(r).isInstanceOf[StructT]
  }

  lazy val convertibleTo: Property[(Type, Type, Boolean)] = createFlatPropertyWithReason[(Type, Type, Boolean)] {
    case (left, right, _) => s"$left is not convertible to $right"
  } {
    case (Single(lst), Single(rst), mayInit) => (lst, rst) match {
      case (left, right) if assignableTo(left, right, mayInit) => successProp
      case (left, right) if mayInit && assignableTo(left, right, false) =>
        // this branch is only for providing better error messages,
        // as it is logically unnecessary
        failedProp(s"Type $right may not be converted to type $left in code that may run during the initialization " +
          s"of this current package.")
      case (IntT(_), Float32T) => successProp
      case (IntT(_), Float64T) => successProp
      case (_: FloatT, IntT(_)) => successProp
      case (IntT(_), IntT(_)) => successProp
      case (SliceT(IntT(config.typeBounds.Byte)), StringT) => successProp
      case (StringT, SliceT(IntT(config.typeBounds.Byte))) => successProp
      case (IntT(_), PermissionT) => successProp
      case (left, right) if conversionDependsOnPrivateStructure(left, right) =>
        failedProp(s"cannot convert $left to $right, as the conversion depends on the structure " +
          s"of a struct type declared in another package, which may contain non-exported fields")
      case (left, right) => (underlyingType(left), underlyingType(right)) match {
        case (l, r) if identicalTypes(l, r) => successProp
        case (IntT(_), IntT(_)) => successProp
        case (ActualPointerT(l), ActualPointerT(r)) if identicalTypes(underlyingType(l), underlyingType(r)) &&
          !(left.isInstanceOf[DeclaredT] && right.isInstanceOf[DeclaredT]) => successProp
        case (GhostPointerT(l), GhostPointerT(r)) if identicalTypes(underlyingType(l), underlyingType(r)) &&
          !(left.isInstanceOf[DeclaredT] && right.isInstanceOf[DeclaredT]) => successProp
        case _ => errorProp()
      }
    }
    case _ => errorProp()
  }
}
