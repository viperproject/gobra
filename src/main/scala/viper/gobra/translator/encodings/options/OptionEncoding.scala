// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.options

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class OptionEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Option(t) / m =>  m match {
      case Exclusive => ctx.option.typ(ctx.typeEncoding.typ(ctx)(t))
      case Shared => vpr.Ref
    }
  }

  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    default(super.expr(ctx)) {
      case (exp : in.DfltVal) :: ctx.Option(t) / Exclusive =>
        unit(withSrc(ctx.option.none(ctx.typeEncoding.typ(ctx)(t)), exp))

      case exp @ in.OptionNone(typ) => {
        val (pos, info, errT) = exp.vprMeta
        val typT = ctx.typeEncoding.typ(ctx)(typ)
        unit(ctx.option.none(typT)(pos, info, errT))
      }

      case exp @ in.OptionSome(op) => for {
        opT <- ctx.expr.translate(op)(ctx)
        (pos, info, errT) = exp.vprMeta
      } yield ctx.option.some(opT)(pos, info, errT)

      case exp @ in.OptionGet(op :: ctx.Option(typ)) => for {
        opT <- ctx.expr.translate(op)(ctx)
        typT = ctx.typeEncoding.typ(ctx)(typ)
        (pos, info, errT) = exp.vprMeta
      } yield ctx.option.get(opT, typT)(pos, info, errT)

      case exp @ in.SequenceConversion(op :: ctx.Option(typ)) => for {
        opT <- ctx.expr.translate(op)(ctx)
        typT = ctx.typeEncoding.typ(ctx)(typ)
        (pos, info, errT) = exp.vprMeta
      } yield ctx.optionToSeq.create(opT, typT)(pos, info, errT)
    }
  }
}
