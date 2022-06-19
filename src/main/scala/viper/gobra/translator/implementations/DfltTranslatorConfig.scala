// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.arrays.ArrayEncoding
import viper.gobra.translator.encodings.channels.ChannelEncoding
import viper.gobra.translator.encodings.{BoolEncoding, DomainEncoding, FloatEncoding, IntEncoding, PermissionEncoding, PointerEncoding, StringEncoding}
import viper.gobra.translator.encodings.combinators.{DefaultEncoding, FinalTypeEncoding, SafeTypeEncodingCombiner, TypeEncoding}
import viper.gobra.translator.encodings.interfaces.InterfaceEncoding
import viper.gobra.translator.encodings.maps.{MapEncoding, MathematicalMapEncoding}
import viper.gobra.translator.encodings.members.{DefaultMethodEncoding, DefaultPredicateEncoding, DefaultPureMethodEncoding}
import viper.gobra.translator.encodings.options.OptionEncoding
import viper.gobra.translator.encodings.preds.PredEncoding
import viper.gobra.translator.encodings.sequences.SequenceEncoding
import viper.gobra.translator.encodings.sets.SetEncoding
import viper.gobra.translator.encodings.slices.SliceEncoding
import viper.gobra.translator.encodings.structs.StructEncoding
import viper.gobra.translator.encodings.typeless.{AssertionEncoding, BuiltInEncoding, CallEncoding, Comments, ControlEncoding, MemoryEncoding, OutlineEncoding, TerminationEncoding}
import viper.gobra.translator.implementations.components._
import viper.gobra.translator.interfaces.{Context, TranslatorConfig}
import viper.gobra.translator.interfaces.components._
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.silver.{ast => vpr}

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
) extends TranslatorConfig {

  val seqToMultiset : SeqToMultiset = new SeqToMultisetImpl(seqMultiplicity)
  val optionToSeq : OptionToSeq = new OptionToSeqImpl(option)
  val slice : Slices = new SlicesImpl(array)

  val arrayEncoding: ArrayEncoding = new ArrayEncoding()

  val interfaceEncoding = new InterfaceEncoding
  val methodEncoding = new DefaultMethodEncoding((p,_) => !p.receiver.typ.isInstanceOf[in.InterfaceT])
  val pureMethodEncoding = new DefaultPureMethodEncoding((p,_) => !p.receiver.typ.isInstanceOf[in.InterfaceT])
  val predicateEncoding = new DefaultPredicateEncoding((p,ctx) => !interfaceEncoding.hasFamily(p.name)(ctx))

  val typeEncoding: TypeEncoding = new FinalTypeEncoding(
    new SafeTypeEncodingCombiner(Vector(
      new BoolEncoding, new IntEncoding, new PermissionEncoding,
      new PointerEncoding, new StructEncoding, arrayEncoding, interfaceEncoding,
      new SequenceEncoding, new SetEncoding, new OptionEncoding, new DomainEncoding,
      new SliceEncoding(arrayEncoding), new PredEncoding, new ChannelEncoding, new StringEncoding,
      new MapEncoding, new MathematicalMapEncoding, new FloatEncoding,
      new AssertionEncoding, new CallEncoding, new MemoryEncoding, new ControlEncoding,
      new TerminationEncoding, new BuiltInEncoding, new OutlineEncoding,
      methodEncoding, pureMethodEncoding, predicateEncoding,
      new Comments,
    ))
  )

  val defaultEncoding: DefaultEncoding = new DefaultEncoding {
    override def method(x: in.Method)(ctx: Context): MemberWriter[vpr.Method] = methodEncoding.methodDefault(x)(ctx)
    override def function(x: in.Function)(ctx: Context): MemberWriter[vpr.Method] = methodEncoding.functionDefault(x)(ctx)
    override def pureMethod(x: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function] = pureMethodEncoding.pureMethodDefault(x)(ctx)
    override def pureFunction(x: in.PureFunction)(ctx: Context): MemberWriter[vpr.Function] = pureMethodEncoding.pureFunctionDefault(x)(ctx)
    override def mpredicate(x: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = predicateEncoding.mpredicateDefault(x)(ctx)
    override def fpredicate(x: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = predicateEncoding.fpredicateDefault(x)(ctx)
  }
}
