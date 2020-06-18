package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.SeqToSet
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}

class SeqToSetImpl extends SeqToSet {
  private val domainName : String = "Seq2Set"
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the "SeqToSet" domain should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "seq2set" domain function.
    */
  private lazy val domainFunc : vpr.DomainFunc = vpr.DomainFunc(
    "seq2set",
    Seq(vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()),
    vpr.SetType(typeVar)
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
    * Definition of the "seq2set_in_l" domain axiom, that relates
    * sequence inclusion with set conclusion.
    */
  private lazy val axiom_in : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val eDecl = vpr.LocalVarDecl("e", typeVar)()

    // the Viper expression: `e in xs`
    val left = vpr.SeqContains(eDecl.localVar, xsDecl.localVar)()

    // the Viper expression `e in seq2set(xs)`
    val right = vpr.AnySetContains(eDecl.localVar, domainFuncApp(xsDecl.localVar))()

    // NOTE: there doesn't seem to be an AST node for logical equivalence
    // in Silver, so I translate to an equality comparison instead.

    vpr.NamedDomainAxiom(
      name = "seq2set_in",
      exp = vpr.Forall(
        Seq(xsDecl, eDecl),
        Seq(vpr.Trigger(Seq(right))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seq2set_app" domain axiom,
    * that relates sequence concatenation with set union.
    */
  private lazy val axiom_app : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val ysDecl = vpr.LocalVarDecl("ys", vpr.SeqType(typeVar))()

    // the Viper expression: `seq2set(xs ++ ys)`
    val left = domainFuncApp(
      vpr.SeqAppend(xsDecl.localVar, ysDecl.localVar)()
    )

    // the Viper expression: `seq2set(xs) union seq2set(ys)`
    val right = vpr.AnySetUnion(
      domainFuncApp(xsDecl.localVar),
      domainFuncApp(ysDecl.localVar)
    )()

    vpr.NamedDomainAxiom(
      name = "seq2set_app",
      exp = vpr.Forall(
        Seq(xsDecl, ysDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seq2set_len" domain axiom,
    * that relates the length of a sequence to the cardinality
    * of the corresponding set conversion.
    */
  private lazy val axiom_len : vpr.DomainAxiom = {
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()

    // the Viper expression: `|seq2set(xs)|`
    val left = vpr.AnySetCardinality(
      domainFuncApp(xsDecl.localVar)
    )()

    // the Viper expression: `|xs|`
    val right = vpr.SeqLength(xsDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = "seq2set_len",
      exp = vpr.Forall(
        Seq(xsDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.LeCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * The "SeqToSet" Viper domain.
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(domainFunc),
    Seq(axiom_in, axiom_app, axiom_len),
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
