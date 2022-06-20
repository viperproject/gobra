// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.util.{Core, ViperWriter}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter}
import viper.silver.{ast => vpr}

class DeferEncoding extends Encoding {

  import viper.gobra.translator.util.ViperWriter.CodeLevel._
  import viper.gobra.translator.util.ViperWriter.{MemberLevel => ml}

  case class Defer(
                    activationVar: vpr.LocalVarDecl,
                    argumentVars: Vector[vpr.LocalVarDecl],
                    stmt: MemberWriter[vpr.Stmt]
                  ) extends ViperWriter.CodeCollectible

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case n: in.Defer =>
      val (pos, info, errT) = n.vprMeta

      val name = n.id
      val args = Core.coreArgs(n.stmt)
      val vars = args.zipWithIndex map { case (v, idx) => in.LocalVar(s"${name}_$idx", v.typ.withAddressability(Addressability.Exclusive))(v.info) }
      val appliedCore = Core.core(n.stmt).run(vars)

      val vprVars = vars map ctx.variable
      val activationVar = vpr.LocalVarDecl(s"${name}_activation", vpr.Bool)(pos,info,errT)

      val w = ml.block(ctx.statement(appliedCore))

      for {
        _ <- sequence((vprVars zip args) map { case (l, r) =>
          ctx.expression(r).flatMap(rv => bind(l.localVar, rv))
        })
        _ <- collect(Defer(activationVar, vprVars, w))
      } yield vpr.LocalVarAssign(activationVar.localVar, vpr.BoolLit(b = true)(pos,info,errT))(): vpr.Stmt
  }

  override def extendStatement(ctx: Context): in.Stmt ==> Extension[CodeWriter[vpr.Stmt]] = {
    case _: in.MethodBodySeqn => w => {
      val defers = w.collect{ case x: Defer => x }
      def order(x: Defer): Int = -x.activationVar.pos.asInstanceOf[vpr.HasLineColumn].line
      val sortedDefers = defers.sortBy(order)

      val defersExec = sortedDefers map { case Defer(a, args, w) => (body: vpr.Stmt) =>
        for {
          deferredStmt <- w
          init = vpr.LocalVarAssign(a.localVar, vpr.FalseLit()(a.pos, a.info, a.errT))(a.pos, a.info, a.errT)
          deferExec = vpr.If(
            a.localVar,
            vpr.Seqn(Vector(deferredStmt), Vector.empty)(a.pos, a.info, a.errT),
            vpr.Seqn(Vector.empty, Vector.empty)(a.pos, a.info, a.errT)
          )(a.pos, a.info, a.errT)
        } yield vpr.Seqn(Vector(init, body, deferExec), a +: args)(a.pos, a.info, a.errT)
      }

      defersExec.foldLeft(w){ case (w, ext) =>
        for {
          body <- w
          nextBody <- fromMemberLevel(ext(body))
        } yield nextBody
      }
    }
  }

}
