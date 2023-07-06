package viper.gobra.frontend.info.implementation.typing.modifiers

import Modifier._

sealed trait OwnerModifier extends Modifier

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

object OwnerModifier {
  case object Shared extends OwnerModifier
  case object Exclusive extends OwnerModifier

  /**
    * Not all combinations of type and addressability modifier are allowed.
    * 1) For pointer types, the base type is always shared. This is over-approximation.
    * 2) For structs, the addressability of a field is the same as the addressability of the struct.
    * This is a design decision to simplify the type system.
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

  val pointerBase: OwnerModifier = Shared

  def field(structOwnerModifier: OwnerModifier): OwnerModifier = structOwnerModifier

  def arrayElement(arrayOwnerModifier: OwnerModifier): OwnerModifier = arrayOwnerModifier

  val sliceElement: OwnerModifier = arrayElement(pointerBase)
  val mapKey: OwnerModifier = Exclusive
  val mapValue: OwnerModifier = Exclusive
  val mathDataStructureElement: OwnerModifier = Exclusive
  val channelElement: OwnerModifier = Exclusive

  def underlying(definedOwnerModifier: OwnerModifier): OwnerModifier = definedOwnerModifier


  /**
    * We infer whether an expression has a shared of exclusive type using the following rules:
    * 1) Variables are annotated as shared or exclusive variables.
    * 2) Parameters are always exclusive. Parameters can not be aliased by a caller.
    * As a special case, we copy parameters that are aliased in the body of a function.
    * 3) R-values exclusive, i.e. values that do not occupy some identifiable location in memory.
    * 4) All access functions (e.g. field access, array access, etc) are defined by our constraints on types.
    */

  val sharedVariable: OwnerModifier = Shared
  val exclusiveVariable: OwnerModifier = Exclusive
  private val parameter: OwnerModifier = Exclusive
  val inParameter: OwnerModifier = parameter
  val outParameter: OwnerModifier = parameter
  val receiver: OwnerModifier = parameter

  val rValue: OwnerModifier = Exclusive

  val boundVariable: OwnerModifier = rValue
  val constant: OwnerModifier = rValue
  val wildcard: OwnerModifier = rValue

  val dereference: OwnerModifier = pointerBase

  def fieldLookup(receiver: OwnerModifier): OwnerModifier = field(receiver)

  def arrayLookup(receiver: OwnerModifier): OwnerModifier = arrayElement(receiver)

  val sliceLookup: OwnerModifier = sliceElement
  val variadicLookup: OwnerModifier = sliceElement
  val mapLookup: OwnerModifier = mapValue

  val reference: OwnerModifier = rValue
  val sliceExpr: OwnerModifier = rValue

  val callResult: OwnerModifier = rValue
  val conversionResult: OwnerModifier = rValue
  val typeAssertionResult: OwnerModifier = rValue
  val receive: OwnerModifier = rValue

  val defaultValue: OwnerModifier = rValue
  val literal: OwnerModifier = rValue
  val iota: OwnerModifier = rValue
  val unit: OwnerModifier = rValue
  val nil: OwnerModifier = rValue

  val mathDataStructureLookup: OwnerModifier = mathDataStructureElement

  def unfolding(bodyOwnerModifier: OwnerModifier): OwnerModifier = bodyOwnerModifier

  val old: OwnerModifier = rValue
  val make: OwnerModifier = Exclusive
  val exprInAcc: OwnerModifier = Exclusive
}
