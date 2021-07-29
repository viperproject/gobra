// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util

import java.util.concurrent.{ExecutorService, Executors, ThreadFactory, TimeUnit}
import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

trait GobraExecutionContext extends ExecutionContext {
  /** terminate executor */
  def terminate(timeoutMSec: Long = 1000): Unit
  /**
    * In contrast to `terminate`, this function terminates the context but also checks whether it was successfully
    * shutdown meaning that no timeout has occurred while doing so.
    */
  def terminateAndAssertInexistanceOfTimeout(): Unit
}

object DefaultGobraExecutionContext {
  val minimalThreadPoolSize: Int = 1
}

class DefaultGobraExecutionContext(val threadPoolSize: Int = Math.max(DefaultGobraExecutionContext.minimalThreadPoolSize, Runtime.getRuntime.availableProcessors()),
                                   threadNamePrefix: String = "thread") extends GobraExecutionContext {
  // this is quite redundant to the code in ViperServer but since Gobra should not have a dependency on ViperServer,
  // there is no other way than to duplicate the code
  protected lazy val threadStackSize: Long = 128L * 1024L * 1024L // 128M seems to consistently be recommended by Silicon and Carbon
  protected lazy val service: ExecutorService = Executors.newFixedThreadPool(
    threadPoolSize, new ThreadFactory() {

      import java.util.concurrent.atomic.AtomicInteger

      private val mCount = new AtomicInteger(1)
      override def newThread(runnable: Runnable): Thread = {
        val threadName = s"$threadNamePrefix-${mCount.getAndIncrement()}"
        new Thread(null, runnable, threadName, threadStackSize)
      }
    })

  private lazy val context: ExecutionContextExecutorService = ExecutionContext.fromExecutorService(service)

  override def execute(runnable: Runnable): Unit = context.execute(runnable)

  override def reportFailure(cause: Throwable): Unit = context.reportFailure(cause)

  @throws(classOf[InterruptedException])
  override def terminate(timeoutMSec: Long = 1000): Unit = {
    context.shutdown()
    context.awaitTermination(timeoutMSec, TimeUnit.MILLISECONDS)
  }

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
