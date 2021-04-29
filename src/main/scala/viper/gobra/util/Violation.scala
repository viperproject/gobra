// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util

import viper.gobra.reporting.VerifierError

object Violation {

  abstract class GobraException(msg: String) extends RuntimeException(msg)

  class KnownZ3BugException(msg: String) extends GobraException(msg)

  class LogicException(msg: String) extends GobraException(msg)

  class UglyErrorMessage(val error: VerifierError) extends GobraException(error.message)

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(cond: Boolean, msg: => String): Unit = if (!cond) violation(msg)

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(msg: String): Nothing = throw new LogicException(s"Logic error: $msg")

  @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
  @inline
  def violation(cond: Boolean, msg: => String, code: => Unit): Unit = if (!cond) { code; violation(msg) }
}