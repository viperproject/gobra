package viper.gobra.translator.interfaces

import viper.gobra.translator.interfaces.translator._
import viper.silver.{ast => vpr}

trait Context {

  // translator
  def ass: Assertions
  def expr: Expressions
  def func: Functions
  def method: Methods
  def stmt: Statements
  def typ: Types

  def loc: Locations

  // mapping

  def addVars(vars: vpr.LocalVarDecl*): Context

  /** copy constructor */
  def :=(
           assN: Assertions = ass,
           exprN: Expressions = expr,
           funcN: Functions = func,
           methodN: Methods = method,
           stmtN: Statements = stmt,
           typN: Types = typ,
           locN: Locations = loc
         ): Context


  def finalize(col: Collector): Unit = {
    ass.finalize(col)
    expr.finalize(col)
    func.finalize(col)
    method.finalize(col)
    stmt.finalize(col)
    typ.finalize(col)
    loc.finalize(col)
  }
}
