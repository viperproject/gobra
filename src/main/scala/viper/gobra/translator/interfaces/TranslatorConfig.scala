package viper.gobra.translator.interfaces

import viper.gobra.translator.interfaces.components.{SeqToSet, Tuples, TypeProperties}
import viper.gobra.translator.interfaces.translator._

trait TranslatorConfig {

  def seqToSet : SeqToSet
  def tuple: Tuples
  def typeProperty: TypeProperties

  def ass: Assertions
  def expr: Expressions
  def method: Methods
  def pureMethod: PureMethods
  def predicate: Predicates
  def stmt: Statements
  def typ: Types

  def loc: Locations
}
