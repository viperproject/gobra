package viper.gobra.translator.encodings.closures

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.Assignee
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.silver.{ast => vpr}

class ClosureEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}

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

    case p: in.SpecImplementationProof =>
      specImplementationProof(p)(ctx)
  }

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = default(super.assertion(ctx)) {
    case a: in.ClosureImplements => specs.closureImplementsAssertion(a)(ctx)
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    domain.finalize(addMemberFn)
    specs.finalize(addMemberFn)
    moe.finalize(addMemberFn)
  }

  private def specImplementationProof(proof: in.SpecImplementationProof)(ctx: Context): CodeWriter[vpr.Stmt] = {
    val inhalePres = cl.seqns(proof.pres map (a => for {
          ass <- ctx.assertion(a)
        } yield vpr.Inhale(ass)(a.vprMeta._1, a.vprMeta._2, a.vprMeta._3)))
    val exhalePosts = cl.seqns(proof.posts map (a => for {
      ass <- ctx.assertion(a)
    } yield vpr.Exhale(ass)(a.vprMeta._1, a.vprMeta._2, a.vprMeta._3)))

    val (pos, info, errT) = proof.vprMeta

    for {
      ndBoolTrue <- ctx.assertion(in.ExprAssertion(proof.ndBool)(proof.info))
      ifStmt <- for {
        whileStmt <- for {
          inhalePres <- inhalePres
          body <- ctx.statement(proof.body)
          exhalePosts <- exhalePosts
          whileBody = vu.seqn(Vector(inhalePres, body, exhalePosts))(pos, info, errT)
        } yield vpr.While(ndBoolTrue, Seq.empty, whileBody)(pos, info, errT)
        assumeFalse = vpr.Assume(vpr.FalseLit()())()
        ifThen = vu.seqn(Vector(whileStmt, assumeFalse))(pos, info, errT)
        ifElse = vu.nop(pos, info, errT)
      } yield vpr.If(ndBoolTrue, ifThen, ifElse)(pos, info, errT)
      implementsAssertion <- ctx.assertion(in.ClosureImplements(proof.closure, proof.spec)(proof.info))
      assumeImplements = vpr.Assume(implementsAssertion)()
    } yield vu.seqn(Vector(ifStmt, assumeImplements))(pos, info, errT)
  }
}