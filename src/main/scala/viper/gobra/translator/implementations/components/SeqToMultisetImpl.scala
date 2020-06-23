package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.{SeqMultiplicity, SeqToMultiset}
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class SeqToMultisetImpl(val seqMultiplicity : SeqMultiplicity) extends SeqToMultiset {
  private val domainName : String = "Seq2Multiset"
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the "Seq2Multiset" domain should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "seq2multiset" domain function.
    */
  private lazy val domainFunc : vpr.DomainFunc = vpr.DomainFunc(
    "seq2multiset",
    Seq(vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()),
    vpr.MultisetType(typeVar)
  )(domainName = domainName)

  /**
    * Definition of a (domain) function application "seq2set(`exp`)".
    */
  private def domainFuncApp(exp : vpr.Exp) : vpr.DomainFuncApp = exp.typ match {
    case vpr.SeqType(t) => vpr.DomainFuncApp(
      func = domainFunc,
      args = Vector(exp),
      typVarMap = Map(typeVar -> t)
    )()
    case t => Violation.violation(s"expected a sequence type, but got $t")
  }

  /**
    * Definition of the "seq2multiset_in" domain axiom, that relates
    * sequence inclusion with multiset inclusion.
    */
  private lazy val axiom_in : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()

    // the Viper expression `x in seq2multiset(xs)`
    val left = vpr.AnySetContains(
      xDecl.localVar,
      domainFuncApp(xsDecl.localVar)
    )()

    // the Viper expression `seqmultiplicity(x, xs)`
    val right = seqMultiplicity.create(xDecl.localVar, xsDecl.localVar)

    vpr.NamedDomainAxiom(
      name = "seq2multiset_in",
      exp = vpr.Forall(
        Seq(xDecl, xsDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seq2multiset_app" domain axiom, that relates
    * sequence concatenation with multiset union.
    */
  private lazy val axiom_app : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val ysDecl = vpr.LocalVarDecl("ys", vpr.SeqType(typeVar))()

    // the Viper expression `seq2multiset(xs ++ ys)`
    val left = domainFuncApp(vpr.SeqAppend(
      xsDecl.localVar, ysDecl.localVar
    )())

    // the Viper expression `seq2multiset(xs) union seq2multiset(ys)`
    val right = vpr.AnySetUnion(
      domainFuncApp(xsDecl.localVar),
      domainFuncApp(ysDecl.localVar)
    )()

    vpr.NamedDomainAxiom(
      name = "seq2multiset_app",
      exp = vpr.Forall(
        Seq(xsDecl, ysDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seq2multiset_size" domain axiom, that relates
    * sequence sizes to the sizes of converted multisets.
    */
  private lazy val axiom_size : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()

    // the Viper expression `|seq2multiset(xs)|`
    val left = vpr.AnySetCardinality(domainFuncApp(xsDecl.localVar))()

    // the Viper expression `|xs|`
    val right = vpr.SeqLength(xsDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = "seq2multiset_size",
      exp = vpr.Forall(
        Seq(xsDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * The "Seq2Multiset" Viper domain.
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(domainFunc),
    Seq(axiom_in, axiom_app, axiom_size),
    Seq(typeVar)
  )()

  /**
    * Finalises translation. May add to collector.
    */
  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }

  /**
    * Creates the Viper (domain) function application that converts `exp` to a set.
    */
  override def create(exp : vpr.Exp) : vpr.DomainFuncApp = {
    generateDomain = true
    domainFuncApp(exp)
  }
}
