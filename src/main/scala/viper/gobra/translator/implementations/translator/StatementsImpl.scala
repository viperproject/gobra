package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.{ExprWriter, StmtWriter}
import viper.silver.{ast => vpr}

class StatementsImpl extends Statements {

  var counter: Int = 0

  def count: Int = {counter += 1; counter}

  import viper.gobra.translator.util.ViperWriter.StmtLevel._
  import viper.gobra.translator.util.ViperWriter.{ExprLevel => el}

  override def finalize(col: Collector): Unit = ()

  override def translate(x: in.Stmt)(ctx: Context): StmtWriter[vpr.Stmt] = withDeepInfo(x){


    def goS(s: in.Stmt): StmtWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): ExprWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): ExprWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    val z = x match {
      case in.Block(decls, stmts) => block{
        for {
          (declsWithPre, nextCtx) <- sequenceC(ctx)(decls map ctx.loc.bottomDecl)
          (decls, pre) = declsWithPre.unzip
          body <- sequence(stmts map ctx.stmt.translateF(nextCtx))
        } yield vpr.Seqn(pre ++ body, decls)()
      }

      case in.Seqn(stmts) => sequence(stmts map goS) map (vpr.Seqn(_, Vector.empty)())

      case in.If(cond, thn, els) => seqnE(
          for {
            c <- goE(cond)
            t <- el.exprS(goS(thn))
            e <- el.exprS(goS(els))
          } yield vpr.If(c, vpr.Seqn(Vector(t), Vector.empty)(), vpr.Seqn(Vector(e), Vector.empty)())()
        )

      case in.While(cond, invs, body) => seqnE(
        for {
          (vCond, vCondStmts) <- el.splitWrittenStmts(goE(cond))
          (vInvs, vInvStmts) <- el.splitWrittenStmts(el.sequence(invs map goA))
          vBody <- el.exprS(goS(body))

          wh = vpr.Seqn(
            vCondStmts ++ vInvStmts ++ Vector(
              vpr.While(vCond, vInvs, vpr.Seqn(Vector(vBody) ++ vCondStmts ++ vInvStmts, Vector.empty)())()
            ),
            Vector.empty
          )()
        } yield wh
      )

      case ass: in.SingleAss => ctx.loc.assignment(ass)(ctx)
      case mk: in.Make => ctx.loc.make(mk)(ctx)

      case in.FunctionCall(targets, func, args) =>
        seqnE(for {
          vArgs <- el.sequence(args map goE)
          vTargets <- el.sequence(targets map (ctx.loc.variable(_)(ctx)))
        } yield vpr.MethodCall(func.name, vArgs, vTargets)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos))

      case in.MethodCall(targets, recv, meth, args, path) =>
        seqnE(for {
          vRecv <- ctx.loc.callReceiver(recv, path)(ctx)
          vArgs <- el.sequence(args map goE)
          vTargets <- el.sequence(targets map (ctx.loc.variable(_)(ctx)))
        } yield vpr.MethodCall(meth.uniqueName, vRecv +: vArgs, vTargets)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos))

      case in.Assert(ass) => seqnE(for {v <- goA(ass)} yield vpr.Assert(v)())
      case in.Assume(ass) => seqnE(for {v <- goA(ass)} yield vpr.Assume(v)()) // Assumes are later rewritten
      case in.Inhale(ass) => seqnE(for {v <- goA(ass)} yield vpr.Inhale(v)())
      case in.Exhale(ass) => seqnE(for {v <- goA(ass)} yield vpr.Exhale(v)())

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
