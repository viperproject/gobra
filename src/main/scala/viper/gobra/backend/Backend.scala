package viper.gobra.backend

import scala.concurrent.Future
import viper.silver

trait Backend[I, C, R, P, O] {
  def verify(id: I, config: C, reporter: R, program: P): Future[O]
  /*
  def start(): Unit
  def handle(input: I): Future[O]
  def stop(): Unit
  */
}
