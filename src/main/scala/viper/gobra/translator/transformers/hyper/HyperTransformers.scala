// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.translator.transformers.hyper

import viper.gobra.backend.BackendVerifier
import viper.gobra.reporting.Source
import viper.gobra.translator.transformers.ViperTransformer
import viper.gobra.translator.util.VprInfo
import viper.hyperfuncs.HyperfuncsAnnotations
import viper.silver.ast._
import viper.silver.plugin.sif.{SIFLowEventExp, SIFLowExp, SIFRelExp}
import viper.silver.verifier.{AbstractError, ConsistencyError}

/**
  * Without hyper mode (`--hyperMode off`), relational constructs are removed: `low` and `lowContext()` become `true`
  * (reported as errors where they would change proof obligations), hyperfuncs are removed, `active(i)` is `true`, and
  * `rel` is an error. With a hyper mode, the product program is built inside Viper by the hyperfuncs plugin (see
  * `ViperBackend.buildOptions`).
  */
class HyperDisabledTransformer extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] =
    HyperDisabledTransformer.removeHyper(HyperDisabledTransformer.removeActive(task.program))
      .map(BackendVerifier.Task(_, task.backtrack))
}

object HyperDisabledTransformer {

  /** Every execution is active, so `active(i)` is `true`. The function encoding `active` is removed. */
  def removeActive(p: Program): Program = {
    val active = p.functions.filter(f => VprInfo.isActiveExecution(f.info)).map(_.name).toSet
    if (active.isEmpty) p
    else {
      val replaced = p.transform { case f: FuncApp if active(f.funcname) => TrueLit()(f.pos, f.info, f.errT) }
      replaced.copy(functions = replaced.functions.filterNot(f => active(f.name)))(p.pos, p.info, p.errT)
    }
  }

  def removeHyper(p: Program): Either[Seq[AbstractError], Program] = {
    val hyperFuncs = p.functions.collect { case f if VprInfo.isHyperFunc(f.info) => f.name }.toSet
    val withoutHyperFuncs = p.copy(functions = p.functions.filterNot(f => hyperFuncs(f.name)))(p.pos, p.info, p.errT)
    removeLow(withoutHyperFuncs, hyperFuncs)
  }

  def removeLow[N <: Node](n: N, hyperFuncs: String => Boolean = _ => false): Either[Seq[AbstractError], N] = {
    def memberHasProofObligations(m: Member): Boolean = m match {
      case m: Method => m.body.nonEmpty
      case m: Function => m.body.nonEmpty
      case _ => true // conservative choice
    }

    var errs: Seq[AbstractError] = Seq.empty

    def removeNodeAndReportErrorIfProofObligationsChange(n: Exp, optMember: Option[Member], message: String): (Node, Option[Member]) = {
      if (optMember.forall(memberHasProofObligations)) {
        errs = errs :+ ConsistencyError(message, n.pos)
      }
      (TrueLit()(n.pos, n.info, n.errT), optMember)
    }
    val transformedN = n.transformWithContext[Option[Member]]({
      case (m: Member, _) => (m, Some(m)) // keep node unchanged and propagate `m` as context
      case (n: SIFLowExp, optMember) =>
        removeNodeAndReportErrorIfProofObligationsChange(n, optMember, s"Low expression found: ${n.exp}")
      case (n: SIFLowEventExp, optMember) =>
        removeNodeAndReportErrorIfProofObligationsChange(n, optMember, s"Low context expression found")
      case (n: FuncApp, optMember) if hyperFuncs(n.funcname) =>
        if (n.typ != Bool) {
          // a non-boolean hyper function cannot be replaced while preserving well-typedness
          errs = errs :+ ConsistencyError(s"Call to hyper function found: $n", n.pos)
          (n, optMember)
        } else removeNodeAndReportErrorIfProofObligationsChange(n, optMember, s"Call to hyper function found: $n")
      case (n: SIFRelExp, optMember) =>
        // it's unclear how to remove a `rel(_, _)` expressions while preserving well-typedness. Thus,
        // we always report an error:
        errs = errs :+ ConsistencyError("Rel expression found: $n", n.pos)
        (n, optMember)
    }, None)
    if (errs.isEmpty) Right(transformedN)
    else Left(errs)
  }
}

/**
  * Members tagged `#[MAJOR]` in their source exist only in execution 0 of the lockstep product (`--hyperMode on`). The
  * hyperfuncs plugin, which builds the product, reads this from the annotation [[HyperfuncsAnnotations.majorKey]].
  */
class MajorAnnotationTransformer extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] =
    Right(BackendVerifier.Task(MajorAnnotationTransformer.annotate(task.program), task.backtrack))
}

object MajorAnnotationTransformer {

  private def isTaggedMajor(m: Member): Boolean =
    Source.unapply(m).exists(_.origin.tag.contains("#[MAJOR]"))

  private def annotateMember[M <: Member](m: M): M =
    if (isTaggedMajor(m)) m.withMeta((m.pos, ConsInfo(AnnotationInfo(Map(HyperfuncsAnnotations.majorKey -> Seq.empty)), m.info), m.errT)).asInstanceOf[M]
    else m

  def annotate(p: Program): Program =
    p.copy(
      domains = p.domains.map(annotateMember),
      fields = p.fields.map(annotateMember),
      functions = p.functions.map(annotateMember),
      predicates = p.predicates.map(annotateMember),
      methods = p.methods.map(annotateMember),
    )(p.pos, p.info, p.errT)
}
