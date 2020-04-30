package viper.gobra.backend

// This is just the backendConfig which is used by ViperServer. maybe change this later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
trait ViperBackendConfig {
  val partialCommandLine: List[String]
}

object ViperBackendConfigs {
  object EmptyConfig extends ViperBackendConfig {val partialCommandLine: List[String] = Nil}
  
  case class SiliconConfig(partialCommandLine: List[String]) extends ViperBackendConfig
  case class CarbonConfig(partialCommandLine: List[String]) extends ViperBackendConfig
  case class CustomConfig(partialCommandLine: List[String]) extends ViperBackendConfig
}
