package viper.gobra.translator.interfaces

import akka.actor.Address
import viper.gobra.ast.internal.LookupTable
import viper.gobra.translator.interfaces.components._
import viper.gobra.translator.interfaces.translator._
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.translator.encodings.TypeEncoding

trait Context {

  // combined encodings
  def typeEncoding: TypeEncoding

  // components
  def array : Arrays
  def seqToSet : SeqToSet
  def seqToMultiset : SeqToMultiset
  def seqMultiplicity : SeqMultiplicity
  def fixpoint: Fixpoint
  def tuple: Tuples
  def equality: Equality
  def condition: Conditions
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
          arrayN : Arrays = array,
          seqToSetN : SeqToSet = seqToSet,
          seqToMultisetN : SeqToMultiset = seqToMultiset,
          seqMultiplicityN : SeqMultiplicity = seqMultiplicity,
          fixpointN: Fixpoint = fixpoint,
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
    array.finalize(col)
    seqToSet.finalize(col)
    seqToMultiset.finalize(col)
    seqMultiplicity.finalize(col)
    fixpoint.finalize(col)
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
