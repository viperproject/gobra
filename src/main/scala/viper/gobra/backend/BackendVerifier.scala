/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.backend

import viper.gobra.frontend.Config
import viper.gobra.reporting.BackTranslator
import viper.silver.{ast => vpr}

object BackendVerifier {

  case class Task(
                   program: vpr.Program,
                   errorT: Seq[BackTranslator.ErrorTransformer],
                   reasonT: Seq[BackTranslator.ReasonTransformer]
                 )

  case class Result(

                   )

  def verify(task: Task)(config: Config): Result = {

  }

  private def verifyWithSilicon(program: vpr.Program)(config: Config): Result = {
    var options: Vector[String] = Vector.empty
    options ++= Vector("--logLevel", "ERROR")
    options ++= Vector("--disableCatchingExceptions")
    options ++= Vector("--enableMoreCompleteExhale")

    val silicon = new Silicon(options)
    silicon.start()
    val verificationResult = silicon.handle(program)
    silicon.stop()




  }

}
