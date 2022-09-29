// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.outlines

import viper.gobra.translator.util.ViperUtil
import viper.silver.{ast => vpr}

class OutlinesImpl extends Outlines {

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
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
    *   ensures  POST[ old[l](e) -> old(e[arguments -> arguments']) ][results -> results'][arguments -> arguments']
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
      val bodyFree = undeclLocalVarsGobraCopy(body).toSet
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

      import vpr.utility.Expressions.{instantiateVariables => subst}

      val actualBody = if (!trusted) {
        val prelude = (arguments zip formals).map{ case (l, r) => vpr.LocalVarAssign(l, r)(l.pos, l.info, l.errT) }
        val ending = (returns zip results).map{ case (l, r) => vpr.LocalVarAssign(l, r)(l.pos, l.info, l.errT) }
        val tb = body.transform{ case lold: vpr.LabelledOld => vpr.Old(lold.exp)(lold.pos, lold.info, lold.errT) }
        Some(vpr.Seqn(prelude ++ (tb +: ending), arguments map ViperUtil.toVarDecl)(body.pos, body.info, body.errT))
      } else None

      val actualPres = pres.map(e => subst(e, arguments, formals))
      val actualPosts = posts.map{ e =>
        val replacedOlds = e.transform{ case lold: vpr.LabelledOld => vpr.Old(subst(lold.exp, arguments, formals))(lold.pos, lold.info, lold.errT) }
        val replacedResults = subst(replacedOlds, results, returns)
        val replacedArguments = subst(replacedResults, arguments, formals)
        replacedArguments
      }

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

  /**
    * TODO: should be removed once the corresponding silver function is fixed.
    *
    * Returns a list of all undeclared local variables used in this statement.
    * If the same local variable is used with different
    * types, an exception is thrown.
    */
  private def undeclLocalVarsGobraCopy(s: vpr.Stmt): Seq[vpr.LocalVar] = {
    def extractLocal(n: vpr.Node, decls: Seq[vpr.LocalVarDecl]) =
      n match {
        case l: vpr.LocalVar => decls.find(_.name == l.name) match {
          case None => List(l)
          case Some(d) if d.typ != l.typ =>
            sys.error(s"Local variable ${l.name} is declared with type ${d.typ} but used with type ${l.typ}.")
          case _ => Nil
        }
        case _ => Nil
      }

    def combineLists(s1: Seq[vpr.LocalVar], s2: Seq[vpr.LocalVar]) = {
      for (l1 <- s1; l2 <- s2) {
        if (l1.name == l2.name && l1.typ != l2.typ) {
          sys.error(s"Local variable ${l1.name} is used with different types ${l1.typ} and ${l2.typ}.")
        }
      }
      (s1 ++ s2).distinct
    }

    def addDecls(n: vpr.Node, decls: Seq[vpr.LocalVarDecl]) = n match {
      case vpr.QuantifiedExp(variables, _) =>
        // add quantified variables
        decls ++ variables
      case vpr.Seqn(_, scoped) =>
        // add variables defined in scope
        decls ++ scoped.collect { case variable: vpr.LocalVarDecl => variable }
      case vpr.Let(variable, _, _) =>
        // add defined variable
        decls ++ Seq(variable)
      case _ =>
        decls
    }

    def combineResults(n: vpr.Node, decls: Seq[vpr.LocalVarDecl], locals: Seq[Seq[vpr.LocalVar]]) = {
      locals.fold(extractLocal(n, decls))(combineLists)
    }

    s.reduceWithContext(Nil, addDecls, combineResults)
  }

}

