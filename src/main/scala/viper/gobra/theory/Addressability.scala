package viper.gobra.theory

sealed trait Addressability

object Addressability {

  case object Shared extends Addressability
  case object Exclusive extends Addressability



  val pointerBase: Addressability = Shared
  def field(structAddressability: Addressability): Addressability = structAddressability
  def arrayElement(arrayAddressability: Addressability): Addressability = arrayAddressability
  val sliceElement: Addressability = arrayElement(pointerBase)
  val mapValue: Addressability = Exclusive
  val mathDataStructureElement: Addressability = Exclusive
  def underlying(definedAddressability: Addressability): Addressability = definedAddressability



  val sharedVariable: Addressability = Shared
  val exclusiveVariable: Addressability = Exclusive
  private val parameter: Addressability = Exclusive
  val inParameter: Addressability = parameter
  val outParameter: Addressability = parameter
  val receiver: Addressability = parameter
  val boundVariable: Addressability = Exclusive
  val constant: Addressability = Exclusive

  val dereference: Addressability = pointerBase
  def fieldLookup(receiver: Addressability): Addressability = field(receiver)
  def arrayLookup(receiver: Addressability): Addressability = arrayElement(receiver)
  val sliceLookup: Addressability = sliceElement
  val mapLookup: Addressability = mapValue

  val rValue: Addressability = Exclusive

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

}
