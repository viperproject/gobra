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
import viper.gobra.translator.encodings.combinators.LeafTypeEncoding
import viper.gobra.translator.encodings.maps.MapEncoding.{checkKeyComparability, comparabilityErrorT, repeatedKeyErrorT}
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.{FunctionGenerator, MethodGenerator, PrimitiveGenerator}
import viper.gobra.translator.util.ViperWriter.CodeLevel._
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.gobra.util.Violation
import viper.silver.verifier.reasons.AssertionFalse
import viper.silver.verifier.{ErrorReason, errors => err}
import viper.silver.{ast => vpr}
import viper.silver.plugin.standard.termination

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

    default(super.expression(ctx)) {
      case (exp: in.DfltVal) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))

      case (exp: in.NilLit) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))

      case l@in.Length(exp :: ctx.Map(keys, values)) =>
        val (pos, info, errT) = l.vprMeta
        for {
          e <- goE(exp)
        } yield mapCardinalityGenerator(Vector(e), (keys, values))(pos, info, errT)(ctx)

      case l@in.IndexedExp(m :: ctx.Map(keys, values), k, _) =>
        val (pos, info, errT) = l.vprMeta
        for {
          base <- goE(m)
          key <-  goE(k)
        } yield mapLookupGenerator(Vector(base, key), (keys, values))(pos, info, errT)(ctx)

      case k@in.MapKeys(mapExp :: ctx.Map(keys, values), _) =>
        val (pos, info, errT) = k.vprMeta
        for {
         e <- goE(mapExp)
        } yield mapKeySetGenerator(Vector(e), (keys, values))(pos, info, errT)(ctx)

      case v@in.MapValues(mapExp :: ctx.Map(keys, values), _) =>
        val (pos, info, errT) = v.vprMeta
        for {
          e <- goE(mapExp)
        } yield mapValueSetGenerator(Vector(e), (keys, values))(pos, info, errT)(ctx)

    }
  }

  /**
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

        // Get optional param to make
        val nwriter = makeArg match {
          case Some(e) => goE(e)
          case None => unit(vpr.IntLit(0)(pos, info, errT))
        }
        val t = ctx.variable(target)
        for {
          n <- nwriter
          makeCall = makeMethodGenerator(Vector(n), Vector(t.localVar), (keys, values))(pos, info, errT)(ctx)
          _ <- errorT {
            case e@err.PreconditionInCallFalse(Source(info), _, _) if e causedBy makeCall =>
              PreconditionError(info) dueTo MapMakePreconditionFailed(info)
          }
        } yield makeCall

      case l@in.SafeMapLookup(resTarget, successTarget, indexedExp@in.IndexedExp(_, _, _)) =>
        val (pos, info, errT) = l.vprMeta
        val res = in.LocalVar(ctx.freshNames.next(), indexedExp.typ.withAddressability(Addressability.Exclusive))(l.info)
        val vprRes = ctx.variable(res)
        val ok = in.LocalVar(ctx.freshNames.next(), in.BoolT(Addressability.Exclusive))(l.info)
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
        val res = in.LocalVar(ctx.freshNames.next(), lit.typ.withAddressability(Exclusive))(lit.info)
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
    mapKeySetGenerator.finalize(addMemberFn)
    mapValueSetGenerator.finalize(addMemberFn)
    mapCardinalityGenerator.finalize(addMemberFn)
    mapLookupGenerator.finalize(addMemberFn)
    makeMethodGenerator.finalize(addMemberFn)
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

  private val mapKeySetGenerator: FunctionGenerator[(in.Type, in.Type)] = new FunctionGenerator[(in.Type, in.Type)] {
    /**
      * Generates viper function for computing the set of keys of a map with type parameters K and V
      *
      * function mapKeySetKV(m: [ Map[K,V] ]) returns (res: Ref)
      *   requires m != nil ==> acc(res.underlyingMapField, _)
      *   ensures  m == nil ==> res == set[K]{}
      *   ensures  m != nil ==> res == domain(res.underlyingMapField)
      *   decreases _
      */
    override def genFunction(x: (in.Type, in.Type))(ctx: Context): vpr.Function = {
      val paramDecl = vpr.LocalVarDecl("x", mapType)()
      val field = underlyingMapField(ctx)(x._1, x._2)
      val vprKeyT = ctx.typ(x._1)
      val resultT = vpr.SetType(vprKeyT)
      vpr.Function(
        name = internalMemberName("mapKeySet", x._1, x._2),
        formalArgs = Seq(paramDecl),
        typ = resultT,
        pres = Seq(
          vpr.Implies(
            vpr.NeCmp(paramDecl.localVar, vpr.NullLit()())(),
            vpr.FieldAccessPredicate(vpr.FieldAccess(paramDecl.localVar, field)(), vpr.WildcardPerm()())()
          )(),
          synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")
        ),
        posts = Seq(
          vpr.Implies(
            vpr.EqCmp(paramDecl.localVar, vpr.NullLit()())(),
            vpr.EqCmp(vpr.Result(resultT)(), vpr.EmptySet(vprKeyT)())()
          )(),
          vpr.Implies(
            vpr.NeCmp(paramDecl.localVar, vpr.NullLit()())(),
            vpr.EqCmp(vpr.Result(resultT)(), vpr.MapDomain(vpr.FieldAccess(paramDecl.localVar, field)())())()
          )()
        ),
        body = None
      )()
    }
  }

  private val mapValueSetGenerator: FunctionGenerator[(in.Type, in.Type)] = new FunctionGenerator[(in.Type, in.Type)] {
    /**
      * Generates viper function for computing the set of values of a map with type parameters K and V
      *
      * function mapValueSetKV(m: [ Map[K,V] ]) returns (res: Ref)
      *   requires m != nil ==> acc(res.underlyingMapField, _)
      *   ensures  m == nil ==> res == set[V]{}
      *   ensures  m != nil ==> res == range(res.underlyingMapField)
      *   decreases _
      */
    override def genFunction(x: (in.Type, in.Type))(ctx: Context): vpr.Function = {
      val paramDecl = vpr.LocalVarDecl("x", mapType)()
      val field = underlyingMapField(ctx)(x._1, x._2)
      val vprValueT = ctx.typ(x._2)
      val resultT = vpr.SetType(vprValueT)
      vpr.Function(
        name = internalMemberName("mapValueSet", x._1, x._2),
        formalArgs = Seq(paramDecl),
        typ = resultT,
        pres = Seq(
          vpr.Implies(
            vpr.NeCmp(paramDecl.localVar, vpr.NullLit()())(),
            vpr.FieldAccessPredicate(vpr.FieldAccess(paramDecl.localVar, field)(), vpr.WildcardPerm()())()
          )(),
          synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")
        ),
        posts = Seq(
          vpr.Implies(
            vpr.EqCmp(paramDecl.localVar, vpr.NullLit()())(),
            vpr.EqCmp(vpr.Result(resultT)(), vpr.EmptySet(vprValueT)())()
          )(),
          vpr.Implies(
            vpr.NeCmp(paramDecl.localVar, vpr.NullLit()())(),
            vpr.EqCmp(vpr.Result(resultT)(), vpr.MapRange(vpr.FieldAccess(paramDecl.localVar, field)())())()
          )()
        ),
        body = None
      )()
    }
  }

  private val mapCardinalityGenerator: FunctionGenerator[(in.Type, in.Type)] = new FunctionGenerator[(in.Type, in.Type)] {
    /**
      * Generates viper function for computing the cardinality of a map with type parameters K and V
      *
      * function mapCardinalityKV(m: [ Map[K,V] ]) returns (res: Int)
      *   requires m != nil ==> acc(res.underlyingMapField, _)
      *   ensures  res == |mapKeySetKV(res.underlyingMapField)|
      *   decreases _
      */
    override def genFunction(x: (in.Type, in.Type))(ctx: Context): vpr.Function = {
      val paramDecl = vpr.LocalVarDecl("x", mapType)()
      val field = underlyingMapField(ctx)(x._1, x._2)
      val resultT = vpr.Int
      vpr.Function(
        name = internalMemberName("mapCardinality", x._1, x._2),
        formalArgs = Seq(paramDecl),
        typ = resultT,
        pres = Seq(
          vpr.Implies(
            vpr.NeCmp(paramDecl.localVar, vpr.NullLit()())(),
            vpr.FieldAccessPredicate(vpr.FieldAccess(paramDecl.localVar, field)(), vpr.WildcardPerm()())()
          )(),
          synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")
        ),
        posts = Seq(
          vpr.EqCmp(vpr.Result(resultT)(), vpr.AnySetCardinality(mapKeySetGenerator(Vector(paramDecl.localVar), (x._1, x._2))()(ctx))())()
        ),
        body = None
      )()
    }
  }

  /*
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
   */

  private val mapLookupGenerator: FunctionGenerator[(in.Type, in.Type)] = new FunctionGenerator[(in.Type, in.Type)] {
    override def genFunction(x: (in.Type, in.Type))(ctx: Context): vpr.Function = {
      val field = underlyingMapField(ctx)(x._1, x._2)
      val vprKeyT = ctx.typ(x._1)
      val vprValueT = ctx.typ(x._2)
      val mapParamDecl = vpr.LocalVarDecl("x", mapType)()
      val keyParamDecl = vpr.LocalVarDecl("k", vprKeyT)()
      val dfltVal = in.DfltVal(x._2)(Source.Parser.Internal)
      val vprDfltVal = pure(ctx.expression(dfltVal))(ctx).res
      vpr.Function(
        name = internalMemberName("mapLookup", x._1, x._2),
        formalArgs = Seq(mapParamDecl, keyParamDecl),
        typ = vprValueT,
        pres = Seq(
          vpr.Implies(
            vpr.NeCmp(mapParamDecl.localVar, vpr.NullLit()())(),
            vpr.FieldAccessPredicate(vpr.FieldAccess(mapParamDecl.localVar, field)(), vpr.WildcardPerm()())()
          )(),
          synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")
        ),
        posts = Seq(
          //vpr.EqCmp(vpr.Result(resultT)(), vpr.AnySetCardinality(mapKeySetGenerator(Vector(paramDecl.localVar), (x._1, x._2))()(ctx))())()
          vpr.Implies(
            vpr.AnySetContains(keyParamDecl.localVar, mapKeySetGenerator(Vector(mapParamDecl.localVar), (x._1, x._2))()(ctx))(),
            vpr.EqCmp(vpr.Result(vprValueT)(), vpr.MapLookup(vpr.FieldAccess(mapParamDecl.localVar, field)(), keyParamDecl.localVar)())()
          )(),
          vpr.Implies(
            vpr.Not(vpr.AnySetContains(keyParamDecl.localVar, mapKeySetGenerator(Vector(mapParamDecl.localVar), (x._1, x._2))()(ctx))())(),
            vpr.EqCmp(vpr.Result(vprValueT)(), vprDfltVal)()
          )()
        ),
        body = None
      )()
    }
  }

  private val makeMethodGenerator: MethodGenerator[(in.Type, in.Type)] = new MethodGenerator[(in.Type, in.Type)] {
    /**
      * Generates viper method for making maps with keys of type K and values of type V:
      *
      * method makeMapMethodKV(n: Int) returns (res: Ref)
      *   requires 0 <= n
      *   ensures  acc(res.underlyingMapField)
      *   ensures  res.underlyingMapField == EmptyMap[K,V]
      *   decreases _
      */
    override def genMethod(types: (in.Type, in.Type))(ctx: Context): vpr.Method = {
      val paramDecl = vpr.LocalVarDecl("n", vpr.Int)()
      val keyT = types._1
      val valT = types._2
      val vprKeyT = ctx.typ(keyT)
      val vprValT = ctx.typ(valT)
      val resultT = ctx.typ(in.MapT(keyT, valT, Addressability.outParameter))
      val result = vpr.LocalVarDecl("res", resultT)()
      val underlyingField = underlyingMapField(ctx)(keyT, valT)
      val post1 = vpr.FieldAccessPredicate(vpr.FieldAccess(result.localVar, underlyingField)(), vpr.FullPerm()())()
      val post2 = vpr.EqCmp(
        vpr.FieldAccess(result.localVar, underlyingField)(),
        vpr.EmptyMap(vprKeyT, vprValT)()
      )()
      vpr.Method(
        name = internalMemberName("makeMapMethod", keyT, valT),
        formalArgs = Seq(paramDecl),
        formalReturns = Seq(result),
        pres = Seq(
          vpr.LeCmp(vpr.IntLit(0)(), paramDecl.localVar)(), // 0 <= n
          synthesized(termination.DecreasesWildcard(None))("This function is assumed to terminate")
        ),
        posts = Seq(post1, post2),
        body = None,
      )()
    }
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
