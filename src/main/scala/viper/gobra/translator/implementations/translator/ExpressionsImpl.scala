package viper.gobra.translator.implementations.translator

import viper.gobra.ast.internal.EqCmp
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.interfaces.translator.Expressions
import viper.gobra.util.ViperWriter.ExprWriter
import viper.silver.{ast => vpr}

class ExpressionsImpl extends Expressions {

  import ExprWriter._

  override def finalize(col: Collector): Unit = {

  }

  override def translate(x: in.Expr)(ctx: Context): ExprWriter[vpr.Exp] = {

    def goE(e: in.Expr): ExprWriter[vpr.Exp] = translate(e)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    x match {
      case in.DfltVal(t) => unit(defaultValue(t))
      case p: in.Deref => ctx.loc.deref(p)(ctx)
      case in.Ref(r, _) => ctx.loc.address(r)(ctx)
      case EqCmp(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.EqCmp(vl, vr)()
      case l: in.Lit => literal(l)(ctx)
      case v: in.Var => ctx.loc.value(v)(ctx)
    }
  }.withInfo(x)

  def literal(l: in.Lit)(ctx: Context): ExprWriter[vpr.Exp] = {

    l match {
      case in.IntLit(v) => unit(vpr.IntLit(v)())
      case in.BoolLit(b) => unit(vpr.BoolLit(b)())
    }
  }.withInfo(l)

  def defaultValue(t: in.Type): vpr.Exp = t match {
    case in.BoolT => vpr.TrueLit()()
    case in.IntT => vpr.IntLit(0)()
    case in.PermissionT => vpr.NoPerm()()
    case in.DefinedT(_, t2) => defaultValue(t2)
    case in.PointerT(_) => vpr.NullLit()()
    case _ => ???
  }


}
