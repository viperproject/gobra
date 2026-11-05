// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation.violation
import viper.silver.ast.{ErrorTrafo, Info, Member, NoInfo, NoPosition, NoTrafos, Position}
import viper.silver.plugin.standard.{predicateinstance, termination}
import viper.silver.{ast => vpr}
import viper.gobra.reporting.Source

import scala.annotation.tailrec

class TerminationEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import TerminationEncoding._

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    case measure: in.TerminationMeasure =>
      val (pos, info, errT) = measure.vprMeta

      measure match {
        /**
         * The last element of every tuple termination measure is a kind marker, which records whether the measure
         * belongs to an interface method (resp. closure spec), to an implementation proof, or to an implementation
         * (see the order described in `finalize`). Implementation proofs call the implementation and are called from
         * the interface method, which turns the strict decrease required at these calls into a non-strict one: the
         * measure of the implementation may be at most the measure of the interface. Similarly, the call in a closure
         * implementation proof (which may be the call of an interface method) may have at most the measure of the
         * implemented spec. The markers of interface methods and their proofs include the rank of the interface method.
         *
         * Termination measures of members of imported packages are marked, as their termination has already been
         * checked when verifying their package (see [[viper.gobra.translator.transformers.ModularTerminationTransformer]]).
         */
        case t: in.TupleTerminationMeasure =>
          for {
            c <- option(t.cond map ctx.expression)
            v <- sequence(t.tuple.map {
              case e: in.Expr => ctx.expression(e)
              case p: in.Access => predicateInstance(p)(ctx)
              case _ => violation("invalid tuple measure argument")
            })
            lastElem: vpr.Exp = (measure, outlineHeightOf(measure)) match {
              case (_, Some(height)) => rankedMarkerApp(outlineMarkerName, height, pos, info, errT)
              case (_: in.ItfMethodMeasure, _) => rankedMarkerApp(itfMarkerName, rankOf(measure), pos, info, errT)
              case (_: in.NonItfMethodMeasure, _) => markerApp(nonItfMarkerName, pos, info, errT)
            }
          } yield termination.DecreasesTuple(v :+ lastElem, c)(pos, markImported(measure, info), errT)
        case m: in.WildcardMeasure =>
          for {
            c <- option(m.cond map ctx.expression)
          } yield termination.DecreasesWildcard(c)(pos, markImported(measure, info), errT)
      }
  }

  private def annotations(measure: in.TerminationMeasure): Seq[Source.Annotation] = {
    @tailrec
    def go(origin: Source.AbstractOrigin, acc: Seq[Source.Annotation]): Seq[Source.Annotation] = origin match {
      case Source.AnnotatedOrigin(o, a) => go(o, a +: acc)
      case _ => acc
    }
    measure.info.origin.fold(Seq.empty[Source.Annotation])(go(_, Seq.empty))
  }

  private def rankOf(measure: in.TerminationMeasure): Int =
    annotations(measure).collectFirst { case Source.InterfaceMethodRankAnnotation(r) => r }.getOrElse(0)

  private def outlineHeightOf(measure: in.TerminationMeasure): Option[Int] =
    annotations(measure).collectFirst { case Source.OutlineHeightAnnotation(h) => h }

  private def markImported(measure: in.TerminationMeasure, info: vpr.Info): vpr.Info =
    if (annotations(measure).contains(Source.ImportedTerminationMeasureAnnotation)) vpr.MakeInfoPair(TerminationEncoding.ImportedMeasureInfo, info) else info

  def predicateInstance(x: in.Access)(ctx: Context): CodeWriter[predicateinstance.PredicateInstance] = {
    val (pos, info, errT) = x.vprMeta
    for {
      v <- ctx.assertion(x)
      pap = v.asInstanceOf[vpr.PredicateAccessPredicate]
    } yield predicateinstance.PredicateInstance(pap.loc.predicateName, pap.loc.args)(pos, info, errT)
  }

  /**
    * Generates the domain of kind markers and their well-founded order:
    *
    *   domain TerminationDomain {
    *     function NonItfMethodMeasure(): TerminationDomain
    *     function ProofMeasure(rank: Int): TerminationDomain
    *     function ItfMethodMeasure(rank: Int): TerminationDomain
    *     function ClosureProofMeasure(): TerminationDomain
    *     function OutlineMeasure(height: Int): TerminationDomain
    *   }
    *
    * The order (see the order described in `finalize`) is axiomatized by a fixed number of axioms, independently of
    * the number of ranks. The axioms only state when markers decrease, never the converse.
    */
  override def finalize(addMemberFn: Member => Unit): Unit = {
    val funcs = Seq(nonItfFunc, proofFunc, itfFunc, closureProofFunc, outlineFunc)
    addMemberFn(vpr.Domain(name = domainName, functions = funcs, axioms = Seq.empty, typVars = Seq.empty, interpretations = None)(NoPosition, NoInfo, NoTrafos))

    val wfOrderDomainName = "WellFoundedOrder"
    val termDomainWFOrderName = domainName + "WellFoundedOrder"
    val typVarMap = Map(vpr.TypeVar("T") -> domainType)
    def wfApp(fname: String, args: Seq[vpr.Exp]): vpr.Exp =
      vpr.DomainFuncApp(funcname = fname, args = args, typVarMap = typVarMap)(NoPosition, NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, NoTrafos)
    def dec(l: vpr.Exp, r: vpr.Exp): vpr.Exp = wfApp("decreasing", Seq(l, r))
    val i = vpr.LocalVarDecl("i", vpr.Int)()
    val j = vpr.LocalVarDecl("j", vpr.Int)()
    def ranked(f: vpr.DomainFunc, v: vpr.LocalVarDecl): vpr.Exp = vpr.DomainFuncApp(f, Seq(v.localVar), Map.empty)()
    val nonItf = vpr.DomainFuncApp(nonItfFunc, Seq.empty, Map.empty)()
    val closureProof = vpr.DomainFuncApp(closureProofFunc, Seq.empty, Map.empty)()
    def forall1(body: vpr.Exp, trigger: vpr.Exp): vpr.Exp = vpr.Forall(Seq(i), Seq(vpr.Trigger(Seq(trigger))()), body)()
    def forall2(cond: vpr.Exp, d: vpr.Exp): vpr.Exp = vpr.Forall(Seq(i, j), Seq(vpr.Trigger(Seq(d))()), vpr.Implies(cond, d)())()
    val lt = vpr.LtCmp(i.localVar, j.localVar)()
    val le = vpr.LeCmp(i.localVar, j.localVar)()
    val axioms = Seq(
      // the marker of implementations is below all others
      { val d = dec(nonItf, ranked(proofFunc, i)); forall1(d, d) },
      { val d = dec(nonItf, ranked(itfFunc, i)); forall1(d, d) },
      dec(nonItf, closureProof),
      // proofs and interface methods are ordered by rank, and for the same rank, the proof is below the interface method
      forall2(le, dec(ranked(proofFunc, i), ranked(itfFunc, j))),
      forall2(lt, dec(ranked(itfFunc, i), ranked(proofFunc, j))),
      forall2(lt, dec(ranked(itfFunc, i), ranked(itfFunc, j))),
      forall2(lt, dec(ranked(proofFunc, i), ranked(proofFunc, j))),
      // the marker of closure proofs is above all others
      { val d = dec(ranked(proofFunc, i), closureProof); forall1(d, d) },
      { val d = dec(ranked(itfFunc, i), closureProof); forall1(d, d) },
      // the markers of outlines are below all others, ordered by their nesting height (an outline nested in another one
      // has a smaller height), see `TerminationLevelsTransform`
      { val d = dec(ranked(outlineFunc, i), nonItf); forall1(d, d) },
      { val d = dec(ranked(outlineFunc, i), closureProof); forall1(d, d) },
      forall2(vpr.TrueLit()(), dec(ranked(outlineFunc, i), ranked(proofFunc, j))),
      forall2(vpr.TrueLit()(), dec(ranked(outlineFunc, i), ranked(itfFunc, j))),
      forall2(vpr.And(vpr.LeCmp(vpr.IntLit(0)(), i.localVar)(), lt)(), dec(ranked(outlineFunc, i), ranked(outlineFunc, j))),
      { val b = wfApp("bounded", Seq(ranked(outlineFunc, i))); forall1(b, b) },
      // all markers are bounded
      wfApp("bounded", Seq(nonItf)),
      wfApp("bounded", Seq(closureProof)),
      { val b = wfApp("bounded", Seq(ranked(proofFunc, i))); forall1(b, b) },
      { val b = wfApp("bounded", Seq(ranked(itfFunc, i))); forall1(b, b) },
    ).map(e => vpr.AnonymousDomainAxiom(e)(domainName = termDomainWFOrderName))
    addMemberFn(vpr.Domain(name = termDomainWFOrderName, functions = Seq.empty, axioms = axioms, typVars = Seq.empty, interpretations = None)(NoPosition, NoInfo, NoTrafos))
  }
}

