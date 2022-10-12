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
    // we report the un-transformed program as this is much more readable
    config.reporter report GeneratedViperMessage(config.taskName, config.packageInfoInputMap(pkgInfo).map(_.name), () => sortAst(task.program), () => task.backtrack)

    if (config.checkConsistency) {
      val errors = task.program.checkTransitively
      if (errors.nonEmpty) Violation.violation(errors.toString)
    }

    val transformers: Seq[ViperTransformer] = Seq(
      new AssumeTransformer,
      new TerminationTransformer
    )

    transformers.foldLeft(task) {
      case (t, transformer) => transformer.transform(t)
    }
  }

  def sortAst(program: vpr.Program): vpr.Program = {
    implicit def domainOrdering: Ordering[vpr.Domain] = Ordering.by(_.name)
    implicit def domainFnOrdering: Ordering[vpr.DomainFunc] = Ordering.by(_.name)
    implicit def domainAxOrdering: Ordering[vpr.DomainAxiom] = Ordering.by(FastPrettyPrinter.pretty(_))
    implicit def fieldOrdering: Ordering[vpr.Field] = Ordering.by(_.name)
    implicit def functionOrdering: Ordering[vpr.Function] = Ordering.by(_.name)
    implicit def predicateOrdering: Ordering[vpr.Predicate] = Ordering.by(_.name)
    implicit def methodOrdering: Ordering[vpr.Method] = Ordering.by(_.name)
    implicit def extensionOrdering: Ordering[vpr.ExtensionMember] = Ordering.by(_.name)

    def sortDomain(domain: vpr.Domain): vpr.Domain = {
      vpr.Domain(
        domain.name,
        domain.functions.sorted,
        domain.axioms.sorted,
        domain.typVars
      )(domain.pos, domain.info, domain.errT)
    }

    vpr.Program(
      program.domains.map(sortDomain).sorted,
      program.fields.sorted,
      program.functions.sorted,
      program.predicates.sorted,
      program.methods.sorted,
      program.extensions.sorted,
    )(program.pos, program.info, program.errT)
  }
}
