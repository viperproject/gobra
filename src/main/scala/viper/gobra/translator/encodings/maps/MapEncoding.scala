// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2021 ETH Zurich.

package viper.gobra.translator.encodings.maps

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting._
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.encodings.maps.MapEncoding.{checkKeyComparability, comparabilityErrorT, repeatedKeyErrorT}
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeLevel._
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.verifier.reasons.AssertionFalse
import viper.silver.verifier.{ErrorReason, errors => err}
import viper.silver.{ast => vpr}

/**
  * Encoding for Go maps. Unlike slices, maps are not thread-safe;
  * thus, all concurrent accesses to maps must be synchronized. In particular,
  * assigning to different indices of a slice cannot occur in parallel. Besides,
  * obtaining the length of a map requires read permissions because maps may
  * change in size unlike slices, e.g.
  *    m := make(map[int]int)
  *    go f(m) // f(m) sets m[10] to 10
  *    go g(m) // where g(m) computes len(m)
  * This encoding prevents Gobra from verifying this example, which is correct
  * because it contains a race condition.
  */
class MapEncoding extends LeafTypeEncoding {
  import viper.gobra.translator.util.TypePatterns._

  private val domainName: String = Names.mapsDomain

  /**
    * Translates a type into a Viper type.
    * Both Exclusive and Shared maps are encoded as vpr.Ref because nil is an admissible value for maps
    */
  override def typ(ctx : Context) : in.Type ==> vpr.Type = {
    case ctx.Map(_, _) / Exclusive => mapType
    case ctx.Map(_, _) / Shared => vpr.Ref
  }

  private lazy val mapType: vpr.Type = {
    isUsed = true
    vpr.Ref
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    * R[ nil(map[K]V°) ] -> null
    * R[ dflt(map[K]V°) ] -> null
    * R[ len(e: map[K]V) ] -> [e] == null? 0 : | getCorrespondingMap([e]) |
    * R[ (e: map[K]V)[idx] ] -> goMapLookup(e[idx])
    * R[ map[K]V { idx1: v1 ... idxn: vn } ] ->
    *   e s.t. getCorrespondingMap(e) == { [idx1]: [v1] ... [idxn]: [vn] } (also checks that the values of keys are all
    *   distinct and throws an error if not)
    * R[ keySet(e: map[K]V) ] -> MapDomain(getCorrespondingMap(e))
    * R[ valueSet(e: map[K]V) ] -> MapRange(getCorrespondingMap(e))
    */
  override def expr(ctx : Context) : in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    default(super.expr(ctx)) {
      case (exp: in.DfltVal) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))

