package viper.gobra.util
//import cats.{Monad, Traverse}
//import cats.data.Writer
import viper.gobra.reporting.Source.RichViperNode

class Writer[A, +R](val out: Vector[A], val res: R) {

  def map[Q](fun: R => Q): Writer[A, Q] = new Writer(out, fun(res))

  def star[Q](fun: Writer[A, R => Q]): Writer[A, Q] = new Writer(out ++ fun.out, fun.res(res))

  def flatMap[Q](fun: R => Writer[A, Q]): Writer[A, Q] = {
    val w = fun(res)
    new Writer(out ++ w.out, w.res)
  }

  def run: (Vector[A], R) = (out, res)
  def written: Vector[A] = out
}

import viper.silver.{ast => vpr}
import viper.gobra.ast.{internal => in}

object ViperWriter {

  class StmtWriter[+R](val global: Vector[vpr.LocalVarDecl], val res: R) {

    def map[Q](fun: R => Q): StmtWriter[Q] = new StmtWriter(global, fun(res))

    def star[Q](fun: StmtWriter[R => Q]): StmtWriter[Q] =
      new StmtWriter(global ++ fun.global, fun.res(res))

    def flatMap[Q](fun: R => StmtWriter[Q]): StmtWriter[Q] = {
      val w = fun(res)
      new StmtWriter(global ++ w.global, w.res)
    }

    def run: (Vector[vpr.LocalVarDecl], R) = (global, res)

    def open: ExprWriter[R] = new ExprWriter[R](Vector.empty, global, Vector.empty, res)
  }

  object StmtWriter {
    def unit[R](res: R): StmtWriter[R] = new StmtWriter[R](Vector.empty, res)

    def sequence[R](ws: Vector[StmtWriter[R]]): StmtWriter[Vector[R]] = {
      val (outs, results) = ws.map(_.run).unzip
      new StmtWriter[Vector[R]](outs.flatten, results)
    }

    implicit class SourcedStmtWriter[R <: vpr.Node](w: StmtWriter[R]) {
      def withInfo(source: in.Node): StmtWriter[R] = w.map(_.withInfo(source))
    }
  }

  class ExprWriter[+R](
                        val local:   Vector[vpr.LocalVarDecl], // variables required for current entity
                        val global:  Vector[vpr.LocalVarDecl], // variables that might be used afterwards (managed in context)
                        val written: Vector[vpr.Stmt],
                        val res: R
                      ) {

    def map[Q](fun: R => Q): ExprWriter[Q] = new ExprWriter(local, global, written, fun(res))

    def star[Q](fun: ExprWriter[R => Q]): ExprWriter[Q] =
      new ExprWriter(local ++ fun.local, global ++ fun.global, written ++ fun.written, fun.res(res))

    def flatMap[Q](fun: R => ExprWriter[Q]): ExprWriter[Q] = {
      val w = fun(res)
      new ExprWriter(local ++ w.local, global ++ w.global, written ++ w.written, w.res)
    }

    def isEmpty: Boolean = local.isEmpty && global.isEmpty && written.isEmpty

    def run: (Vector[vpr.LocalVarDecl], Vector[vpr.LocalVarDecl], Vector[vpr.Stmt], R) = (local, global, written, res)

    // TODO: maybe closing: Stmt => R => X
    def close(implicit closing: ExprWriter.Closing[R]): StmtWriter[vpr.Stmt] ={
      val v = closing(res)
      new StmtWriter(global, vpr.Seqn(written :+ v, local)(v.pos))
    }

    def pack: (vpr.Stmt, StmtWriter[R]) =
      (
        vpr.Seqn(written, local)(),
        new StmtWriter(global, res)
      )

  }

  object ExprWriter {

    class Closing[-R](toStmt: R => vpr.Stmt) {
      def apply(v: R): vpr.Stmt = toStmt(v)
    }

    implicit val stmtClosing: Closing[vpr.Stmt] = new Closing(identity)

    def unit[R](res: R): ExprWriter[R] = new ExprWriter(Vector.empty, Vector.empty, Vector.empty, res)

//    def complete[R <: vpr.Stmt](w: ExprWriter[R])(src: in.Source): StmtWriter[vpr.Stmt] =
//      new StmtWriter[vpr.Stmt](w.global, vpr.Seqn(w.written :+ w.res, w.local)(src.vprSrc))

    implicit class SourcedExprWriter[R <: vpr.Node](w: ExprWriter[R]) {
      def withInfo(source: in.Node): ExprWriter[R] = w.map(_.withInfo(source))
    }

    def sequence[R](ws: Vector[ExprWriter[R]]): ExprWriter[Vector[R]] = {
      val (locals, globals, writtens, results) = ws.map(_.run)
        .foldLeft(
          (Vector.empty[vpr.LocalVarDecl], Vector.empty[vpr.LocalVarDecl], Vector.empty[vpr.Stmt], Vector.empty[R])
        ){
          case ((ls, gs, ss, rs),(l, g, s, r)) => (ls ++ l, gs ++ g, ss ++ s, rs :+ r)
        }

      new ExprWriter(locals, globals, writtens, results)
    }
  }
}




class WriterUtil[W] {

//  type WriterForm[R] = Writer[W, R]

  object Syntax {

    def unit[R](res: R): Writer[W, R] = new Writer(Vector.empty[W], res)

    def sequence[R](ws: Vector[Writer[W, R]]): Writer[W, Vector[R]] = {
      val (outs, results) = ws.map(_.run).unzip
      new Writer(outs.flatten, results)
    }

    def stack[Z <: W](w: Writer[W, Z]): Vector[W] = w.out :+ w.res

    implicit class WriterFunctor[R, Q](fun: R => Q) {
      def <#>(w: Writer[W, R]): Writer[W, Q] = w.map(fun)
    }

    implicit class WriterApplicable[R, Q](fun: Writer[W, R => Q]) {
      def <*>(w: Writer[W, R]): Writer[W, Q] = w.star(fun)
    }

    implicit class WriterCombiner[Z <: W](w: Writer[W, Z]) {
      def stack: Vector[W] = w.out :+ w.res
    }

//    import cats.instances.vector._
//
//    val monad: Monad[WriterForm] = Monad[WriterForm]
//
//    def unit[R](x: R): WriterForm[R] = monad.pure(x)
//
//    def sequence[R](ws: Vector[WriterForm[R]]): WriterForm[Vector[R]] = Traverse[Vector].sequence(ws)
//
//    implicit class WriterFunctor[R, Q](f: R => Q) {
//      def <#>(b: WriterForm[R]): WriterForm[Q] = b map f
//    }
//
//    implicit class WriterApplicative[R, Q](f: WriterForm[R => Q]) {
//      def <*>(b: WriterForm[R]): WriterForm[Q] = monad.ap(f)(b)
//      def push: R => WriterForm[Q] = (x: R) => monad.map(f)(_(x))
//    }
//
//    implicit class WriterStackCombiner[+Q](w: Writer[Vector[Q], Q]) {
//      def stack: Vector[Q] = { val (xs, x) = w.run; xs :+ x }
//    }
  }


}
