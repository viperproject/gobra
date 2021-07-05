// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.
package viper.gobra.translator.encodings.arrays
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.arrays.ArrayEncoding.ComponentParameter
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.silver.ast.{DomainType, NoInfo, NoPosition, NoTrafos}
import viper.silver.{ast => vpr}

/**
  * Creates an embedded type E that is made up of all elements of 't' that satisfy 'p',
  * where 't' and 'p' are parametrized by an instance of P.
  */

trait ExclusiveArrayEmbeddingComponent[P] extends Generator {

  /** Returns the embedded type E. */
  def typ(id: P)(ctx: Context): vpr.Type

  /** Takes 'x' (element of 't'('id')), checks that 'x' satisfies 'p'('x', 'id'), and embeds 'x' into E */
  def box(x: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp

  /** Extracts 'y' (element of E) back into 't'('id'). The result satisfies 'p'(_, 'id'). */
  def unbox(y: vpr.Exp, id: P)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp
}


object ExclusiveArrayEmbeddingComponent {

    class Impl(p: (vpr.Exp, ComponentParameter) => Context => vpr.Exp, t: ComponentParameter => Context => vpr.Type) extends ExclusiveArrayEmbeddingComponent[ComponentParameter] {

      override def finalize(col: Collector): Unit = {
        generatedMember foreach col.addMember
      }

      /** Returns the embedded type E. */
      override def typ(id: ComponentParameter)(ctx: Context): vpr.Type = {
        vpr.DomainType(domain = getDomain(id)(ctx), typVarsMap = Map.empty)
      }

      /** Takes 'x' (element of 't'('id')), checks that 'x' satisfies 'p'('x', 'id'), and embeds 'x' into E */
      override def box(x: vpr.Exp, id: ComponentParameter)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp = {
        vpr.FuncApp(func = getBoxFunc(id)(ctx), args = Seq(x))(pos, info, errT)
      }

      /** Extracts 'y' (element of E) back into 't'('id'). The result satisfies 'p'(_, 'id'). */
      override def unbox(y: vpr.Exp, id: ComponentParameter)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.FuncApp = {
        vpr.FuncApp(func = getUnboxFunc(id)(ctx), args = Seq(y))(pos, info, errT)
      }

      private def getDomain(x: ComponentParameter)(ctx: Context): vpr.Domain = {
        genDomainMap.getOrElse(x, {genTriple(x)(ctx); genDomainMap(x)})
      }

      private def getBoxFunc(x: ComponentParameter)(ctx: Context): vpr.Function = {
        genBoxFuncMap.getOrElse(x, {genTriple(x)(ctx); genBoxFuncMap(x)})
      }

      private def getUnboxFunc(x: ComponentParameter)(ctx: Context): vpr.Function = {
        genUnboxFuncMap.getOrElse(x, {genTriple(x)(ctx); genUnboxFuncMap(x)})
      }

      private var generatedMember: List[vpr.Member] = List.empty
      private var genDomainMap: Map[ComponentParameter, vpr.Domain] = Map.empty
      private var genBoxFuncMap: Map[ComponentParameter, vpr.Function] = Map.empty
      private var genUnboxFuncMap: Map[ComponentParameter, vpr.Function] = Map.empty

