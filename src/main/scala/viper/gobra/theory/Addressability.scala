// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.theory

sealed trait Addressability {
  def isShared: Boolean
  def isExclusive: Boolean = !isShared
  def pretty: String
}

/** Implements the logic of addressability. */
object Addressability {

  /**
    * We extend the type system of Go by introducing an additional type modifier for all types.
    * We refer to this modifier also as 'addressability modifier'.
    * The addressability modifier is either 'shared' or 'exclusive'.
    * An expression of a shared type can be aliased.
    * Such an expression can be referenced (&e) or captured by a closure.
    * Conversely, an expression of an exclusive type cannot be aliased.
    * Programs where an exclusive expression is referenced or captured are rejected by Gobra's type checker.
    *
    * As a consequence, operating on exclusive expressions does not require permissions,
    * whereas operating on shared expressions does require permissions.
    *
    * In Gobra programs, the addressability modifier is not written for types, but inferred from the context.
    * However, a variable declaration is annotated with @ to declare that the variable has a shared type,
    * e.g. 'x@ := 42; p := &x'. The modifier of a variable could be inferred, but we decided against this
    * because from our experience, then users can have problems to identify whether a variables is shared or exclusive.
    */

  /** Addressability modifier. Expressions of shared type can be aliased. */
  case object Shared extends Addressability {
    override val isShared: Boolean = true
    override val pretty: String = "@"
  }
  /** Addressability modifier. Expressions of exclusive type cannot be aliased. */
  case object Exclusive extends Addressability {
    override val isShared: Boolean = false
    override val pretty: String = "."
  }


  /**
    * Not all combinations of type and addressability modifier are allowed.
    * 1) For pointer types, the base type is always shared. This is over-approximation.
    * 2) For structs, the addressability of a field is the same as the addressability of the struct.
    *    This is a design decision to simplify the type system.
    * 3) Similarly, for arrays, the addressability of an index is the same as the addressability of the array.
    * 4) Slices behave the same as pointers to arrays. As such, elements of a slice are always shared.
    * 5) For maps, by definition of Go, map keys and values can not be referenced or captured. Thus they are exclusive.
    * 6) Similarly, elements of mathematical data structures are not associated with an address and thus are exclusive.
    * 7) For channels, a sent message can only be received a single time and thus the channel's element type is exclusive.
    * 8) For type definitions, the addressability modifier propagates through the type definition.
    *
    * Because of these definitions, the addressability of a nested type is determined by its outer type.
    * In our writing, we sometimes drop the addressability modifier of nested types,
    * e.g. we write 'struct{ f int }@' instead of 'struct{ f int@ }@', where @ is the shared modifier.
    */

  val pointerBase: Addressability = Shared
  def field(structAddressability: Addressability): Addressability = structAddressability
  def arrayElement(arrayAddressability: Addressability): Addressability = arrayAddressability
  val sliceElement: Addressability = arrayElement(pointerBase)
  val mapKey: Addressability = Exclusive
  val mapValue: Addressability = Exclusive
  val mathDataStructureElement: Addressability = Exclusive
  val channelElement: Addressability = Exclusive
  def underlying(definedAddressability: Addressability): Addressability = definedAddressability


  /**
    * We infer whether an expression has a shared of exclusive type using the following rules:
    * 1) Variables are annotated as shared or exclusive variables.
    * 2) Parameters are always exclusive. Parameters can not be aliased by a caller.
    *    As a special case, we copy parameters that are aliased in the body of a function.
    * 3) R-values exclusive, i.e. values that do not occupy some identifiable location in memory.
    * 4) All access functions (e.g. field access, array access, etc) are defined by our constraints on types.
    */

  val sharedVariable: Addressability = Shared
  val exclusiveVariable: Addressability = Exclusive
  private val parameter: Addressability = Exclusive
  val inParameter: Addressability = parameter
  val outParameter: Addressability = parameter
  val receiver: Addressability = parameter

  val rValue: Addressability = Exclusive

  val boundVariable: Addressability = rValue
  val constant: Addressability = rValue

  val dereference: Addressability = pointerBase
  def fieldLookup(receiver: Addressability): Addressability = field(receiver)
  def arrayLookup(receiver: Addressability): Addressability = arrayElement(receiver)
  val sliceLookup: Addressability = sliceElement
  val variadicLookup: Addressability = sliceElement
  val mapLookup: Addressability = mapValue

  val reference: Addressability = rValue
  val sliceExpr: Addressability = rValue

  val callResult: Addressability = rValue
  val conversionResult: Addressability = rValue
  val typeAssertionResult: Addressability = rValue
  val receive: Addressability = rValue

  val defaultValue: Addressability = rValue
  val literal: Addressability = rValue
  val unit: Addressability = rValue
  val nil: Addressability = rValue

  val mathDataStructureLookup: Addressability = mathDataStructureElement

  def unfolding(bodyAddressability: Addressability): Addressability = bodyAddressability
  val old: Addressability = rValue
  val make: Addressability = Exclusive

}
