package viper.gobra.translator.encodings.closures

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeLevel.unit
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.ast.{ErrorTrafo, Info, Member, Position}
import viper.silver.{ast => vpr}

protected class ClosureSpecsManager {

  def register(spec: in.ClosureSpecProxy)(ctx: Context, info: Source.Parser.Info): Unit = {
    if (!specsSeen.contains(spec)) vprImplementsFunctions :+= implementsFunction(spec)(ctx, info)
    specsSeen += spec
  }

  def register(lit: in.FunctionLikeLit)(ctx: Context): Unit =
    register(in.ClosureSpecProxy(lit.name, Vector.empty, lit.captured.size)(lit.info))(ctx, lit.info)

  def closureImplementsAssertion(closure: in.Expr, spec: in.ClosureSpecProxy)(ctx: Context, info: Source.Parser.Info): CodeWriter[vpr.Exp] = {
    register(spec)(ctx, info)
    implementsFunctionCall(closure, spec)(ctx, info)
  }

  def finalize(addMemberFn: Member => Unit): Unit = {
    vprImplementsFunctions foreach { f => addMemberFn(f.res) }
  }

  def maxCaptVariables: Int = if (specsSeen.isEmpty) 0 else specsSeen.map(_.numCaptured).max

  private var specsSeen: Set[in.ClosureSpecProxy] = Set.empty
  private var vprImplementsFunctions: Vector[MemberWriter[vpr.Member]] = Vector.empty

  private val genericFuncType: in.FunctionT = in.FunctionT(Vector.empty, Vector.empty, Addressability.rValue)
  private val genericPointerType: in.PointerT = in.PointerT(in.BoolT(Addressability.Shared) ,Addressability.inParameter)

  private def closureSpecName(spec: in.ClosureSpecProxy): String =  s"${spec.funcName}$$${spec.params.map(_._1).mkString("_")}"
  private def implementsFunctionName(spec: in.ClosureSpecProxy): String = s"${Names.closureImplementsFunc}$$${closureSpecName(spec)}"
  private def implementsFunctionProxy(spec: in.ClosureSpecProxy)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(implementsFunctionName(spec))(info)

  private def domainFuncName(i: Int): String = s"${Names.closureCaptDomainFunc(i)}${Names.closureDomain}"
  private def captVarAccess(closure: vpr.Exp, i: Int)(pos: Position, info: Info, errT: ErrorTrafo) =
    vpr.DomainFuncApp(domainFuncName(i), Seq(closure), Map.empty)(pos, info, vpr.Ref, Names.closureDomain, errT)

  // Generates encoding: function closureImplements_funcName_(closure, parameters) bool
  private def implementsFunction(spec: in.ClosureSpecProxy)(ctx: Context, info: Source.Parser.Info): MemberWriter[vpr.Member] = {
    val proxy = implementsFunctionProxy(spec)(info)
    val closurePar = in.Parameter.In(Names.closureArg, genericFuncType)(info)
    val params = spec.params.map(p => in.Parameter.In(Names.closureImplementsParam(p._1), p._2.typ)(p._2.info))
    val captured = (1 to spec.numCaptured).map(idx => in.Parameter.In(Names.closureCaptDomainFunc(idx), genericPointerType)(info))
    val args = Vector(closurePar) ++ params ++ captured
    val result = Vector(in.Parameter.Out("r", in.BoolT(Addressability.outParameter))(info))
    val func = in.PureFunction(proxy, args, result, Vector.empty, Vector.empty, Vector.empty, None)(info)
    ctx.defaultEncoding.pureFunction(func)(ctx)
  }

  private def implementsFunctionCall(closure: in.Expr, spec: in.ClosureSpecProxy)(ctx: Context, info: Source.Parser.Info): CodeWriter[vpr.Exp] = {
    if (spec.numCaptured == 0) {
      val proxy = implementsFunctionProxy(spec)(info)
      val funcCall = in.PureFunctionCall(proxy, Vector(closure) ++ spec.params.map(p => p._2), in.BoolT(Addressability.rValue))(info)
      ctx.expression(funcCall)
    } else {
      val vprClosure = ctx.expression(closure).res
      val (pos, info, errT) = spec.vprMeta
      val argsAsVars = (1 to spec.numCaptured) map { i => captVarAccess(vprClosure, i)(pos, info, errT) }
      unit(vpr.FuncApp(implementsFunctionName(spec), Vector(vprClosure) ++ argsAsVars)(pos, info, vpr.Bool, errT))
    }
  }
}
