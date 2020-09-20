package viper.gobra.translator.encodings.arrays

import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import ArrayEncoding.ComponentParameter

trait ExclusiveArrayComponent extends Generator {

  /** Returns type of exclusive-array domain. */
  def typ(t: ComponentParameter)(ctx: Context): vpr.Type

  /** Constructor of shared-array domain. */
  def create(args: Vector[vpr.Exp], t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Getter of exclusive-array domain. */
  def get(base: vpr.Exp, idx: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Update function of shared-array domain. */
  def update(base: vpr.Exp, idx: vpr.Exp, newVal: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

}
