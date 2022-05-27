// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.outlines

import viper.gobra.translator.util.ViperUtil
import viper.silver.ast.Member
import viper.silver.{ast => vpr}

class OutlinesImpl extends Outlines {

  override def finalize(addMemberFn: Member => Unit): Unit = {
    generatedMembers foreach addMemberFn
  }
  private var generatedMembers: List[vpr.Member] = List.empty

  /**
    * Moves code `body` into a generated method with name `name`, preconditions `pres`, and postconditions `pros`.
    * Returns a call to the generated method with the appropriate arguments and results.
    *
    * outline
    *  requires PRE
    *  ensures  POST
    * (
    *   BODY
    * )
    *
    * is transformed to
    *
    * results := name(arguments)
    *
    * with
    *
    * method name(arguments') returns (results')
    *   requires PRE[arguments -> arguments']
    *   ensures  POST[results -> results'][arguments -> arguments'][ old[l] -> old ]
    * {
    *    var arguments
    *    arguments := arguments'
    *    BODY[ old[l] -> old ]
    *    results' := results
    * }
    *
    * where
    *   arguments := free(BODY) + free(PRE) + (free(POST) - modified(BODY))   // variables used in body and spec
    *   results   := modified(BODY) intersect free(Body)                      // arguments modified in body
    *
    *
    *   free(n) := free variables in n
    *   modified(n) := variables modified in n
    *
    */
  override def outline(
                        name: String,
                        pres: Vector[vpr.Exp],
                        posts: Vector[vpr.Exp],
                        body: vpr.Stmt,
                        trusted: Boolean,
                      )(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Stmt = {

    val (arguments, results) = {
      val bodyFree = body.undeclLocalVars.toSet
      val preFree = pres
        .map(e => vpr.utility.Expressions.freeVariables(e).collect{ case x: vpr.LocalVar => x })
        .foldLeft(Set.empty[vpr.LocalVar]){ case (l,r) => l ++ r }
      val postFree = posts
        .map(e => vpr.utility.Expressions.freeVariables(e).collect{ case x: vpr.LocalVar => x })
        .foldLeft(Set.empty[vpr.LocalVar]){ case (l,r) => l ++ r }
      val modified = body.writtenVars.toSet
      ((bodyFree ++ preFree ++ (postFree diff modified)).toVector, (modified intersect bodyFree).toVector)
    }

    generatedMembers ::= {
      val formals = arguments.map(v => v.copy(name = s"${v.name}$$in")(v.pos, v.info, v.errT))
      val returns = results.map(v => v.copy(name = s"${v.name}$$out")(v.pos, v.info, v.errT))
      val actualBody = if (!trusted) {
        val prelude = (arguments zip formals).map{ case (l, r) => vpr.LocalVarAssign(l, r)(l.pos, l.info, l.errT) }
        val ending = (returns zip results).map{ case (l, r) => vpr.LocalVarAssign(l, r)(l.pos, l.info, l.errT) }
        val tb = body.transform{ case lold: vpr.LabelledOld => vpr.Old(lold.exp)(lold.pos, lold.info, lold.errT) }
        Some(vpr.Seqn(prelude ++ (tb +: ending), arguments map ViperUtil.toVarDecl)(body.pos, body.info, body.errT))
      } else None
      import vpr.utility.Expressions.{instantiateVariables => subst}
      val actualPres = pres.map(e => subst(e, arguments, formals))
      val actualPosts = posts.map(e => subst(subst(e, results, returns), arguments, formals).transform{
        case lold: vpr.LabelledOld => vpr.Old(lold.exp)(lold.pos, lold.info, lold.errT)
      })

      vpr.Method(
        name = name,
        formalArgs = formals map ViperUtil.toVarDecl,
        formalReturns = returns map ViperUtil.toVarDecl,
        pres = actualPres,
        posts = actualPosts,
        body = actualBody,
      )(pos, info, errT)
    }

    vpr.MethodCall(methodName = name, args = arguments, targets = results)(pos, info, errT)
  }

  /**
    * Generates method with name `name`, preconditions `pres`, and postconditions `pros`.
    * The generated method takes as arguments `arguments`. `modifies` specifies which of the arguments are modified.
    * Returns a call to the generated method with the appropriate arguments and results.
    */
  def outlineWithoutBody(
                          name: String,
                          pres: Vector[vpr.Exp],
                          posts: Vector[vpr.Exp],
                          arguments: Vector[vpr.LocalVar],
                          modifies:  Vector[vpr.LocalVar],
                        )(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Stmt = {
    val results = modifies

    generatedMembers ::= {
      val returns = results.map(v => v.copy(name = s"${v.name}$$out")(v.pos, v.info, v.errT))
      import vpr.utility.Expressions.{instantiateVariables => subst}
      val actualPosts = posts.map(e => subst(e, results, returns).transform{
        case lold: vpr.LabelledOld => vpr.Old(lold.exp)(lold.pos, lold.info, lold.errT)
      })

      vpr.Method(
        name = name,
        formalArgs = arguments map ViperUtil.toVarDecl,
        formalReturns = returns map ViperUtil.toVarDecl,
        pres = pres,
        posts = actualPosts,
        body = None,
      )(pos, info, errT)
    }

    vpr.MethodCall(methodName = name, args = arguments, targets = results)(pos, info, errT)
  }

}