      /**
        * Generates domain, box function, and unbox function:
        *
        * domain N{}
        *
        * function boxN(x: T): N
        *   requires p(x)
        *   ensures  unboxN(result) == x
        *
        * function unboxN(y: N): T
        *   ensures p(result) && boxN(result) == y
        *
        * */
      private def genTriple(id: ComponentParameter)(ctx: Context): Unit = {
        val domainName = s"${Names.embeddingDomain}${Names.freshName}"

        val domain = vpr.Domain(
          name = domainName,
          functions = Seq.empty,
          axioms = Seq.empty,
          typVars = Seq.empty
        )()

        exclusiveDomainAxiom(id, DomainType(domain, Map.empty))(ctx)

        val N = vpr.DomainType(domain = domain, typVarsMap = Map.empty)
        val x = vpr.LocalVarDecl("x", t(id)(ctx))()
        val y = vpr.LocalVarDecl("y", N)()

        def boxApp(arg: vpr.Exp): vpr.FuncApp = {
          vpr.FuncApp(
            funcname = s"${Names.embeddingBoxFunc}_${N.domainName}",
            args = Seq(arg)
          )(vpr.NoPosition, vpr.NoInfo, typ = x.typ, vpr.NoTrafos)
        }

        def domainBoxApp(arg: vpr.Exp): vpr.DomainFuncApp = {
          vpr.DomainFuncApp(
            funcname = s"${Names.embeddingBoxFunc}_${N.domainName}", args = Seq(arg), typVarMap = Map.empty
          )(NoPosition, NoInfo, N, domainName = domainName + "ArrayToSeq", NoTrafos)
        }

        def unboxApp(arg: vpr.Exp): vpr.FuncApp = {
          vpr.FuncApp(
            funcname = s"${Names.embeddingUnboxFunc}_${N.domainName}",
            args = Seq(arg)
          )(vpr.NoPosition, vpr.NoInfo, typ = x.typ, vpr.NoTrafos)
        }

        def domainUnboxApp(arg: vpr.Exp): vpr.DomainFuncApp = {
          val resT = t(id)(ctx)
          vpr.DomainFuncApp(
            funcname = s"${Names.embeddingUnboxFunc}_${N.domainName}", args = Seq(arg), typVarMap = Map.empty
          )(NoPosition, NoInfo, resT, domainName = domainName + "ArrayToSeq", NoTrafos)
        }

        val box = vpr.Function(
          name = s"${Names.embeddingBoxFunc}_${N.domainName}",
          formalArgs = Seq(x),
          typ = N,
          pres = Seq(p(x.localVar, id)(ctx)),
          posts = Seq(vpr.EqCmp(unboxApp(vpr.Result(N)()), x.localVar)(),
          vpr.EqCmp(vpr.Result(N)(), domainBoxApp(x.localVar))()),
          body = None
        )()

        val unbox = {
          val resT = t(id)(ctx)

          vpr.Function(
            name = s"${Names.embeddingUnboxFunc}_${N.domainName}",
            formalArgs = Seq(y),
            typ = resT,
            pres = Seq.empty,
            posts = Seq(p(vpr.Result(resT)(), id)(ctx),
              vpr.EqCmp(boxApp(vpr.Result(resT)()), y.localVar)(),
            vpr.EqCmp(vpr.Result(resT)(), domainUnboxApp(y.localVar))()),
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

      private def exclusiveDomainAxiom(componentParameter: ComponentParameter, domainType: vpr.DomainType)(ctx: Context): Unit = {
        val domainName = domainType.domainName
        require(domainName != "")

        val arrayType = t(componentParameter)(ctx)
        val length = componentParameter._1

        val embeddedArg = vpr.LocalVarDecl("b", domainType)()
        val arraySeqTyp = arrayType
        val seqArg = vpr.LocalVarDecl("a", arrayType)()
        val newDomainName = domainName + "Array"

        val unboxFunc = vpr.DomainFunc(
          name = s"${Names.embeddingUnboxFunc}_$domainName", formalArgs = Seq(embeddedArg), typ = arrayType
        )(domainName = newDomainName)

        val boxFunc = vpr.DomainFunc(
          name = s"${Names.embeddingBoxFunc}_$domainName", formalArgs = Seq(seqArg), typ = domainType
        )(domainName = newDomainName)

        def unboxApp(arg: vpr.Exp) = vpr.DomainFuncApp(
          func = unboxFunc, args = Seq(arg), typVarMap = Map.empty
        )()

        def boxApp(arg: vpr.Exp) = vpr.DomainFuncApp(
          func = boxFunc, args = Seq(arg), typVarMap = Map.empty
        )()

        val unboxAxiom = {
          val unboxAppA = unboxApp(embeddedArg.localVar)
          vpr.AnonymousDomainAxiom(
            vpr.Forall(
              variables = Seq(embeddedArg),
              triggers = Seq(vpr.Trigger(Seq(unboxApp(embeddedArg.localVar)))()),
              exp = vpr.And(
                vpr.EqCmp(vpr.SeqLength(unboxAppA)(), vpr.IntLit(length)())(),
                vpr.EqCmp(boxApp(unboxAppA), embeddedArg.localVar)()
              )()
            )()
          )(domainName = newDomainName)
        }

        val seqVar = vpr.LocalVarDecl("s", arraySeqTyp)()

        val boxAxiom = {
          vpr.AnonymousDomainAxiom(
            vpr.Forall(
              variables = Seq(seqVar),
              triggers = Seq(vpr.Trigger(Seq(boxApp(seqVar.localVar)))()),
              exp = vpr.Implies(
                vpr.EqCmp(vpr.SeqLength(seqVar.localVar)(), vpr.IntLit(length)())(),
                vpr.EqCmp(unboxApp(boxApp(seqVar.localVar)), seqVar.localVar)()
              )()
            )()
          )(domainName = newDomainName)
        }

        val domain = vpr.Domain(
          name = newDomainName,
          functions = Seq(boxFunc, unboxFunc),
          axioms = Seq(boxAxiom, unboxAxiom),
          typVars = Seq.empty
        )()

        generatedMember ::= domain
      }
    }
}

