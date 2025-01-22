// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2025 ETH Zurich.

package viper.gobra.ast.internal.transform

import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.ast.internal.Program
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.reporting.AppliedInternalTransformsMessage
import viper.gobra.util.{GobraExecutionContext, VerifierPhaseNonFinal}
import viper.gobra.util.VerifierPhase.PhaseResult

object Transformations extends VerifierPhaseNonFinal[(Config, PackageInfo, Program), Program] {
  override val name: String = "Internal transformations"

  /**
    * Applies transformations to programs in the internal language. Currently, only adds overflow checks but it can
    * be easily extended to perform more transformations
    */
  override protected def execute(input: (Config, PackageInfo, Program))(implicit executor: GobraExecutionContext): PhaseResult[Program] = {
    val (config, pkgInfo, program) = input
    // constant propagation does not cause duplication of verification errors caused
    // by overflow checks (if enabled) because all overflows in constant declarations
    // can be found by the well-formedness checks.
    var transformations: Vector[InternalTransform] = Vector(CGEdgesTerminationTransform, ConstantPropagation)
    if (config.checkOverflows) {
      transformations :+= OverflowChecksTransform
    }
    val result = transformations.foldLeft(program)((prog, transf) => transf.transform(prog))
    config.reporter.report(AppliedInternalTransformsMessage(config.packageInfoInputMap(pkgInfo).map(_.name), () => result))
    EitherT.right(result, Vector.empty)
  }
}
