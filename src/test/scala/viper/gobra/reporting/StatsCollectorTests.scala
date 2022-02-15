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
import viper.gobra.frontend.{Config, PackageEntry, ScallopGobraConfig}
import viper.gobra.util.DefaultGobraExecutionContext
import viper.gobra.Gobra
import scala.concurrent.Await
import scala.concurrent.duration.Duration


class StatsCollectorTests extends AnyFunSuite with BeforeAndAfterAll {
  val statsCollectorTestPathPropertyName = "GOBRATESTS_REGRESSIONS_DIR"
  val statsCollectorTestDir: String = System.getProperty(statsCollectorTestPathPropertyName, "src/test/resources/stats_collector")


  test("Integration without chopper") {
    val config = createConfig(Array("-i", statsCollectorTestDir, "-r", "-I", statsCollectorTestDir))
    runIntegration(config)
  }

  //TODO don't exclude package once #417 is fixed
  test("Integration with chopper") {
    val config = createConfig(Array("-i", statsCollectorTestDir, "-r", "-I", statsCollectorTestDir, "--chop", "10",
      "--excludePackages", "subpackage"
    ))
    runIntegration(config)
  }

  private def createConfig(args: Array[String]): Config = {
    // set throwError to true: Scallop will throw an exception instead of terminating the program in case an
    // exception occurs (e.g. a validation failure)
    throwError.value = true
    // Simulate pick of package, Gobra normally does
    new ScallopGobraConfig(args.toSeq).config
  }

  private def runIntegration(config: Config): Unit = {
    // Overwrite reporter
    val statsCollector = StatsCollector(NoopReporter)
    val executor = new DefaultGobraExecutionContext()
    val gobraInstance = new Gobra()

    config.inputPackageMap.foreach({case (PackageEntry(pkgDir, pkg), inputs) =>
      val taskName = pkgDir + " - " + pkg

      Await.result(gobraInstance.verify(config.copy(reporter = statsCollector, inputs = inputs), Some(taskName))(executor), Duration.Inf)
      assert(statsCollector.typeInfos.contains(taskName))
      val typeInfo = statsCollector.typeInfos(taskName)

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
        /*case p: PTypeDef => p.right match {
          case p: PInterfaceType => p.methSpecs ++ p.predSpec
          case _ => Vector()
        }*/
        case _ => Vector()
      }))

      // Check if all expected members are present
      expectedGobraMembers
        .map(member => statsCollector.getMemberInformation(member, statsCollector.typeInfos(taskName)))
        .filter(memberInfo => memberInfo.pkg == pkg && memberInfo.pkgDir == pkgDir)
        .foreach(memberInfo => {
          val memberKey = statsCollector.gobraMemberKey(memberInfo.pkgDir, memberInfo.pkg, memberInfo.memberName, memberInfo.args)
          assert(statsCollector.memberMap.contains(memberKey))

          val memberEntry = statsCollector.memberMap(memberKey)
          assert(memberEntry.isTrusted == memberInfo.isTrusted)
          assert(memberEntry.isAbstract == memberInfo.isAbstract)

          // Assert there is at least one non-imported viper member for this gobra member
          assert(memberEntry.viperMembers.values.exists(p => !p.fromImport))
      })

      statsCollector.report(VerificationTaskFinishedMessage(taskName))
      assert(!statsCollector.typeInfos.contains(taskName))
    })

    executor.terminateAndAssertInexistanceOfTimeout()
  }
}
