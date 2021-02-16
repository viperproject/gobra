// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.util.concurrent.TimeUnit

import org.scalatest.DoNotDiscover
import viper.gobra.reporting.VerifierResult

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Tool for benchmarking Gobra's overall performance (i.e. performance of `Gobra.verify(...)`) (wrapped as a ScalaTest).
  * This tool is heavily inspired by PortableSiliconTests (credits go to Felix and Arshavir).
  * Example run command:
  * ```
  * Z3_EXE=z3.exe
  * sbt "test:runMain
  *   -DGOBRATESTS_TARGET=./target
  *   -DGOBRATESTS_WARMUP=./warmup
  *   -DGOBRATESTS_REPETITIONS=5
  *   -DGOBRATESTS_CSV=data.csv
  *   org.scalatest.tools.Runner
  *   -o -s
  *   viper.gobra.OverallBenchmarkTests"
  * ```
  *
  * The command above will:
  * 1. Warm-up the JVM by verifying all .gobra files in ./warmup
  * 2. Measure time of 5 runs of each .gobra file in ./target
  * 3. Discard ("trim") the slowest and the fastest runs and compute
  *   - the mean
  *   - absolute and relative standard deviation
  *   - the best
  *   - the median
  *   - the worst
  *   run times of all these tests, and
  * 4. Print the timing info (per phase) into STDOUT, and write mean and standard deviation
  *    to file data.csv
  * 5. Create JAR files (e.g., target/scala-2.13/gobra_2.13-0.1.0-SNAPSHOT.jar,
  *                            target/scala-2.13/gobra_2.13-0.1.0-SNAPSHOT-tests.jar)
  *    that can be used to run tests with SBT without the need to distribute/ recompile
  *    the Gobra sources. To run the test without recompiling the sources, these
  *    JAR files should be put into a directory test-location/lib/
  *    where test-location/ is the directory where you invoke SBT via:
  *    ```
  *    sbt "set trapExit := false" \
  *    "test:runMain org.scalatest.tools.Runner -o -s viper.gobra.OverallBenchmarkTests"
  *    ```
  *    Note that this command takes the same JVM property arguments as used above.
  *    Linard (26.1.2021): I was not able to execute the tests using the generated JAR. This might have changed since Scala 2.13.
  *
  * The warmup and the target must be disjoint (not in a sub-directory relation).
  *
  * The following JVM properties are available:
  *   - GOBRATESTS_TARGET = path/to/target/files/    // Mandatory
  *   - GOBRATESTS_WARMUP = path/to/warmup/files/    // Optional. If not specified, skip JVM warmup phase.
  *   - GOBRATESTS_REPETITIONS = n // Optional, defaults to 1. If less than 4, no "trimming" will happen.
  *   - GOBRATESTS_CSV = path/to/file.csv // Optional. If provided, mean & stddev are written to CSV file.
  *   - GOBRATESTS_TIMEOUT = 180 // timeout in seconds, by default 180s
  */
@DoNotDiscover
class OverallBenchmarkTests extends BenchmarkTests {

  override def name = "Gobra-Overall Benchmarks"

  // reuse the same frontend for all test files
  override val gobraFrontend: GobraFrontendForTesting = new OverallGobraFrontend()


  class OverallGobraFrontend extends GobraFrontendForTesting {
    private val gobra: Gobra = new Gobra

    private var overallPhaseResult: Option[VerifierResult] = None
    private val overallPhase = Phase("Gobra.verify", () => {
      assert(config.isDefined)
      val c = config.get
      val resultFuture = gobra.verify(c.inputFiles, c)(executor)
      overallPhaseResult = Some(Await.result(resultFuture, Duration(timeoutSec, TimeUnit.SECONDS)))
    })

    /** Phases of the frontend which are executed sequentially. */
    override val phases: Seq[Phase] = Seq(overallPhase)

    /**
      * Reset any messages recorded internally (errors from previous program translations, etc.)
      */
    override def resetMessages(): Unit = {
      overallPhaseResult = None
    }

    override def gobraResult: VerifierResult = {
      assert(overallPhaseResult.isDefined)
      overallPhaseResult.get
    }
  }
}
