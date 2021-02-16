// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.arrays

import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import ArrayEncoding.ComponentParameter

trait ExclusiveArrayComponent extends Generator {

  /** Returns type of exclusive-array domain. */
  def typ(t: ComponentParameter)(ctx: Context): vpr.Type

  /** Constructor of exclusive-array domain. */
  def create(args: Vector[vpr.Exp], t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Getter of exclusive-array domain. */
  def get(base: vpr.Exp, idx: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Update function of shared-array domain. */
  def update(base: vpr.Exp, idx: vpr.Exp, newVal: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Batch update function of shared-array domain. */
  def update(base : vpr.Exp, elems : Map[vpr.Exp, vpr.Exp], t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Length of exclusive-array domain. */
  def length(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Returns an exclusive array from a sequence. */
  def fromSeq(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Returns argument as sequence. */
  def toSeq(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp
}
