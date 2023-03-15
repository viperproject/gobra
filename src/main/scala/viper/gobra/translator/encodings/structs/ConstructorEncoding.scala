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
  import viper.gobra.translator.util.{ViperUtil => VU}
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
   *    ensures Q
   *    construct *T() {
   *      proof
   *    }
   * ]
   * 
   * is encoded as:
   *
   * method *TConstruct(lit: T) returns (ret: *T)
   *    ensures [Q]
   * {
   *    var z (*T)°;
   *    inhale Footprint[*z] && [*z == lit];
   *    [ret = z];
   *    [proof];
   * }
   * 
   * The encoding inhales the Footprint of *z and assigns 'lit' to *z.
   * 
   * If there is another constructor 'TConstruct' for the 'lit' argument, we call upon
   * the TConstruct for that and we encode it as:
   *
   * method *TConstruct(lit: T) returns (ret: *T)
   *    ensures [Q]
   * {
   *    var z (*T)°;
   *    var l T;
   *    [l = TConstruct(lit): T]
   *    inhale Footprint[*z] && [*z == l];
   *    [ret = z];
   *    [proof];
   * }
   * 
   * 
   * If there are no dereference or no assignment constructor(s), then the method generates
   * a *TDeref or *TAssign. To do that it takes the fold statments inside the constructors body
   * and the specification Q:
   *
   * [
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
   * function *TDeref(this: *T) returns (ret: T) 
   *    requires [Q[Access]]
   * {
   *    let z == this in [unfolding An] in ... [unfolding A1] in [*this]
   * }
   * 
   * method *TAssign(this: *T, init: T) returns (ret: *T)
   *    requires [Q[Access]]
   *    ensures [Q[Access]]
   *    ensures *TDeref(this) == rhs
   * {
   *    [unfold An]
   *    ...
   *    [unfold A1]
   *    [*this = rhs]
   *    [fold A1]
   *    ...
   *    [fold An]
   * }
   * 
   * The "this"" and "init" arguments were virtually generated from the constructor 
   * and resused in the generated constructors. Q[Access] only takes the permission
   * part of the specification.
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
      bodyCon <- option(shConstructorBody(ctor.body, exp, exprTyp, ret)(ctx))

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

  private def shConstructorBody(body: Option[in.Stmt], exp: in.LocalVar, typ: in.Type, ret: in.LocalVar)(ctx: Context): Option[Writer[vpr.Seqn]] = {
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
          // if Exclusive Constructor exists it gets called, otherwise it compares the fields
          _ <- if (ctx.lookupConstructor(typ).isEmpty) { for {
            eq <- (zDeref, exp) match { // DEREFERENCE should not be called in CONSTRUCTOR
              case ((z: in.Deref) :: ctx.Struct(fz), e :: ctx.Struct(fe)) =>
                for {
                  x <- cl.bind(z)(ctx)
                  y <- cl.bind(e)(ctx)
                  lhsFAccs = fz.map(f => in.FieldRef(x, f)(x.info))
                  rhsFAccs = fe.map(f => in.FieldRef(y, f)(y.info))
                  equalFields <- cl.sequence((lhsFAccs zip rhsFAccs).map { case (lhsFA, rhsFA) => ctx.equal(lhsFA, rhsFA)(b) })
                } yield VU.bigAnd(equalFields)(pos, info, errT)
              case _ => ???
            }
            w <- cl.write(vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT))
          } yield w } else { 
            val newExpr = in.LocalVar(ctx.freshNames.next(), typ)(z.info)
            for {
              _ <- cl.local(ctx.variable(newExpr))
              stmt <- ctx.statement(in.New(newExpr, exp)(z.info))
              _ <- cl.write(stmt)
              vLhs <- zDeref match { // DEREFERENCE should not be called in CONSTRUCTOR
                case (z: in.Deref) :: ctx.CompleteStruct(fs) =>
                  for {
                    x <- cl.bind(z)(ctx)
                    locFAs = fs.map(f => in.FieldRef(x, f)(z.info))
                    args <- cl.sequence(locFAs.map(fa => ctx.expression(fa)))
                  } yield withSrc(ctx.tuple.create(args), z)
                case _ => cl.pure(ctx.expression(zDeref))(ctx)
              }
              vRhs <- ctx.expression(newExpr)
              eq = vpr.EqCmp(vLhs, vRhs)(pos, info, errT)
              w <- cl.write(vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT))
            } yield w 
          }

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
   *    requires Q
   *    pure deref *T() {
   *      proof
   *    }
   * ]
   * 
   * is encoded as:
   *
   * function *TDeref(this: *T) returns (ret: T)
   *    requires [Q]
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
   *    requires Q
   *    ensures Q
   *    ensures *this == rhs
   *    assign *T(rhs: T) {
   *      proof
   *    }
   * ]
   * 
   * is encoded as:
   *
   * function *TAssign(this: *T, rhs: T) returns (ret: *T)
   *    requires [Q]
   *    ensures [Q]
   *    ensures *TDeref == rhs
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
      body <- option(assignmentBody(ass.body, ret)(ctx))

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

 /* if an assignment is in the body of ASSIGN then it should not call itself
  * 
  * [lhs: Struct{F}@ = rhs] -> FOREACH f in F: [lhs.f = rhs.f]
  */
  private def assignmentBody(body: Option[in.Stmt], ret: in.LocalVar)(ctx: Context): Option[Writer[vpr.Seqn]] = {
    for {
      stmt <- if (body.isEmpty) { None } 
      else { 
        val b = body.get
        val (pos, info, errT) = b.vprMeta
        val r = vpr.LocalVar(ret.id, ctx.typ(ret.typ))(pos, info, errT)
        Some(
          block(
            cl.seqns(
              for { 
                w <- b match {
                  case in.Block(_, stmts) => stmts.map(s => s match {
                    // an assignment inside the assign declaration should not call itself
                    case a@in.SingleAss(in.Assignee(lhs :: ctx.Struct(lhsFs) / Addressability.Shared), rhs :: ctx.Struct(rhsFs)) 
                      if !ctx.lookupAssignments(lhs.typ).isEmpty => 
                        assert(lhs.typ == getPointer(ret.typ).withAddressability(Addressability.Shared), s"type of ${lhs} does not match ${ret}")
                        for {
                          x <- cl.bind(lhs)(ctx)
                          newX = in.LocalVar(x.id, ret.typ)(ret.info)
                          eqDflt <- ctx.equal(newX, in.DfltVal(ret.typ)(ret.info))(b)
                          _ <- cl.write(vpr.Inhale(eqDflt)(pos, info, errT))

                          y <- cl.bind(rhs)(ctx)
                          lhsFAs = lhsFs.map(f => in.FieldRef(x, f)(x.info)).map(in.Assignee.Field)
                          rhsFAs = rhsFs.map(f => in.FieldRef(y, f)(y.info))
                          res <- cl.seqns((lhsFAs zip rhsFAs).map { case (lhsFA, rhsFA) => ctx.assignment(lhsFA, rhsFA)(a) })
                          _ <- cl.write(res)

                          e = vpr.LocalVar(x.id, ctx.typ(x.typ))(pos, info, errT)
                        } yield vpr.LocalVarAssign(r, e)(pos, info, errT)
                    case s => ctx.statement(s)
                  })
                  case _ => ???
                }
              } yield w 
            )
          )
        )
      }
    } yield stmt
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
