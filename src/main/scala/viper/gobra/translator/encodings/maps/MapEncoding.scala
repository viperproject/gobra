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
import viper.gobra.frontend.info.implementation.typing.modifiers.owner.OwnerModifier.{ Shared, Exclusive }
import viper.gobra.frontend.info.implementation.typing.modifiers.owner.OwnerModifier
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.encodings.maps.MapEncoding.{checkKeyComparability, comparabilityErrorT, repeatedKeyErrorT}
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.PrimitiveGenerator
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

  /**
    * Translates a type into a Viper type.
    * Both Exclusive and Shared maps are encoded as vpr.Ref because nil is an admissible value for maps
    */
  override def typ(ctx: Context): in.Type ==> vpr.Type = {
    case ctx.Map(_, _) / Exclusive => mapType
    case ctx.Map(_, _) / Shared => vpr.Ref
  }

  private val mapType: vpr.Type = vpr.Ref

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    * R[ nil(map[K]V°) ] -> null
    * R[ dflt(map[K]V°) ] -> null
    * R[ len(e: map[K]V) ] -> [e] == null? 0 : | getCorrespondingMap([e]) |
    * R[ (e: map[K]V)[idx] ] -> [e] == null? [ dflt(V) ] : goMapLookup(e[idx])
    * R[ keySet(e: map[K]V) ] -> [e] == null? 0 : MapDomain(getCorrespondingMap(e))
    * R[ valueSet(e: map[K]V) ] -> [e] == null? 0 : MapRange(getCorrespondingMap(e))
    */
  override def expression(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    def goT(t: in.Type): vpr.Type = ctx.typ(t)

    default(super.expression(ctx)) {
      case (exp: in.DfltVal) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))

      case (exp: in.NilLit) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))

      case l@in.Length(exp :: ctx.Map(keys, values)) =>
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

      case l@in.IndexedExp(_ :: ctx.Map(_, _), _, _) => for {(res, _) <- goMapLookup(l)(ctx)} yield res

      case k@in.MapKeys(mapExp :: ctx.Map(keys, values), _) =>
        for {
          vprMap <- goE(mapExp)
          correspondingMap <- getCorrespondingMap(mapExp, keys, values)(ctx)
          correspondingMapDomain = withSrc(vpr.MapDomain(correspondingMap), k)
          res = withSrc(vpr.CondExp(
            withSrc(vpr.EqCmp(vprMap, withSrc(vpr.NullLit(), k)), k),
            withSrc(vpr.EmptySet(goT(keys)), k),
            correspondingMapDomain
          ), k)
        } yield res

      case v@in.MapValues(mapExp :: ctx.Map(keys, values), _) =>
        for {
          vprMap <- goE(mapExp)
          correspondingMap <- getCorrespondingMap(mapExp, keys, values)(ctx)
          correspondingMapRange = withSrc(vpr.MapRange(correspondingMap), v)
          res = withSrc(vpr.CondExp(
            withSrc(vpr.EqCmp(vprMap, withSrc(vpr.NullLit(), v)), v),
            withSrc(vpr.EmptySet(goT(values)), v),
            correspondingMapRange
          ), v)
        } yield res
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
    *
    *  R[ map[K]V { idx1: v1 ... idxn: vn } ] ->
    *     e s.t. getCorrespondingMap(e) == { [idx1]: [v1] ... [idxn]: [vn] } (also checks that the values of keys are all
    *     distinct and throws an error if not)
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)
    def goT(t: in.Type): vpr.Type = ctx.typ(t)

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
              case e@err.ExhaleFailed(Source(info), _, _) if e.causedBy(checks(0)) =>
                MapMakePreconditionError(info)
            } else unit(())

            mapVar = in.LocalVar(ctx.freshNames.next(), t.withOwnerModifier(Exclusive))(makeStmt.info)
            mapVarVpr = ctx.variable(mapVar)
            _ <- local(mapVarVpr)

            correspondingMap <- getCorrespondingMap(mapVar, keys, values)(ctx)
            _ <- write(vpr.NewStmt(mapVarVpr.localVar, Seq(underlyingMapField(ctx)(keys, values)))(pos, info, errT))
            _ <- write(vpr.Inhale(vpr.EqCmp(correspondingMap, vpr.EmptyMap(goT(keys), goT(values))(pos, info, errT))(pos, info, errT))(pos, info, errT))
            ass <- ctx.assignment(in.Assignee.Var(target), mapVar)(makeStmt)
          } yield ass
        )

      case l@in.SafeMapLookup(resTarget, successTarget, indexedExp@in.IndexedExp(_, _, _)) =>
        val (pos, info, errT) = l.vprMeta
        val res = in.LocalVar(ctx.freshNames.next(), indexedExp.typ.withOwnerModifier(OwnerModifier.Exclusive))(l.info)
        val vprRes = ctx.variable(res)
        val ok = in.LocalVar(ctx.freshNames.next(), in.BoolT(OwnerModifier.Exclusive))(l.info)
        val vprOk = ctx.variable(ok)

        seqn(
          for {
            _ <- local(vprRes)
            _ <- local(vprOk)

            (lookupVal, okCond) <- goMapLookup(indexedExp)(ctx)
            lookupValAss = vpr.LocalVarAssign(vprRes.localVar, lookupVal)(pos, info, errT)
            okAss = vpr.LocalVarAssign(vprOk.localVar, okCond)(pos, info, errT)
            _ <- write(okAss)
            _ <- write(lookupValAss)

            resAss <- ctx.assignment(in.Assignee.Var(resTarget), res)(l)
            _ <- write(resAss)

            okAss <- ctx.assignment(in.Assignee.Var(successTarget), ok)(l)
          } yield okAss
        )

      case lit: in.NewMapLit =>
        val (pos, info, errT) = lit.vprMeta
        val res = in.LocalVar(ctx.freshNames.next(), lit.typ.withOwnerModifier(Exclusive))(lit.info)
        val vRes = ctx.variable(res)

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
            unit(vpr.EmptyMap(goT(lit.keys), goT(lit.values))(pos, info, errT))
          }
          _ <- local(vRes)
          correspondingMap <- getCorrespondingMap(res, lit.keys, lit.values)(ctx)
          // inhale acc(res.underlyingMapField)
          _ <- write(
            vpr.Inhale(
              vpr.FieldAccessPredicate(
                vpr.FieldAccess(vRes.localVar, underlyingMapField(ctx)(lit.keys, lit.values))(pos, info, errT),
                vpr.FullPerm()(pos, info, errT))(pos, info, errT))(pos, info, errT))
          // inhale getCorrespondingMap(res) == underlyingMap; recall that underlyingMap == ExplicitMap(mapletList)
          _ <- write(vpr.Inhale(vpr.EqCmp(underlyingMap, correspondingMap)(pos, info, errT))(pos, info, errT))
          ass <- ctx.assignment(in.Assignee.Var(lit.target), res)(lit)
        } yield ass
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
    *     var m: Map[ [k], [v] ]
    *     m = [mapExp].underlyingMapField
    *     [mapExp].underlyingMapField := m[ [idx] = [newVal] ]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = {

    default(super.assignment(ctx)) {
      case (in.Assignee(in.IndexedExp(m :: ctx.Map(keys, values), idx, _)), rhs, src) =>
        val (pos, info, errT) = src.vprMeta
        seqn(
          for {
            isCompKey <- MapEncoding.checkKeyComparability(idx)(ctx)
            _ <- assert(isCompKey, comparabilityErrorT) // key must be comparable
            vRhs <- ctx.expression(rhs)
            vIdx <- ctx.expression(idx)
            correspondingMapM <- getCorrespondingMap(m, keys, values)(ctx)
          } yield vpr.FieldAssign(correspondingMapM, vpr.MapUpdate(correspondingMapM, vIdx, vRhs)(pos, info, errT))(pos, info, errT)
        )
    }
  }

  /**
    * Encodes assertions.
    * [acc(m: map[K]V, perm)] -> acc([m].underlyingMapField, [perm])
    */
  override def assertion(ctx: Context): in.Assertion ==> CodeWriter[vpr.Exp] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    default(super.assertion(ctx)) {
      case n@in.Access(in.Accessible.ExprAccess(exp :: ctx.Map(keys, values)), perm) =>
        val (pos, info, errT) = n.vprMeta
        for {
          vE <- goE(exp)
          vP <- goE(perm)
        } yield vpr.FieldAccessPredicate(vpr.FieldAccess(vE, underlyingMapField(ctx)(keys, values))(pos, info, errT), vP)(pos, info, errT)
    }
  }

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    underlyingMapFieldGenerator.finalize(addMemberFn)
  }

  /**
    * Builds the expression `[exp].underlyingMapField`
    */
  private def getCorrespondingMap(exp: in.Expr, keys: in.Type, values: in.Type)(ctx: Context): CodeWriter[vpr.FieldAccess] = {
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    for {
      vExp <- goE(exp)
      res = withSrc(vpr.FieldAccess(vExp, underlyingMapField(ctx)(keys, values)), exp)
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
    def goE(x: in.Expr): CodeWriter[vpr.Exp] = ctx.expression(x)

    lookupExp match {
      case l@in.IndexedExp(exp :: ctx.Map(keys, values), idx, _) =>
        for {
          vIdx <- goE(idx)
          isComp <- MapEncoding.checkKeyComparability(idx)(ctx)
          vDflt <- goE(in.DfltVal(values)(l.info))
          mapVpr <- goE(exp)
          correspondingMap <- getCorrespondingMap(exp, keys, values)(ctx)
          containsExp = withSrc(vpr.CondExp(
            withSrc(vpr.EqCmp(mapVpr, withSrc(vpr.NullLit(), l)), l),
            withSrc(vpr.FalseLit(), l),
            goMapContains(correspondingMap, vIdx)(l)),
            l)
          lookupRes = withSrc(vpr.CondExp(containsExp, withSrc(vpr.MapLookup(correspondingMap, vIdx), l), vDflt), l)
          lookupResCheckComp <- assert(isComp, lookupRes, comparabilityErrorT)(ctx)
        } yield (lookupResCheckComp, containsExp)
      case _ => Violation.violation(s"unexpected case reached")
    }
  }

  private def internalMemberName(prefix: String, k: in.Type, v: in.Type): String = {
    val kN = Names.serializeType(k)
    val vN = Names.serializeType(v)
    s"$prefix$$$kN$$$vN"
  }

  /**
    * Field of the corresponding map type
    */
  private val underlyingMapFieldPrefix: String = "underlyingMapField"
  private def underlyingMapField(ctx: Context)(k: in.Type, v: in.Type): vpr.Field = {
    val name = internalMemberName(underlyingMapFieldPrefix, k, v)
    val vprK = ctx.typ(k)
    val vprV = ctx.typ(v)
    underlyingMapFieldGenerator(name, vprK, vprV)
  }

  private val underlyingMapFieldGenerator: PrimitiveGenerator.PrimitiveGenerator[(String, vpr.Type, vpr.Type), vpr.Field] =
    PrimitiveGenerator.simpleGenerator {
      case (name: String, k: vpr.Type, v: vpr.Type) =>
        val f = vpr.Field(name = name, typ = vpr.MapType(k, v))()
        (f, Vector(f))
    }
}

object MapEncoding {
  protected[maps] def checkKeyComparability(key: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {
    val isComp = ctx.isComparable(key)
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
