// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.util
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source

class DesugarWriter {

  def unit[R](res: R): Writer[R] = Writer(Vector.empty, Vector.empty, res)
  def create[R](stmts: Vector[in.Stmt], decls: Vector[in.BlockDeclaration], res: R) = Writer(stmts, decls, res)

  def sequence[R](ws: Vector[Writer[R]]): Writer[Vector[R]] = {
    val (stmts, declss, results) = ws.map(_.run).unzip3
    Writer(stmts.flatten, declss.flatten, results)
  }

  def option[R](ws: Option[Writer[R]]): Writer[Option[R]] = ws match {
    case Some(value) => value.map(Some(_))
    case None => unit(None)
  }

  def write(xs: in.Stmt*): Writer[Unit] = Writer(xs.toVector, Vector.empty, ())
  def declare(xs: in.BlockDeclaration*): Writer[Unit] = Writer(Vector.empty, xs.toVector, ())

  def seqn(w: Writer[in.Stmt]): Writer[in.Stmt] = {
    if (w.stmts.isEmpty) w // do not create in.Seqn if not necessary
    else create(Vector.empty, w.decls, in.Seqn(w.stmts :+ w.res)(w.res.info))
  }

  def block(w: Writer[in.Stmt]): in.Stmt = {
    if (w.decls.isEmpty && w.stmts.isEmpty) w.res // do not create in.Block if not necessary
    else in.Block(w.decls, w.stmts :+ w.res)(w.res.info)
  }

  def blockV(w: Writer[Vector[in.Stmt]])(info: Source.Parser.Info): in.Stmt = {
    in.Block(w.decls, w.stmts ++ w.res)(info)
  }

  def prelude[R](w: Writer[R]): Writer[(Vector[in.Stmt], R)] =
    create(Vector.empty, w.decls, (w.stmts, w.res))

  case class Writer[+R](stmts: Vector[in.Stmt], decls: Vector[in.BlockDeclaration], res: R) {

    def map[Q](fun: R => Q): Writer[Q] = Writer(stmts, decls, fun(res))

    def star[Q](fun: Writer[R => Q]): Writer[Q] =
      Writer(stmts ++ fun.stmts, decls ++ fun.decls, fun.res(res))

    def flatMap[Q](fun: R => Writer[Q]): Writer[Q] = {
      val w = fun(res)
      Writer(stmts ++ w.stmts, decls ++ w.decls, w.res)
    }

    def withFilter(fun: R => Boolean): Writer[R] = {
      assert(fun(res))
      this
    }

    def run: (Vector[in.Stmt], Vector[in.BlockDeclaration], R) = (stmts, decls, res)
    def written: Vector[in.Stmt] = stmts
    def declared: Vector[in.BlockDeclaration] = decls
  }
}
