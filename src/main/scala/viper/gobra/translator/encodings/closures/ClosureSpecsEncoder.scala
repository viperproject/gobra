// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.closures

import viper.gobra.ast.internal.{FunctionLikeMemberOrLit, LookupTable}
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.gobra.reporting.{PreconditionError, Source, SpecNotImplementedByClosure}
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.typeless.CallEncoding
import viper.gobra.translator.util.ViperWriter.CodeLevel.errorT
import viper.gobra.translator.util.ViperWriter.MemberKindCompanion.ErrorT
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.verifier.{reasons, errors => vprerr}
import viper.silver.{ast => vpr}

protected class ClosureSpecsEncoder {

  /** Encodes spec implementation expressions as calls to the corresponding generated domain function.
    * R[ cl implements spec{params} ] -> closureImplements$[spec]$[idx of params]([cl], [params])
    * */
  def closureImplementsExpression(a: in.ClosureImplements)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(a.spec)(ctx, a.info)
    ctx.expression(in.DomainFunctionCall(implementsFunctionProxy(a.spec)(a.info),
      Vector(a.closure) ++ a.spec.params.toVector.sortBy(_._1).map(_._2), in.BoolT(Addressability.rValue))(a.info))
  }

  /**
    * Encodes:
    *   - function literal getters
    *   - function getters
    *
    * R[ pure? func funcLitName(_)_ { _ } ] ->
    *   register spec instance funcLitName{}
    *   return closureGet$funcLitName([ pointers to captured vars ])
    *
    * R[ funcName ] ->
    *   register spec instance funcName{}
    *   return closureGet$funcName()
    */
  def callToClosureGetter(func: in.FunctionMemberOrLitProxy, captured: Vector[(in.Expr, in.Parameter.In)] = Vector.empty)(ctx: Context): CodeWriter[vpr.Exp] = {
    val errorTransformers = register(in.ClosureSpec(func, Map.empty)(func.info))(ctx, func.info)
    for {
      exp <- ctx.expression(in.PureFunctionCall(closureGetterFunctionProxy(func), captured.map(c => c._1), genericFuncType)(func.info))
      _ <- errorT(errorTransformers: _*)
    } yield exp
  }

  /**
    * Encodes (non-pure) closure calls.
    *
    * [ts := cl(args) as spec{params}] -> [ts] := closureCall$[spec]$[idx of params]([cl], [params], [args])
    */
  def closureCall(c: in.ClosureCall)(ctx: Context): CodeWriter[vpr.Stmt] = {
    register(c.spec)(ctx, c.spec.info)

    for {
      call <- ctx.statement(in.FunctionCall(c.targets, closureCallProxy(c.spec)(c.info), closureCallArgs(c.closure, c.args, c.spec)(ctx))(c.info))
      callNode = call.deepCollect{ case methCall: vpr.MethodCall => methCall }.head
      _ <- errorT(doesNotImplementSpecErr(callNode, c.closure.info.tag))
    } yield call
  }

  /**
    * Encodes pure closure calls.
    *
    * [cl(args) as spec{params}] -> closureCall$[spec]$[idx of params]([cl], [vars captured by cl], [args + params])
    */
  def pureClosureCall(c: in.PureClosureCall)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(c.spec)(ctx, c.spec.info)

    for {
      exp <- ctx.expression(in.PureFunctionCall(closureCallProxy(c.spec)(c.info), closureCallArgs(c.closure, c.args, c.spec)(ctx), c.typ)(c.info))
      callNode = exp.deepCollect{ case funcApp: vpr.FuncApp => funcApp }.head
      _ <- errorT(doesNotImplementSpecErr(callNode, c.closure.info.tag))
    } yield exp
  }

  // TODO
  // Distinguish between pure/impure?
  def goClosureCall(c: in.GoClosureCall)(ctx: Context): CodeWriter[vpr.Stmt] = {
    register(c.spec)(ctx, c.spec.info)

    val (pos, info, errT) = c.vprMeta
    for {
      member <- callableMemberWithClosure(c.spec)(ctx)
      args <- sequence()
      stmt = member match {
        case x => ???
      }

      stmt = CallEncoding.translateGoCall(spec.pres, spec.args, closureCallArgs(c.closure, c.args, c.spec)(ctx).tail)(ctx)(pos, info, errT)
    } yield ???
    val spec = memberOrLit(ctx)(c.spec.func)


    /*
    // TODO: better error
    for {
      n <- ctx.statement(in.GoFunctionCall(closureCallProxy(c.spec)(c.info), closureCallArgs(c.closure, c.args, c.spec)(ctx))(c.info))
      // callNode = call.deepCollect{ case methCall: vpr.MethodCall => methCall }.head
      //_ <- errorT(doesNotImplementSpecErr(n, c.closure.info.tag))
    } yield n

     */
  }

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    genMembers foreach { m => addMemberFn(m.res) }
  }

  private def captured(ctx: Context)(func: in.FunctionMemberOrLitProxy): Vector[(in.Expr, in.Parameter.In)] = func match {
    case _: in.FunctionProxy => Vector.empty
    case p: in.FunctionLitProxy => ctx.table.lookup(p).captured
  }

  private def memberOrLit(ctx: Context)(func: in.FunctionMemberOrLitProxy): in.FunctionLikeMemberOrLit = func match {
    case p: in.FunctionProxy => ctx.table.lookup(p).asInstanceOf[in.FunctionMember]
    case p: in.FunctionLitProxy => ctx.table.lookup(p)
  }

  /**
    * Registers spec fName{params}:
    * - if necessary, updates the maximum number of captured variables for the corresponding type
    * - if the spec has not already been registered:
    *   > generates a closureImplements$[spec] domain function (see [[implementsFunction]])
    *   > generates a closureCall$[spec] method/function (see [[callableMemberWithClosure]])
    *   > collects any error transformers generated while encoding closureCall$[...]
    * - if no spec with base function fName has already been registered:
    *   > generates a closureGet$fName getter function (see [[closureGetter]])
    * - Adds all of the generated members to [[genMembers]]
    * - Returns the collected error transformers
    * */
  private def register(spec: in.ClosureSpec)(ctx: Context, info: Source.Parser.Info): Vector[ErrorTransformer] = {
    var errorTransformers: Vector[ErrorTransformer] = Vector.empty
    updateCaptVarTypes(ctx)(captured(ctx)(spec.func).map(_._1.typ))
    if (!specsSeen.contains((spec.func, spec.params.keySet))) {
      specsSeen += ((spec.func, spec.params.keySet))
      val implementsF = implementsFunction(spec)(ctx, info)
      val callable = callableMemberWithClosure(spec)(ctx)
      errorTransformers = callable.sum.collect { case ErrorT(t) => t }
      genDomFuncs :+= implementsF
      genMembers :+= callable
    }
    if (!funcsUsedAsClosures.contains(spec.func)) {
      funcsUsedAsClosures += spec.func
      val getter = closureGetter(spec.func)(ctx)
      genMembers :+= getter
    }

    errorTransformers
  }

  private var captVarTypeAmount = Map[vpr.Type, Int]()
  def captVarsTypeMap: Map[vpr.Type, Int] = captVarTypeAmount

  /**
    * Updates the maximum counts of captured variables for each encountered type.
    * Assigns a unique id to each encountered type.
    */
  private def updateCaptVarTypes(ctx: Context)(types: Vector[in.Type]): Unit = {
    types.groupBy(ctx.typ).foreach {
      case (typ, vec) =>
        val amount = captVarTypeAmount.getOrElse(typ, 0)
        if (vec.size > amount) captVarTypeAmount += typ -> vec.size
    }
  }

  private var specsSeen: Set[(in.FunctionMemberOrLitProxy, Set[Int])] = Set.empty
  private var funcsUsedAsClosures: Set[in.FunctionMemberOrLitProxy] = Set.empty
  private var genMembers: Vector[MemberWriter[vpr.Member]] = Vector.empty
  private var genDomFuncs: Vector[vpr.DomainFunc] = Vector.empty

  def generatedDomainFunctions: Vector[vpr.DomainFunc] = genDomFuncs

  private val genericFuncType: in.FunctionT = in.FunctionT(Vector.empty, Vector.empty, Addressability.rValue)

  private def closureSpecName(spec: in.ClosureSpec): String =  s"${spec.func}$$${spec.params.keySet.toSeq.sorted.mkString("_")}"
  private def implementsFunctionName(spec: in.ClosureSpec) = s"${Names.closureImplementsFunc}$$${closureSpecName(spec)}"
  private def implementsFunctionProxy(spec: in.ClosureSpec)(info: Source.Parser.Info): in.DomainFuncProxy = in.DomainFuncProxy(implementsFunctionName(spec), Names.closureDomain)(info)
  private def closureGetterName(func: in.FunctionMemberOrLitProxy): String = s"${Names.closureGetter}$$$func"
  private def closureGetterFunctionProxy(func: in.FunctionMemberOrLitProxy): in.FunctionProxy = in.FunctionProxy(closureGetterName(func))(func.info)
  private def closureCallProxy(spec: in.ClosureSpec)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(s"${Names.closureCall}$$${closureSpecName(spec)}")(info)

  /**
    * Given spec{params}, generates domain function:
    * function closureImplements$[spec]$[idx of params](closure: Closure, [params name and type]): Bool
    * */
  private def implementsFunction(spec: in.ClosureSpec)(ctx: Context, info: Source.Parser.Info): vpr.DomainFunc = {
    val closurePar = in.Parameter.In(Names.closureArg, genericFuncType)(info)
    val params = spec.params.map(p => in.Parameter.In(Names.closureImplementsParam(p._1), p._2.typ.withAddressability(Addressability.inParameter))(p._2.info))
    val args = (Vector(closurePar) ++ params) map ctx.variable
    vpr.DomainFunc(implementsFunctionName(spec), args, vpr.Bool)(domainName = Names.closureDomain)
  }

  /**
    * Given fName, generates:
    * function closureGet$[fName](captvar1_0: Type0, captvar2_0: Type0, captvar1_1: Type1, ...): Closure
    *   ensures [result implements fName]
    *   for X-th captured variable of T-th type: ensures captVarXClosure_T(result) == captvarX_T(result)
    * */
  private def closureGetter(func: in.FunctionMemberOrLitProxy)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = closureGetterFunctionProxy(func)
    val info = func.info
    val result = in.Parameter.Out(Names.closureArg, genericFuncType)(info)
    val satisfiesSpec = in.ExprAssertion(in.ClosureImplements(result, in.ClosureSpec(func, Map.empty)(info))(info))(info)
    val (args, captAssertions) = capturedArgsAndAssertions(ctx)(result, captured(ctx)(func), info)
    val getter = in.PureFunction(proxy, args, Vector(result), Vector.empty, Vector(satisfiesSpec) ++ captAssertions, Vector.empty, None)(memberOrLit(ctx)(func).info)
    ctx.defaultEncoding.pureFunction(getter)(ctx)
  }

  /**
    * Returns the arguments for an encoded closure call:
    *   (closure, args, fName{params}) -> (closure, vars captured by fName, args + params)
   */
  private def closureCallArgs(closure: in.Expr, args: Vector[in.Expr], spec: in.ClosureSpec)(ctx: Context): Vector[in.Expr] = {
    val capt = captured(ctx)(spec.func)
    val captProxies = captVarProxies(ctx)(capt.map(_._1.typ), spec.func.info)
    val captArgs = (1 to capt.size).map(i => in.DomainFunctionCall(captProxies(i-1), Vector(closure), capt(i-1)._2.typ)(closure.info))
    val argsAndParams = {
      var argsUsed = 0
      (1 to (args.size + spec.params.size)).map(i => if (spec.params.contains(i)) spec.params(i) else { argsUsed += 1; args(argsUsed-1) })
    }
    Vector(closure) ++ captArgs ++ argsAndParams
  }

  /**
    * Returns (captArgs, assertions), where:
    *   - captArgs is the list of parameters corresponding to the variables captured by the closure
    *   - assertions is the list of assertions, one for each captured variable captVarXClosure_T, of the form:
    *       captVarXClosure_T(result) == captvarX_T(result)
    */
  private def capturedArgsAndAssertions(ctx: Context)(closure: in.Expr, captured: Vector[(in.Expr, in.Parameter.In)], info: Source.Parser.Info): (Vector[in.Parameter.In], Vector[in.Assertion]) = {
    val captArgs = captured.map(c => c._2)
    val captProxies = captVarProxies(ctx)(captured.map(c => c._1.typ), info)
    val capturesVar: Int => in.Assertion = i => in.ExprAssertion(in.EqCmp(in.DomainFunctionCall(
      captProxies(i-1), Vector(closure), captArgs(i-1).typ)(info), captArgs(i-1))(info))(info)
    val assertions = (1 to captured.size).toVector map { i => capturesVar(i) }
    (captArgs, assertions)
  }

  /** Generate the proxies for the domain functions corresponding to the variables captured by the closure.
    * The name is captVarNClosure_[type], where [type] is the serialized Viper type of the variable, and N denotes
    * the position of the variable among all the variables with the same type. */
  private def captVarProxies(ctx: Context)(types: Vector[in.Type], info: Source.Parser.Info): Vector[in.DomainFuncProxy] = {
    var seenSoFar = Map[vpr.Type, Int]()
    types map { t =>
      val vprTyp = ctx.typ(t)
      val i = seenSoFar.getOrElse(vprTyp, 0) + 1
      seenSoFar += vprTyp -> i
      in.DomainFuncProxy(Names.closureCaptVarDomFunc(i, vprTyp), Names.closureDomain)(info)
    }
  }

  /** Replaces all the parameter expressions in spec with the corresponding in-parameter of function f */
  private def specWithFuncArgs(spec: in.ClosureSpec, f: FunctionLikeMemberOrLit): in.ClosureSpec =
    in.ClosureSpec(spec.func, spec.params.map{ case (i, _) => i -> f.args(i-1)})(spec.info)

  /** Generates a Viper method/function with the same specification as the original.
    *
    * For function literals that capture variables, adds precondition:
    *   closure == closureGet$[spec.func]([capture variable arguments])
    * For function literals that do not capture variables or function members, adds precondition:
    *   closureImplements$[spec](closure, [spec.params])
    *
    * For pure functions, adds a postcondition:
    *   ensures result == [RETURN EXPRESSION]
    * We need this because, for pure functions, we consider the body as part of the specification
    *   (i.e. after a call, we can make assertions based on the body)
    *
    * For function literals, also includes captured variables among the arguments, and encodes the body as
    * well as the specification, so that the literal is verified. */
  private def callableMemberWithClosure(spec: in.ClosureSpec)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = closureCallProxy(spec)(spec.info)
    val func = memberOrLit(ctx)(spec.func)
    val closurePar = in.Parameter.In(Names.closureArg, genericFuncType)(func.info)
    val captArgs = captured(ctx)(spec.func).map(_._2)
    val implementsAssertion = if (captArgs.isEmpty)
      Some(in.ExprAssertion(in.ClosureImplements(closurePar, specWithFuncArgs(spec, func))(spec.info))(spec.info)) else None
    val fromClosureGetter = if (captArgs.nonEmpty)
      Some(in.ExprAssertion(in.EqCmp(closurePar,
        in.PureFunctionCall(closureGetterFunctionProxy(spec.func), captArgs, genericFuncType)(spec.info))(spec.info)
      )(spec.info)) else None
    val args = Vector(closurePar) ++ captArgs ++ func.args
    val pres = implementsAssertion.toVector ++ fromClosureGetter ++ func.pres

    // Store the origin of the spec; if the first precondition fails, we use this to recognise and transform the error message
    implementAssertionSpecOriginToStr += spec.info.origin.get -> spec.info.tag

    func match {
      case _: in.Function =>
        val m = in.Function(proxy, args, func.results, pres, func.posts, func.terminationMeasures, None)(spec.info)
        ctx.defaultEncoding.function(m)(ctx)
      case lit: in.FunctionLit =>
        val body = if (spec.params.isEmpty) lit.body else None
        val func = in.Function(proxy, args, lit.results, pres, lit.posts, lit.terminationMeasures, body)(lit.info)
        ctx.defaultEncoding.function(func)(ctx)
      case f: in.PureFunction =>
        val posts = func.posts ++ assertionFromPureFunctionBody(f.body, f.results.head)
        val m = in.PureFunction(proxy, args, f.results, pres, posts, f.terminationMeasures, None)(spec.info)
        ctx.defaultEncoding.pureFunction(m)(ctx)
      case lit: in.PureFunctionLit =>
        val body = if (spec.params.isEmpty) lit.body else None
        val posts = lit.posts ++ (if (spec.params.isEmpty) Vector.empty else assertionFromPureFunctionBody(lit.body, lit.results.head).toVector)
        val func = in.PureFunction(proxy, args, lit.results, pres, posts, lit.terminationMeasures, body)(lit.info)
        ctx.defaultEncoding.pureFunction(func)(ctx)
    }
  }

  private var implementAssertionSpecOriginToStr: Map[Source.AbstractOrigin, String] = Map.empty
  private def doesNotImplementSpecErr(callNode: vpr.Node, closureStr: String): ErrorTransformer = {
    case vprerr.PreconditionInCallFalse(node@Source(info), reasons.AssertionFalse(Source(assInfo)), _) if (callNode eq node) && implementAssertionSpecOriginToStr.contains(assInfo.origin) =>
      PreconditionError(info).dueTo(SpecNotImplementedByClosure(info, closureStr, implementAssertionSpecOriginToStr(assInfo.origin)))
    case vprerr.PreconditionInAppFalse(node@Source(info), reasons.AssertionFalse(Source(assInfo)), _) if (callNode eq node) && implementAssertionSpecOriginToStr.contains(assInfo.origin) =>
      PreconditionError(info).dueTo(SpecNotImplementedByClosure(info, closureStr, implementAssertionSpecOriginToStr(assInfo.origin)))
  }

  /** From { body }, get assertion result == body
    * This is useful to avoid multiple error messages when the body does not verify,
    * by encoding the body as a postcondition.
    * The original body is still encoded, so this does not affect correctness. */
  private def assertionFromPureFunctionBody(body: Option[in.Expr], res: in.Expr): Option[in.Assertion] =
    body.map(e => in.ExprAssertion(in.EqCmp(res, e)(e.info))(e.info))
}
