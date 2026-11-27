// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast._
import viper.silver.plugin.sif.{SIFExtendedTransformer, SIFLowExp}

import scala.collection.mutable

/**
  * The product construction of Viper's information-flow plugin (the "extended" encoding of Eilers, Müller & Hitz,
  * which supports branches on secrets), as a [[ProductConstruction]] for the encoding of hyperfunctions
  * ([[HyperfuncEncoding]]). It extends silver's implementation ([[SIFExtendedTransformer]]) and overrides it only where
  * the semantics of hyperfunctions differs:
  *
  *  - the activation of a method's executions is the SIF plugin's activation condition (`p1`, `p2`), including
  *    executions that returned or left a loop early;
  *  - relational atoms are evaluated, like unary ones, in each active execution (Viper's plugin evaluated them only
  *    where both executions are active), so `!low(x)` is false where only one execution is active;
  *  - execution 0 reads the original variables, fields and predicates, execution 1 their copies (`translatePrime`),
  *    so hyperfunctions that read the heap may be recursive.
  */
class ExtendedProduct extends SIFExtendedTransformer with ProductConstruction {

  /** The encoding of hyperfunctions, set by [[transformProgram]]. */
  private var encoding: HyperfuncEncoding = null
  private var hyperFunctions: Map[String, Function] = Map.empty

  private def isOwnRelational(e: Node): Boolean = encoding.isOwnRelational(e)

  /** Translates the program `p` (prepared by [[HyperfuncsPreprocessing]]) into a product program. */
  def transformProgram(p: Program, execParameters: Map[String, Set[Int]]): Program = {
    hyperFunctions = p.functions.filter(f => HyperfuncsAnnotations.isHyperfunction(f.info)).map(f => f.name -> f).toMap
    encoding = new HyperfuncEncoding(this, hyperFunctions.keySet, execParameters, new Multiplicity(p, hyperFunctions.keySet))
    transform(p, enableTiming = false)
  }

  //////////////////////////////// ProductConstruction ////////////////////////////////

  override def executions: Int = 2

  override def inExecution(e: Exp, i: Int, act: Seq[Exp]): Exp =
    if (i == 0) translateNormal(e, act(0), act(1)) else translatePrime(e, act(0), act(1))

  override def lowComparison(l: SIFLowExp, i: Int, j: Int, act: Seq[Exp]): Exp =
    translateSIFLowExpComparison(l, act(0), act(1))

  override def withParameterCopies[T](params: Seq[LocalVarDecl])(body: Seq[Seq[LocalVarDecl]] => T): T = {
    val primedBefore = primedNames.clone()
    val primed = params.map { a =>
      val newName = NameTransformer.getUnusedName(a.name)
      primedNames.update(a.name, newName)
      a.copy(name = newName)(a.pos, a.info, a.errT)
    }
    try body(Seq(params, primed))
    finally {
      primedNames.clear()
      primedNames ++= primedBefore
    }
  }

  override def freshName(base: String): String = NameTransformer.getUnusedName(base)

  // `reportError` is the SIF plugin's

  //////////////////////////////// Integration with the SIF plugin ////////////////////////////////

  /**
    * The SIF plugin's translation, with hyperfunctions replaced by abstract stubs, which are translated afterwards (the
    * SIF plugin rejects relational assertions in functions, and the translation of a hyperfunction needs the names of
    * the copies of fields and predicates for execution 1, which the SIF plugin creates).
    */
  override def transform(p: Program, enableTiming: Boolean): Program = {
    val stubs = p.functions.map(f =>
      if (hyperFunctions.contains(f.name)) f.copy(pres = Seq.empty, posts = Seq.empty, body = None)(f.pos, f.info, f.errT) else f)
    val withNames = p.copy(functions = stubs)(p.pos, p.info, p.errT)
    val product = super.transform(withNames, enableTiming)
    NameTransformer.markUsed(p.collect { case d: Declaration => d.name })
    val functions = product.functions.map(f =>
      hyperFunctions.get(f.name).map(encoding.translateDefinition).getOrElse(f))
    val res = product.copy(functions = functions ++ encoding.auxiliaryFunctions)(product.pos, product.info, product.errT)
    Config.transformedProgramCallback.foreach(fn => fn(res))
    res
  }

  override def translateMethod(m: Method): Method =
    if (HyperfuncsAnnotations.isHyperfunction(m.info)) encoding.translateTerminationProof(m, translateToUnary)
    else super.translateMethod(m)

  override def isDirectlyUnary(e: Exp): Boolean = super.isDirectlyUnary(e) && !e.exists(isOwnRelational)

  // A relational atom is evaluated in each active execution, like a unary one: `rel(e, i)` is defined only if
  // execution i is active, and `low` relates the active executions.
  override def translateSIFExp1(e: Exp, p1: Exp, p2: Exp): Exp = translateNormal(e, p1, p2)
  override def translateSIFExp2(e: Exp, p1: Exp, p2: Exp): Exp = translatePrime(e, p1, p2)

  private var holeCounter = 0

  /**
    * Translates the hyperfunction constructs in `e` first ([[HyperfuncEncoding.translateOwn]]) and the rest with the
    * SIF plugin's `base` translation. The already translated parts are hidden from `base` as leaves ([[HFHole]]), since
    * `base` would otherwise translate them again (e.g., rename the fields of execution 1 a second time).
    */
  private def withOwn[T <: Exp](e: T, p1: Exp, p2: Exp)(base: T => T): T = {
    val table = mutable.HashMap.empty[Int, Exp]
    val holed = e.transform {
      case x: Exp if isOwnRelational(x) =>
        val translated = encoding.translateOwn(x, Seq(p1, p2))
        holeCounter += 1
        table.update(holeCounter, translated)
        HFHole(holeCounter, x.typ)(x.pos)
    }
    val res = base(holed)
    if (table.isEmpty) res else res.transform { case h: HFHole if table.contains(h.id) => table(h.id) }
  }

  override def translateNormal[T <: Exp](e: T, p1: Exp, p2: Exp): T = withOwn(e, p1, p2)(super.translateNormal(_, p1, p2))

  override def translatePrime[T <: Exp](e: T, p1: Exp, p2: Exp): T = withOwn(e, p1, p2)(super.translatePrime(_, p1, p2))

  override def translateToUnary(e: Exp): Exp = super.translateToUnary(e.transform {
    case a: Exp if a.typ == Bool && !isConnective(a) && a.exists(isOwnRelational) => TrueLit()(a.pos, a.info, a.errT)
  })

  override def translatePredLowFuncBody(e: Exp): Exp = e match {
    // a predicate's low function is used only where both executions are active
    case a if a.typ == Bool && !isConnective(a) && a.exists(isOwnRelational) =>
      val t = TrueLit()(a.pos)
      And(translateNormal(a, t, t), translatePrime(a, t, t))(a.pos, a.info, a.errT)
    case _ => super.translatePredLowFuncBody(e)
  }

  private def isConnective(e: Exp): Boolean = e match {
    case _: And | _: Or | _: Implies | _: Not => true
    case _ => false
  }
}
