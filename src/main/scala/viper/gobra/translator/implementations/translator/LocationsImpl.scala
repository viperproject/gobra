package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Locations
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.{PrimitiveGenerator, ViperUtil}
import viper.gobra.translator.util.ViperWriter.{ExprWriter, MemberWriter, StmtWriter}
import viper.silver.{ast => vpr}
import viper.gobra.reporting.Source.{withInfo => nodeWithInfo}

class LocationsImpl extends Locations {

  import viper.gobra.translator.util.ViperWriter.ExprLevel._
  import viper.gobra.translator.util.ViperWriter.{StmtLevel => sl, MemberLevel => ml}

  override def finalize(col: Collector): Unit = {
    _pointerField.finalize(col)
  }

  private lazy val _pointerField: PrimitiveGenerator.PrimitiveGenerator[vpr.Type, vpr.Field] =
    PrimitiveGenerator.simpleGenerator(
      (t: vpr.Type) => {
        val f = vpr.Field(name = Names.pointerFields(t), typ = t)()
        (f, Vector(f))
      }
    )

  private def pointerField(t: in.Type)(ctx: Context): vpr.Field = _pointerField(ctx.typ.translate(t)(ctx))

  /**
    * [v]w -> v
    */
  override def variable(v: in.Var)(ctx: Context): ExprWriter[vpr.LocalVar] = withDeepInfo(v){

    def goT(t: in.Type): vpr.Type = ctx.typ.translate(t)(ctx)

    v match {
      case in.Parameter(id, t)    => unit(vpr.LocalVar(id)(goT(t)))
      case in.LocalVar.Val(id, t) => unit(vpr.LocalVar(id)(goT(t)))
      case in.LocalVar.Ref(id, t) => unit(vpr.LocalVar(id)(vpr.Ref))
    }
  }


  override def topDecl(v: in.TopDeclaration)(ctx: Context): MemberWriter[((vpr.LocalVarDecl, StmtWriter[vpr.Stmt]), Context)] = {
    v match {
      case v: in.Var =>
        ml.splitE(variable(v)(ctx)).map{ case (e, w) =>
          ((ViperUtil.toVarDecl(e), sl.closeE(w)(e)), ctx)
        }
    }
  }

  override def bottomDecl(v: in.BottomDeclaration)(ctx: Context): StmtWriter[((vpr.Declaration, vpr.Stmt), Context)] = {
    v match {
      case v: in.Var =>
        val (x, u) = variable(v)(ctx).cut
        sl.closeE(u)(x).map{ s => ((ViperUtil.toVarDecl(x), s), ctx)}
    }
  }


  override def assignment(ass: in.SingleAss)(ctx: Context): StmtWriter[vpr.Stmt] = sl.withDeepInfo(ass){sl.seqnE{
    for {
      right <- ctx.expr.translate(ass.right)(ctx)

      assignment <- ass.left match {
        case in.Assignee.Var(v: in.LocalVar.Val) =>
          for {l <- variable(v)(ctx)} yield vpr.LocalVarAssign(l, right)()

        case in.Assignee.Var(v: in.LocalVar.Ref) =>
          for {rcv <- variable(v)(ctx); l = vpr.FieldAccess(rcv, pointerField(v.typ)(ctx))()}
            yield vpr.FieldAssign(l, right)()

        case in.Assignee.Pointer(p) =>
          for {l <- deref(p)(ctx)} yield vpr.FieldAssign(l, right)()
      }
    } yield assignment
  }}



  /**
    * [v]w -> v      , if v is non-ref
    * [v]w -> v.val  , otherwise
    */
  override def value(v: in.Var)(ctx: Context): ExprWriter[vpr.Exp] = withDeepInfo(v){
    v match {
      case _: in.Parameter | _: in.LocalVar.Val => variable(v)(ctx)
      case in.LocalVar.Ref(_, t) =>
        for {rcv <- variable(v)(ctx)} yield vpr.FieldAccess(rcv, pointerField(t)(ctx))()
    }
  }

  /**
    * [&e]w -> e
    */
  override def address(ref: in.Addressable)(ctx: Context): ExprWriter[vpr.Exp] = ref match {
    case in.Addressable.Var(v) => variable(v)(ctx)
  }

  /**
    * [*e]w -> ([e]w).val
    */
  override def deref(ref: in.Deref)(ctx: Context): ExprWriter[vpr.FieldAccess] =
    for {rcv <- ctx.expr.translate(ref.exp)(ctx)} yield nodeWithInfo(vpr.FieldAccess(rcv, pointerField(ref.typ)(ctx)))(ref)
}
