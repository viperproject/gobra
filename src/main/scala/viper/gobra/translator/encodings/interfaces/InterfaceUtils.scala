// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.interfaces

import viper.gobra.ast.internal.theory.TypeHead
import viper.gobra.reporting.Source
import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}

class InterfaceUtils(interfaces: InterfaceComponent, types: TypeComponent, poly: PolymorphValueComponent) {

  /** Returns [x != nil: Interface{}] */
  def receiverNotNil(recv: vpr.Exp)(ctx: Context): vpr.Exp = {
    val (pos, info: Source.Verifier.Info, errT) = recv.meta
    // In Go, checking that an interface receiver is not nil never panics.
    vpr.Not(vpr.EqCmp(recv, nilInterface()(pos, info, errT)(ctx))(pos, info, errT))(pos, info.createAnnotatedInfo(Source.ReceiverNotNilCheckAnnotation), errT)
  }

  /** Returns nil interface */
  def nilInterface()(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo)(ctx: Context): vpr.Exp = {
    val value = poly.box(vpr.NullLit()(pos, info, errT))(pos, info, errT)(ctx)
    val typ = types.typeApp(TypeHead.NilHD)(pos, info, errT)(ctx)
    interfaces.create(value, typ)(pos, info, errT)(ctx)
  }
}
