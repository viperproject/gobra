// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.context

import viper.gobra.ast.internal.GlobalVarDecl
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.Config
import viper.gobra.translator.encodings._
import viper.gobra.translator.encodings.adts.AdtEncoding
import viper.gobra.translator.encodings.arrays.ArrayEncoding
import viper.gobra.translator.encodings.channels.ChannelEncoding
import viper.gobra.translator.encodings.closures.ClosureEncoding
import viper.gobra.translator.encodings.combinators.{DefaultEncoding, FinalTypeEncoding, SafeTypeEncodingCombiner, TypeEncoding}
import viper.gobra.translator.encodings.interfaces.InterfaceEncoding
import viper.gobra.translator.encodings.maps.{MapEncoding, MathematicalMapEncoding}
import viper.gobra.translator.encodings.defaults.{DefaultGlobalVarEncoding, DefaultMethodEncoding, DefaultPredicateEncoding, DefaultPureMethodEncoding, DefaultTriggerExprEncoding}
import viper.gobra.translator.encodings.options.OptionEncoding
import viper.gobra.translator.encodings.preds.PredEncoding
import viper.gobra.translator.encodings.sequences.SequenceEncoding
import viper.gobra.translator.encodings.sets.SetEncoding
import viper.gobra.translator.encodings.slices.SliceEncoding
import viper.gobra.translator.encodings.structs.StructEncoding
import viper.gobra.translator.encodings.typeless._
import viper.gobra.translator.library.arrays.{Arrays, ArraysImpl}
import viper.gobra.translator.library.conditions.{Conditions, ConditionsImpl}
import viper.gobra.translator.library.equality.{Equality, EqualityImpl}
import viper.gobra.translator.library.fields.{Fields, FieldsImpl}
import viper.gobra.translator.library.fixpoints.{Fixpoint, FixpointImpl}
import viper.gobra.translator.library.multiplicity.{SeqMultiplicity, SeqMultiplicityImpl}
import viper.gobra.translator.library.options.{OptionImpl, Options}
import viper.gobra.translator.library.slices.{Slices, SlicesImpl}
import viper.gobra.translator.library.tos._
import viper.gobra.translator.library.tuples.{Tuples, TuplesImpl}
import viper.gobra.translator.library.unknowns.{UnknownValues, UnknownValuesImpl}
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
)(config: Config) extends TranslatorConfig {

  val seqToMultiset : SeqToMultiset = new SeqToMultisetImpl(seqMultiplicity)
  val optionToSeq : OptionToSeq = new OptionToSeqImpl(option)
  val slice : Slices = new SlicesImpl(array)

  val arrayEncoding: ArrayEncoding = new ArrayEncoding()

  val methodEncoding = new DefaultMethodEncoding
  val pureMethodEncoding = new DefaultPureMethodEncoding
  val predicateEncoding = new DefaultPredicateEncoding
  val globalVarEncoding = new DefaultGlobalVarEncoding
  val triggerExprEncoding = new DefaultTriggerExprEncoding

  val typeEncoding: TypeEncoding = new FinalTypeEncoding(
    new SafeTypeEncodingCombiner(Vector(
      new BoolEncoding, new IntEncoding, new PermissionEncoding,
      new PointerEncoding, new StructEncoding, arrayEncoding, new ClosureEncoding(config), new InterfaceEncoding,
      new SequenceEncoding, new SetEncoding, new OptionEncoding, new DomainEncoding, new AdtEncoding,
      new SliceEncoding(arrayEncoding), new PredEncoding, new ChannelEncoding, new StringEncoding,
      new MapEncoding, new MathematicalMapEncoding, new FloatEncoding,
      new AssertionEncoding, new CallEncoding, new MemoryEncoding, new ControlEncoding,
      new TerminationEncoding, new BuiltInEncoding, new OutlineEncoding, new DeferEncoding,
      new GlobalEncoding, new Comments,
    ), Vector(
      methodEncoding, pureMethodEncoding, predicateEncoding, globalVarEncoding, triggerExprEncoding
    ))
  )

  val defaultEncoding: DefaultEncoding = new DefaultEncoding {
    override def method(x: in.Method)(ctx: Context): MemberWriter[vpr.Method] = methodEncoding.methodDefault(x)(ctx)
    override def function(x: in.Function)(ctx: Context): MemberWriter[vpr.Method] = methodEncoding.functionDefault(x)(ctx)
    override def pureMethod(x: in.PureMethod)(ctx: Context): MemberWriter[vpr.Function] = pureMethodEncoding.pureMethodDefault(x)(ctx)
    override def pureFunction(x: in.PureFunction)(ctx: Context): MemberWriter[vpr.Function] = pureMethodEncoding.pureFunctionDefault(x)(ctx)
    override def mpredicate(x: in.MPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = predicateEncoding.mpredicateDefault(x)(ctx)
    override def fpredicate(x: in.FPredicate)(ctx: Context): MemberWriter[vpr.Predicate] = predicateEncoding.fpredicateDefault(x)(ctx)
    override def globalVarDeclaration(x: GlobalVarDecl)(ctx: Context): MemberWriter[Vector[vpr.Function]] = globalVarEncoding.globalVarDeclarationDefault(x)(ctx)
  }
}
