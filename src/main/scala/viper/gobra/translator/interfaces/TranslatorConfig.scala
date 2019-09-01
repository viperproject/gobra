package viper.gobra.translator.interfaces

import viper.gobra.translator.interfaces.components.Tuples
import viper.gobra.translator.interfaces.translator._

trait TranslatorConfig {

  def tuple: Tuples

  def ass: Assertions
  def expr: Expressions
  def method: Methods
  def pureMethod: PureMethods
  def predicate: Predicates
  def stmt: Statements
  def typ: Types

  def loc: Locations
}
