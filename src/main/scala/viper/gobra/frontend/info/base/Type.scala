// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend.{PAdtClause, PAdtType, PImport, PInterfaceType, PNode, PStructType, PTypeDecl}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.util.TypeBounds

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

  case object StringT extends Type

  case class IntT(kind: TypeBounds.IntegerKind) extends Type

  case class ArrayT(length: BigInt, elem: Type) extends Type {
    require(length >= 0, "The length of an array must be non-negative")
  }

  case class SliceT(elem: Type) extends Type

  case class VariadicT(elem: Type) extends Type

  case class AdtT(decl: PAdtType, context: ExternalTypeInfo) extends Type

  case class AdtClauseT(clauses: ListMap[String, Type], decl: PAdtClause, context: ExternalTypeInfo) extends Type

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

  case class PredT(args: Vector[Type]) extends Type

  // TODO: at least add type info
  case class InterfaceT(decl: PInterfaceType, context: ExternalTypeInfo) extends Type {

    lazy val isEmpty: Boolean = {
      decl.methSpecs.isEmpty && decl.predSpec.isEmpty &&
        decl.embedded.isEmpty
    }
  }


  case class InternalTupleT(ts: Vector[Type]) extends Type

  case class InternalSingleMulti(sin: Type, mul: InternalTupleT) extends Type

  case class ImportT(decl: PImport) extends Type

  case object SortT extends Type

  sealed trait GhostType extends Type

  case object AssertionT extends GhostType

  sealed trait GhostCollectionType extends GhostType {
    def elem : Type
  }

  case class SequenceT(elem : Type) extends GhostCollectionType

  sealed trait GhostUnorderedCollectionType extends GhostCollectionType

  case class SetT(elem : Type) extends GhostUnorderedCollectionType

  case class MultisetT(elem : Type) extends GhostUnorderedCollectionType

  case object PermissionT extends GhostType


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

  /**
    * Parameteric type for built-in members.
    * `messages` maps from types of arguments to error messages.
    * The partial function `typing` maps from types of arguments to the return type.
    * It should be defined for all argument types for which no error message was returned by `messages`.
    * Note that Vector[Type] represents the types of arguments for functions and fpredicates but is a singleton
    * vector storing the receiver's type for methods and mpredicates.
    */
  case class AbstractType(messages: (PNode, Vector[Type]) => Messages, typing: Vector[Type] ==> FunctionT) extends Type
}
