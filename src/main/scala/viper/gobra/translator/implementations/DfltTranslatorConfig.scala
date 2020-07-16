package viper.gobra.translator.implementations

import viper.gobra.translator.implementations.components.{FixpointImpl, TuplesImpl, TypePropertiesImpl}
import viper.gobra.translator.implementations.translator._
import viper.gobra.translator.interfaces.TranslatorConfig
import viper.gobra.translator.interfaces.components.{Fixpoint, Tuples, TypeProperties}
import viper.gobra.translator.interfaces.translator._

class DfltTranslatorConfig(

   val fixpoint: Fixpoint = new FixpointImpl(),
   val tuple: Tuples     = new TuplesImpl(),
   val typeProperty: TypeProperties = new TypePropertiesImpl(),

   val ass: Assertions   = new AssertionsImpl(),
   val expr: Expressions = new ExpressionsImpl(),
   val method: Methods   = new MethodsImpl(),
   val pureMethod: PureMethods = new PureMethodsImpl(),
   val predicate: Predicates = new PredicatesImpl(),
   val stmt: Statements  = new StatementsImpl(),
   val typ: Types        = new TypesImpl(),

   val loc: Locations    = new LocationsImpl()
                       ) extends TranslatorConfig
