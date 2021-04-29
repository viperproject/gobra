// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.{ArrayT, ChannelT, GhostSliceT, IntT, InternalSingleMulti, InternalTupleT, MapT, MultisetT, PermissionT, PointerT, SequenceT, SetT, Single, SliceT, Type}
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
            case (a, UNTYPED_INT_CONST) if underlyingType(a).isInstanceOf[IntT] => Some(a)
            case (UNTYPED_INT_CONST, b) if underlyingType(b).isInstanceOf[IntT] => Some(b)
            case (IntT(_), PermissionT) => Some(PermissionT)
            case (PermissionT, IntT(_)) => Some(PermissionT)
            case (SequenceT(l), SequenceT(r)) => typeMerge(l,r) map SequenceT
            case (SetT(l), SetT(r)) => typeMerge(l,r) map SetT
            case (MultisetT(l), MultisetT(r)) => typeMerge(l,r) map MultisetT
            case (ArrayT(len1, l), ArrayT(len2, r)) if len1 == len2 => typeMerge(l,r) map (ArrayT(len1, _))
            case (SliceT(l), SliceT(r)) => typeMerge(l,r) map SliceT
            case (GhostSliceT(l), GhostSliceT(r)) => typeMerge(l,r) map GhostSliceT
            case (MapT(k1, v1), MapT(k2, v2)) => for {
              k <- typeMerge(k1, k2)
              v <- typeMerge(v1, v2)
            } yield MapT(k,v)
            case (PointerT(l), PointerT(r)) => typeMerge(l,r) map PointerT
            case (ChannelT(l, mod1), ChannelT(r, mod2)) if mod1 == mod2 => typeMerge(l,r) map (ChannelT(_,mod1))
            case (InternalTupleT(v1), InternalTupleT(v2)) =>
              val v = v1.zip(v2).map {case (l, r) => typeMerge(l, r)}
              if (v.contains(None)) None else Some(InternalTupleT(v map (_.get)))
            case (InternalSingleMulti(sin1, multi1), InternalSingleMulti(sin2, multi2)) => for {
              sin <- typeMerge(sin1, sin2)
              multi <- typeMerge(multi1, multi2)
            } yield InternalSingleMulti(sin, multi.asInstanceOf[InternalTupleT])

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
