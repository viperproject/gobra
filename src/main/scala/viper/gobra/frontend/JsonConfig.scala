package viper.gobra.frontend

import viper.gobra.reporting.{ConfigError, VerifierError}

import java.io.File
import java.nio.file.{Files, Path}
import scala.reflect.ClassTag

/** wrapper around `ViperBackend` as json4s fails to directly parse a `ViperBackend`
  * despite custom serializer
  */
object ViperBackendJson {
  import viper.gobra.backend._
  sealed trait Backend extends CliEnumConverter.EnumCase {
    def underlying: ViperBackend
    override val value: String = underlying.value
  }
  case object Silicon extends Backend {
    override val underlying: ViperBackend = ViperBackends.SiliconBackend
  }
  case object Carbon extends Backend {
    override val underlying: ViperBackend = ViperBackends.CarbonBackend
  }
  case object ViperServerWithSilicon extends Backend {
    override val underlying: ViperBackend = ViperBackends.ViperServerWithSilicon()
  }
  case object ViperServerWithCarbon extends Backend {
    override val underlying: ViperBackend = ViperBackends.ViperServerWithCarbon()
  }
}
object ViperBackendJsonConverter extends CliEnumConverter.CliEnum[ViperBackendJson.Backend] {
  import viper.gobra.frontend.ViperBackendJson._
  override def values: List[Backend] = List(
    Silicon, Carbon, ViperServerWithSilicon, ViperServerWithCarbon)
}

trait Resolvable {
  type R <: Resolvable // allows us to give a tight return type to `resolvePaths`:
  /** resolves all relative paths in relation to `basePath` */
  def resolvePaths(basePath: Path): R

  protected def resolveOptPath(basePath: Path, path: Option[String]): Option[String] = {
    path.map(resolvePath(basePath, _))
  }

  protected def resolvePath(basePath: Path, path: String): String =
    basePath.resolve(path).toString
}

/** trait that all top-level JSON objects that we store in files implement */
sealed trait GobraJsonConfig
case class GobraModuleCfg(
                        installation_cfg: Option[GobraInstallCfg],
                        default_job_cfg: Option[VerificationJobCfg],
                      ) extends GobraJsonConfig with Resolvable {
  type R = GobraModuleCfg

  override def resolvePaths(basePath: Path): GobraModuleCfg =
    GobraModuleCfg(
      installation_cfg = installation_cfg.map(_.resolvePaths(basePath)),
      default_job_cfg = default_job_cfg.map(_.resolvePaths(basePath)),
    )
}

case class GobraInstallCfg(
                            jar_path: Option[String] = None,
                            jvm_options: Option[List[String]] = None,
                            z3_path: Option[String] = None,
                          ) extends Resolvable {
  type R = GobraInstallCfg

  /** resolves all relative paths in relation to `basePath` */
  override def resolvePaths(basePath: Path): GobraInstallCfg =
    copy(
      jar_path = resolveOptPath(basePath, jar_path),
      z3_path = resolveOptPath(basePath, z3_path),
    )
}

case class VerificationJobCfg(
                               assume_injectivity_inhale: Option[Boolean] = None,
                               backend: Option[ViperBackendJson.Backend] = None,
                               check_consistency: Option[Boolean] = None,
                               overflow: Option[Boolean] = None,
                               conditionalize_permissions: Option[Boolean] = None,
                               only_files_with_header: Option[Boolean] = None,
                               includes: Option[List[String]] = None,
                               input_files: Option[List[String]] = None,
                               mce_mode: Option[MCE.Mode] = None,
                               module: Option[String] = None,
                               more_joins: Option[MoreJoins.Mode] = None,
                               pkg_path: Option[String] = None, // TODO: what is this?
                               parallelize_branches: Option[Boolean] = None,
                               print_vpr: Option[Boolean] = None,
                               project_root: Option[String] = None,
                               recursive: Option[Boolean] = None, // TODO: why would a package specify recursion?
                               require_triggers: Option[Boolean] = None,
                               other: Option[List[String]] = None,
                             ) extends GobraJsonConfig with Resolvable {
  type R = VerificationJobCfg

  /** resolves all relative paths in relation to `basePath` */
  override def resolvePaths(basePath: Path): VerificationJobCfg =
    copy(
      includes = includes.map(_.map(resolvePath(basePath, _))),
      input_files = input_files.map(_.map(resolvePath(basePath, _))),
      pkg_path = resolveOptPath(basePath, pkg_path),
      project_root = resolveOptPath(basePath, project_root),
    )
}

case object GobraJsonConfigHandler {
  import org.json4s._
  import org.json4s.native._

  private lazy val serializers: Formats = DefaultFormats.withStrictOptionParsing +
    ConfigEnumSerializer(ViperBackendJsonConverter) +
    ConfigEnumSerializer(MCEConverter) +
    ConfigEnumSerializer(MoreJoinsConverter)

  /**
    * NOTE: provide the generic parameter `C` to avoid runtime errors even if
    * the compiler seems to infer it
    */
  def fromJson[C <: GobraJsonConfig with Resolvable](file: File)(implicit mf: Manifest[C]): Either[Vector[VerifierError], C#R] = {
    if (file.exists() && file.isFile) {
      val fileContent = Files.readString(file.toPath)
      try {
        val c = Serialization.read(fileContent)(serializers, mf)
        Right(c.resolvePaths(file.getParentFile.toPath))
      } catch {
        case e: Exception =>
          Left(Vector(ConfigError(s"Failed to parse JSON file: ${e.getMessage}")))
      }
    } else {
      Left(Vector(ConfigError(s"JSON configuration file does not exist: ${file.getAbsoluteFile.toString}")))
    }
  }

  def toJson(c: GobraJsonConfig): String = {
    Serialization.write(c)(serializers)
  }

  case class ConfigEnumSerializer[E <: CliEnumConverter.EnumCase](c: CliEnumConverter.CliEnum[E])(implicit ct: ClassTag[E]) extends CustomSerializer[E](_ => ( {
    case JString(dt) => c.convert(dt)
  }, {
    case e: E => JString(e.value)
  }))
}
