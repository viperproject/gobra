// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer, RichErrorMessage}
import viper.gobra.reporting.Source.RichViperNode
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{DefaultErrorBackTranslator, Source, VerificationError}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.MemberKindCompanion.{ErrorT, ReasonT}
import viper.gobra.util.Violation
import viper.silver.verifier.ErrorReason
import viper.silver.verifier.{errors => vprerr}

import scala.annotation.unused

object ViperWriter {

  sealed trait DataKind
  sealed trait DataSum

  case class DataContainer[T <: DataKind](data: Vector[T], remainder: Vector[DataKind]) {
    def combine(other: DataContainer[T]): DataContainer[T] =
      DataContainer(data ++ other.data, remainder ++ other.remainder)

    lazy val all: Vector[DataKind] = data ++ remainder

    def split: (DataContainer[T], Vector[DataKind]) =
      (DataContainer(data, Vector.empty), remainder)
  }

  object DataContainer {
    def empty[T <: DataKind]: DataContainer[T] = DataContainer(Vector.empty, Vector.empty)

    def combine[T <: DataKind](sums: Vector[DataContainer[T]]): DataContainer[T] = {
      val (datas, remainders) = sums.map(d => (d.data, d.remainder)).unzip
      DataContainer(datas.flatten, remainders.flatten)
    }
  }

  sealed trait DataKindCompanion[K <: DataKind, S <: DataSum] {
    def sum(data: Vector[K]): S
    def unsum(sum: S): Vector[K]
    def ownKind(point: DataKind): Option[K]

    def container(data: Vector[DataKind]): DataContainer[K] = {
      val (own, other) = data.foldLeft[(Vector[K], Vector[DataKind])]((Vector.empty, Vector.empty)){
        case ((ow, ot), e) => ownKind(e) match {
          case None    => (ow, e +: ot)
          case Some(k) => (k +: ow, ot)
        }
      }

      DataContainer(own, other)
    }

    def container(sum: S): DataContainer[K] = container(unsum(sum))
  }




  trait MemberKind extends DataKind

  case class MemberSum(
                        errorT: Vector[ErrorTransformer],
                        reasonT: Vector[ReasonTransformer]
                      ) extends DataSum

  case object MemberKindCompanion extends DataKindCompanion[MemberKind, MemberSum] {

    case class ErrorT(x: ErrorTransformer) extends MemberKind
    case class ReasonT(x: ReasonTransformer) extends MemberKind


    override def ownKind(point: DataKind): Option[MemberKind] = point match {
      case x: MemberKind => Some(x)
      case _ => None
    }

    override def sum(data: Vector[MemberKind]): MemberSum = {
      var errorT: Vector[ErrorTransformer] = Vector.empty
      var reasonT: Vector[ReasonTransformer] = Vector.empty

      data foreach {
        case ErrorT(x)  => errorT  = errorT :+ x
        case ReasonT(x) => reasonT = reasonT :+ x
      }

      MemberSum(errorT, reasonT)
    }

    override def unsum(s: MemberSum): Vector[MemberKind] =
      (s.errorT map ErrorT) ++ (s.reasonT map ReasonT)
  }

  trait CodeKind extends DataKind

  case class Global(x: vpr.Declaration) extends CodeKind
  case class Local(x: vpr.Declaration) extends CodeKind

  // to preserve the order of stmt-like output
  sealed trait Code extends CodeKind {
    def isPure: Boolean
  }

  case class Statement(x: vpr.Stmt) extends Code {
    override val isPure: Boolean = false
  }

  case class Assumption(x: vpr.Exp) extends Code {
    override val isPure: Boolean = false
  }

  case class Binding(v: vpr.LocalVar, e: vpr.Exp) extends Code {
    override val isPure: Boolean = true
  }

