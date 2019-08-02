package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Expressions
import viper.gobra.translator.util.ViperWriter.ExprWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class ExpressionsImpl extends Expressions {

  import viper.gobra.translator.util.ViperWriter.ExprLevel._

  override def finalize(col: Collector): Unit = {

  }

  override def translate(x: in.Expr)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(x){

    def goE(e: in.Expr): ExprWriter[vpr.Exp] = translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    x match {
      case in.DfltVal(t) => defaultValue(t)
      case in.Tuple(args) => Violation.violation("Tuples expressions are not supported at this point in time")
      case p: in.Deref => ctx.loc.evalue(p)(ctx)
      case f: in.FieldRef => ctx.loc.evalue(f)(ctx)
      case r: in.Ref => ctx.loc.evalue(r)(ctx)

      case in.Negation(op) => for{o <- goE(op)} yield vpr.Not(o)()

      case in.EqCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.EqCmp(vl, vr)()
      case in.UneqCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.NeCmp(vl, vr)()
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


      case l: in.Lit => literal(l)(ctx)
      case v: in.Var => ctx.loc.evalue(v)(ctx)
    }
  }

  def literal(l: in.Lit)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(l){

    l match {
      case in.IntLit(v) => unit(vpr.IntLit(v)())
      case in.BoolLit(b) => unit(vpr.BoolLit(b)())
    }
  }

  def defaultValue(t: in.Type): ExprWriter[vpr.Exp] = t match {
    case in.BoolT => unit(vpr.TrueLit()())
    case in.IntT => unit(vpr.IntLit(0)())
    case in.PermissionT => unit(vpr.NoPerm()())
    case in.DefinedT(_, t2) => defaultValue(t2)
    case in.PointerT(_) => unit(vpr.NullLit()())
    case _ => ???
  }


}
