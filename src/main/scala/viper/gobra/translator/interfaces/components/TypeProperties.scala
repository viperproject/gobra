// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.components

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.interfaces.Context

trait TypeProperties {

  def underlyingType(typ: in.Type)(ctx: Context): in.Type

  def isStructType(typ: in.Type)(ctx: Context): Boolean = structType(typ)(ctx).nonEmpty

  def structType(typ: in.Type)(ctx: Context): Option[in.StructT]

  def isClassType(typ: in.Type)(ctx: Context): Boolean = classType(typ)(ctx).nonEmpty

  def classType(typ: in.Type)(ctx: Context): Option[in.StructT]

  def isStructPointerType(typ: in.Type)(ctx: Context): Boolean = structPointerType(typ)(ctx).nonEmpty

  def structPointerType(typ: in.Type)(ctx: Context): Option[in.StructT]

  def isPointerTyp(typ: in.Type)(ctx: Context): Boolean = pointerTyp(typ)(ctx).nonEmpty

  def pointerTyp(typ: in.Type)(ctx: Context): Option[in.Type]
}
