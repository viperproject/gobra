// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.ast.internal.theory

import viper.gobra.ast.internal._
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.util.RoseTree
import viper.gobra.util.TypeBounds.IntegerKind

/** The head of a type. Used to abstract over types. */
sealed trait TypeHead

object TypeHead {

  case object BoolHD extends TypeHead
  case object StringHD extends TypeHead
  case class IntHD(kind: IntegerKind) extends TypeHead
  case object PointerHD extends TypeHead
  case class DefinedHD(name: String) extends TypeHead
  /** 'fields' stores for each field the name and whether the field is ghost. */
  case class StructHD(fields: Vector[(String, Boolean)]) extends TypeHead
  case object ArrayHD extends TypeHead
  case object SliceHD extends TypeHead
  case object MapHD extends TypeHead
  case class InterfaceHD(name: String) extends TypeHead
  case class DomainHD(name: String) extends TypeHead
  case object ChannelHD extends TypeHead
  case object NilHD extends TypeHead
  case object UnitHD extends TypeHead
  case object PermHD extends TypeHead
  case object SortHD extends TypeHead
  case object SeqHD extends TypeHead
  case object SetHD extends TypeHead
  case object MSetHD extends TypeHead
  case object MathMapHD extends TypeHead
  case object OptionHD extends TypeHead
  case class TupleHD(arity: Int) extends TypeHead
  case class PredHD(arity: Int) extends TypeHead

  val emptyInterfaceHD: InterfaceHD = InterfaceHD(Names.emptyInterface)

  /** Returns a tree representation of the type. */
  def typeTree(typ: Type): RoseTree[TypeHead] =
    RoseTree(typeHead(typ), children(typ) map typeTree)

  /** Returns a tree representation of the type where the addressability information is preserved. */
  def typeTreeWithAddressability(typ: Type): RoseTree[(TypeHead, Addressability)] =
    RoseTree((typeHead(typ), typ.addressability), children(typ) map typeTreeWithAddressability)

  /** Returns type-head representation of the argument type. */
  def typeHead(typ: Type): TypeHead = typ match {
    case _: BoolT => BoolHD
    case _: StringT => StringHD
    case t: IntT => IntHD(t.kind)
    case _: PointerT => PointerHD
    case t: DefinedT => DefinedHD(t.name)
    case t: StructT => StructHD(t.fields.map(f => (f.name, f.ghost)))
    case t: InterfaceT => InterfaceHD(t.name)
    case t: DomainT => DomainHD(t.name)
    case _: ChannelT => ChannelHD
    case VoidT => UnitHD
    case _: PermissionT => PermHD
    case SortT => SortHD
    case _: ArrayT => ArrayHD
    case _: SliceT => SliceHD
    case _: MapT => MapHD
    case _: SequenceT => SeqHD
    case _: SetT => SetHD
    case _: MultisetT => MSetHD
    case _: MathMapT => MathMapHD
    case _: OptionT => OptionHD
    case t: TupleT => TupleHD(t.ts.size)
    case t: PredT => PredHD(t.args.size)
  }

  /** Returns the direct children types of a type. */
  def children(typ: Type): Vector[Type] = typ match {
    case _: BoolT => Vector.empty
    case _: StringT => Vector.empty
    case _: IntT => Vector.empty
    case t: PointerT => Vector(t.t)
    case _: DefinedT => Vector.empty
    case t: StructT => t.fields.map(_.typ)
    case _: InterfaceT => Vector.empty
    case _: DomainT => Vector.empty
    case t: ChannelT => Vector(t.elem)
    case VoidT => Vector.empty
    case _: PermissionT => Vector.empty
    case SortT => Vector.empty
    case t: ArrayT => Vector(t.elems)
    case t: SliceT => Vector(t.elems)
    case t: MapT => Vector(t.keys, t.values)
    case t: SequenceT => Vector(t.t)
    case t: SetT => Vector(t.t)
    case t: MultisetT => Vector(t.t)
    case t: MathMapT => Vector(t.keys, t.values)
    case t: OptionT => Vector(t.t)
    case t: TupleT => t.ts
    case t: PredT => t.args
  }

  /** Returns the direct children types of a type expression. */
  def children(typ: TypeExpr): Vector[Expr] = typ match {
    case _: BoolTExpr => Vector.empty
    case _: StringTExpr => Vector.empty
    case _: IntTExpr => Vector.empty
    case t: PointerTExpr => Vector(t.elems)
    case _: DefinedTExpr => Vector.empty
    case t: StructTExpr => t.fields.map(_._2)
    case t: ArrayTExpr => Vector(t.elems)
    case t: SliceTExpr => Vector(t.elems)
    case t: MapTExpr => Vector(t.keys, t.elems)
    case _: PermTExpr => Vector.empty
    case t: SequenceTExpr => Vector(t.elems)
    case t: SetTExpr => Vector(t.elems)
    case t: MultisetTExpr => Vector(t.elems)
    case t: MathMapTExpr => Vector(t.keys, t.elems)
    case t: OptionTExpr => Vector(t.elems)
    case t: TupleTExpr => t.elems
  }

  /** Returns type-head representation of the argument type expression. */
  def typeHead(typ: TypeExpr): TypeHead = typ match {
    case _: BoolTExpr => BoolHD
    case _: StringTExpr => StringHD
    case t: IntTExpr => IntHD(t.kind)
    case _: PointerTExpr => PointerHD
    case t: DefinedTExpr => DefinedHD(t.name)
    case t: StructTExpr => StructHD(t.fields.map(t => (t._1, t._3)))
    case _: ArrayTExpr => ArrayHD
    case _: SliceTExpr => SliceHD
    case _: MapTExpr => MapHD
    case _: PermTExpr => PermHD
    case _: SequenceTExpr => SeqHD
    case _: SetTExpr => SetHD
    case _: MultisetTExpr => MSetHD
    case _: MathMapTExpr => MathMapHD
    case _: OptionTExpr => OptionHD
    case t: TupleTExpr => TupleHD(t.elems.size)
  }

  /** Returns arity of the type represented by the argument type-head. */
  def arity(typeHead: TypeHead): Int = typeHead match {
    case BoolHD => 0
    case StringHD => 0
    case _: IntHD => 0
    case PointerHD => 1
    case _: DefinedHD => 0
    case t: StructHD => t.fields.size
    case ArrayHD => 1
    case SliceHD => 1
    case MapHD => 2
    case _: InterfaceHD => 0
    case _: DomainHD => 0
    case ChannelHD => 1
    case NilHD => 0
    case UnitHD => 0
    case PermHD => 0
    case SortHD => 0
    case SeqHD => 1
    case SetHD => 1
    case MSetHD => 1
    case MathMapHD => 2
    case OptionHD => 1
    case t: TupleHD => t.arity
    case t: PredHD => t.arity
  }


}