      case (exp: in.NilLit) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))

      case l@ in.Length(exp :: ctx.Map(keys, values)) =>
        val (pos, info, errT) = l.vprMeta
        // Encodes
        // [ len(m) ] -> [ m ] == null? 0 : | getCorrespondingMap([m]) |
        for {
          e <- goE(exp)
          correspondingMap <- getCorrespondingMap(exp, keys, values)(ctx)
          res = vpr.CondExp(
            vpr.EqCmp(e, vpr.NullLit()(pos, info, errT))(pos, info, errT),
            vpr.IntLit(BigInt(0))(pos, info, errT),
            vpr.MapCardinality(correspondingMap)(pos, info, errT)
          )(pos, info, errT)
        } yield res

      case l@in.IndexedExp(_ :: ctx.Map(_, _), _) => for { (res, _) <- goMapLookup(l)(ctx) } yield res

      case (lit: in.MapLit) :: ctx.Map(keys, values) =>
        val (pos, info, errT) = lit.vprMeta
        val res = in.LocalVar(Names.freshName, lit.typ.withAddressability(Exclusive))(lit.info)
        val vRes = ctx.typeEncoding.variable(ctx)(res)

        for {
          mapletList <- sequence(lit.entries.toVector.map {
            case (key, value) => for {
              kVpr <- goE(key)
              isCompKey <- checkKeyComparability(key)(ctx)
              k <- assert(isCompKey, kVpr, comparabilityErrorT)(ctx) // key must be comparable
              v <- goE(value)
            } yield vpr.Maplet(k, v)(pos, info, errT)
          })

          underlyingMap <- if (mapletList.nonEmpty) {
            // silver assumes that the argument of ExplicitMap is not empty
            val keySeq = mapletList map (_.key)
            // checks whether all keys are distinct
            val checkAllDiffKeys = vpr.EqCmp(
              vpr.AnySetCardinality(vpr.ExplicitSet(keySeq)(pos, info, errT))(pos, info, errT),
              vpr.SeqLength(vpr.ExplicitSeq(keySeq)(pos, info, errT))(pos, info, errT)
            )(pos, info, errT)
            assert(checkAllDiffKeys, vpr.ExplicitMap(mapletList)(pos, info, errT), repeatedKeyErrorT)(ctx)
          } else {
            unit(vpr.EmptyMap(goT(keys), goT(values))(pos, info, errT))
          }
          _ <- local(vRes)
          correspondingMap <- getCorrespondingMap(res, keys, values)(ctx)
          // inhale acc(res.underlyingMapField)
          _ <- write(
            vpr.Inhale(
              vpr.FieldAccessPredicate(
                vpr.FieldAccess(vRes.localVar, underlyingMapField)(pos, info, errT),
                vpr.FullPerm()(pos, info, errT))(pos, info, errT))(pos, info, errT))
          // inhale getCorrespondingMap(res) == underlyingMap; recall that underlyingMap == ExplicitMap(mapletList)
          _ <- write(vpr.Inhale(vpr.EqCmp(underlyingMap, correspondingMap)(pos, info, errT))(pos, info, errT))
        } yield vRes.localVar

      case k@ in.MapKeys(mapExp :: ctx.Map(keys, values)) =>
        for {
          correspondingMap <- getCorrespondingMap(mapExp, keys, values)(ctx)
        } yield withSrc(vpr.MapDomain(correspondingMap), k)

      case v@ in.MapValues(mapExp:: ctx.Map(keys, values)) =>
        for {
          correspondingMap <- getCorrespondingMap(mapExp, keys, values)(ctx)
        } yield withSrc(vpr.MapRange(correspondingMap), v)
    }
  }

  /**
    * Encodes the allocation of a new map
    *  [r := make(map[T1]T2, n)] ->
    *    asserts 0 <= [n]
    *    var a Ref := new(underlyingMapField)
    *    inhales len(m) == 0, where m is the underlying map of a
    *    [r] := a
    *
    *  [v, ok := exp[idx] ->
    *     var res [T1]
    *     var ok' Bool
    *     let (lookupVal, okCond) be the result of goMapLookup(exp[idx])
    *     res := lookupVal
    *     ok' := okCond
    *     [v] := res
    *     [ok] := ok'
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

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
            _ <- write(checks: _*)
            _ <- if (checks.nonEmpty) errorT {
              case e@err.ExhaleFailed(Source(info), _, _) if checks.nonEmpty && e.causedBy(checks(0)) =>
                MapMakePreconditionError(info)
            } else unit(())

            mapVar = in.LocalVar(Names.freshName, t.withAddressability(Exclusive))(makeStmt.info)
            mapVarVpr = ctx.typeEncoding.variable(ctx)(mapVar)
            _ <- local(mapVarVpr)

            correspondingMap <- getCorrespondingMap(mapVar, keys, values)(ctx)
            _ <- write(vpr.NewStmt(mapVarVpr.localVar, Seq(underlyingMapField))(pos, info, errT))
            _ <- write(vpr.Inhale(vpr.EqCmp(correspondingMap, vpr.EmptyMap(goT(keys), goT(values))(pos, info, errT))(pos, info, errT))(pos, info, errT))
            ass <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(target), mapVar, makeStmt)
          } yield ass
        )

      case l@ in.SafeMapLookup(resTarget, successTarget, indexedExp@ in.IndexedExp(_, _)) =>
        val (pos, info, errT) = l.vprMeta
        val res = in.LocalVar(Names.freshName, indexedExp.typ.withAddressability(Addressability.Exclusive))(l.info)
        val vprRes = ctx.typeEncoding.variable(ctx)(res)
        val ok = in.LocalVar(Names.freshName, in.BoolT(Addressability.Exclusive))(l.info)
        val vprOk = ctx.typeEncoding.variable(ctx)(ok)

        seqn(
          for {
            _ <- local(vprRes)
            _ <- local(vprOk)

            (lookupVal, okCond) <- goMapLookup(indexedExp)(ctx)
            lookupValAss = vpr.LocalVarAssign(vprRes.localVar, lookupVal)(pos, info, errT)
            okAss = vpr.LocalVarAssign(vprOk.localVar, okCond)(pos, info, errT)
            _ <- write(okAss)
            _ <- write(lookupValAss)

            resAss <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(resTarget), res, l)
            _ <- write(resAss)

            okAss <- ctx.typeEncoding.assignment(ctx)(in.Assignee.Var(successTarget), ok, l)
          } yield okAss
      )
    }
  }

  /**
    * Encodes whether a value is comparable or not.
    */
  override def isComparable(ctx: Context): in.Expr ==> Either[Boolean, CodeWriter[vpr.Exp]] = {
    case _ :: ctx.Map(_, _) => Left[Boolean, CodeWriter[vpr.Exp]](false)
  }

  /**
    * Encodes an assignment.
    * [ mapExp[idx] = newVal ] ->
    *     var res: Ref
    *     var m: Map[ [k], [v] ]
    *     m = getCorrespondingMap(mapExp)
    *     inhale getMap(res) == m[ [idx] = [newVal] ]
    *     m.underlyingField = res
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = {
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    default(super.assignment(ctx)){
      case (in.Assignee(in.IndexedExp(m :: ctx.Map(keys, values), idx)), rhs, src) =>
        val (pos, info, errT) = src.vprMeta
        val res = in.LocalVar(Names.freshName, in.IntT(Exclusive))(m.info)
        val vRes = ctx.typeEncoding.variable(ctx)(res)
        seqn(
          for {
            isCompKey <- MapEncoding.checkKeyComparability(idx)(ctx)
            _ <- assert(isCompKey, comparabilityErrorT) // key must be comparable

            vRhs <- ctx.expr.translate(rhs)(ctx)
            vM <- ctx.expr.translate(m)(ctx)
            vIdx <- ctx.expr.translate(idx)(ctx)
            _ <- local(vRes)

            correspondingMapM <- getCorrespondingMap(m, keys, values)(ctx)

            // cannot be replaced by `getCorrespondingMap(vRes, goT(k), goT(v)`, there is no field access to the underlyingMapField
            correspondingMapRes = vpr.DomainFuncApp(
              func = getMapFunc,
              args = Seq(vRes.localVar),
              typVarMap = Map(keyParam -> goT(keys), valueParam -> goT(values))
            )(pos, info, errT)

            inhale = vpr.Inhale(vpr.EqCmp(correspondingMapRes, vpr.MapUpdate(correspondingMapM, vIdx, vRhs)(pos, info, errT))(pos, info, errT))(pos, info, errT)
            _ <- write(inhale)
          } yield vpr.FieldAssign(vpr.FieldAccess(vM, underlyingMapField)(pos, info, errT), vRes.localVar)(pos, info, errT)
        )
    }
  }

  /**
    * Encodes assertions.
    * [acc(m: map[K]V, perm)] -> acc([m].underlyingMapField, [perm])
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
    if (isUsed) {
      col.addMember(genDomain())
    }
  }

  private var isUsed: Boolean = false

  private val keyParam = vpr.TypeVar("K")
  private val valueParam = vpr.TypeVar("V")

  /**
    * Generates
    *   domain GobraMap[K,V] {
    *     function getMap(addr: Int): Map[K,V]
    *   }
    */
  private def genDomain(): vpr.Domain = {
    vpr.Domain(
      name = domainName,
      functions = Seq(getMapFunc),
      axioms = Seq(),
      typVars = Seq(keyParam, valueParam)
    )()
  }

  private val getMapFuncName: String = "getMap"
  private val getMapFunc: vpr.DomainFunc = vpr.DomainFunc(
    name = getMapFuncName,
    formalArgs = Seq(vpr.LocalVarDecl("id", vpr.Int)()),
    typ = vpr.MapType(keyParam, valueParam),
  )(domainName = domainName)

  /**
    * Field of the corresponding map id
    */
  private val underlyingMapFieldName: String = "underlyingMapField"
  private def underlyingMapField: vpr.Field = vpr.Field(underlyingMapFieldName, vpr.Int)()

  /**
    * Builds the expression `getMap([exp].underlyingMapField)`
    */
  private def getCorrespondingMap(exp: in.Expr, keys: in.Type, values: in.Type)(ctx: Context): CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)
    def goT(t: in.Type): vpr.Type = ctx.typeEncoding.typ(ctx)(t)

    for {
      vExp <- goE(exp)
      res = withSrc(vpr.DomainFuncApp(
        func = getMapFunc,
        args = Seq(withSrc(vpr.FieldAccess(vExp, underlyingMapField), exp)),
        typVarMap = Map(keyParam -> goT(keys), valueParam -> goT(values))
      ), exp)
    } yield res
  }

  /**
    * Builds the expression `idx in Domain(vprMap)`
    */
  private def goMapContains(vprMap: vpr.Exp, idx: vpr.Exp)(src: in.Node): vpr.Exp =
    withSrc(vpr.AnySetContains(idx, withSrc(vpr.MapDomain(vprMap), src)), src)

  /**
    * Computes the result of looking up a value in an indexed expression and a bool expression asserting
    * whether the key is in the map
    */
  private def goMapLookup(lookupExp: in.IndexedExp)(ctx: Context): CodeWriter[(vpr.Exp, vpr.Exp)] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expr.translate(x)(ctx)

    lookupExp match {
      case l@in.IndexedExp(exp :: ctx.Map(keys, values), idx) =>
        for {
          vIdx <- goE(idx)
          isComp <- MapEncoding.checkKeyComparability(idx)(ctx)
          vDflt <- goE(in.DfltVal(values)(l.info))
          correspondingMap <- getCorrespondingMap(exp, keys, values)(ctx)
          containsExp = goMapContains(correspondingMap, vIdx)(l)
          lookupRes = withSrc(vpr.CondExp(containsExp, withSrc(vpr.MapLookup(correspondingMap, vIdx), l), vDflt), l)
          lookupResCheckComp <- assert(isComp, lookupRes, comparabilityErrorT)(ctx)
        } yield (lookupResCheckComp, containsExp)
      case _ => Violation.violation(s"unexpected case reached")
    }
  }
}

object MapEncoding {
  protected[maps] def checkKeyComparability(key: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {
    val isComp = ctx.typeEncoding.isComparable(ctx)(key)
    val (pos, info, errT) = key.vprMeta

    isComp match {
      case Left(false) => unit[vpr.Exp](vpr.FalseLit()(pos, info, errT))
      case Left(true) => unit[vpr.Exp](vpr.TrueLit()(pos, info, errT))
      case Right(compExp) => compExp
    }
  }

  protected[maps] val comparabilityErrorT: (Source.Verifier.Info, ErrorReason) => VerificationError = {
    case (info, AssertionFalse(_)) => AssertError(info) dueTo KeyNotComparableReason(info)
    case _ => Violation.violation("unexpected case reached")
  }

  protected[maps] val repeatedKeyErrorT: (Source.Verifier.Info, ErrorReason) => VerificationError = {
    case (info, AssertionFalse(_)) => AssertError(info) dueTo RepeatedMapKeyReason(info)
    case _ => Violation.violation("unexpected case reached")
  }
}
