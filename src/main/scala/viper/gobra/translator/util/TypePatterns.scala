// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context

import scala.annotation.tailrec

/**
  * Patterns for pattern matching to facilitate checks on types of internal representation.
  */
object TypePatterns {

  /** Matches every expression and splits it into the expression and its type. */
  object :: {
    def unapply[T <: in.Expr](arg: T): Some[(T, in.Type)] = Some(arg, arg.typ)
  }

  /** Matches every type and splits it into the type and its addressability modifier. */
  object / {
    def unapply[T <: in.Type](arg: T): Some[(T, Addressability)] = Some(arg, arg.addressability)
  }

  /** Matches every shared type. */
  object Sh {
    def unapply(arg: in.Type): Boolean = arg.addressability.isShared
  }

  /** Matches every exclusive type. */
  object Ex {
    def unapply(arg: in.Type): Boolean = arg.addressability.isExclusive
  }

  /** One pattern for every type. The patterns disregard the addressability modifier. */
  implicit class ContextTypePattern(ctx: Context) {

    /**
      * Returns T for exclusive *T and shared T.
      * Both these types have the same memory layout, used to encode the address of a type.
      * */
    object Ref {
      def unapply(arg: in.Type): Option[in.Type] = {
        arg match {
          case ctx.*(t) / Exclusive => Some(t)
          case t / Shared => Some(t)
          case _ => None
        }
      }
    }

    object Bool {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx).isInstanceOf[in.BoolT]
    }

    object String {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx).isInstanceOf[in.StringT]
    }

    object Int {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx).isInstanceOf[in.IntT]
    }

    object Void {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx) == in.VoidT
    }

    object Perm {
      def unapply(arg: in.Type): Boolean =
        underlyingType(arg)(ctx).isInstanceOf[in.PermissionT]
    }

    object Array {
      def unapply(arg: in.Type): Option[(BigInt, in.Type)] = underlyingType(arg)(ctx) match {
        case t : in.ArrayT => Some(t.length, t.elems)
        case _ => None
      }
    }

    object Slice {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.SliceT => Some(t.elems)
        case _ => None
      }
    }

    object Map {
      def unapply(arg: in.Type): Option[(in.Type, in.Type)] = underlyingType(arg)(ctx) match {
        case t : in.MapT => Some((t.keys, t.values))
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

    object MathematicalMap {
      def unapply(arg: in.Type): Option[(in.Type, in.Type)] = underlyingType(arg)(ctx) match {
        case t : in.MathMapT => Some((t.keys, t.values))
        case _ => None
      }
    }

    object AnySet {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.MultisetT => Some(t.t)
        case t : in.SetT => Some(t.t)
        case _ => None
      }
    }

    object Option {
      def unapply(arg : in.Type) : Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.OptionT => Some(t.t)
        case _ => None
      }
    }

    object Pointer {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.PointerT => Some(t.t)
        case _ => None
      }
    }

    /** An alias pattern for pointer types. */
    object * {
      def unapply(arg: in.Type): Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.PointerT => Some(t.t)
        case _ => None
      }
    }

    object Struct {
      def unapply(arg: in.Type): Option[Vector[in.Field]] = underlyingType(arg)(ctx) match {
        case t : in.StructT => Some(t.fields)
        case _ => None
      }
    }

    object Interface {
      def unapply(arg: in.Type): Option[String] = underlyingType(arg)(ctx) match {
        case t : in.InterfaceT => Some(t.name)
        case _ => None
      }
    }

    object NotInterface {
      def unapply(arg: in.Type): Boolean = underlyingType(arg)(ctx) match {
        case _ : in.InterfaceT => false
        case _ => true
      }
    }

    object Domain {
      def unapply(arg: in.Type): Option[in.DomainT] = underlyingType(arg)(ctx) match {
        case t : in.DomainT => Some(t)
        case _ => None
      }
    }

    object Tuple {
      def unapply(arg: in.Type): Option[Vector[in.Type]] = underlyingType(arg)(ctx) match {
        case t : in.TupleT => Some(t.ts)
        case _ => None
      }
    }

    object Channel {
      def unapply(arg : in.Type) : Option[in.Type] = underlyingType(arg)(ctx) match {
        case t : in.ChannelT => Some(t.elem)
        case _ => None
      }
    }

    object Pred {
      def unapply(arg: in.Type): Option[Vector[in.Type]] = underlyingType(arg)(ctx) match {
        case t : in.PredT => Some(t.args)
        case _ => None
      }
    }
  }



  @tailrec
  /** Returns the underlying type of argument type. */
  def underlyingType(typ: in.Type)(ctx: Context): in.Type = typ match {
    case t: in.DefinedT => underlyingType(ctx.lookup(t))(ctx)
    case _ => typ
  }

  /** Returns the underlying struct type of the argument. Returns None, if the argument is not a struct type. */
  def structType(typ: in.Type)(ctx: Context): Option[in.StructT] = underlyingType(typ)(ctx) match {
    case st: in.StructT => Some(st)
    case _ => None
  }

  /** Returns the underlying pointer type of the argument. Returns None, if the argument is not a pointer type. */
  def pointerTyp(typ: in.Type)(ctx: Context): Option[in.Type] = underlyingType(typ)(ctx) match {
    case in.PointerT(t, _) => Some(t)
    case _ => None
  }

  /** Returns the underlying array type of the argument. Returns None, if the argument is not an array type. */
  def arrayType(typ : in.Type)(ctx : Context) : Option[in.ArrayT] = underlyingType(typ)(ctx) match {
    case t : in.ArrayT => Some(t)
    case _ => None
  }


}
