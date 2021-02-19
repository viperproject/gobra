// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings

import org.apache.commons.codec.binary.Hex
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeLevel.unit
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class StringEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  private val domainName: String = Names.stringsDomain
  private var strLengths: Map[String, Int] = Map.empty
  private var funcs: Map[String, vpr.DomainFunc] = Map.empty
  private val stringBeginning: String = "stringLit"

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.String() / Exclusive => vpr.Int
    case ctx.String() / Shared => vpr.Ref
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * [ strLit: string° ] -> stringLitX() where X is a unique suffix dependant on the value of the string literal
    * [ len(s: string) ] -> strLen([s])
    * [ (s1: string) + (s2: string) ] -> strConcat([ s1 ], [ s2 ])
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def genLitFuncName(lit: String): String = stringBeginning + Hex.encodeHexString(lit.getBytes("UTF-8"))

    default(super.expr(ctx)) {
      case (e: in.DfltVal) :: ctx.String() / Exclusive =>
        val litFuncName = genLitFuncName("") // "" is the default string value
        strLengths += litFuncName -> 0
        unit(withSrc(vpr.DomainFuncApp(func = makeFunc(litFuncName), Seq(), Map.empty), e))
      case lit: in.StringLit if lit.typ.addressability == Exclusive =>
        val litFuncName = genLitFuncName(lit.s)
        strLengths += litFuncName -> lit.s.length
        unit(withSrc(vpr.DomainFuncApp(func = makeFunc(litFuncName), Seq(), Map.empty), lit))
      case len@in.Length(exp :: ctx.String()) =>
        for { e <- goE(exp) } yield withSrc(vpr.DomainFuncApp(func = lenFunc, Seq(e), Map.empty), len)
      case concat@in.Concat(l :: ctx.String(), r :: ctx.String()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.DomainFuncApp(concatFunc, Seq(lEncoded, rEncoded), Map.empty),concat)
    }
  }

  /**
    * Encodes a string variable as an vpr.Int
    */
  override def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = {
    case v :: ctx.String() / Exclusive if typ(ctx).isDefinedAt(v.typ) =>
      val (pos, info, errT) = v.vprMeta
      vpr.LocalVarDecl(v.id, vpr.Int)(pos, info, errT)
    case v :: ctx.String() / Shared if typ(ctx).isDefinedAt(v.typ) =>
      val (pos, info, errT) = v.vprMeta
      vpr.LocalVarDecl(v.id, vpr.Ref)(pos, info, errT)
  }

  override def finalize(col: Collector): Unit = {
    col.addMember(genDomain())
  }

  /** Every string literal in the program is encoded as a unique domain function in the String domain,
    * whose value corresponds to the string id.
    *     unique function stringLitX(): Int
    * Here, X is an unique suffix depending on the literal value.
    */
  private def makeFunc(name: String): vpr.DomainFunc = {
    val func = vpr.DomainFunc(
      name = name,
      formalArgs = Seq(),
      typ = vpr.Int,
      unique = true
    )(domainName = domainName)
    funcs += name -> func
    func
  }

  /**
    * Generates
    *   function strLen(id: Int): Int
    */
  private val lenFuncName: String = "strLen"
  private val lenFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = lenFuncName,
    formalArgs = Seq(vpr.LocalVarDecl(Names.freshName, vpr.Int)()),
    typ = vpr.Int,
  )(domainName = domainName)

  /**
    * Generates
    *   function strConcat(l: Int, r: Int): Int
    * where l and r are string ids
    */
  private val concatFuncName: String = "strConcat"
  private val concatFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = concatFuncName,
    formalArgs = Seq(vpr.LocalVarDecl(Names.freshName, vpr.Int)(), vpr.LocalVarDecl(Names.freshName, vpr.Int)()),
    typ = vpr.Int,
  )(domainName = domainName)

  private def genDomain(): vpr.Domain = {
    funcs += lenFuncName -> lenFunc
    funcs += concatFuncName -> concatFunc

    /**
      * The length of every string literal in the program is axiomatized as
      *   axiom {
      *     strLen(literal()) == X
      *   }
      * where `literal` is one of the generated unique domain functions and X is the length of the corresponding string
      */
    val lenAxioms = strLengths.toSeq.map {
      case (str, len) =>
        vpr.AnonymousDomainAxiom {
          val encodedStr: vpr.Exp = vpr.DomainFuncApp(funcs(str), Seq.empty, Map.empty)()
          val lenCall = vpr.DomainFuncApp(func = lenFunc, Seq(encodedStr), Map.empty)()
          vpr.EqCmp(lenCall, vpr.IntLit(BigInt(len))())()
        }(domainName = domainName)
    }

    /**
      * Generates
      *   axiom {
      *     forall l: Int, r: Int :: {strLen(strConcat(l, r))} strLen(strConcat(l, r)) == strLen(l) + strLen(r)
      *   }
      *   where l and r correspond to string ids
      */
    val appAxiom: vpr.DomainAxiom = vpr.AnonymousDomainAxiom {
      val var1 = vpr.LocalVarDecl(Names.freshName, vpr.Int)()
      val var2 = vpr.LocalVarDecl(Names.freshName, vpr.Int)()
      val lenConcat = vpr.DomainFuncApp(lenFunc, Seq(vpr.DomainFuncApp(concatFunc, Seq(var1.localVar, var2.localVar), Map.empty)()), Map.empty)()
      val trigger = vpr.Trigger(Seq(lenConcat))()
      val exp = vpr.EqCmp(lenConcat, vpr.Add(
        vpr.DomainFuncApp(lenFunc, Seq(var1.localVar), Map.empty)(),
        vpr.DomainFuncApp(lenFunc, Seq(var2.localVar), Map.empty)()
      )())()
      vpr.Forall(Seq(var1, var2), Seq(trigger), exp)()
    }(domainName = domainName)

    vpr.Domain(
      name = domainName,
      functions = funcs.values.toSeq,
      axioms = appAxiom +: lenAxioms,
      typVars = Seq.empty
    )()
  }
}