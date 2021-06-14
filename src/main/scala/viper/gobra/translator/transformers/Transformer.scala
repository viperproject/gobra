package viper.gobra.translator.transformers

import viper.gobra.backend.BackendVerifier.Task

trait Transformer {
  def transform(task: Task): Task
}
