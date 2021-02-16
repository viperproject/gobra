// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.preds

import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.Names
import viper.silver.{ast => vpr}

class DefuncComponentImpl extends DefuncComponent {

  private type Token = String

  /**
    * Returns the *predicate token* for type pred('ts').
    * The predicate token identifies the kind of a predicate.
    * All predicate expressions of the same kind are embedded using the same domain and evaluation predicate.
    * */
  private def embedPredType(ts: Vector[in.Type])(ctx: Context): Token = {
    embedPredTypeMap.getOrElse(ts, {
      val name = s"S${embedPredTypeMap.size}"
      embedPredTypeMap += (ts -> name)
      kindArgs += (name -> (ts map ctx.typeEncoding.typ(ctx)))
      encounteredTokens += name
      name
    })
  }
  private var embedPredTypeMap: Map[Vector[in.Type], Token] = Map.empty

  /** maps predicate tokens to the argument types of the corresponding predicate kind. */
  private var kindArgs: Map[Token, Vector[vpr.Type]] = Map.empty
  /** set of all encountered predicate tokens. */
  private var encounteredTokens: Set[Token] = Set.empty

  private def domainName(S: Token): String = s"${Names.predDomain}_$S"
  private def domainType(S: Token): vpr.DomainType = vpr.DomainType(domainName(S), Map.empty)(Seq.empty)
  private def evalName(S: String): String = s"eval_${domainName(S)}"

  /** Returns the predicate type with argument types 'ts'. */
  override def typ(ts: Vector[in.Type])(ctx: Context): vpr.Type = {
    val S = embedPredType(ts)(ctx)
    domainType(S)
  }

  /**
    * Returns the predicate identifier associated to a predicate 'predicate' with a combination of applied arguments 'pattern' where the predicate has type pred('predTs').
    * 'pattern' has value 'true' at index i iff the i-th argument of 'predicate' is not partially applied with a concrete value.
    * */
  override def id(predicate: in.PredicateProxy, predTs: Vector[in.Type], pattern: Vector[Boolean])(ctx: Context): BigInt = {
    val resTs = predTs.zip(pattern) collect { case (t, true) => t }
    val S = embedPredType(resTs)(ctx)
    idMap.getOrElse((S, predicate, pattern), {
      val newId = idCountMap.getOrElse(S, 0)
      idCountMap += (S -> (newId + 1))
      idMap += ((S, predicate, pattern) -> newId)

      encounteredIds += (S -> (encounteredIds.getOrElse(S, Set.empty) + newId))
      val appliedArgTypes = computeAppliedTs(predTs, pattern) map ctx.typeEncoding.typ(ctx)
      appliedArgs += ((S, newId: BigInt) -> appliedArgTypes)
//      patterns += ((S, newId: BigInt) -> pattern)
//      applications += ((S, newId: BigInt) -> ((args: Vector[vpr.Exp]) => ctx.predicate.proxyBodyAccess(predicate, args)()(ctx)))

      newId
    })
  }
  private var idMap: Map[(Token, in.PredicateProxy, Vector[Boolean]), BigInt] = Map.empty
  private var idCountMap: Map[Token, Int] = Map.empty

  /** set of encountered predicate identifiers for a predicate token. */
  private var encounteredIds: Map[Token, Set[BigInt]] = Map.empty
  /** maps predicate token together with predicate identifier to applied args of predicate instance. */
  private var appliedArgs: Map[(Token, BigInt), Vector[vpr.Type]] = Map.empty
//  /** maps predicate token together with predicate identifier to the pattern of applied arguments. */
//  private var patterns: Map[(Token, BigInt), Vector[Boolean]] = Map.empty
//  /** maps predicate token together with predicate identifier to the application of the predicate instance. */
//  private var applications: Map[(Token, BigInt), Vector[vpr.Exp] => vpr.Exp] = Map.empty


//  /** function id_S(p: pred_S): Int */
//  private def idFunc(S: Token): vpr.DomainFunc = {
//    vpr.DomainFunc(
//      name = s"${domainName(S)}_id",
//      formalArgs = Seq(vpr.LocalVarDecl("p", domainType(S))()),
//      typ = vpr.Int
//    )(domainName = domainName(S))
//  }
//  private def idFuncApp(S: Token, arg: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp = {
//    vpr.DomainFuncApp(func = idFunc(S), Seq(arg), Map.empty)(pos, info, errT)
//  }
//
//  /** function arg_S_ID_'idx'(p: pred_S): T° where T° is the type of the 'idx'-th applied argument of ID. */
//  private def argFunc(S: Token, id: BigInt, idx: Int): vpr.DomainFunc = {
//    require(appliedArgs contains (S, id))
//    vpr.DomainFunc(
//      name = s"${domainName(S)}_arg_${id}_$idx",
//      formalArgs = Seq(vpr.LocalVarDecl("p", domainType(S))()),
//      typ = appliedArgs(S, id)(idx)
//    )(domainName = domainName(S))
//  }
//  private def argFuncApp(S: Token, id: BigInt, idx: Int, arg: vpr.Exp)(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos): vpr.DomainFuncApp = {
//    vpr.DomainFuncApp(func = argFunc(S, id, idx), Seq(arg), Map.empty)(pos, info, errT)
//  }

