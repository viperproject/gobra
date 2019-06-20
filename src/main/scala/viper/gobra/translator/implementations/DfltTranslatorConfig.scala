package viper.gobra.translator.implementations

import viper.gobra.translator.implementations.translator._
import viper.gobra.translator.interfaces.TranslatorConfig
import viper.gobra.translator.interfaces.translator._

class DfltTranslatorConfig(
   val ass: Assertions   = new AssertionsImpl(),
   val expr: Expressions = new ExpressionsImpl(),
   val func: Functions   = new FunctionsImpl(),
   val method: Methods   = new MethodsImpl(),
   val stmt: Statements  = new StatementsImpl(),
   val typ: Types        = new TypesImpl(),

   val loc: Locations    = new LocationsImpl()
                       ) extends TranslatorConfig
