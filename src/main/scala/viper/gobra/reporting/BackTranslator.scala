// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.reporting
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.TypeInfo
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.{Config,Parser}
import viper.silver.{ast => vpr}
import viper.silver
import java.nio.file.{Path,Paths}

import scala.annotation.unused

object BackTranslator {

  trait ErrorBackTranslator {
    def translate(error: silver.verifier.VerificationError): VerificationError
    def translate(reason: silver.verifier.ErrorReason): VerificationErrorReason
  }
 case class VerificationBackTrackInfo(
                            errorT: Seq[BackTranslator.ErrorTransformer],
                            reasonT: Seq[BackTranslator.ReasonTransformer]
                          )
  case class BackTrackInfo(
                            errorT: Seq[BackTranslator.ErrorTransformer],
                            reasonT: Seq[BackTranslator.ReasonTransformer],
                            viperprogram:vpr.Program,
                            typeInfo:TypeInfo,
                            config:Config
                          )

  type ErrorTransformer = PartialFunction[silver.verifier.VerificationError, VerificationError]
  type ReasonTransformer = PartialFunction[silver.verifier.ErrorReason, VerificationErrorReason]

  def backTranslate(result: BackendVerifier.Result)(/* @unused */ config: Config): VerifierResult = 
  result match {
    case BackendVerifier.Success => VerifierResult.Success
    case BackendVerifier.Failure(errors, backtrack) => 
      val errorTranslator =  new DefaultErrorBackTranslator(backtrack.copy(config=config)) //TODO make this more clean e.g. remove config from backtrack Info
      VerifierResult.Failure(errors map  errorTranslator.translate)
  }

  implicit class RichErrorMessage(error: silver.verifier.ErrorMessage) {
    def causedBy(node: vpr.Node with vpr.Positioned): Boolean =
      node == error.offendingNode && node.pos == error.offendingNode.pos
  }
}