  /** function make_S_ID(x1: T°1, ..., xm: T°m): pred_S where T°1, ..., T°m are the types of the applied arguments of ID. */
  private def makeFunc(S: Token, id: BigInt): vpr.DomainFunc = {
    require(appliedArgs contains (S, id))
    vpr.DomainFunc(
      name = s"${domainName(S)}_make_$id",
      formalArgs = appliedArgs(S, id).zipWithIndex map { case (t, idx) =>vpr.LocalVarDecl(s"x$idx", t)() },
      typ = domainType(S)
    )(domainName = domainName(S))
  }
  private def makeFuncApp(S: Token, id: BigInt, args: Vector[vpr.Exp])(pos: vpr.Position, info: vpr.Info, errT: vpr.ErrorTrafo): vpr.DomainFuncApp = {
    vpr.DomainFuncApp(func = makeFunc(S, id), args, Map.empty)(pos, info, errT)
  }

  /** Returns the predicate expression of type pred('resTs') resulting from the construction on 'id' with arguments 'args'. */
  override def make(id: BigInt, args: Vector[vpr.Exp], resTs: Vector[in.Type])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    val S = embedPredType(resTs)(ctx)
    makeFuncApp(S, id, args)(pos, info, errT)
  }

  /** Returns the predicate instance of predicate expression 'base' with type pred('baseTs') with arguments 'args'. */
  override def instance(base: vpr.Exp, baseTs: Vector[in.Type], args: Vector[vpr.Exp])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.PredicateAccess = {
    val S = embedPredType(baseTs)(ctx)
    vpr.PredicateAccess(base +: args, evalName(S))(pos, info, errT)
  }


  override def finalize(col: Collector): Unit = {
    encounteredTokens foreach (S => col.addMember(genDomain(S)))
    encounteredTokens foreach (S => genEval(S) foreach col.addMember)
  }

  /** Returns the default value of the predicate type with arguments 'ts' */
  override def default(ts: Vector[in.Type])(pos: vpr.Position = vpr.NoPosition, info: vpr.Info = vpr.NoInfo, errT: vpr.ErrorTrafo = vpr.NoTrafos)(ctx: Context): vpr.Exp = {
    vpr.DomainFuncApp(func = genDefaultFunc(ts)(ctx), Seq.empty, Map.empty)(pos, info, errT)
  }

  /** Creates a domain function that returns the default value of the predicate type with the 'ts' parameter list */
  private def genDefaultFunc(ts: Vector[in.Type])(ctx: Context): vpr.DomainFunc = {
    val S = embedPredType(ts)(ctx)
    genDefaultFuncMap.getOrElse(S, {
      val res = vpr.DomainFunc(
        name = s"${domainName(S)}_default",
        formalArgs = Seq.empty,
        typ = domainType(S)
      )(domainName = domainName(S))

      genDefaultFuncMap += (S -> res)
      res
    })
  }

  private var genDefaultFuncMap: Map[Token, vpr.DomainFunc] = Map.empty

  /**
    * Generates
    *
    * domain pred_S {
    *   // REMOVED // id_S(p: pred_S): Int
    *
    *   // For every encountered ID with applied argument types T°1, ..., T°m
    *   function make_S_ID(x1: T°1, ..., xm: T°m): pred_S
    *
    *   // REMOVED // function arg_S_ID_1(p: pred_S): T°1
    *   // REMOVED // ...
    *   // REMOVED // function arg_S_ID_m(p: pred_S): T°m
    *
    *   // REMOVED // axiom {
    *   // REMOVED //   forall x1: T°1, ..., xm: T°m :: {make_S_ID(x1, ..., xm)}
    *   // REMOVED //     let p == (make_S_ID(x1, ..., xm)) in
    *   // REMOVED //       id_S(p) == ID && arg_S_ID_1(p) == x1 && .. && arg_S_ID_m(p) == xm
    *   // REMOVED // }
    * }
    */
  private def genDomain(S: Token): vpr.Domain = {

    val ids = encounteredIds.getOrElse(S, Set.empty)

    var funcs: List[vpr.DomainFunc] = List.empty
    val axioms: List[vpr.DomainAxiom] = List.empty
//    funcs ::= idFunc(S)
    for (id <- ids) {
//      val innerArgTypes = appliedArgs(S, id)
//      val xsDecl = innerArgTypes.zipWithIndex map { case (t, idx) => vpr.LocalVarDecl(s"x$idx", t)() }; val xs = xsDecl map (_.localVar)
//      val pDecl = vpr.LocalVarDecl("p", domainType(S))(); val p = pDecl.localVar

      // generate make and arg functions
      funcs ::= makeFunc(S, id)

//      innerArgTypes.indices foreach (idx => funcs ::= argFunc(S, id, idx))

//      // generate axiom
//      axioms ::= {
//        val makeApp = makeFuncApp(S, id, xs)()
//        val idEq = vpr.EqCmp(idFuncApp(S, p)(), vpr.IntLit(id)())()
//        val argEqs = xs.zipWithIndex map { case (x, idx) => vpr.EqCmp(argFuncApp(S, id, idx, p)(), x)() }
//        val body = vpr.Let(pDecl, makeApp, VU.bigAnd(idEq +: argEqs)(vpr.NoPosition, vpr.NoInfo, vpr.NoTrafos))()
//
//        vpr.AnonymousDomainAxiom(
//          if (innerArgTypes.isEmpty) body
//          else vpr.Forall(xsDecl, Seq(vpr.Trigger(Seq(makeApp))()), body)()
//        )(domainName = domainName(S))
//      }
    }

    // adds the default value of the predicate type
    funcs ++= genDefaultFuncMap.get(S)

    vpr.Domain(
      name = domainName(S),
      functions = funcs,
      axioms = axioms,
      typVars = Seq.empty
    )()
  }

  /**
    * Generates
    * predicate eval_S(p: pred_S, y1: T1, ..., yn: Tn)
    *
    * // REMOVED // predicate eval_S(p: pred_S, y1: T1, ..., yn: Tn) {
    * // REMOVED //   // For every encountered ID with applied argument types T°1, ..., T°m
    * // REMOVED //   id_S(p) == ID ? 'application'(e1, ..., ek)
    * // REMOVED //      // where application is the body of the predicate corresponding to ID
    * // REMOVED //   ...
    * // REMOVED //   eval_S_unknown(p, y1, ..., yn)
    * // REMOVED // }
    *
    * // REMOVED // predicate eval_S_unknown(p: pred_S, y1: T1, ..., yn: Tn)
    */
  private def genEval(S: Token): Vector[vpr.Predicate] = {

    val pDecl = vpr.LocalVarDecl("p", domainType(S))() //; val p = pDecl.localVar
    val ysDecl = kindArgs(S).zipWithIndex map { case (t, idx) => vpr.LocalVarDecl(s"y$idx", t)() } //; val ys = ysDecl map (_.localVar)
    val formalsDecl = pDecl +: ysDecl //; val formals = formalsDecl map (_.localVar)
//    val ids = encounteredIds.getOrElse(S, Set.empty)

//    val bodies = ids map { id =>
//      val innerArgTypes = appliedArgs(S, id)
//      val pattern = patterns(S, id)
//      val xs = innerArgTypes.indices.toVector map (idx => argFuncApp(S, id, idx, p)())
//
//      val args = pattern.foldLeft((ys, xs, Vector.empty[vpr.Exp])){
//        case ((next +: outer, inner, res), true) => (outer, inner, res :+ next)
//        case ((outer, next +: inner, res), false) => (outer, inner, res :+ next)
//        case _ => Violation.violation("This case should never be reached")
//      }._3
//
//      applications(S, id)(args)
//    }
//
//    val unknownPred = vpr.Predicate(
//      name = s"${evalName(S)}_unknown",
//      formalArgs = formalsDecl,
//      body = None
//    )()
//    val unkownPredInstance: vpr.Exp = vpr.PredicateAccess(formals, unknownPred.name)()
//
//
//    val body = {
//      (ids zip bodies).foldRight(unkownPredInstance){
//        case ((id, thn), els) =>
//          val cond = vpr.EqCmp(idFuncApp(S, p)(), vpr.IntLit(id)())()
//          vpr.CondExp(cond, thn, els)()
//      }
//    }

    val evalPred = vpr.Predicate(
      name = evalName(S),
      formalArgs = formalsDecl,
      body = None
    )()

    Vector(evalPred)
  }
}
