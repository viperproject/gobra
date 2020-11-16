// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0

package viper.gobra.translator.encodings.interfaces

import viper.gobra.translator.encodings.LeafTypeEncoding
import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

class InterfaceEncoding extends LeafTypeEncoding {

  // import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._
  // import viper.gobra.translator.util.{ViperUtil => VU}

  override def finalize(col: Collector): Unit = {

  }

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Interface(_) / m =>
      m match {
        case Exclusive => ??? : vpr.Type
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
      case (e: in.DfltVal) :: ctx.Interface(_) / Exclusive => ??? : CodeWriter[vpr.Exp]

      case in.TypeAssertion(exp, t) => ???

      case in.TypeOf(exp) => ???

      case in.ToInterface(exp, _) => ???

      case in.BoolTExpr() => ???
      case in.IntTExpr(kind) => ???
      case in.PermTExpr() => ???
      case in.PointerTExpr(elem) => ???
      case in.StructTExpr(fs) => ???
      case in.ArrayTExpr(len, elem) => ???
      case in.SequenceTExpr(elem) => ???
      case in.SetTExpr(elem) => ???
      case in.MultisetTExpr(elem) => ???
      case in.OptionTExpr(elem) => ???
      case in.TupleTExpr(elem) => ???
      case in.DefinedTExpr(name) => ???
    }
  }


  /**
    * Encodes statements.
    * This includes make-statements.
    *
    * The default implements:
    * [v: *T = make(lit)] -> var z (*T)°; inhale Footprint[*z] && [*z == lit]; [v = z]
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)){
      case in.SafeTypeAssertion(resTarget, successTarget, expr, typ) => ??? : CodeWriter[vpr.Stmt]
    }
  }



}
