// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.maps

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{MakePreconditionError, Source}
import viper.gobra.theory.Addressability.Exclusive
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeLevel._
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.verifier.{errors => err}
import viper.silver.{ast => vpr}

class MapEncoding extends LeafTypeEncoding {
  import viper.gobra.translator.util.TypePatterns._

  private val domainName: String = Names.mapsDomain

  //TODO: doc
  /**
    * Translates a type into a Viper type.
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Map(_, _) => vpr.Ref // TODO: explain why both are Ref
    // case ctx.Map(_) / Exclusive => vpr.Ref
    // case ctx.Map(_) / Shared => vpr.Ref
    /*
      case Exclusive => ctx.slice.typ(ctx.typeEncoding.typ(ctx)(t))
      case Shared => vpr.Ref
     */
  }

  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    default(super.expr(ctx)) {
      case (exp: in.DfltVal) :: ctx.Map(_, _) => unit(withSrc(vpr.NullLit(), exp))
      case (exp: in.NilLit) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))
      case l@in.Length(exp :: ctx.Map(k, v) / Exclusive) =>
        for {
          // TODO: must check whether there is permission to the slice, otherwise race condition (test with the other I wrote)
          // maybe introduce a new viper function with the correct precondition
          e <- goE(exp)
          res = withSrc(vpr.FuncApp(mapLenFunction, Seq(e)), l)
        } yield res
    }
  }

  /**
    * Encodes the allocation of a new map
    *
    *  [r := make(map[T1]T2, n)] ->
    *    assert dynamicType(T1) is comparable
    *    asserts 0 <= [n]
    *    var a Ref := new(val)
    *    inhales len(getMap(a)) == 0 // TODO: acc(a.val) && len(getMap(a.val)) == 0
    *    r := a
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    // TODO: refactor
    default(super.statement(ctx)) {
      case makeStmt@in.MakeMap(target, t@in.MapT(keys, values, _), makeArg) =>
        val (pos, info, errT) = makeStmt.vprMeta

        // Runtime check asserting 0 <= [n]
        val runtimeCheck = makeArg.toVector map { n =>
          for {
            nVpr <- goE(n)
            runtimeCheckExp = vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), nVpr)(pos, info, errT)
          } yield vpr.Exhale(runtimeCheckExp)(pos, info, errT)
        }

        seqn(
          for {
            checks <- sequence(runtimeCheck)
            _ <- if (checks.length == 1) write(checks(0)) else unit()
            _ <- errorT {
              case e@err.ExhaleFailed(Source(info), _, _) if checks.nonEmpty && e.causedBy(checks(0)) => MakePreconditionError(info)
            }

            mapVar = in.LocalVar(Names.freshName, t.withAddressability(Exclusive))(makeStmt.info)
            mapVarVpr = ctx.typeEncoding.variable(ctx)(mapVar)
            _ <- local(mapVarVpr)

            zeroLit = vpr.IntLit(BigInt(0))(pos, info, errT)
            _ <- write(
              vpr.NewStmt(
                mapVarVpr.localVar,
                Seq(underlyingMapField)
              )(pos, info, errT))
            _ <- write(
              vpr.Inhale(
                vpr.EqCmp(
                  vpr.MapCardinality(
                    vpr.DomainFuncApp(
                      getMapFunc,
                      Seq(vpr.FieldAccess(mapVarVpr.localVar, underlyingMapField)(pos, info, errT)),
                      Map(keyParam -> goT(keys), valueParam -> goT(values)))(pos, info, errT))(pos, info, errT),
                  zeroLit)(pos, info, errT)
              )(pos, info, errT)
            )
            ass <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(target), mapVar, makeStmt)
          } yield ass
        )

    }
  }

  override def finalize(col: Collector): Unit = {
    col.addMember(genDomain())
    col.addMember(underlyingMapField)
    col.addMember(mapLenFunction)
  }


  private val keyParam = vpr.TypeVar("K")
  private val valueParam = vpr.TypeVar("V")

  /**
    * Generates
    *   domain GobraMap[K,V] {
    *     function getMap(addr: Ref): Map[K,V]
    *
    *     axiom nullMap {
    *       // getMap(null) == Map[K,V]()
    *       |getMap(null)| == 0 // alternative
    *     }
    *   }
    */
  private def genDomain(): vpr.Domain = {
    vpr.Domain(
      name = domainName,
      functions = Seq(getMapFunc),
      axioms = Seq(nullMapAxiom),
      typVars = Seq(keyParam, valueParam)
    )()
  }

  private val getMapFuncName: String = "getMap"
  private val getMapFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = getMapFuncName,
    formalArgs = Seq(vpr.LocalVarDecl("ref", vpr.Ref)()),
    typ = vpr.MapType(keyParam, valueParam),
  )(domainName = domainName)

  /*
  private val nullMapAxiomName: String = "nullMap"
  private val nullMapAxiom: vpr.DomainAxiom = vpr.NamedDomainAxiom(
    name = nullMapAxiomName,
    exp = {
      val getMapNull = vpr.DomainFuncApp(getMapFunc, Seq(vpr.NullLit()()), Map(keyParam -> keyParam, valueParam -> valueParam))()
      val emptyMap = vpr.EmptyMap(keyParam, valueParam)()
      vpr.EqCmp(getMapNull, emptyMap)()
    })(domainName = domainName)
  */

  private val nullMapAxiomName: String = "nullMap"
  private val nullMapAxiom: vpr.DomainAxiom = vpr.NamedDomainAxiom(
    name = nullMapAxiomName,
    exp = {
      val getMapNullSize =
        vpr.MapCardinality(
          vpr.DomainFuncApp.apply(
            func = getMapFunc,
            args = Seq(vpr.NullLit()()),
            typVarMap = Map(keyParam -> keyParam, valueParam -> valueParam)
          )()
        )()
      val zeroLit = vpr.IntLit(BigInt(0))()
      vpr.EqCmp(getMapNullSize, zeroLit)()
    })(domainName = domainName)

  // field required to differentiate nill map from empty (non-nill) map
  private val underlyingMapFieldName = "underlyingMapField" // TODO: change to avoid collisions
  private val underlyingMapField: vpr.Field = vpr.Field(underlyingMapFieldName, vpr.Ref)()

  /**
    *   function mapLen(m: Ref): Int
    *     requires m != null ==> acc(m.underlyingMapField, wildcard)
    *     ensures m != null ==> result == |underlyingMap(m.underlyingMapField)|
    *     ensures m == null ==> result == 0
    */
    // TODO: explain why access is required
  private val mapLenFunctionName = "mapLen" // TODO: change name to avoid collision
  private def mapLenFunction: vpr.Function = {
    val argDecl = vpr.LocalVarDecl("m", vpr.Ref)()
    vpr.Function(
      name = mapLenFunctionName,
      formalArgs = Seq(argDecl),
      typ = vpr.Int,
      pres = Seq(
        vpr.Implies(
          vpr.NeCmp(
            argDecl.localVar,
            vpr.NullLit()())(),
          vpr.FieldAccessPredicate(
            vpr.FieldAccess(
              argDecl.localVar,
              underlyingMapField)(),
            vpr.WildcardPerm()()
          )()
        )()
      ),
      posts =
        Seq(
          // ensures m != null ==> result == |underlyingMap(m.underlyingMapField)|
          /*vpr.Implies(
            vpr.NeCmp(
              argDecl.localVar,
              vpr.NullLit()())(),
            vpr.EqCmp(
              vpr.Result(vpr.Int)(),
              vpr.MapCardinality(
                vpr.DomainFuncApp(
                  func = getMapFunc,
                  args = Seq(vpr.FieldAccess(argDecl.localVar, underlyingMapField)()),
                  typVarMap = Map(keyParam -> keyParam, valueParam -> valueParam))())())())(), */
          // ensures m == null ==> result == 0
          vpr.Implies(
            vpr.EqCmp(
              argDecl.localVar,
              vpr.NullLit()())(),
            vpr.EqCmp(
              vpr.Result(vpr.Int)(),
              vpr.IntLit(BigInt(0))()
            )()
          )()
        ),
      body = None)()
  }


}
