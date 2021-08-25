// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings

import org.apache.commons.codec.binary.Hex
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.FunctionGenerator
import viper.gobra.translator.util.ViperWriter.CodeLevel._
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.TypeBounds
import viper.silver.{ast => vpr}

import scala.annotation.unused

class StringEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  private val domainName: String = Names.stringsDomain
  private var encodedStrings: Map[String, vpr.DomainFunc] = Map.empty
  private val stringBeginning: String = "stringLit"
  private def genLitFuncName(lit: String): String = stringBeginning + Hex.encodeHexString(lit.getBytes("UTF-8"))

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.String() / Exclusive => stringType
    case ctx.String() / Shared => vpr.Ref
  }

  private lazy val stringType: vpr.Type = {
    isUsed = true
    vpr.Int
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * [ strLit: string° ] -> stringLitX() where X is a unique suffix dependant on the value of the string literal
    * [ len(s: string) ] -> strLen([s])
    * [ (s1: string) + (s2: string) ] -> strConcat([ s1 ], [ s2 ])
    * [ s[low : high] : string -> strSlice([ s ], [ low ], [ high ])
    * [ string(s :: []byte) ] -> byteSliceToStrFunc([ s ])
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)) {
      case (e: in.DfltVal) :: ctx.String() / Exclusive =>
        unit(withSrc(vpr.DomainFuncApp(func = makeFunc(""), Seq(), Map.empty), e)) // "" is the default string value
      case (lit: in.StringLit) :: _ / Exclusive =>
        unit(withSrc(vpr.DomainFuncApp(func = makeFunc(lit.s), Seq(), Map.empty), lit))
      case len @ in.Length(exp :: ctx.String()) =>
        for { e <- goE(exp) } yield withSrc(vpr.DomainFuncApp(func = lenFunc, Seq(e), Map.empty), len)
      case concat @ in.Concat(l :: ctx.String(), r :: ctx.String()) =>
        for {
          lEncoded <- goE(l)
          rEncoded <- goE(r)
        } yield withSrc(vpr.DomainFuncApp(concatFunc, Seq(lEncoded, rEncoded), Map.empty),concat)
      case slice @ in.Slice(base :: ctx.String(), low, high, _, _) =>
        for {
          baseExp <- goE(base)
          lowExp  <- goE(low)
          highExp <- goE(high)
        } yield withSrc(vpr.FuncApp(strSlice, Seq(baseExp, lowExp, highExp)), slice)
      case conv@in.Conversion(in.StringT(_), expr :: ctx.Slice(in.IntT(_, TypeBounds.Byte))) =>
        val (pos, info, errT) = conv.vprMeta
        for { e <- goE(expr) } yield byteSliceToStr(e)(ctx)(pos, info, errT)
    }
  }


  /**
    * Encodes the (effectul) conversion from a string to a []byte
    * [ target = []byte(str) ] ->
    *   [
    *     var s []byte
    *     inhale forall i Int :: 0 <= i && i < len(s) ==> acc(&s[i])
    *     target = s
    *   ]
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {

    def goA(x: in.Assertion): CodeWriter[vpr.Exp] = ctx.ass.translate(x)(ctx)

    default(super.statement(ctx)) {
      case conv@in.EffectfulConversion(target, in.SliceT(in.IntT(_, TypeBounds.Byte), _), _) =>
        // the argument of type string is not used in the viper encoding. May change in the future to be able to prove more
        // interesting properties
        val (pos, info, errT) = conv.vprMeta

        val sliceT = in.SliceT(in.IntT(Addressability.sliceElement, TypeBounds.Byte), Addressability.outParameter)
        val slice = in.LocalVar(Names.freshName, sliceT)(conv.info)
        val vprSlice = ctx.typeEncoding.variable(ctx)(slice)
        val qtfVar = in.BoundVar("i", in.IntT(Addressability.boundVariable))(conv.info)
        val post = in.SepForall(
          vars = Vector(qtfVar),
          triggers = Vector(in.Trigger(Vector(in.IndexedExp(slice, qtfVar, sliceT)(conv.info)))(conv.info)),
          body = in.Implication(
            in.And(in.AtMostCmp(in.IntLit(BigInt(0))(conv.info), qtfVar)(conv.info), in.LessCmp(qtfVar, in.Length(slice)(conv.info))(conv.info))(conv.info),
            in.Access(in.Accessible.Address(in.IndexedExp(slice, qtfVar, sliceT)(conv.info)), in.FullPerm(conv.info))(conv.info)
          )(conv.info)
        )(conv.info)

        seqn(
          for {
            _ <- local(vprSlice)
            vprPost <- goA(post)
            _ <- write(vpr.Inhale(vprPost)(pos, info, errT))
            ass <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(target), slice, conv)
          } yield ass
        )
    }
  }

  override def finalize(col: Collector): Unit = {
    if (isUsed) {
      col.addMember(genDomain())
      col.addMember(strSlice)
      byteSliceToStrFuncGenerator.finalize(col)
    }
  }
  private var isUsed: Boolean = false

  /** Every string literal in the program is encoded as a unique domain function in the String domain,
    * whose value corresponds to the string id.
    *     unique function stringLitX(): Int
    * Here, X is an unique suffix depending on the literal value.
    */
  private def makeFunc(name: String): vpr.DomainFunc = {
    val func = vpr.DomainFunc(
      name = genLitFuncName(name),
      formalArgs = Seq(),
      typ = stringType,
      unique = true
    )(domainName = domainName)
    encodedStrings += name -> func
    func
  }

  /**
    * Generates
    *   function strLen(id: Int): Int
    */
  private val lenFuncName: String = "strLen"
  private lazy val lenFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = lenFuncName,
    formalArgs = Seq(vpr.LocalVarDecl("id", stringType)()),
    typ = vpr.Int,
  )(domainName = domainName)

  /**
    * Generates
    *   function strConcat(l: Int, r: Int): Int
    * where l and r are string ids
    */
  private val concatFuncName: String = "strConcat"
  private lazy val concatFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = concatFuncName,
    formalArgs = Seq(vpr.LocalVarDecl("l", stringType)(), vpr.LocalVarDecl("r", stringType)()),
    typ = stringType,
  )(domainName = domainName)

  /**
    * Generates
    *   function strSlice(s: Int, l: Int, h: Int): Int
    *     requires 0 <= l
    *     requires l <= h
    *     requires h <= len(s)
    *     ensures strLen(s) == h - l
    * where s is a string id and l and r are the lower and upper bounds of the slice
    */
  private val strSliceName: String = "strSlice"
  val strSlice: vpr.Function = {
    val argS = vpr.LocalVarDecl("s", stringType)()
    val argL = vpr.LocalVarDecl("l", vpr.Int)()
    val argH = vpr.LocalVarDecl("h", vpr.Int)()
    vpr.Function(
      name = strSliceName,
      formalArgs = Seq(argS, argL, argH),
      typ = stringType,
      pres = Seq(
        vpr.LeCmp(vpr.IntLit(0)(), argL.localVar)(),
        vpr.LeCmp(argL.localVar, argH.localVar)(),
        vpr.LeCmp(argH.localVar, vpr.DomainFuncApp(lenFunc, Seq(argS.localVar), Map.empty)())()
      ),
      posts = Seq(
        vpr.EqCmp(
          vpr.DomainFuncApp(lenFunc, Seq(vpr.Result(stringType)()), Map.empty)(),
          vpr.Sub(argH.localVar, argL.localVar)()
        )()
      ),
      body = None
    )()
  }

  private def genDomain(): vpr.Domain = {
    /**
      * The length of every string literal in the program is axiomatized as
      *   axiom {
      *     strLen(literal()) == X
      *   }
      * where `literal` is one of the generated unique domain functions and X is the length of the corresponding string
      */
    val litLenAxioms = encodedStrings.keys.toSeq.map { str =>
      vpr.AnonymousDomainAxiom {
        val encodedStr: vpr.Exp = vpr.DomainFuncApp(encodedStrings(str), Seq.empty, Map.empty)()
        val lenCall = vpr.DomainFuncApp(func = lenFunc, Seq(encodedStr), Map.empty)()
        vpr.EqCmp(lenCall, vpr.IntLit(BigInt(str.length))())()
      }(domainName = domainName)
    }

    /**
      * Every string has a non-negative length:
      *   axiom {
      *     forall x string :: { strLen(str) } 0 <= strLen(x)
      *   }
      */
    val lenAxiom = vpr.AnonymousDomainAxiom {
      val qtfVar = vpr.LocalVarDecl("str", stringType)()
      val lenApp = vpr.DomainFuncApp(lenFunc, Seq(qtfVar.localVar), Map.empty)()
      vpr.Forall(
        variables = Seq(qtfVar),
        triggers = Seq(vpr.Trigger(Seq(lenApp))()),
        exp = vpr.LeCmp(vpr.IntLit(0)(), lenApp)()
      )()
    }(domainName = domainName)

    /**
      * Generates
      *   axiom {
      *     forall l: Int, r: Int :: {strLen(strConcat(l, r))} strLen(strConcat(l, r)) == strLen(l) + strLen(r)
      *   }
      *   where l and r correspond to string ids
      */
    val appAxiom: vpr.DomainAxiom = vpr.AnonymousDomainAxiom {
      val var1 = vpr.LocalVarDecl("l", stringType)()
      val var2 = vpr.LocalVarDecl("r", stringType)()
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
      functions = lenFunc +: concatFunc +: encodedStrings.values.toSeq,
      axioms = appAxiom +: lenAxiom +: litLenAxioms,
      typVars = Seq.empty
    )()
  }

  /** Generates the function
    *   requires forall i int :: 0 <= i && i < len(s) ==> acc(&s[i], _)
    *   pure func byteSliceToStrFunc(s []byte) string
    */
  private val byteSliceToStrFuncName: String = "byteSliceToStrFunc"
  private val byteSliceToStrFuncGenerator: FunctionGenerator[Unit] = new FunctionGenerator[Unit] {
    override def genFunction(@unused x: Unit)(ctx: Context): vpr.Function = {
      val info = Source.Parser.Internal
      val paramT = in.SliceT(in.IntT(Addressability.sliceElement, TypeBounds.Byte), Addressability.outParameter)
      val param = in.Parameter.In("s", paramT)(info)
      val res = in.Parameter.Out("res", in.StringT(Addressability.outParameter))(info)
      val qtfVar = in.BoundVar("i", in.IntT(Addressability.boundVariable))(info)
      val pre = in.SepForall(
        vars = Vector(qtfVar),
        triggers = Vector(in.Trigger(Vector(in.IndexedExp(param, qtfVar, paramT)(info)))(info)),
        body = in.Implication(
          in.And(in.AtMostCmp(in.IntLit(BigInt(0))(info), qtfVar)(info), in.LessCmp(qtfVar, in.Length(param)(info))(info))(info),
          in.Access(in.Accessible.Address(in.IndexedExp(param, qtfVar, paramT)(info)), in.WildcardPerm(info))(info)
        )(info)
      )(info)

      val func: in.PureFunction = in.PureFunction(
        name = in.FunctionProxy(byteSliceToStrFuncName)(info),
        args = Vector(param),
        results = Vector(res),
        pres = Vector(pre),
        posts = Vector(),
        body = None
      )(info)
      val translatedFunc = ctx.pureMethod.pureFunction(func)(ctx)
      translatedFunc.res
    }
  }

  private def byteSliceToStr(slice: vpr.Exp)(ctx: Context)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.FuncApp =
    byteSliceToStrFuncGenerator(Vector(slice), ())(pos, info, errT)(ctx)
}