  case class CodeSum(
                      global: Vector[vpr.Declaration],
                      local: Vector[vpr.Declaration],
                      code: Vector[Code]
                    ) extends DataSum
  {
    def asStatement(body: vpr.Stmt): (vpr.Stmt, Vector[vpr.Declaration], Vector[DataKind]) = {
      val memberKinds: Vector[DataKind] = Vector.empty

      val codeStmts = code map {
        case Statement(x) => x
        case Binding(v, e) => vpr.LocalVarAssign(v, e)(e.pos, e.info, e.errT)
        case Assumption(x) => vpr.Assume(x)(x.pos, x.info, x.errT)
      }

      val stmt =
        if (codeStmts.isEmpty && local.isEmpty) body
        else vpr.Seqn(codeStmts :+ body, local)()
      (stmt, global, memberKinds)
    }

    def asStatement: (vpr.Stmt, Vector[vpr.Declaration], Vector[DataKind]) = {
      val memberKinds: Vector[DataKind] = Vector.empty

      val codeStmts = code map {
        case Statement(x) => x
        case Binding(v, e) => vpr.LocalVarAssign(v, e)(e.pos, e.info, e.errT)
        case Assumption(x) => vpr.Assume(x)(x.pos, x.info, x.errT)
      }

      val stmt = vpr.Seqn(codeStmts, local)()
      (stmt, global, memberKinds)
    }

    def asExpr(body: vpr.Exp, @unused ctx: Context): (vpr.Exp, Vector[vpr.Declaration], Vector[vpr.Declaration], Vector[DataKind]) = {
      val memberKinds: Vector[DataKind] = Vector.empty

      val codeExpr = code.foldRight(body){ case (x, w) =>
        x match {
          case _: Statement | _: Assumption => Violation.violation(s"expected pure term, but got $x")

          case Binding(v, e) =>
            vpr.Let(ViperUtil.toVarDecl(v), e, w)(w.pos, w.info, w.errT) // let lhs = rhs in e
        }
      }

      (codeExpr, local, global, memberKinds)
    }
  }

  case object CodeKindCompanion extends DataKindCompanion[CodeKind, CodeSum] {

    override def ownKind(point: DataKind): Option[CodeKind] = point match {
      case x: CodeKind => Some(x)
      case _ => None
    }

    override def sum(data: Vector[CodeKind]): CodeSum = {
      var global: Vector[vpr.Declaration] = Vector.empty
      var local: Vector[vpr.Declaration] = Vector.empty
      var code: Vector[Code] = Vector.empty

      data foreach {
        case Global(x) => global = global :+ x
        case Local(x)  => local = local :+ x
        case x: Code   => code = code :+ x
      }

      CodeSum(global, local, code)
    }

    override def unsum(s: CodeSum): Vector[CodeKind] =
      (s.global map Global) ++ (s.local map Local) ++ s.code
  }


  sealed class LeveledViperWriter[K <: DataKind, S <: DataSum](companion: DataKindCompanion[K, S]) {

    def unit[R](res: R): Writer[R] =
      Writer(DataContainer.empty, res)

    def create[R](sum: Vector[DataKind], res: R): Writer[R] =
      Writer(companion.container(sum), res)

    def sequence[R](ws: Vector[Writer[R]]): Writer[Vector[R]] = {
      val (sums, ress) = ws.map(w => (w.sum, w.res)).unzip
      Writer(DataContainer.combine(sums), ress)
    }

    def option[R](ws: Option[Writer[R]]): Writer[Option[R]] = ws match {
      case Some(w) => w.map(Some(_))
      case None => unit(None)
    }

    def move[R,Q](w: Writer[R])(f: S => R => (S, Q)): Writer[Q] = {
      val (codeSum, remainder, r) = w.execute
      val (newS, newR) = f(codeSum)(r)
      val newData = DataContainer(companion.unsum(newS), remainder)
      w.copy(newData, newR)
    }

    def withInfo[R <: vpr.Node](src: in.Node)(w: Writer[R]): Writer[R] = w.map(_.withInfo(src))
    def withDeepInfo[R <: vpr.Node](src: in.Node)(w: Writer[R]): Writer[R] = w.map(_.withDeepInfo(src))


    case class Writer[+R](sum: DataContainer[K], res: R) {

      def execute: (S, Vector[DataKind], R) = (companion.sum(sum.data), sum.remainder, res)

      def run: (Vector[DataKind], R) = (sum.all, res)

      @inline
      def foreach(fun: R => Unit): Unit = fun(res)

      @inline
      def map[Q](fun: R => Q): Writer[Q] = copy(sum, fun(res))

      def cut: (R, Writer[Unit]) = (res, map(_ => ()))

      @inline
      def flatMap[Q](fun: R => Writer[Q]): Writer[Q] = {
        val next = fun(res)
        copy(sum.combine(next.sum), next.res)
      }

      def withFilter(fun: R => Boolean): Writer[R] = {
        assert(fun(res))
        this
      }

      def copy[Q](newSum: DataContainer[K] = sum, newRes: Q = res): Writer[Q] = {
        Writer(newSum, newRes)
      }
    }
  }



