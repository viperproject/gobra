// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

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
