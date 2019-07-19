package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{ExprWriter, MemberWriter, StmtWriter}
import viper.silver.{ast => vpr}

trait Locations extends Generator {

  def variable(v: in.Var)(ctx: Context): ExprWriter[vpr.LocalVar]

  def topDecl(v: in.TopDeclaration)(ctx: Context): MemberWriter[((vpr.LocalVarDecl, StmtWriter[vpr.Stmt]), Context)]

  def bottomDecl(v: in.BottomDeclaration)(ctx: Context): StmtWriter[((vpr.Declaration, vpr.Stmt), Context)]

  def value(v: in.Var)(ctx: Context): ExprWriter[vpr.Exp]

  def address(ref: in.Addressable)(ctx: Context): ExprWriter[vpr.Exp]

  def deref(ref: in.Deref)(ctx: Context): ExprWriter[vpr.FieldAccess]

  def assignment(ass: in.SingleAss)(ctx: Context): StmtWriter[vpr.Stmt]

  def assignment(left: in.Assignee, right: in.Expr)(src: in.Node)(ctx: Context): StmtWriter[vpr.Stmt]

}
