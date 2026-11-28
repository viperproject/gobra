// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.backend

import viper.gobra.frontend.{CliEnumConverter, Config, Hyper, MCE, PackageInfo}
import viper.gobra.reporting.SIFEncodedViperMessage
import viper.gobra.util.GobraExecutionContext
import viper.server.ViperConfig
import viper.server.core.ViperCoreServer
import viper.silicon.decider.Z3ProverAPI
import viper.server.vsi.DefaultVerificationServerStart
import viper.hyperfuncs.HyperfuncsConfig
import viper.silver.{ast => vpr}

import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.util.Using

trait ViperBackend extends CliEnumConverter.EnumCase {
  def create(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo)(implicit executor: GobraExecutionContext): ViperVerifier

  protected def buildOptions(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo): Vector[String] = {
    var options: Vector[String] = Vector.empty

    options ++= exePaths
    if (config.assumeInjectivityOnInhale) {
      options ++= Vector("--assumeInjectivityOnInhale")
    }
    if (config.hyperModeOrDefault != Hyper.Disabled) {
      // The hyperfuncs plugin builds the product program: for `Hyper.EnabledExtended` with Viper's SIF plugin
      // (SIFExtendedTransformer), which verifies every activation (jcp19/hyperfuncs#13); for `Hyper.Enabled` and
      // `Hyper.NoMajor` with the lockstep construction (low-guard checks, k executions).
      options ++= Vector("--plugin", "viper.hyperfuncs.HyperfuncsPlugin")
      HyperfuncsConfig.construction = config.hyperModeOrDefault match {
        case Hyper.EnabledExtended => HyperfuncsConfig.Extended
        case mode => HyperfuncsConfig.Lockstep(config.hyperExecutionsOrDefault, noMinor = mode == Hyper.NoMajor)
      }
      // Gobra's type checker enforces the static rules of hyperfuncs
      HyperfuncsConfig.checkStaticRules = false
      // since Gobra adds gotos to handle return statements, which might jump out of a loop, we cannot use the default
      // encoding of gotos in the SIFExtendedTransformer:
      HyperfuncsConfig.enableGotoLowEventEncoding = true
      HyperfuncsConfig.enableExperimentalFeatures = config.enableExperimentalHyperFeatures
      // `rel` is experimental only in hyper mode extended
      HyperfuncsConfig.enableRel = config.hyperModeOrDefault != Hyper.EnabledExtended || config.enableExperimentalHyperFeatures
      // errors of the activity checks of `rel(e, i)` read "active(i)" at the position of the `rel`
      HyperfuncsConfig.activeConditionInfo = (at, i) =>
        at.info.getUniqueInfo[viper.gobra.reporting.Source.Verifier.Info]
          .orElse(viper.gobra.reporting.Source.searchInfo(at)) match {
          case Some(vi) => vi.copy(origin = viper.gobra.reporting.Source.Origin(vi.origin.pos, s"active($i)"))
          case None => vpr.NoInfo
        }
      // registers a callback to report the encoded Viper program such that we can print it if the user set `--printSIFVpr`:
      HyperfuncsConfig.transformedProgramCallback = Some(encodedVpr =>
        config.reporter.report(SIFEncodedViperMessage(
          config.taskName,
          config.packageInfoInputMap(pkgInfo).map(_.name),
          () => encodedVpr)))
    }

    options
  }
}

trait SiliconBasedBackend extends ViperBackend {
  override protected def buildOptions(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo): Vector[String] = {
    var options: Vector[String] = super.buildOptions(exePaths, config, pkgInfo)
    options ++= Vector("--logLevel", "ERROR")
    options ++= Vector("--disableCatchingExceptions")
    if (config.conditionalizePermissions) {
      options ++= Vector("--conditionalizePermissions")
    }
    if (config.z3APIMode) {
      options ++= Vector(s"--prover=${Z3ProverAPI.name}")
    }
    if (config.disableNL) {
      options ++= Vector(s"--disableNL")
    }
    if (config.unsafeWildcardOptimization) {
      options ++= Vector(s"--unsafeWildcardOptimization")
    }
    options ++= Vector(s"--moreJoins=${config.moreJoins.viperValue}")
    val mceSiliconOpt = config.mceMode match {
      case MCE.Disabled => "0"
      case MCE.Enabled  => "1"
      case MCE.OnDemand => "2"
    }
    options ++= Vector(s"--exhaleMode=$mceSiliconOpt")
    // Gobra seems to be much slower with the new silicon axiomatization of collections.
    // For now, we stick to the old one.
    options ++= Vector("--useOldAxiomatization")
    if (config.parallelizeBranches) {
      options ++= Vector("--parallelizeBranches")
    }
    if (config.disableSetAxiomatization) {
      // Since resources are stored within the .jar archive, we cannot
      // directly pass the axiom file to Silicon.
      val tmpPath = Paths.get("gobra_tmp")
      val axiomTmpPath = tmpPath.resolve("noaxioms_sets.vpr")
      val axiom: Source = Source.fromResource("noaxioms/sets.vpr")

      Files.createDirectories(tmpPath)
      Using(axiom) { source =>
        Files.write(axiomTmpPath, source.mkString.getBytes)
      }

      options ++= Vector("--setAxiomatizationFile", axiomTmpPath.toString())
    }
    config.assertTimeout.foreach { timeout =>
      options ++= Vector(s"--assertTimeout=$timeout")
    }

    options
  }
}

