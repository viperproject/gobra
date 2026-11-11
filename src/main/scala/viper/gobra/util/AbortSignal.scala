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
  *
  * Thread safety: every listener is invoked exactly once -- concurrent `abort` and `onAbort`
  * calls may drain the listener queue concurrently, but `ConcurrentLinkedQueue.poll` hands each
  * listener to exactly one of the draining threads, and the ordering guarantees of the atomic
  * `aborted` flag ensure that a listener registered concurrently with `abort` is drained by the
  * registering thread if the aborting thread's drain missed it. Consequently, listeners may be
  * invoked on either the aborting or the registering thread, and different listeners may execute
  * concurrently -- listeners must be thread-safe and should not block.
  */
class AbortSignal {
  private val aborted = new AtomicBoolean(false)
  private val listeners = new ConcurrentLinkedQueue[() => Unit]()

  def isAborted: Boolean = aborted.get()

  def abort(): Unit = {
    aborted.set(true)
    notifyAndDeregisterListeners()
  }

  /** invokes `listener` as soon as this signal is aborted (immediately if it already is) */
  def onAbort(listener: () => Unit): Unit = {
    listeners.add(listener)
    if (isAborted) {
      notifyAndDeregisterListeners()
    }
  }

  // processes the queue of listeners in a thread-safe way guaranteeing
  // that each listener is notified only once
  private def notifyAndDeregisterListeners(): Unit = {
    var listener = listeners.poll()
    while (listener != null) {
      try {
        listener()
      } catch {
        // a throwing listener must neither escape into the (unrelated) caller that happens to
        // drain it nor prevent the remaining listeners from being invoked:
        case e: Exception => e.printStackTrace()
      }
      listener = listeners.poll()
    }
  }
}

/** internal marker with which verification futures fail when the verification has been aborted */
class AbortedException extends RuntimeException("the verification has been aborted")
