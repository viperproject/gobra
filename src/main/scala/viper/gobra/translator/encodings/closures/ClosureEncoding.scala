package viper.gobra.translator.encodings.closures

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.Names.{funcLitGetter, serializeTypeIgnoringAddr}
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation.violation
import viper.silver.ast.{ErrorTrafo, Info, Member, Position}
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
        case Exclusive =>
          closureDomainManager.registerType(f)
          closureDomainManager.vprType(f)
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes literal function expressions as TODO .
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expression(ctx)){

    case (l: in.FunctionLikeLit) :: ctx.Function(t) =>
      closureDomainManager.registerType(t, l.captured.size)
      val (captExpr, captParam) = l.captured.unzip
      genMembers ::= satisfiesFunction(l.name, t, captParam)(ctx, l.info)
      genMembers ::= functionFromLiteral(l)(ctx)
      genMembers ::= closureDomainManager.funcLitGetter(l, t)
      val callToGetter = in.PureFunctionCall(in.FunctionProxy(funcLitGetterName(l.name))(l.info), captExpr map { e => in.Ref(e)(l.info) }, l.typ)(l.info)
      ctx.expression(callToGetter)

    case e@in.DfltVal(_) :: ctx.Function(t) / Exclusive =>
      val (pos, info, errT) = e.vprMeta
      unit(closureDomainManager.dflt(t)(pos, info, errT))
  }

  private def satisfiesFunctionName(funcName: String): String = s"${Names.satisfiesFunc}$$${funcName}"
  private def satisfiesFunctionProxy(funcName: String)(info: Source.Parser.Info): in.FunctionProxy = in.FunctionProxy(satisfiesFunctionName(funcName))(info)
  private def satisfiesFunction(funcName: String, funcTyp: in.Type, params: Vector[in.Parameter.In])(ctx: Context, info: Source.Parser.Info): vpr.Member = {
    val proxy = satisfiesFunctionProxy(funcName)(info)
    val closurePar = in.Parameter.In("c_CLOSURE", funcTyp)(info)
    val args = Vector(closurePar) ++ params
    val result = Vector(in.Parameter.Out("r_SAT", in.BoolT(Addressability.outParameter))(info))
    val func = in.PureFunction(proxy, args, result, Vector.empty, Vector.empty, Vector.empty, None)(info)
    ctx.defaultEncoding.pureFunction(func)(ctx).res
  }

  private def functionFromLiteral(lit: in.FunctionLikeLit)(ctx: Context): vpr.Member = {
    val proxy = in.FunctionProxy(lit.name)(lit.info)
    val closurePar = in.Parameter.In("c_CLOSURE", lit.typ)(lit.info)
    val args = Vector(closurePar) ++ lit.captured.map(_._2) ++ lit.args
    val satisfiesFuncArgs = Vector(closurePar) ++ lit.captured.map(_._2)
    val pres = Vector(in.ExprAssertion(
      in.EqCmp(in.PureFunctionCall(satisfiesFunctionProxy(lit.name)(lit.info), satisfiesFuncArgs, in.BoolT(Addressability.outParameter))(lit.info),
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

  def funcLitGetterName(funcName: String): String = s"${Names.funcLitGetter}_$funcName"

  override def finalize(addMemberFn: Member => Unit): Unit = {
    closureDomainManager.finalize(addMemberFn)
    genMembers foreach addMemberFn
  }

  private var genMembers: List[vpr.Member] = List.empty

  private object closureDomainManager {
    // Map from a closure type name to the maximum number of variables captured by closures of this type
    private var closureTypeNamesWithInfo: Map[String, Int] = Map.empty

    private def name(t: in.FunctionT): String = serializeTypeIgnoringAddr(t)
    private def domainName(n: String): String = s"${Names.closureDomain}$n"
    private def domainFuncName(n: String, i: Int): String = s"capt${i}_${domainName(n)}"
    private def dfltFuncName(n: String): String = s"${Names.closureDefaultFunc}_$n"

    def vprType(t: in.FunctionT): vpr.DomainType = vprType(name(t))
    def dflt(t: in.FunctionT)(pos: Position, info: Info, errT: ErrorTrafo): vpr.FuncApp = vpr.FuncApp(dfltFuncName(name(t)), Seq.empty)(pos, info, vprType(t), errT)
    def funcLitGetter(l: in.FunctionLikeLit, t: in.FunctionT): vpr.Member = {
      val typName = name(t)
      val dn = domainName(typName)
      val typ = vprType(typName)
      val args = (1 to l.captured.size) map { i => vpr.LocalVarDecl(s"capt$i", vpr.Ref)() }
      val argsAsVars = (1 to l.captured.size) map { i => vpr.LocalVar(s"capt$i", vpr.Ref)() }
      val (pos, info, errT) = l.vprMeta
      val capturesVar: Int => vpr.EqCmp = i =>
        vpr.EqCmp(vpr.DomainFuncApp(domainFuncName(typName, i), Seq(vpr.Result(typ)()), Map.empty)(pos, info, vpr.Ref, dn, errT), argsAsVars(i-1))()
      val satisfiesSpec: vpr.EqCmp = vpr.EqCmp(vpr.FuncApp(satisfiesFunctionName(l.name), Vector(vpr.Result(typ)()) ++ argsAsVars)(pos, info, vpr.Bool, errT), vpr.BoolLit(b=true)())()
      val posts = ((1 to l.captured.size) map { i => capturesVar(i) }) ++ Vector(satisfiesSpec)
      vpr.Function(funcLitGetterName(l.name), args, typ, Seq.empty, posts, None)(pos, info, errT)
    }

    private def vprType(n: String): vpr.DomainType = vpr.DomainType(domainName(n), Map.empty)(Vector.empty)
    private def vprDomain(n: String): vpr.Domain = vpr.Domain(
      domainName(n), (1 to closureTypeNamesWithInfo(n)) map
        { i => vpr.DomainFunc(domainFuncName(n, i), Seq(vpr.LocalVarDecl("c", vprType(n))()), vpr.Ref)(domainName = domainName(n))},
      Seq.empty, Seq.empty)()
    private def dfltFunc(n: String): vpr.Function = vpr.Function(dfltFuncName(n), Seq.empty, vprType(n), Seq.empty, Seq.empty, None)()

    /* **
     * Registers that a closure with this type is in the program, with nc captured variables.
     */
    def registerType(t: in.FunctionT, nc: Int = 0): Unit = {
      val n = name(t)
      closureTypeNamesWithInfo += n -> Math.max(nc, closureTypeNamesWithInfo.getOrElse(n, 0))
    }

    def finalize(addMemberFn: Member => Unit): Unit = {
      closureTypeNamesWithInfo.keys foreach { d =>
        addMemberFn(vprDomain(d))
        addMemberFn(dfltFunc(d))
      }
    }
  }
}