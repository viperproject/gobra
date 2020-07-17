package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

trait Locations extends Generator {

  def arity(typ: in.Type)(ctx: Context): Int

  def values(t: in.Type)(ctx: Context): Vector[in.Expr => (CodeWriter[vpr.Exp], Locations.SubValueRep)]

  def parameter(v: in.Declaration)(ctx: Context): (Vector[vpr.LocalVarDecl], CodeWriter[Unit])

  def target(v: in.LocalVar.Val)(ctx: Context): CodeWriter[Vector[vpr.LocalVar]]

  def argument(arg: in.Expr)(ctx: Context): CodeWriter[Vector[vpr.Exp]]

  def localDecl(v: in.BottomDeclaration)(ctx: Context): (Vector[vpr.Declaration], CodeWriter[vpr.Stmt])

  def initialize(v: in.TopDeclaration)(ctx: Context): CodeWriter[vpr.Stmt]

  def evalue(l: in.Location)(ctx: Context): CodeWriter[vpr.Exp]

  def defaultValue(t: in.Type)(src: in.Node)(ctx: Context): CodeWriter[vpr.Exp]

  def literal(lit: in.Lit)(ctx: Context): CodeWriter[vpr.Exp]

  def equal(lhs: in.Expr, rhs: in.Expr)(src: in.Node)(ctx: Context): CodeWriter[vpr.Exp]

  def assignment(ass: in.SingleAss)(ctx: Context): CodeWriter[vpr.Stmt]

  def make(mk: in.Make)(ctx: Context): CodeWriter[vpr.Stmt]

  def access(acc: in.Access)(ctx: Context): CodeWriter[vpr.Exp]

  def predicateAccess(acc: in.PredicateAccess)(ctx: Context): CodeWriter[vpr.PredicateAccessPredicate]

  def ttype(typ: in.Type)(ctx: Context): vpr.Type

  def copyFromTuple(exp: vpr.Exp, typ: in.Type)(ctx: Context): CodeWriter[vpr.Exp]
}

object Locations {
  case class SubValueRep(typ: in.Type)
}
