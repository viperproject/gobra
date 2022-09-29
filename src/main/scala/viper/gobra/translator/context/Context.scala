// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.context

import viper.gobra.ast.internal._
import viper.gobra.translator.encodings.combinators.{DefaultEncoding, TypeEncoding}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.gobra.ast.{internal => in}
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
import viper.silver.{ast => vpr}

trait Context {

  // components
  def field: Fields

  def array: Arrays

  def seqToSet: SeqToSet

  def seqToMultiset: SeqToMultiset

  def seqMultiplicity: SeqMultiplicity

  def option: Options

  def optionToSeq: OptionToSeq

  def slice: Slices

  def fixpoint: Fixpoint

  def tuple: Tuples

  def equality: Equality

  def condition: Conditions

  def unknownValue: UnknownValues

  // translator
  protected def typeEncoding: TypeEncoding

  def defaultEncoding: DefaultEncoding

  def typ(x: in.Type): vpr.Type = typeEncoding.typ(this)(x)

  def variable(x: in.BodyVar): vpr.LocalVarDecl = typeEncoding.variable(this)(x)

  def member(x: in.Member): MemberWriter[Vector[vpr.Member]] = typeEncoding.member(this)(x)

  def method(x: in.Member): MemberWriter[vpr.Method] = typeEncoding.finalMethod(this)(x)

  def function(x: in.Member): MemberWriter[vpr.Function] = typeEncoding.finalFunction(this)(x)

  def predicate(x: in.Member): MemberWriter[vpr.Predicate] = typeEncoding.finalPredicate(this)(x)

  def globalVarDeclaration(x: in.Member): MemberWriter[Vector[vpr.Function]] = typeEncoding.finalGlobalVarDeclatarion(this)(x)

  def varPrecondition(x: in.Parameter.In): Option[MemberWriter[vpr.Exp]] = typeEncoding.varPrecondition(this).lift(x)

  def varPostcondition(x: in.Parameter.Out): Option[MemberWriter[vpr.Exp]] = typeEncoding.varPostcondition(this).lift(x)

  def initialization(x: in.Location): CodeWriter[vpr.Stmt] = typeEncoding.initialization(this)(x)

  def assignment(x: in.Assignee, rhs: in.Expr)(src: in.Node): CodeWriter[vpr.Stmt] = typeEncoding.assignment(this)(x, rhs, src)

  def equal(lhs: in.Expr, rhs: in.Expr)(src: in.Node): CodeWriter[vpr.Exp] = typeEncoding.equal(this)(lhs, rhs, src)

  def goEqual(lhs: in.Expr, rhs: in.Expr)(src: in.Node): CodeWriter[vpr.Exp] = typeEncoding.goEqual(this)(lhs, rhs, src)

  def expression(x: in.Expr): CodeWriter[vpr.Exp] = typeEncoding.finalExpression(this)(x)

  def assertion(x: in.Assertion): CodeWriter[vpr.Exp] = typeEncoding.finalAssertion(this)(x)

  def invariant(x: in.Assertion): (CodeWriter[Unit], vpr.Exp) = typeEncoding.invariant(this)(x)

  def precondition(x: in.Assertion): MemberWriter[vpr.Exp] = typeEncoding.precondition(this)(x)

  def postcondition(x: in.Assertion): MemberWriter[vpr.Exp] = typeEncoding.postcondition(this)(x)

  def reference(x: in.Location): CodeWriter[vpr.Exp] = typeEncoding.reference(this)(x)

  def safeReference(x: in.Location): CodeWriter[vpr.Exp] = typeEncoding.safeReference(this)(x)

  def footprint(x: in.Location, perm: in.Expr): CodeWriter[vpr.Exp] = typeEncoding.addressFootprint(this)(x, perm)

  def isComparable(x: in.Expr): Either[Boolean, CodeWriter[vpr.Exp]] = typeEncoding.isComparable(this)(x)

  def statement(x: in.Stmt): CodeWriter[vpr.Stmt] = typeEncoding.finalStatement(this)(x)

  // lookup
  def table: LookupTable

  def lookup(t: in.DefinedT): in.Type = table.lookup(t)

  def lookup(f: in.FunctionProxy): in.FunctionMember = table.lookup(f) match {
    case fm: FunctionMember => fm
    case bf: BuiltInFunction => typeEncoding.builtInFunction(this)(bf)
  }

  def lookup(m: in.MethodProxy): in.MethodMember = table.lookup(m) match {
    case mm: MethodMember => mm
    case bm: BuiltInMethod => typeEncoding.builtInMethod(this)(bm)
  }

  def lookup(p: in.MPredicateProxy): in.MPredicate = table.lookup(p) match {
    case mp: MPredicate => mp
    case bmp: BuiltInMPredicate => typeEncoding.builtInMPredicate(this)(bmp)
  }

  def lookup(p: in.FPredicateProxy): in.FPredicate = table.lookup(p) match {
    case fp: FPredicate => fp
    case bfp: BuiltInFPredicate => typeEncoding.builtInFPredicate(this)(bfp)
  }

  def underlyingType(t: in.Type): in.Type = t match {
    case t: in.DefinedT => underlyingType(lookup(t))
    case t => t
  }

  // mapping
  def addVars(vars: vpr.LocalVarDecl*): Context

  // fresh variable counter
  /** publicly exposed infinite iterator providing fresh names */
  def freshNames: Iterator[String] = internalFreshNames

  /** internal fresh name iterator that additionally provides a getter function for its counter value */
  protected def internalFreshNames: Context.FreshNameIterator

  /** copy constructor */
  def :=(
          fieldN: Fields = field,
          arrayN: Arrays = array,
          seqToSetN: SeqToSet = seqToSet,
          seqToMultisetN: SeqToMultiset = seqToMultiset,
          seqMultiplicityN: SeqMultiplicity = seqMultiplicity,
          optionN: Options = option,
          optionToSeqN: OptionToSeq = optionToSeq,
          sliceN: Slices = slice,
          fixpointN: Fixpoint = fixpoint,
          tupleN: Tuples = tuple,
          equalityN: Equality = equality,
          conditionN: Conditions = condition,
          unknownValueN: UnknownValues = unknownValue,
          typeEncodingN: TypeEncoding = typeEncoding,
          defaultEncodingN: DefaultEncoding = defaultEncoding,
          initialFreshCounterValueN: Option[Int] = None,
        ): Context = {
    update(fieldN, arrayN, seqToSetN, seqToMultisetN, seqMultiplicityN, optionN, optionToSeqN, sliceN, fixpointN, tupleN, equalityN, conditionN, unknownValueN, typeEncodingN, defaultEncodingN, initialFreshCounterValueN)
  }

  protected def update(
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
        ): Context


  def finalize(col: Collector): Unit = {
    // components
    col.finalize(field)
    col.finalize(array)
    col.finalize(seqToSet)
    col.finalize(seqToMultiset)
    col.finalize(seqMultiplicity)
    col.finalize(option)
    col.finalize(optionToSeq)
    col.finalize(slice)
    col.finalize(fixpoint)
    col.finalize(tuple)
    col.finalize(equality)
    col.finalize(condition)
    col.finalize(unknownValue)

    // translators
    col.finalize(typeEncoding)
  }

}

object Context {
  trait FreshNameIterator extends Iterator[String] {
    def getValue: Int
  }
}