package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.TypeBounds
import viper.silver.{ast => vpr}

class StringEncoding extends LeafTypeEncoding {
  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.String() / m => ctx.typeEncoding.typ(ctx)(underlyingStruct(m))
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
      case (e: in.DfltVal) :: ctx.String() / Exclusive =>
        //unit(withSrc(vpr.IntLit(0), e))
        val v = in.StructLit(underlyingStruct(Exclusive), Vector(in.IntLit(BigInt(0))(Source.Parser.Internal)))(Source.Parser.Internal)
        // unit(withSrc(goE(v), lit))
        goE(v)
      case lit: in.StringLit => //unit(withSrc(vpr.IntLit(0), lit))
        println(s"s: ${lit.s} ; len: ${lit.s.length}")
        val v = in.StructLit(underlyingStruct(Exclusive), Vector(in.IntLit(BigInt(lit.s.getBytes("UTF-8")))(Source.Parser.Internal)))(Source.Parser.Internal)
        // unit(withSrc(goE(v), lit))
        goE(v)
      // case in.Length(exp :: ctx.String()) => for {
      //  expT <- goE(exp)
      // } yield withSrc(len(expT), exp)
    }
  }

  //def len()

  private def underlyingStruct(addr: Addressability): in.Type =
    in.StructT("string",
      //Vector(in.Field("length", in.IntT(Addressability.field(addr), TypeBounds.DefaultInt), false)(Source.Parser.Internal),
        //TODO: in.Field("str", in.PointerT(in.IntT(Addressability.sharedVariable, TypeBounds.Byte), Addressability.field(addr)), false)(Source.Parser.Internal)), //TODO: change default int to the type determined by config
      Vector(in.Field("length", in.IntT(Addressability.field(addr), TypeBounds.DefaultInt), false)(Source.Parser.Internal)),
      addr)
}