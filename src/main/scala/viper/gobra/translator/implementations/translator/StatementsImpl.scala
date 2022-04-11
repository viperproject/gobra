// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.implementations.translator

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.{GoCallPreconditionReason, PreconditionError, Source}
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.translator.Statements
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.translator.util.{Comments, ViperUtil => vu}
import viper.gobra.util.Violation
import viper.silver.verifier.{errors => err}
import viper.silver.{ast => vpr}

class StatementsImpl extends Statements {

  var counter: Int = 0

  def count: Int = {counter += 1; counter}

  import viper.gobra.translator.util.ViperWriter.CodeLevel._

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = ()

  /** Clients can assume that the returned writer does not contain local variable definitions or written statements. */
  override def translate(x: in.Stmt)(ctx: Context): CodeWriter[vpr.Stmt] = {

    def result(res: CodeWriter[vpr.Stmt]): CodeWriter[vpr.Stmt] = seqn(res)

    val typEncodingOptRes = ctx.typeEncoding.statement(ctx).lift(x)
    if (typEncodingOptRes.isDefined) return result(typEncodingOptRes.get map (s => stmtComment(x, s)))


    val (pos, info, errT) = x.vprMeta

    def goS(s: in.Stmt): CodeWriter[vpr.Stmt] = translate(s)(ctx)
    def goA(a: in.Assertion): CodeWriter[vpr.Exp] = ctx.ass.translate(a)(ctx)
    def goE(e: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(e)(ctx)

    /**
      * Translates a go call to a function or method with pre-condition `pre` which is parameterized by
      * formal parameters `formalParams` and is instantiated with `args`
      */
    def translateGoCall(pre: Vector[in.Assertion],
                        formalParams: Vector[in.Parameter.In],
                        args: Vector[in.Expr]): Writer[vpr.Stmt] = {
      Violation.violation(
        args.length == formalParams.length,
        "number of passed arguments must match number of expected arguments"
      )

      for {
        vArgss <- sequence(args map goE)
        funcArgs <- sequence(formalParams map goE)
        substitutions = (funcArgs zip vArgss).toMap
        preCond <- sequence(pre map goA)
        preCondInstance = preCond.map{ _.replace(substitutions) }
        and = vu.bigAnd(preCondInstance)(pos, info, errT)
        exhale = vpr.Exhale(and)(pos, info, errT)
        _ <- errorT {
          case err.ExhaleFailed(Source(info), _, _) => PreconditionError(info).dueTo(GoCallPreconditionReason(info))
        }
      } yield exhale
    }

    def gatherBreakLabels(node: in.Node, label: Option[String], depthControl : Boolean = true): Vector[String] =
      label match {
        case None =>
          node match {
            case in.Break(None, l) => Vector(l)
            case _ : in.While => Vector.empty
            case n => n.subnodes.foldLeft(Vector[String]())((a : Vector[String], b : in.Node) => (a ++ gatherBreakLabels(b, None)))
          }
        case Some(labelString) =>
          node match {
            case in.Break(None, l) => if (depthControl) Vector(l) else Vector.empty
            case in.Break(Some(breakLabelString), l) => if (labelString.equals(breakLabelString)) Vector(l) else Vector.empty
            case n: in.While => n.subnodes.foldLeft(Vector[String]())((a : Vector[String], b : in.Node) => (a ++ gatherBreakLabels(b, label, false)))
            case n => n.subnodes.foldLeft(Vector[String]())((a : Vector[String], b : in.Node) => (a ++ gatherBreakLabels(b, label, depthControl)))
          }
      }

    def gatherContinueVariables(node: in.Node) : Vector[String] = {
      val (varsLabels, insideLabels) = gatherContinueVariablesHelper(node)
      varsLabels.filter((v : (String, String)) => !(insideLabels(v._2))).map(_._1)
    }

    def gatherContinueVariablesHelper(node: in.Node) : (Vector[(String, String)], Set[String]) = {
      node match {
        case in.Continue(Some(label), Some(varName), _) => (Vector((varName, label)), Set.empty)
        case n: in.While =>
          n.label match {
            case None => n.subnodes.foldLeft((Vector[(String, String)](), Set[String]()))(gatherContinueVariablesZip)
            case Some(labelName) =>
                n.subnodes.foldLeft((Vector[(String, String)](), Set(labelName)))(gatherContinueVariablesZip)
          }
        case n => n.subnodes.foldLeft((Vector[(String, String)](), Set[String]()))(gatherContinueVariablesZip)
      }
    }

    def gatherContinueVariablesZip(a : (Vector[(String, String)], Set[String]), b : in.Node) : (Vector[(String, String)], Set[String]) = {
      val (c, d) = gatherContinueVariablesHelper(b)
      (a._1 ++ c, a._2 | d)
    }

    def gatherLabeledContinueVars(node : in.Node, label : String) : Vector[String] =
      node match {
        case in.Continue(Some(l), Some(varName), _) =>
          if (l.equals(label)) Vector(varName) else Vector.empty
        case n => n.subnodes.foldLeft(Vector[String]())((a : Vector[String], b : in.Node) => a ++ gatherLabeledContinueVars(b, label))
      }
    

    val vprStmt: CodeWriter[vpr.Stmt] = x match {
      case in.Block(decls, stmts) =>
        val vDecls = decls map (blockDecl(_)(ctx))
        block{
          for {
            _ <- global(vDecls: _*)
            vBody <- sequence(stmts map goS)
          } yield vu.seqn(vBody)(pos, info, errT)
        }

      case in.Initialization(left) => ctx.typeEncoding.initialization(ctx)(left)

      case in.Seqn(stmts) => seqns(stmts map goS)

      case in.Label(id) =>
        unit(vpr.Label(id.name, Seq.empty)(pos, info, errT))

      case in.Continue(_, varName, invs) =>
        for {
          preCond <- sequence(invs map goA)
          assertions = preCond.map(x => vpr.Assert(x)(pos, info, errT))
          assignment = varName match {
            case None => Vector.empty
            case Some(name) => {
              val decl = vpr.LocalVarDecl(name, vpr.Bool)(pos, info, errT)
              Vector(vpr.LocalVarAssign(decl.localVar, vpr.BoolLit(true)(pos, info, errT))(pos, info, errT))
            }
          }
          cont = vu.seqn(assertions ++ assignment ++ Vector(vpr.Assume(vpr.BoolLit(false)(pos, info, errT))(pos, info, errT)))(pos, info, errT)
        } yield cont

      case in.Break(_, escLabel) =>
        unit(vpr.Goto(escLabel)(pos, info, errT))

      case in.If(cond, thn, els) =>
          for {
            c <- goE(cond)
            t <- goS(thn)
            e <- goS(els)
          } yield vpr.If(c, vu.toSeq(t), vu.toSeq(e))(pos, info, errT)

      case n@in.While(cond, invs, terminationMeasure, body, label) =>

        for {
          (cws, vCond) <- split(goE(cond))
          (iws, vInvs) = invs.map(ctx.ass.invariant(_)(ctx)).unzip
          cpre <- seqnUnit(cws)
          ipre <- seqnUnits(iws)

          vBody <- goS(body)

          continueVars = label match {
            case Some(l) => gatherLabeledContinueVars(n, l)
            case None => Vector[String]()
          }

          continueLocalVars = continueVars.map[vpr.LocalVarDecl](vpr.LocalVarDecl(_, vpr.Bool)(pos, info, errT))
          continueVarDecls = continueLocalVars.map((x : vpr.LocalVarDecl) => vpr.LocalVarAssign(x.localVar, vpr.BoolLit(false)(pos, info, errT))(pos, info, errT))

          vBody1 = vu.seqn(continueVarDecls ++ vBody.children.head.asInstanceOf[Vector[vpr.Stmt]])(pos, info, errT)

          cpost = vpr.If(vCond, vu.toSeq(cpre), vu.nop(pos, info, errT))(pos, info, errT)
          ipost = ipre

          measure <- option(terminationMeasure map ctx.measures.translateF(ctx))

          labelNames = gatherBreakLabels(body, label)
          labels = labelNames.map(x => vpr.Label(x, Vector.empty)(pos, info, errT))

          varNames = gatherContinueVariables(n)
          vars = varNames.map(vpr.LocalVarDecl(_, vpr.Bool)(pos, info, errT).localVar)

          ifstmt = Vector(vpr.If(vu.bigOr(vars)(pos, info, errT), vu.seqn(Vector(vpr.Assume(vpr.BoolLit(false)(pos, info, errT))(pos, info, errT)))(pos, info, errT), vu.nop(pos, info, errT))(pos, info, errT))

          wh = vu.seqn(Vector(
            cpre, ipre, vpr.While(vCond, vInvs ++ measure, vu.seqn(Vector(vBody1, cpost, ipost))(pos, info, errT))(pos, info, errT)
          ) ++ labels ++ ifstmt)(pos, info, errT)
        } yield wh

      case ass: in.SingleAss => ctx.typeEncoding.assignment(ctx)(ass.left, ass.right, ass)

      case in.FunctionCall(targets, func, args) =>
        for {
          vArgss <- sequence(args map goE)
          vTargets <- sequence(targets map goE)
          // vTargets can be field-accesses, but a MethodCall in Viper requires variables as targets.
          // Therefore, we introduce auxiliary variables and
          // add an assignment from the auxiliary variables to the actual targets
          (vUsedTargets, auxTargetsWithAssignment) = vTargets.map(viperTarget(_)(ctx)).unzip
          (auxTargetDecls, backAssignments) = auxTargetsWithAssignment.flatten.unzip
          _ <- local(auxTargetDecls: _*)
          _ <- write(vpr.MethodCall(func.name, vArgss, vUsedTargets)(pos, info, errT))
          assignToTargets = vpr.Seqn(backAssignments, Seq())(pos, info, errT)
        } yield assignToTargets

      case in.MethodCall(targets, recv, meth, args) =>
        for {
          vRecv <- goE(recv)
          vArgss <- sequence(args map goE)
          vTargets <- sequence(targets map goE)
          // vTargets can be field-accesses, but a MethodCall in Viper requires variables as targets.
          // Therefore, we introduce auxiliary variables and
          // add an assignment from the auxiliary variables to the actual targets
          (vUsedTargets, auxTargetsWithAssignment) = vTargets.map(viperTarget(_)(ctx)).unzip
          (auxTargetDecls, backAssignments) = auxTargetsWithAssignment.flatten.unzip
          _ <- local(auxTargetDecls: _*)
          _ <- write(vpr.MethodCall(meth.uniqueName, vRecv +: vArgss, vUsedTargets)(pos, info, errT))
          assignToTargets = vpr.Seqn(backAssignments, Seq())(pos, info, errT)
        } yield assignToTargets

      case in.GoFunctionCall(func, args) =>
        val funcM = ctx.lookup(func)
        translateGoCall(funcM.pres, funcM.args, args)
      case in.GoMethodCall(recv, meth, args) =>
        val methM = ctx.lookup(meth)
        translateGoCall(methM.pres, methM.receiver +: methM.args, recv +: args)
      case in.Assert(ass) => for {v <- goA(ass)} yield vpr.Assert(v)(pos, info, errT)
      case in.Assume(ass) => for {v <- goA(ass)} yield vpr.Assume(v)(pos, info, errT) // Assumes are later rewritten
      case in.Inhale(ass) => for {v <- goA(ass)} yield vpr.Inhale(v)(pos, info, errT)
      case in.Exhale(ass) => for {v <- goA(ass)} yield vpr.Exhale(v)(pos, info, errT)

      case fold: in.Fold => for {a <- ctx.ass.translate(fold.acc)(ctx) } yield vpr.Fold(a.asInstanceOf[vpr.PredicateAccessPredicate])(pos, info, errT)
      case unfold: in.Unfold => for { a <- ctx.ass.translate(unfold.acc)(ctx) } yield vpr.Unfold(a.asInstanceOf[vpr.PredicateAccessPredicate])(pos, info, errT)

      case in.PackageWand(wand, blockOpt) => for {
        w <- goA(wand)
        s <- sequence(blockOpt.toVector.map(goS))
      } yield vpr.Package(w.asInstanceOf[vpr.MagicWand], vu.seqn(s)(pos, info, errT))(pos, info, errT)

      case in.ApplyWand(wand) =>
        for {w <- goA(wand)} yield vpr.Apply(w.asInstanceOf[vpr.MagicWand])(pos, info, errT)

      case in.Return() => unit(vpr.Goto(Names.returnLabel)(pos, info, errT))

      case _ => Violation.violation(s"Statement $x did not match with any implemented case.")
    }

    result(vprStmt) map (s => stmtComment(x, s))
  }

  private def viperTarget(x: vpr.Exp)(ctx: Context): (vpr.LocalVar, Option[(vpr.LocalVarDecl, vpr.AbstractAssign)]) = {
    x match {
      case x: vpr.LocalVar => (x, None)
      case _ =>
        val decl = vpr.LocalVarDecl(ctx.freshNames.next(), x.typ)(x.pos, x.info, x.errT)
        val ass  = vu.valueAssign(x, decl.localVar)(x.pos, x.info, x.errT)
        (decl.localVar, Some((decl, ass)))
    }
  }

  def stmtComment(x: in.Stmt, res: vpr.Stmt): vpr.Stmt = {
    x match {
      case _: in.Seqn => res
      case _ => Comments.prependComment(x.formattedShort, res)
    }
  }

  def blockDecl(x: in.BlockDeclaration)(ctx: Context): vpr.Declaration = {
    x match {
      case x: in.BodyVar => ctx.typeEncoding.variable(ctx)(x)
      case l: in.LabelProxy =>
        val (pos, info, errT) = x.vprMeta
        vpr.Label(l.name, Seq.empty)(pos, info, errT)
    }
  }


}
