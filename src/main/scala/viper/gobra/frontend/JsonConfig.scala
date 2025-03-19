package viper.gobra.frontend

import viper.gobra.reporting.{ConfigError, VerifierError}

/** trait for an enum value that can appear as a configuration value */
sealed trait ConfigEnum {
  def value: String
}
/** trait for an enum value to configure parsing and serialization of this enum */
sealed trait JsonEnumConfiguration[E <: ConfigEnum] {
  def values: List[E]
}

sealed trait ViperBackend extends ConfigEnum
case object Silicon extends ViperBackend {
  override def value: String = "SILICON"
}
case object Carbon extends ViperBackend {
  override def value: String = "CARBON"
}
case object SiliconViperServer extends ViperBackend {
  override def value: String = "VSWITHSILICON"
}
case object CarbonViperServer extends ViperBackend {
  override def value: String = "VSWITHCARBON"
}
case object ViperBackendConfiguration extends JsonEnumConfiguration[ViperBackend] {
  override def values: List[ViperBackend] = List(Silicon, Carbon, SiliconViperServer, CarbonViperServer)
}

sealed trait MceMode extends ConfigEnum
case object MceOn extends MceMode {
  override def value: String = "on"
}
case object MceOd extends MceMode {
  override def value: String = "od"
}
case object MceOff extends MceMode {
  override def value: String = "off"
}
case object MceModeConfiguration extends JsonEnumConfiguration[MceMode] {
  override def values: List[MceMode] = List(MceOn, MceOd, MceOff)
}

sealed trait MoreJoins extends ConfigEnum
case object MoreJoinsAll extends MoreJoins {
  override def value: String = "all"
}
case object MoreJoinsImpure extends MoreJoins {
  override def value: String = "impure"
}
case object MoreJoinsOff extends MoreJoins {
  override def value: String = "off"
}
case object MoreJoinsConfiguration extends JsonEnumConfiguration[MoreJoins] {
  override def values: List[MoreJoins] = List(MoreJoinsAll, MoreJoinsImpure, MoreJoinsOff)
}

/** trait that all top-level JSON objects that we store in files implement */
sealed trait GobraJsonConfig
case class GobraModuleCfg(
                        installation_cfg: Option[GobraInstallCfg],
                        default_job_cfg: Option[VerificationJobCfg],
                      ) extends GobraJsonConfig

case class GobraInstallCfg(
                            jar_path: Option[String] = None,
                            jvm_options: Option[List[String]] = None,
                            z3_path: Option[String] = None,
                          )

case class VerificationJobCfg(
                               assume_injectivity_inhale: Option[Boolean] = None,
                               backend: Option[ViperBackend] = None,
                               check_consistency: Option[Boolean] = None,
                               overflow: Option[Boolean] = None,
                               conditionalize_permissions: Option[Boolean] = None,
                               only_files_with_header: Option[Boolean] = None,
                               includes: Option[List[String]] = None,
                               input_files: Option[List[String]] = None,
                               mce_mode: Option[MceMode] = None,
                               module: Option[String] = None,
                               more_joins: Option[MoreJoins] = None,
                               pkg_path: Option[String] = None, // TODO: what is this?
                               parallelize_branches: Option[Boolean] = None,
                               print_vpr: Option[Boolean] = None,
                               project_root: Option[String] = None,
                               recursive: Option[Boolean] = None, // TODO: why would a package specify recursion?
                               require_triggers: Option[Boolean] = None,
                               other: Option[List[String]] = None, // TODO: what is this?
                             ) extends GobraJsonConfig

case object GobraJsonConfigHandler {
  import org.json4s._
  import org.json4s.native._

  private lazy val serializers: Formats = Serialization.formats(NoTypeHints) + List(
    ConfigEnumSerializer(ViperBackendConfiguration),
    ConfigEnumSerializer(MceModeConfiguration),
    ConfigEnumSerializer(MoreJoinsConfiguration),
  )

  def fromJson[C <: GobraJsonConfig](json: String): Either[Vector[VerifierError], C] = {
    try {
      Right(Serialization.read(json)(serializers, manifest[C]))
    } catch {
      case e: Exception =>
        Left(Vector(ConfigError(s"Failed to parse JSON file: ${e.getMessage}")))
    }
  }

  def toJson(c: GobraJsonConfig): String = {
    Serialization.write(c)(serializers)
  }

  case class ConfigEnumSerializer[E <: ConfigEnum](c: JsonEnumConfiguration[E]) extends CustomSerializer[E](_ => ( {
    case JString(dt) if c.values.exists(_.value == dt) => c.values.find(_.value == dt).get
  }, {
    case e: E => JString(e.value)
  }))
}
