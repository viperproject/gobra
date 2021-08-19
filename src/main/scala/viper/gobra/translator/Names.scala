// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator

import viper.silver.{ast => vpr}

object Names {
  def returnLabel: String = "returnLabel"

  private var freshCounter = 0
  def freshName: String = {
    val str = s"fn$$$$$freshCounter"
    freshCounter += 1
    str
  }

  /* sanitizes type name to a valid Viper name */
  def serializeType(t: vpr.Type): String = {
    t.toString()
      .replace('[', '_')
      .replace("]", "")
      .replace(",", "") // a parameterized Viper type uses comma-space separated types if there are multiple
      .replace(" ", "")
  }


  // assert
  def assertFunc: String = "assertArg1"
  def typedAssertFunc(t: vpr.Type): String = s"assertArg2_${serializeType(t)}"

  // equality
  def equalityDomain: String = "Equality"
  def equalityFunc: String = "eq"

  // embedding domain
  def embeddingDomain: String = "Emb"
  def embeddingBoxFunc: String = "box"
  def embeddingUnboxFunc: String = "unbox"

  // polymorph-value domain
  def polyValueDomain: String = "Poly"
  def polyValueBoxFunc: String = "box"
  def polyValueUnboxFunc: String = "unbox"

  // interface
  def emptyInterface: String = "empty_interface"
  def toInterfaceFunc: String = "toInterface"
  def typeOfFunc: String = "typeOfInterface"
  def dynamicPredicate: String = "dynamic_pred"
  def implicitThis: String = "thisItf"

  // pointer
  def pointerField(t : vpr.Type) : String = {
    s"val$$_${serializeType(t)}"
  }

  // struct
  def sharedStructDomain: String = "ShStruct"
  def sharedStructDfltFunc: String = "shStructDefault"
  def getterFunc(i:Int,arity:Int): String = s"get${i}of$arity"

  // types
  def typesDomain: String = "Types"
  def stringsDomain: String = "String"
  def mapsDomain: String = "GobraMap"

  // array
  def sharedArrayDomain: String = "ShArray"
  def arrayConversionFunc: String = "arrayConversion"
  def arrayDefaultFunc: String = "arrayDefault"
  def location(domain: String) = s"${domain}loc" //  function ShArrayloc(a: ShArray[T], i: Int): T
  def length(domain: String) = s"${domain}len" // function ShArraylen(a: ShArray[T]): Int
  def first(domain: String) = s"${domain}first" // function ShArrayfirst(r: T): ShArray[T]
  def second(domain: String) = s"${domain}second" // function ShArraysecond(r: T): Int

  // slices
  def fullSliceFromArray: String = "sfullSliceFromArray"
  def fullSliceFromSlice: String = "sfullSliceFromSlice"
  def sliceConstruct: String = "sconstruct"
  def sliceDefaultFunc: String = "sliceDefault"
  def sliceFromArray: String = "ssliceFromArray"
  def sliceFromSlice: String = "ssliceFromSlice"
  def sliceArray :String = "sarray" //function sarray(s: Slice[T]): ShArray[T]
  def sliceOffset :String = "soffset" // function soffset(s: Slice[T]): Int
  def sliceLength :String = length("s") // function slen(s: Slice[T]): Int
  def sliceCapacity :String = "scap" //function scap(s: Slice[T]): Int

  // sequences
  def emptySequenceFunc: String = "sequenceEmpty"

  // predicate
  def predDomain: String = "Pred"

  // domain
  def dfltDomainValue(domainName: String): String = s"dflt$domainName"

  // unknown values
  def unknownValuesDomain: String = "UnknownValueDomain"
  def unknownValueFunc: String = "unknownValue"

  // built-in members
  def builtInMember: String = "built_in"

  //strings
  def stringLength: String = "strLen" //  function strLen(id: Int): Int ---- ? TODO: possibly make this Stinglen aka length("String")?
  def stringConact: String = "strConcat" //  function strConcat(l: Int, r: Int): Int
  def stringPrefix: String = "stringLit" 
  

  //options TODO: maybe extract "opt"
  def optionSome: String = "optSome" //function optSome(e: T): Option[T]
  def optionNone: String = "optNone" //function optNone(): Option[T]
  def optionGet : String = "optGet" //function optGet(o: Option[T]): T
  def optionIsNone:String= "optIsNone" //function optIsNone(o: Option[T]): Bool
  // ints
  def bitwiseAnd: String = "intBitwiseAnd"
  def bitwiseOr: String = "intBitwiseOr"
  def bitwiseXor: String = "intBitwiseXor"
  def bitClear: String = "intBitClear"
  def shiftLeft: String = "intShiftLeft"
  def shiftRight: String = "intShiftRight"
  def bitwiseNeg: String = "intBitwiseNeg"
}
