package viper.gobra.translator.encodings.structs

import viper.gobra.translator.interfaces.translator.Generator
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}

trait ExclusiveStructComponent extends Generator {

  /** Returns type of shared-struct domain. */
  def typ(args: Vector[vpr.Type])(ctx: Context): vpr.Type

  /** Constructor of shared-struct domain. */
  def create(args: Vector[vpr.Exp])(src: in.Node)(ctx: Context): vpr.Exp

  /** Getter of shared-struct domain. */
  def get(base: vpr.Exp, idx: Int, arity: Int)(src: in.Node)(ctx: Context): vpr.Exp

  /**
    * Update function of shared-struct domain.
    *
    * The default implementation is:
    * update(e, i, v, n) -> create( get(e, 0, n), ..., get(e, i-1, n), v, get(e, i+1, n), ..., get(e, n-1, n) )
    * */
  def update(base: vpr.Exp, idx: Int, newVal: vpr.Exp, arity: Int)(src: in.Node)(ctx: Context): vpr.Exp = {
    val lowerArgs = (0 until idx).map(l => get(base, l, arity)(src)(ctx)).toVector
    val higherArgs = ((idx + 1) until arity).map(l => get(base, l, arity)(src)(ctx)).toVector
    create(lowerArgs ++ (newVal +: higherArgs))(src)(ctx)
  }
}
