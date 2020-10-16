// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.slices

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class SliceEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Slice(t) / m => m match {
      case Exclusive => ctx.option.typ(ctx.slice.typ(ctx.typeEncoding.typ(ctx)(t)))
      case Shared => vpr.Ref
    }
  }

  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case (exp : in.DfltVal) :: ctx.Slice(t) / m => {
        val (pos, info, errT) = exp.vprMeta
        m match {
          case Exclusive => unit(nilSlice(t)(ctx)(pos, info, errT))
          case Shared => unit(vpr.NullLit()(pos, info, errT))
        }
      }

      case (exp : in.NilLit) :: ctx.Slice(t) / Exclusive => {
        val (pos, info, errT) = exp.vprMeta
        unit(nilSlice(t)(ctx)(pos, info, errT))
      }

      case in.Length(exp :: ctx.Slice(typ) / Exclusive) => for {
        expT <- goE(exp)
        typT = ctx.typeEncoding.typ(ctx)(typ)
        (pos, info, errT) = exp.vprMeta
      } yield vpr.CondExp(
        vpr.EqCmp(expT, nilSlice(typT)(ctx)(pos, info, errT))(pos, info, errT),
        vpr.IntLit(0)(pos, info, errT),
        ctx.slice.len(ctx.option.get(expT, ctx.slice.typ(typT))(pos, info, errT))(pos, info, errT)
      )(pos, info, errT)

      case in.Capacity(exp :: ctx.Slice(typ) / Exclusive) => for {
        expT <- goE(exp)
        typT = ctx.typeEncoding.typ(ctx)(typ)
        (pos, info, errT) = exp.vprMeta
      } yield vpr.CondExp(
        vpr.EqCmp(expT, nilSlice(typT)(ctx)(pos, info, errT))(pos, info, errT),
        vpr.IntLit(0)(pos, info, errT),
        ctx.slice.cap(ctx.option.get(expT, ctx.slice.typ(typT))(pos, info, errT))(pos, info, errT)
      )(pos, info, errT)
    }
  }

  override def reference(ctx : Context) : in.Location ==> CodeWriter[vpr.Exp] = default(super.reference(ctx)) {
    case (exp @ in.IndexedExp(base :: ctx.Slice(typ), idx)) :: _ / Shared => for {
      baseT <- ctx.expr.translate(base)(ctx)
      idxT <- ctx.expr.translate(idx)(ctx)
      typT = ctx.typeEncoding.typ(ctx)(typ)
    } yield {
      val (pos, info, errT) = exp.vprMeta
      val getT = ctx.option.get(baseT, ctx.slice.typ(typT))(pos, info, errT)
      ctx.slice.loc(getT, idxT, typT)(pos, info, errT)
    }
  }


  private def nilSlice(typ : in.Type)(ctx : Context)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Exp =
    nilSlice(ctx.typeEncoding.typ(ctx)(typ))(ctx)(pos, info, errT)

  private def nilSlice(typ : vpr.Type)(ctx : Context)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Exp =
    ctx.option.none(ctx.slice.typ(typ))(pos, info, errT)
}
