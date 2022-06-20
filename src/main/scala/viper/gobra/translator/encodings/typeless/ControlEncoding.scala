// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}

class ControlEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case n@ in.Block(decls, stmts) =>
      val (pos, info, errT) = n.vprMeta
      val vDecls = decls map (blockDecl(_)(ctx))
      block{
        for {
          _ <- global(vDecls: _*)
          vBody <- sequence(stmts map ctx.statement)
        } yield vu.seqn(vBody)(pos, info, errT)
      }

    case in.Seqn(stmts) => seqns(stmts map ctx.statement)

    case n@ in.If(cond, thn, els) =>
      val (pos, info, errT) = n.vprMeta
      for {
        c <- ctx.expression(cond)
        t <- ctx.statement(thn)
        e <- ctx.statement(els)
      } yield vpr.If(c, vu.toSeq(t), vu.toSeq(e))(pos, info, errT)

    case n@ in.While(cond, invs, terminationMeasure, body) =>
      val (pos, info, errT) = n.vprMeta
      for {
        (cws, vCond) <- split(ctx.expression(cond))
        (iws, vInvs) = invs.map(ctx.invariant).unzip
        cpre <- seqnUnit(cws)
        ipre <- seqnUnits(iws)

        vBody <- ctx.statement(body)

        cpost = vpr.If(vCond, vu.toSeq(cpre), vu.nop(pos, info, errT))(pos, info, errT)
        ipost = ipre

        measure <- option(terminationMeasure map ctx.assertion)

        wh = vu.seqn(Vector(
          cpre, ipre, vpr.While(vCond, vInvs ++ measure, vu.seqn(Vector(vBody, cpost, ipost))(pos, info, errT))(pos, info, errT)
        ))(pos, info, errT)
      } yield wh

    case n@ in.Label(id) =>
      val (pos, info, errT) = n.vprMeta
      unit(vpr.Label(id.name, Seq.empty)(pos, info, errT))

    case n@ in.Continue(_, escLabel) =>
      val (pos, info, errT) = n.vprMeta
      unit(vpr.Goto(escLabel)(pos, info, errT))

    case n@ in.Break(_, escLabel) =>
      val (pos, info, errT) = n.vprMeta
      unit(vpr.Goto(escLabel)(pos, info, errT))

    case n@ in.Return() =>
      val (pos, info, errT) = n.vprMeta
      unit(vpr.Goto(Names.returnLabel)(pos, info, errT))
  }

  def blockDecl(x: in.BlockDeclaration)(ctx: Context): vpr.Declaration = {
    x match {
      case x: in.BodyVar => ctx.variable(x)
      case l: in.LabelProxy =>
        val (pos, info, errT) = x.vprMeta
        vpr.Label(l.name, Seq.empty)(pos, info, errT)
    }
  }
}
