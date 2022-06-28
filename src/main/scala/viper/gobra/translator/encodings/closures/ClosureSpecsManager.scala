package viper.gobra.translator.encodings.closures

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.{ast => vpr}

protected class ClosureSpecsManager {

  def closureImplementsAssertion(a: in.ClosureImplements)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(a.spec)(ctx, a.info)
    implementsFunctionCall(a.closure, a.spec)(ctx, a.info)
  }

  def callToClosureGetter(func: in.FunctionMemberOrLitProxy, captured: Vector[in.Expr] = Vector.empty)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(in.ClosureSpec(func, Map.empty)(func.info))(ctx, func.info)
    closureGetterFunctionCall(func, captured)(ctx)
  }

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    vprClosureGetterFunctions foreach { g => addMemberFn(g.res) }
    vprImplementsFunctions foreach { f => addMemberFn(f.res) }
  }

  private def register(spec: in.ClosureSpec)(ctx: Context, info: Source.Parser.Info): Unit = {
    if (!specsSeen.contains((spec.func, spec.params.keySet))) {
      specsSeen += ((spec.func, spec.params.keySet))
      vprImplementsFunctions :+= implementsFunction(spec)(ctx, info)
    }
    if (!funcsUsedAsClosures.contains(spec.func)) {
      funcsUsedAsClosures += spec.func
      vprClosureGetterFunctions :+= closureGetter(spec.func)(ctx)
      maxCapturedVariables = Math.max(maxCapturedVariables, numCaptVars(spec.func, ctx))
    }

  }

  private def numCaptVars(func: in.FunctionMemberOrLitProxy, ctx: Context): Int = func match {
    case _: in.FunctionProxy => 0
    case p: in.FunctionLitProxy => ctx.table.lookup(p).captured.size
  }

  private var maxCapturedVariables: Int = 0
  def maxCaptVariables: Int = maxCapturedVariables

  private var specsSeen: Set[(in.FunctionMemberOrLitProxy, Set[Int])] = Set.empty
  private var funcsUsedAsClosures: Set[in.FunctionMemberOrLitProxy] = Set.empty
  private var vprClosureGetterFunctions: Vector[MemberWriter[vpr.Member]] = Vector.empty
  private var vprImplementsFunctions: Vector[MemberWriter[vpr.Member]] = Vector.empty

  private val genericFuncType: in.FunctionT = in.FunctionT(Vector.empty, Vector.empty, Addressability.rValue)
  private val genericPointerType: in.PointerT = in.PointerT(in.BoolT(Addressability.Shared) ,Addressability.inParameter)

  private def closureSpecName(spec: in.ClosureSpec): String =  s"${spec.func}$$${spec.params.keySet.toSeq.sorted.mkString("_")}"
  private def implementsFunctionProxy(spec: in.ClosureSpec)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(s"${Names.closureImplementsFunc}$$${closureSpecName(spec)}")(info)

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

  private def implementsFunctionCall(closure: in.Expr, spec: in.ClosureSpec)(ctx: Context, info: Source.Parser.Info): CodeWriter[vpr.Exp] = {
    val proxy = implementsFunctionProxy(spec)(info)
    val funcCall = in.PureFunctionCall(proxy, Vector(closure) ++ spec.params.toVector.sortBy(_._1).map(_._2), in.BoolT(Addressability.rValue))(info)
    ctx.expression(funcCall)
  }

  private def closureGetterFunctionProxy(func: in.FunctionMemberOrLitProxy): in.FunctionProxy = in.FunctionProxy(s"${Names.closureGetter}$$$func")(func.info)
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

  private def closureGetterFunctionCall(func: in.FunctionMemberOrLitProxy, captured: Vector[in.Expr])(ctx: Context): CodeWriter[vpr.Exp] =
    ctx.expression(in.PureFunctionCall(closureGetterFunctionProxy(func), captured map { e => in.Ref(e)(e.info) }, genericFuncType)(func.info))
}
