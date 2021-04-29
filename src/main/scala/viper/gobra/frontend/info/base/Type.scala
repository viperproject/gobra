// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.base

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.util.Messaging.Messages
import viper.gobra.ast.frontend.{PDomainType, PImport, PInterfaceType, PNode, PStructType, PTypeDecl}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.util.TypeBounds

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

object Type {

  sealed trait Type

  abstract class PrettyType(pretty: => String) extends Type {
    override lazy val toString: String = pretty
  }

  sealed trait ContextualType extends Type {
    val context: ExternalTypeInfo
  }

  case object UnknownType extends PrettyType("unknown")

  case object VoidType extends PrettyType("void")

  case object NilType extends PrettyType("nil")

  case class DeclaredT(decl: PTypeDecl, context: ExternalTypeInfo) extends PrettyType(decl.left.name) with ContextualType

  case object BooleanT extends PrettyType("bool")

  case object StringT extends PrettyType("string")

  case class IntT(kind: TypeBounds.IntegerKind) extends PrettyType(kind.name)

  case class ArrayT(length: BigInt, elem: Type) extends PrettyType(s"[$length]$elem") {
    require(length >= 0, "The length of an array must be non-negative")
  }

  case class SliceT(elem: Type) extends PrettyType(s"[]$elem")

  case class VariadicT(elem: Type) extends PrettyType(s"...$elem")

  case class OptionT(elem : Type) extends PrettyType(s"option[$elem]")

  case class DomainT(decl: PDomainType, context: ExternalTypeInfo) extends PrettyType("domain{...}") with ContextualType

  case class MapT(key: Type, elem: Type) extends PrettyType(s"map[$key]$elem")

  case class PointerT(elem: Type) extends PrettyType(s"*$elem")

  case class ChannelT(elem: Type, mod: ChannelModus) extends PrettyType(s"$mod $elem")

  sealed abstract class ChannelModus(override val toString: String)

  object ChannelModus {

    case object Bi extends ChannelModus("chan")

    case object Recv extends ChannelModus("<-chan")

    case object Send extends ChannelModus("chan<-")
  }

  case class StructT(clauses: ListMap[String, (Boolean, Type)], decl: PStructType, context: ExternalTypeInfo) extends ContextualType {
    lazy val fields: ListMap[String, Type] = clauses.filter(isField).map(removeFieldIndicator)
    lazy val embedded: ListMap[String, Type] = clauses.filterNot(isField).map(removeFieldIndicator)
    private def isField(clause: (String, (Boolean, Type))): Boolean = clause._2._1
    private def removeFieldIndicator(clause: (String, (Boolean, Type))): (String, Type) = (clause._1, clause._2._2)

    override lazy val toString: String = {
      val fields = clauses.map{ case (n, (_, t)) => s"$n: $t" }
      s"struct{ ${fields.mkString("; ")}}"
    }
  }

  case class FunctionT(args: Vector[Type], result: Type) extends PrettyType(s"func(${args.mkString(",")}) $result")

  case class PredT(args: Vector[Type]) extends PrettyType(s"pred(${args.mkString(",")})")

  // TODO: at least add type info
  case class InterfaceT(decl: PInterfaceType, context: ExternalTypeInfo) extends ContextualType {

    lazy val isEmpty: Boolean = {
      decl.methSpecs.isEmpty && decl.predSpec.isEmpty &&
        decl.embedded.isEmpty
    }

    override lazy val toString: String = {
      if (isEmpty) "interface{}" else s"interface{ ${decl.methSpecs.map(_.id.name).mkString(", ")} }"
    }
  }


  case class InternalTupleT(ts: Vector[Type]) extends PrettyType(s"(${ts.mkString(",")})")

  case class InternalSingleMulti(sin: Type, mul: InternalTupleT) extends Type

  case class ImportT(decl: PImport) extends PrettyType(decl.formatted)

  case object SortT extends PrettyType("Type")

  sealed trait GhostType extends Type

  case object AssertionT extends PrettyType("assertion") with GhostType

  case class GhostSliceT(elem: Type) extends PrettyType(s"ghost []$elem") with GhostType

  sealed trait GhostCollectionType extends GhostType {
    def elem : Type
  }

  case class SequenceT(elem : Type) extends PrettyType(s"seq[$elem]") with GhostCollectionType

  sealed trait GhostUnorderedCollectionType extends GhostCollectionType

  case class SetT(elem : Type) extends PrettyType(s"set[$elem]") with GhostUnorderedCollectionType

  case class MultisetT(elem : Type) extends PrettyType(s"mset[$elem]") with GhostUnorderedCollectionType

  case class MathMapT(key : Type, elem : Type) extends PrettyType(s"mmap[$key]$elem") with GhostUnorderedCollectionType

  case object PermissionT extends PrettyType(s"perm") with GhostType

  @tailrec
  def source(t: Type): Option[(PNode, ExternalTypeInfo)] = {
    t match {
      case t: DeclaredT => Some((t.decl, t.context))
      case t: ArrayT => source(t.elem)
      case t: SliceT => source(t.elem)
      case t: VariadicT => source(t.elem)
      case t: OptionT => source(t.elem)
      case t: DomainT => Some((t.decl, t.context))
      case t: PointerT => source(t.elem)
      case t: ChannelT => source(t.elem)
      case t: StructT => Some((t.decl, t.context))
      case t: InterfaceT => Some((t.decl, t.context))
      case t: GhostSliceT => source(t)
      case t: GhostCollectionType => source(t)
      case _ => None
    }
  }


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
  case class AbstractType(messages: (PNode, Vector[Type]) => Messages, typing: Vector[Type] ==> FunctionT) extends PrettyType("abstract")
}
