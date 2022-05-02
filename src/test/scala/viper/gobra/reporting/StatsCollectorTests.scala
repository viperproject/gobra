// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.
package viper.gobra.reporting

import org.rogach.scallop.throwError
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.{Config, PackageInfo, ScallopGobraConfig}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext, Violation}
import viper.gobra.Gobra

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class StatsCollectorTests extends AnyFunSuite with BeforeAndAfterAll {
  val statsCollectorTestDir: String = "src/test/resources/stats_collector"
  var executor: GobraExecutionContext = _
  var gobraInstance: Gobra = _

  override def beforeAll(): Unit = {
    executor = new DefaultGobraExecutionContext()
    gobraInstance = new Gobra()
  }

  override def afterAll(): Unit = {
    executor.terminateAndAssertInexistanceOfTimeout()
  }

  test("Integration without chopper") {
    val config = createConfig(Array("--recursive", "--projectRoot", statsCollectorTestDir, "-I", statsCollectorTestDir))
    runIntegration(config)
  }

  test("Integration with chopper") {
    val config = createConfig(Array("--recursive", "--projectRoot", statsCollectorTestDir, "-I", statsCollectorTestDir, "--chop", "10"))
    runPackagesSeparately(config)
    runIntegration(config)
  }

  private def createConfig(args: Array[String]): Config = {
    // set throwError to true: Scallop will throw an exception instead of terminating the program in case an
    // exception occurs (e.g. a validation failure)
    throwError.value = true
    // Simulate pick of package, Gobra normally does
    val config = new ScallopGobraConfig(args.toSeq).config
    Violation.violation(config.isRight, "creating the config has failed")
    config.toOption.get
  }

  private def runPackagesSeparately(config: Config): Unit = {
    // Overwrite reporter
    config.packageInfoInputMap.keys.foreach(pkgInfo => {
      val statsCollector = StatsCollector(NoopReporter)
      val result = runAndCheck(config.copy(reporter = statsCollector, taskName = pkgInfo.id), statsCollector, pkgInfo)

      // Assert that errors are somehow reflected in the stats
      // It's hard to test this further, since there isn't much information about viper or gobra members available
      // inside of the VerifierError class
      result match {
        case VerifierResult.Success => assert(statsCollector.memberMap.values.flatMap(_.viperMembers.values).forall(_.success))
        case VerifierResult.Failure(_) => assert(statsCollector.memberMap.values.flatMap(_.viperMembers.values).exists(!_.success))
      }
    })
  }

  private def runIntegration(config: Config): Unit = {
    // Overwrite reporter
    var errorCount = 0
    val statsCollector = StatsCollector(NoopReporter)
    config.packageInfoInputMap.keys.foreach(pkgInfo => {
      val results = runAndCheck(config.copy(reporter = statsCollector, taskName = pkgInfo.id), statsCollector, pkgInfo)

      results match {
        case VerifierResult.Success => assert(statsCollector.memberMap.values.flatMap(_.viperMembers.values).count(!_.success) == errorCount)
        case VerifierResult.Failure(_) =>
          val newErrors = statsCollector.memberMap.values.flatMap(_.viperMembers.values).count(!_.success)
          assert(newErrors > errorCount)
          errorCount = newErrors
      }
    })
  }

  def runAndCheck(config: Config, statsCollector: StatsCollector, pkgInfo: PackageInfo): VerifierResult = {
    val result = Await.result(gobraInstance.verify(pkgInfo, config)(executor), Duration.Inf)

    val nonVerificationErrors = result match {
      case r: VerifierResult.Failure => r.errors.filter(!_.isInstanceOf[VerificationError])
      case _ => Vector()
    }

    if(nonVerificationErrors.nonEmpty) {
      val s = "Encountered parsing errors during task " + pkgInfo.id + ": \n" +nonVerificationErrors.map(_.formattedMessage)
      fail(s)
    }

    assert(statsCollector.typeInfos.contains(pkgInfo.id))
    val typeInfo = statsCollector.typeInfos(pkgInfo.id)
    val interfaceImplementations: List[Type.Type] = typeInfo.interfaceImplementations.values.flatten.toList

    val expectedGobraMembers: Vector[PNode] = typeInfo.tree.originalRoot.programs
      .flatMap(p => p.declarations.flatMap({
        case p: PFunctionDecl => Vector(p)
        case p: PMethodDecl => Vector(p)
        case p: PFPredicateDecl => Vector(p)
        case p: PImplementationProof => p.memberProofs
        // The result will only contain predicates of non-implemented structs, since the other ones are merged together
        // into a single predicate, that we do not receive, since it gets filtered because there isn't a single gobra
        // member it belongs to
        case p: PMPredicateDecl if !interfaceImplementations.contains(typeInfo.typ(p.receiver)) => Vector(p)
        case _ => Vector()
      }))

    // Check if all expected members are present and the entries are correct
    expectedGobraMembers
      .map(member => statsCollector.getMemberInformation(member, statsCollector.typeInfos(pkgInfo.id), null))
      .filter(memberInfo => memberInfo.pkgId == pkgInfo.id)
      .foreach(memberInfo => {
        assert(statsCollector.memberMap.contains(memberInfo.id))

        val memberEntry = statsCollector.memberMap(memberInfo.id)
        assert(memberEntry.info.isTrusted == memberInfo.isTrusted)
        assert(memberEntry.info.isAbstractAndNotImported == memberInfo.isAbstractAndNotImported)

        // Assert there is at least one non-imported viper member for this gobra member
        assert(memberEntry.viperMembers.values.exists(p => !p.fromImport))
      })

    // Assert all methods stored were actually verified
    statsCollector.memberMap.values.flatMap(_.viperMembers.values).forall(_.verified)

    // Test cleanup mechanism
    statsCollector.report(VerificationTaskFinishedMessage(pkgInfo.id))
    assert(!statsCollector.typeInfos.contains(pkgInfo.id))

    result
  }
}
