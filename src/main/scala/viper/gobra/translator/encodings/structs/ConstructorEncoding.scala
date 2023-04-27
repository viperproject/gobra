// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

package viper.gobra.translator.encodings.structs

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.Names
import viper.gobra.theory.Addressability
import viper.silver.{ast => vpr}
import viper.gobra.translator.library.construct.{Construct, ConstructImpl}


class ConstructorEncoding extends Encoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  private val struct: StructEncoding = new StructEncoding

  override def finalize (addMemberFn: vpr.Member => Unit): Unit = {
    construction.finalize(addMemberFn)
  } 

  private val construction: Construct = new ConstructImpl

  override def method(ctx: Context): in.Member ==> MemberWriter[vpr.Method] = {
    case x: in.Constructor if x.typ.isInstanceOf[in.PointerT] => shConstructor(x)(ctx)
    case x: in.Constructor if !x.typ.isInstanceOf[in.PointerT] => exConstructor(x)(ctx)
    case x: in.Assignments => assignments(x)(ctx)
  }

  override def function(ctx: Context): in.Member ==> MemberWriter[vpr.Function] = {
    case x: in.Dereference if x.ret.isInstanceOf[in.PointerT] => dereference(x)(ctx)
  }

  /* 
   * Takes a constructor 'ctor' and creates a method '*TConstruct'.
   * [
   *    requires P
   *    ensures Q
   *    construct *T() {
   *      proof
   *    }
   * ]
   * 
   * is encoded as:
   *
   * method *TConstruct(value: T) returns (result: *T)
   *    requires [P]
   *    ensures [Q]
   * {
   *    var z (*T)°;
   *    inhale Footprint[*z] && [*z == value];
   *    [result = z];
   *    [proof];
   * }
   * 
   * The encoding inhales the Footprint of *z and assigns 'lit' to *z.
   * 
   * If there are no dereference or no assignment constructor(s), then the method generates
   * a *TDeref or *TAssign. To do that it takes the fold statments inside the constructor body
   * and the specification Q:
   *
   * [
   *    requires P
   *    ensures Q
   *    construct *T() {
   *      fold A1
   *      fold A2
   *      ...
   *      fold An
   *    }
   * ]
   * 
   * is encoded as:
   *
   * function *TDeref([result: *T]) returns ([ret: T]) 
   *    requires [Q[Access]]
   * {
   *    let [z == result] in [unfolding An] in ... [unfolding A1] in [*result]
   * }
   * 
   * method *TAssign([result: *T], [value: T]) returns ([result: *T])
   *    requires [Q[Access]]
   *    ensures [Q[Access]]
   *    ensures *TDeref(result) == [value]
   * {
   *    [unfold An]
   *    ...
   *    [unfold A1]
   *    [*result = value]
   *    [fold A1]
   *    ...
   *    [fold An]
   * }
   * 
   * The "result" and "value" arguments are virtually generated from the constructor 
   * and reused in the generated constructors. Q[Access] only takes the permission
   * and predicates of the specification Q.
   */
  private def shConstructor(ctor: in.Constructor)(ctx: Context): MemberWriter[vpr.Method] = {
    val (pos, info, errT) = ctor.vprMeta
    val src = ctor.info

    val name = ctor.id.name

    val arg0 = ctor.args(0)
    val arg1 = ctor.args(1)

    val ret = in.LocalVar(arg0.id, arg0.typ)(src)
    val vRetD = vpr.LocalVarDecl(arg0.id, ctx.typ(arg0.typ))(pos, info, errT)

    val exprTyp = getPointer(arg0.typ)
    val exp = in.LocalVar(arg1.id, exprTyp)(src)
    val vExprD = vpr.LocalVarDecl(arg1.id, ctx.typ(exp.typ))(pos, info, errT)

    //generate DEREFERENCE if not defined
    val deref = if (ctx.lookupDereference(exprTyp.withAddressability(Addressability.Shared)).isEmpty) { construction.dereference(ctor)(ctx) } 
        else { option(None) }

    //generate ASSIGNMENTS if not defined
    val assign = if (ctx.lookupAssignments(exprTyp.withAddressability(Addressability.Shared)).isEmpty) { construction.assignments(ctor)(ctx) }
        else { option(None) }

    for {
      pres <- sequence(ctor.pres.map(ctx.precondition))
      posts <- sequence(ctor.posts.map(ctx.postcondition))
      bodyCon <- option(shConstructorBody(ctor.body, exp, ret)(ctx))

      _ <- if (bodyCon.isEmpty) option(None)
           else errorT(construction.constructWellFormedError(bodyCon.get))

      _ <- deref
      _ <- assign
    
      method = vpr.Method(
        name = s"${name}_${Names.constrConstruct}",
        formalArgs = Seq(vExprD),
        formalReturns = Seq(vRetD),
        pres = pres,
        posts = posts,
        body = bodyCon
      )(pos, info, errT)
    } yield method
  }

  private def shConstructorBody(body: Option[in.Stmt], exp: in.LocalVar, ret: in.LocalVar)(ctx: Context): Option[Writer[vpr.Seqn]] = {
    if (body.isEmpty) { None }  
    else { 
      val b = body.get
      val (pos, info, errT) = b.vprMeta
      val z = in.LocalVar(ctx.freshNames.next(), ret.typ.withAddressability(Addressability.Exclusive))(b.info)
      val zDeref = in.Deref(z, underlyingType(z.typ)(ctx))(z.info)
      val vZ = vpr.LocalVar(z.id, ctx.typ(z.typ))(pos, info, errT)
      val vRet = vpr.LocalVar(ret.id, ctx.typ(ret.typ))(pos, info, errT)
      Some( block(
        for {
          _ <- cl.local(ctx.variable(z))
          eqDflt <- ctx.equal(z, in.DfltVal(z.typ)(z.info))(b)
          _ <- cl.write(vpr.Inhale(eqDflt)(pos, info, errT))

          footprint <- struct.addressFootprint(ctx)(zDeref, in.FullPerm(zDeref.info))

          _ <- for {
            eq <- ctx.equal(zDeref, exp)(b)
            w <- cl.write(vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT))
          } yield w 

          _ <- cl.bind(vRet, vZ)
          
          stmt <- cl.seqns(b match {
            case in.Block(_, stmts) => stmts.map(s => ctx.statement(s)) 
            case _ => ???
          })

        } yield stmt 
    ))}
  }

  private def exConstructor(ctor: in.Constructor)(ctx: Context): MemberWriter[vpr.Method] = {
    val (pos, info, errT) = ctor.vprMeta
    val src = ctor.info

    val name = ctor.id.name

    val ret = in.LocalVar(ctor.args(0).id, ctor.typ)(src)
    val vRetD = vpr.LocalVarDecl(ret.id, ctx.typ(ret.typ))(pos, info, errT)
    val vRet = vpr.LocalVar(ret.id, ctx.typ(ret.typ))(pos, info, errT)

    val vPosts = sequence(ctor.posts.map(ctx.postcondition))

    val z = in.LocalVar(ctx.freshNames.next(), ret.typ.withAddressability(Addressability.Exclusive))(src)
    val vZ = vpr.LocalVar(z.id, ctx.typ(z.typ))(pos, info, errT)

    val expr = in.LocalVar(ctor.args(1).id, ret.typ)(src)
    val vExprD = vpr.LocalVarDecl(expr.id, ctx.typ(expr.typ))(pos, info, errT)
    
    val body = if (ctor.body.isEmpty) { None } 
    else { 
      val b = ctor.body.getOrElse(null)
      Some( block(
        for {
          _ <- cl.bind(vRet, vZ)

          stmt <- cl.seqns(b match {
            case in.Block(_, stmt) => stmt.map(s => ctx.statement(s))
            case _ => ???
          })

      } yield stmt
    ))}

    for {
      posts <- vPosts
      body <- option(body)
      method = vpr.Method(
        name = s"${name}_${Names.constrConstruct}",
        formalArgs = Seq(vExprD),
        formalReturns = Seq(vRetD),
        pres = Seq.empty,
        posts = posts,
        body = body
      )(pos, info, errT)
    } yield method
  }

  /*
   * Takes a dereference 'deref' and creates a method '*TDeref'.
   * 
   * [
   *    requires P
   *    ensures Q
   *    pure deref *T() {
   *      proof
   *    }
   * ]
   * 
   * is encoded as:
   *
   * function *TDeref([this: *T]) returns ([ret: T])
   *    requires [P]
   *    ensures [Q]
   * {
   *    [proof]
   * }
   */
  private def dereference(deref: in.Dereference)(ctx: Context): MemberWriter[vpr.Function] = {
    require(deref.results.size == 1)

    val name = deref.id.name

    val (pos, info, errT) = deref.vprMeta
    val vArgs = deref.args.map(ctx.variable)
    val vArgPres = deref.args.flatMap(ctx.varPrecondition)

    val vResults = deref.results.map(ctx.variable)
    val vResultPosts = deref.results.flatMap(ctx.varPostcondition)
    assert(vResults.size == 1)
    val resultType = if (vResults.size == 1) vResults.head.typ else ctx.tuple.typ(vResults map (_.typ))

    val fixResultvar = (x: vpr.Exp) => {
      x.transform { case v: vpr.LocalVar if v.name == deref.results.head.id => vpr.Result(resultType)() }
    }

    for {
      pres <- sequence(vArgPres ++ deref.pres.map(ctx.precondition))
      posts <- sequence(vResultPosts ++ deref.posts.map(ctx.postcondition(_).map(fixResultvar(_))))

      body <- option(deref.body map { b => pure(ctx.expression(b))(ctx) })

      _ <- errorT(construction.permissionDerefError(generated=false))
      _ <- if (body.isEmpty) option(None) else errorT(construction.derefWellFormedError(deref.info, body.get))

      function = vpr.Function(
        name = s"${name}_${Names.derefConstruct}",
        formalArgs = vArgs,
        typ = resultType,
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)

    } yield function
  }

  /*
   * Takes an assignment 'ass' and creates a method '*TAssign'.
   * 
   * [
   *    requires P
   *    ensures Q
   *    ensures *this == value
   *    assign *T(value: T) {
   *      proof
   *    }
   * ]
   * 
   * is encoded as:
   *
   * method *TAssign([this: *T], [value: T]) returns ([ret: *T])
   *    requires [P]
   *    ensures [Q]
   *    ensures *TDeref([this]) == [value]
   * {
   *    [proof]
   * }
   */
  private def assignments(ass: in.Assignments)(ctx: Context): MemberWriter[vpr.Method] = {
    val (pos, info, errT) = ass.vprMeta
    val src = ass.info

    val name = ass.id.name

    val arg0 = ass.args(0)
    val arg1 = ass.args(1)

    val ret = in.LocalVar(ctx.freshNames.next(), arg0.typ)(src)
    val vRetD = vpr.LocalVarDecl(ret.id, ctx.typ(ret.typ))(pos, info, errT)

    val thi = in.LocalVar(arg0.id, arg0.typ)(src)
    val vThiD = vpr.LocalVarDecl(thi.id, ctx.typ(thi.typ))(pos, info, errT)

    val arg = in.LocalVar(arg1.id, arg1.typ)(src)
    val vArgD = vpr.LocalVarDecl(arg.id, ctx.typ(arg.typ))(pos, info, errT)

    val exprTyp = getPointer(ass.ret).withAddressability(Addressability.Shared)

    for {
      pres <- sequence(ass.pres.map(ctx.precondition))
      posts <- dereferenceInAssignmentSpec(ass.posts, exprTyp)(ctx)
      body <- if (ass.body.isEmpty) option(None) else option(Some(block(ctx.statement(ass.body.get)))) //option(assignmentBody(ass.body, ret)(ctx))

      _ <- errorT(construction.permissionAssignError(generated=false))
      _ <- if (body.isEmpty) option(None) else errorT(construction.assignWellFormedError(ass.info, body.get))

      method = vpr.Method(
        name = s"${name}_${Names.assignConstruct}",
        formalArgs = Seq(vThiD) ++ Seq(vArgD),
        formalReturns = Seq(vRetD),
        pres = pres,
        posts = posts,
        body = body
      )(pos, info, errT)
    } yield method
  } 

  // if there is a dereference inside the ASSIGN specification then DEREF needs to be called
  private def dereferenceInAssignmentSpec(spec: Vector[in.Assertion], typ: in.Type)(ctx: Context): Writer[Vector[vpr.Exp]] = {
    val deref = ctx.lookupDereference(typ).isEmpty
    val gener = ctx.lookupConstructor(typ).isEmpty //generated DEREF from CONSTRUCT
    if (deref && gener) sequence(spec.map(ctx.postcondition))
    else sequence(spec.flatMap(topLevelConjuncts(_)).map{
      case e@in.ExprAssertion(d: in.EqCmp) if (d.left.isInstanceOf[in.Deref] || d.right.isInstanceOf[in.Deref]) => 
        val name = if (!deref) ctx.lookupDereference(typ).get.name else ctx.lookupConstructor(typ).get.name + s"_${Names.generatedConstruct}"
        val (pos, info, errT) = e.vprMeta
        if (!d.left.isInstanceOf[in.Deref] && d.right.isInstanceOf[in.Deref]) {
          pure(for {
            vLhs <- ctx.expression(d.left)
            vRh <- ctx.expression(d.right.asInstanceOf[in.Deref].exp)
            vRhs = vpr.FuncApp(s"${name}_${Names.derefConstruct}", Seq(vRh))(pos, info, vRh.typ, errT)
          } yield withSrc(vpr.EqCmp(vLhs, vRhs), e))(ctx)
        } else if (d.left.isInstanceOf[in.Deref] && !d.right.isInstanceOf[in.Deref]) {
          pure(for {
            vRhs <- ctx.expression(d.right)
            vLh <- ctx.expression(d.left.asInstanceOf[in.Deref].exp)
            vLhs = vpr.FuncApp(s"${name}_${Names.derefConstruct}", Seq(vLh))(pos, info, vLh.typ, errT)
          } yield withSrc(vpr.EqCmp(vLhs, vRhs), e))(ctx)
        } else {
          pure(for {
            vRh <- ctx.expression(d.right.asInstanceOf[in.Deref].exp)
            vLh <- ctx.expression(d.left.asInstanceOf[in.Deref].exp)
            vRhs = vpr.FuncApp(s"${name}_${Names.derefConstruct}", Seq(vRh))(pos, info, vRh.typ, errT)
            vLhs = vpr.FuncApp(s"${name}_${Names.derefConstruct}", Seq(vLh))(pos, info, vLh.typ, errT)
          } yield withSrc(vpr.EqCmp(vLhs, vRhs), e))(ctx)
        }
      case a => ctx.postcondition(a) 
    })
  }

  private def topLevelConjuncts(a: in.Assertion): Seq[in.Assertion] = a match {
    case in.SepAnd(a1, a2) => topLevelConjuncts(a1) ++ topLevelConjuncts(a2)
    case _ => Seq(a)
  }

  private def getPointer(typ: in.Type): in.Type = typ match {
    case in.PointerT(t, _) => t.withAddressability(Addressability.Exclusive)
    case _ => typ
  }
}
