// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.library.tuples

import viper.gobra.translator.transformers.SimpleViperTransformer
import viper.silver.ast.utility.rewriter.Traverse
import viper.silver.{ast => vpr}

trait TupleInlineTransformer extends SimpleViperTransformer {

  override def transform(program: vpr.Program): vpr.Program = {
    program.transform((transformation _).unlift, Traverse.TopDown)
  }

  def isInlinedType(name: String): Boolean
  def getterIdx(name: String): Option[Int]
  def getApp(e: vpr.Exp, idx: Int, size: Int)(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp
  def createApp(args: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.Exp

  def inlineMap(xs: Seq[vpr.Declaration]): Map[String, Seq[vpr.LocalVarDecl]] = {
    def aux(x: vpr.Declaration): Map[String, Seq[vpr.LocalVarDecl]] = x match {
      case x@vpr.LocalVarDecl(_, t@vpr.DomainType(name, _)) if isInlinedType(name) =>
        val (pos, info, errT) = x.meta
        val types = t.typeParameters.map(te => t.partialTypVarsMap.getOrElse(te, te))
        val shallowInline = types.zipWithIndex.map { case (te, idx) => vpr.LocalVarDecl(s"${x.name}_$idx", te)(pos, info, errT) }
        Map(x.name -> shallowInline) ++ inlineMap(shallowInline)

      case _ => Map.empty
    }
    xs.foldLeft(Map.empty[String, Seq[vpr.LocalVarDecl]]) { case (l, r) => l ++ aux(r) }
  }

  def inlinedVarDecls(x: vpr.Declaration)(inlineMap: Map[String, Seq[vpr.LocalVarDecl]]): Seq[vpr.Declaration] = {
    inlineMap.get(x.name).fold(Seq(x))(_.flatMap(inlinedVarDecls(_)(inlineMap)))
  }

  def transformRead(x: vpr.LocalVar)(inlineMap: Map[String, Seq[vpr.LocalVarDecl]]): vpr.Exp = {
    inlineMap.get(x.name) match {
      case None => x
      case Some(vars) => createApp(vars.toVector map (x => transformRead(x.localVar)(inlineMap)))(x.pos, x.info, x.errT)
    }
  }

  def transformAssignment(s: vpr.LocalVarAssign)(inlineMap: Map[String, Seq[vpr.LocalVarDecl]]): vpr.Stmt = {
    inlineMap.get(s.lhs.name) match {
      case None => s
      case Some(vars) =>
        vpr.Seqn(
          vars.zipWithIndex.map { case (x, idx) =>
            val assignment = vpr.LocalVarAssign(
              x.localVar,
              getApp(s.rhs, idx, vars.size)(s.pos, s.info, s.errT)
            )(s.pos, s.info, s.errT)
            transformAssignment(assignment)(inlineMap)
          }, Seq.empty
        )(s.pos, s.info, s.errT)
    }
  }

  def transformGet(e: vpr.Exp)(inlineMap: Map[String, Seq[vpr.LocalVarDecl]]): vpr.Exp = {
    val getter = (getterIdx _).unlift
    e match {
      case vpr.DomainFuncApp(getter(idx), Seq(arg), _) => transformGet(arg)(inlineMap) match {
        case x: vpr.LocalVar if inlineMap.contains(x.name) => inlineMap(x.name)(idx).localVar
        case _ => e
      }
      case _ => e
    }
  }

  def transformation(node: vpr.Node): Option[vpr.Stmt] = node match {
    case seqn: vpr.Seqn =>
      val m = inlineMap(seqn.scopedDecls)

      if (m.isEmpty) None
      else {
        val transformedSS = seqn.ss.map(s => s.transform({
          case e@vpr.DomainFuncApp(name, _, _) if getterIdx(name).isDefined => transformGet(e)(m)
          case x: vpr.LocalVar if m.contains(x.name) => transformRead(x)(m)
          case s: vpr.LocalVarAssign if m.contains(s.lhs.name) => transformAssignment(s)(m)
        }, Traverse.TopDown))

        val transformedScopedDecls = seqn.scopedDecls.flatMap(inlinedVarDecls(_)(m))

        Some(vpr.Seqn(transformedSS, transformedScopedDecls)(seqn.pos, seqn.info, seqn.errT))
      }

    case _ => None
  }
}
