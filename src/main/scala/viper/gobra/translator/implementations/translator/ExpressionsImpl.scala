package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Expressions
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class ExpressionsImpl extends Expressions {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(col: Collector): Unit = {

  }

  override def translate(x: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {

    val (pos, info, errT) = x.vprMeta

    def goE(e: in.Expr): CodeWriter[vpr.Exp] = translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    x match {

      case unfold: in.Unfolding =>
        for {
          a <- ctx.loc.predicateAccess(unfold.op)(ctx)
          e <- goE(unfold.in)
        } yield vpr.Unfolding(a, e)(pos, info, errT)

      case in.PureFunctionCall(func, args, typ) =>
        val arity = ctx.loc.arity(typ)(ctx)
        val resultType = ctx.loc.ttype(typ)(ctx)
        for {
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          app = vpr.FuncApp(func.name, vArgss.flatten)(pos, info, resultType, errT)
          res <- if (arity == 1) unit(app) else {
            copyResult(app) flatMap (z => ctx.loc.copyFromTuple(z, typ)(ctx))
          }
        } yield res

      case in.PureMethodCall(recv, meth, args, typ) =>
        val arity = ctx.loc.arity(typ)(ctx)
        val resultType = ctx.loc.ttype(typ)(ctx)
        for {
          vRecvs <- ctx.loc.argument(recv)(ctx)
          vArgss <- sequence(args map (ctx.loc.argument(_)(ctx)))
          app = vpr.FuncApp(meth.uniqueName, vRecvs ++ vArgss.flatten)(pos, info, resultType, errT)
          res <- if (arity == 1) unit(app) else {
            copyResult(app) flatMap (z => ctx.loc.copyFromTuple(z, typ)(ctx))
          }
        } yield res

      case in.DfltVal(t) => ctx.loc.defaultValue(t)(x)(ctx)
      case in.Tuple(args) => Violation.violation("Tuples expressions are not supported at this point in time")
      case p: in.Deref => ctx.loc.evalue(p)(ctx)
      case f: in.FieldRef => ctx.loc.evalue(f)(ctx)
      case r: in.Ref => ctx.loc.evalue(r)(ctx)

      case in.Negation(op) => for{o <- goE(op)} yield vpr.Not(o)(pos, info, errT)

      case in.EqCmp(l, r) => ctx.loc.equal(l, r)(x)(ctx)
      case in.UneqCmp(l, r) => ctx.loc.equal(l, r)(x)(ctx).map(vpr.Not(_)(pos, info, errT))
      case in.LessCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LtCmp(vl, vr)(pos, info, errT)
      case in.AtMostCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.LeCmp(vl, vr)(pos, info, errT)
      case in.GreaterCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GtCmp(vl, vr)(pos, info, errT)
      case in.AtLeastCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.GeCmp(vl, vr)(pos, info, errT)

      case in.And(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.And(vl, vr)(pos, info, errT)
      case in.Or(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Or(vl, vr)(pos, info, errT)

      case in.Add(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Add(vl, vr)(pos, info, errT)
      case in.Sub(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Sub(vl, vr)(pos, info, errT)
      case in.Mul(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mul(vl, vr)(pos, info, errT)
      case in.Mod(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mod(vl, vr)(pos, info, errT)
      case in.Div(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Div(vl, vr)(pos, info, errT)

      case in.Old(op) => for { o <- goE(op) } yield vpr.Old(o)(pos, info, errT)
      case in.LabelledOld(l, op) => for {o <- goE(op) } yield vpr.LabelledOld(o, l.name)(pos, info, errT)
      case in.Now(op) => for { v <- ctx.loc.copyTo(op, Names.freshName)(ctx) } yield v

      case in.Conditional(cond, thn, els, _) => for {vcond <- goE(cond); vthn <- goE(thn); vels <- goE(els)
                                                  } yield vpr.CondExp(vcond, vthn, vels)(pos, info, errT)

      case l: in.Lit => ctx.loc.literal(l)(ctx)
      case v: in.Var => ctx.loc.evalue(v)(ctx)
    }
  }






}
