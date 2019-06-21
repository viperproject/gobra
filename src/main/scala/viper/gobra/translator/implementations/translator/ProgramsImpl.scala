package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.backend.BackendVerifier
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.gobra.translator.implementations.{CollectorImpl, ContextImpl}
import viper.gobra.translator.interfaces.TranslatorConfig
import viper.gobra.translator.interfaces.translator.Programs
import viper.silver.{ast => vpr}
import viper.gobra.reporting.Source.withInfo

class ProgramsImpl extends Programs {
  override def translate(program: in.Program)(conf: TranslatorConfig): BackendVerifier.Task = {

    val ctx = new ContextImpl(conf)

    val functions = program.functions map (ctx.func.translate(_)(ctx))

    val col = new CollectorImpl()
    ctx.finalize(col)

    val vProgram = withInfo(vpr.Program(
      domains = col.domains,
      fields = col.fields,
      predicates = col.predicate,
      functions = col.functions,
      methods = col.methods ++ functions
    ))(program)

    val backTrackInfo = BackTrackInfo(col.errorT, col.reasonT)


    BackendVerifier.Task(
      program = vProgram,
      backtrack = backTrackInfo
    )
  }
}
