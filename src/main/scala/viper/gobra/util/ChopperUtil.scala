// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util


import java.io.File
import java.nio.file.Files
import java.util.Properties
import viper.silver.{ast => vpr}
import viper.silver.ast.SourcePosition
import viper.silver.ast.utility.Chopper
import viper.gobra.frontend.{Config, PackageInfo}
import viper.gobra.reporting.ChoppedViperMessage
import viper.gobra.backend.BackendVerifier.Task

object ChopperUtil {

  val GobraChopperFileLocation = "GobraChopper.conf"

  /** Splits task program into multiple Viper programs depending on config. */
  def computeChoppedPrograms(task: Task, pkgInfo: PackageInfo)(config: Config): Vector[vpr.Program] = {


    val programs = Chopper.chop(task.program)(
      selection = computeIsolateMap(config, pkgInfo),
      bound = Some(config.choppingUpperBound),
      penalty = getPenalty
    )

    // Report Chopped Programs
    programs.zipWithIndex.foreach{ case (chopped, idx) =>
      config.reporter report ChoppedViperMessage(config.packageInfoInputMap(pkgInfo).map(_.name), idx, () => chopped, () => task.backtrack)
    }

    programs
  }

  def computeIsolateMap(config: Config, pkgInfo: PackageInfo): Option[vpr.Member => Boolean] = {
    import viper.gobra.reporting.Source

    val isIsolated = config.isolate match {
      case Some(isolatedPositions) =>
        def hitPosition(x: SourcePosition, target: SourcePosition): Boolean = {
          (target.end match {
            case None => x.start.line == target.start.line
            case Some(pos) => target.start.line <= x.start.line && x.start.line <= pos.line
          }) && Files.isSameFile(x.file, target.file)
        }
        (memberPosition: SourcePosition) => isolatedPositions.exists(hitPosition(_, memberPosition))

      case None =>
        import viper.gobra.frontend.Source.TransformableSource
        (memberPosition: SourcePosition) => config.packageInfoInputMap(pkgInfo).exists(_.contains(memberPosition))
    }

    Some {
      case x@(_: vpr.Method | _: vpr.Function | _: vpr.Predicate) => x match {
        case Source(Source.Verifier.Info(_, _, origin, _)) => isIsolated(origin.pos)
        case _ => false
      }
      case _ => false
    }
  }

  /**
    * If a configuration is present at [[GobraChopperFileLocation]],
    * then a penalty object using this configuration is created and returned.
    * Otherwise, if no configuration is present, the default configuration is returned.
    * */
  def getPenalty: Chopper.Penalty[Chopper.Vertex] = {
    import scala.io.Source
    import viper.silver.ast.utility.Chopper.Penalty

    val file = new File(GobraChopperFileLocation)
    if (!file.exists()) {
      Penalty.Default
    } else {
      val dfltConf = Penalty.defaultPenaltyConfig
      val settings = new Properties
      settings.load(Source.fromFile(file).bufferedReader())
      def get(name: String, dflt: Int): Int = {
        val x = settings.getProperty(name)
        if (x == null) dflt else x.toIntOption.getOrElse(dflt)
      }

      val penaltyConf = Penalty.PenaltyConfig(
        method          = get("method_body",      dfltConf.method),
        methodSpec      = get("method_spec",      dfltConf.methodSpec),
        function        = get("function",         dfltConf.function),
        predicate       = get("predicate_body",   dfltConf.predicate),
        predicateSig    = get("predicate_spec",   dfltConf.predicateSig),
        field           = get("field",            dfltConf.field),
        domainType      = get("domain_type",      dfltConf.domainType),
        domainFunction  = get("domain_function",  dfltConf.domainFunction),
        domainAxiom     = get("domain_axiom",     dfltConf.domainAxiom),
        sharedThreshold = get("threshold_shared", dfltConf.sharedThreshold)
      )

      println("Loaded chopper configuration from file.")

      new Penalty.DefaultImpl(penaltyConf)
    }
  }
}
