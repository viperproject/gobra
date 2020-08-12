package viper.gobra.translator.interfaces.components

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context

trait TypeProperties {

  def underlyingType(typ: in.Type)(ctx: Context): in.Type

  def isStructType(typ: in.Type)(ctx: Context): Boolean = structType(typ)(ctx).nonEmpty

  def structType(typ: in.Type)(ctx: Context): Option[in.StructT]

  def isClassType(typ: in.Type)(ctx: Context): Boolean = classType(typ)(ctx).nonEmpty

  def classType(typ: in.Type)(ctx: Context): Option[in.StructT]

  def isStructPointerType(typ: in.Type)(ctx: Context): Boolean = structPointerType(typ)(ctx).nonEmpty

  def structPointerType(typ: in.Type)(ctx: Context): Option[in.StructT]

  def isPointerTyp(typ: in.Type)(ctx: Context): Boolean = pointerTyp(typ)(ctx).nonEmpty

  def pointerTyp(typ: in.Type)(ctx: Context): Option[in.Type]

  def isArrayType(typ : in.Type)(ctx : Context) : Boolean = arrayType(typ)(ctx).nonEmpty

  def arrayType(typ : in.Type)(ctx : Context) : Option[in.ArrayT]
}
