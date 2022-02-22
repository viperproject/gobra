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
import viper.gobra.frontend.{Config, ScallopGobraConfig}
import viper.gobra.util.{DefaultGobraExecutionContext, GobraExecutionContext}
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
    val config = createConfig(Array("-i", statsCollectorTestDir, "-r", "-I", statsCollectorTestDir))
    runIntegration(config)
  }

  //TODO don't exclude package once #417 is fixed
  test("Integration with chopper") {
    val config = createConfig(Array("-i", statsCollectorTestDir, "-r", "-I", statsCollectorTestDir, "--chop", "10",
      "--excludePackages", "subpackage"
    ))
    runPackagesSeparately(config)
    runIntegration(config)
  }

  private def createConfig(args: Array[String]): Config = {
    // set throwError to true: Scallop will throw an exception instead of terminating the program in case an
    // exception occurs (e.g. a validation failure)
    throwError.value = true
    // Simulate pick of package, Gobra normally does
    new ScallopGobraConfig(args.toSeq).config
  }

  private def runPackagesSeparately(config: Config): Unit = {
    // Overwrite reporter
    config.inputPackageMap.foreach({case (pkgId, inputs) =>
      val statsCollector = StatsCollector(NoopReporter)
      val result = runAndCheck(config.copy(inputs = inputs, reporter = statsCollector, taskName = pkgId), statsCollector, pkgId)

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
    config.inputPackageMap.foreach({ case (pkgId, inputs) =>
      val results = runAndCheck(config.copy(inputs = inputs, reporter = statsCollector, taskName=pkgId), statsCollector, pkgId)

      results match {
        case VerifierResult.Success => assert(statsCollector.memberMap.values.flatMap(_.viperMembers.values).count(!_.success) == errorCount)
        case VerifierResult.Failure(_) =>
          val newErrors = statsCollector.memberMap.values.flatMap(_.viperMembers.values).count(!_.success)
          assert(newErrors > errorCount)
          errorCount = newErrors
      }
    })
  }

  def runAndCheck(config: Config, statsCollector: StatsCollector, pkgId: String): VerifierResult = {
    val result = Await.result(gobraInstance.verify(config)(executor), Duration.Inf)

    val nonVerificationErrors = result match {
      case r: VerifierResult.Failure => r.errors.filter(!_.isInstanceOf[VerificationError])
      case _ => Vector()
    }

    if(nonVerificationErrors.nonEmpty) {
      val s = "Encountered parsing errors during task " + pkgId + ": \n" +nonVerificationErrors.map(_.formattedMessage)
      fail(s)
    }

    assert(statsCollector.typeInfos.contains(pkgId))
    val typeInfo = statsCollector.typeInfos(pkgId)
    val interfaceImplementations: List[Type.Type] = typeInfo.interfaceImplementations.values.flatten.toList

    val expectedGobraMembers: Vector[PNode] = typeInfo.tree.originalRoot.programs
      .filter(p => !p.isBuiltin)
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
      .map(member => statsCollector.getMemberInformation(member, statsCollector.typeInfos(pkgId)))
      .filter(memberInfo => memberInfo.pkgId == pkgId)
      .foreach(memberInfo => {
        val memberKey = statsCollector.gobraMemberKey(pkgId, memberInfo.memberName, memberInfo.args)
        assert(statsCollector.memberMap.contains(memberKey))

        val memberEntry = statsCollector.memberMap(memberKey)
        assert(memberEntry.isTrusted == memberInfo.isTrusted)
        assert(memberEntry.isAbstract == memberInfo.isAbstract)

        // Assert there is at least one non-imported viper member for this gobra member
        assert(memberEntry.viperMembers.values.exists(p => !p.fromImport))
      })

    // Assert all methods stored were actually verified
    statsCollector.memberMap.values.flatMap(_.viperMembers.values).forall(_.verified)

    // Test cleanup mechanism
    statsCollector.report(VerificationTaskFinishedMessage(pkgId))
    assert(!statsCollector.typeInfos.contains(pkgId))

    result
  }
}
