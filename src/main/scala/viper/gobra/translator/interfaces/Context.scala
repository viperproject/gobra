// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces

import viper.gobra.ast.internal.{BuiltInFPredicate, BuiltInFunction, BuiltInMPredicate, BuiltInMethod, FPredicate, FunctionMember, LookupTable, MPredicate, MethodMember}
import viper.gobra.translator.interfaces.components._
import viper.gobra.translator.interfaces.translator._
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.TypeEncoding

trait Context {

  // components
  def field: Fields
  def array : Arrays
  def seqToSet : SeqToSet
  def seqToMultiset : SeqToMultiset
  def seqMultiplicity : SeqMultiplicity
  def option : Options
  def optionToSeq : OptionToSeq
  def slice : Slices
  def fixpoint: Fixpoint
  def tuple: Tuples
  def equality: Equality
  def condition: Conditions
  def unknownValue: UnknownValues

  // translator
  def typeEncoding: TypeEncoding
  def ass: Assertions
  def expr: Expressions
  def method: Methods
  def pureMethod: PureMethods
  def predicate: Predicates
  def builtInMembers: BuiltInMembers
  def stmt: Statements

  // lookup
  def table: LookupTable
  def lookup(t: in.DefinedT): in.Type = table.lookup(t)
  def lookup(f: in.FunctionProxy): in.FunctionMember = table.lookup(f) match {
    case fm: FunctionMember => fm
    case bf: BuiltInFunction => builtInMembers.function(bf)(this)
  }
  def lookup(m: in.MethodProxy): in.MethodMember = table.lookup(m) match {
    case mm: MethodMember => mm
    case bm: BuiltInMethod => builtInMembers.method(bm)(this)
  }
  def lookup(p: in.MPredicateProxy): in.MPredicate = table.lookup(p) match {
    case mp: MPredicate => mp
    case bmp: BuiltInMPredicate => builtInMembers.mpredicate(bmp)(this)
  }
  def lookup(p: in.FPredicateProxy): in.FPredicate = table.lookup(p) match {
    case fp: FPredicate => fp
    case bfp: BuiltInFPredicate => builtInMembers.fpredicate(bfp)(this)
  }

  // mapping

  def addVars(vars: vpr.LocalVarDecl*): Context

  /** copy constructor */
  def :=(
          fieldN: Fields = field,
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
          stmtN: Statements = stmt
         ): Context


  def finalize(col : Collector): Unit = {
    // components
    field.finalize(col)
    array.finalize(col)
    seqToSet.finalize(col)
    seqToMultiset.finalize(col)
    seqMultiplicity.finalize(col)
    option.finalize(col)
    optionToSeq.finalize(col)
    slice.finalize(col)
    fixpoint.finalize(col)
    tuple.finalize(col)
    equality.finalize(col)
    condition.finalize(col)
    unknownValue.finalize(col)

    // translators
    typeEncoding.finalize(col)
    ass.finalize(col)
    expr.finalize(col)
    method.finalize(col)
    pureMethod.finalize(col)
    predicate.finalize(col)
    builtInMembers.finalize(col)
    stmt.finalize(col)
  }
}
