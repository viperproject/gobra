package viper.gobra.translator.interfaces

import viper.gobra.translator.interfaces.translator._

trait TranslatorConfig {
  def ass: Assertions
  def expr: Expressions
  def func: Functions
  def method: Methods
  def stmt: Statements
  def typ: Types

  def loc: Locations
}
