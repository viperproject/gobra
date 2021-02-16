// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces
import viper.silver.{ast => vpr}

trait Collector {
  def addMember(m: vpr.Member): Unit

  def domains: Seq[vpr.Domain]
  def fields: Seq[vpr.Field]
  def predicate: Seq[vpr.Predicate]
  def functions: Seq[vpr.Function]
  def methods: Seq[vpr.Method]
  def extensions: Seq[vpr.ExtensionMember]
}
