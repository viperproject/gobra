// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.util

import java.nio.file.Path

import viper.gobra.util.Violation
import viper.silicon.Silicon
import viper.silver.{ast => vpr}
import viper.silver.frontend.{DefaultStates, ViperAstProvider}
import viper.silver.reporter.{NoopReporter, Reporter}

object ViperParser {

  def parseProgram(program: String): vpr.Program = {
    val frontend = new StringViperAstProvider(program)
    frontend.execute()
    if (frontend.errors.nonEmpty) {
      Violation.violation(s"errors while parsing Viper program ${program}: ${frontend.errors}")
    }
    frontend.translationResult
  }

  def parseDomains(domain: String): Vector[vpr.Domain] = {
    val members = parseMembers(domain)
    val domains = members.collect{ case d: vpr.Domain => d }

    if (domains.size != members.size) {
      Violation.violation(s"expected domain, but got $members.")
    }

    domains
  }

  def parseMembers(program: String): Vector[vpr.Member] = {
    val frontend = new StringViperAstProvider(program)
    frontend.execute()
    if (frontend.errors.nonEmpty) {
      Violation.violation(s"errors while parsing Viper program ${program}: ${frontend.errors}")
    }
    val parsedProgram = frontend.translationResult
    parsedProgram.members.toVector
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
