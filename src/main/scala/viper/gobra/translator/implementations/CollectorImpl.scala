// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations

import viper.gobra.translator.interfaces.Collector
import viper.silver.{ast => vpr}

class CollectorImpl extends Collector {
  protected var _domains: List[vpr.Domain] = List.empty
  protected var _fields: List[vpr.Field] = List.empty
  protected var _predicates: List[vpr.Predicate] = List.empty
  protected var _functions: List[vpr.Function] = List.empty
  protected var _methods: List[vpr.Method]  = List.empty
  protected var _extensions: List[vpr.ExtensionMember] = List.empty

  override def addMember(m: vpr.Member): Unit = m match {
    case d: vpr.Domain => _domains ::= d
    case f: vpr.Field => _fields ::= f
    case p: vpr.Predicate => _predicates ::= p
    case f: vpr.Function => _functions ::= f
    case m: vpr.Method => _methods ::= m
    case e: vpr.ExtensionMember => _extensions ::= e
  }

  override def domains: Seq[vpr.Domain] = _domains
  override def fields: Seq[vpr.Field] = _fields
  override def predicate: Seq[vpr.Predicate] = _predicates
  override def functions: Seq[vpr.Function] = _functions
  override def methods: Seq[vpr.Method] = _methods
  override def extensions: Seq[vpr.ExtensionMember] = _extensions
}
