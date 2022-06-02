// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.components

import viper.gobra.translator.interfaces.components.{Arrays, Slices}
import viper.gobra.translator.util.ViperParser
import viper.silver.{ast => vpr}

class SlicesImpl(val arrays : Arrays) extends Slices {

  /**
    * Determines whether the "Slice" Viper domain
    * should be generated upon finalisation.
    */
  private var generateDomain : Boolean = false


  private val domainN: String = "Slice"
  private val typeVar : vpr.TypeVar = vpr.TypeVar("T")

  private val makeN: String = "smake"
  private val arrayN: String = "sarray"
  private val offN: String = "soffset"
  private val lenN: String = "slen"
  private val locN: String = "sloc"
  private val capN: String = "scap"
  private val addN: String = "sadd"
  private val subN: String = "ssub"


  def funcDecl(x: vpr.DomainFuncApp): (String, String) = {
    val args = {x.args.zipWithIndex.map{ case (arg, idx) => s"x$idx: ${arg.typ}"}}
    (
      x.funcname,
      s"function ${x.funcname}(${args.mkString(",")}): ${x.typ}"
    )
  }

  private lazy val domain = {

    val arrayVar = vpr.LocalVar("a", arrays.typ(typeVar))()
    val (alenN, arrayLenFuncDecl) = funcDecl(arrays.len(arrayVar)())
    val (alocN, arrayLocFuncDecl) = funcDecl(arrays.loc(arrayVar, vpr.LocalVar("i", vpr.Int)())())

    ViperParser.parseDomains(
      s"""
         |
         | domain $domainN[$typeVar] {
         |   function $arrayN(s: $domainN[$typeVar]): ${arrays.typ(typeVar)}
         |
         |   function $makeN(arr: ${arrays.typ(typeVar)}, offset: Int, len: Int, cap: Int): $domainN[$typeVar]
         |
         |   function $offN(s: $domainN[$typeVar]): Int
         |   function $lenN(s: $domainN[$typeVar]): Int
         |   function $capN(s: $domainN[$typeVar]): Int
         |
         |   function $locN(s: $domainN[$typeVar], i: Int): $typeVar
         |   function $addN(l: Int, r: Int): Int
         |   function $subN(l: Int, r: Int): Int
         |
         |   axiom {
         |     forall s: $domainN[$typeVar], i: Int :: { $locN(s,i) }
         |       $subN($addN(i, $offN(s)), $offN(s)) == i && $locN(s,i) == $alocN($arrayN(s), $addN(i, $offN(s)))
         |   }
         |
         |   axiom {
         |     forall a: ${arrays.typ(typeVar)}, off: Int, len: Int, cap: Int, j: Int :: { $alocN(a,j), $makeN(a,off,len,cap) }
         |       $addN($subN(j, off), off) == j && $alocN(a,j) == $locN($makeN(a,off,len,cap), $subN(j, off))
         |   }
         |
         |   axiom {
         |     forall s: $domainN[$typeVar] :: { $offN(s) } 0 <= $offN(s)
         |   }
         |
         |   axiom {
         |     forall s: $domainN[$typeVar] :: { $lenN(s) }{ $capN(s) } 0 <= $lenN(s) && $lenN(s) <= $capN(s)
         |   }
         |
         |   axiom {
         |     forall s: $domainN[$typeVar] :: { $offN(s), $capN(s) } { $alenN($arrayN(s)) }
         |       $offN(s) + $capN(s) <= $alenN($arrayN(s))
         |   }
         |
         |   axiom {
         |     forall arr: ${arrays.typ(typeVar)}, off: Int, len: Int, cap: Int :: { $makeN(arr,off,len,cap) }
         |       (0 <= off && 0 <= len && len <= cap && off + cap <= $alenN(arr)) ==>
         |           ($arrayN($makeN(arr,off,len,cap)) == arr
         |         && $offN($makeN(arr,off,len,cap)) == off
         |         && $lenN($makeN(arr,off,len,cap)) == len
         |         && $capN($makeN(arr,off,len,cap)) == cap)
         |   }
         |
         |   axiom {
         |     forall s: $domainN[$typeVar] :: { $arrayN(s) }{ $offN(s) }{ $lenN(s) }{ $capN(s) }
         |       s == $makeN($arrayN(s), $offN(s), $lenN(s), $capN(s))
         |   }
         |
         |   axiom {
         |     forall l: Int, r: Int :: { $addN(l,r) } $addN(l,r) == l + r
         |   }
         |
         |   axiom {
         |     forall l: Int, r: Int :: { $subN(l,r) } $subN(l,r) == l - r
         |   }
         | }
         |
         | domain ${arrays.typ(typeVar)} {
         |   $arrayLenFuncDecl
         |   $arrayLocFuncDecl
         | }
         |
         |
         |
         |""".stripMargin
    ).head
  }

  /** A function application of "sarray". */
  override def array(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    val sliceTypVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    vpr.DomainFuncApp(
      funcname = arrayN,
      args = Vector(exp),
      typVarMap = sliceTypVarMap
    )(pos, info, arrays.typ(sliceTypVarMap(typeVar)), domainN, errT)
  }

  /** A function application of "scap". */
  override def cap(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      funcname = capN,
      args = Vector(exp),
      typVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, vpr.Int, domainN, errT)
  }

  /** A function application of "slen". */
  override def len(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      funcname = lenN,
      args = Vector(exp),
      typVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, vpr.Int, domainN, errT)
  }

  /** A function application of "soffset". */
  override def offset(exp : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.DomainFuncApp = {
    generateDomain = true
    vpr.DomainFuncApp(
      funcname = offN,
      args = Vector(exp),
      typVarMap = exp.typ.asInstanceOf[vpr.DomainType].typVarsMap
    )(pos, info, vpr.Int, domainN, errT)
  }

  /**
    * A function application of the "sloc" function.
    */
  override def loc(base : vpr.Exp, index : vpr.Exp)(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Exp = {
    generateDomain = true
    val sliceTypVarMap = base.typ.asInstanceOf[vpr.DomainType].typVarsMap
    vpr.DomainFuncApp(
      funcname = locN,
      args = Vector(base, index),
      typVarMap = sliceTypVarMap
    )(pos, info, sliceTypVarMap(typeVar), domainN, errT)
  }

  /** A function application of "smake". */
  def make(arr: vpr.Exp, off: vpr.Exp, len: vpr.Exp, cap: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos) : vpr.DomainFuncApp = {
    generateDomain = true
    val arrayTypVarMap = arr.typ.asInstanceOf[vpr.DomainType].typVarsMap
    vpr.DomainFuncApp(
      funcname = makeN,
      args = Vector(arr, off, len, cap),
      typVarMap = arrayTypVarMap
    )(pos, info, typ(arrayTypVarMap(typeVar)), domainN, errT)
  }

  /** Yields the Viper domain type of slices. */
  def typ(t : vpr.Type) : vpr.DomainType = {
    generateDomain = true
    vpr.DomainType(domain, Map(typeVar -> t))
  }

  override def finalize(addMemberFn: vpr.Member => Unit) : Unit = {
    if (generateDomain) {
      addMemberFn(domain)
    }
  }
}
