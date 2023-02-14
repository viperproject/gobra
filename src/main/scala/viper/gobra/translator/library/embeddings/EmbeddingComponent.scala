// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.embeddings

import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.library.Generator
import viper.gobra.translator.util.ViperUtil.synthesized
import viper.silver.{ast => vpr}
import viper.silver.plugin.standard.termination

trait EmbeddingParameter {
  def serialize: String
}

/**
  * Creates an embedded type E that is made up of all elements of 't' that satisfy 'p',
  * where 't' and 'p' are parametrized by an instance of P.
  */
trait EmbeddingComponent[P <: EmbeddingParameter] extends Generator {

  /** Returns the embedded type E. */
  def typ(id: P)(ctx: Context): vpr.Type

  /** Takes 'x' (element of 't'('id')), checks that 'x' satisfies 'p'('x', 'id'), and embeds 'x' into E */
  def box(x: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp

  /** Extracts 'y' (element of E) back into 't'('id'). The result satisfies 'p'(_, 'id'). */
  def unbox(y: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp
}


object EmbeddingComponent {

  class Impl[P <: EmbeddingParameter](p: (vpr.Exp, P) => Context => vpr.Exp, t: P => Context => vpr.Type) extends EmbeddingComponent[P] {

    override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
      generatedMember foreach addMemberFn
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
      * note that the function names of box and unbox depends not just on P but also the embedded type E (represented by `NT` below)
      *
      * domain N{}
      *
      * function boxNT(x: T): N
      *   requires p(x)
      *   ensures  unbox(result) == x
      *   decreases
      *
      * function unboxNT(y: N): T
      *   ensures p(result) && boxN(result) == y
      *   decreases
      *
      * */
    private def genTriple(id: P)(ctx: Context): Unit = {
      val domain = vpr.Domain(
        name = s"${Names.embeddingDomain}_${id.serialize}",
        functions = Seq.empty,
        axioms = Seq.empty,
        typVars = Seq.empty
      )()

      /** embedded type */
      val T = t(id)(ctx)
      val N = vpr.DomainType(domain = domain, typVarsMap = Map.empty)
      val x = vpr.LocalVarDecl("x", T)()
      val y = vpr.LocalVarDecl("y", N)()

      val boxName = s"${Names.embeddingBoxFunc}_${N.domainName}_${Names.serializeType(T)}"
      val unboxName = s"${Names.embeddingUnboxFunc}_${N.domainName}_${Names.serializeType(T)}"

      def boxApp(arg: vpr.Exp): vpr.FuncApp = {
        vpr.FuncApp(
          funcname = boxName,
          args = Seq(arg)
        )(vpr.NoPosition, vpr.NoInfo, typ = y.typ, vpr.NoTrafos)
      }

      def unboxApp(arg: vpr.Exp): vpr.FuncApp = {
        vpr.FuncApp(
          funcname = unboxName,
          args = Seq(arg)
        )(vpr.NoPosition, vpr.NoInfo, typ = x.typ, vpr.NoTrafos)
      }

      val box = vpr.Function(
        name = boxName,
        formalArgs = Seq(x),
        typ = N,
        pres = Seq(
          p(x.localVar, id)(ctx),
          synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")
        ),
        posts = Seq(vpr.EqCmp(unboxApp(vpr.Result(N)()), x.localVar)()),
        body = None
      )()

      val unbox = vpr.Function(
        name = unboxName,
        formalArgs = Seq(y),
        typ = T,
        pres = Seq(synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")),
        posts = Seq(p(vpr.Result(T)(), id)(ctx), vpr.EqCmp(boxApp(vpr.Result(T)()), y.localVar)()),
        body = None
      )()

      generatedMember ::= domain
      genDomainMap += (id -> domain)
      generatedMember ::= box
      genBoxFuncMap += (id -> box)
      generatedMember ::= unbox
      genUnboxFuncMap += (id -> unbox)
    }
  }
}
