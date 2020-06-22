package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.SeqMultiplicity
import viper.silver.{ast => vpr}

class SeqMultiplicityImpl extends SeqMultiplicity {
  private val domainName : String = "SeqMultiplicity"
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  /**
    * Determines whether the "SeqMultiplicity" domain
    * should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "seqmultiplicity" domain function.
    */
  private lazy val domainFunc : vpr.DomainFunc = vpr.DomainFunc(
    "seqmultiplicity",
    Seq(
      vpr.LocalVarDecl("x", typeVar)(),
      vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    ),
    vpr.Int
  )(domainName = domainName)

  /**
    * Definition of a (domain) function application "seqmultiplicity(`left`, `right`)".
    */
  private def domainFuncApp(left : vpr.Exp, right : vpr.Exp) : vpr.DomainFuncApp = {
    vpr.DomainFuncApp(
      func = domainFunc,
      args = Vector(left, right),
      typVarMap = Map(typeVar -> left.typ)
    )()
  }

  /**
    * Definition of the "seqmultiplicity_bounds" domain axiom,
    * that gives bounds to the sequence multiplicity operator
    */
  private lazy val axiom_bounds : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val funcApp = domainFuncApp(xDecl.localVar, xsDecl.localVar)

    vpr.NamedDomainAxiom(
      name = "seqmultiplicity_bounds",
      exp = vpr.Forall(
        Seq(xDecl, xsDecl),
        Seq(vpr.Trigger(Seq(funcApp))()),
        vpr.And(
          vpr.LeCmp(vpr.IntLit(0)(), funcApp)(),
          vpr.LeCmp(funcApp, vpr.SeqLength(xsDecl.localVar)())()
        )()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seqmultiplicity_singleton" domain axiom,
    * that gives the multiplicity of a singleton sequence
    */
  private lazy val axiom_singleton : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val yDecl = vpr.LocalVarDecl("y", typeVar)()

    val funcApp = domainFuncApp(
      xDecl.localVar,
      vpr.ExplicitSeq(Seq(yDecl.localVar))()
    )

    val right = vpr.CondExp(
      vpr.EqCmp(xDecl.localVar, yDecl.localVar)(),
      vpr.IntLit(1)(),
      vpr.IntLit(0)()
    )()

    vpr.NamedDomainAxiom(
      name = "seqmultiplicity_singleton",
      exp = vpr.Forall(
        Seq(xDecl, yDecl),
        Seq(vpr.Trigger(Seq(funcApp))()),
        vpr.EqCmp(funcApp, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seqmultiplicity_app" domain axiom,
    * that relates sequence multiplicity to sequence concatenation.
    */
  private lazy val axiom_app : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()
    val ysDecl = vpr.LocalVarDecl("ys", vpr.SeqType(typeVar))()

    // the Viper expression `seqmultiplicity(x, xs ++ ys)`.
    val left = domainFuncApp(
      xDecl.localVar,
      vpr.SeqAppend(xsDecl.localVar, ysDecl.localVar)()
    )

    // the Viper expression `seqmultiplicity(x, xs) + seqmultiplicity(x, ys)`.
    val right = vpr.Add(
      domainFuncApp(xDecl.localVar, xsDecl.localVar),
      domainFuncApp(xDecl.localVar, ysDecl.localVar)
    )()

    vpr.NamedDomainAxiom(
      name = "seqmultiplicity_app",
      exp = vpr.Forall(
        Seq(xDecl, xsDecl, ysDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the "seqmultiplicity_in" domain axiom,
    * that relates sequence multiplicity to sequence inclusion.
    */
  private lazy val axiom_in : vpr.DomainAxiom = {
    val xDecl = vpr.LocalVarDecl("x", typeVar)()
    val xsDecl = vpr.LocalVarDecl("xs", vpr.SeqType(typeVar))()

    // the Viper expression: `x in xs`
    val left = vpr.SeqContains(xDecl.localVar, xsDecl.localVar)()

    // the Viper expression: `seqmultiplicity(x, xs)`
    val right = vpr.LtCmp(
      vpr.IntLit(0)(),
      domainFuncApp(xDecl.localVar, xsDecl.localVar)
    )()

    // Is there no logical equivalence operator in Silver's AST?
    // Instead I, for now, translate the quantifier body to a `vpr.EqCmp`.

    vpr.NamedDomainAxiom(
      name = "seqmultiplicity_in",
      exp = vpr.Forall(
        Seq(xDecl, xsDecl),
        Seq(vpr.Trigger(Seq(left))()),
        vpr.EqCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * The "SeqMultiplicity" Viper domain.
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(domainFunc),
    Seq(axiom_bounds, axiom_singleton, axiom_app, axiom_in),
    Seq(typeVar)
  )()

  /**
    * Finalises translation. May add to collector.
    */
  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }

  /**
    * Creates the Viper (domain) function application for the
    * sequence multiplicity operation "`left` # `right`".
    * Here `right` is expected to be of a sequence type.
    */
  override def create(left : vpr.Exp, right : vpr.Exp) : vpr.DomainFuncApp = {
    generateDomain = true
    domainFuncApp(left, right)
  }
}
