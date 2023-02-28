// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.theory.Addressability
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}


class AssignmentsEncoding extends Encoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def method(ctx: Context): in.Member ==> MemberWriter[vpr.Method] = {
    case x: in.Assignments => shAssignments(x)(ctx)
    //case x: in.Assignments if !x.ret.isInstanceOf[in.PointerT] => exAssignments(x)(ctx)
  }

  private def shAssignments(ass: in.Assignments)(ctx: Context): MemberWriter[vpr.Method] = {
    val (pos, info, errT) = ass.vprMeta
    val src = ass.info

    val name = ass.id.uniqueName

    val ret = in.LocalVar(ctx.freshNames.next(), ass.ret)(src)
    val vRetD = vpr.LocalVarDecl(ret.id, ctx.typ(ret.typ))(pos, info, errT)

    val thi = in.LocalVar(ass.args(0).id, ass.ret)(src)
    val vThiD = vpr.LocalVarDecl(thi.id, ctx.typ(thi.typ))(pos, info, errT)

    val arg = in.LocalVar(ass.args(1).id, ass.args(1).typ)(src)
    val vArgD = vpr.LocalVarDecl(arg.id, ctx.typ(arg.typ))(pos, info, errT)

    val vPres = sequence(ass.pres.map(ctx.precondition))
    val vPosts = sequence(ass.posts.map(ctx.postcondition))

    val body = for {
      stmt <- if (ass.body.isEmpty) { None } 
      else { 
        val b = ass.body.getOrElse(null)
        Some (block(cl.seqns(b match {
          case in.Block(_, stmts) => stmts.map(s => s match {
            //an assignment inside the assign declaration should not use itself
            case a@in.SingleAss(in.Assignee(lhs :: ctx.Struct(lhsFs) / Addressability.Shared), rhs :: ctx.Struct(rhsFs)) 
              if !ctx.lookupAssignments(lhs.typ).isEmpty => 
                for {
                  x <- cl.bind(lhs)(ctx)
                  y <- cl.bind(rhs)(ctx)
                  lhsFAs = lhsFs.map(f => in.FieldRef(x, f)(x.info)).map(in.Assignee.Field)
                  rhsFAs = rhsFs.map(f => in.FieldRef(y, f)(y.info))
                  res <- cl.seqns((lhsFAs zip rhsFAs).map { case (lhsFA, rhsFA) => ctx.assignment(lhsFA, rhsFA)(a) })
                } yield res
            case s => ctx.statement(s)
          })
          case _ => ???
      })))}
    } yield stmt

    for {
      pres <- vPres
      posts <- vPosts
      body <- option(body)

      method = vpr.Method(
        name = name,
        formalArgs = Seq(vThiD) ++ Seq(vArgD),
        formalReturns = Seq(vRetD),
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)
    } yield method
  } 

  private def exAssignments(ass: in.Assignments)(ctx: Context): MemberWriter[vpr.Method] = {
    val (pos, info, errT) = ass.vprMeta
    val src = ass.info

    val name = ass.id.uniqueName

    val ret = in.LocalVar(ctx.freshNames.next(), ass.ret)(src)
    val vRetD = vpr.LocalVarDecl(ret.id, ctx.typ(ret.typ))(pos, info, errT)

    val thi = in.LocalVar(ass.args(0).id, ass.ret)(src)
    val vThiD = vpr.LocalVarDecl(thi.id, ctx.typ(thi.typ))(pos, info, errT)

    val arg = in.LocalVar(ass.args(1).id, ass.args(1).typ)(src)
    val vArgD = vpr.LocalVarDecl(arg.id, ctx.typ(arg.typ))(pos, info, errT)

    val vPres = sequence(ass.pres.map(ctx.precondition))
    val vPosts = sequence(ass.posts.map(ctx.postcondition))

    val body = for {
      stmt <- if (ass.body.isEmpty) { None } 
      else { 
        val b = ass.body.getOrElse(null)
        Some (block(cl.seqns(b match {
          case in.Block(_, stmts) => stmts.map(s => s match {
            //an assignment inside the assign declaration should not use itself
            case a@in.SingleAss(in.Assignee(lhs :: ctx.Struct(lhsFs) / Addressability.Shared), rhs :: ctx.Struct(rhsFs)) 
              if !ctx.lookupAssignments(lhs.typ).isEmpty => 
                for {
                  x <- cl.bind(lhs)(ctx)
                  y <- cl.bind(rhs)(ctx)
                  lhsFAs = lhsFs.map(f => in.FieldRef(x, f)(x.info)).map(in.Assignee.Field)
                  rhsFAs = rhsFs.map(f => in.FieldRef(y, f)(y.info))
                  res <- cl.seqns((lhsFAs zip rhsFAs).map { case (lhsFA, rhsFA) => ctx.assignment(lhsFA, rhsFA)(a) })
                } yield res
            case s => ctx.statement(s)
          })
          case _ => ???
      })))}
    } yield stmt

    for {
      pres <- vPres
      posts <- vPosts
      body <- option(body)

      method = vpr.Method(
        name = name,
        formalArgs = Seq(vThiD) ++ Seq(vArgD),
        formalReturns = Seq(vRetD),
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)
    } yield method
  }
}
