// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import viper.gobra.ast.internal._
import viper.gobra.reporting.Source.Parser.Internal
import viper.gobra.theory.Addressability
import viper.gobra.util.TypeBounds

/**
  * The polymorphic value encoding of interfaces states that boxing is a bijection between a type and Ref only for
  * types that are known to have infinitely many values; for finite types, this would bound the number of
  * references and make the axioms inconsistent. Types of unknown cardinality have to be treated like finite ones.
  */
class PolymorphValueCardinalityUnitTests extends AnyFunSuite with Matchers {
  private val exclusive = Addressability.Exclusive
  private val intT: Type = IntT(exclusive, TypeBounds.DefaultInt)
  private val boolT: Type = BoolT(exclusive)
  private val unitT: Type = DomainT("unit", exclusive)
  private val colorT: Type = AdtT("color", "color", exclusive)
  private val definedUnitT: DefinedT = DefinedT("definedUnit", exclusive)
  private val definedIntT: DefinedT = DefinedT("definedInt", exclusive)
  private val lookup: DefinedT => Type = Map(definedUnitT -> unitT, definedIntT -> intT)

  private def field(name: String, typ: Type): Field = Field(name, typ, ghost = false)(Internal)
  private def structT(fields: Field*): Type = StructT(fields.toVector, ghost = false, exclusive)
  private def isInfinite(typ: Type): Boolean = PolymorphValueComponentImpl.infiniteCardinality(typ)(lookup)

  private def checkAll(expected: Boolean, types: Type*): Unit =
    types foreach { t => withClue(s"$t: ") { isInfinite(t) shouldBe expected } }

  test("Cardinality: types encoded as Int, Perm, or Ref are infinite") {
    checkAll(expected = true,
      intT, Float32T(exclusive), Float64T(exclusive), StringT(exclusive), PermissionT(exclusive),
      PointerT(BoolT(Addressability.Shared), exclusive), MapT(boolT, boolT, exclusive), ChannelT(boolT, exclusive),
      BoolT(Addressability.Shared), definedIntT
    )
  }

  test("Cardinality: sequences, multisets, slices, and interfaces are infinite for any component types") {
    checkAll(expected = true,
      SequenceT(boolT, exclusive), MultisetT(boolT, exclusive), SliceT(boolT, exclusive), InterfaceT("I", exclusive)
    )
  }

  test("Cardinality: composite types are infinite iff a component with unbounded multiplicity is infinite") {
    checkAll(expected = true,
      SetT(intT, exclusive), MathMapT(boolT, intT, exclusive), MathMapT(intT, boolT, exclusive), ArrayT(2, intT, exclusive),
      structT(field("b", boolT), field("n", intT)), TupleT(Vector(boolT, intT), exclusive), OptionT(intT, exclusive)
    )
    checkAll(expected = false,
      SetT(boolT, exclusive), MathMapT(boolT, boolT, exclusive), ArrayT(0, intT, exclusive), ArrayT(2, boolT, exclusive),
      structT(), structT(field("b", boolT), field("u", unitT)), TupleT(Vector(boolT, colorT), exclusive),
      OptionT(boolT, exclusive), SetT(unitT, exclusive)
    )
  }

  test("Cardinality: finite types and types of unknown cardinality are not infinite") {
    checkAll(expected = false,
      boolT, VoidT, unitT, colorT, definedUnitT,
      FunctionT(Vector.empty, Vector(intT), exclusive), PredT(Vector(intT), exclusive)
    )
  }
}
