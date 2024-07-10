// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.transformers
import java.nio.file.Path
import viper.gobra.backend.BackendVerifier
import viper.silicon.Silicon
import viper.silver.ast.utility.FileLoader
import viper.silver.{ast => vpr}
import viper.silver.frontend.{DefaultStates, ViperAstProvider}
import viper.silver.plugin.SilverPlugin
import viper.silver.plugin.standard.predicateinstance.PredicateInstance.PredicateInstanceDomainName
import viper.silver.plugin.standard.termination.{DecreasesTuple, TerminationPlugin}
import viper.silver.reporter.{NoopReporter, Reporter}
import viper.silver.plugin.standard.predicateinstance.PredicateInstancePlugin
import viper.silver.verifier.AbstractError

// This class should be removed in the future because Viper already implements inference of
// imports for termination domains. However, at the moment, Viper performs the inference in
// `beforeVerify`, which is already too late.
class TerminationDomainTransformer extends ViperTransformer {

  override def transform(task: BackendVerifier.Task): Either[Seq[AbstractError], BackendVerifier.Task] = {
    // constructs a separate Viper program (as a string) that should be parsed
    // after parsing this separate Viper program, the resulting AST is combined with `task`

    val declarationImport = "decreases/declaration.vpr"
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

    // find the types of all expressions used as decreases measures
    val measureTypes = task.program.deepCollect {
      case DecreasesTuple(tupleExpressions, _) => tupleExpressions.map(_.typ)
    }
    // does program contain any (possibly empty) decreases tuples?
    val containsTerminationChecks: Boolean = measureTypes.nonEmpty
    val distinctMeasureTypes = measureTypes.flatten.distinct
    // map these types to the respective files that should be imported
    val importsForMeasureTypes = distinctMeasureTypes
      .map(type2Import)
      .distinct
    // we need at least `declarationImport` if there is any tuple decreases measure. However, we do not need to import
    // this file if we already import any other file with the domain for a particular type
    val imports = if (importsForMeasureTypes.nonEmpty) importsForMeasureTypes
    else if (containsTerminationChecks) Seq(declarationImport)
    else Seq.empty
    // if `allImport` is in the list of files that should be imported, we can ignore all others and instead only import
    // `allImport`
    val importsAll = imports.contains(allImport)
    /** list of Viper standard imports that should be parsed */
    val filteredImports = if (importsAll) Seq(allImport) else imports
    val progWithImports = filteredImports.map(p => s"import <${p}>").mkString("\n")
    for {
      vprProgram <- parseVpr(progWithImports)
    } yield combine(task, vprProgram)
  }

  private def parseVpr(program: String): Either[Seq[AbstractError], vpr.Program] = {
    val frontend = new StringViperAstProvider(program)
    frontend.execute()
    if (frontend.errors.isEmpty) {
      Right(frontend.translationResult)
    } else {
      Left(frontend.errors)
    }
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

    override def execute(args: Seq[String] = Seq(), loader: Option[FileLoader] = None): Unit = {
      super.execute(args ++ Seq("--ignoreFile", Silicon.dummyInputFilename), loader)
    }
  }
}