trait CarbonBasedBackend extends ViperBackend {
  override protected def buildOptions(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo): Vector[String] = {
    val options: Vector[String] = super.buildOptions(exePaths, config, pkgInfo)
    // options ++= Vector("--logLevel", "ERROR")
    options
  }
}

object ViperBackends {

  object SiliconBackend extends SiliconBasedBackend {
    override val value: String = "SILICON"
    override def create(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo)(implicit executor: GobraExecutionContext): Silicon = {
      val options = buildOptions(exePaths, config, pkgInfo)
      new Silicon(options)
    }
  }

  object CarbonBackend extends CarbonBasedBackend {
    override val value: String = "CARBON"
    override def create(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo)(implicit executor: GobraExecutionContext): Carbon = {
      val options = buildOptions(exePaths, config, pkgInfo)
      new Carbon(options)
    }
  }

  abstract class ViperServerBackend(initialServer: Option[ViperCoreServer] = None) extends ViperBackend {
    private var server: Option[ViperCoreServer] = initialServer

    /** abstract method that should return the backend-specific configuration. The configuration will then be passed
      * on to the ViperServer instance.
      */
    def getViperVerifierConfig(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo): ViperVerifierConfig

    /** returns a ViperServer instance with an underlying ViperCoreServer. A fresh ViperServer instance should be used
      * for each verification. The underlying ViperCoreServer instance is reused if one already exists.
      */
    override def create(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo)(implicit executionContext: GobraExecutionContext): ViperServer = {
      val initializedServer = getOrCreateServer(config)(executionContext)
      val executor = initializedServer.executor
      // the executor used to verify is expected to correspond to the one used by the server:
      assert(executor == executionContext, "a different execution context is used than expected")
      val verifierConfig = getViperVerifierConfig(exePaths, config, pkgInfo)
      new ViperServer(initializedServer, verifierConfig, config.abortSignal)(executor)
    }

    /** returns an existing ViperCoreServer instance or otherwise creates a new one */
    protected def getOrCreateServer(config: Config)(executionContext: GobraExecutionContext): ViperCoreServer = {
      server.getOrElse({
        var serverConfig = List("--logLevel", config.logLevel.levelStr)
        if(config.cacheFile.isDefined) {
          serverConfig = serverConfig.appendedAll(List("--cacheFile", config.cacheFile.get.toString))
        }

        val createdServer = new ViperCoreServer(new ViperConfig(serverConfig))(executionContext) with DefaultVerificationServerStart
        // store server for next time:
        server = Some(createdServer)
        createdServer
      })
    }

    /** resets the ViperCoreServer instance such that a new one will be created for the next verification */
    def resetServer(): Unit =
      server = None
  }

  case class ViperServerWithSilicon(initialServer: Option[ViperCoreServer] = None) extends ViperServerBackend(initialServer) with SiliconBasedBackend {
    override val value: String = "VSWITHSILICON"
    override def getViperVerifierConfig(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo): ViperVerifierConfig = {
      val options = super.buildOptions(exePaths, config, pkgInfo)
      ViperServerConfig.ConfigWithSilicon(options.toList)
    }
  }

  case class ViperServerWithCarbon(initialServer: Option[ViperCoreServer] = None) extends ViperServerBackend(initialServer) with CarbonBasedBackend {
    override val value: String = "VSWITHCARBON"
    override def getViperVerifierConfig(exePaths: Vector[String], config: Config, pkgInfo: PackageInfo): ViperVerifierConfig = {
      val options = super.buildOptions(exePaths, config, pkgInfo)
      ViperServerConfig.ConfigWithCarbon(options.toList)
    }
  }
}
