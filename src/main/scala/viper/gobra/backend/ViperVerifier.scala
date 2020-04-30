package viper.gobra.backend

import viper.silver
import viper.silver.reporter.{Reporter, StdIOReporter}
import scala.concurrent.Future

trait ViperVerifier extends Backend[silver.ast.Program, silver.verifier.VerificationResult] {

  def verify(reporter: Reporter, config: ViperBackendConfig, program: silver.ast.Program): Future[silver.verifier.VerificationResult]
/*
  def start(reporter: Reporter): Unit

  override def start(): Unit = start(StdIOReporter())

  /** Alias for [[handle]] */
  def verify(program: silver.ast.Program): Future[silver.verifier.VerificationResult] = handle(program)
*/
}
