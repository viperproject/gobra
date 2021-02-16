// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.Collector
import viper.gobra.translator.interfaces.components.{Arrays, Slices}
import viper.silver.{ast => vpr}

class SlicesImpl(val arrays : Arrays) extends Slices {
  private val domainName : String = "Slice"
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  private lazy val sadd_func = sadd_func_def()

  /**
    * Determines whether the "Slice" Viper domain
    * should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false

  /**
    * {{{
    * function sarray(s : Slice[T]) : ShArray[T]
    * }}}
    */
  private lazy val sarray_func : vpr.DomainFunc = vpr.DomainFunc(
    "sarray",
    Seq(vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    arrays.typ(typeVar)
  )(domainName = domainName)

  /**
    * {{{
    * function soffset(s : Slice[T]) : Int
    * }}}
    */
  private lazy val soffset_func : vpr.DomainFunc = vpr.DomainFunc(
    "soffset",
    Seq(vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    vpr.Int
  )(domainName = domainName)

  /**
    * {{{
    * function slen(s : Slice[T]) : Int
    * }}}
    */
  private lazy val slen_func : vpr.DomainFunc = vpr.DomainFunc(
    "slen",
    Seq(vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    vpr.Int
  )(domainName = domainName)

  /**
    * {{{
    * function slen(s : Slice[T]) : Int
    * }}}
    */
  private lazy val scap_func : vpr.DomainFunc = vpr.DomainFunc(
    "scap",
    Seq(vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()),
    vpr.Int
  )(domainName = domainName)

  /**
    * {{{
    * axiom slice_offset_nonneg {
    *   forall s : Slice[T] :: { soffset(s) } 0 <= soffset(s)
    * }
    * }}}
    */
  private lazy val slice_offset_nonneg_axiom : vpr.DomainAxiom = {
    val sDecl = vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val exp = offset(sDecl.localVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(sDecl),
        Seq(vpr.Trigger(Seq(exp))()),
        vpr.LeCmp(vpr.IntLit(0)(), exp)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom slice_len_nonneg {
    *   forall s : Slice[T] :: { slen(s) } 0 <= slen(s)
    * }
    * }}}
    */
  private lazy val slice_len_nonneg_axiom : vpr.DomainAxiom = {
    val sDecl = vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val exp = len(sDecl.localVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(sDecl),
        Seq(vpr.Trigger(Seq(exp))()),
        vpr.LeCmp(vpr.IntLit(0)(), exp)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom slice_len_leq_cap {
    *   forall s : Slice[T] :: { slen(s) } { scap(s) } slen(s) <= scap(s)
    * }
    * }}}
    */
  private lazy val slice_len_leq_cap_axiom : vpr.DomainAxiom = {
    val sDecl = vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val left = len(sDecl.localVar)()
    val right = cap(sDecl.localVar)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(sDecl),
        Seq(vpr.Trigger(Seq(left))(), vpr.Trigger(Seq(right))()),
        vpr.LeCmp(left, right)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom slice_cap_leq_alen {
    *   forall s : Slice[T] :: { soffset(s), scap(s) } { alen(sarray(s)) }
    *     soffset(s) + scap(s) <= alen(sarray(s))
    * }
    * }}}
    */
  private lazy val slice_cap_leq_alen_axiom : vpr.DomainAxiom = {
    val sDecl = vpr.LocalVarDecl("s", vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar)))()
    val soffset = offset(sDecl.localVar)()
    val scap = cap(sDecl.localVar)()
    val alen = arrays.len(array(sDecl.localVar, typeVar)())()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(sDecl),
        Seq(vpr.Trigger(Seq(soffset, scap))(), vpr.Trigger(Seq(alen))()),
        vpr.LeCmp(vpr.Add(soffset, scap)(), alen)()
      )()
    )(domainName = domainName)
  }

  /**
    * Definition of the following auxiliary Viper function,
    * used to improve verification with the `loc` function:
    *
    * {{{
    * function sadd(left: Int, right: Int): Int
    *   ensures result == left + right
    * {
    *   left + right
    * }
    * }}}
    */
  private def sadd_func_def() : vpr.Function = {
    val lDecl = vpr.LocalVarDecl("left", vpr.Int)()
    val rDecl = vpr.LocalVarDecl("right", vpr.Int)()
    val body : vpr.Exp = vpr.Add(lDecl.localVar, rDecl.localVar)()
    val post : vpr.Exp = vpr.EqCmp(vpr.Result(vpr.Int)(), body)()
    vpr.Function("sadd", Seq(lDecl, rDecl), vpr.Int, Seq(), Seq(post), Some(body))()
  }

  /**
    * {{{
    * domain Slice[T] {
    *   function sarray(s : Slice[T]) : ShArray[T]
    *   function soffset(s : Slice[T]) : Int
    *   function slen(s : Slice[T]) : Int
    *   function scap(s : Slice[T]) : Int
    *
    *   axiom slice_offset_nonneg {
    *     forall s : Slice[T] :: { soffset(s) } 0 <= soffset(s)
    *   }
    *
    *   axiom slice_len_nonneg {
    *     forall s : Slice[T] :: { slen(s) } 0 <= slen(s)
    *   }
    *
    *   axiom slice_len_leq_cap {
    *     forall s : Slice[T] :: { slen(s) } { scap(s) } slen(s) <= scap(s)
    *   }
    *
    *   axiom slice_cap_leq_alen {
    *     forall s : Slice[T] :: { soffset(s), scap(s) } { alen(sarray(s)) }
    *       soffset(s) + scap(s) <= alen(sarray(s))
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(sarray_func, soffset_func, slen_func, scap_func),
    Seq(slice_offset_nonneg_axiom, slice_len_nonneg_axiom,
      slice_len_leq_cap_axiom, slice_cap_leq_alen_axiom),
    Seq(typeVar)
  )()

  /** A function application of "sadd". */
  private def add(left : vpr.Exp, right : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.FuncApp = {
    generateDomain = true
    vpr.FuncApp(sadd_func.name, Seq(left, right))(pos, info, vpr.Int, errT)
  }

  /** A function application of "sarray". */
  override def array(exp : vpr.Exp, t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = sarray_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> t)
    )(pos, info, errT)
  }

  /** A function application of "scap". */
  override def cap(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = scap_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> typeVar)
    )(pos, info, errT)
  }

  /** A function application of "slen". */
  override def len(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = slen_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> typeVar)
    )(pos, info, errT)
  }

  /** A function application of "soffset". */
  override def offset(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = soffset_func,
      args = Vector(exp),
      typVarMap = Map(typeVar -> typeVar)
    )(pos, info, errT)
  }

  /**
    * A function application of the "sloc" function.
    */
  override def loc(base : vpr.Exp, index : vpr.Exp, t : vpr.Type)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Exp = {
    arrays.loc(
      array(base, t)(pos, info, errT),
      add(offset(base)(pos, info, errT), index)(pos, info, errT)
    )(pos, info, errT)
  }

  /** Yields the Viper domain type of slices. */
  def typ(t : vpr.Type) : vpr.DomainType = {
    generateDomain = true
    vpr.DomainType(domain, Map(typeVar -> t))
  }

  override def finalize(col : Collector) : Unit = {
    if (generateDomain) {
      col.addMember(domain)
      col.addMember(sadd_func)
    }
  }
}
