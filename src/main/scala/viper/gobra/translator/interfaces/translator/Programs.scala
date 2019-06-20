package viper.gobra.translator.interfaces.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.backend.BackendVerifier
import viper.gobra.translator.interfaces.TranslatorConfig

trait Programs {
  def translate(program: in.Program)(conf: TranslatorConfig): BackendVerifier.Task
}
