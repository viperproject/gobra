// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.context

import viper.gobra.ast.internal.LookupTable
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.combinators.{DefaultEncoding, TypeEncoding}
import viper.gobra.translator.library.arrays.Arrays
import viper.gobra.translator.library.conditions.Conditions
import viper.gobra.translator.library.equality.Equality
import viper.gobra.translator.library.fields.Fields
import viper.gobra.translator.library.fixpoints.Fixpoint
import viper.gobra.translator.library.multiplicity.SeqMultiplicity
import viper.gobra.translator.library.options.Options
import viper.gobra.translator.library.slices.Slices
import viper.gobra.translator.library.tos.{OptionToSeq, SeqToMultiset, SeqToSet}
import viper.gobra.translator.library.tuples.Tuples
import viper.gobra.translator.library.unknowns.UnknownValues
import viper.silver.ast.LocalVarDecl

class ContextImpl(
                   override val field: Fields,
                   override val array: Arrays,
                   override val seqToSet: SeqToSet,
                   override val seqToMultiset: SeqToMultiset,
                   override val seqMultiplicity: SeqMultiplicity,
                   override val option: Options,
                   override val optionToSeq: OptionToSeq,
                   override val slice: Slices,
                   override val fixpoint: Fixpoint,
                   override val tuple: Tuples,
                   override val equality: Equality,
                   override val condition: Conditions,
                   override val unknownValue: UnknownValues,
                   override val typeEncoding: TypeEncoding,
                   override val defaultEncoding: DefaultEncoding,
                   override val table: LookupTable,
                   override val internalFreshNames: Context.FreshNameIterator = ContextImpl.FreshNameIteratorImpl(0),
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
      conf.defaultEncoding,
      table,
    )
  }

  /** copy constructor */
  override def update(
                   fieldN: Fields,
                   arrayN: Arrays,
                   seqToSetN: SeqToSet,
                   seqToMultisetN: SeqToMultiset,
                   seqMultiplicityN: SeqMultiplicity,
                   optionN: Options,
                   optionToSeqN: OptionToSeq,
                   sliceN: Slices,
                   fixpointN: Fixpoint,
                   tupleN: Tuples,
                   equalityN: Equality,
                   conditionN: Conditions,
                   unknownValueN: UnknownValues,
                   typeEncodingN: TypeEncoding,
                   defaultEncodingN: DefaultEncoding,
                   initialFreshCounterValueN: Option[Int],
                 ): Context = new ContextImpl(
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
    defaultEncodingN,
    table,
    initialFreshCounterValueN match {
      case None => internalFreshNames
      case Some(n) => ContextImpl.FreshNameIteratorImpl(n)
    },
  )

  override def addVars(vars: LocalVarDecl*): Context = this

}

object ContextImpl {
  case class FreshNameIteratorImpl(private val initialValue: Int) extends Context.FreshNameIterator {
    private var currentValue: Int = initialValue

    override def hasNext: Boolean = true

    override def next(): String = {
      val value = currentValue
      currentValue += 1
      s"${Names.freshNamePrefix}$value"
    }

    override def getValue: Int = currentValue
  }
}
