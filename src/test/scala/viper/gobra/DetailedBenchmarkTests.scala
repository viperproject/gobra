// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra

import java.nio.file.Path
import org.scalatest.DoNotDiscover
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.ast.internal.Program
import viper.gobra.ast.internal.transform.OverflowChecksTransform
import viper.gobra.backend.BackendVerifier
import viper.gobra.frontend.PackageResolver.{AbstractPackage, RegularPackage}
import viper.gobra.frontend.Parser.ParseResult
import viper.gobra.frontend.info.{Info, TypeInfo}
import viper.gobra.frontend.{Desugar, Parser}
import viper.gobra.reporting.{AppliedInternalTransformsMessage, BackTranslator, VerifierError, VerifierResult}
import viper.gobra.translator.Translator

import scala.concurrent.Future

/**
  * Tool for benchmarking Gobra's performance split into its individual steps (wrapped as a ScalaTest).
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
  *   viper.gobra.DetailedBenchmarkTests"
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
  * 5. Create JAR files by running `sbt test:assembly`. This creates the JAR `target/scala-2.13/gobra-test.jar`
  *    that can be used to run tests with SBT without the need to distribute/ recompile
  *    the Gobra sources. To run the test without recompiling the sources, these
  *    JAR files should be put into a directory test-location/lib/
  *    where test-location/ is the directory where you invoke SBT via:
  *    ```
  *    sbt "set trapExit := false" \
  *    "test:runMain org.scalatest.tools.Runner -o -s viper.gobra.DetailedBenchmarkTests"
  *    ```
  *    Note that this command takes the same JVM property arguments as used above.
  *    Linard (26.1.2021): I was not able to execute the tests using SBT. This might have changed since Scala 2.13.
  *                       `-R target/scala-2.13/test-classes` seems to be missing in the command above
  *                       However, the following command can be used to execute the benchmarks without SBT:
  *                       ```
  *                       java -Xss128m -cp target/scala-2.13/gobra-test.jar -Dlogback.configurationFile=conf/logback.xml -DGOBRATESTS_TARGET=<path to directory> org.scalatest.run viper.gobra.DetailedBenchmarkTests
  *                       ```
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
class DetailedBenchmarkTests extends BenchmarkTests {

  override def name = "Gobra-Detailed Benchmarks"

  // reuse the same frontend for all test files
  override val gobraFrontend: GobraFrontendForTesting = new DetailedGobraFrontend()


  class DetailedGobraFrontend extends GobraFrontendForTesting {
    val gobra: Gobra = new Gobra // we only need the instance for merging in-file configs

    override def reset(files: Seq[Path]): Unit = {
      // call to super creates a "base" config that stores the files
      super.reset(files)
      // however, as we will directly invoke the individual steps of Gobra, we have to manually merge in-file configs
      // such that the Gobra programs show the same behavior as when invoking `Gobra.verify`:
      assert(config.isDefined)
      val c = config.get
      assert(c.packageInfoInputMap.size == 1)
      val pkgInfo = c.packageInfoInputMap.keys.head
      config = gobra.getAndMergeInFileConfig(c, pkgInfo).toOption
    }

    private val parsing = InitialStepEitherT("parsing", () => {
      assert(config.isDefined)
      val c = config.get
      assert(c.packageInfoInputMap.size == 1)
      val pkgInfo = c.packageInfoInputMap.keys.head
      Parser.parse(c, pkgInfo)(executor)
    })

    private val typeChecking = NextStepEitherT("type-checking", parsing, (parseResults: Map[AbstractPackage, ParseResult]) => {
        assert(config.isDefined)
        val c = config.get
        assert(c.packageInfoInputMap.size == 1)
        val pkgInfo = c.packageInfoInputMap.keys.head
        Info.check(c, RegularPackage(pkgInfo.id), parseResults)(executor)
      })

    private val desugaring: NextStep[TypeInfo, Vector[VerifierError], Program] =
      NextStep("desugaring", typeChecking, { case (typeInfo: TypeInfo) =>
        assert(config.isDefined)
        val c = config.get
        Right(Desugar.desugar(c, typeInfo)(executor))
      })

    private val internalTransforming = NextStep("internal transforming", desugaring, (program: Program) => {
      assert(config.isDefined)
      val c = config.get
      assert(c.packageInfoInputMap.size == 1)
      val pkgInfo = c.packageInfoInputMap.keys.head
      if (c.checkOverflowsOrDefault) {
        val result = OverflowChecksTransform.transform(program)
        c.reporter report AppliedInternalTransformsMessage(c.packageInfoInputMap(pkgInfo).map(_.name), () => result)
        Right(result)
      } else {
        Right(program)
      }
    })

    private val encoding = NextStep("Viper encoding", internalTransforming, (program: Program) => {
      assert(config.isDefined)
      val c = config.get
      assert(c.packageInfoInputMap.size == 1)
      val pkgInfo = c.packageInfoInputMap.keys.head
      Translator.translate(program, pkgInfo)(c)
    })

    private val verifying = NextStepEitherT("Viper verification", encoding, (viperTask: BackendVerifier.Task) => {
      assert(config.isDefined)
      val c = config.get
      assert(c.packageInfoInputMap.size == 1)
      val pkgInfo = c.packageInfoInputMap.keys.head
      val resultFuture: Future[Either[Vector[VerifierError], VerifierResult]] = BackendVerifier.verify(viperTask, pkgInfo)(c)
        .map(res => Right(BackTranslator.backTranslate(res)(c)))
      EitherT.fromEither(resultFuture)
    })

    private val lastStep = verifying

    /** Phases of the frontend which are executed sequentially. */
    override val phases: Seq[Phase] = lastStep.phases

    /**
     * Reset any messages recorded internally (errors from previous program translations, etc.)
     */
    override def resetMessages(): Unit = {
      verifying.reset()
    }

    override def gobraResult: VerifierResult = lastStep.res match {
      case Some(Left(Vector())) => VerifierResult.Success
      case Some(Left(errors))   => VerifierResult.Failure(errors)
      case Some(Right(result))  => result
    }
  }
}
