// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Methods
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.ast.Method
import viper.silver.{ast => vpr}

class MethodsImpl extends Methods {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = ()

  override def method(x: in.Method)(ctx: Context): MemberWriter[Method] = {
    val (pos, info, errT) = x.vprMeta

    val vRecv = ctx.typeEncoding.variable(ctx)(x.receiver)
    val vRecvPres = ctx.typeEncoding.precondition(ctx).lift(x.receiver).toVector

    val vArgs = x.args.map(ctx.typeEncoding.variable(ctx))
    val vArgPres = x.args.flatMap(ctx.typeEncoding.precondition(ctx).lift(_))

    val vResults = x.results.map(ctx.typeEncoding.variable(ctx))
    val vResultPosts = x.results.flatMap(ctx.typeEncoding.postcondition(ctx).lift(_))
    val vResultInit = cl.seqns(x.results map ctx.typeEncoding.initialization(ctx))

    for {
      pres <- sequence((vRecvPres ++ vArgPres) ++ x.pres.map(ctx.ass.precondition(_)(ctx)))
      posts <- sequence(vResultPosts ++ x.posts.map(ctx.ass.postcondition(_)(ctx)))
      measures <- sequence(x.terminationMeasures.map(ctx.measures.decreases(_)(ctx)))

      body <- option(x.body.map{ b => block{
        for {
          init <- vResultInit
          core <- methodBody(b)(ctx)
        } yield vu.seqn(Vector(init, core))(pos, info, errT)
      }})

      method = vpr.Method(
        name = x.name.uniqueName,
        formalArgs = vRecv +: vArgs,
        formalReturns = vResults,
        pres = pres ++ measures,
        posts = posts,
        body = body
      )(pos, info, errT)

    } yield method
  }


  override def function(x: in.Function)(ctx: Context): MemberWriter[Method] = {
    assert(x.info.origin.isDefined, s"$x has no defined source")

    val (pos, info, errT) = x.vprMeta

    val vArgs = x.args.map(ctx.typeEncoding.variable(ctx))
    val vArgPres = x.args.flatMap(ctx.typeEncoding.precondition(ctx).lift(_))

    val vResults = x.results.map(ctx.typeEncoding.variable(ctx))
    val vResultPosts = x.results.flatMap(ctx.typeEncoding.postcondition(ctx).lift(_))
    val vResultInit = cl.seqns(x.results map ctx.typeEncoding.initialization(ctx))

    for {
      pres <- sequence(vArgPres ++ x.pres.map(ctx.ass.precondition(_)(ctx)))
      posts <- sequence(vResultPosts ++ x.posts.map(ctx.ass.postcondition(_)(ctx)))
      measures <- sequence(x.terminationMeasures.map(ctx.measures.decreases(_)(ctx)))

      body <- option(x.body.map{ b => block{
        for {
          init <- vResultInit
          core <- methodBody(b)(ctx)
        } yield vu.seqn(Vector(init, core))(pos, info, errT)
      }})

      method = vpr.Method(
        name = x.name.name,
        formalArgs = vArgs,
        formalReturns = vResults,
        pres = pres ++ measures,
        posts = posts,
        body = body
      )(pos, info, errT)

    } yield method
  }

  def methodBody(x: in.MethodBody)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val (pos, info, errT) = x.vprMeta

    val returnLabel = vpr.Label(Names.returnLabel, Vector.empty)(pos, info, errT)

    cl.block{
      for {
        _ <- cl.global(returnLabel)
        _ <- cl.global(x.decls.map(blockDecl(_)(ctx)): _*)
        vBody <- cl.sequence(x.stmts map (ctx.stmt.translate(_)(ctx)))
        vPostprocessing <- cl.sequence(x.postprocessing map (ctx.stmt.translate(_)(ctx)))
      } yield vu.seqn(vBody ++ Vector(returnLabel) ++ vPostprocessing)(pos, info, errT)
    }
  }

  def blockDecl(x: in.BlockDeclaration)(ctx: Context): vpr.Declaration = {
    x match {
      case x: in.BodyVar => ctx.typeEncoding.variable(ctx)(x)
      case l: in.LabelProxy =>
        val (pos, info, errT) = x.vprMeta
        vpr.Label(l.name, Seq.empty)(pos, info, errT)
    }
  }
}
