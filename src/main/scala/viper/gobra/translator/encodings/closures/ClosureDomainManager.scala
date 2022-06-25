package viper.gobra.translator.encodings.closures

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.gobra.translator.Names.serializeTypeIgnoringAddr
import viper.silver.ast.{ErrorTrafo, Info, Member, Position}
import viper.silver.{ast => vpr}

private [closures] class ClosureDomainManager {
  def apply(t: in.FunctionT, numCaptured: Int = 0): ClosureDomain = {
    val tName = serializeTypeIgnoringAddr(t)
    ClosureDomain(tName, registerType(tName, numCaptured))
  }

  def finalize(addMemberFn: Member => Unit): Unit = {
    closureTypeNamesWithMaxCapt foreach { t =>
      val d = ClosureDomain(t._1, t._2)
      addMemberFn(d.vprDomain)
      addMemberFn(d.dfltFunction)
    }
  }

  // Map from a closure type name to the maximum number of variables captured by closures of this type
  private var closureTypeNamesWithMaxCapt: Map[String, Int] = Map.empty

  // Registers that a closure with this type is in the program, with nc captured variables.
  // Returns the maximum number of captured variables seen so far for a closure of this type.
  private def registerType(tName: String, maxCapt: Int = 0): Int = {
    val oldMax = closureTypeNamesWithMaxCapt.getOrElse(tName, -1)
    if (maxCapt > oldMax) { closureTypeNamesWithMaxCapt += tName -> maxCapt; maxCapt }
    else oldMax
  }
}

private case class ClosureDomain(tName: String, maxCapt: Int = 0) {
  lazy val domName: String = s"${Names.closureDomain}$tName"
  lazy val domainFuncName: Int => String = i => s"${Names.closureCaptDomainFunc(i)}_$domName"

  lazy val vprType: vpr.DomainType = vpr.DomainType(domName, Map.empty)(Vector.empty)
  lazy val vprDomain: vpr.Domain = vpr.Domain(
    domName, (1 to maxCapt) map
      { i => vpr.DomainFunc(domainFuncName(i), Seq(vpr.LocalVarDecl(Names.closureArg, vprType)()), vpr.Ref)(domainName = domName)},
    Seq.empty, Seq.empty)()

  lazy val dfltFuncName: String = s"${Names.closureDefaultFunc}_$tName"
  lazy val dfltFunction: vpr.Function = vpr.Function(dfltFuncName, Seq.empty, vprType, Seq.empty, Seq.empty, None)()
  lazy val dfltGetter: (Position, Info, ErrorTrafo) => vpr.FuncApp =
    (pos, info, errT) => vpr.FuncApp(dfltFuncName, Seq.empty)(pos, info, vprType, errT)
}