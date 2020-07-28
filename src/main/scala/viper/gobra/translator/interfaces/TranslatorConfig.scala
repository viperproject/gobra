package viper.gobra.translator.interfaces

import viper.gobra.translator.interfaces.components._
import viper.gobra.translator.interfaces.translator._

trait TranslatorConfig {
  // components
  def array : Arrays
  def seqToSet : SeqToSet
  def seqToMultiset : SeqToMultiset
  def seqMultiplicity : SeqMultiplicity
  def fixpoint: Fixpoint
  def tuple: Tuples
  def typeProperty: TypeProperties

  // translators
  def ass: Assertions
  def expr: Expressions
  def method: Methods
  def pureMethod: PureMethods
  def predicate: Predicates
  def stmt: Statements
  def typ: Types

  def loc: Locations
}
