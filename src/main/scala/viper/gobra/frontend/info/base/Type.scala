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

  case object IntT extends Type

  case class ArrayT(length: BigInt, elem: Type) extends Type {
    require(length >= 0, "The length of an array must be non-negative")
  }

  case class SliceT(elem: Type) extends Type

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