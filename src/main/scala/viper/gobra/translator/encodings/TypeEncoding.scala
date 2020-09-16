package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.reporting.Source.Parser.Info
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}

trait TypeEncoding extends Generator {

  /**
    * Translates an in-parameter.
    * Returns:
    * 1) The encoded Viper parameters
    * 2) An optional extension to the precondition
    */
  def inParameter(ctx: Context): in.Parameter.In ==> (Vector[vpr.LocalVarDecl], Option[MemberWriter[vpr.Exp]]) = PartialFunction.empty

  /**
    * Translates an out-parameter.
    * Returns:
    * 1) The encoded Viper parameters
    * 2) An optional extension to the postcondition
    * 3) Initialization code for the out-parameter.
    *    The initialization has to guarantee that the out-parameter receives its default value.
    */
  def outParameter(ctx: Context): in.Parameter.Out ==> (Vector[vpr.LocalVarDecl], Option[MemberWriter[vpr.Exp]], CodeWriter[Unit]) = PartialFunction.empty

  /**
    * Translates an declaration
    * Returns:
    * 1) The encoded Viper declaration
    * 2) Initialization code for the declaration.
    *    The initialization has to guarantee that a declared variable receives its default value.
    */
  def localDecl(ctx: Context): in.BottomDeclaration ==> (Vector[vpr.Declaration], CodeWriter[Unit]) = PartialFunction.empty

  // def variable: (, Context) ==> (Vector[vpr.LocalVarDecl], CodeWriter[Unit])

  // def typ: (in.Type, Context) ==> vpr.Type

  def assignment(ctx: Context): (in.Assignee, in.Expr, Info) ==> CodeWriter[vpr.Stmt] = PartialFunction.empty

  def equal(ctx: Context): (in.Expr, in.Expr, Info) ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  // literal, defaultValue, make
  def asRValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  def asLValue(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  // TODO: maybe remove
  // def reference: (in.Location, Context) ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  def access(ctx: Context): in.Access ==> CodeWriter[vpr.Exp] = PartialFunction.empty
}

