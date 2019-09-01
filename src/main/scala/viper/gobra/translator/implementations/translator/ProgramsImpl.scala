package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.backend.BackendVerifier
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.gobra.translator.implementations.{CollectorImpl, ContextImpl}
import viper.gobra.translator.interfaces.TranslatorConfig
import viper.gobra.translator.interfaces.translator.Programs
import viper.silver.{ast => vpr}
import viper.gobra.reporting.Source.{withInfo => nodeWithInfo}
import viper.silver.ast.utility.AssumeRewriter

class ProgramsImpl extends Programs {

  import viper.gobra.translator.util.ViperWriter.MemberLevel._

  override def translate(program: in.Program)(conf: TranslatorConfig): BackendVerifier.Task = {

    val ctx = new ContextImpl(conf)

    val progW = for {
      methods <- sequence(program.members collect {
        case f: in.Function => ctx.method.function(f)(ctx)
        case m: in.Method => ctx.method.method(m)(ctx)
      })

      functions <- sequence(program.members collect {
        case f: in.PureFunction => ctx.pureMethod.pureFunction(f)(ctx)
        case m: in.PureMethod => ctx.pureMethod.pureMethod(m)(ctx)
      })

      predicates <- sequence(program.members collect {
        case p: in.MPredicate => ctx.predicate.mpredicate(p)(ctx)
        case p: in.FPredicate => ctx.predicate.fpredicate(p)(ctx)
      })

      col = {
        val c = new CollectorImpl()
        ctx.finalize(c)
        c
      }

      vProgram = nodeWithInfo(vpr.Program(
        domains = col.domains,
        fields = col.fields,
        predicates = col.predicate ++ predicates,
        functions = col.functions ++ functions,
        methods = col.methods ++ methods
      ))(program)

    } yield vProgram

    val (error, _, prog) = progW.execute

    val progWithoutAssumes = {
      val uncleanProg = AssumeRewriter.rewriteAssumes(prog)
      // FIXME: required due to inconvenient silver assume rewriter
      val cleanedDomains: Seq[vpr.Domain] = uncleanProg.domains.map{ d =>
        if (d.name == "Assume") d.copy(name = "Assume$")(d.pos, d.info, d.errT)
        else d
      }
      uncleanProg.copy(domains = cleanedDomains)(uncleanProg.pos, uncleanProg.info, uncleanProg.errT)
    }

    val backTrackInfo = BackTrackInfo(error.errorT, error.reasonT)

    BackendVerifier.Task(
      program = progWithoutAssumes,
      backtrack = backTrackInfo
    )
  }
}
