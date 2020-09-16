package viper.gobra.translator.encodings.structs

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.internal.{Access, Assignee, BottomDeclaration, Expr, Location, Parameter}
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source.Parser.Info
import viper.gobra.translator.encodings.TypeEncoding
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.ast.{Declaration, Exp, LocalVarDecl, Stmt}
import viper.silver.{ast => vpr}

class StructEncoding extends TypeEncoding with Generator {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.TypePatterns._

  private val exDomain: ExclusiveStructDomain = ???
  private val shDomain: SharedStructDomain = ???

  override def finalize(col: Collector): Unit = {
    exDomain.finalize(col)
    shDomain.finalize(col)
  }

  override def inParameter(ctx: Context): Parameter.In ==> (Vector[LocalVarDecl], Option[MemberWriter[Exp]]) = {
    case (v: in.Var) :: ctx.Struct(fs) / m => ???
  }

  override def outParameter(ctx: Context): Parameter.Out ==> (Vector[LocalVarDecl], Option[MemberWriter[Exp]], CodeWriter[Unit]) = super.outParameter(ctx)

  override def localDecl(ctx: Context): BottomDeclaration ==> (Vector[Declaration], CodeWriter[Unit]) = super.localDecl(ctx)

  override def assignment(ctx: Context): (Assignee, Expr, Info) ==> CodeWriter[Stmt] = super.assignment(ctx)

  override def equal(ctx: Context): (Expr, Expr, Info) ==> CodeWriter[Exp] = super.equal(ctx)

  override def asRValue(ctx: Context): Expr ==> CodeWriter[Exp] = super.asRValue(ctx)

  override def asLValue(ctx: Context): Location ==> CodeWriter[Exp] = super.asLValue(ctx)

  override def access(ctx: Context): Access ==> CodeWriter[Exp] = super.access(ctx)
}
