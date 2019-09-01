package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class StatementsImpl extends Statements {

  var counter: Int = 0

  def count: Int = {counter += 1; counter}

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Stmt)(ctx: Context): CodeWriter[vpr.Stmt] = withDeepInfo(x){


    def goS(s: in.Stmt): CodeWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    val z = x match {
      case in.Block(decls, stmts) =>
        val (vDeclss, inits) = decls.map(ctx.loc.localDecl(_)(ctx)).unzip
        val vDecls = vDeclss.flatten
        block{
          for {
            _ <- global(vDecls: _*)
            vInits <- seqns(inits)
            vBody <- sequence(stmts map ctx.stmt.translateF(ctx))
          } yield vu.seqn(vInits +: vBody)
        }

      case in.Seqn(stmts) => seqns(stmts map goS)

      case in.If(cond, thn, els) =>
          for {
            c <- goE(cond)
            t <- seqn(goS(thn))
            e <- seqn(goS(els))
          } yield vpr.If(c, t, e)()

      case in.While(cond, invs, body) =>
        for {
          (cws, vCond) <- split(goE(cond))
          (iws, vInvs) = invs.map(ctx.ass.invariant(_)(ctx)).unzip
          cpre <- seqnUnit(cws)
          ipre <- seqnUnits(iws)
          vBody <- goS(body)

          cpost = vpr.If(vCond, cpre, vu.nop)()
          ipost = ipre

          wh = vu.seqn(Vector(
            cpre, ipre, vpr.While(vCond, vInvs, vu.seqn(Vector(vBody, cpost, ipost)))()
          ))
        } yield wh

      case ass: in.SingleAss => ctx.loc.assignment(ass)(ctx)
      case mk: in.Make => ctx.loc.make(mk)(ctx)

      case in.FunctionCall(targets, func, args) =>
        for {
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          vTargetss <- sequence(targets map (ctx.loc.target(_)(ctx)))
        } yield vpr.MethodCall(func.name, vArgss.flatten, vTargetss.flatten)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

      case in.MethodCall(targets, recv, meth, args, path) =>
        for {
          vRecvs <- ctx.loc.callReceiver(recv, path)(ctx)
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          vTargetss <- sequence(targets map (ctx.loc.target(_)(ctx)))
        } yield vpr.MethodCall(meth.uniqueName, vRecvs ++ vArgss.flatten, vTargetss.flatten)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

      case in.Assert(ass) => for {v <- goA(ass)} yield vpr.Assert(v)()
      case in.Assume(ass) => for {v <- goA(ass)} yield vpr.Assume(v)() // Assumes are later rewritten
      case in.Inhale(ass) => for {v <- goA(ass)} yield vpr.Inhale(v)()
      case in.Exhale(ass) => for {v <- goA(ass)} yield vpr.Exhale(v)()

      case fold: in.Fold => for {a <- ctx.loc.predicateAccess(fold.op)(ctx) } yield vpr.Fold(a)()
      case unfold: in.Unfold => for { a <- ctx.loc.predicateAccess(unfold.op)(ctx) } yield vpr.Unfold(a)()

      case in.Return() => unit(vpr.Goto(Names.returnLabel)())


    }
//    if (!x.isInstanceOf[in.Seqn]){
//      println(s"////////////////// INPUT ($count)/////////////////")
//      println(s"$x")
//      println("------------------ Output ----------------")
//      println(s"${z.res}")
//      println("//////////////////////////////////////////")
//    }

    z
  }


}
