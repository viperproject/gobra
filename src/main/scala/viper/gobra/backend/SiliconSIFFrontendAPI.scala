package viper.gobra.backend

import viper.gobra.frontend.{Config, ConfigDefaults, Hyper}
import viper.gobra.translator.transformers.hyper.{SIFLowGuardTransformer, SIFLowGuardTransformerHelper}
import viper.silicon.SiliconFrontendAPI
import viper.silver.ast.Program
import viper.silver.parser.FastParser
import viper.silver.plugin.{SilverPlugin, SilverPluginManager}
import viper.silver.reporter.Reporter

import scala.annotation.unused

// TODO: make this a trait and mix it in in the creation of SiliconFrontend and Carbon
class SiliconSIFFrontendAPI(config: Config, override val reporter: Reporter) extends SiliconFrontendAPI(reporter) {
  private val defaultPlugins: Seq[String] = {
    val sifPlugin = config.hyperMode.getOrElse(ConfigDefaults.DefaultHyperMode) match {
      case Hyper.EnabledExtended =>
        // TODO: requires using Marco's plugin from https://github.com/viperproject/silver-sif-extension
        ???
      case Hyper.Enabled =>
        Some("viper.gobra.backend.SIFLowGuardTransformerEnabledPlugin")
      case Hyper.Disabled => None
      // TODO
      case Hyper.NoMajor => ???
    }
    Seq(
      "viper.silver.plugin.standard.adt.AdtPlugin",
      "viper.silver.plugin.standard.termination.TerminationPlugin",
      "viper.silver.plugin.standard.predicateinstance.PredicateInstancePlugin",
      "viper.silver.plugin.standard.refute.RefutePlugin",
    ) ++ sifPlugin.toList
  }

  // TODO: drop
  def selectSIFPlugin(config: Config): Option[String] = {
    config.hyperMode.getOrElse(ConfigDefaults.DefaultHyperMode) match {

      case Hyper.EnabledExtended =>
        // TODO: requires using Marco's plugin from https://github.com/viperproject/silver-sif-extension
        ???

      case Hyper.Enabled =>
        Some("viper.gobra.backend.SIFLowGuardTransformerEnabledPlugin")

      case Hyper.Disabled => None

      // TODO
      case Hyper.NoMajor => ???
    }
  }

  private var _plugins: SilverPluginManager = {
    val ps = defaultPlugins
    println(s"default plugins: $defaultPlugins")
    SilverPluginManager(ps match {
      case Seq() => None
      case s => Some(s.mkString(":"))
    })(reporter, logger, _config, fp)
  }

  override def plugins: SilverPluginManager = {
    _plugins
  }

  override def resetPlugins(): Unit = {
    // TODO: make these values public in silver
    val smokeDetectionPlugin = "viper.silver.plugin.standard.smoke.SmokeDetectionPlugin"
    val refutePlugin = "viper.silver.plugin.standard.refute.RefutePlugin"
    val pluginsArg: Option[String] = if (_config != null) {
      // concat defined plugins and default plugins
      val list = (if (_config.enableSmokeDetection()) Set(smokeDetectionPlugin, refutePlugin) else Set()) ++
        (if (_config.disableDefaultPlugins()) Set() else defaultPlugins) ++
        _config.plugin.toOption.toSet

      if (list.isEmpty) {
        None
      } else {
        Some(list.mkString(":"))
      }
    } else {
      None
    }
    _plugins = SilverPluginManager(pluginsArg)(reporter, logger, _config, fp)
  }
}

// TODO: move this to a separate file
case class MinimalSIFLowGuardTransformer() extends SIFLowGuardTransformer

// TODO: move this to a separate file
case class SIFLowGuardTransformerEnabledPlugin(@unused reporter: viper.silver.reporter.Reporter,
                                               @unused logger: ch.qos.logback.classic.Logger,
                                               config: viper.silver.frontend.SilFrontendConfig,
                                               fp: FastParser) extends SilverPlugin {
  override def beforeVerify(input: Program): Program = {
    val trans = MinimalSIFLowGuardTransformer()
    trans.program(input, onlyMajor = SIFLowGuardTransformerHelper.onlyMajor(input), noMinor = false)
  }



}

// TODO: move this to a separate file
case class SIFLowGuardTransformerNoMajorPlugin() extends SilverPlugin
