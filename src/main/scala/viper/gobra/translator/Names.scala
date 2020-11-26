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


  // assert
  def assertFunc: String = "assertArg"

  // equality
  def equalityDomain: String = "Equality"
  def equalityFunc: String = "eq"

  // embedding domain
  def embeddingDomain: String = "Emb"
  def embeddingBoxFunc: String = "box"
  def embeddingUnboxFunc: String = "unbox"

  // pointer
  def pointerField(t : vpr.Type) : String = {
    // sanitizes type name to a valid Viper field name
    val ts = t.toString()
      .replace('[', '_')
      .replace("]", "")

    s"val$$_$ts"
  }

  // struct
  def sharedStructDomain: String = "ShStruct"
  def sharedStructDfltFunc: String = "shStructDefault"

  // array
  def sharedArrayDomain: String = "ShArray"
  def arrayConversionFunc: String = "arrayConversion"
  def arrayDefaultFunc: String = "arrayDefault"

  // slices
  def fullSliceFromArray: String = "sfullSliceFromArray"
  def fullSliceFromSlice: String = "sfullSliceFromSlice"
  def sliceConstruct: String = "sconstruct"
  def sliceDefaultFunc: String = "sliceDefault"
  def sliceFromArray: String = "ssliceFromArray"
  def sliceFromSlice: String = "ssliceFromSlice"

  // sequences
  def emptySequenceFunc: String = "sequenceEmpty"

  // unknown values
  def unknownValuesDomain: String = "UnknownValueDomain"
  def unknownValueFunc: String = "unknownValue"
}
