// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class PermissionEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Perm() / m =>
      m match {
        case Exclusive => vpr.Perm
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    default(super.expression(ctx)){
      // the default value for Perm is NoPerm to be similar to the zero values for other literals
      case (e: in.DfltVal) :: ctx.Perm() / Exclusive => unit(withSrc(vpr.NoPerm(), e))
      case (e: in.PermLit) :: ctx.Perm() / Exclusive =>
        val dividend = withSrc(vpr.IntLit(e.dividend), e)
        val divisor  = withSrc(vpr.IntLit(e.divisor),  e)
        unit(withSrc(vpr.FractionalPerm(dividend, divisor), e))
      case fp: in.FullPerm => unit(withSrc(vpr.FullPerm(), fp))
      case np: in.NoPerm => unit(withSrc(vpr.NoPerm(), np))
      case wp: in.WildcardPerm => unit(withSrc(vpr.WildcardPerm(), wp))
      case cp: in.CurrentPerm =>
        val (pos, info, errT) = cp.vprMeta
        for {
          arg <- ctx.assertion(in.Access(cp.acc, in.FullPerm(cp.info))(cp.info))
          pap = arg.asInstanceOf[vpr.AccessPredicate]
          res = vpr.CurrentPerm(pap.loc)(pos, info, errT)
        } yield res
      case pm@ in.PermMinus(exp) => for { e <- goE(exp) } yield withSrc(vpr.PermMinus(e), pm)
      case fp@ in.FractionalPerm(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FractionalPerm(vl, vr), fp)
      case pa@ in.PermAdd(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.PermAdd(vl, vr), pa)
      case ps@ in.PermSub(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.PermSub(vl, vr), ps)
      case pm@ in.PermMul(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.PermMul(vl, vr), pm)
      case pd@ in.PermDiv(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.PermDiv(vl, vr), pd)

      // Perm comparisons
      case lt@in.PermLtCmp(l, r) => for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.PermLtCmp(vl, vr), lt)
      case le@in.PermLeCmp(l, r) => for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.PermLeCmp(vl, vr), le)
      case gt@in.PermGtCmp(l, r) => for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.PermGtCmp(vl, vr), gt)
      case ge@in.PermGeCmp(l, r) => for { vl <- goE(l); vr <- goE(r) } yield withSrc(vpr.PermGeCmp(vl, vr), ge)

    }
  }
}