object TerminationEncoding {
  val domainName = "TerminationDomain"
  val domainType: vpr.DomainType = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty)

  private val nonItfMarkerName = "NonItfMethodMeasure"
  private val closureProofMarkerName = "ClosureProofMeasure"
  private val itfMarkerName = "ItfMethodMeasure"
  private val proofMarkerName = "ProofMeasure"
  private val outlineMarkerName = "OutlineMeasure"

  private def markerFunc(name: String, ranked: Boolean): vpr.DomainFunc =
    vpr.DomainFunc(name = name, formalArgs = if (ranked) Seq(vpr.LocalVarDecl("rank", vpr.Int)()) else Seq.empty,
      typ = domainType, interpretation = None)(NoPosition, NoInfo, domainName, NoTrafos)
  private val nonItfFunc = markerFunc(nonItfMarkerName, ranked = false)
  private val closureProofFunc = markerFunc(closureProofMarkerName, ranked = false)
  private val itfFunc = markerFunc(itfMarkerName, ranked = true)
  private val proofFunc = markerFunc(proofMarkerName, ranked = true)
  private val outlineFunc = markerFunc(outlineMarkerName, ranked = true)

  /**
    * The order of kind markers: `NonItfMethodMeasure()` is below the markers of implementation proofs and interface
    * methods, which are ordered by rank and, for the same rank, the proof below the interface method. The marker of
    * closure implementation proofs is above all others.
    */
  private def markerApp(name: String, pos: Position, info: Info, errT: ErrorTrafo): vpr.DomainFuncApp =
    name match {
      case `nonItfMarkerName` => vpr.DomainFuncApp(func = nonItfFunc, args = Seq.empty, typVarMap = Map.empty)(pos, info, errT)
      case `closureProofMarkerName` => vpr.DomainFuncApp(func = closureProofFunc, args = Seq.empty, typVarMap = Map.empty)(pos, info, errT)
      case _ => violation(s"unexpected unranked kind marker $name")
    }

  private def rankedMarkerApp(name: String, rank: Int, pos: Position, info: Info, errT: ErrorTrafo): vpr.DomainFuncApp = {
    val func = if (name == itfMarkerName) itfFunc else if (name == outlineMarkerName) outlineFunc else proofFunc
    vpr.DomainFuncApp(func = func, args = Seq(vpr.IntLit(rank)(pos, info, errT)), typVarMap = Map.empty)(pos, info, errT)
  }

  /** Marks the termination measures of members of imported packages. */
  case object ImportedMeasureInfo extends vpr.Info {
    override val comment: Seq[String] = Nil
    override val isCached: Boolean = false
  }

  /**
    * Turns the termination measures in `exp` (stemming from an interface method or closure spec) into measures of an
    * implementation proof by replacing their kind marker with the proof marker of the same rank.
    */
  def toProofMeasure(exp: vpr.Exp, imported: Boolean): vpr.Exp = replaceMarker(exp, imported, {
    case app if app.funcname == itfMarkerName =>
      vpr.DomainFuncApp(func = proofFunc, args = app.args, typVarMap = Map.empty)(app.pos, app.info, app.errT)
    case app => rankedMarkerApp(proofMarkerName, 0, app.pos, app.info, app.errT)
  })

  /**
    * Turns the termination measure `exp` of a closure spec into the measure of a closure implementation proof by
    * replacing its kind marker with `ClosureProofMeasure()`, which is above every other marker.
    */
  def toClosureProofMeasure(exp: vpr.Exp): vpr.Exp =
    replaceMarker(exp, imported = false, app => markerApp(closureProofMarkerName, app.pos, app.info, app.errT))

  private def isMarkerName(name: String): Boolean =
    name == nonItfMarkerName || name == itfMarkerName || name == proofMarkerName || name == closureProofMarkerName || name == outlineMarkerName

  private def replaceMarker(exp: vpr.Exp, imported: Boolean, replacement: vpr.DomainFuncApp => vpr.DomainFuncApp): vpr.Exp = exp.transform {
    case t: termination.DecreasesTuple =>
      val newTuple = t.tupleExpressions.map(_.transform {
        case app: vpr.DomainFuncApp if app.domainName == domainName && isMarkerName(app.funcname) => replacement(app)
      })
      termination.DecreasesTuple(newTuple, t.condition)(t.pos, withImportedMarker(t.info, imported), t.errT)
    case w: termination.DecreasesWildcard =>
      termination.DecreasesWildcard(w.condition)(w.pos, withImportedMarker(w.info, imported), w.errT)
  }

  def isImported(n: vpr.Infoed): Boolean = n.info.getUniqueInfo[ImportedMeasureInfo.type].isDefined

  private def withImportedMarker(info: vpr.Info, imported: Boolean): vpr.Info = {
    val withoutMarker = info.removeUniqueInfo[ImportedMeasureInfo.type]
    if (imported) vpr.MakeInfoPair(ImportedMeasureInfo, withoutMarker) else withoutMarker
  }
}
