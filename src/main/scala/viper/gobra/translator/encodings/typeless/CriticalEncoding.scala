// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.reporting.Source.{InvariantMightBeOpenAnnotation, InvariantNotRestoredAnnotation}
import viper.gobra.theory.Addressability
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

/**
  * Encodes critical regions `critical inv (body)`, which temporarily open the invariant `inv`
  * around `body`:
  *
  *   assert Invariant(inv)             // inv must be a (duplicable) invariant
  *   assert !(inv in openInvs)         // inv must not be open already (no re-entrancy)
  *   openInvs := openInvs union {inv}
  *   inhale acc(inv())
  *   body
  *   openInvs := openInvs setminus {inv}
  *   exhale acc(inv())                 // the invariant must be restored at the end of the region
  *
  * `openInvs` is the per-member set of currently open invariants. It is declared and initialized
  * by the desugarer, which also preserves its value across loops (see [[in.Critical]]).
  */
class CriticalEncoding extends Encoding {

  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    case n: in.Critical =>
      val invSrc = n.inv.info
      val isInvSrc = n.invIsInv.info
      val isOpenSrc = annotated(invSrc, InvariantMightBeOpenAnnotation())
      val notRestoredSrc = annotated(invSrc, InvariantNotRestoredAnnotation())
      val predT = in.PredT(Vector.empty, Addressability.Exclusive)

      def invAccess(src: Source.Parser.Info): in.Assertion = in.Access(
        in.Accessible.PredExpr(in.PredExprInstance(n.inv, Vector.empty)(src)),
        in.FullPerm(src)
      )(src)

      val isInv = in.Assert(in.ExprAssertion(n.invIsInv)(isInvSrc))(isInvSrc)
      val checkNotOpen = in.Assert(
        in.ExprAssertion(in.Negation(in.Contains(n.inv, n.openInvs)(isOpenSrc))(isOpenSrc))(isOpenSrc)
      )(isOpenSrc)
      val markOpen = in.SingleAss(
        in.Assignee.Var(n.openInvs),
        in.Union(n.openInvs, in.SetLit(predT, Vector(n.inv))(invSrc), predT)(invSrc)
      )(invSrc)
      val inhaleInv = in.Inhale(invAccess(invSrc))(invSrc)
      val markClosed = in.SingleAss(
        in.Assignee.Var(n.openInvs),
        in.SetMinus(n.openInvs, in.SetLit(predT, Vector(n.inv))(invSrc), predT)(invSrc)
      )(invSrc)
      val exhaleInv = in.Exhale(invAccess(notRestoredSrc))(notRestoredSrc)

      ctx.statement(in.Seqn(
        Vector(isInv, checkNotOpen, markOpen, inhaleInv, n.body, markClosed, exhaleInv)
      )(n.info))
  }

  private def annotated(info: Source.Parser.Info, annotation: Source.Annotation): Source.Parser.Info = info match {
    case s: Source.Parser.Single => s.createAnnotatedInfo(annotation)
    case i => i
  }
}
