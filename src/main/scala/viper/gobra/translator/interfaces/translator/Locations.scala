package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

trait Locations extends Generator {

  def fieldDecl(f: in.Field)(ctx: Context): Vector[vpr.Field]

  def topDecl(v: in.TopDeclaration)(ctx: Context): ((Vector[vpr.LocalVarDecl], CodeWriter[vpr.Stmt]), Context)

  def bottomDecl(v: in.BottomDeclaration)(ctx: Context): ((Vector[vpr.Declaration], CodeWriter[vpr.Stmt]), Context)

  def assignment(ass: in.SingleAss)(ctx: Context): CodeWriter[vpr.Stmt]

  def make(mk: in.Make)(ctx: Context): CodeWriter[vpr.Stmt]

  def rvalue(l: in.Expr)(ctx: Context): CodeWriter[vpr.Exp]

  def evalue(l: in.Location)(ctx: Context): CodeWriter[vpr.Exp]

  def variable(v: in.Var)(ctx: Context): CodeWriter[vpr.LocalVar]

  def callReceiver(recv: in.Expr, path: in.MemberPath)(ctx: Context): CodeWriter[vpr.Exp]

  def access(acc: in.Access)(ctx: Context): CodeWriter[vpr.Exp]



}
