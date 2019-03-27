package viper.gobra.frontend.info.base

import viper.gobra.ast.frontend.{PInterfaceType, PStructType, PTypeDecl}

object Type {

  sealed trait Type

  case object UnknownType extends Type

  case object VoidType extends Type

  case object NilType extends Type

  case class DeclaredT(decl: PTypeDecl) extends Type

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

  case class StructT(decl: PStructType) extends Type

  case class FunctionT(args: Vector[Type], result: Type) extends Type

  case class InterfaceT(decl: PInterfaceType) extends Type


  case class InternalTupleT(ts: Vector[Type]) extends Type

  case class InternalSingleMulti(sin: Type, mul: InternalTupleT) extends Type


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