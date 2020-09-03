package viper.gobra.backend

import scala.concurrent.Future

trait Backend[I, C, R, P, O] {
  def verify(id: I, config: C, reporter: R, program: P): Future[O]
}
