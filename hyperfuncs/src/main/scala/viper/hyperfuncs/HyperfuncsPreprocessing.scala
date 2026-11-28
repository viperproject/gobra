// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.hyperfuncs.HyperfuncsAst.isExecType
import viper.silver.ast._
import viper.silver.ast.utility.Simplifier
import viper.silver.ast.utility.rewriter.Traverse
import viper.silver.plugin.sif.{SIFLowEventExp, SIFLowExp, SIFRelExp}
import viper.silver.plugin.standard.termination.DecreasesTuple

import scala.collection.mutable

/**
  * Rewrites the type-checked program into the form that [[ExtendedProduct]] translates. It runs before Viper's
  * termination plugin:
  *
  *  0. Let-expressions whose bodies are relational are inlined (see [[inlineRelationalLets]]).
  *  1. Execution quantifiers `forall i: Exec :: e` (`exists`) are unrolled into a conjunction (disjunction) over the
  *     two executions.
  *  2. Execution identifiers become integers: parameters of type Exec get type Int and the implicit precondition
  *     `0 <= i && i <= 1`, and `rel(e, i)` with a parameter `i` becomes `i == 0 ? rel(e, 0) : rel(e, 1)`. The
  *     execution parameters of each hyperfunction are remembered, since they are passed once, not per execution.
  *  3. Hyperfunctions carry an annotation (see [[HyperfuncsAnnotations]]), which Viper's termination
  *     plugin copies to the termination proofs it generates for them. In their contracts and bodies, `low`, `rel` and `active` become calls of abstract marker
  *     functions, since the termination plugin does not support the information-flow constructs (as in Gobra's
  *     `HyperfuncMarkerTransformer`). [[unmark]] restores them after the termination plugin ran.
  */
object HyperfuncsPreprocessing {
  val lowMarkerKey: String = "hyperfunctionLowMarker"
  val relMarkerKey: String = "hyperfunctionRelMarker"
  val activeMarkerKey: String = "hyperfunctionActiveMarker"

  private def hasKey(info: Info, key: String): Boolean = info.getAllInfos[AnnotationInfo].exists(_.values.contains(key))
  private def isMarker(f: Function): Boolean =
    hasKey(f.info, lowMarkerKey) || hasKey(f.info, relMarkerKey) || hasKey(f.info, activeMarkerKey)

  /** Unrolls quantifiers over executions (bottom-up, such that nested quantifiers are unrolled first). */
  def unrollExecutionQuantifiers[N <: Node](n: N): N = n.transform({
    case q: QuantifiedExp if q.variables.exists(v => isExecType(v.typ)) => unroll(q)
  }, Traverse.BottomUp)

  private def unroll(q: QuantifiedExp): Exp = {
    val (execVars, others) = q.variables.partition(v => isExecType(v.typ))
    val assignments: Seq[Map[LocalVar, Exp]] = execVars.foldLeft(Seq(Map.empty[LocalVar, Exp])) { (acc, v) =>
      for (m <- acc; i <- Seq(0, 1)) yield m + (v.localVar -> HFExecLit(i)(q.pos))
    }
    // comparisons of execution literals are decided, such that instances like `0 != 0 && ...` disappear
    def decide(e: Exp): Exp = Simplifier.simplify(e.transform {
      case c@EqCmp(HFExecLit(a), HFExecLit(b)) => BoolLit(a == b)(c.pos, c.info, c.errT)
      case c@NeCmp(HFExecLit(a), HFExecLit(b)) => BoolLit(a != b)(c.pos, c.info, c.errT)
    })
    val instances = assignments.map { m =>
      val body = decide(q.exp.replace(m.asInstanceOf[Map[Node, Node]]))
      if (others.isEmpty) body
      else q match {
        case f: Forall =>
          val ts = f.triggers.map(t => t.replace(m.asInstanceOf[Map[Node, Node]]))
          val res = Forall(others, ts, body)(f.pos, f.info, f.errT)
          if (ts.isEmpty) res.autoTrigger else res
        case e: Exists =>
          val ts = e.triggers.map(t => t.replace(m.asInstanceOf[Map[Node, Node]]))
          Exists(others, ts, body)(e.pos, e.info, e.errT)
        case other => other
      }
    }
    q match {
      case _: Exists => instances.reduceLeft((a, b) => Or(a, b)(q.pos, q.info, q.errT))
      case _ => instances.reduceLeft((a, b) => And(a, b)(q.pos, q.info, q.errT))
    }
  }

  /**
    * Inlines `let x == e in b` where `b` contains relational constructs: the bound value is unary, i.e., one value per
    * execution, so `rel(x, i)` in `b` must denote the value of `e` in execution i, not the one of the execution in
    * which the let-expression is evaluated.
    */
  def inlineRelationalLets[N <: Node](n: N, isRelational: Node => Boolean): N = n.transform({
    case Let(v, e, body) if body.exists(isRelational) => body.replace(v.localVar, e)
  }, Traverse.BottomUp)

  /** Replaces execution identifiers by integers in `n`. */
  def lowerExecutions[N <: Node](n: N): N = n.transform({
    case d: LocalVarDecl if isExecType(d.typ) => LocalVarDecl(d.name, Int)(d.pos, d.info, d.errT)
    case v: LocalVar if isExecType(v.typ) => LocalVar(v.name, Int)(v.pos, v.info, v.errT)
    case l: HFExecLit => IntLit(l.n)(l.pos, l.info, l.errT)
    case r@HFRelExp(e, idx) =>
      val e2 = lowerExecutions(e)
      lowerExecutions(idx) match {
        case i: IntLit => SIFRelExp(e2, i)(r.pos, r.info, r.errT)
        case i => CondExp(EqCmp(i, IntLit(0)(r.pos))(r.pos),
          SIFRelExp(e2, IntLit(0)(r.pos))(r.pos, r.info, r.errT),
          SIFRelExp(e2, IntLit(1)(r.pos))(r.pos, r.info, r.errT))(r.pos, r.info, r.errT)
      }
    case a@HFActiveExp(idx) => HFActiveExp(lowerExecutions(idx))(a.pos, a.info, a.errT)
  })

