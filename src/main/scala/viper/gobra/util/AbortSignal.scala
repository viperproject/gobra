// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.util

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

/**
  * Signal to cooperatively abort a running verification: the verification checks the signal at
  * certain points (e.g. between compilation stages) and registers listeners to interrupt
  * long-running backend work. Aborting is idempotent and listeners registered after aborting are
  * invoked immediately.
  */
class AbortSignal {
  private val aborted = new AtomicBoolean(false)
  private val listeners = new ConcurrentLinkedQueue[() => Unit]()

  def isAborted: Boolean = aborted.get()

  def abort(): Unit = {
    aborted.set(true)
    drainListeners()
  }

  /** invokes `listener` as soon as this signal is aborted (immediately if it already is) */
  def onAbort(listener: () => Unit): Unit = {
    listeners.add(listener)
    if (isAborted) {
      drainListeners()
    }
  }

  private def drainListeners(): Unit = {
    var listener = listeners.poll()
    while (listener != null) {
      listener()
      listener = listeners.poll()
    }
  }
}

/** internal marker with which verification futures fail when the verification has been aborted */
class AbortedException extends RuntimeException("the verification has been aborted")
