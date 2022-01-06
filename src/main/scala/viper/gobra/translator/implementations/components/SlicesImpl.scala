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
  private val domainType: vpr.DomainType = vpr.DomainType(domainName, Map[vpr.TypeVar, vpr.Type]())(Seq(typeVar))

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
    Seq(vpr.LocalVarDecl("s", domainType)()),
    arrays.typ(typeVar)
  )(domainName = domainName)

  /**
    * {{{
    * function soffset(s : Slice[T]) : Int
    * }}}
    */
  private lazy val soffset_func : vpr.DomainFunc = vpr.DomainFunc(
    "soffset",
    Seq(vpr.LocalVarDecl("s", domainType)()),
    vpr.Int
  )(domainName = domainName)

  /**
    * {{{
    * function slen(s : Slice[T]) : Int
    * }}}
    */
  private lazy val slen_func : vpr.DomainFunc = vpr.DomainFunc(
    "slen",
    Seq(vpr.LocalVarDecl("s", domainType)()),
    vpr.Int
  )(domainName = domainName)

  /**
    * {{{
    * function scap(s : Slice[T]) : Int
    * }}}
    */
  private lazy val scap_func : vpr.DomainFunc = vpr.DomainFunc(
    "scap",
    Seq(vpr.LocalVarDecl("s", domainType)()),
    vpr.Int
  )(domainName = domainName)

  /**
    * {{{
    * function smake(arr: Array[T], offset: Int, len: Int, cap: Int): Slice[T]
    * }}}
    */
  private lazy val smake_func : vpr.DomainFunc = vpr.DomainFunc(
    "smake",
    Seq(vpr.LocalVarDecl("a", arrays.typ(typeVar))(), vpr.LocalVarDecl("o", vpr.Int)(), vpr.LocalVarDecl("l", vpr.Int)(), vpr.LocalVarDecl("c", vpr.Int)()),
    domainType
  )(domainName = domainName)

  /**
    * {{{
    * axiom slice_offset_nonneg {
    *   forall s : Slice[T] :: { soffset(s) } 0 <= soffset(s)
    * }
    * }}}
    */
  private lazy val slice_offset_nonneg_axiom : vpr.DomainAxiom = {
    val sDecl = vpr.LocalVarDecl("s", domainType)()
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
    val sDecl = vpr.LocalVarDecl("s", domainType)()
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
    val sDecl = vpr.LocalVarDecl("s", domainType)()
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
    val sDecl = vpr.LocalVarDecl("s", domainType)()
    val soffset = offset(sDecl.localVar)()
    val scap = cap(sDecl.localVar)()
    val alen = arrays.len(array(sDecl.localVar)())()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(sDecl),
        Seq(vpr.Trigger(Seq(soffset, scap))(), vpr.Trigger(Seq(alen))()),
        vpr.LeCmp(vpr.Add(soffset, scap)(), alen)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom slice_deconstructor_over_constructor {
    *   forall arr, off, len, cap :: { smake(arr,off,len,cap) }
    *     0 <= off && 0 <= len && len <= cap && off + cap <= alen(arr) ==>
    *       sarray(smake(arr,off,len,cap)) == arr && ...
    * }
    * }}}
    */
  private lazy val slice_deconstructor_over_constructor: vpr.DomainAxiom = {
    val arrDecl = vpr.LocalVarDecl("a", arrays.typ(typeVar))(); val arr = arrDecl.localVar
    val offDecl = vpr.LocalVarDecl("o", vpr.Int)(); val off = offDecl.localVar
    val lehDecl = vpr.LocalVarDecl("l", vpr.Int)(); val leh = lehDecl.localVar
    val cayDecl = vpr.LocalVarDecl("c", vpr.Int)(); val cay = cayDecl.localVar

    val smake = make(arr,off,leh,cay)()
    val lhs = vpr.And(
      vpr.LeCmp(vpr.IntLit(0)(), off)(), vpr.And(
        vpr.LeCmp(vpr.IntLit(0)(), leh)(), vpr.And(
          vpr.LeCmp(leh, cay)(),
          vpr.LeCmp(vpr.Add(off,cay)(), arrays.len(arr)())())())())()
    val rhs = vpr.And(
      vpr.EqCmp(array(smake)(), arr)(), vpr.And(
        vpr.EqCmp(offset(smake)(), off)(), vpr.And(
          vpr.EqCmp(len(smake)(), leh)(),
          vpr.EqCmp(cap(smake)(), cay)())())())()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(arrDecl, offDecl, lehDecl, cayDecl),
        Seq(vpr.Trigger(Seq(smake))()),
        vpr.Implies(lhs, rhs)()
      )()
    )(domainName = domainName)
  }

  /**
    * {{{
    * axiom slice_constructor_over_deconstructor {
    *   forall s :: { sarray(s) }{ soffset(s) }{ slen(s) }{ scap(s) }
    *     s == smake(sarray(s), soffset(s), slen(s), scap(s))
    * }
    * }}}
    */
  private lazy val slice_constructor_over_deconstructor : vpr.DomainAxiom = {
    val sDecl = vpr.LocalVarDecl("s", domainType)(); val s = sDecl.localVar

    val sarray = array(s)()
    val soff = offset(s)()
    val slen = len(s)()
    val scap = cap(s)()

    vpr.AnonymousDomainAxiom(
      vpr.Forall(
        Seq(sDecl),
        Seq(vpr.Trigger(Seq(sarray))(), vpr.Trigger(Seq(soff))(), vpr.Trigger(Seq(slen))(), vpr.Trigger(Seq(scap))()),
        vpr.EqCmp(s, make(sarray, soff, slen, scap)())()
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
    *   function smake(arr: Array[T], offset: Int, len: Int, cap: Int): Slice[T]
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
    *
    *   axiom slice_deconstructor_over_constructor {
    *     forall arr, off, len, cap :: { smake(arr,off,len,cap) }
    *       0 <= off && 0 <= len && len <= cap && off + cap <= alen(arr) ==>
    *         sarray(smake(arr,off,len,cap)) == arr && ...
    *   }
    *
    *   axiom slice_constructor_over_deconstructor {
    *     forall s :: { sarray(s) }{ soffset(s) }{ slen(s) }{ scap(s) }
    *       s == smake(sarray(s), soffset(s), slen(s), scap(s))
    *   }
    * }
    * }}}
    */
  private lazy val domain : vpr.Domain = vpr.Domain(
    domainName,
    Seq(sarray_func, soffset_func, slen_func, scap_func, smake_func),
    Seq(slice_offset_nonneg_axiom, slice_len_nonneg_axiom,
      slice_len_leq_cap_axiom, slice_cap_leq_alen_axiom,
      slice_deconstructor_over_constructor, slice_constructor_over_deconstructor),
    Seq(typeVar)
  )()

  /** A function application of "sadd". */
  private def add(left : vpr.Exp, right : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.FuncApp = {
    generateDomain = true
    vpr.FuncApp(sadd_func.name, Seq(left, right))(pos, info, vpr.Int, errT)
  }

  /** A function application of "sarray". */
  override def array(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = sarray_func,
      args = Vector(exp),
      typVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, errT)
  }

  /** A function application of "scap". */
  override def cap(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = scap_func,
      args = Vector(exp),
      typVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, errT)
  }

  /** A function application of "slen". */
  override def len(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = slen_func,
      args = Vector(exp),
      typVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, errT)
  }

  /** A function application of "soffset". */
  override def offset(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = soffset_func,
      args = Vector(exp),
      typVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, errT)
  }

  /**
    * A function application of the "sloc" function.
    */
  override def loc(base : vpr.Exp, index : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Exp = {
    arrays.loc(
      array(base)(pos, info, errT),
      add(offset(base)(pos, info, errT), index)(pos, info, errT)
    )(pos, info, errT)
  }

  /** A function application of "smake". */
  def make(arr: vpr.Exp, off: vpr.Exp, len: vpr.Exp, cap: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      func = smake_func,
      args = Vector(arr, off, len, cap),
      typVarMap = arr.typ.asInstanceOf[vpr.DomainType].typVarsMap
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