  /**
    * Applies steps 1-3 to a program that passed [[HyperfuncsChecks]]. Returns the program and, for each hyperfunction,
    * the indices of its execution parameters.
    */
  def apply(p: Program, hyper: Set[String]): (Program, Map[String, Set[Int]]) = {
    // parameters of type Exec (Viper's surface syntax) and the positions listed in the annotation (front ends)
    val execParams: Map[String, Set[Int]] = p.functions.filter(f => hyper(f.name)).map(f =>
      f.name -> (f.formalArgs.zipWithIndex.collect { case (a, i) if isExecType(a.typ) => i }.toSet ++
        HyperfuncsAnnotations.executionParameters(f.info))).toMap

    def isRelational(n: Node): Boolean = n match {
      case _: SIFLowExp | _: SIFLowEventExp | _: SIFRelExp | _: HFRelExp | _: HFActiveExp => true
      case f: FuncApp => hyper(f.funcname)
      case q: QuantifiedExp => q.variables.exists(v => isExecType(v.typ))
      case _ => false
    }
    val unrolled = unrollExecutionQuantifiers(inlineRelationalLets(p, isRelational))

    val functions = unrolled.functions.map { f =>
      if (!hyper(f.name)) lowerExecutions(f)
      else {
        // implicit range of the execution parameters
        val ranges = f.formalArgs.filter(a => isExecType(a.typ)).map { a =>
          val v = LocalVar(a.name, Int)(a.pos)
          And(LeCmp(IntLit(0)(a.pos), v)(a.pos), LeCmp(v, IntLit(1)(a.pos))(a.pos))(a.pos)
        }
        val lowered = lowerExecutions(f)
        lowered.copy(pres = ranges ++ lowered.pres)(lowered.pos, lowered.info, lowered.errT)
      }
    }
    val lowered = unrolled.copy(
      domains = unrolled.domains.filterNot(_.name == HyperfuncsAst.ExecTypeName).map(lowerExecutions(_)),
      functions = functions,
      predicates = unrolled.predicates.map(lowerExecutions(_)),
      methods = unrolled.methods.map(lowerExecutions(_)))(p.pos, p.info, p.errT)
    (mark(lowered, hyper), execParams)
  }

  /** Step 3: markers for `low`, `rel` and `active` in hyperfunctions. */
  def mark(p: Program, hyper: Set[String]): Program = {
    val used = mutable.Set.empty[String] ++ p.collect { case d: Declaration => d.name }
    def fresh(base: String): String = {
      var i = 0
      while (used(s"$base$i")) i += 1
      used += s"$base$i"
      s"$base$i"
    }
    val markers = mutable.LinkedHashMap.empty[(String, Type), Function]
    def marker(key: String, base: String, args: Seq[LocalVarDecl], typ: Type, forType: Type): Function =
      markers.getOrElseUpdate((key, forType), {
        // the marker terminates, which the termination plugin checks for calls from functions with decreases clauses
        Function(fresh(base), args, typ, Seq(DecreasesTuple(Seq.empty, None)()), Seq.empty, None)(info = AnnotationInfo(Map(key -> Seq.empty)))
      })
    def markExp(e: Exp): Exp = e.transform {
      case l@SIFLowExp(x, None, _) =>
        val m = marker(lowMarkerKey, "hf_low_marker", Seq(LocalVarDecl("e", x.typ)()), Bool, x.typ)
        FuncApp(m, Seq(markExp(x)))(l.pos, l.info, l.errT)
      case r@SIFRelExp(x, i) =>
        val m = marker(relMarkerKey, "hf_rel_marker", Seq(LocalVarDecl("e", x.typ)(), LocalVarDecl("i", Int)()), x.typ, x.typ)
        FuncApp(m, Seq(markExp(x), i))(r.pos, r.info, r.errT)
      case a@HFActiveExp(i) =>
        val m = marker(activeMarkerKey, "hf_active_marker", Seq(LocalVarDecl("i", Int)()), Bool, Bool)
        FuncApp(m, Seq(i))(a.pos, a.info, a.errT)
    }
    val functions = p.functions.map { f =>
      if (!hyper(f.name)) f
      else f.copy(pres = f.pres.map(markExp), posts = f.posts.map(markExp), body = f.body.map(markExp))(f.pos, f.info, f.errT)
    }
    p.copy(functions = functions ++ markers.values)(p.pos, p.info, p.errT)
  }

  /** Restores `low`, `rel` and `active` from their markers and removes the marker functions. */
  def unmark(p: Program): Program = {
    val markers = p.functions.filter(isMarker).map(f => f.name -> f).toMap
    if (markers.isEmpty) return p
    val unmarked = p.copy(functions = p.functions.filterNot(isMarker))(p.pos, p.info, p.errT)
    unmarked.transform({
      case f: FuncApp if markers.contains(f.funcname) =>
        val m = markers(f.funcname)
        if (hasKey(m.info, lowMarkerKey)) SIFLowExp(f.args.head)(f.pos, f.info, f.errT)
        else if (hasKey(m.info, relMarkerKey)) SIFRelExp(f.args.head, f.args(1).asInstanceOf[IntLit])(f.pos, f.info, f.errT)
        else HFActiveExp(f.args.head)(f.pos, f.info, f.errT)
    }, Traverse.BottomUp)
  }
}
