// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util

import viper.server.core.{DefaultVerificationExecutionContext, VerificationExecutionContext}

trait GobraExecutionContext extends VerificationExecutionContext {
  /**
    * In contrast to `terminate`, this function terminates the context but also checks whether it was successfully
    * shutdown meaning that no timeout has occurred while doing so.
    */
  def terminateAndAssertInexistanceOfTimeout(): Unit
}

class DefaultGobraExecutionContext extends DefaultVerificationExecutionContext with GobraExecutionContext {
  /**
    * In contrast to `terminate`, this function terminates the context but also checks whether it was successfully
    * shutdown meaning that no timeout has occurred while doing so.
    */
  @throws(classOf[InterruptedException])
  override def terminateAndAssertInexistanceOfTimeout(): Unit = {
    val timeoutMs = 1000 // 1 sec
    val startTime = System.currentTimeMillis()
    // terminate executor with a larger timeout such that we can distinguish a timeout from terminate taking quite long
    terminate(10 * timeoutMs)
    val terminateDurationMs = System.currentTimeMillis() - startTime
    // check whether timeout has been exceeded and cause an assertion failure:
    assert(terminateDurationMs < timeoutMs)
  }
}
