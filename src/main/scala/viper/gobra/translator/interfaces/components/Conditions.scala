// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.interfaces.components

import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.gobra.reporting.{Source, VerificationError}
import viper.gobra.translator.interfaces.translator.Generator
import viper.silver.verifier.ErrorReason
import viper.silver.{ast => vpr}

trait Conditions extends Generator {
  /** Returns true, but asserts that the argument holds. */
  def assert(x: vpr.Exp): vpr.Exp
  def assert(x: vpr.Exp, reasonT: (Source.Verifier.Info, ErrorReason) => VerificationError): (vpr.Exp, ErrorTransformer)
  /** Returns 'exp', but asserts that 'cond' holds. */
  def assert(cond: vpr.Exp, exp: vpr.Exp, reasonT: (Source.Verifier.Info, ErrorReason) => VerificationError): (vpr.Exp, ErrorTransformer)
}
