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
         * (see [[TerminationEncoding.markerOrder]]). Implementation proofs call the implementation and are called from
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
            lastElem: vpr.Exp = measure match {
              case _: in.ItfMethodMeasure =>
                val rank = rankOf(measure)
                maxRank = math.max(maxRank, rank)
                markerApp(itfMarkerName(rank), pos, info, errT)
              case _: in.NonItfMethodMeasure => markerApp(nonItfMarkerName, pos, info, errT)
            }
          } yield termination.DecreasesTuple(v :+ lastElem, c)(pos, markImported(measure, info), errT)
        case m: in.WildcardMeasure =>
          for {
            c <- option(m.cond map ctx.expression)
          } yield termination.DecreasesWildcard(c)(pos, markImported(measure, info), errT)
      }
  }

  /** The largest rank of an interface method encountered so far. */
  private var maxRank: Int = 0

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
    *     function ProofMeasure(): TerminationDomain, function ItfMethodMeasure(): TerminationDomain   // rank 0
    *     function ProofMeasure$k(): TerminationDomain, function ItfMethodMeasure$k(): TerminationDomain // rank k > 0
    *     function ClosureProofMeasure(): TerminationDomain
    *   }
    *
    *   domain TerminationDomainWellFoundedOrder {
    *     axiom { decreasing(m1, m2) } for every pair of markers with m1 < m2 (see [[TerminationEncoding.markerOrder]])
    *     axiom { bounded(m) } for every marker m
    *   }
    */
  override def finalize(addMemberFn: Member => Unit): Unit = {
    val names = markerNames(maxRank)
    val funcs = names.map(markerFunc)
    addMemberFn(vpr.Domain(name = domainName, functions = funcs, axioms = Seq.empty, typVars = Seq.empty, interpretations = None)(NoPosition, NoInfo, NoTrafos))

    val wfOrderDomainName = "WellFoundedOrder"
    val termDomainWFOrderName = domainName + "WellFoundedOrder"
    val typVarMap = Map(vpr.TypeVar("T") -> domainType)
    def wfApp(fname: String, args: Seq[vpr.Exp]): vpr.Exp =
      vpr.DomainFuncApp(funcname = fname, args = args, typVarMap = typVarMap)(NoPosition, NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, NoTrafos)
    val decreasingAxioms = for {
      smaller <- names
      bigger <- names
      if markerLt(smaller, bigger)
    } yield wfApp("decreasing", Seq(markerApp(smaller), markerApp(bigger)))
    val boundedAxioms = names.map(n => wfApp("bounded", Seq(markerApp(n))))
    val axioms = (decreasingAxioms ++ boundedAxioms).map(e => vpr.AnonymousDomainAxiom(e)(domainName = termDomainWFOrderName))
    addMemberFn(vpr.Domain(name = termDomainWFOrderName, functions = Seq.empty, axioms = axioms, typVars = Seq.empty, interpretations = None)(NoPosition, NoInfo, NoTrafos))
  }
}

object TerminationEncoding {
  val domainName = "TerminationDomain"
  val domainType: vpr.DomainType = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty)

  private val nonItfMarkerName = "NonItfMethodMeasure"
  private val closureProofMarkerName = "ClosureProofMeasure"
  private def itfMarkerName(rank: Int): String = if (rank == 0) "ItfMethodMeasure" else s"ItfMethodMeasure$$$rank"
  private def proofMarkerName(rank: Int): String = if (rank == 0) "ProofMeasure" else s"ProofMeasure$$$rank"

  private def markerNames(maxRank: Int): Seq[String] =
    nonItfMarkerName +: (0 to maxRank).flatMap(r => Seq(proofMarkerName(r), itfMarkerName(r))) :+ closureProofMarkerName

  /**
    * The order of kind markers: `NonItfMethodMeasure` is below the markers of implementation proofs and interface
    * methods, which are ordered by rank and, for the same rank, the proof below the interface method. The marker of
    * closure implementation proofs is above all others.
    */
  private def markerOrder(name: String): (Int, Int) = name match {
    case `nonItfMarkerName` => (-1, 0)
    case `closureProofMarkerName` => (Int.MaxValue, 0)
    case "ProofMeasure" => (0, 1)
    case "ItfMethodMeasure" => (0, 2)
    case s"ProofMeasure$$$r" => (r.toInt, 1)
    case s"ItfMethodMeasure$$$r" => (r.toInt, 2)
    case _ => violation(s"unexpected kind marker $name")
  }

  private def markerLt(l: String, r: String): Boolean = {
    import scala.math.Ordering.Implicits._
    markerOrder(l) < markerOrder(r)
  }

  private def markerFunc(name: String): vpr.DomainFunc =
    vpr.DomainFunc(name = name, formalArgs = Seq.empty, typ = domainType, interpretation = None)(NoPosition, NoInfo, domainName, NoTrafos)

  private def markerApp(name: String, pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos): vpr.DomainFuncApp =
    vpr.DomainFuncApp(func = markerFunc(name), args = Seq.empty, typVarMap = Map.empty)(pos, info, errT)

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
    case s"ItfMethodMeasure$$$r" => proofMarkerName(r.toInt)
    case _ => proofMarkerName(0)
  })

  /**
    * Turns the termination measure `exp` of a closure spec into the measure of a closure implementation proof by
    * replacing its kind marker with `ClosureProofMeasure()`, which is above every other marker.
    */
  def toClosureProofMeasure(exp: vpr.Exp): vpr.Exp = replaceMarker(exp, imported = false, _ => closureProofMarkerName)

  private def isMarkerName(name: String): Boolean = name == nonItfMarkerName || name.startsWith("ItfMethodMeasure") ||
    name.startsWith("ProofMeasure") || name == closureProofMarkerName

  private def replaceMarker(exp: vpr.Exp, imported: Boolean, replacement: String => String): vpr.Exp = exp.transform {
    case t: termination.DecreasesTuple =>
      val newTuple = t.tupleExpressions.map(_.transform {
        case app: vpr.DomainFuncApp if app.domainName == domainName && isMarkerName(app.funcname) =>
          markerApp(replacement(app.funcname), app.pos, app.info, app.errT)
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
