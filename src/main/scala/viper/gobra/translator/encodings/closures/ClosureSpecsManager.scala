package viper.gobra.translator.encodings.closures

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.{PreconditionError, Source, SpecNotImplementedByClosure}
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeLevel.errorT
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.verifier.{errors => vprerr}
import viper.silver.{ast => vpr}

protected class ClosureSpecsManager {

  def closureImplementsAssertion(a: in.ClosureImplements)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(a.spec)(ctx, a.info)
    ctx.expression(in.PureFunctionCall(implementsFunctionProxy(a.spec)(a.info),
      Vector(a.closure) ++ a.spec.params.toVector.sortBy(_._1).map(_._2), in.BoolT(Addressability.rValue))(a.info))
  }

  def callToClosureGetter(func: in.FunctionMemberOrLitProxy, captured: Vector[in.Expr] = Vector.empty)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(in.ClosureSpec(func, Map.empty)(func.info))(ctx, func.info)
    ctx.expression(in.PureFunctionCall(closureGetterFunctionProxy(func), captured map { e => in.Ref(e)(e.info) }, genericFuncType)(func.info))
  }

  def closureCall(c: in.CallWithSpec)(ctx: Context): CodeWriter[vpr.Stmt] = {
    register(c.spec)(ctx, c.spec.info)

    def doesNotImplementSpecErr(call: vpr.MethodCall): ErrorTransformer = {
      case e@vprerr.PreconditionInCallFalse(Source(info), reason, _)
        if (e causedBy call) && (reason.readableMessage contains s"Assertion ${Names.closureImplementsFunc}") =>
        PreconditionError(info).dueTo(SpecNotImplementedByClosure(info, c.closure.info.tag, c.spec.info.tag))
    }

    for {
      call <- ctx.statement(in.FunctionCall(c.targets, closureCallProxy(c.spec)(c.info), closureCallArgs(c.closure, c.args, c.spec)(ctx))(c.info))
      _ <- errorT(doesNotImplementSpecErr(call.collect{ case m: vpr.MethodCall => m }.head))
    } yield call
  }

  def pureClosureCall(c: in.PureCallWithSpec)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(c.spec)(ctx, c.spec.info)

    def doesNotImplementSpecErr(call: vpr.Exp): ErrorTransformer = {
      case e@vprerr.PreconditionInAppFalse(Source(info), reason, _)
        if (e causedBy call) && (reason.readableMessage contains s"Assertion ${Names.closureImplementsFunc}") =>
        PreconditionError(info).dueTo(SpecNotImplementedByClosure(info, c.closure.info.tag, c.spec.info.tag))
    }

    for {
      exp <- ctx.expression(in.PureFunctionCall(closureCallProxy(c.spec)(c.info), closureCallArgs(c.closure, c.args, c.spec)(ctx), c.typ)(c.info))
      _ <- errorT(doesNotImplementSpecErr(exp))
    } yield exp
  }

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    genMembers foreach { m => addMemberFn(m.res) }
  }

  private def register(spec: in.ClosureSpec)(ctx: Context, info: Source.Parser.Info): Unit = {
    if (!specsSeen.contains((spec.func, spec.params.keySet))) {
      specsSeen += ((spec.func, spec.params.keySet))
      val implementsF = implementsFunction(spec)(ctx, info)
      val callable = callableMemberWithClosure(spec)(ctx)
      genMembers :+= implementsF
      genMembers :+= callable
    }
    if (!funcsUsedAsClosures.contains(spec.func)) {
      funcsUsedAsClosures += spec.func
      val getter = closureGetter(spec.func)(ctx)
      genMembers :+= getter
      maxCapturedVariables = Math.max(maxCapturedVariables, numCaptVars(spec.func, ctx))
    }
  }

  private def numCaptVars(func: in.FunctionMemberOrLitProxy, ctx: Context): Int = func match {
    case _: in.FunctionProxy => 0
    case p: in.FunctionLitProxy => ctx.table.lookup(p).captured.size
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
  private def implementsFunctionProxy(spec: in.ClosureSpec)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(s"${Names.closureImplementsFunc}$$${closureSpecName(spec)}")(info)
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
    val numCaptured = numCaptVars(func, ctx)
    val args = (1 to numCaptured).toVector map { i => in.Parameter.In(Names.closureCaptVar(i), genericPointerType)(info) }
    val result = in.Parameter.Out("closure", genericFuncType)(info)
    val satisfiesSpec = in.ClosureImplements(result, in.ClosureSpec(func, Map.empty)(info))(info)
    val capturesVar: Int => in.Assertion = i => in.ExprAssertion(in.EqCmp(in.DomainFunctionCall(
      in.DomainFuncProxy(Names.closureCaptDomFunc(i), Names.closureDomain)(info), Vector(result), genericPointerType)(info), args(i-1))(info))(info)
    val posts = Vector(satisfiesSpec) ++ ((1 to numCaptured) map { i => capturesVar(i) })
    val getter = in.PureFunction(proxy, args, Vector(result), Vector.empty, posts, Vector.empty, None)(info)
    ctx.defaultEncoding.pureFunction(getter)(ctx)
  }

  private def closureCallArgs(closure: in.Expr, args: Vector[in.Expr], spec: in.ClosureSpec)(ctx: Context): Vector[in.Expr] = {
    val numCaptured = spec.func match {
      case f: in.FunctionLitProxy => val lit = ctx.table.lookup(f)
        if (lit.captured.nonEmpty) lit.captured.size else 0
      case _ => 0
    }
    val captArgs = (1 to numCaptured).map(i => in.DomainFunctionCall(
      in.DomainFuncProxy(Names.closureCaptDomFunc(i), Names.closureDomain)(closure.info), Vector(closure), genericPointerType)(closure.info))

    val argsAndParams = {
      var argsUsed = 0
      (1 to (args.size + spec.params.size)).map(i => if (spec.params.contains(i)) spec.params(i) else { argsUsed += 1; args(argsUsed-1) })
    }
    Vector(closure) ++ captArgs ++ argsAndParams
  }

  private def callableMemberWithClosure(spec: in.ClosureSpec)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = closureCallProxy(spec)(spec.info)
    val (lit, captured) = spec.func match {
      case f: in.FunctionLitProxy => val lit = ctx.table.lookup(f)
        (lit, lit.captured)
      case f: in.FunctionProxy => (ctx.table.lookup(f).asInstanceOf[in.FunctionMember], Vector.empty)
    }
    val closurePar = in.Parameter.In(Names.closureArg, genericFuncType)(lit.info)
    val args = Vector(closurePar) ++ captured.map(_._2) ++ lit.args
    val pres = Vector(in.ClosureImplements(closurePar, spec)(spec.info)) ++ lit.pres
    lit match {
      case _: in.Function | _: in.FunctionLit =>
        val func = in.Function(proxy, args, lit.results, pres, lit.posts, lit.terminationMeasures, None)(lit.info)
        ctx.defaultEncoding.function(func)(ctx)
      case _: in.PureFunction | _: in.PureFunctionLit =>
        val func = in.PureFunction(proxy, args, lit.results, pres, lit.posts, lit.terminationMeasures, None)(lit.info)
        ctx.defaultEncoding.pureFunction(func)(ctx)
    }
  }
}
