package viper.gobra.translator.encodings.closures

import viper.gobra.translator.Names
import viper.silver.ast.Member
import viper.silver.{ast => vpr}

class ClosureDomainManager(specs: ClosureSpecsManager) {

  def finalize(addMemberFn: Member => Unit): Unit = {
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

  private def vprDomain: vpr.Domain = vpr.Domain(
    Names.closureDomain,
    ((1 to specs.maxCaptVariables) map { i => vpr.DomainFunc(Names.closureCaptVarDomFunc(i), Seq(vpr.LocalVarDecl(Names.closureArg, vprType)()), vpr.Ref)(domainName = Names.closureDomain)}) ++
    ((1 to specs.maxCaptClosures) map { i => vpr.DomainFunc(Names.closureCaptClDomFunc(i), Seq(vpr.LocalVarDecl(Names.closureArg, vprType)()), vprType)(domainName = Names.closureDomain)}),
    Seq.empty, Seq.empty)()

  private val dfltFunction: vpr.Function = vpr.Function(Names.closureDefaultFunc, Seq.empty, vprType, Seq.empty, Seq.empty, None)()
}
