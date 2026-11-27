// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.hyperfuncs

import viper.silver.ast._
import viper.silver.plugin.sif.SIFLowExp
import viper.silver.verifier.AbstractError

import scala.annotation.unused

/**
  * A product construction, as seen by the encoding of hyperfunctions ([[HyperfuncEncoding]]): the primitives from which
  * the encoding defines `rel`, `active`, `low`, lifting, and hyperfunction definitions and calls. Executions are
  * numbered `0 … executions - 1`.
  *
  * An *activation* is a sequence of `executions` boolean expressions: the condition under which each execution is
  * active at the current program point (in a method), or the activation flags of a hyperfunction (in its definition).
  * A construction in which every execution is always active passes `TrueLit`s.
  */
trait ProductConstruction {

  /** The number of executions related by the product. */
  def executions: Int

  /**
    * The unary expression `e` in execution `i`: local variables, fields, predicates and heap-dependent functions are
    * replaced by their copies for execution `i`. The hyperfunction constructs in `e` are translated with the
    * activation `act` (through [[HyperfuncEncoding.translateOwn]]).
    */
  def inExecution(e: Exp, i: Int, act: Seq[Exp]): Exp

  /**
    * Does `e` contain relational constructs (including instances of relational predicates and the hyperfunction
    * constructs)?
    */
  def isRelational(e: Exp): Boolean

  /** `low(e)` restricted to executions `i < j`: their values of `e` agree. */
  def lowComparison(l: SIFLowExp, i: Int, j: Int, act: Seq[Exp]): Exp

  /**
    * Runs `body` in a scope in which the parameters `params` of a hyperfunction have one copy per execution:
    * `body` receives, for each execution, the declarations of the copies (execution 0 may use the original
    * parameters), and within it, [[inExecution]] maps each parameter to its copy.
    */
  def withParameterCopies[T](params: Seq[LocalVarDecl])(body: Seq[Seq[LocalVarDecl]] => T): T

  /**
    * Whether every execution is always active (the lockstep construction). Then hyperfunctions take no activation
    * flags, and the encoding drops the trivial conditions.
    */
  def activationIsTrivial: Boolean = false

  /**
    * Runs `body` in a scope in which the variables `names` are rigid (one value for all executions, e.g., execution
    * parameters and variables bound by quantifiers in relational assertions): [[inExecution]] leaves them unchanged.
    */
  def withRigidVariables[T](@unused names: Seq[String])(body: => T): T = body

  /** A name that is not used in the program. */
  def freshName(base: String): String

  def reportError(error: AbstractError): Unit
}
