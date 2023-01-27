// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator


import viper.gobra.ast.internal.Program
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.reporting.GeneratedViperMessage
import viper.gobra.translator.context.DfltTranslatorConfig
import viper.gobra.translator.encodings.programs.ProgramsImpl
import viper.gobra.translator.transformers.{AssumeTransformer, TerminationTransformer, ViperTransformer}
import viper.gobra.util.Violation
import viper.silver.ast.pretty.FastPrettyPrinter
import viper.silver.{ast => vpr}

object Translator {

  def translate(program: Program, pkgInfo: PackageInfo)(config: Config): BackendVerifier.Task = {
    val translationConfig = new DfltTranslatorConfig()
    val programTranslator = new ProgramsImpl()
    val task = programTranslator.translate(program)(translationConfig)

    val transformers: Seq[ViperTransformer] = Seq(
      new AssumeTransformer,
      new TerminationTransformer
    )

    val transformedTask = transformers.foldLeft(task) {
      case (t, transformer) => transformer.transform(t)
        .fold(errs => Violation.violation(s"Applying transformer ${transformer.getClass.getSimpleName} resulted in errors: ${errs.toString}"), identity)
    }

    if (config.checkConsistency) {
      val errors = transformedTask.program.checkTransitively
      if (errors.nonEmpty) Violation.violation(errors.toString)
    }

    config.reporter report GeneratedViperMessage(config.taskName, config.packageInfoInputMap(pkgInfo).map(_.name), () => sortAst(transformedTask.program), () => transformedTask.backtrack)
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
