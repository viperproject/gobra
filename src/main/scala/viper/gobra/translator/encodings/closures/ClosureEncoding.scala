package viper.gobra.translator.encodings.closures

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.encodings.interfaces.InterfaceEncoding
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.ast.{Exp, Member}
import viper.silver.{ast => vpr}

class ClosureEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  val specs = new ClosureSpecsManager
  val domain = new ClosureDomainManager(specs)
  val moe = new MethodObjectEncoder(domain)

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

    case e@in.DfltVal(_) :: ctx.Function(t) / Exclusive =>
      ctx.expression(in.PureFunctionCall(in.FunctionProxy(Names.closureDefaultFunc)(e.info), Vector.empty, t)(e.info))
  }

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[Exp] = default(super.assertion(ctx)) {
    case a: in.ClosureImplements => specs.closureImplementsAssertion(a)(ctx)
  }

  override def finalize(addMemberFn: Member => Unit): Unit = {
    domain.finalize(addMemberFn)
    specs.finalize(addMemberFn)
    moe.finalize(addMemberFn)
    genMembers foreach addMemberFn
  }

  private var genMembers: List[vpr.Member] = List.empty

  // Generates the encoding of the function literal as a separate Viper method or function
  private def functionFromLiteral(lit: in.FunctionLikeLit)(ctx: Context): vpr.Member = {
    val proxy = in.FunctionProxy(lit.name.name)(lit.info)
    val closurePar = in.Parameter.In(Names.closureArg, lit.typ)(lit.info)
    val args = Vector(closurePar) ++ lit.captured.map(_._2) ++ lit.args
    val pres = Vector(in.ClosureImplements(closurePar, in.ClosureSpec(lit.name, Map.empty)(lit.info))(lit.info)) ++ lit.pres
    lit match {
      case l: in.FunctionLit =>
        val func = in.Function(proxy, args, l.results, pres, l.posts, l.terminationMeasures, l.body)(lit.info)
        ctx.defaultEncoding.function(func)(ctx).res
      case l: in.PureFunctionLit =>
        val func = in.PureFunction(proxy, args, l.results, pres, l.posts, l.terminationMeasures, l.body)(lit.info)
        ctx.defaultEncoding.pureFunction(func)(ctx).res
    }
  }
}