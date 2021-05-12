// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.TypeBounds

trait TypeIdentity extends BaseProperty { this: TypeInfoImpl =>

  lazy val identicalTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not identical to $right"
  } {
    case (Single(lst), Single(rst)) => (lst, rst) match {

      // Two integer types are equal if they have the same type or they are from types which are aliased
      case (IntT(x), IntT(y)) => x == y || Set(x,y).subsetOf(Set(TypeBounds.SignedInteger32, TypeBounds.Rune)) || Set(x,y).subsetOf(Set(TypeBounds.UnsignedInteger8, TypeBounds.Byte))
      case (BooleanT, BooleanT) => true
      case (AssertionT, AssertionT) => true
      case (StringT, StringT) => true
      case (PermissionT, PermissionT) => true

      case (DeclaredT(l, contextL), DeclaredT(r, contextR)) => l == r && contextL == contextR

      case (ArrayT(ll, l), ArrayT(rl, r)) => ll == rl && identicalTypes(l, r)
      case (SliceT(l), SliceT(r)) => identicalTypes(l, r)
      case (GhostSliceT(l), GhostSliceT(r)) => identicalTypes(l, r)
      case (MapT(k1, v1), MapT(k2, v2)) => identicalTypes(k1, k2) && identicalTypes(v1, v2)
      case (SequenceT(l), SequenceT(r)) => identicalTypes(l, r)
      case (SetT(l), SetT(r)) => identicalTypes(l, r)
      case (MultisetT(l), MultisetT(r)) => identicalTypes(l, r)
      case (MathMapT(k1, v1), MathMapT(k2, v2)) => identicalTypes(k1, k2) && identicalTypes(v1, v2)
      case (OptionT(l), OptionT(r)) => identicalTypes(l, r)
      case (l: DomainT, r: DomainT) => l == r

      case (StructT(clausesL, _, contextL), StructT(clausesR, _, contextR)) =>
        contextL == contextR && clausesL.size == clausesR.size && clausesL.zip(clausesR).forall {
          case (lm, rm) => lm._1 == rm._1 && lm._2._1 == rm._2._1 && identicalTypes(lm._2._2, rm._2._2)
        }

      case (l: InterfaceT, r: InterfaceT) =>
        val lm = interfaceMethodSet(l).toMap
        val rm = interfaceMethodSet(r).toMap
        lm.keySet.forall(k => rm.get(k).exists(m => identicalTypes(memberType(m), memberType(lm(k))))) &&
          rm.keySet.forall(k => lm.get(k).exists(m => identicalTypes(memberType(m), memberType(rm(k)))))

      case (PointerT(l), PointerT(r)) => identicalTypes(l, r)

      case (FunctionT(larg, lr), FunctionT(rarg, rr)) =>
        larg.size == rarg.size && larg.zip(rarg).forall {
          case (l, r) => identicalTypes(l, r)
        } && identicalTypes(lr, rr)

      case (PredT(larg), PredT(rarg)) =>
        larg.size == rarg.size && larg.zip(rarg).forall {
          case (l, r) => identicalTypes(l, r)
        }

      case (ChannelT(le, lm), ChannelT(re, rm)) => identicalTypes(le, re) && lm == rm

      case (VoidType, VoidType) => true

      case _ => false
    }

    case (InternalTupleT(lv), InternalTupleT(rv)) =>
      lv.size == rv.size && lv.zip(rv).forall {
        case (l, r) => identicalTypes(l, r)
      }
    case (VoidType, InternalTupleT(Vector())) => true
    case (InternalTupleT(Vector()), VoidType) => true

    case _ => false
  }
}
