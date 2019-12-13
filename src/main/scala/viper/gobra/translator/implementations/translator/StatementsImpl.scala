package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{Comments, PrimitiveGenerator, ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class StatementsImpl extends Statements {

  var counter: Int = 0

  def count: Int = {counter += 1; counter}

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = _havocFunction.finalize(col)

  private lazy val _havocFunction: PrimitiveGenerator.PrimitiveGenerator[vpr.Type, vpr.Function] =
    PrimitiveGenerator.simpleGenerator(
      (t: vpr.Type) => {
        val f = vpr.Function(Names.havocFunctions(t), Seq(), t, Seq(), Seq(), None)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)
        (f, Vector(f))
      }
    )

  private def havocFunction(t: in.Type)(ctx: Context) = _havocFunction(ctx.typ.translate(t)(ctx))

  override def translate(x: in.Stmt)(ctx: Context): CodeWriter[vpr.Stmt] = {

    val (pos, info, errT) = x.vprMeta


    def goS(s: in.Stmt): CodeWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    val vprStmt: CodeWriter[vpr.Stmt] = x match {
      case in.Block(decls, stmts) =>
        val (vDeclss, inits) = decls.map(ctx.loc.localDecl(_)(ctx)).unzip
        val vDecls = vDeclss.flatten
        block{
          for {
            _ <- global(vDecls: _*)
            vInits <- seqns(inits)
            vBody <- sequence(stmts map ctx.stmt.translateF(ctx))
          } yield vu.seqn(vInits +: vBody)(pos, info, errT)
        }

      case in.Seqn(stmts) => seqns(stmts map goS)

      case in.If(cond, thn, els) =>
          for {
            c <- goE(cond)
            t <- seqn(goS(thn))
            e <- seqn(goS(els))
          } yield vpr.If(c, t, e)(pos, info, errT)

      case in.While(cond, invs, body) =>

        for {
          (cws, vCond) <- split(goE(cond))
          (iws, vInvs) = invs.map(ctx.ass.invariant(_)(ctx)).unzip
          cpre <- seqnUnit(cws)
          ipre <- seqnUnits(iws)
          vBody <- goS(body)

          cpost = vpr.If(vCond, cpre, vu.nop(pos, info, errT))(pos, info, errT)
          ipost = ipre

          wh = vu.seqn(Vector(
            cpre, ipre, vpr.While(vCond, vInvs, vu.seqn(Vector(vBody, cpost, ipost))(pos, info, errT))(pos, info, errT)
          ))(pos, info, errT)
        } yield wh

      case ass: in.SingleAss => ctx.loc.assignment(ass)(ctx)
      case mk: in.Make => ctx.loc.make(mk)(ctx)

      case in.FunctionCall(targets, func, args) =>
        for {
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          vTargetss <- sequence(targets map (ctx.loc.target(_)(ctx)))
        } yield vpr.MethodCall(func.name, vArgss.flatten, vTargetss.flatten)(pos, info, errT)

      case in.MethodCall(targets, recv, meth, args) =>
        for {
          vRecvs <- ctx.loc.argument(recv)(ctx)
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          vTargetss <- sequence(targets map (ctx.loc.target(_)(ctx)))
        } yield vpr.MethodCall(meth.uniqueName, vRecvs ++ vArgss.flatten, vTargetss.flatten)(pos, info, errT)

      case in.Assert(ass) => for {v <- goA(ass)} yield vpr.Assert(v)(pos, info, errT)
      case in.Assume(ass) => for {v <- goA(ass)} yield vpr.Assume(v)(pos, info, errT) // Assumes are later rewritten
      case in.Inhale(ass) => for {v <- goA(ass)} yield vpr.Inhale(v)(pos, info, errT)
      case in.Exhale(ass) => for {v <- goA(ass)} yield vpr.Exhale(v)(pos, info, errT)

      case fold: in.Fold => for {a <- ctx.loc.predicateAccess(fold.op)(ctx) } yield vpr.Fold(a)(pos, info, errT)
      case unfold: in.Unfold => for { a <- ctx.loc.predicateAccess(unfold.op)(ctx) } yield vpr.Unfold(a)(pos, info, errT)

      case in.Havoc(exp) => ctx.loc.havoc(exp)(ctx)

      case in.Return() => unit(vpr.Goto(Names.returnLabel)(pos, info, errT))


    }

    vprStmt map (s => stmtComment(x, s))
  }

  def stmtComment(x: in.Stmt, res: vpr.Stmt): vpr.Stmt = {
    x match {
      case _: in.Seqn => res
      case _ => Comments.prependComment(x.formattedShort, res)
    }
  }


}
