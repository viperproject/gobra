package viper.gobra.translator.interfaces

import viper.gobra.translator.interfaces.components.Tuples
import viper.gobra.translator.interfaces.translator._
import viper.silver.{ast => vpr}

trait Context {

  // components
  def tuple: Tuples

  // translator
  def ass: Assertions
  def expr: Expressions
  def method: Methods
  def pureMethod: PureMethods
  def predicate: Predicates
  def stmt: Statements
  def typ: Types

  def loc: Locations

  // mapping

  def addVars(vars: vpr.LocalVarDecl*): Context

  /** copy constructor */
  def :=(
          tupleN: Tuples = tuple,
          assN: Assertions = ass,
          exprN: Expressions = expr,
          methodN: Methods = method,
          pureMethodN: PureMethods = pureMethod,
          predicateN: Predicates = predicate,
          stmtN: Statements = stmt,
          typN: Types = typ,
          locN: Locations = loc
         ): Context


  def finalize(col: Collector): Unit = {
    tuple.finalize(col)

    ass.finalize(col)
    expr.finalize(col)
    method.finalize(col)
    pureMethod.finalize(col)
    predicate.finalize(col)
    stmt.finalize(col)
    typ.finalize(col)
    loc.finalize(col)
  }
}
