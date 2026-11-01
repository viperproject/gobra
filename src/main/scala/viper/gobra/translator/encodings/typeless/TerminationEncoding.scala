// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

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

  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    case measure: in.TerminationMeasure =>
      val (pos, info, errT) = measure.vprMeta

      measure match {
        /**
         * The last element of every tuple termination measure records whether the measure belongs to an interface
         * method (resp. closure spec), to an implementation proof, or to an implementation. These elements are ordered
         * as follows: NonItfMethodMeasure < ProofMeasure < ItfMethodMeasure < ClosureProofMeasure. Implementation
         * proofs call the implementation and are called from the interface method, which turns the strict decrease
         * required at these calls into a non-strict one: the measure of the implementation may be at most the measure
         * of the interface. Similarly, the call in a closure implementation proof (which may be the call of an
         * interface method) may have at most the measure of the implemented spec.
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
              case _: in.ItfMethodMeasure => itfMethodApp(pos, info, errT)
              case _: in.NonItfMethodMeasure => nonItfMethodApp(pos, info, errT)
            }
          } yield termination.DecreasesTuple(v :+ lastElem, c)(pos, markImported(measure, info), errT)
        case m: in.WildcardMeasure =>
          for {
            c <- option(m.cond map ctx.expression)
          } yield termination.DecreasesWildcard(c)(pos, markImported(measure, info), errT)
      }
  }

  private def markImported(measure: in.TerminationMeasure, info: vpr.Info): vpr.Info = {
    @tailrec
    def isImported(origin: Source.AbstractOrigin): Boolean = origin match {
      case Source.AnnotatedOrigin(_, Source.ImportedTerminationMeasureAnnotation) => true
      case Source.AnnotatedOrigin(o, _) => isImported(o)
      case _ => false
    }
    if (measure.info.origin.exists(isImported)) vpr.MakeInfoPair(TerminationEncoding.ImportedMeasureInfo, info) else info
  }

  def predicateInstance(x: in.Access)(ctx: Context): CodeWriter[predicateinstance.PredicateInstance] = {
    val (pos, info, errT) = x.vprMeta
    for {
      v <- ctx.assertion(x)
      pap = v.asInstanceOf[vpr.PredicateAccessPredicate]
    } yield predicateinstance.PredicateInstance(pap.loc.predicateName, pap.loc.args)(pos, info, errT)
  }

  /**
   * Generates the following domains for termination measures related to interface/non-interface methods:
   *
   *   domain TerminationDomain {
   *       function ItfMethodMeasure(): TerminationDomain
   *       function ProofMeasure(): TerminationDomain
   *       function NonItfMethodMeasure(): TerminationDomain
   *   }
   */
  private val domainName = "TerminationDomain"
  private val itfMethod = vpr.DomainFunc(
    name = "ItfMethodMeasure",
    formalArgs = Seq.empty,
    typ = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty),
    interpretation = None
  )(vpr.NoPosition, vpr.NoInfo, domainName, vpr.NoTrafos)
  private def itfMethodApp(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos) =
    vpr.DomainFuncApp(func = itfMethod, args = Seq.empty, typVarMap = Map.empty)(pos, info, errT)
  private def proofApp(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos) =
    vpr.DomainFuncApp(func = TerminationEncoding.proofMethod, args = Seq.empty, typVarMap = Map.empty)(pos, info, errT)
  private val nonItfMethod = vpr.DomainFunc(
    name = "NonItfMethodMeasure",
    formalArgs = Seq.empty,
    typ = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty),
    interpretation = None
  )(vpr.NoPosition, vpr.NoInfo, domainName, vpr.NoTrafos)
  private def nonItfMethodApp(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos) =
    vpr.DomainFuncApp(func = nonItfMethod, args = Seq.empty, typVarMap = Map.empty)(pos, info, errT)
  private def closureProofApp(pos: Position = NoPosition, info: Info = NoInfo, errT: ErrorTrafo = NoTrafos) =
    vpr.DomainFuncApp(func = TerminationEncoding.closureProofMethod, args = Seq.empty, typVarMap = Map.empty)(pos, info, errT)
  private val termDomain = vpr.Domain(
    name = domainName,
    functions = Seq(itfMethod, TerminationEncoding.proofMethod, nonItfMethod, TerminationEncoding.closureProofMethod),
    axioms = Seq.empty,
    typVars = Seq.empty,
    interpretations = None
  )(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

  /**
   * Generates the following domains that defines the well-founded order on termination measures related
   * to interface/non-interface methods:
   *
   * domain TerminationDomainWellFoundedOrder {
   *    axiom {
   *        decreasing(NonItfMethodMeasure(), ItfMethodMeasure())
   *    }
   *
   *    axiom {
   *        decreasing(NonItfMethodMeasure(), ProofMeasure()) && decreasing(ProofMeasure(), ItfMethodMeasure())
   *    }
   *
   *    axiom {
   *        decreasing(NonItfMethodMeasure(), ClosureProofMeasure()) && decreasing(ProofMeasure(), ClosureProofMeasure()) &&
   *          decreasing(ItfMethodMeasure(), ClosureProofMeasure())
   *    }
   *
   *    axiom {
   *        bounded(NonItfMethodMeasure())
   *    }
   *
   *    axiom {
   *        bounded(ItfMethodMeasure())
   *    }
   *
   *    axiom {
   *        bounded(ClosureProofMeasure())
   *    }
   * }
   */
  private def decreasingApp(l: vpr.Exp, r: vpr.Exp): vpr.Exp = vpr.DomainFuncApp(
    funcname = "decreasing",
    args = Seq(l, r),
    typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
  )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos)
  private val wfOrderDomainName = "WellFoundedOrder"
  private val termDomainWFOrderName = domainName + "WellFoundedOrder"
  private val termDomainWFOrder = vpr.Domain(
    name = termDomainWFOrderName,
    functions = Seq.empty,
    axioms = Seq(
      // decreasing(nonItfMethod, itfMethod)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "decreasing",
          args = Seq(nonItfMethodApp(), itfMethodApp()),
          typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName),
      // decreasing(nonItfMethod, proof) && decreasing(proof, itfMethod)
      vpr.AnonymousDomainAxiom(
        exp = vpr.And(
          vpr.DomainFuncApp(
            funcname = "decreasing",
            args = Seq(nonItfMethodApp(), proofApp()),
            typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
          )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos),
          vpr.DomainFuncApp(
            funcname = "decreasing",
            args = Seq(proofApp(), itfMethodApp()),
            typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
          )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos)
        )()
      )(domainName = termDomainWFOrderName),
      // decreasing(nonItfMethod, closureProof) && decreasing(proof, closureProof) && decreasing(itfMethod, closureProof)
      vpr.AnonymousDomainAxiom(
        exp = vpr.And(
          decreasingApp(nonItfMethodApp(), closureProofApp()),
          vpr.And(decreasingApp(proofApp(), closureProofApp()), decreasingApp(itfMethodApp(), closureProofApp()))()
        )()
      )(domainName = termDomainWFOrderName),
      // bounded(closureProof)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "bounded",
          args = Seq(closureProofApp()),
          typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName),
      // bounded(proof)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "bounded",
          args = Seq(proofApp()),
          typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName),
      // bounded(nonItfMethod)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "bounded",
          args = Seq(nonItfMethodApp()),
          typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName),
      // bounded(itfMethod)
      vpr.AnonymousDomainAxiom(
        exp = vpr.DomainFuncApp(
          funcname = "bounded",
          args = Seq(itfMethodApp()),
          typVarMap = Map(vpr.TypeVar("T") -> vpr.DomainType(termDomain, Map.empty))
        )(vpr.NoPosition, vpr.NoInfo, typ = vpr.Bool, domainName = wfOrderDomainName, vpr.NoTrafos)
      )(domainName = termDomainWFOrderName)

    ),
    typVars = Seq.empty,
    interpretations = None
  )(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos)

  override def finalize(addMemberFn: Member => Unit): Unit = {
    addMemberFn(termDomain)
    addMemberFn(termDomainWFOrder)
  }
}

