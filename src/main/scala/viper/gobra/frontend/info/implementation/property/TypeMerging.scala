// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.{IntT, SequenceT, Single, Type, UntypedConst}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TypeMerging extends BaseProperty { this: TypeInfoImpl =>

  /**
    * Returns the unique common assignable type of l and r (if it exists).
    */
  def typeMerge(l: Type, r: Type): Option[Type] =
    // currently, only merging of l and r being identical types is supported
    // possible future improvement: if l is assignable to r, then return r instead of None (or vice versa)
    (l, r) match {
      case (Single(lst), Single(rst)) =>
        if (identicalTypes(lst, rst)) Some(lst) else {
          (lst, rst) match {
            case (a, IntT(UntypedConst)) if underlyingType(a).isInstanceOf[IntT] => Some(a)
            case (IntT(UntypedConst), b) if underlyingType(b).isInstanceOf[IntT] => Some(b)
            case (SequenceT(l), SequenceT(r)) => for {
              typ <- typeMerge(l,r)
            } yield SequenceT(typ)
            case _ => None
          }
        }
      case _ => None
    }

  lazy val mergeableTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not mergeable with $right"
  } {
    case (left, right) => typeMerge(left, right).isDefined
  }

}
