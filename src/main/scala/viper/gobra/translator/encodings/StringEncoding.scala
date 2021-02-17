// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings

import org.apache.commons.codec.binary.Hex
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.Exclusive
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeLevel.unit
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class StringEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  private val domainName: String = Names.stringsDomain
  private val domainType: vpr.DomainType = vpr.DomainType(domainName, Map.empty)(Seq.empty)
  private var strLengths: Map[String, Int] = Map.empty
  private var funcs: Map[String, vpr.DomainFunc] = Map.empty
  private val stringBeginning: String = "stringLit"

  // TODO: doc the length func
  private val lenFuncName: String = "strLen"
  private val lenFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = lenFuncName,
    formalArgs = Seq(vpr.LocalVarDecl(Names.freshName, vpr.Int)()),
    typ = vpr.Int,
  )(domainName = domainName)

  //TODO: doc the string concat
  private val concatFuncName: String = "strConcat"
  private val concatFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = concatFuncName,
    formalArgs = Seq(vpr.LocalVarDecl(Names.freshName, vpr.Int)(), vpr.LocalVarDecl(Names.freshName, vpr.Int)()),
    typ = vpr.Int,
  )(domainName = domainName)

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.String() => domainType
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    // TODO: doc
    default(super.expr(ctx)) {
      case (e: in.DfltVal) :: ctx.String() / Exclusive =>
        val encodedStr = stringBeginning + "Default"
        strLengths += encodedStr -> 0
        unit(withSrc(vpr.DomainFuncApp(func = makeFunc(encodedStr), Seq(), Map.empty), e))
      case lit: in.StringLit =>
        val encodedStr = stringBeginning + Hex.encodeHexString(lit.s.getBytes("UTF-8"))
        strLengths += encodedStr -> lit.s.length
        unit(withSrc(vpr.DomainFuncApp(func = makeFunc(encodedStr), Seq(), Map.empty), lit))
      case len@in.Length(exp :: ctx.String()) =>
        for { e <- goE(exp) } yield withSrc(vpr.DomainFuncApp(func = lenFunc, Seq(e), Map.empty), len)
      case concat@in.Concat(l :: ctx.String(), r :: ctx.String()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.DomainFuncApp(concatFunc, Seq(lEncoded, rEncoded), Map.empty),concat)
    }
  }

  override def variable(ctx: Context): in.BodyVar ==> vpr.LocalVarDecl = {
    case v if typ(ctx).isDefinedAt(v.typ) =>
      val (pos, info, errT) = v.vprMeta
      vpr.LocalVarDecl(v.id, vpr.Int)(pos, info, errT)
  }

  override def finalize(col: Collector): Unit = {
    col.addMember(genDomain())
  }

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

  private def genDomain(): vpr.Domain = {
    funcs += lenFuncName -> lenFunc
    funcs += concatFuncName -> concatFunc

    // TODO: doc axioms
    val lenAxioms = strLengths.toSeq.map {
      case (str, len) =>
        vpr.AnonymousDomainAxiom {
          val encodedStr: vpr.Exp = vpr.DomainFuncApp(funcs(str), Seq.empty, Map.empty)()
          val lenCall = vpr.DomainFuncApp(func = lenFunc, Seq(encodedStr), Map.empty)()
          vpr.EqCmp(lenCall, vpr.IntLit(BigInt(len))())()
        }(domainName = domainName)
    }

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