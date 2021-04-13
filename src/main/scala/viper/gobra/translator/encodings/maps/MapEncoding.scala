// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings.maps

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.{MakePreconditionError, Source}
import viper.gobra.theory.Addressability
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
  // TODO: use fields and field generators instead

  //  TODO: Unlike slices, maps are not thread-safe: a modification to a map must be synchronized with others.

  /**
    * Translates a type into a Viper type.
    * Both Exclusive and Shared maps are encoded as vpr.Ref because nil is an admissible value for maps
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Map(_, _) => vpr.Ref
  }

  // TODO: doc
  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    default(super.expr(ctx)) {
      case (exp: in.DfltVal) :: ctx.Map(_, _) => unit(withSrc(vpr.NullLit(), exp))
      case (exp: in.NilLit) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))

      /** Unlike slices, taking the length of a map requires read permissions to it because maps may change in size, e.g.
       *    m := make(map[int]int)
       *    go f(m) // f(m) sets m[10] to 10
       *    go g(m) // where g(m) computes len(m)
       *
       *  There is a race condition here but Gobra still verifies if len(m) does not require read permissions to m.
       */
      case l@ in.Length(exp :: ctx.Map(k, v) / Exclusive) =>
        val (pos, info, errT) = l.vprMeta
        for {
          e <- goE(exp)
          keys = goT(k)
          values = goT(v)
          // [ len(m) ] ->
          //      [ m ] == null? 0 : |underlyingMap([ m ].underlyingMapField)|
          res = vpr.CondExp(
            vpr.EqCmp(e, vpr.NullLit()(pos, info, errT))(pos, info, errT),
            vpr.IntLit(BigInt(0))(pos, info, errT),
            vpr.MapCardinality(
              vpr.DomainFuncApp(
                func = getMapFunc,
                args = Seq(vpr.FieldAccess(e, underlyingMapField)(pos, info, errT)),
                typVarMap = Map(keyParam -> keys, valueParam -> values))(pos, info, errT))(pos, info, errT)
          )(pos, info, errT)
        } yield res

      case l@ in.IndexedExp(exp :: ctx.Map(k, v), idx) =>
        val (pos, info, errT) = l.vprMeta
        for {
          e <- goE(exp)
          id <- goE(idx)
          dflt <- goE(in.DfltVal(v)(l.info))
          // TODO: abstract this pattern in its own function
          correspondingMap = vpr.DomainFuncApp(
            func = getMapFunc,
            args = Seq(vpr.FieldAccess(e, underlyingMapField)(pos, info, errT)),
            typVarMap = Map(keyParam -> goT(k), valueParam -> goT(v)))(pos, info, errT)
          cond = vpr.CondExp(
            vpr.AnySetContains(id, vpr.MapDomain(correspondingMap)(pos, info, errT))(pos, info, errT),
            vpr.MapLookup(correspondingMap, id)(pos, info, errT),
            dflt
          )(pos, info, errT)
        } yield cond


        // TODO: incomplete, requires a function in reverse direction?
      /*
      case (lit: in.MapLit) :: ctx.Map(_, _) => {
        val (pos, info, errT) = lit.vprMeta
        for {
          mapletList <- sequence(lit.entries.toVector.map {
            case (key, value) => for {
              k <- goE(key)
              v <- goE(value)
            } yield vpr.Maplet(k, v)(pos, info, errT)
          })
        } yield vpr.ExplicitMap(mapletList)(pos, info, errT)
      }
       */
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

        // TODO: check whether map keys are comparable in both kinds of lookups
        // TODO: refactor, change, not working yet
      case l@ in.SafeMapLookup(resTarget, successTarget, indexedExp@ in.IndexedExp(base, idx)) =>
        val (pos, info, errT) = l.vprMeta
        val res = in.LocalVar(Names.freshName, indexedExp.typ.withAddressability(Addressability.Exclusive))(l.info)
        val vprRes = ctx.typeEncoding.variable(ctx)(res)
        val ok = in.LocalVar(Names.freshName, in.BoolT(Addressability.Exclusive))(l.info)
        val vprOk = ctx.typeEncoding.variable(ctx)(ok)

        seqn(
          for {
            _ <- local(vprRes)
            _ <- local(vprOk)

            e <- goE(base)
            id <- goE(idx)
            correspondingMap = vpr.DomainFuncApp(
              func = getMapFunc,
              args = Seq(vpr.FieldAccess(e, underlyingMapField)(pos, info, errT)),
              typVarMap = Map(keyParam -> goT(base.typ.asInstanceOf[in.MapT].keys), valueParam -> goT(idx.typ)))(pos, info, errT)
            dflt <- goE(in.DfltVal(idx.typ)(l.info))

            okCond = vpr.AnySetContains(id, vpr.MapDomain(correspondingMap)(pos, info, errT))(pos, info, errT)
            okAss = vpr.LocalVarAssign(vprOk.localVar, okCond)(pos, info, errT)
            _ <- write(okAss)

            // TODO: abstract this pattern in its own function
            lookupVal = vpr.CondExp(
              okCond,
              vpr.MapLookup(correspondingMap, id)(pos, info, errT),
              dflt
            )(pos, info, errT)
            lookupValAss = vpr.LocalVarAssign(vprRes.localVar, lookupVal)(pos, info, errT)
            _ <- write(lookupValAss)

            resAss <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(resTarget), res, l)
            _ <- write(resAss)

            // successTarget := ok
            okAss <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(successTarget), ok, l)
          } yield okAss
      )
    }
  }

  /** TODO: doc
    * Encodes an assignment.
    * The first and second argument is the left-hand side and right-hand side, respectively.
    *
    * To avoid conflicts with other encodings, an encoding for type T
    * should be defined at the following left-hand sides:
    * (1) exclusive variables of type T
    * (2) exclusive operations on type T (e.g. a field access for structs)
    * (3) shared expressions of type T
    * In particular, being defined at shared operations on type T causes conflicts with (3)
    *
    * Super implements:
    * [v: T° = rhs] -> VAR[v] = [rhs]
    * [loc: T@ = rhs] -> exhale Footprint[loc]; inhale Footprint[loc] && [loc == rhs]
    *
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    default(super.assignment(ctx)){
      case (in.Assignee(in.IndexedExp(m :: ctx.Map(k, v), idx)), rhs, src) => // if typ(ctx).isDefinedAt(t) =>
        val (pos, info, errT) = src.vprMeta
        val res = in.LocalVar(Names.freshName, m.typ.withAddressability(Addressability.Exclusive))(m.info)
        val vRes = ctx.typeEncoding.variable(ctx)(res)
        seqn(
          for {
            vRhs <- ctx.expr.translate(rhs)(ctx)
            vM <- ctx.expr.translate(m)(ctx)
            vIdx <- ctx.expr.translate(idx)(ctx)
            _ <- local(vRes)

            correspondingMapM = vpr.DomainFuncApp(
              func = getMapFunc,
              args = Seq(vpr.FieldAccess(vM, underlyingMapField)(pos, info, errT)),
              typVarMap = Map(keyParam -> goT(k), valueParam -> goT(v)))(pos, info, errT)

            correspondingMapRes = vpr.DomainFuncApp(
              func = getMapFunc,
              args = Seq(vRes.localVar),
              typVarMap = Map(keyParam -> goT(k), valueParam -> goT(v)))(pos, info, errT)

            // inhale1 = vpr.Inhale(vpr.FieldAccessPredicate(vpr.FieldAccess(vRes.localVar, underlyingMapField)(pos, info, errT), vpr.FullPerm()(pos, info, errT))(pos, info, errT))(pos, info, errT)
            inhale2 = vpr.Inhale(vpr.EqCmp(correspondingMapRes, vpr.MapUpdate(correspondingMapM, vIdx, vRhs)(pos, info, errT))(pos, info, errT))(pos, info, errT)
            // _ <- write(inhale1)
            _ <- write(inhale2)


            // exhale = vpr.Exhale(vpr.FieldAccessPredicate(vpr.FieldAccess(vM, underlyingMapField)(pos, info, errT), vpr.FullPerm()(pos, info, errT))(pos, info, errT))(pos, info, errT)
            // _ <- write(exhale)

            // inhale = vpr.Inhale(vpr.FieldAccessPredicate(vpr.FieldAccess(vM, underlyingMapField)(pos, info, errT), vpr.FullPerm()(pos, info, errT))(pos, info, errT))(pos, info, errT)
            // _ <- write(inhale)


            // eq = vpr.Inhale(vpr.EqCmp(correspondingMap, vpr.Old(vpr.MapUpdate(correspondingMap, vIdx, vRhs)(pos, info, errT))(pos, info, errT))(pos, info, errT))(pos, info, errT)
          } yield vpr.FieldAssign(vpr.FieldAccess(vM, underlyingMapField)(pos, info, errT), vRes.localVar)(pos, info, errT)
        )
    }
  }

  /** TODO: redo doc
    * Encodes assertions.
    *
    * Constraints:
    * - in.Access with in.PredicateAccess has to encode to vpr.PredicateAccessPredicate.
    *
    * [acc(p(e1, ..., en))] -> eval_S([p], [e1], ..., [en]) where p: pred(S)
    */
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    default(super.assertion(ctx)) {
      case n@ in.Access(in.Accessible.ExprAccess(exp :: ctx.Map(_, _)), perm) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vE <- goE(exp)
          vP <- goE(perm)
        } yield vpr.FieldAccessPredicate(vpr.FieldAccess(vE, underlyingMapField)(pos, info, errT), vP)(pos, info, errT)
    }
  }

  override def finalize(col: Collector): Unit = {
    col.addMember(genDomain())
    col.addMember(underlyingMapField)
  }


  private val keyParam = vpr.TypeVar("K")
  private val valueParam = vpr.TypeVar("V")

  /**
    * Generates
    *   domain GobraMap[K,V] {
    *     function getMapFromId(addr: Ref): Map[K,V] // TODO: make addr of type int instead of ref
    *     function getIdFromMap(Map[K,V]) // TODO: make addr of type int instead of ref. Rmove?
    *
    *     axiom nullMap {
    *       |getMap(null)| == 0
    *     }
    *
    *     axiom  { // TODO: remove?
    *       forall id: Ref :: getIdFromMap(getMapFromId(id)) = id
    *       forall m: Map[K,V] :: getMapFromId(getIdFromMap(m)) = m
    *     }
    *
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

  private val getMapFuncName: String = "getMapFromId"
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
}
