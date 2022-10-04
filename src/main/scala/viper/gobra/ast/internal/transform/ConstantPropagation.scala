// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.ast.internal.transform

import viper.gobra.ast.{internal => in}
import viper.gobra.util.Violation

object ConstantPropagation extends InternalTransform {
  override def name(): String = "constant_propagation"

  /**
    * Program-to-program transformation
    */
  override def transform(p: in.Program): in.Program = {
    val (constDecls, noConstDecls) = p.members partition(_.isInstanceOf[in.GlobalConstDecl])
    val progNoConsts = in.Program(
      types = p.types,
      members = noConstDecls,
      table = p.table,
    )(p.info)

    progNoConsts transform {
      case c: in.GlobalConst =>
        val litOpt = constDecls collectFirst { case in.GlobalConstDecl(l, r) if l == c => r.withInfo(c.info) }
        litOpt match {
          case Some(l) => l
          case _ => Violation.violation(s"Did not find declaration of constant $c")
        }
    }
  }
}