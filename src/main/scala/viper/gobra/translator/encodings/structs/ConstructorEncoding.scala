// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.theory.Addressability
import viper.silver.{ast => vpr}


class ConstructorEncoding extends Encoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  private val struct: StructEncoding = new StructEncoding

  override def method(ctx: Context): in.Member ==> MemberWriter[vpr.Method] = {
    case x: in.Constructor if x.ret.isInstanceOf[in.PointerT] => shConstructor(x)(ctx)
    case x: in.Constructor if !x.ret.isInstanceOf[in.PointerT] => exConstructor(x)(ctx)
  }

  /* 
   * Takes a constructor 'ctor' and creates a method '*TConstruct'.
   * [
   *    ensures Q
   *    construct T() {
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
   * Alternatively, one can create an abstract constructor without the proof.
   */
  private def shConstructor(ctor: in.Constructor)(ctx: Context): MemberWriter[vpr.Method] = {
    val (pos, info, errT) = ctor.vprMeta
    val src = ctor.info

    val name = ctor.id.uniqueName

    val ret = in.LocalVar(ctor.args(0).id, ctor.ret)(src)
    val vRet = vpr.LocalVar(ret.id, ctx.typ(ret.typ))(pos, info, errT)
    val vRetD = vpr.LocalVarDecl(ret.id, ctx.typ(ret.typ))(pos, info, errT)

    val vPosts = sequence(ctor.posts.map(ctx.postcondition))

    val z = in.LocalVar(ctx.freshNames.next(), ret.typ.withAddressability(Addressability.Exclusive))(src)
    val vZ = vpr.LocalVar(z.id, ctx.typ(z.typ))(pos, info, errT)
    val zDeref = in.Deref(z, underlyingType(z.typ)(ctx))(src)

    val exprTyp = ctor.ret match {
      case in.PointerT(t, _) => t.withAddressability(Addressability.Exclusive)
      case _ => ???
    }
    val expr = in.LocalVar(ctor.args(1).id, exprTyp)(src)
    val vExprD = vpr.LocalVarDecl(expr.id, ctx.typ(expr.typ))(pos, info, errT)
    
    val body = if (ctor.body.isEmpty) { None } 
    else { 
      val b = ctor.body.getOrElse(null)
      Some( block(
        for {
          _ <- cl.local(ctx.variable(z))

          footprint <- struct.addressFootprint(ctx)(zDeref, in.FullPerm(zDeref.info))
          _ <- if (ctx.lookupConstructor(exprTyp).isEmpty) { for {
            eq <- ctx.equal(zDeref, expr)(ctor)
            w <- cl.write(vpr.Inhale(vpr.And(footprint, eq)(pos, info, errT))(pos, info, errT))
          } yield w } else { 
            val newExpr = in.LocalVar(ctx.freshNames.next(), exprTyp)(src)
            for {
              _ <- cl.local(ctx.variable(newExpr))
              stmt <- ctx.statement(in.New(newExpr, expr)(src))
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

    for {
      posts <- vPosts
      body <- option(body)
      method = vpr.Method(
        name = name,
        formalArgs = Seq(vExprD),
        formalReturns = Seq(vRetD),
        pres = Seq.empty,
        posts = posts,
        body = body
      )(pos, info, errT)
    } yield method
  }

  private def exConstructor(ctor: in.Constructor)(ctx: Context): MemberWriter[vpr.Method] = {
    val (pos, info, errT) = ctor.vprMeta
    val src = ctor.info

    val name = ctor.id.uniqueName

    val ret = in.LocalVar(ctor.args(0).id, ctor.ret)(src)
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
        name = name,
        formalArgs = Seq(vExprD),
        formalReturns = Seq(vRetD),
        pres = Seq.empty,
        posts = posts,
        body = body
      )(pos, info, errT)
    } yield method
  }
}
