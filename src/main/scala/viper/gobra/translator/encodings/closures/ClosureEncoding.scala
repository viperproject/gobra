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
import viper.silver.ast.Member
import viper.silver.{ast => vpr}

class ClosureEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case f@in.FunctionT(_, _, addr) =>
      addr match {
        case Exclusive => domainManager(f).vprType
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes literal function expressions as TODO .
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expression(ctx)){

    case (l: in.FunctionLikeLit) :: ctx.Function(t) =>
      val d = domainManager(t, l.captured.size)
      val (captExpr, captParam) = l.captured.unzip
      val newMembers = List(implementsFunction(l.name, t, captParam)(ctx, l.info), functionFromLiteral(l)(ctx), funcLitGetter(l, d))
      genMembers ++= newMembers
      // return call funcLitGetter(&c1, &c2, ...) where c1, c2... are the captured variables
      ctx.expression(in.PureFunctionCall(
        in.FunctionProxy(funcLitGetterName(l.name))(l.info), captExpr map { e => in.Ref(e)(l.info) }, l.typ)(l.info))

    case e@in.DfltVal(_) :: ctx.Function(t) / Exclusive =>
      val (pos, info, errT) = e.vprMeta
      unit(domainManager(t).dfltGetter(pos, info, errT))
  }

  override def finalize(addMemberFn: Member => Unit): Unit = {
    domainManager.finalize(addMemberFn)
    genMembers foreach addMemberFn
  }

  private val domainManager: ClosureDomainManager = new ClosureDomainManager
  private var genMembers: List[vpr.Member] = List.empty

  // Generates encoding: function closureImplements_funcName_(closure, parameters) bool
  private def implementsFunction(funcName: String, funcTyp: in.Type, params: Vector[in.Parameter.In])(ctx: Context, info: Source.Parser.Info): vpr.Member = {
    val proxy = implementsFunctionProxy(funcName)(info)
    val closurePar = in.Parameter.In(Names.closureArg, funcTyp)(info)
    val args = Vector(closurePar) ++ params
    val result = Vector(in.Parameter.Out("r", in.BoolT(Addressability.outParameter))(info))
    val func = in.PureFunction(proxy, args, result, Vector.empty, Vector.empty, Vector.empty, None)(info)
    ctx.defaultEncoding.pureFunction(func)(ctx).res
  }
  private def implementsFunctionName(funcName: String): String = s"${Names.closureImplementsFunc}$$$funcName"
  private def implementsFunctionProxy(funcName: String)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(implementsFunctionName(funcName))(info)

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

  /* **
   * Generates encoding:
   *   function closureGet__litName_(pointers to capturedVars): Closure_litType_
   *   ensures result implements _litName_
   *   for i-th captured var c: ensures capt_i__litType_(result) == c
   */
  private def funcLitGetter(l: in.FunctionLikeLit, d: ClosureDomain): vpr.Member = {
    val args = (1 to l.captured.size) map { i => vpr.LocalVarDecl(Names.closureCaptDomainFunc(i), vpr.Ref)() }
    val argsAsVars = (1 to l.captured.size) map { i => vpr.LocalVar(Names.closureCaptDomainFunc(i), vpr.Ref)() }
    val (pos, info, errT) = l.vprMeta
    val capturesVar: Int => vpr.EqCmp = i => vpr.EqCmp(
      vpr.DomainFuncApp(d.domainFuncName(i), Seq(vpr.Result(d.vprType)()), Map.empty)(pos, info, vpr.Ref, d.domName, errT),
      argsAsVars(i-1))()
    val satisfiesSpec: vpr.EqCmp = vpr.EqCmp(
      vpr.FuncApp(implementsFunctionName(l.name), Vector(vpr.Result(d.vprType)()) ++ argsAsVars)(pos, info, vpr.Bool, errT),
      vpr.BoolLit(b=true)())()
    val posts = ((1 to l.captured.size) map { i => capturesVar(i) }) ++ Vector(satisfiesSpec)
    vpr.Function(funcLitGetterName(l.name), args, d.vprType, Seq.empty, posts, None)(pos, info, errT)
  }
  private def funcLitGetterName(funcName: String): String = s"${Names.funcLitGetter}_$funcName"
}