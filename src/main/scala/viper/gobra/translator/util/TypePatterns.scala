package viper.gobra.translator.util

import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.translator.interfaces.Context

import scala.annotation.tailrec

object TypePatterns {

  object :: {
    def unapply(arg: in.Expr): Option[(in.Expr, in.Type)] = Some(arg, arg.typ)
  }

  object / {
    def unapply(arg: in.Type): Option[(in.Type, Addressability)] = Some(arg, arg.addressability)
  }

  object Sh {
    def unapply(arg: in.Type): Boolean = arg.addressability.isShared
  }

  object Ex {
    def unapply(arg: in.Type): Boolean = arg.addressability.isExclusive
  }

  implicit class ContextTypePattern(ctx: Context) {

    object Bool {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx).isInstanceOf[in.BoolT]
    }

    object Int {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx).isInstanceOf[in.IntT]
    }

    object Void {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx) == in.VoidT
    }

    object Nil {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx) == in.NilT
    }

    object Perm {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx).isInstanceOf[in.PermissionT]
    }

    object Array {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.ArrayT => Some(t.elems)
        case _ => None
      }
    }

    object Seq {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.SequenceT => Some(t.t)
        case _ => None
      }
    }

    object Set {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.SetT => Some(t.t)
        case _ => None
      }
    }

    object Multiset {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.MultisetT => Some(t.t)
        case _ => None
      }
    }

    object * {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.PointerT => Some(t.t)
        case _ => None
      }
    }

    object Pointer {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.PointerT => Some(t.t)
        case _ => None
      }
    }

    object Struct {
      def unapply(arg: in.Type): Option[Seq[in.Field]] = underlyingType(arg)(ctx) match {
        case t : in.StructT => Some(t.fields)
        case _ => None
      }
    }

    object Tuple {
      def unapply(arg: in.Type): Option[Seq[in.Type]] = underlyingType(arg)(ctx) match {
        case t : in.TupleT => Some(t.ts)
        case _ => None
      }
    }

  }



  @tailrec
  def underlyingType(typ: in.Type)(ctx: Context): in.Type = typ match {
    case t: in.DefinedT => underlyingType(ctx.lookup(t))(ctx)
    case _ => typ
  }

  def structType(typ: in.Type)(ctx: Context): Option[in.StructT] = underlyingType(typ)(ctx) match {
    case st: in.StructT => Some(st)
    case _ => None
  }

  def pointerTyp(typ: in.Type)(ctx: Context): Option[in.Type] = underlyingType(typ)(ctx) match {
    case in.PointerT(t, _) => Some(t)
    case _ => None
  }

  def arrayType(typ : in.Type)(ctx : Context) : Option[in.ArrayT] = underlyingType(typ)(ctx) match {
    case t : in.ArrayT => Some(t)
    case _ => None
  }

//  def classType(typ: in.Type)(ctx: Context): Option[in.StructT] = {
//
//    def afterAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
//      case st: in.StructT => Some(st)
//      case _ => None
//    }
//    def beforeAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
//      case in.PointerT(et, _) => afterAtMostOneRef(et)
//      case _ => afterAtMostOneRef(typ)
//    }
//    beforeAtMostOneRef(typ)
//  }
//
//  def structPointerType(typ: in.Type)(ctx: Context): Option[in.StructT] = {
//    def afterAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
//      case st: in.StructT => Some(st)
//      case _ => None
//    }
//    def beforeAtMostOneRef(typ: in.Type): Option[in.StructT] = underlyingType(typ)(ctx) match {
//      case in.PointerT(et, _) => afterAtMostOneRef(et)
//      case _ => None
//    }
//    beforeAtMostOneRef(typ)
//  }


}
