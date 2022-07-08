package viper.gobra.translator.encodings.closures

import viper.gobra.ast.internal.{Expr, FunctionLikeMemberOrLit, Parameter}
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer, RichErrorMessage}
import viper.gobra.reporting.{PreconditionError, Source, SpecNotImplementedByClosure}
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeLevel.{errorT, reasonR}
import viper.gobra.translator.util.ViperWriter.MemberKindCompanion.ErrorT
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.verifier.{reasons, errors => vprerr}
import viper.silver.{ast => vpr}

protected class ClosureSpecsManager {

  def closureImplementsAssertion(a: in.ClosureImplements)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(a.spec)(ctx, a.info)
    ctx.expression(in.PureFunctionCall(implementsFunctionProxy(a.spec)(a.info),
      Vector(a.closure) ++ a.spec.params.toVector.sortBy(_._1).map(_._2), in.BoolT(Addressability.rValue))(a.info))
  }

  def callToClosureGetter(func: in.FunctionMemberOrLitProxy, captured: Vector[(in.Expr, in.Parameter.In)] = Vector.empty)(ctx: Context): CodeWriter[vpr.Exp] = {
    val errorTransformers = register(in.ClosureSpec(func, Map.empty)(func.info))(ctx, func.info)
    for {
      exp <- ctx.expression(in.PureFunctionCall(closureGetterFunctionProxy(func), captured.map(c => c._1), genericFuncType)(func.info))
      _ <- errorT(errorTransformers: _*)
    } yield exp
  }

  def closureCall(c: in.CallWithSpec)(ctx: Context): CodeWriter[vpr.Stmt] = {
    register(c.spec)(ctx, c.spec.info)

    for {
      call <- ctx.statement(in.FunctionCall(c.targets, closureCallProxy(c.spec)(c.info), closureCallArgs(c.closure, c.args, c.spec)(ctx))(c.info))
      _ <- errorT(doesNotImplementSpecErr(c.closure, c.spec))
    } yield call
  }

  def pureClosureCall(c: in.PureCallWithSpec)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(c.spec)(ctx, c.spec.info)

    for {
      exp <- ctx.expression(in.PureFunctionCall(closureCallProxy(c.spec)(c.info), closureCallArgs(c.closure, c.args, c.spec)(ctx), c.typ)(c.info))
      _ <- errorT(doesNotImplementSpecErr(c.closure, c.spec))
    } yield exp
  }

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    genMembers foreach { m => addMemberFn(m.res) }
  }

  private def doesNotImplementSpecErr(closureExpr: in.Expr, spec: in.ClosureSpec): ErrorTransformer = {
    val implementsFuncName = implementsFunctionName(spec)

    def transformer: ErrorTransformer = {
      case vprerr.PreconditionInCallFalse(Source(info), reasons.AssertionFalse(vpr.FuncApp(implementsFuncName, Seq(closure))), _)
        if closure.info.isInstanceOf[Source.Verifier.Info] && closure.info.asInstanceOf[Source.Verifier.Info].node == closureExpr =>
        PreconditionError(info).dueTo(SpecNotImplementedByClosure(info, closureExpr.info.tag, spec.info.tag))
    }
    transformer
  }

  private def captured(ctx: Context)(func: in.FunctionMemberOrLitProxy): Vector[(in.Expr, in.Parameter.In)] = func match {
    case _: in.FunctionProxy => Vector.empty
    case p: in.FunctionLitProxy => ctx.table.lookup(p).captured
  }

  private def memberOrLit(ctx: Context)(func: in.FunctionMemberOrLitProxy): in.FunctionLikeMemberOrLit = func match {
    case p: in.FunctionProxy => ctx.table.lookup(p).asInstanceOf[in.FunctionMember]
    case p: in.FunctionLitProxy => ctx.table.lookup(p)
  }

  private def register(spec: in.ClosureSpec)(ctx: Context, info: Source.Parser.Info): Vector[ErrorTransformer] = {
    var errorTransformers: Vector[ErrorTransformer] = Vector.empty
    if (!specsSeen.contains((spec.func, spec.params.keySet))) {
      specsSeen += ((spec.func, spec.params.keySet))
      val implementsF = implementsFunction(spec)(ctx, info)
      val callable = callableMemberWithClosure(spec)(ctx)
      errorTransformers = callable.sum.collect{ case ErrorT(t) => t }.toVector
      genMembers :+= implementsF
      genMembers :+= callable
    }
    if (!funcsUsedAsClosures.contains(spec.func)) {
      funcsUsedAsClosures += spec.func
      val getter = closureGetter(spec.func)(ctx)
      genMembers :+= getter
      maxCapturedVariables = Math.max(maxCapturedVariables, captured(ctx)(spec.func).size)
    }

    errorTransformers
  }

  private var maxCapturedVariables: Int = 0
  def maxCaptVariables: Int = maxCapturedVariables
  def numSpecs: Int = specsSeen.size

  private var specsSeen: Set[(in.FunctionMemberOrLitProxy, Set[Int])] = Set.empty
  private var funcsUsedAsClosures: Set[in.FunctionMemberOrLitProxy] = Set.empty
  private var genMembers: Vector[MemberWriter[vpr.Member]] = Vector.empty

  private val genericFuncType: in.FunctionT = in.FunctionT(Vector.empty, Vector.empty, Addressability.rValue)
  private val genericPointerType: in.PointerT = in.PointerT(in.BoolT(Addressability.Shared) ,Addressability.inParameter)

  private def closureSpecName(spec: in.ClosureSpec): String =  s"${spec.func}$$${spec.params.keySet.toSeq.sorted.mkString("_")}"
  private def implementsFunctionName(spec: in.ClosureSpec) = s"${Names.closureImplementsFunc}$$${closureSpecName(spec)}"
  private def implementsFunctionProxy(spec: in.ClosureSpec)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(implementsFunctionName(spec))(info)
  private def closureGetterFunctionProxy(func: in.FunctionMemberOrLitProxy): in.FunctionProxy = in.FunctionProxy(s"${Names.closureGetter}$$$func")(func.info)
  private def closureCallProxy(spec: in.ClosureSpec)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(s"${Names.closureCall}$$${closureSpecName(spec)}")(info)

  // Generates encoding: function closureImplements_funcName_(closure, parameters) bool
  private def implementsFunction(spec: in.ClosureSpec)(ctx: Context, info: Source.Parser.Info): MemberWriter[vpr.Member] = {
    val proxy = implementsFunctionProxy(spec)(info)
    val closurePar = in.Parameter.In(Names.closureArg, genericFuncType)(info)
    val params = spec.params.map(p => in.Parameter.In(Names.closureImplementsParam(p._1), p._2.typ)(p._2.info))
    val args = Vector(closurePar) ++ params
    val result = Vector(in.Parameter.Out("r", in.BoolT(Addressability.outParameter))(info))
    val func = in.PureFunction(proxy, args, result, Vector.empty, Vector.empty, Vector.empty, None)(info)
    ctx.defaultEncoding.pureFunction(func)(ctx)
  }

  private def closureGetter(func: in.FunctionMemberOrLitProxy)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = closureGetterFunctionProxy(func)
    val info = func.info
    val result = in.Parameter.Out(Names.closureArg, genericFuncType)(info)
    val satisfiesSpec = in.ClosureImplements(result, in.ClosureSpec(func, Map.empty)(info))(info)
    val (args, captAssertions) = capturedArgsAndAssertions(result, captured(ctx)(func), info)
    val getter = in.PureFunction(proxy, args, Vector(result), Vector.empty, Vector(satisfiesSpec) ++ captAssertions, Vector.empty, None)(memberOrLit(ctx)(func).info)
    ctx.defaultEncoding.pureFunction(getter)(ctx)
  }

  private def closureCallArgs(closure: in.Expr, args: Vector[in.Expr], spec: in.ClosureSpec)(ctx: Context): Vector[in.Expr] = {
    val capt = captured(ctx)(spec.func)
    val captArgs = (1 to capt.size).map(i => in.DomainFunctionCall(
      in.DomainFuncProxy(Names.closureCaptVarDomFunc(i), Names.closureDomain)(closure.info), Vector(closure), capt(i-1)._2.typ)(closure.info))
    val argsAndParams = {
      var argsUsed = 0
      (1 to (args.size + spec.params.size)).map(i => if (spec.params.contains(i)) spec.params(i) else { argsUsed += 1; args(argsUsed-1) })
    }
    Vector(closure) ++ captArgs ++ argsAndParams
  }

  private def capturedArgsAndAssertions(closure: in.Expr, captured: Vector[(in.Expr, in.Parameter.In)], info: Source.Parser.Info): (Vector[in.Parameter.In], Vector[in.Assertion]) = {
    val captArgs = captured.map(c => c._2)
    val capturesVar: Int => in.Assertion = i => in.ExprAssertion(in.EqCmp(in.DomainFunctionCall(
      in.DomainFuncProxy(Names.closureCaptVarDomFunc(i), Names.closureDomain)(info), Vector(closure), captArgs(i-1).typ)(info), captArgs(i-1))(info))(info)
    val assertions = (1 to captured.size).toVector map { i => capturesVar(i) }
    (captArgs, assertions)
  }

  private def specWithFuncArgs(spec: in.ClosureSpec, f: FunctionLikeMemberOrLit): in.ClosureSpec =
    in.ClosureSpec(spec.func, spec.params.map{ case (i, _) => i -> f.args(i-1)})(spec.info)

  private def callableMemberWithClosure(spec: in.ClosureSpec)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = closureCallProxy(spec)(spec.info)
    val lit = memberOrLit(ctx)(spec.func)
    val closurePar = in.Parameter.In(Names.closureArg, genericFuncType)(lit.info)
    val (captArgs, captAssertions) = capturedArgsAndAssertions(closurePar, captured(ctx)(spec.func), spec.func.info)
    val args = Vector(closurePar) ++ captArgs ++ lit.args
    val implementsAssertion = in.ClosureImplements(closurePar, specWithFuncArgs(spec, lit))(spec.info)
    val pres = Vector(implementsAssertion) ++ lit.pres ++ captAssertions
    lit match {
      case _: in.Function =>
        val func = in.Function(proxy, args, lit.results, pres, lit.posts, lit.terminationMeasures, None)(spec.info)
        ctx.defaultEncoding.function(func)(ctx)
      case lit: in.FunctionLit =>
        val body = if (spec.params.isEmpty) lit.body else None
        val func = in.Function(proxy, args, lit.results, pres, lit.posts, lit.terminationMeasures, body)(lit.info)
        val res = ctx.defaultEncoding.function(func)(ctx)
        res
      case _: in.PureFunction =>
        val func = in.PureFunction(proxy, args, lit.results, pres, lit.posts, lit.terminationMeasures, None)(spec.info)
        ctx.defaultEncoding.pureFunction(func)(ctx)
      case lit: in.PureFunctionLit =>
        val body = if (spec.params.isEmpty) lit.body else None
        val func = in.PureFunction(proxy, args, lit.results, pres, lit.posts, lit.terminationMeasures, body)(lit.info)
        ctx.defaultEncoding.pureFunction(func)(ctx)
    }
  }
}
