package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.util.ViperWriter.{ExprWriter, StmtWriter}
import viper.silver.{ast => vpr}

trait Locations extends Generator {

  def variable(v: in.Var)(ctx: Context): ExprWriter[vpr.LocalVar]

  // TODO: use specialized writer
  def formalArg(v: in.Parameter)(ctx: Context): vpr.LocalVarDecl

  def formalRes(v: in.LocalVar)(ctx: Context): vpr.LocalVarDecl

  def value(v: in.Var)(ctx: Context): ExprWriter[vpr.Exp]

  def address(ref: in.Addressable)(ctx: Context): ExprWriter[vpr.Exp]

  def deref(ref: in.Deref)(ctx: Context): ExprWriter[vpr.FieldAccess]

  def assignment(left: in.Assignee, right: vpr.Exp)(ctx: Context)(src: in.Source): StmtWriter[vpr.Stmt]

}
