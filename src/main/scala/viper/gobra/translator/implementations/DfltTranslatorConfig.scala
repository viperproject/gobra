// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations

import viper.gobra.translator.encodings.arrays.ArrayEncoding
import viper.gobra.translator.encodings.channels.ChannelEncoding
import viper.gobra.translator.encodings.{BoolEncoding, DomainEncoding, IntEncoding, PermissionEncoding, PointerEncoding, StringEncoding, TypeEncoding}
import viper.gobra.translator.encodings.combinators.{FinalTypeEncoding, SafeTypeEncodingCombiner}
import viper.gobra.translator.encodings.interfaces.InterfaceEncoding
import viper.gobra.translator.encodings.maps.{MapEncoding, MathematicalMapEncoding}
import viper.gobra.translator.encodings.options.OptionEncoding
import viper.gobra.translator.encodings.preds.PredEncoding
import viper.gobra.translator.encodings.sequences.SequenceEncoding
import viper.gobra.translator.encodings.sets.SetEncoding
import viper.gobra.translator.encodings.slices.SliceEncoding
import viper.gobra.translator.encodings.structs.StructEncoding
import viper.gobra.translator.implementations.components._
import viper.gobra.translator.implementations.translator._
import viper.gobra.translator.interfaces.TranslatorConfig
import viper.gobra.translator.interfaces.components._
import viper.gobra.translator.interfaces.translator._

class DfltTranslatorConfig(
  val field : Fields = new FieldsImpl,
  val array : Arrays = new ArraysImpl,
  val seqToSet : SeqToSet = new SeqToSetImpl,
  val seqMultiplicity : SeqMultiplicity = new SeqMultiplicityImpl,
  val option : Options = new OptionImpl,
  val fixpoint: Fixpoint = new FixpointImpl,
  val tuple : Tuples = new TuplesImpl,
  val equality: Equality = new EqualityImpl,
  val condition: Conditions = new ConditionsImpl,
  val unknownValue: UnknownValues = new UnknownValuesImpl,
  val ass : Assertions = new AssertionsImpl,
  val expr : Expressions = new ExpressionsImpl,
  val method : Methods = new MethodsImpl,
  val pureMethod : PureMethods = new PureMethodsImpl,
  val predicate : Predicates = new PredicatesImpl,
  val builtInMembers : BuiltInMembers = new BuiltInMembersImpl,
  val stmt : Statements = new StatementsImpl
) extends TranslatorConfig {

  val seqToMultiset : SeqToMultiset = new SeqToMultisetImpl(seqMultiplicity)
  val optionToSeq : OptionToSeq = new OptionToSeqImpl(option)
  val slice : Slices = new SlicesImpl(array)

  private val arrayEncoding: ArrayEncoding = new ArrayEncoding()

  val typeEncoding: TypeEncoding = new FinalTypeEncoding(
    new SafeTypeEncodingCombiner(Vector(
      new BoolEncoding, new IntEncoding, new PermissionEncoding,
      new PointerEncoding, new StructEncoding, arrayEncoding, new InterfaceEncoding,
      new SequenceEncoding, new SetEncoding, new OptionEncoding, new DomainEncoding,
      new SliceEncoding(arrayEncoding), new PredEncoding, new ChannelEncoding, new StringEncoding,
      new MapEncoding, new MathematicalMapEncoding
    ))
  )
}
