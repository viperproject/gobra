// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.ast.internal.theory.TypeHead
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}

/** Encoding of Gobra types into Viper expressions. */
trait TypeComponent extends Generator {

  /** Type of viper expressions encoding Gobra types.  */
  def typ()(ctx: Context): vpr.Type

  /** Translates Gobra types into Viper type expressions. */
  def typeToExpr(typ: in.Type)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Generates precise equality axioms for 'typ'. */
  def genPreciseEqualityAxioms(typ: in.Type)(ctx: Context): Unit

  /** Behavioral subtype relation. */
  def behavioralSubtype(subType: vpr.Exp, superType: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Function returning whether a type is comparable. */
  def isComparableType(typ: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp

  /** Constructor for Viper type expressions. */
  def typeApp(head: TypeHead, args: Vector[vpr.Exp] = Vector.empty)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp
}
