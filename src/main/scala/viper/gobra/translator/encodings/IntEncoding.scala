package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class IntEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Int() / m =>
      m match {
        case Exclusive => vpr.Int
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes expressions as r-values, i.e. values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    */
  override def rValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    super.rValue(ctx) orElse {
      case (e: in.DfltVal) :: ctx.Int() => unit(vpr.IntLit(0).tupled(e.vprMeta))
      case lit: in.IntLit => unit(vpr.IntLit(lit.v).tupled(lit.vprMeta))

      case e@ in.Add(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Add(vl, vr).tupled(e.vprMeta)
      case e@ in.Sub(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Sub(vl, vr).tupled(e.vprMeta)
      case e@ in.Mul(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mul(vl, vr).tupled(e.vprMeta)
      case e@ in.Mod(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Mod(vl, vr).tupled(e.vprMeta)
      case e@ in.Div(l, r) => for {vl <- goE(l); vr <- goE(r)} yield vpr.Div(vl, vr).tupled(e.vprMeta)
    }
  }
}
