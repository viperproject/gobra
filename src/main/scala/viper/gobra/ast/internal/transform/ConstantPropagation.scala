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

  override def transform(p: in.Program): in.Program = {
    val (constDecls, noConstDecls) = p.members.partition(_.isInstanceOf[in.GlobalConstDecl])
    def propagate[T <: in.Node](n: T): T = n.transform{
      case c: in.GlobalConst =>
        val litOpt = constDecls collectFirst { case in.GlobalConstDecl(l, r) if l == c => r.withInfo(c.info) }
        litOpt match {
          case Some(l) => l
          case _ => Violation.violation(s"Did not find declaration of constant $c")
        }
    }

    // TODO: duplicated work can be avoided - currently, propagate is applied twice per member
    val newTable = new in.LookupTable(
      definedTypes = p.table.definedTypes,
      definedMethods = p.table.definedMethods.view.mapValues(propagate).toMap,
      definedFunctions = p.table.definedFunctions.view.mapValues(propagate).toMap,
      definedMPredicates = p.table.definedMPredicates.view.mapValues(propagate).toMap,
      definedFPredicates = p.table.definedFPredicates.view.mapValues(propagate).toMap,
      definedFuncLiterals = p.table.definedFuncLiterals.view.mapValues(propagate).toMap,
      directMemberProxies = p.table.directMemberProxies,
      directInterfaceImplementations = p.table.directInterfaceImplementations,
      implementationProofPredicateAliases = p.table.implementationProofPredicateAliases,
    )

    in.Program(
      types = p.types,
      members = noConstDecls.map(propagate), // does not emit constant declarations
      table = newTable,
    )(p.info)
  }
}