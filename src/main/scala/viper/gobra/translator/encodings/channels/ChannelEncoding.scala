package viper.gobra.translator.encodings.channels

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class ChannelEncoding extends LeafTypeEncoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Channel(t) / m =>  m match {
      case Exclusive => vpr.Int // for now we simply map it to Viper Ints
      case Shared => vpr.Ref
    }
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * R[ dflt(channel[T]) ] -> 0
    * R[ nil : channel[T] ] -> 0
    */
  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    default(super.expr(ctx)) {
      case (exp : in.DfltVal) :: ctx.Channel(t) / Exclusive =>
        unit(withSrc(vpr.IntLit(0), exp))

      case (lit: in.NilLit) :: ctx.Channel(t) =>
        unit(withSrc(vpr.IntLit(0), lit))
    }
  }

  /**
    * Encodes statements.
    * This includes make-statements.
    *
    * The default implements:
    * [v: *T = make(lit)] -> var z (*T)°; inhale Footprint[*z] && [*z == lit]; [v = z]
    *
    *
    *
    *
    */
  /*
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)){

    }
  }
  */
}
