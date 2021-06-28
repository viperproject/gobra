// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.theory

import viper.gobra.ast.internal._
import viper.gobra.theory.Addressability
import viper.gobra.util.RoseTree

import scala.annotation.tailrec

object Comparability {

  /** Supertype of the different kinds of comparability. */
  sealed trait Kind
  object Kind {
    /** Kind of types that are always comparable. */
    case object Comparable extends Kind
    /** Kind of types that are never comparable. */
    case object NonComparable extends Kind
    /** Kind of types that are comparable depending on their dynamic value. */
    case object Dynamic extends Kind
    /** Kind of types that are comparable when their element types are comparable. */
    case object Recursive extends Kind
  }

  @tailrec
  /** Returns the kind of comparability for the argument type head. */
  def compareKind(typeHead: TypeHead)(reg: DefinedT => Type): Kind = typeHead match {
    case TypeHead.BoolHD => Kind.Comparable
    case TypeHead.StringHD => Kind.Comparable
    case _: TypeHead.IntHD => Kind.Comparable
    case TypeHead.PointerHD => Kind.Comparable

    case t: TypeHead.DefinedHD =>
      compareKind(TypeHead.typeHead(reg(DefinedT(t.name, Addressability.Exclusive))))(reg)

    case _: TypeHead.StructHD => Kind.Recursive
    case TypeHead.ArrayHD => Kind.Recursive
    case TypeHead.SliceHD => Kind.NonComparable
    case TypeHead.MapHD => Kind.NonComparable
    case _: TypeHead.InterfaceHD => Kind.Dynamic
    case _: TypeHead.DomainHD => Kind.Comparable
    case TypeHead.ChannelHD => Kind.NonComparable
    case TypeHead.NilHD => Kind.Comparable
    case TypeHead.UnitHD => Kind.Comparable
    case TypeHead.PermHD => Kind.Comparable
    case TypeHead.SortHD => Kind.Comparable
    case TypeHead.SeqHD => Kind.Recursive
    case TypeHead.SetHD => Kind.Recursive
    case TypeHead.MSetHD => Kind.Recursive
    case TypeHead.MathMapHD => Kind.Recursive
    case TypeHead.OptionHD => Kind.Recursive
    case _: TypeHead.TupleHD => Kind.Recursive
    case _: TypeHead.PredHD => Kind.Comparable
  }

  /** Returns whether the type is comparable. If none is returned, then comparability depends on the dynamic value. */
  def comparable(typeTree: RoseTree[TypeHead])(reg: DefinedT => Type): Option[Boolean] = {
    compareKind(typeTree.head)(reg) match {
      case Kind.Comparable => Some(true)
      case Kind.NonComparable =>Some(false)
      case Kind.Dynamic => None

      case Kind.Recursive =>
        val x = typeTree.children flatMap (comparable(_)(reg))
        if (x.exists(!_)) Some(false)
        else if (x.size == typeTree.children.size) Some(true)
        else None
    }
  }

  /** Returns whether the type is comparable. If none is returned, then comparability depends on the dynamic value. */
  def comparable(typ: Type)(reg: DefinedT => Type): Option[Boolean] =
    comparable(TypeHead.typeTree(typ))(reg)

}
