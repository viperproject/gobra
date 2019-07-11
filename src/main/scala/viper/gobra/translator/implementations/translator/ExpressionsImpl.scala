package viper.gobra.translator.implementations.translator

import viper.gobra.ast.internal.EqCmp
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Expressions
import viper.gobra.translator.util.ViperWriter.ExprWriter
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
      case p: in.Deref => ctx.loc.deref(p)(ctx)
      case in.Ref(r, _) => ctx.loc.address(r)(ctx)
      case EqCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.EqCmp(vl, vr)()
      case l: in.Lit => literal(l)(ctx)
      case v: in.Var => ctx.loc.value(v)(ctx)
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
