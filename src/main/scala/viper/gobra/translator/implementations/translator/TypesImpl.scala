// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

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
    * [nil]    -> ref
    * [S]      -> ??? // TODO: will be the tuple type
    * [void] undef
    */
  override def translate(x: in.Type)(ctx: Context): vpr.Type = x match {
    case in.BoolT => vpr.Bool
    case in.IntT  => vpr.Int
    case in.PermissionT => vpr.Perm
    case t: in.DefinedT => translate(ctx.typeProperty.underlyingType(t)(ctx))(ctx)
    case in.PointerT(_) => vpr.Ref
    case in.NilT => vpr.Ref
    case st: in.StructT => vpr.Int // TODO
    case in.TupleT(ts) => Violation.violation("Tuple types are not supported at this point in time")

    case in.VoidT => Violation.violation("void is not a translatable type")
  }


}
