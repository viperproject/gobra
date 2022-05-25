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
    * var leaking;
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
    *   arguments := free(BODY) + (free(POST) - declared(BODY))    // variables used in body and spec
    *   results := (modified(BODY) intersect arguments) + leaking  // arguments modified in body
    *   leaking := free(POST) intersect declared(BODY)             // variables declared in body that should be returned
    *
    *   free(n) := free variables in n
    *   modified(n) := variables modified in n
    *   declared(n) := variables declared in n
    *
    */
  override def outline(
                        name: String,
                        pres: Vector[vpr.Exp],
                        posts: Vector[vpr.Exp],
                        body: Option[vpr.Seqn], // TODO: add argument specifying the variables that should be returned
                      )(pos : vpr.Position, info : vpr.Info, errT : vpr.ErrorTrafo) : vpr.Stmt = {

    val (arguments, modifiedArgs, leaking) = {
      val freeVariablesInStmt = body.toSet.flatMap((b: vpr.Seqn) => b.undeclLocalVars)
      val freeVariablesInPre = pres
        .map(e => vpr.utility.Expressions.freeVariables(e).collect{ case x: vpr.LocalVar => x })
        .foldLeft(Set.empty[vpr.LocalVar]){ case (l,r) => l ++ r }
      val freeVariablesInPost = posts
        .map(e => vpr.utility.Expressions.freeVariables(e).collect{ case x: vpr.LocalVar => x })
        .foldLeft(Set.empty[vpr.LocalVar]){ case (l,r) => l ++ r }
      val freeVariablesInSpec = freeVariablesInPre ++ freeVariablesInPost
      val declaredVariables = body.toSet.flatMap((b: vpr.Seqn) => b.transitiveScopedDecls.collect{ case x: vpr.LocalVarDecl => x.localVar })
      val freeVariables = freeVariablesInStmt ++ (freeVariablesInSpec diff declaredVariables)
      val modifiedVariables = body.toSet.flatMap((b: vpr.Seqn) => b.writtenVars)
      val leakingVariables = freeVariablesInPost intersect declaredVariables
      val modifiedArgumentVariables = modifiedVariables intersect freeVariables
      (freeVariables.toVector, modifiedArgumentVariables.toVector, leakingVariables.toVector)
    }
    val results = modifiedArgs ++ leaking

    generatedMembers ::= {
      val formals = arguments.map(v => v.copy(name = s"${v.name}$$in")(v.pos, v.info, v.errT))
      val returns = results.map(v => v.copy(name = s"${v.name}$$out")(v.pos, v.info, v.errT))
      val actualBody = body.map{ b =>
        val prelude = (arguments zip formals).map{ case (l, r) => vpr.LocalVarAssign(l, r)(l.pos, l.info, l.errT) }
        val ending = (returns zip results).map{ case (l, r) => vpr.LocalVarAssign(l, r)(l.pos, l.info, l.errT) }
        val tb = b.transform{ case lold: vpr.LabelledOld => vpr.Old(lold.exp)(lold.pos, lold.info, lold.errT) }
        vpr.Seqn(prelude ++ (tb +: ending), arguments map ViperUtil.toVarDecl)(b.pos, b.info, b.errT)
      }
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
        body = actualBody
      )(pos, info, errT)
    }

    val call = vpr.MethodCall(methodName = name, args = arguments, targets = results)(pos, info, errT)

    if (leaking.isEmpty) call
    else vpr.Seqn(Vector(call), leaking map ViperUtil.toVarDecl)(pos, info, errT)
  }

}