object TerminationEncoding {
  val domainName = "TerminationDomain"

  private[typeless] val proofMethod = vpr.DomainFunc(
    name = "ProofMeasure",
    formalArgs = Seq.empty,
    typ = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty),
    interpretation = None
  )(vpr.NoPosition, vpr.NoInfo, domainName, vpr.NoTrafos)

  private[typeless] val closureProofMethod = vpr.DomainFunc(
    name = "ClosureProofMeasure",
    formalArgs = Seq.empty,
    typ = vpr.DomainType(domainName = domainName, partialTypVarsMap = Map.empty)(Seq.empty),
    interpretation = None
  )(vpr.NoPosition, vpr.NoInfo, domainName, vpr.NoTrafos)

  /** Marks the termination measures of members of imported packages. */
  case object ImportedMeasureInfo extends vpr.Info {
    override val comment: Seq[String] = Nil
    override val isCached: Boolean = false
  }

  /**
    * Turns the termination measures in `exp` (stemming from an interface method or closure spec) into measures of an
    * implementation proof by replacing their last element with `ProofMeasure()`.
    */
  def toProofMeasure(exp: vpr.Exp, imported: Boolean): vpr.Exp = replaceLastElement(exp, imported, proofMethod)

  /**
    * Turns the termination measure `exp` of a closure spec into the measure of a closure implementation proof by
    * replacing its last element with `ClosureProofMeasure()`, which is above the last element of every other measure.
    */
  def toClosureProofMeasure(exp: vpr.Exp): vpr.Exp = replaceLastElement(exp, imported = false, closureProofMethod)

  private def replaceLastElement(exp: vpr.Exp, imported: Boolean, last: vpr.DomainFunc): vpr.Exp = exp.transform {
    case t: termination.DecreasesTuple =>
      val newTuple = t.tupleExpressions.map(_.transform {
        case app: vpr.DomainFuncApp if app.funcname == "ItfMethodMeasure" || app.funcname == "NonItfMethodMeasure" =>
          vpr.DomainFuncApp(last, Seq.empty, Map.empty)(app.pos, app.info, app.errT)
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
