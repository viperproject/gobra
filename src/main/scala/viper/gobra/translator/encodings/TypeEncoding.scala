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
    * Translates variables. Returns the encoded Viper variables.
    */
  def variable(ctx: Context): in.Var ==> Vector[vpr.LocalVarDecl] = PartialFunction.empty

  /**
    * Returns extensions to the precondition for an in-parameter.
    */
  def precondition(ctx: Context): in.Parameter.In ==> MemberWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Returns extensions to the postcondition for an out-parameter
    */
  def postcondition(ctx: Context): in.Parameter.Out ==> MemberWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Returns initialization code for a declaration.
    * The initialization code has to guarantee that:
    * 1) the declared variable has its default value afterwards
    * 2) all permissions for the declared variables are owned afterwards
    */
  def initialization(ctx: Context): in.BottomDeclaration ==> CodeWriter[vpr.Stmt] = PartialFunction.empty

  /**
    * Translates a type into Viper types.
    */
  def typ(ctx: Context): in.Type ==> Vector[vpr.Type] = PartialFunction.empty

  /**
    * Encodes an assignment.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    */
  def assignment(ctx: Context): (in.Assignee, in.Expr, Info) ==> CodeWriter[vpr.Stmt] = PartialFunction.empty

  /**
    * Encodes the comparison of two expressions.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    */
  def equal(ctx: Context): (in.Expr, in.Expr, Info) ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Encodes expressions as r-values, i.e. a value that does not occupy some identifiable location in memory.
    * This includes literals and default values.
    */
  def rValue(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Encodes expressions as r-values, i.e. a value that does not occupy some identifiable location in memory.
    * This includes literals and default values.
    */
  def lValue(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Encodes the reference of an expression.
    */
  def reference(ctx: Context): in.Location ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Encodes access permissions.
    */
  def access(ctx: Context): in.Access ==> CodeWriter[vpr.Exp] = PartialFunction.empty

  /**
    * Encodes make statements.
    */
  def make(ctx: Context): in.Make ==> CodeWriter[vpr.Stmt] = PartialFunction.empty
}

