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
 import viper.silver.plugin.standard.termination.TerminationPlugin
 import viper.silver.reporter.{NoopReporter, Reporter}

 class TerminationTransformer extends ViperTransformer {

   override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
     (addDecreasesDomains _ andThen executeTerminationPlugin)(task)
   }

   private def addDecreasesDomains(task: BackendVerifier.Task): BackendVerifier.Task = {
     // constructs a separate Viper program (as a string) that should be parsed
     // after parsing this separate Viper program, the resulting AST is combined with `task`

     /** list of Viper standard imports that should be parsed */
     val imports = Seq("decreases/all.vpr")
     val progWithImports = imports.map(p => s"import <${p}>").mkString("\n")
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
     val plugin = new TerminationPlugin(null, null, null)
     val transformedProgram = plugin.beforeVerify(task.program)
     task.copy(program = transformedProgram)
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
