// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.closures

import viper.gobra.translator.Names
import viper.silver.{ast => vpr}

// Encodes the Closure domain, if needed
class ClosureDomainEncoder(specs: ClosureSpecsEncoder) {

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    if (domainNeeded) {
      addMemberFn(vprDomain)
    }
  }

  private var domainNeeded: Boolean = false
  lazy val vprType: vpr.DomainType = {
    domainNeeded = true
    vpr.DomainType(Names.closureDomain, Map.empty)(Vector.empty)
  }

  /** Encoding of the Closure domain. This is used to represent variables with function types. It looks as follows:
    *
    * domain Closure {
    *   function closureNil(): Closure
    *
    *   [[capturedVarsDomainFuncs]]
    *
    *   foreach spec in the package:
    *     function closureImplements$[spec](closure: Closure, [spec.params])
    *}
    *
    * Domain function captVarXClosure_T is used to retrieve the X-th captured variable of T-th type,
    * for closures obtained from function literals. For each type TypeT, the number of domain functions
    * is the maximum number of variables with type TypeT captured by a literal within the package.
    * */
  private def vprDomain: vpr.Domain =
    vpr.Domain(
      Names.closureDomain,
      Seq(nilClosureDomainFunc) ++ capturedVarsDomainFuncs ++ specs.generatedDomainFunctions,
      Seq.empty,
      Seq.empty)()

  /**
    * Assume Types is the set of captured variables in the package.
    *
    * Generates, and returns in a sequence, the following domain functions:
    * for TypeT in Types
    *   for X=1..N(T)
    *     function captVarXClosure_[TypeT](closure: Closure): TypeT
    *
    * [TypeT] is the serialized name of TypeT
    */
  private def capturedVarsDomainFuncs: Seq[vpr.DomainFunc] = {
    specs.captVarsTypeMap.flatMap {
      case (typ, num) =>
        (1 to num) map { i =>  vpr.DomainFunc(Names.closureCaptVarDomFunc(i, typ),
          Seq(vpr.LocalVarDecl(Names.closureArg, vprType)()), typ)(domainName = Names.closureDomain) }
    }.toSeq
  }

  private lazy val nilClosureDomainFunc: vpr.DomainFunc = vpr.DomainFunc(Names.closureNilFunc, Seq.empty, vprType)(domainName = Names.closureDomain)
}
