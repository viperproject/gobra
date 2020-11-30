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

  // polymorph-value domain
  def polyValueDomain: String = "Poly"
  def polyValueBoxFunc: String = "box"
  def polyValueUnboxFunc: String = "unbox"

  // interface
  def emptyInterface: String = "empty_interface"

  // pointer
  def pointerField(t: vpr.Type) : String = s"val$$_$t"

  // struct
  def sharedStructDomain: String = "ShStruct"
  def sharedStructDfltFunc: String = "shStructDefault"

  // types
  def typesDomain: String = "Types"

  // array
  def sharedArrayDomain: String = "ShArray"
  def arrayConversionFunc: String = "arrayConversion"
  def arrayDefaultFunc: String = "arrayDefault"

  // unknown values
  def unknownValuesDomain: String = "UnknownValueDomain"
  def unknownValueFunc: String = "unknownValue"

}