  case object MemberLevel extends LeveledViperWriter[MemberKind, MemberSum](MemberKindCompanion) {

    def pure(w: CodeLevel.Writer[vpr.Exp])(ctx: Context): MemberLevel.Writer[vpr.Exp] = {
      val (codeSum, remainder, r) = w.execute
      require(codeSum.code.forall(_.isPure))

      val (newR, _, _, remainderAddition) = codeSum.asExpr(r, ctx)
      MemberLevel.create(remainderAddition ++ remainder, newR)
    }

    def split[R](w: CodeLevel.Writer[R]): Writer[(R, CodeLevel.Writer[Unit])] = {
      val (codeData, remainder) = w.sum.split
      create(remainder, (w.res, w.copy(codeData, ())))
    }

    def block(w: CodeLevel.Writer[vpr.Stmt]): Writer[vpr.Seqn] = {
      val (codeSum, remainder, r) = w.execute
      val (codeStmt, globals, remainderAddition) = codeSum.asStatement(r)
      val newR = vpr.Seqn(Vector(codeStmt), globals)(r.pos, r.info, r.errT)
      create(remainderAddition ++ remainder, newR)
    }

    def errorT(errTs: ErrorTransformer*): Writer[Unit] =
      create(errTs.toVector.map(ErrorT), ())

    def reasonR(reaTs: ReasonTransformer*): Writer[Unit] =
      create(reaTs.toVector.map(ReasonT), ())
  }

  type MemberWriter[+R] = MemberLevel.Writer[R]



