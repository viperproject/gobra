package viper.gobra.translator.transformers
import viper.gobra.backend.BackendVerifier
import viper.silver.plugin.standard.termination.TerminationPlugin

class TerminationTransformer extends Transformer {
  override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
    val plugin = new TerminationPlugin(null, null, null)
    val transformedProgram = plugin.beforeVerify(task.program)
    BackendVerifier.Task(
      program = transformedProgram,
      backtrack = task.backtrack
    )
  }
}
