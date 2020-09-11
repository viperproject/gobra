package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.components.TypeProperties
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context

import scala.annotation.tailrec

class TypePropertiesImpl extends TypeProperties {

  @tailrec
  override final def underlyingType(typ: in.Type)(ctx: Context): in.Type = typ match {
    case t: in.DefinedT => underlyingType(ctx.lookup(t))(ctx)
    case _ => typ
  }

  override def structType(typ: in.Type)(ctx: Context): Option[in.StructT] = underlyingType(typ)(ctx) match {
    case st: in.StructT => Some(st)
    case _ => None
  }

  override def classType(typ: in.Type)(ctx: Context): Option[in.StructT] = {

    def afterAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
      case st: in.StructT => Some(st)
      case _ => None
    }
    def beforeAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
      case in.PointerT(et, _) => afterAtMostOneRef(et)
      case _ => afterAtMostOneRef(typ)
    }
    beforeAtMostOneRef(typ)
  }

  override def structPointerType(typ: in.Type)(ctx: Context): Option[in.StructT] = {
    def afterAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
      case st: in.StructT => Some(st)
      case _ => None
    }
    def beforeAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
      case in.PointerT(et, _) => afterAtMostOneRef(et)
      case _ => None
    }
    beforeAtMostOneRef(typ)
  }

  override def pointerTyp(typ: in.Type)(ctx: Context): Option[in.Type] = underlyingType(typ)(ctx) match {
    case in.PointerT(t, _) => Some(t)
    case _ => None
  }

  override def arrayType(typ : in.Type)(ctx : Context) : Option[in.ArrayT] = underlyingType(typ)(ctx) match {
    case t : in.ArrayT => Some(t)
    case _ => None
  }
}
