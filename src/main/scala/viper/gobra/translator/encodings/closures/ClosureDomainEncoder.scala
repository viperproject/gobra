// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.closures

import viper.gobra.translator.Names
import viper.silver.{ast => vpr}

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


  /** Domain Closure. Domain functions captVarNClosure_tid(c) are grouped by type. For each type, there are as many
    * captured variables as the maximum number of captured variables of this type in a single closure. */
  private def vprDomain: vpr.Domain = vpr.Domain(Names.closureDomain, capturedVarsDomainFuncs, Seq.empty, Seq.empty)()

  private def capturedVarsDomainFuncs: Seq[vpr.DomainFunc] = {
    specs.captVarsTypeMap.flatMap {
      case (typ, (tid, num)) =>
        (1 to num) map { i =>  vpr.DomainFunc(Names.closureCaptVarDomFunc(i, tid), Seq(vpr.LocalVarDecl(Names.closureArg, vprType)()), typ)(domainName = Names.closureDomain) }
    }.toSeq
  }

  private val dfltFunction: vpr.Function = vpr.Function(Names.closureNilFunc, Seq.empty, vprType, Seq.empty, Seq.empty, None)()
}
