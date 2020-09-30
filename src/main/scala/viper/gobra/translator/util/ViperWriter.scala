// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.util

import viper.gobra.reporting.BackTranslator.{ErrorTransformer, ReasonTransformer}
import viper.gobra.reporting.Source.RichViperNode
import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.MemberKindCompanion.{ErrorT, ReasonT}
import viper.gobra.util.Violation

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

  case class Assertion(x: vpr.Exp) extends Code {
    override val isPure: Boolean = true
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
    def asStatement(body: vpr.Stmt): (vpr.Seqn, Vector[vpr.Declaration]) = {
      val codeStmts = code map {
        case Statement(x) => x
        case Binding(v, e) => vpr.LocalVarAssign(v, e)(e.pos, e.info, e.errT)
        case Assertion(x) => vpr.Assert(x)(x.pos, x.info, x.errT)
        case Assumption(x) => vpr.Assume(x)(x.pos, x.info, x.errT)
      }

      val stmt = vpr.Seqn(codeStmts :+ body, local)()
      (stmt, global)
    }

    def asStatement: (vpr.Seqn, Vector[vpr.Declaration]) = {
      val codeStmts = code map {
        case Statement(x) => x
        case Binding(v, e) => vpr.LocalVarAssign(v, e)(e.pos, e.info, e.errT)
        case Assertion(x) => vpr.Assert(x)(x.pos, x.info, x.errT)
        case Assumption(x) => vpr.Assume(x)(x.pos, x.info, x.errT)
      }

      val stmt = vpr.Seqn(codeStmts, local)()
      (stmt, global)
    }

    def asExpr(body: vpr.Exp, ctx: Context): (vpr.Exp, Vector[vpr.Declaration], Vector[vpr.Declaration]) = {

      val codeExpr = code.foldRight(body){ case (x, w) =>
        x match {
          case _: Statement | _: Assumption => Violation.violation(s"expected pure term, but got $x")

          case Binding(v, e) =>
            vpr.Let(ViperUtil.toVarDecl(v), e, w)(w.pos, w.info, w.errT) // let lhs = rhs in e

          case Assertion(a) =>
            vpr.And(ctx.condition.assert(a), w)(w.pos, w.info, w.errT) // Maybe use inhaleExhaleExp instead
        }
      }

      (codeExpr, local, global)
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

      val newR = codeSum.asExpr(r, ctx)._1
      MemberLevel.create(remainder, newR)
    }

    def split[R](w: CodeLevel.Writer[R]): Writer[(R, CodeLevel.Writer[Unit])] = {
      val (codeData, remainder) = w.sum.split
      create(remainder, (w.res, w.copy(codeData, ())))
    }

    def block(w: CodeLevel.Writer[vpr.Stmt]): Writer[vpr.Seqn] = {
      val (codeSum, remainder, r) = w.execute
      val (codeStmt, globals) = codeSum.asStatement(r)
      val newR = vpr.Seqn(Vector(codeStmt), globals)(r.pos, r.info, r.errT)
      create(remainder, newR)
    }

    def errorT(errTs: ErrorTransformer*): Writer[Unit] =
      create(errTs.toVector.map(ErrorT), ())

    def reasonR(reaTs: ReasonTransformer*): Writer[Unit] =
      create(reaTs.toVector.map(ReasonT), ())
  }

  type MemberWriter[R] = MemberLevel.Writer[R]



  case object CodeLevel extends LeveledViperWriter[CodeKind, CodeSum](CodeKindCompanion) {

    def pure(w: Writer[vpr.Exp])(ctx: Context): Writer[vpr.Exp] = {
      move(w){ s => r =>
        require(s.code.forall(_.isPure))
        val newSum = s.copy(global = Vector.empty, local = Vector.empty, code = Vector.empty)
        val newR = s.asExpr(r, ctx)._1
        (newSum, newR)
      }
    }

    def seqns(ws: Vector[Writer[vpr.Stmt]]): Writer[vpr.Seqn] =
      sequence(ws.map(seqn)).map(vpr.Seqn(_, Vector.empty)())

    def seqn(w: Writer[vpr.Stmt]): Writer[vpr.Seqn] =
      move(w){ s => r =>
        val newSum = s.copy(local = Vector.empty, code = Vector.empty)
        val code = s.asStatement(r)._1
        (newSum, code)
      }

    def seqnUnits(ws: Vector[Writer[Unit]], assume: Boolean = false): Writer[vpr.Seqn] =
      sequence(ws.map(seqnUnit)).map(vpr.Seqn(_, Vector.empty)())

    def seqnUnit(w: Writer[Unit]): Writer[vpr.Seqn] =
      move(w){ s => _ =>
        val newSum = s.copy(local = Vector.empty, code = Vector.empty)
        val code = s.asStatement._1
        (newSum, code)
      }

    def block(w: Writer[vpr.Stmt]): Writer[vpr.Seqn] =
      move(w){ s => r =>
        val newSum = s.copy(global = Vector.empty, local = Vector.empty, code = Vector.empty)
        val (code, global) = s.asStatement(r)
        (newSum, vpr.Seqn(Vector(code), global)(r.pos, r.info, r.errT))
      }

    def split[R](w: Writer[R]): Writer[(Writer[Unit], R)] = {
      val (codeData, remainder) = w.sum.split
      create(remainder, (w.copy(codeData, ()), w.res))
    }

    def write(stmts: vpr.Stmt*): Writer[Unit] =
      create(stmts.toVector.map(Statement), ())

    def assume(cond: vpr.Exp*): Writer[Unit] =
      create(cond.toVector.map(Assumption), ())

    def bind(lhs: vpr.LocalVar, rhs: vpr.Exp): Writer[Unit] =
      create(Vector(Binding(lhs, rhs)), ())

    def assert(cond: vpr.Exp*): Writer[Unit] =
      create(cond.toVector.map(Assertion), ())

    def local(locals: vpr.Declaration*): Writer[Unit] =
      create(locals.toVector.map(Local), ())

    def global(globals: vpr.Declaration*): Writer[Unit] =
      create(globals.toVector.map(Global), ())

    def errorT(errTs: ErrorTransformer*): Writer[Unit] =
      create(errTs.toVector.map(ErrorT), ())

    def reasonR(reaTs: ReasonTransformer*): Writer[Unit] =
      create(reaTs.toVector.map(ReasonT), ())

    def copyResult(r: vpr.Exp): CodeWriter[vpr.LocalVar] = {
      val z = vpr.LocalVar(Names.freshName, r.typ)(r.pos, r.info, r.errT)
      for {
        _ <- local(vu.toVarDecl(z))
        _ <- bind(z, r)
      } yield z
    }
  }

  type CodeWriter[R] = CodeLevel.Writer[R]


  /* ** Utilities */

  /**
    * Yields the first `vpr.Position` in `xs` that is
    * not equal to `vpr.NoPosition`. If no such element exists,
    * then `vpr.NoPosition` is returned instead.
    */
  private def vprPosition(xs : Vector[vpr.Exp]) : vpr.Position = xs match {
    case Vector() => vpr.NoPosition
    case e +: es => e.pos match {
      case vpr.NoPosition => vprPosition(es)
      case pos => pos
    }
  }

  /**
    * Yields the first `vpr.Info` in `xs` that is
    * not equal to `vpr.NoInfo`. If no such element exists,
    * then `vpr.NoPosition` is returned instead.
    */
  private def vprInfo(xs : Vector[vpr.Exp]) : vpr.Info = xs match {
    case Vector() => vpr.NoInfo
    case e +: es => e.info match {
      case vpr.NoInfo => vprInfo(es)
      case info => info
    }
  }

  /**
    * Yields the first `vpr.ErrorTrafo` in `xs` that is
    * not equal to `vpr.NoTrafos`. If no such element exists,
    * then `vpr.NoPosition` is returned instead.
    */
  private def vprErrorTrafo(xs : Vector[vpr.Exp]) : vpr.ErrorTrafo = xs match {
    case Vector() => vpr.NoTrafos
    case e +: es => e.errT match {
      case vpr.NoTrafos => vprErrorTrafo(es)
      case trafo => trafo
    }
  }
}
