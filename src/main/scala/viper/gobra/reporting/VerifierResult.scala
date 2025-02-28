// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.



package viper.gobra.reporting

sealed trait VerifierResult

object VerifierResult {
  case class Success(warnings: Vector[VerifierWarning]) extends VerifierResult
  case class Failure(errors: Vector[VerifierError], warnings: Vector[VerifierWarning]) extends VerifierResult
}
