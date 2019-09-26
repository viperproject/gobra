package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Expressions
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class ExpressionsImpl extends Expressions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = {

  }

  override def translate(x: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = withDeepInfo(x){

    def goE(e: in.Expr): CodeWriter[vpr.Exp] = translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    x match {

      case unfold: in.Unfolding =>
        for {
          a <- ctx.loc.predicateAccess(unfold.op)(ctx)
          e <- goE(unfold.in)
        } yield vpr.Unfolding(a, e)()

      case in.PureFunctionCall(func, args, typ) =>
        val arity = ctx.loc.arity(typ)(ctx)
        val resultType = ctx.loc.ttype(typ)(ctx)
        for {
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          app = vpr.FuncApp(func.name, vArgss.flatten)(vpr.NoPosition, vpr.NoInfo, resultType, vpr.NoTrafos)
          res <- if (arity == 1) unit(app) else {
            copyResult(app) flatMap (z => ctx.loc.copyFromTuple(z, typ)(ctx))
          }
        } yield res

      case in.PureMethodCall(recv, meth, args, path, typ) =>
        val arity = ctx.loc.arity(typ)(ctx)
        val resultType = ctx.loc.ttype(typ)(ctx)
        for {
          vRecvs <- ctx.loc.callReceiver(recv, path)(ctx)
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          app = vpr.FuncApp(meth.uniqueName, vRecvs ++ vArgss.flatten)(vpr.NoPosition, vpr.NoInfo, resultType, vpr.NoTrafos)
          res <- if (arity == 1) unit(app) else {
            copyResult(app) flatMap (z => ctx.loc.copyFromTuple(z, typ)(ctx))
          }
        } yield res

      case in.DfltVal(t) => ctx.loc.defaultValue(t)(ctx)
      case in.Tuple(args) => Violation.violation("Tuples expressions are not supported at this point in time")
      case p: in.Deref => ctx.loc.evalue(p)(ctx)
      case f: in.FieldRef => ctx.loc.evalue(f)(ctx)
      case r: in.Ref => ctx.loc.evalue(r)(ctx)

      case in.Negation(op) => for{o <- goE(op)} yield vpr.Not(o)()

      case in.EqCmp(l, r) => ctx.loc.equal(l, r)(ctx)
      case in.UneqCmp(l, r) => ctx.loc.equal(l, r)(ctx).map(vpr.Not(_)())
      case in.LessCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LtCmp(vl, vr)()
      case in.AtMostCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LeCmp(vl, vr)()
      case in.GreaterCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GtCmp(vl, vr)()
      case in.AtLeastCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GeCmp(vl, vr)()

      case in.And(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.And(vl, vr)()
      case in.Or(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Or(vl, vr)()

      case in.Add(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Add(vl, vr)()
      case in.Sub(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Sub(vl, vr)()
      case in.Mul(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mul(vl, vr)()
      case in.Mod(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mod(vl, vr)()
      case in.Div(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Div(vl, vr)()
      case in.Old(op) => for { o <- goE(op) } yield vpr.Old(o)()

      case l: in.Lit => ctx.loc.literal(l)(ctx)
      case v: in.Var => ctx.loc.evalue(v)(ctx)
    }
  }






}
