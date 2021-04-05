// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.maps

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability
import viper.gobra.theory.Addressability.Exclusive
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.LeafTypeEncoding
import viper.gobra.translator.interfaces.{Collector, Context}
import viper.gobra.translator.util.ViperWriter.CodeLevel.{seqn, seqns, unit}
import viper.gobra.translator.util.ViperWriter.CodeWriter
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

    default(super.expr(ctx)) {
      case (exp: in.DfltVal) :: ctx.Map(_, _) => unit(withSrc(vpr.NullLit(), exp))
      case (exp : in.NilLit) :: ctx.Map(_, _) / Exclusive => unit(withSrc(vpr.NullLit(), exp))
    }
  }

  /**
    * Encodes the allocation of a new map
    *
    *  [r := make(map[T1]T2, n)] ->
    *    asserts isComparable(T1)
    *    asserts 0 <= [n]
    *    var a [ map[T1]T2 ] = Map(underlyingMap) // TODO: must have field of type map
    *    inhales len(a) == 0 // TODO: check if it is ok
    *    r := a
    */
  override def statement(ctx: Context): in.Stmt ==> CodeWriter[vpr.Stmt] = {
    default(super.statement(ctx)) {
      case makeStmt@in.MakeMap(target, t@in.MapT(_, _, _), _) =>
        val (pos, info, errT) = makeStmt.vprMeta
        val mapVar = in.LocalVar(Names.freshName, t.withAddressability(Addressability.Exclusive))(makeStmt.info)
        //val vprMap = ctx.typeEncoding.variable(ctx)(mapVar)
        seqns(Vector())
        /*
        seqn(
          for {
            _ <- ???
          } yield ???
        )

        ???
         */
    }
  }

  override def finalize(col: Collector): Unit = {
    col.addMember(genDomain())
  }


  private val keyParam = vpr.TypeVar("K")
  private val valueParam = vpr.TypeVar("V")

  /**
    * Generates
    *   domain GobraMap[K,V] {
    *     function getMap(addr: Ref): Map[K,V]
    *
    *     axiom nullMap {
    *       // getMap(null) == Map[K,V]() // causes exception in silver
    *       |getMap(null)| == 0 // alternative
    *     }
    *   }
    */
  private def genDomain(): vpr.Domain = {
    vpr.Domain(
      name = domainName,
      functions = Seq(getMapFunc),
      axioms = Nil,// Seq(nullMapAxiom), // TODO: causes silver to crash
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
      val getMapNull = vpr.DomainFuncApp(getMapFunc, Seq(vpr.NullLit()()), Map.empty)()
      val emptyMap = vpr.EmptyMap(keyParam, valueParam)()
      vpr.EqCmp(getMapNull, emptyMap)()
    })(domainName = domainName)
  */

  private val nullMapAxiomName: String = "nullMap"
  private val nullMapAxiom: vpr.DomainAxiom = vpr.NamedDomainAxiom(
    name = nullMapAxiomName,
    exp = {
      val getMapNullSize = vpr.MapCardinality(vpr.DomainFuncApp(getMapFunc, Seq(vpr.NullLit()()), Map.empty)())()
      val zeroLit = vpr.IntLit(BigInt(0))()
      vpr.EqCmp(getMapNullSize, zeroLit)()
    })(domainName = domainName)
}
