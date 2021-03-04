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
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)){
      // the default value for Perm is NoPerm to be similar to the zero values for other literals
      case (e: in.DfltVal) :: ctx.Perm() / Exclusive => unit(withSrc(vpr.NoPerm(), e))
      case fp: in.FullPerm => unit(withSrc(vpr.FullPerm(), fp))
      case np: in.NoPerm => unit(withSrc(vpr.NoPerm(), np))
      case fp@ in.FractionalPerm(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.FractionalPerm(vl, vr), fp)
      case wp: in.WildcardPerm => unit(withSrc(vpr.WildcardPerm(), wp))
      case ep: in.EpsilonPerm => unit(withSrc(vpr.EpsilonPerm(), ep))
      case no@ in.NamedOpPerm(namedOp) => for { n <- goE(namedOp) } yield withSrc({ case args => n.withMeta(args) }, no) // TODO: make look better
    }
  }
}
