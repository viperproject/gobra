// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.implementation.typing.modifiers.OwnerModifier
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.util.{Core, ViperWriter}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.{ast => vpr}

class DeferEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}

  /** Used to collect info about defer statements that occur in a method body. */
  case class Defer(
                    activationVar: vpr.LocalVarDecl,
                    temporaryVars: Vector[vpr.LocalVarDecl],
                    stmt: MemberWriter[vpr.Stmt]
                  ) extends ViperWriter.CodeCollectible

  /**
    * For every defer, we generate an activation variable `active` and temporary variables `temp1, temp2, ...`
    * The encoding of a defer statement is split into two parts:
    *
    * 1) The defer statement itself (by [[statement]]):
    *
    *      [defer C(e1, ..., eN)] ->
    *         active = true
    *         temp1 = [e1]; ...; tempN = [eN]
    *
    * 2) Around the enclosing method body `body` (by [[extendStatement]]):
    *
    *      var active = false
    *      var temp1, ..., tempN
    *      body
    *      if (active) { C(temp1, ..., tempN) }
    *
    * */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case n: in.Defer =>
      val (pos, info, errT) = n.vprMeta

      val name = ctx.freshNames.next()
      val args = Core.coreArgs(n.stmt)
      val vars = args.zipWithIndex map { case (v, idx) => in.LocalVar(s"${name}_$idx", v.typ.withOwnerModifier(OwnerModifier.Exclusive))(v.info) }
      val appliedCore = Core.core(n.stmt).run(vars)

      val vprVars = vars map ctx.variable // temporary variables
      val activationVar = vpr.LocalVarDecl(s"${name}_activation", vpr.Bool)(pos,info,errT) // activation variable

      val w = ml.block(ctx.statement(appliedCore))

      for {
        _ <- sequence((vprVars zip args) map { case (l, r) =>
          // temp1 = [e1]; ...; tempN = [eN]
          ctx.expression(r).flatMap(rv => bind(l.localVar, rv))
        })
        _ <- collect(Defer(activationVar, vprVars, w))
        // active = true
      } yield vpr.LocalVarAssign(activationVar.localVar, vpr.BoolLit(b = true)(pos,info,errT))(): vpr.Stmt
  }

  override def extendStatement(ctx: Context): in.Stmt ==> Extension[CodeWriter[vpr.Stmt]] = {
    case _: in.MethodBodySeqn => w => {
      val defers = w.collect{ case x: Defer => x }
      // defers are executed in reverse program order. This is fine if we do not have gotos, or defers in loops.
      def order(x: Defer): Int = -x.activationVar.pos.asInstanceOf[vpr.HasLineColumn].line
      val sortedDefers = defers.sortBy(order)

      val defersExec = sortedDefers map { case Defer(active, temps, w) => (body: vpr.Stmt) =>
        // var active = false
        // var temp1, ..., tempN
        // body
        // if (active) { C(temp1, ..., tempN) }
        for {
          deferredStmt <- w // C(temp1, ..., tempN)
          init = vpr.LocalVarAssign(active.localVar, vpr.FalseLit()(active.pos, active.info, active.errT))(active.pos, active.info, active.errT)
          deferExec = vpr.If(
            active.localVar,
            vpr.Seqn(Vector(deferredStmt), Vector.empty)(active.pos, active.info, active.errT),
            vpr.Seqn(Vector.empty, Vector.empty)(active.pos, active.info, active.errT)
          )(active.pos, active.info, active.errT)
        } yield vpr.Seqn(Vector(init, body, deferExec), active +: temps)(active.pos, active.info, active.errT)
      }

      // performs transformation of body for each defer (in the reverse program order)
      defersExec.foldLeft(w){ case (w, ext) =>
        for {
          body <- w
          nextBody <- fromMemberLevel(ext(body))
        } yield nextBody
      }
    }
  }

}
