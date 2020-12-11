package viper.gobra.util

import java.util.concurrent.ExecutorService

import viper.server.core.DefaultVerificationExecutionContext

object DefaultGobraExecutionContext {
  val minimalThreadPoolSize: Int = DefaultVerificationExecutionContext.minNumberOfThreads
}

class DefaultGobraExecutionContext(val threadPoolSize: Int = DefaultGobraExecutionContext.minimalThreadPoolSize) extends DefaultVerificationExecutionContext with GobraExecutionContext {
  lazy val service: ExecutorService = executorService
  override lazy val nThreads: Int = threadPoolSize
}
