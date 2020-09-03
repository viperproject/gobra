package viper.gobra.util

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}


object FutureEither {
  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global
}

case class FutureEither[E, T](x: Future[Either[E, T]]) {

  import FutureEither._

  def map[Q](f: T => Q): FutureEither[E, Q] = FutureEither(x.map(_.map(f)))
  def flatMap[Q](f: T => FutureEither[E, Q]): FutureEither[E, Q] =
    FutureEither(x.flatMap{
      _.map(f) match {
        case Left(data) => Future(Left(data))
        case Right(z) => z.x
      }
    })
}
