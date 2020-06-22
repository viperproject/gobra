package viper.gobra.translator.interfaces

import viper.gobra.ast.internal.LookupTable
import viper.gobra.translator.interfaces.components.{SeqMultiplicity, SeqToSet, Tuples, TypeProperties}
import viper.gobra.translator.interfaces.translator._
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}

trait Context {

  // components
  def seqToSet : SeqToSet
  def seqMultiplicity : SeqMultiplicity
  def tuple: Tuples
  def typeProperty: TypeProperties

  // translator
  def ass: Assertions
  def expr: Expressions
  def method: Methods
  def pureMethod: PureMethods
  def predicate: Predicates
  def stmt: Statements
  def typ: Types

  def loc: Locations

  // lookup

  def table: LookupTable
  def lookup(t: in.DefinedT): in.Type = table.lookup(t)

  // mapping

  def addVars(vars: vpr.LocalVarDecl*): Context

  /** copy constructor */
  def :=(
          seqToSetN : SeqToSet = seqToSet,
          seqMultiplicityT : SeqMultiplicity = seqMultiplicity,
          tupleN: Tuples = tuple,
          typeN: TypeProperties = typeProperty,
          assN: Assertions = ass,
          exprN: Expressions = expr,
          methodN: Methods = method,
          pureMethodN: PureMethods = pureMethod,
          predicateN: Predicates = predicate,
          stmtN: Statements = stmt,
          typN: Types = typ,
          locN: Locations = loc
         ): Context


  def finalize(col : Collector): Unit = {
    // components
    seqToSet.finalize(col)
    seqMultiplicity.finalize(col)
    tuple.finalize(col)

    // translators
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
