package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.Arrays
import viper.silver.{ast => vpr}

/**
  * Contains functionality for generating and using the "Array" Viper domain.
  */
class ArraysImpl extends Arrays {
  private val domainName : String = "Array"

  /**
    * Determines whether the "Array" domain should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * Definition of the "aslot" Viper domain function:
    *
    * {{{
    * function aslot(a : Array, i : Int) : Ref
    * }}}
    */
  private lazy val aslot_func : vpr.DomainFunc = vpr.DomainFunc(
    "aslot",
    Seq(
      vpr.LocalVarDecl("a", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq()))(),
      vpr.LocalVarDecl("i", vpr.Int)()
    ),
    vpr.Ref
  )(domainName = domainName)

  /**
    * Definition of the "alen" Viper domain function:
    *
    * {{{
    * function alen(a : Array) : Int
    * }}}
    */
  private lazy val alen_func : vpr.DomainFunc = vpr.DomainFunc(
    "alen",
    Seq(vpr.LocalVarDecl("a", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq()))()),
    vpr.Int
  )(domainName = domainName)

  /**
    * Definition of the "alen_nonneg" axiom of the "Array"
    * Viper domain, which captures that the length of any
    * array is (invariably) non-negative:
    *
    * {{{
    * axiom alen_nonneg {
    *   forall a : Array :: { alen(a) } 0 <= alen(a)
    * }
    * }}}
    */
  private lazy val alen_nonneg_axiom : vpr.DomainAxiom = {
    val aDecl = vpr.LocalVarDecl("a", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq()))()
    val app = length(aDecl.localVar)()

    vpr.NamedDomainAxiom(
      name = "alen_nonneg",
      exp = vpr.Forall(
        Seq(aDecl),
        Seq(vpr.Trigger(Seq(app))()),
        vpr.LeCmp(vpr.IntLit(0)(), app)()
      )()
    )(domainName = domainName)
  }

  /**
    * The "Array" Viper domain:
    *
    * {{{
    * domain Array {
    *   function aslot(a : Array, i : Int) : Ref
    *   function alen(a : Array) : Int
    *
    *   axiom alen_nonneg {
    *     forall a : Array :: { alen(a) } 0 <= alen(a)
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(aslot_func, alen_func),
    Seq(alen_nonneg_axiom),
    Seq()
  )()

  /**
    * Yields a function application of the "alen" domain function,
    * with argument `exp` (which should be of an array type).
    */
  override def length(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) = vpr.DomainFuncApp(
    func = alen_func,
    args = Vector(exp),
    typVarMap = Map()
  )(pos, info, errT)

  /**
    * Yields a function application of the "aslot" domain function,
    * with arguments `base` and `index` for the array and the index,
    * respectively. Here `base` should be of an array type and
    * `index` of an integer type.
    */
  override def slot(base : vpr.Exp, index : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) = vpr.DomainFuncApp(
    func = aslot_func,
    args = Vector(base, index),
    typVarMap = Map()
  )(pos, info, errT)

  /**
    * Finalizes translation.
    * May add the "Array" Viper domain to the collector.
    */
  override def finalize(col : Collector) : Unit = {
    if (generateDomain) col.addMember(domain)
  }

  /**
    * Yields the Viper domain type of arrays.
    */
  def typ() : vpr.DomainType = {
    generateDomain = true
    vpr.DomainType(domain, Map())
  }
}
