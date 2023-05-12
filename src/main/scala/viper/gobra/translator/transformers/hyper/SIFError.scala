// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.transformers.hyper

import viper.silver.ast.{Fold, Unfold}
import viper.silver.verifier.errors.ErrorNode
import viper.silver.verifier._


case class SIFTerminationChannelCheckFailed(offendingNode: ErrorNode, reason: ErrorReason,
                                            override val cached: Boolean = false) extends AbstractVerificationError {
  val id: String = "termination_channel_check.failed"
  val text: String = "Termination channel might exist."
  override def withNode(offendingNode: ErrorNode = this.offendingNode): ErrorMessage =
    SIFTerminationChannelCheckFailed(offendingNode, this.reason)

  override def withReason(r: ErrorReason): AbstractVerificationError = SIFTerminationChannelCheckFailed(offendingNode, r)
}

case class SIFFoldNotLow(offendingNode: Fold) extends AbstractErrorReason {
  val id: String = "sif.fold"
  val readableMessage: String = s"The low parts of predicate ${offendingNode.acc.loc.predicateName} might not hold."

  override def withNode(offendingNode: ErrorNode = this.offendingNode): ErrorMessage =
    SIFFoldNotLow(offendingNode.asInstanceOf[Fold])
}

case class SIFUnfoldNotLow(offendingNode: Unfold) extends AbstractErrorReason {
  val id: String = "sif.unfold"
  val readableMessage: String = s"The low parts of predicate ${offendingNode.acc.loc.predicateName} might not hold."

  override def withNode(offendingNode: ErrorNode = this.offendingNode): ErrorMessage =
    SIFUnfoldNotLow(offendingNode.asInstanceOf[Unfold])
}

case class SIFTermCondNotLow(offendingNode: SIFTerminatesExp) extends AbstractErrorReason {
  val id: String = "sif_termination.condition_not_low"
  val readableMessage: String = s"Termination condition ${offendingNode.cond} might not be low."

  override def withNode(offendingNode: ErrorNode = this.offendingNode): ErrorMessage =
    SIFTermCondNotLow(offendingNode.asInstanceOf[SIFTerminatesExp])
}

case class SIFTermCondLowEvent(offendingNode: SIFTerminatesExp) extends AbstractErrorReason {
  val id: String = "sif_termination.not_lowevent"
  val readableMessage: String =
    s"Termination condition ${offendingNode.cond} evaluating to false might not imply both executions don't terminate."

  override def withNode(offendingNode: ErrorNode = this.offendingNode): ErrorMessage =
    SIFTermCondLowEvent(offendingNode.asInstanceOf[SIFTerminatesExp])
}

case class SIFTermCondNotTight(offendingNode: SIFTerminatesExp) extends AbstractErrorReason {
  val id: String = "sif_termination.condition_not_tight"
  val readableMessage: String = s"Termination condition ${offendingNode.cond} might not be tight."

  override def withNode(offendingNode: ErrorNode = this.offendingNode): ErrorMessage =
    SIFTermCondNotTight(offendingNode.asInstanceOf[SIFTerminatesExp])
}
