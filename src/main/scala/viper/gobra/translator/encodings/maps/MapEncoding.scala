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
import viper.gobra.theory.Addressability.{Exclusive, Shared}
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

  //  TODO: Unlike slices, maps are not thread-safe: a modification to a map must be synchronized with others.

  /**
    * Translates a type into a Viper type.
    * Both Exclusive and Shared maps are encoded as vpr.Ref because nil is an admissible value for maps
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Map(_, _) => vpr.Ref
  }

  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    default(super.expr(ctx)) {
      case (exp: in.DfltVal) :: ctx.Map(_, _) => unit(withSrc(vpr.NullLit(), exp))
      case (exp: in.NilLit) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))
      case in.Length(exp :: ctx.Map(k, v) / Exclusive) =>
        for {
          e <- goE(exp)
          keyType = goT(k)
          valueType = goT(v)
          // Todo: use ternary op here
          res <- handleLength(e, keyType, valueType)
        } yield res
    }
  }

  /**
    * Encodes the allocation of a new map
    *
    *  [r := make(map[T1]T2, n)] ->
    *    asserts 0 <= [n]
    *    var a Ref := new(val)
    *    inhales len(getMap(a.underlyingMap)) == 0 // TODO: bit fragile, changes in the handleLen method may affect this, abstract len(getMap(a.underlyingMap)) in its own method
    *    r := a
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    // TODO: refactor, put in own method?
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
  }


  private val keyParam = vpr.TypeVar("K")
  private val valueParam = vpr.TypeVar("V")

  // TODO: maybe this can be done as a function generator instead of a domain?
  /**
    * Generates
    *   domain GobraMap[K,V] {
    *     function getMap(addr: Ref): Map[K,V]
    *
    *     axiom nullMap {
    *       |getMap(null)| == 0
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

  /**
    * This field is required in order to differentiate nil map from empty (non-nil) map
    */
  private val underlyingMapFieldName = "underlyingMapField" // TODO: change to avoid collisions
  private val underlyingMapField: vpr.Field = vpr.Field(underlyingMapFieldName, vpr.Ref)()

  /** Encodes the specification of the len() function for maps as follows:
    *   function mapLen(m: Ref): Int
    *     requires m != null ==> acc(m.underlyingMapField, wildcard)
    *     // TODO: m == null? 0 : |underlyingMap(m.underlyingMapField)|
    *     ensures m != null ==> result == |underlyingMap(m.underlyingMapField)|
    *     ensures m == null ==> result == 0
    *
    * Alternatively, we could have encoded this as a vpr.Function. However, the need to pass the types of keys and
    * values makes things a bit harder. (instead, one could use a FunctionGenerator)
    *
    * Unlike slices, taking the length of a map requires read permissions to it because maps may change in size, e.g.
    *   m := make(map[int]int)
    *   go f(m) // f(m) sets m[10] to 10
    *   go g(m) // where g(m) computes len(m)
    * There is a race condition here but Gobra still verifies if len(m) does not require read permissions to m.
    */
  private def handleLength(e: vpr.Exp, keys: vpr.Type, values: vpr.Type): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = e.meta
    val resVarDecl = vpr.LocalVarDecl(Names.freshName, vpr.Int)(pos, info, errT)
    for {
      // var res: Int
      _ <- local(resVarDecl)

      // assert e != null ==> acc(e.underlyingMap, _)
      _ <- write(
        vpr.Assert(
          vpr.Implies(
            vpr.NeCmp(e, vpr.NullLit()(pos, info, errT))(pos, info, errT),
            vpr.FieldAccessPredicate(
              vpr.FieldAccess(e, underlyingMapField)(pos, info, errT),
              vpr.WildcardPerm()(pos, info, errT)
            )(pos, info, errT)
          )(pos, info, errT)
        )(pos, info, errT))

      // assume e == null ==> res == 0
      _ <- write(
        vpr.Assume(
          vpr.Implies(
            vpr.EqCmp(e, vpr.NullLit()(pos, info, errT))(pos, info, errT),
            vpr.EqCmp(resVarDecl.localVar, vpr.IntLit(BigInt(0))(pos, info, errT))(pos, info, errT)
          )(pos, info, errT)
        )(pos, info, errT)
      )

      // assume e != null ==> res == |getMap(e.underlyingMap)|
      _ <- write(
        vpr.Assume(
          vpr.Implies(
            vpr.NeCmp(e, vpr.NullLit()(pos, info, errT))(),
            vpr.EqCmp(
              resVarDecl.localVar,
              vpr.MapCardinality(
                vpr.DomainFuncApp(
                  func = getMapFunc,
                  args = Seq(vpr.FieldAccess(e, underlyingMapField)(pos, info, errT)),
                  typVarMap = Map(keyParam -> keys, valueParam -> values))(pos, info, errT))(pos, info, errT))(pos, info, errT))(pos, info, errT))(pos, info, errT)
      )
    } yield resVarDecl.localVar // return res
  }
}
