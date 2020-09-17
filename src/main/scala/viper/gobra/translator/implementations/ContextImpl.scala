// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations

import viper.gobra.ast.internal.LookupTable
import viper.gobra.translator.interfaces.{Context, TranslatorConfig}
import viper.gobra.translator.interfaces.translator._
import viper.gobra.translator.interfaces.components._
import viper.silver.ast.LocalVarDecl

case class ContextImpl(
                        array : Arrays,
                        seqToSet : SeqToSet,
                        seqToMultiset : SeqToMultiset,
                        seqMultiplicity : SeqMultiplicity,
                        fixpoint: Fixpoint,
                        tuple: Tuples,
                        typeProperty: TypeProperties,
                        ass: Assertions,
                        expr: Expressions,
                        method: Methods,
                        pureMethod: PureMethods,
                        predicate: Predicates,
                        stmt: Statements,
                        typ: Types,
                        loc: Locations,
                        table: LookupTable
                      ) extends Context {

  def this(conf: TranslatorConfig, table: LookupTable) {
    this(
      conf.array,
      conf.seqToSet,
      conf.seqToMultiset,
      conf.seqMultiplicity,
      conf.fixpoint,
      conf.tuple,
      conf.typeProperty,
      conf.ass,
      conf.expr,
      conf.method,
      conf.pureMethod,
      conf.predicate,
      conf.stmt,
      conf.typ,
      conf.loc,
      table
    )
  }

  /** copy constructor */
  override def :=(
                   arrayN : Arrays = array,
                   seqToSetN : SeqToSet = seqToSet,
                   seqToMultisetN : SeqToMultiset = seqToMultiset,
                   seqMultiplicityN : SeqMultiplicity = seqMultiplicity,
                   fixpointN: Fixpoint = fixpoint,
                   tupleN: Tuples = tuple,
                   typePropertyN: TypeProperties = typeProperty,
                   assN: Assertions = ass,
                   exprN: Expressions = expr,
                   methodN: Methods = method,
                   pureMethodN: PureMethods = pureMethod,
                   predicateN: Predicates = predicate,
                   stmtN: Statements = stmt,
                   typN: Types = typ,
                   locN: Locations = loc,
                 ): Context = copy(
    array,
    seqToSet,
    seqToMultiset,
    seqMultiplicity,
    fixpoint,
    tuple,
    typeProperty,
    assN,
    exprN,
    methodN,
    pureMethodN,
    predicateN,
    stmtN,
    typN
  )

  override def addVars(vars: LocalVarDecl*): Context = this
}