  case object CodeLevel extends LeveledViperWriter[CodeKind, CodeSum](CodeKindCompanion) {

    def pure(w: Writer[vpr.Exp])(ctx: Context): Writer[vpr.Exp] = {
      val (codeSum, remainder, r) = w.execute
      require(codeSum.code.forall(_.isPure))
      val (codeStmt, _, _, remainderAddition) = codeSum.asExpr(r, ctx)
      val newData = DataContainer(Vector.empty[CodeKind], remainderAddition ++ remainder)
      w.copy(newData, codeStmt)
    }

    def seqns(ws: Vector[Writer[vpr.Stmt]]): Writer[vpr.Seqn] =
      sequence(ws.map(seqn)).map(vpr.Seqn(_, Vector.empty)())

    def seqn(w: Writer[vpr.Stmt]): Writer[vpr.Stmt] = {
      val (codeSum, remainder, r) = w.execute
      val (codeStmt, _, remainderAddition) = codeSum.asStatement(r)
      val newSum = codeSum.copy(local = Vector.empty, code = Vector.empty)
      val newData = DataContainer(CodeKindCompanion.unsum(newSum), remainderAddition ++ remainder)
      w.copy(newData, codeStmt)
    }

    def seqnUnits(ws: Vector[Writer[Unit]]): Writer[vpr.Seqn] =
      sequence(ws.map(seqnUnit)).map(vpr.Seqn(_, Vector.empty)())

    def seqnUnit(w: Writer[Unit]): Writer[vpr.Stmt] = {
      val (codeSum, remainder, _) = w.execute
      val (codeStmt, _, remainderAddition) = codeSum.asStatement
      val newSum = codeSum.copy(local = Vector.empty, code = Vector.empty)
      val newData = DataContainer(CodeKindCompanion.unsum(newSum), remainderAddition ++ remainder)
      w.copy(newData, codeStmt)
    }

    def block(w: Writer[vpr.Stmt]): Writer[vpr.Seqn] = {
      val (codeSum, remainder, r) = w.execute
      val (codeStmt, global, remainderAddition) = codeSum.asStatement(r)
      val newR = vpr.Seqn(Vector(codeStmt), global)(r.pos, r.info, r.errT)
      val newData = DataContainer(Vector.empty[CodeKind], remainderAddition ++ remainder)
      w.copy(newData, newR)
    }

    def split[R](w: Writer[R]): Writer[(Writer[Unit], R)] = {
      val (codeData, remainder) = w.sum.split
      create(remainder, (w.copy(codeData, ()), w.res))
    }

    /* Emits Viper statements. */
    def write(stmts: vpr.Stmt*): Writer[Unit] =
      create(stmts.toVector.map(Statement), ())

    /* Emits Viper statements. */
    def assume(cond: vpr.Exp*): Writer[Unit] =
      create(cond.toVector.map(Assumption), ())

    /* Can be used in expressions. */
    def bind(lhs: vpr.LocalVar, rhs: vpr.Exp): Writer[Unit] =
      create(Vector(Binding(lhs, rhs)), ())

    /* Can be used in expressions. */
    def assert(cond: vpr.Exp, exp: vpr.Exp, reasonT: (Source.Verifier.Info, ErrorReason) => VerificationError)(ctx: Context): Writer[vpr.Exp] = {
      // In the future, this might do something more sophisticated
      val (res, errT) = ctx.condition.assert(cond, exp, reasonT)
      errorT(errT).map(_ => res)
    }

    /* Emits Viper statements. */
    def assert(cond: vpr.Exp, reasonT: (Source.Verifier.Info, ErrorReason) => VerificationError): Writer[Unit] = {
      val res = vpr.Assert(cond)(cond.pos, cond.info, cond.errT)
      for {
        _ <- write(res)
        _ <- errorT({
          case e@ vprerr.AssertFailed(Source(info), reason, _) if e causedBy res =>
            reasonT(info, reason)
        })
      } yield ()
    }

    /* Emits Viper statements. */
    def assertWithDefaultReason(cond: vpr.Exp, error: Source.Verifier.Info => VerificationError): Writer[Unit] = {
      val res = vpr.Assert(cond)(cond.pos, cond.info, cond.errT)
      for {
        _ <- write(res)
        _ <- errorT({
          case e@ vprerr.AssertFailed(Source(info), reason, _) if e causedBy res =>
            error(info) dueTo DefaultErrorBackTranslator.defaultTranslate(reason)
        })
      } yield ()
    }

    /* Emits Viper statements. */
    def exhale(cond: vpr.Exp, reasonT: (Source.Verifier.Info, ErrorReason) => VerificationError): Writer[Unit] = {
      val res = vpr.Exhale(cond)(cond.pos, cond.info, cond.errT)
      for {
        _ <- write(res)
        _ <- errorT({
          case e@ vprerr.ExhaleFailed(Source(info), reason, _) if e causedBy res =>
            reasonT(info, reason)
        })
      } yield ()
    }

    /* Emits Viper statements. */
    def exhaleWithDefaultReason(cond: vpr.Exp, error: Source.Verifier.Info => VerificationError): Writer[Unit] = {
      val res = vpr.Exhale(cond)(cond.pos, cond.info, cond.errT)
      for {
        _ <- write(res)
        _ <- errorT({
          case e@ vprerr.ExhaleFailed(Source(info), reason, _) if e causedBy res =>
            error(info) dueTo DefaultErrorBackTranslator.defaultTranslate(reason)
        })
      } yield ()
    }

    /* Can be used in expressions. */
    def local(locals: vpr.Declaration*): Writer[Unit] =
      create(locals.toVector.map(Local), ())

    /* Can be used in expressions. */
    def global(globals: vpr.Declaration*): Writer[Unit] =
      create(globals.toVector.map(Global), ())

    /* Can be used in expressions. */
    def errorT(errTs: ErrorTransformer*): Writer[Unit] =
      create(errTs.toVector.map(ErrorT), ())

    /* Can be used in expressions. */
    def reasonR(reaTs: ReasonTransformer*): Writer[Unit] =
      create(reaTs.toVector.map(ReasonT), ())

    /* Can be used in expressions. */
    def copyResult(r: vpr.Exp): CodeWriter[vpr.LocalVar] = {
      val z = vpr.LocalVar(Names.freshName, r.typ)(r.pos, r.info, r.errT)
      for {
        _ <- local(vu.toVarDecl(z))
        _ <- bind(z, r)
      } yield z
    }
  }

  type CodeWriter[+R] = CodeLevel.Writer[R]
}
