// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.transformers
import java.nio.file.Path
import viper.gobra.backend.BackendVerifier
import viper.gobra.util.Violation
import viper.silicon.Silicon
import viper.silver.{ast => vpr}
import viper.silver.frontend.{DefaultStates, ViperAstProvider}
import viper.silver.plugin.standard.predicateinstance.PredicateInstance.PredicateInstanceDomainName
import viper.silver.plugin.standard.termination.{DecreasesTuple, TerminationPlugin}
import viper.silver.reporter.{NoopReporter, Reporter}
import viper.silver.plugin.standard.predicateinstance.PredicateInstancePlugin

class TerminationTransformer extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
    (addDecreasesDomains _ andThen executeTerminationPlugin)(task)
  }

  private def addDecreasesDomains(task: BackendVerifier.Task): BackendVerifier.Task = {
    // constructs a separate Viper program (as a string) that should be parsed
    // after parsing this separate Viper program, the resulting AST is combined with `task`

    val allImport = "decreases/all.vpr"
    def type2Import(typ: vpr.Type): String = typ match {
      case vpr.Bool => "decreases/bool.vpr"
      case vpr.Int => "decreases/int.vpr"
      case vpr.MultisetType(_) => "decreases/multiset.vpr"
      case vpr.Perm => "decreases/rational.vpr"
      case vpr.Ref => "decreases/ref.vpr"
      case vpr.SeqType(_) => "decreases/seq.vpr"
      case vpr.SetType(_) => "decreases/set.vpr"
      case vpr.DomainType(name, _) if name == PredicateInstanceDomainName => "decreases/predicate_instance.vpr"
      case _ => allImport // fallback
    }

    // find the types of all expressions used as decreases measues
    val measureTypes = task.program.deepCollect {
      case DecreasesTuple(tupleExpressions, _) => tupleExpressions.map(_.typ)
    }.flatten.distinct
    // map these types to the respective files that should be imported
    val imports = measureTypes
      .map(type2Import)
      .distinct
    // if `allImport` is in the list of files that should be imported, we can ignore all others and instead only import
    // `allImport`
    val importsAll = imports.contains(allImport)
    /** list of Viper standard imports that should be parsed */
    val filteredImports = if (importsAll) Seq(allImport) else imports
    val progWithImports = filteredImports.map(p => s"import <${p}>").mkString("\n")
    val vprProgram = parseVpr(progWithImports)
    combine(task, vprProgram)
  }

  private def parseVpr(program: String): vpr.Program = {
    val frontend = new StringViperAstProvider(program)
    frontend.execute()
    if (frontend.errors.nonEmpty) {
      Violation.violation(s"errors while parsing Viper program ${program}: ${frontend.errors}")
    }
    frontend.translationResult
  }

  private def combine(task: BackendVerifier.Task, other: vpr.Program): BackendVerifier.Task = {
    val prog = task.program
    val newProg = vpr.Program(
      prog.domains ++ other.domains,
      prog.fields ++ other.fields,
      prog.functions ++ other.functions,
      prog.predicates ++ other.predicates,
      prog.methods ++ other.methods,
      prog.extensions ++ other.extensions,
    )(prog.pos, prog.info, prog.errT)
    task.copy(program = newProg)
  }

  private def executeTerminationPlugin(task: BackendVerifier.Task): BackendVerifier.Task = {
    val plugin = new TerminationPlugin(null, null, null, null)
    val predInstancePlugin = new PredicateInstancePlugin(null, null, null, null)
    val transformedProgram = plugin.beforeVerify(task.program)
    val programWithoutPredicateInstances = predInstancePlugin.beforeVerify(transformedProgram)
    task.copy(program = programWithoutPredicateInstances)
  }

  /**
    * Viper AST provider taking an in-memory Viper program (as a string) as input instead of reading it from a file.
    */
  class StringViperAstProvider(program: String, reporter: Reporter = NoopReporter) extends ViperAstProvider(reporter) {
    /**
      * overrides `reset` to change the behavior of `_input`: instead of reading from the file stored at `input`, it
      * directly uses `program`, the constructor argument.
      */
    override def reset(input: Path): Unit = {
      if (state < DefaultStates.Initialized) sys.error("The translator has not been initialized.")
      _state = DefaultStates.InputSet
      _inputFile = Some(input)
      _input = Some(program)
      _errors = Seq()
      _parsingResult = None
      _semanticAnalysisResult = None
      _verificationResult = None
      _program = None
      resetMessages()
    }

    override def execute(args: Seq[String] = Seq()): Unit = {
      super.execute(args ++ Seq("--ignoreFile", Silicon.dummyInputFilename))
    }
  }
}

