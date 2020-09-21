// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{DomainGenerator, FunctionGenerator}
import viper.silver.{ast, ast => vpr}

/**
  * Creates an embedded type E that is made up of all elements of 't' that satisfy 'p',
  * where 't' and 'p' are parametrized by an instance of P.
  */
abstract class EmbeddingComponent[P](p: (vpr.Exp, P) => Context => vpr.Exp, t: P => Context => vpr.Type) extends Generator {

  /** Returns the embedded type E. */
  abstract def typ(id: P)(ctx: Context): vpr.Type

  /** Takes 'x' (element of 't'('id')), checks that 'x' satisfies 'p'('x', 'id'), and embeds 'x' into E */
  abstract def box(x: vpr.Exp, id: P)(ctx: Context): vpr.FuncApp

  /** Extracts 'y' (element of E) back into 't'('id'). The result satisfies 'p'(_, 'id'). */
  abstract def unbox(y: vpr.Exp, id: P)(ctx: Context): vpr.FuncApp
}


object EmbeddingComponent {

  class Impl[P](p: (vpr.Exp, P) => Context => vpr.Exp, t: P => Context => vpr.Type) extends EmbeddingComponent[P](p, t) {

    override def finalize(col: Collector): Unit = {
      domainType.finalize(col)
      boxFunc.finalize(col)
      unboxFunc.finalize(col)
    }

    /** Returns the embedded type E. */
    override def typ(id: P)(ctx: Context): vpr.Type = {
      domainType(Vector.empty, id)(ctx)
    }

    /** Takes 'x' (element of 't'('id')), checks that 'x' satisfies 'p'('x', 'id'), and embeds 'x' into E */
    override def box(x: vpr.Exp, id: P)(ctx: Context): vpr.FuncApp = {
      boxFunc(Vector(x), id)()(ctx)
    }

    /** Extracts 'y' (element of E) back into 't'('id'). The result satisfies 'p'(_, 'id'). */
    override def unbox(y: vpr.Exp, id: P)(ctx: Context): vpr.FuncApp = {
      unboxFunc(Vector(y), id)()(ctx)
    }

    /** Generates: domain N{} */
    private val domainType: DomainGenerator[P] = new DomainGenerator[P] {
      override def genDomain(x: P)(ctx: Context): vpr.Domain = {
        vpr.Domain(
          name = s"${Names.embeddingDomain}${Names.freshName}",
          functions = Seq.empty,
          axioms = Seq.empty,
          typVars = Seq.empty
        )()
      }
    }

    /**
      * Generates:
      * function boxN(x: T): N
      *   requires p(x)
      *   ensures  unbox(result) == x
      */
    private val boxFunc: FunctionGenerator[P] = new FunctionGenerator[P]{
      override def genFunction(id: P)(ctx: Context): ast.Function = {
        val x = vpr.LocalVarDecl("x", t(id)(ctx))()
        val N = domainType(Vector.empty, id)(ctx)


        vpr.Function(
          name = s"${Names.embeddingBoxFunc}_${N.domainName}",
          formalArgs = Seq(x),
          typ = N,
          pres = Seq(p(x.localVar, id)(ctx)),
          posts = Seq(vpr.EqCmp(unboxFunc(Vector(vpr.Result(N)()), id)()(ctx), x.localVar)()),
          body = None
        )()
      }
    }

    /**
      * Generates:
      * function unboxN(y: N): T
      *   ensures p(result)
      */
    private val unboxFunc: FunctionGenerator[P] = new FunctionGenerator[P]{
      override def genFunction(id: P)(ctx: Context): ast.Function = {
        val N = domainType(Vector.empty, id)(ctx)
        val y = vpr.LocalVarDecl("y", t(id)(ctx))()

        vpr.Function(
          name = s"${Names.embeddingUnboxFunc}_${N.domainName}",
          formalArgs = Seq(y),
          typ = N,
          pres = Seq.empty,
          posts = Seq(p(y.localVar, id)(ctx)),
          body = None
        )()
      }
    }

}




}