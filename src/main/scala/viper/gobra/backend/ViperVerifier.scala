package viper.gobra.backend

import viper.silver

trait ViperVerifier extends Backend[silver.ast.Program, silver.verifier.VerificationResult] {

  /** Alias for [[handle]] */
  def verify(program: silver.ast.Program): silver.verifier.VerificationResult = handle(program)
}
