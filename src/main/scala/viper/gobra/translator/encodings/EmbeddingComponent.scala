// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.{ast => vpr}

/**
  * Creates an embedded type E that is made up of all elements of 't' that satisfy 'p',
  * where 't' and 'p' are parametrized by an instance of P.
  */
trait EmbeddingComponent[P] extends Generator {

  /** Returns the embedded type E. */
  def typ(id: P)(ctx: Context): vpr.Type

  /** Takes 'x' (element of 't'('id')), checks that 'x' satisfies 'p'('x', 'id'), and embeds 'x' into E */
  def box(x: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp

  /** Extracts 'y' (element of E) back into 't'('id'). The result satisfies 'p'(_, 'id'). */
  def unbox(y: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp
}


object EmbeddingComponent {

  class Impl[P](p: (vpr.Exp, P) => Context => vpr.Exp, t: P => Context => vpr.Type) extends EmbeddingComponent[P] {

    override def finalize(col: Collector): Unit = {
      generatedMember foreach col.addMember
    }

    /** Returns the embedded type E. */
    override def typ(id: P)(ctx: Context): vpr.Type = {
      vpr.DomainType(domain = getDomain(id)(ctx), typVarsMap = Map.empty)
    }

    /** Takes 'x' (element of 't'('id')), checks that 'x' satisfies 'p'('x', 'id'), and embeds 'x' into E */
    override def box(x: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp = {
      vpr.FuncApp(func = getBoxFunc(id)(ctx), args = Seq(x))(pos, info, errT)
    }

    /** Extracts 'y' (element of E) back into 't'('id'). The result satisfies 'p'(_, 'id'). */
    override def unbox(y: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp = {
      vpr.FuncApp(func = getUnboxFunc(id)(ctx), args = Seq(y))(pos, info, errT)
    }

    private def getDomain(x: P)(ctx: Context): vpr.Domain = {
      genDomainMap.getOrElse(x, {genTriple(x)(ctx); genDomainMap(x)})
    }

    private def getBoxFunc(x: P)(ctx: Context): vpr.Function = {
      genBoxFuncMap.getOrElse(x, {genTriple(x)(ctx); genBoxFuncMap(x)})
    }

    private def getUnboxFunc(x: P)(ctx: Context): vpr.Function = {
      genUnboxFuncMap.getOrElse(x, {genTriple(x)(ctx); genUnboxFuncMap(x)})
    }

    private var generatedMember: List[vpr.Member] = List.empty
    private var genDomainMap: Map[P, vpr.Domain] = Map.empty
    private var genBoxFuncMap: Map[P, vpr.Function] = Map.empty
    private var genUnboxFuncMap: Map[P, vpr.Function] = Map.empty

    /**
      * Generates domain, box function, and unbox function:
      *
      * domain N{}
      *
      * function boxN(x: T): N
      *   requires p(x)
      *   ensures  unbox(result) == x
      *
      * function unboxN(y: N): T
      *   ensures p(result) && boxN(result) == y
      *
      * */
    private def genTriple(id: P)(ctx: Context): Unit = {
      val domain = vpr.Domain(
        name = s"${Names.embeddingDomain}${Names.freshName}",
        functions = Seq.empty,
        axioms = Seq.empty,
        typVars = Seq.empty
      )()

      val N = vpr.DomainType(domain = domain, typVarsMap = Map.empty)
      val x = vpr.LocalVarDecl("x", t(id)(ctx))()
      val y = vpr.LocalVarDecl("y", N)()

      def boxApp(arg: vpr.Exp): vpr.FuncApp = {
        vpr.FuncApp(
          funcname = s"${Names.embeddingBoxFunc}_${N.domainName}",
          args = Seq(arg)
        )(vpr.NoPosition, vpr.NoInfo, typ = x.typ, vpr.NoTrafos)
      }

      def unboxApp(arg: vpr.Exp): vpr.FuncApp = {
        vpr.FuncApp(
          funcname = s"${Names.embeddingUnboxFunc}_${N.domainName}",
          args = Seq(arg)
        )(vpr.NoPosition, vpr.NoInfo, typ = x.typ, vpr.NoTrafos)
      }

      val box = vpr.Function(
        name = s"${Names.embeddingBoxFunc}_${N.domainName}",
        formalArgs = Seq(x),
        typ = N,
        pres = Seq(p(x.localVar, id)(ctx)),
        posts = Seq(vpr.EqCmp(unboxApp(vpr.Result(N)()), x.localVar)()),
        body = None
      )()

      val unbox = {
        val resT = t(id)(ctx)

        vpr.Function(
          name = s"${Names.embeddingUnboxFunc}_${N.domainName}",
          formalArgs = Seq(y),
          typ = resT,
          pres = Seq.empty,
          posts = Seq(p(vpr.Result(resT)(), id)(ctx), vpr.EqCmp(boxApp(vpr.Result(resT)()), y.localVar)()),
          body = None
        )()
      }

      generatedMember ::= domain
      genDomainMap += (id -> domain)
      generatedMember ::= box
      genBoxFuncMap += (id -> box)
      generatedMember ::= unbox
      genUnboxFuncMap += (id -> unbox)
    }
}




}