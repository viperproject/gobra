// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.context

import viper.gobra.translator.encodings.combinators.{DefaultEncoding, TypeEncoding}
import viper.gobra.translator.library.arrays.Arrays
import viper.gobra.translator.library.conditions.Conditions
import viper.gobra.translator.library.equality.Equality
import viper.gobra.translator.library.fields.Fields
import viper.gobra.translator.library.multiplicity.SeqMultiplicity
import viper.gobra.translator.library.options.Options
import viper.gobra.translator.library.slices.Slices
import viper.gobra.translator.library.tos.{OptionToSeq, SeqToMultiset, SeqToSet}
import viper.gobra.translator.library.tuples.Tuples
import viper.gobra.translator.library.unknowns.UnknownValues

trait TranslatorConfig {
  // components
  def field: Fields

  def array: Arrays

  def seqToSet: SeqToSet

  def seqToMultiset: SeqToMultiset

  def seqMultiplicity: SeqMultiplicity

  def option: Options

  def optionToSeq: OptionToSeq

  def slice: Slices


  def tuple: Tuples

  def equality: Equality

  def condition: Conditions

  def unknownValue: UnknownValues

  // translators
  def typeEncoding: TypeEncoding

  def defaultEncoding: DefaultEncoding
}
