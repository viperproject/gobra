package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.{ExprWriter, MemberWriter, StmtWriter}
import viper.silver.{ast => vpr}

trait Locations extends Generator {

  def fieldDecl(f: in.Field)(ctx: Context): Vector[vpr.Field]

  def topDecl(v: in.TopDeclaration)(ctx: Context): MemberWriter[((vpr.LocalVarDecl, StmtWriter[vpr.Stmt]), Context)]

  def bottomDecl(v: in.BottomDeclaration)(ctx: Context): StmtWriter[((vpr.Declaration, vpr.Stmt), Context)]

  def assignment(ass: in.SingleAss)(ctx: Context): StmtWriter[vpr.Stmt]

  def make(mk: in.Make)(ctx: Context): StmtWriter[vpr.Stmt]

  def rvalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp]

  def evalue(l: in.Location)(ctx: Context): ExprWriter[vpr.Exp]

  def variable(v: in.Var)(ctx: Context): ExprWriter[vpr.LocalVar]

  def callReceiver(recv: in.Expr, path: in.MemberPath)(ctx: Context): ExprWriter[vpr.Exp]

  def access(acc: in.Access)(ctx: Context): ExprWriter[vpr.Exp]



}
