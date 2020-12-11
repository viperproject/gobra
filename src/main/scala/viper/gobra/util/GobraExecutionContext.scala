package viper.gobra.util

import java.util.concurrent.ExecutorService

import viper.server.core.VerificationExecutionContext

trait GobraExecutionContext extends VerificationExecutionContext {
  def service: ExecutorService
}
