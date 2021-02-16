// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.backend.BackendVerifier
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.gobra.translator.implementations.{CollectorImpl, ContextImpl}
import viper.gobra.translator.interfaces.TranslatorConfig
import viper.gobra.translator.interfaces.translator.Programs
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.gobra.util.Violation
import viper.silver.{ast => vpr}
import viper.silver.ast.utility.AssumeRewriter

class ProgramsImpl extends Programs {

  import viper.gobra.translator.util.ViperWriter.MemberLevel._

  override def translate(program: in.Program)(conf: TranslatorConfig): BackendVerifier.Task = {

    val (pos, info, errT) = program.vprMeta

    val ctx = new ContextImpl(conf, program.table)

    def goM(member: in.Member): MemberWriter[Vector[vpr.Member]] = {
      val typeEncodingOpt = ctx.typeEncoding.member(ctx).lift(member)
      if (typeEncodingOpt.isDefined) typeEncodingOpt.get
      else {
        member match {
          case f: in.Function => ctx.method.function(f)(ctx).map(Vector(_))
          case m: in.Method => ctx.method.method(m)(ctx).map(Vector(_))
          case f: in.PureFunction => ctx.pureMethod.pureFunction(f)(ctx).map(Vector(_))
          case m: in.PureMethod => ctx.pureMethod.pureMethod(m)(ctx).map(Vector(_))
          case p: in.MPredicate => ctx.predicate.mpredicate(p)(ctx).map(Vector(_))
          case p: in.FPredicate => ctx.predicate.fpredicate(p)(ctx).map(Vector(_))
          case gc: in.GlobalConstDecl => ctx.fixpoint.create(gc)(ctx); unit(Vector.empty)
          case m: in.BuiltInMethod => ctx.builtInMembers.method(m)(ctx); unit(Vector.empty)
          case f: in.BuiltInFunction => ctx.builtInMembers.function(f)(ctx); unit(Vector.empty)
          case p: in.BuiltInMPredicate => ctx.builtInMembers.mpredicate(p)(ctx); unit(Vector.empty)
          case p: in.BuiltInFPredicate => ctx.builtInMembers.fpredicate(p)(ctx); unit(Vector.empty)
          case p => Violation.violation(s"found unsupported member: $p")
        }
      }
    }

    val progW = for {
      memberss <- sequence(program.members map goM)
      members = memberss.flatten

      col = {
        val c = new CollectorImpl()
        ctx.finalize(c)
        c
      }


      domains = members collect { case x: vpr.Domain => x }
      fields = members collect { case x: vpr.Field => x }
      predicates = members.collect { case x: vpr.Predicate => x }
      functions = members.collect { case x: vpr.Function => x }
      methods = members collect { case x: vpr.Method => x }
      extensions = members collect { case x: vpr.ExtensionMember => x }

      vProgram = vpr.Program(
        domains = col.domains ++ domains,
        fields = col.fields ++ fields,
        predicates = col.predicate ++ predicates,
        functions = col.functions ++ functions,
        methods = col.methods ++ methods,
        extensions = col.extensions ++ extensions
      )(pos, info, errT)

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

    /** If you want to enable Viper sanity checks, then comment-in this code: */
//    val errors = progWithoutAssumes.checkTransitively
//    if (errors.nonEmpty) Violation.violation(errors.toString)

    val backTrackInfo = BackTrackInfo(error.errorT, error.reasonT)

    BackendVerifier.Task(
      program = progWithoutAssumes,
      backtrack = backTrackInfo
    )
  }
}
