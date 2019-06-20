package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.translator.Types
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class TypesImpl extends Types {

  override def finalize(col: Collector): Unit = ()

  /**
    * [bool]   -> bool
    * [int]    -> int
    * [frac]   -> perm
    * [n := t] -> [t]
    * [*t]     -> ref
    * [void] undef
    */
  override def translate(x: in.Type)(ctx: Context): vpr.Type = x match {
    case in.BoolT => vpr.Bool
    case in.IntT  => vpr.Int
    case in.FracT => vpr.Perm
    case in.DefinedT(_, t) => translate(t)(ctx)
    case in.PointerT(_) => vpr.Ref

    case in.VoidT => Violation.violation("void is not a translatable type")
  }
}
