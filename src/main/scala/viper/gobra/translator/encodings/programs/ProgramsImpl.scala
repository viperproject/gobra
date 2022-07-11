// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.programs

import viper.gobra.ast.{internal => in}
import viper.gobra.backend.BackendVerifier
import viper.gobra.reporting.BackTranslator.BackTrackInfo
import viper.gobra.translator.context.{CollectorImpl, Context, ContextImpl, TranslatorConfig}
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.silver.{ast => vpr}

class ProgramsImpl extends Programs {

  import viper.gobra.translator.util.ViperWriter.MemberLevel._

  override def translate(program: in.Program)(conf: TranslatorConfig): BackendVerifier.Task = {

    val (pos, info, errT) = program.vprMeta

    val mainCtx = new ContextImpl(conf, program.table)

    def goM(member: in.Member): MemberWriter[(Vector[vpr.Member], Context)] = {
      /** we use a separate context for each member in order to reset the fresh counter */
      val ctx = mainCtx := (initialFreshCounterValueN = 0)
      ctx.member(member).map(m => (m, ctx))
    }

    val progW = for {
      membersWithCtxs <- sequence(program.members map goM)
      (memberss, ctxs) = membersWithCtxs.unzip
      members = memberss.flatten

      col = {
        val c = new CollectorImpl()
        ctxs.foreach(ctx => ctx.finalize(c))
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

    val (error, _, prog) = progW.run

    val backTrackInfo = BackTrackInfo(error.errorT, error.reasonT)

    BackendVerifier.Task(
      program = prog,
      backtrack = backTrackInfo
    )
  }
}
