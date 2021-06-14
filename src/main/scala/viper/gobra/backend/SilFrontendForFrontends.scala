// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.backend

import java.nio.file.Path

import viper.silicon
import viper.silver.ast.{Node, Program}
import viper.silver.frontend.{DefaultStates, SilFrontend}
import viper.silver.verifier.ConsistencyError

/**
  * This trait adapts a SilFrontend to Viper frontends that encode a program into a Viper program and thus do not need
  * and phases related to parsing. Key to use this trait is setting `encoding` to the Viper program that the frontend
  * has produced.
  * `execute` should be used to run the phases.
  */
trait SilFrontendForFrontends extends SilFrontend {
  val encoding: Program

  val performConsistencyChecks: Boolean

  /** the state to which SilFrontend should be initialized. This state has to be the immediate predecessor of the first
    * phase as this will be checked in the functions that execute a particular phase (see `DefaultFrontend` trait).
    *
    * E.g. if the first phase is ConsistencyCheck, `consistencyCheck()` mandates that the state
    * is `DefaultStates.Translation`.
    */
  private val initialState = DefaultStates.Translation
  override val phases = Seq(ConsistencyCheck, Verification)

  // initialize the frontend:
  setState(initialState)

  override def reset(input: Path): Unit = {
    // super cannot be invoked as `input` does not exist and the super implementation tries to read from `input`

    // the following implementation has been copied from `super.reset(input)` with the following modifications:
    // - `_state` is initialized to the special `initialState` which does not correspond to the initial state that is
    //    usually used.
    // - `_input` is not set to the file content of `input` but to an empty string (see comment below).
    // - `_program` is set to `encoding` as we skip all phases that would read, parse, and create a program and instead
    //    provide the Viper program as input.

    if (state < DefaultStates.Initialized) sys.error("The translator has not been initialized.")

    _inputFile = Some(input)
    _errors = Seq()
    _parsingResult = None
    _semanticAnalysisResult = None
    _verificationResult = None

    // as consistency checking is first phase, we have to set the initial state accordingly:
    _state = initialState
    // we use the empty string to make parse phase happy. However, `doParsing` will simply ignore the provided string
    _input = Some("")
    // `encoding` provides the Viper program that should be used
    // in case consistency checks should be skipped, we convert it to a program without consistency checks:
    val modifiedEncoding = if (performConsistencyChecks) {
      encoding
    } else {
      new Program(encoding.domains, encoding.fields, encoding.functions, encoding.predicates,
        encoding.methods, encoding.extensions)(encoding.pos, encoding.info) with NodeWithoutConsistency
    }
    _program = Some(modifiedEncoding)

    resetMessages()
  }

  /**
    * Executes all phases.
    * @param args arguments that are used to create the verifier. Note that no (fake) filename should be provided as
    *         this function already takes care of that (including adding `--ignoreFile`)
    */
  override def execute(args: Seq[String]): Unit = {
    super.execute(args ++ Seq("--ignoreFile", silicon.Silicon.dummyInputFilename))
  }
}

trait NodeWithoutConsistency extends Node {
  // do not perform any consistency checks:
  override lazy val checkTransitively: Seq[ConsistencyError] = Seq()
}
