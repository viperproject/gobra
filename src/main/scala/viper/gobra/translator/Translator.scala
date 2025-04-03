// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator


import viper.gobra.ast.internal.Program
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.{Config, Hyper, PackageInfo}
import viper.gobra.reporting.{ConsistencyError, GeneratedViperMessage, TransformerFailureMessage, VerifierError}
import viper.gobra.translator.context.DfltTranslatorConfig
import viper.gobra.translator.encodings.programs.ProgramsImpl
import viper.gobra.translator.transformers.hyper.SIFLowGuardTransformerImpl
import viper.gobra.translator.transformers.{AssumeTransformer, TerminationDomainTransformer, ViperTransformer}
import viper.gobra.util.Violation
import viper.silver.ast.{AbstractSourcePosition, SourcePosition}
import viper.silver.ast.pretty.FastPrettyPrinter
import viper.silver.verifier.AbstractError
import viper.silver.{ast => vpr}

object Translator {

  private def createConsistencyErrors(errs: Seq[AbstractError]): Vector[ConsistencyError] =
    errs.map(err => {
      val pos = err.pos match {
        case sp: AbstractSourcePosition => Some(new SourcePosition(sp.file, sp.start, sp.end))
        case _ => None
      }
      ConsistencyError(err.readableMessage, pos)
    }).toVector

  def translate(program: Program, pkgInfo: PackageInfo)(config: Config): Either[Vector[VerifierError], BackendVerifier.Task] = {
    val translationConfig = new DfltTranslatorConfig()(config)
    val programTranslator = new ProgramsImpl()
    val task = programTranslator.translate(program)(translationConfig)

    // for hyper mode `EnabledExtended`, we use Viper's SIF plugin instead
    val sifTransformer =
      if (config.hyperModeOrDefault == Hyper.EnabledExtended) Seq.empty
      else Seq(new SIFLowGuardTransformerImpl(config))
    val transformers: Seq[ViperTransformer] = Seq(
      new AssumeTransformer,
      new TerminationDomainTransformer,
    ) ++ sifTransformer

    val transformedTask = transformers.foldLeft[Either[Vector[VerifierError], BackendVerifier.Task]](Right(task)) {
      case (Right(t), transformer) => transformer.transform(t).left.map(createConsistencyErrors)
      case (errs, _) => errs
    }

    if (config.checkConsistency) {
      transformedTask
        .flatMap(task => {
          val consistencyErrs = task.program.checkTransitively
          if (consistencyErrs.isEmpty) Right(())
          else Left(createConsistencyErrors(consistencyErrs))
        })
        .left.map(errs => Violation.violation(errs.toString))
    }

    val inputs = config.packageInfoInputMap(pkgInfo).map(_.name)
    transformedTask.fold(
      errs => config.reporter report TransformerFailureMessage(inputs, errs),
      task => config.reporter report GeneratedViperMessage(config.taskName, inputs, () => sortAst(task.program), () => task.backtrack))
    transformedTask
  }

  /**
    * sorts AST members alphabetically to ease the comparison of (similar) Viper ASTs
    */
  def sortAst(program: vpr.Program): vpr.Program = {
    lazy val memberOrdering: Ordering[vpr.Member] = Ordering.by(_.name)
    implicit lazy val domainFnOrdering: Ordering[vpr.DomainFunc] = Ordering.by(_.name)
    implicit lazy val domainAxOrdering: Ordering[vpr.DomainAxiom] = Ordering.by(FastPrettyPrinter.pretty(_))

    def sortDomain(domain: vpr.Domain): vpr.Domain = {
      vpr.Domain(
        domain.name,
        domain.functions.sorted,
        domain.axioms.sorted,
        domain.typVars
      )(domain.pos, domain.info, domain.errT)
    }

    vpr.Program(
      program.domains.map(sortDomain).sorted(memberOrdering),
      program.fields.sorted(memberOrdering),
      program.functions.sorted(memberOrdering),
      program.predicates.sorted(memberOrdering),
      program.methods.sorted(memberOrdering),
      program.extensions.sorted(memberOrdering),
    )(program.pos, program.info, program.errT)
  }
}
