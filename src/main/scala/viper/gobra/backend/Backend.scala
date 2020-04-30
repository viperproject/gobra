package viper.gobra.backend

import scala.concurrent.Future
import viper.silver

trait Backend[I, O] {
  def verify(reporter: silver.reporter.Reporter, config: ViperBackendConfig, input: I): Future[O]
  /*
  def start(): Unit
  def handle(input: I): Future[O]
  def stop(): Unit
  */
}
