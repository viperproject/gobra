package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class StringEncoding extends LeafTypeEncoding {
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.String() / m =>
      m match {
        case Exclusive => ???
        case Shared    => vpr.Ref
      }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {

    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.expr(ctx)){
      case (e: in.DfltVal) :: ctx.String() / Exclusive => unit(withSrc(vpr.IntLit(0), e))
      case lit: in.StringLit => unit(withSrc(???, ???))

        /*
      case e@ in.Add(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Add(vl, vr), e)
      case e@ in.Sub(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Sub(vl, vr), e)
      case e@ in.Mul(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Mul(vl, vr), e)
      case e@ in.Mod(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Mod(vl, vr), e)
      case e@ in.Div(l, r) => for {vl <- goE(l); vr <- goE(r)} yield withSrc(vpr.Div(vl, vr), e)
         */
    }
  }
}
