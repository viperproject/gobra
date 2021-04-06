// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.{ast => vpr}

class DomainEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Domain(domain) / m =>
      m match {
        case Exclusive => domainType(domain.name)
        case Shared    => vpr.Ref
      }
  }

  private def domainType(domainName: String): vpr.DomainType = vpr.DomainType(domainName, Map.empty)(Seq.empty)

  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    case domain: in.DomainDefinition =>
      val domainName = domain.name
      val (dPos, dInfo, dErrT) = domain.vprMeta

      val funcs = domain.funcs map { f =>
        val (fPos, fInfo, fErrT) = f.vprMeta
        val formalArgs = f.args map ctx.typeEncoding.variable(ctx)
        val resType = ctx.typeEncoding.typ(ctx)(f.results.typ)
        vpr.DomainFunc(f.name.name, formalArgs, resType)(fPos, fInfo, domainName, fErrT)
      }

      val defaultFunc = vpr.DomainFunc(
        Names.dfltDomainValue(domainName),
        Seq.empty,
        domainType(domainName)
      )(dPos, dInfo, domainName, dErrT)

      val axioms = ml.sequence(domain.axioms map { ax =>
        val (axPos, axInfo, axErrT) = ax.vprMeta
        val body = ml.pure(ctx.expr.translate(ax.expr)(ctx))(ctx)
        body.map(exp => vpr.AnonymousDomainAxiom(exp)(axPos, axInfo, domainName, axErrT))
      })


      axioms.map(axs =>
        Vector(
          vpr.Domain(domainName, funcs :+ defaultFunc, axs)(dPos, dInfo, dErrT): vpr.Member
        )
      )
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case (e: in.DfltVal) :: ctx.Domain(d) / Exclusive =>
        val (pos, info, errT) = e.vprMeta
        unit(
          vpr.DomainFuncApp(
            funcname = Names.dfltDomainValue(d.name),
            Seq.empty,
            Map.empty
          )(pos, info, domainType(d.name), d.name, errT): vpr.Exp
        )

      case fc: in.DomainFunctionCall =>
        val (pos, info, errT) = fc.vprMeta
        for {
          args <- sequence(fc.args map goE)
          resType = ctx.typeEncoding.typ(ctx)(fc.typ)
        } yield vpr.DomainFuncApp(
          funcname = fc.func.name,
          args,
          Map.empty
        )(pos, info, resType, fc.func.domainName, errT): vpr.Exp
    }
  }
}
