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

  /** Domain Closure, with as many domain functions as the maximum number of captured variables.
    * Domain function captVarNClosure(c) returns a Ref, since a closure keeps pointers to the captured variables.  */
  private def vprDomain: vpr.Domain = vpr.Domain(
    Names.closureDomain,
    ((1 to specs.maxCaptVariables) map { i => vpr.DomainFunc(Names.closureCaptVarDomFunc(i), Seq(vpr.LocalVarDecl(Names.closureArg, vprType)()), vpr.Ref)(domainName = Names.closureDomain)}) ++
    Seq.empty, Seq.empty)()

  private val dfltFunction: vpr.Function = vpr.Function(Names.closureDefaultFunc, Seq.empty, vprType, Seq.empty, Seq.empty, None)()
}
