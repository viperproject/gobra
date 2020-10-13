// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import viper.gobra.ast.frontend.{PImport, PInterfaceType, PStructType, PTypeDecl}
import viper.gobra.frontend.info.ExternalTypeInfo

import scala.collection.immutable.ListMap

object Type {

  sealed trait Type

  sealed trait ContextualType extends Type {
    val context: ExternalTypeInfo
  }

  case object UnknownType extends Type

  case object VoidType extends Type

  case object NilType extends Type

  case class DeclaredT(decl: PTypeDecl, context: ExternalTypeInfo) extends ContextualType

  case object BooleanT extends Type

  case class IntT(kind: IntegerKind) extends Type

  sealed trait IntegerKind
  case object UntypedConst extends IntegerKind
  sealed abstract class BoundedIntegerKind(val lower: BigInt, val upper: BigInt) extends IntegerKind
  case object Int extends BoundedIntegerKind(-2147483648, 2147483647) // TODO: allow for changes according to the int size of the machine arch (either 32 or 64 bit)
  case object Int8 extends BoundedIntegerKind(-128, 127)
  case object Int16 extends BoundedIntegerKind(-32768, 32767)
  sealed abstract class IntWith32bit extends BoundedIntegerKind(-2147483648, 2147483647)
  case object Int32 extends IntWith32bit
  case object Rune extends IntWith32bit
  case object Int64 extends BoundedIntegerKind(-9223372036854775808L, 9223372036854775807L)
  case object UInt extends BoundedIntegerKind(0, 4294967295L) // TODO: allow for changes according to the int size of the machine arch (either 32 or 64 bit)
  sealed abstract class UIntWith8bit extends BoundedIntegerKind(0, 255)
  case object UInt8 extends UIntWith8bit
  case object Byte extends UIntWith8bit
  case object UInt16 extends BoundedIntegerKind(0, 65535)
  case object UInt32 extends BoundedIntegerKind(0, 4294967295L)
  case object UInt64 extends BoundedIntegerKind(0, BigInt("18446744073709551615"))
  case object UIntPtr extends BoundedIntegerKind(0, BigInt("18446744073709551615")) //TODO: change according to spec

  case class ArrayT(length: BigInt, elem: Type) extends Type {
    require(length >= 0, "The length of an array must be non-negative")
  }

  case class SliceT(elem: Type) extends Type

  case class OptionT(elem : Type) extends Type

  case class MapT(key: Type, elem: Type) extends Type

  case class PointerT(elem: Type) extends Type

  case class ChannelT(elem: Type, mod: ChannelModus) extends Type

  sealed trait ChannelModus

  object ChannelModus {

    case object Bi extends ChannelModus

    case object Recv extends ChannelModus

    case object Send extends ChannelModus

  }

  case class StructT(clauses: ListMap[String, (Boolean, Type)], decl: PStructType, context: ExternalTypeInfo) extends ContextualType {
    lazy val fields: ListMap[String, Type] = clauses.filter(isField).map(removeFieldIndicator)
    lazy val embedded: ListMap[String, Type] = clauses.filterNot(isField).map(removeFieldIndicator)
    private def isField(clause: (String, (Boolean, Type))): Boolean = clause._2._1
    private def removeFieldIndicator(clause: (String, (Boolean, Type))): (String, Type) = (clause._1, clause._2._2)
  }

  case class FunctionT(args: Vector[Type], result: Type) extends Type

  // TODO: at least add type info
  case class InterfaceT(decl: PInterfaceType) extends Type


  case class InternalTupleT(ts: Vector[Type]) extends Type

  case class InternalSingleMulti(sin: Type, mul: InternalTupleT) extends Type

  case class ImportT(decl: PImport) extends Type



  sealed trait GhostType extends Type

  case object AssertionT extends GhostType

  sealed trait GhostCollectionType extends GhostType {
    def elem : Type
  }

  case class SequenceT(elem : Type) extends GhostCollectionType

  sealed trait GhostUnorderedCollectionType extends GhostCollectionType

  case class SetT(elem : Type) extends GhostUnorderedCollectionType

  case class MultisetT(elem : Type) extends GhostUnorderedCollectionType


  /**
    * Type Contexts
    */

  sealed trait TypeContext {
    def unapply(arg: Type): Option[Type]
  }

  case object Argument extends TypeContext {
    override def unapply(arg: Type): Option[Type] = arg match {
      case t: InternalSingleMulti => Some(t.sin)
      case UnknownType => None
      case t => Some(t)
    }
  }

  case object Assign extends TypeContext {
    override def unapply(arg: Type): Option[Type] = arg match {
      case t: InternalSingleMulti => Some(t.mul)
      case UnknownType => None
      case t => Some(t)
    }
  }

  case object Single extends TypeContext {
    override def unapply(arg: Type): Option[Type] = arg match {
      case InternalSingleMulti(sin, _) => Some(sin)
      case _: InternalTupleT => None
      case UnknownType => None
      case t => Some(t)
    }
  }
}