package viper.gobra.translator.encodings.arrays

import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.silver.{ast => vpr}
import ArrayEncoding.ComponentParameter

trait SharedArrayComponent extends Generator {

  /** Returns type of exclusive-array domain. */
  def typ(t: ComponentParameter)(ctx: Context): vpr.Type

  /** Getter of shared-array domain. */
  def get(base: vpr.Exp, idx: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /** Length of shared-array domain. */
  def length(arg: vpr.Exp, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp
}
