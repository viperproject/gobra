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
  def embeddingDomain: String = "emb"
  def embeddingBoxFunc: String = "box"
  def embeddingUnboxFunc: String = "unbox"

  // pointer
  def pointerField(t: vpr.Type) : String = s"val$$_$t"

  // array
  def arrayConversionFunc: String = "arrayConversion"
  def arrayDefaultFunc: String = "arrayDefault"



  def fieldExtension(base: String, ext: String): String = s"${base}_$ext" // remove
  def inlinedVar(base: String, idx: Int): String = s"${base}_$idx" // remove
  def addressableField(base: String): String = s"${base}R" // remove
  def nonAddressableField(base: String): String = s"${base}V" // remove
}
