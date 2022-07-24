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
      addMemberFn(dfltFunction)
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
    *   function captVar1Closure_0(closure: Closure): Type0
    *   [...]
    *   function captVarNClosure_0(closure: Closure): Type0
    *
    *   function captVar1Closure_1(closure: Closure): Type1
    *   [...]
    *   function captVar1Closure_M(closure: Closure): TypeM
    *}
    *
    * Domain function captVarXClosure_T is used to retrieve the X-th captured variable of T-th type,
    * for closures obtained from function literals. For each type TypeT, the number of domain functions
    * is the maximum number of variables with type TypeT captured by a literal within the package.
    * */
  private def vprDomain: vpr.Domain = vpr.Domain(Names.closureDomain, capturedVarsDomainFuncs, Seq.empty, Seq.empty)()

  private def capturedVarsDomainFuncs: Seq[vpr.DomainFunc] = {
    specs.captVarsTypeMap.flatMap {
      case (typ, (tid, num)) =>
        (1 to num) map { i =>  vpr.DomainFunc(Names.closureCaptVarDomFunc(i, tid), Seq(vpr.LocalVarDecl(Names.closureArg, vprType)()), typ)(domainName = Names.closureDomain) }
    }.toSeq
  }

  private val dfltFunction: vpr.Function = vpr.Function(Names.closureNilFunc, Seq.empty, vprType, Seq.empty, Seq.empty, None)()
}
