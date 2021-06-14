package viper.gobra.translator.transformers
import viper.gobra.backend.BackendVerifier
import viper.silver.ast.utility.AssumeRewriter
import viper.silver.{ast => vpr}

class AssumeTransformer extends Transformer {
  override def transform(task: BackendVerifier.Task): BackendVerifier.Task = {
    val progWithoutAssumes = {
      val uncleanProg = AssumeRewriter.rewriteAssumes(task.program)
      // FIXME: required due to inconvenient silver assume rewriter
      val cleanedDomains: Seq[vpr.Domain] = uncleanProg.domains.map{ d =>
        if (d.name == "Assume") d.copy(name = "Assume$")(d.pos, d.info, d.errT)
        else d
      }
      uncleanProg.copy(domains = cleanedDomains)(uncleanProg.pos, uncleanProg.info, uncleanProg.errT)
    }

    BackendVerifier.Task(
      program = progWithoutAssumes,
      backtrack = task.backtrack
    )
  }
}
