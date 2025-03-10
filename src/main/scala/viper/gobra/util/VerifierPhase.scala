// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2025 ETH Zurich.

package viper.gobra.util

import scala.concurrent.Future
import com.typesafe.scalalogging.LazyLogging
import scalaz.EitherT
import scalaz.Scalaz.futureInstance
import viper.gobra.reporting.{ParserError, ParserMessage, ParserWarning, TypeError, TypeMessage, TypeWarning, VerifierError, VerifierMessage, VerifierResult, VerifierWarning}
import viper.gobra.util.VerifierPhase.PhaseResult

object VerifierPhase {
  type ErrorsAndWarnings = Vector[VerifierMessage]
  type Warnings = Vector[VerifierWarning]
  type PhaseResult[S] = EitherT[ErrorsAndWarnings, Future, (S, Warnings)]

  trait Splitter[M <: VerifierMessage, E <: VerifierError, W <: VerifierWarning] {
    def split(msg: M): Either[E, W]
  }

  object Splitter {
    implicit object VerifierSplitter extends Splitter[VerifierMessage, VerifierError, VerifierWarning] {
      override def split(msg: VerifierMessage): Either[VerifierError, VerifierWarning] = msg match {
        case e: VerifierError => Left(e)
        case w: VerifierWarning => Right(w)
      }
    }
    implicit object ParserSplitter extends Splitter[ParserMessage, ParserError, ParserWarning] {
      override def split(msg: ParserMessage): Either[ParserError, ParserWarning] = msg match {
        case e: ParserError => Left(e)
        case w: ParserWarning => Right(w)
      }
    }
    implicit object TypeSplitter extends Splitter[TypeMessage, TypeError, TypeWarning] {
      override def split(msg: TypeMessage): Either[TypeError, TypeWarning] = msg match {
        case e: TypeError => Left(e)
        case w: TypeWarning => Right(w)
      }
    }
  }

  def splitErrorsAndWarnings[M <: VerifierMessage, E <: VerifierError, W <: VerifierWarning](i: Vector[M])(implicit splitter: Splitter[M, E, W]): (Vector[E], Vector[W]) = {
    i.partitionMap { splitter.split }
  }
}

trait VerifierPhase[I, R] extends LazyLogging {
  val name: String

  /** do not directly invoke `execute` but call `perform` instead */
  protected def execute(input: I)(implicit executor: GobraExecutionContext): R

  protected def resultIfNotPerformed(input: I)(implicit executor: GobraExecutionContext): R

  /**
    * @param shouldPerform specifies whether this phase should be skipped in which case Left(Vector.empty) is returned
    * @param logTiming specifies whether executing time of this phase should be logged
    */
  def perform(input: I, shouldPerform: Boolean = true, logTiming: Boolean = true)(implicit executor: GobraExecutionContext): R = {
    if (shouldPerform) {
      val startMs = System.currentTimeMillis()
      val res = execute(input)(executor)
      if (logTiming) {
        logger.debug {
          val durationS = f"${(System.currentTimeMillis() - startMs) / 1000f}%.1f"
          s"$name phase done, took ${durationS}s"
        }
      }
      res
    } else {
      resultIfNotPerformed(input)
    }
  }
}

trait VerifierPhaseNonFinal[I, S] extends VerifierPhase[I, PhaseResult[S]] {
  override protected def resultIfNotPerformed(input: I)(implicit executor: GobraExecutionContext): PhaseResult[S] =
    EitherT.left(Vector.empty)
}

trait VerifierPhaseFinal[I] extends VerifierPhase[I, Future[VerifierResult]]
