package viper.gobra.translator.encodings.closures

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.ast.{ErrorTrafo, Exp, Info, Member, Position}
import viper.silver.{ast => vpr}

class ClosureEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  val specs = new ClosureSpecsManager

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case in.FunctionT(_, _, addr) =>
      addr match {
        case Exclusive => vprClosureDomainType
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes literal function expressions as TODO .
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expression(ctx)){

    case l: in.FunctionLikeLit =>
      specs.register(l)(ctx)
      val (captExpr, _) = l.captured.unzip
      val newMembers = List(functionFromLiteral(l)(ctx), funcLitGetter(l))
      genMembers ++= newMembers
      // return call funcLitGetter(&c1, &c2, ...) where c1, c2... are the captured variables
      ctx.expression(in.PureFunctionCall(
        in.FunctionProxy(funcLitGetterName(l.name))(l.info), captExpr map { e => in.Ref(e)(l.info) }, l.typ)(l.info))

    case e@in.DfltVal(_) :: ctx.Function(_) / Exclusive =>
      val (pos, info, errT) = e.vprMeta
      unit(vpr.FuncApp(Names.closureDefaultFunc, Seq.empty)(pos, info, vprClosureDomainType, errT))
  }

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[Exp] = default(super.assertion(ctx)) {
    case a@in.ClosureImplements(closure, spec) =>
      specs.closureImplementsAssertion(closure, spec)(ctx, a.info)
  }

  override def finalize(addMemberFn: Member => Unit): Unit = {
    if (closureDomainNeeded) {
      addMemberFn(vprDomain)
      addMemberFn(dfltFunction)
    }
    genMembers foreach addMemberFn
    specs.finalize(addMemberFn)
  }

  private var genMembers: List[vpr.Member] = List.empty

  private def closureSpecName(spec: in.ClosureSpecProxy): String =  s"${spec.funcName}$$${spec.params.map(_._1).mkString("_")}"
  private def implementsFunctionName(spec: in.ClosureSpecProxy): String = s"${Names.closureImplementsFunc}$$${closureSpecName(spec)}"
  private def implementsFunctionName(funcName: String)(info: Source.Parser.Info): String = implementsFunctionName(in.ClosureSpecProxy(funcName, Vector.empty, 0)(info))
  private def implementsFunctionProxy(spec: in.ClosureSpecProxy)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(implementsFunctionName(spec))(info)
  private def implementsFunctionProxy(funcName: String)(info: Source.Parser.Info): in.FunctionProxy = implementsFunctionProxy(in.ClosureSpecProxy(funcName, Vector.empty, 0)(info))(info)

  // Generates the encoding of the function literal as a separate Viper method or function
  private def functionFromLiteral(lit: in.FunctionLikeLit)(ctx: Context): vpr.Member = {
    val proxy = in.FunctionProxy(lit.name)(lit.info)
    val closurePar = in.Parameter.In(Names.closureArg, lit.typ)(lit.info)
    val args = Vector(closurePar) ++ lit.captured.map(_._2) ++ lit.args
    val satisfiesFuncArgs = Vector(closurePar) ++ lit.captured.map(_._2)
    val pres = Vector(in.ExprAssertion(
      in.EqCmp(in.PureFunctionCall(implementsFunctionProxy(lit.name)(lit.info), satisfiesFuncArgs, in.BoolT(Addressability.outParameter))(lit.info),
        in.BoolLit(b=true)(lit.info))(lit.info))(lit.info)) ++ lit.pres
    lit match {
      case l: in.FunctionLit =>
        val func = in.Function(proxy, args, l.results, pres, l.posts, l.terminationMeasures, l.body)(lit.info)
        ctx.defaultEncoding.function(func)(ctx).res
      case l: in.PureFunctionLit =>
        val func = in.PureFunction(proxy, args, l.results, pres, l.posts, l.terminationMeasures, l.body)(lit.info)
        ctx.defaultEncoding.pureFunction(func)(ctx).res
    }
  }

  private def domainFuncName(i: Int): String = s"${Names.closureCaptDomainFunc(i)}${Names.closureDomain}"
  private def captVarAccess(closure: vpr.Exp, i: Int)(pos: Position, info: Info, errT: ErrorTrafo) =
    vpr.DomainFuncApp(domainFuncName(i), Seq(closure), Map.empty)(pos, info, vpr.Ref, Names.closureDomain, errT)

  private var closureDomainNeeded: Boolean = false
  private lazy val vprClosureDomainType: vpr.DomainType = {
    closureDomainNeeded = true
    vpr.DomainType(Names.closureDomain, Map.empty)(Vector.empty)
  }
  private def vprDomain: vpr.Domain = vpr.Domain(
    Names.closureDomain, (1 to specs.maxCaptVariables) map
      { i => vpr.DomainFunc(domainFuncName(i), Seq(vpr.LocalVarDecl(Names.closureArg, vprClosureDomainType)()), vpr.Ref)(domainName = Names.closureDomain)},
    Seq.empty, Seq.empty)()

  private val dfltFunction: vpr.Function = vpr.Function(Names.closureDefaultFunc, Seq.empty, vprClosureDomainType, Seq.empty, Seq.empty, None)()

  /* **
   * Generates encoding:
   *   function closureGet__litName_(pointers to capturedVars): Closure_litType_
   *   ensures result implements _litName_
   *   for i-th captured var c: ensures capt_i__litType_(result) == c
   */
  private def funcLitGetter(l: in.FunctionLikeLit): vpr.Member = {
    val args = (1 to l.captured.size) map { i => vpr.LocalVarDecl(Names.closureCaptDomainFunc(i), vpr.Ref)() }
    val argsAsVars = (1 to l.captured.size) map { i => vpr.LocalVar(Names.closureCaptDomainFunc(i), vpr.Ref)() }
    val (pos, info, errT) = l.vprMeta
    val capturesVar: Int => vpr.EqCmp = i => vpr.EqCmp(
      captVarAccess(vpr.Result(vprClosureDomainType)(), i)(pos, info, errT),
      argsAsVars(i-1))()
    val satisfiesSpec: vpr.EqCmp = vpr.EqCmp(
      vpr.FuncApp(implementsFunctionName(l.name)(l.info), Vector(vpr.Result(vprClosureDomainType)()) ++ argsAsVars)(pos, info, vpr.Bool, errT),
      vpr.BoolLit(b=true)())()
    val posts = ((1 to l.captured.size) map { i => capturesVar(i) }) ++ Vector(satisfiesSpec)
    vpr.Function(funcLitGetterName(l.name), args, vprClosureDomainType, Seq.empty, posts, None)(pos, info, errT)
  }
  private def funcLitGetterName(funcName: String): String = s"${Names.funcLitGetter}_$funcName"

}