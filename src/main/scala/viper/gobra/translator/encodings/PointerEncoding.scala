// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class PointerEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    *
    * Type[(*T)°] -> Type[T]
    * Type[(*T)@] -> Ref
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.*(t) / m =>
      m match {
        case Exclusive => ctx.typeEncoding.typ(ctx)(t)
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes the comparison of two expressions.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    * An encoding for type T should be defined at left-hand sides of type T and exclusive *T.
    * (Except the encoding of pointer types, which is not defined at exclusive *T to avoid a conflict).
    *
    * [lhs: *T@ == rhs: *T] -> [lhs] == [rhs]
    * [lhs: **T° == rhs: **T] -> [lhs] == [rhs]
    */
  override def equal(ctx: Context): (in.Expr, in.Expr, in.Node) ==> CodeWriter[vpr.Exp] = {
    case (lhs :: ctx.*(_) / Shared, rhs :: ctx.*(_), src) => super.equal(ctx)(lhs, rhs, src)
    case (lhs :: ctx.*(ctx.*(_)) / Exclusive, rhs :: ctx.*(ctx.*(_)), src) => super.equal(ctx)(lhs, rhs, src)
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    *
    * [dflt(*T°)] -> [dflt(T@)]
    * [nil: *T°] -> [dflt(T@)]
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expr(ctx)){
    case (dflt: in.DfltVal) :: ctx.*(t) / Exclusive =>
      ctx.expr.translate(in.DfltVal(t)(dflt.info))(ctx)

    case (lit: in.NilLit) :: ctx.*(t) =>
      ctx.expr.translate(in.DfltVal(t)(lit.info))(ctx)
  }

  /**
    * Encodes the reference of an expression.
    *
    * To avoid conflicts with other encodings, an encoding for type T should be defined at shared operations on type T.
    * Super implements shared variables with [[variable]].
    *
    * Ref[*e] -> [e]
    */
  override def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = default(super.reference(ctx)){
    case (loc: in.Deref) :: _ / Shared =>
      ctx.expr.translate(loc.exp)(ctx)
  }
}
