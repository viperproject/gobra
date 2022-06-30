package viper.gobra.translator.encodings.closures

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class ClosureEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  val specs = new ClosureSpecsManager
  val domain = new ClosureDomainManager(specs)
  val moe = new MethodObjectManager(domain)

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case in.FunctionT(_, _, addr) =>
      addr match {
        case Exclusive => domain.vprType
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes literal function expressions and calls to the default closure generator.
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expression(ctx)){

    case l: in.FunctionLikeLit =>
      val vprFunc = functionFromLiteral(l)(ctx)
      genMembers :+= vprFunc
      specs.callToClosureGetter(l.name, l.captured.map(_._1))(ctx)

    case f: in.FunctionObject =>
      specs.callToClosureGetter(f.func)(ctx)

    case m: in.MethodObject =>
      moe.callToMethodClosureGetter(m)(ctx)

    case c: in.PureCallWithSpec =>
      specs.pureClosureCall(c)(ctx)

    case e@in.DfltVal(_) :: ctx.Function(t) / Exclusive =>
      ctx.expression(in.PureFunctionCall(in.FunctionProxy(Names.closureDefaultFunc)(e.info), Vector.empty, t)(e.info))
  }

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = default(super.statement(ctx)) {
    case c: in.CallWithSpec =>
      specs.closureCall(c)(ctx)
  }

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = default(super.assertion(ctx)) {
    case a: in.ClosureImplements => specs.closureImplementsAssertion(a)(ctx)
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    genMembers foreach addMemberFn
    domain.finalize(addMemberFn)
    specs.finalize(addMemberFn)
    moe.finalize(addMemberFn)
  }

  private var genMembers: List[vpr.Member] = List.empty

  // Generates the encoding of the function literal as a separate Viper method or function
  private def functionFromLiteral(lit: in.FunctionLikeLit)(ctx: Context): vpr.Member = {
    val proxy = in.FunctionProxy(lit.name.name)(lit.info)
    val args = lit.captured.map(_._2) ++ lit.args
    lit match {
      case l: in.FunctionLit =>
        val func = in.Function(proxy, args, l.results, l.pres, l.posts, l.terminationMeasures, l.body)(lit.info)
        ctx.defaultEncoding.function(func)(ctx).res
      case l: in.PureFunctionLit =>
        val func = in.PureFunction(proxy, args, l.results, l.pres, l.posts, l.terminationMeasures, l.body)(lit.info)
        ctx.defaultEncoding.pureFunction(func)(ctx).res
    }
  }
}