// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations

import viper.gobra.ast.internal.LookupTable
import viper.gobra.translator.encodings.TypeEncoding
import viper.gobra.translator.interfaces.{Context, TranslatorConfig}
import viper.gobra.translator.interfaces.translator._
import viper.gobra.translator.interfaces.components._
import viper.silver.ast.LocalVarDecl

case class ContextImpl(
                        field : Fields,
                        array : Arrays,
                        seqToSet : SeqToSet,
                        seqToMultiset : SeqToMultiset,
                        seqMultiplicity : SeqMultiplicity,
                        option : Options,
                        optionToSeq : OptionToSeq,
                        slice : Slices,
                        fixpoint: Fixpoint,
                        tuple: Tuples,
                        equality: Equality,
                        condition: Conditions,
                        unknownValue: UnknownValues,
                        typeEncoding: TypeEncoding,
                        ass: Assertions,
                        expr: Expressions,
                        method: Methods,
                        pureMethod: PureMethods,
                        predicate: Predicates,
                        builtInMembers: BuiltInMembers,
                        stmt: Statements,
                        table: LookupTable
                      ) extends Context {

  def this(conf: TranslatorConfig, table: LookupTable) = {
    this(
      conf.field,
      conf.array,
      conf.seqToSet,
      conf.seqToMultiset,
      conf.seqMultiplicity,
      conf.option,
      conf.optionToSeq,
      conf.slice,
      conf.fixpoint,
      conf.tuple,
      conf.equality,
      conf.condition,
      conf.unknownValue,
      conf.typeEncoding,
      conf.ass,
      conf.expr,
      conf.method,
      conf.pureMethod,
      conf.predicate,
      conf.builtInMembers,
      conf.stmt,
      table
    )
  }

  /** copy constructor */
  override def :=(
                   fieldN : Fields = field,
                   arrayN : Arrays = array,
                   seqToSetN : SeqToSet = seqToSet,
                   seqToMultisetN : SeqToMultiset = seqToMultiset,
                   seqMultiplicityN : SeqMultiplicity = seqMultiplicity,
                   optionN : Options = option,
                   optionToSeqN : OptionToSeq = optionToSeq,
                   sliceN : Slices = slice,
                   fixpointN: Fixpoint = fixpoint,
                   tupleN: Tuples = tuple,
                   equalityN: Equality = equality,
                   conditionN: Conditions = condition,
                   unknownValueN: UnknownValues = unknownValue,
                   typeEncodingN: TypeEncoding = typeEncoding,
                   assN: Assertions = ass,
                   exprN: Expressions = expr,
                   methodN: Methods = method,
                   pureMethodN: PureMethods = pureMethod,
                   predicateN: Predicates = predicate,
                   builtInMembersN: BuiltInMembers = builtInMembers,
                   stmtN: Statements = stmt,
                 ): Context = copy(
    fieldN,
    arrayN,
    seqToSetN,
    seqToMultisetN,
    seqMultiplicityN,
    optionN,
    optionToSeqN,
    sliceN,
    fixpointN,
    tupleN,
    equalityN,
    conditionN,
    unknownValueN,
    typeEncodingN,
    assN,
    exprN,
    methodN,
    pureMethodN,
    predicateN,
    builtInMembersN,
    stmtN
  )

  override def addVars(vars: LocalVarDecl*): Context = this
}
