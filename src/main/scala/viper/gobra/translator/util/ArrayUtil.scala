package viper.gobra.translator.util

import viper.gobra.ast.internal.LocalVar

import scala.collection.immutable.IndexedSeq
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}

object ArrayUtil {

  /* ** Functionality */

  def access(base : vpr.Exp, baseType : in.ArrayT, index : vpr.Exp)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    baseType match {
      case in.ArrayT(_, _, Addressability.Exclusive) => vpr.SeqIndex(base, index)(pos, info, errT)
      case in.ArrayT(_, t, Addressability.Shared) => ctx.loc.arrayIndexField(base, index, t)(ctx)(pos, info, errT)
    }
  }

  def access(base : vpr.Exp, baseType : in.ArrayT, indices : IndexedSeq[vpr.Exp])(src : in.Node)(ctx : Context) : vpr.Exp = {
    indices match {
      case Vector() => base
      case index +: tail =>
        val elem = access(base, baseType, index)(src)(ctx)
        baseType.elems match {
          case t: in.ArrayT => access(elem, t, tail)(src)(ctx)
          case _ => elem
        }
    }
  }

  /**
    * Gives a fresh local variable for an `typ`-typed array.
    */
  def anonymousLocalVar(typ : in.ArrayT)(info : Source.Parser.Info): LocalVar.Inter =
    in.LocalVar.Inter(Names.freshName, typ)(info)

  def boundaryCondition(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    vpr.And(
      vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), base)(pos, info, errT),
      vpr.LtCmp(base, vpr.IntLit(baseType.length)(pos, info, errT))(pos, info, errT)
    )(pos, info, errT)
  }

  def boundaryCondition(base : IndexedSeq[(vpr.Exp, in.ArrayT)])(src : in.Node) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val init = vpr.TrueLit()(pos, info, errT)

    base.map(e => boundaryCondition(e._1, e._2)(src)).foldLeft[vpr.Exp](init) {
      case (l, r) => vpr.And(l, r)(pos, info, errT)
    }
  }

  def defaultValueAssumption(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node)(ctx : Context) : vpr.Stmt =
    inhale(defaultValueCondition(base, baseType)(src)(ctx))(src)

  def defaultValueCondition(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val dims = dimensions(baseType).last
    val decls = dims.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
    val declVars = decls.map(d => d.localVar)
    val tailVars = declVars.zip(dims)
    val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
    val accessExp = access(base, dims.head, declVars)(src)(ctx)
    val dflt = ctx.loc.defaultValue(dims.last.elems)(src)(ctx).res

    vpr.Forall(
      decls,
      Seq(vpr.Trigger(Seq(accessExp))(pos, info, errT)),
      vpr.Implies(
        conditions,
        vpr.EqCmp(accessExp, dflt)(pos, info, errT)
      )(pos, info, errT)
    )(pos, info, errT)
  }

  def equalsAssumption(left : vpr.Exp, leftType : in.ArrayT, right : vpr.Exp, rightType : in.ArrayT)(src : in.Node)(ctx : Context) : vpr.Stmt =
    inhale(equalsCondition(left, leftType, right, rightType)(src)(ctx))(src)

  def equalsCondition(left : vpr.Exp, leftType : in.ArrayT, right : vpr.Exp, rightType : in.ArrayT)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    if (left.typ.isInstanceOf[vpr.SeqType] && right.typ.isInstanceOf[vpr.SeqType]) {
      vpr.EqCmp(left, right)(pos, info, errT)
    } else {
      val leftDims = dimensions(leftType).last
      val rightDims = dimensions(rightType).last

      require(leftDims.length == rightDims.length, s"cannot create an equality condition for two arrays of different dimensions")

      val decls = leftDims.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
      val declVars = decls.map(d => d.localVar)
      val tailVars = declVars.zip(leftDims)
      val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
      val accessLeft = access(left, leftType, declVars)(src)(ctx)
      val accessRight = access(right, rightType, declVars)(src)(ctx)

      vpr.Forall(
        decls,
        Seq(
          vpr.Trigger(Seq(accessLeft))(pos, info, errT),
          vpr.Trigger(Seq(accessRight))(pos, info, errT)
        ),
        vpr.Implies(
          conditions,
          vpr.EqCmp(accessLeft, accessRight)(pos, info, errT)
        )(pos, info, errT)
      )(pos, info, errT)
    }
  }

  def footprintAssumptions(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node)(ctx : Context) : Vector[vpr.Stmt] =
    footprintConditions(base, baseType)(src)(ctx).map(inhale(_)(src))

  def footprintCondition(array : vpr.Exp, arrayType : in.ArrayT)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val base = vpr.TrueLit()(pos, info, errT)

    footprintConditions(array, arrayType)(src)(ctx).foldLeft[vpr.Exp](base) {
      case (a, b) => vpr.And(a, b)(pos, info, errT)
    }
  }

  def footprintConditions(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node)(ctx : Context) : Vector[vpr.Exp] = {
    val lengths = lengthConditions(base, baseType)(src)(ctx)

    baseType match {
      case a: in.ArrayT if a.addressability.isExclusive => lengths
      case baseType: in.ArrayT if baseType.addressability.isShared =>
        val ownerships = ownershipConditions(base, baseType)(src)(ctx)
        lengths zip ownerships flatMap { case (l, r) => Vector(l, r) }
    }
  }

  def length(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    baseType match {
      case a: in.ArrayT if a.addressability.isExclusive => vpr.SeqLength(base)(pos, info, errT)
      case a: in.ArrayT if a.addressability.isShared => ctx.array.length(base)(pos, info, errT)
    }
  }

  def lengthCondition(base : vpr.Exp, dims : IndexedSeq[in.ArrayT])(src : in.Node)(ctx : Context) : vpr.Exp = {
    require(0 < dims.length, s"no idea on how to handle a zero-dimensional array")

    val (pos, info, errT) = src.vprMeta

    dims.dropRight(1) match {
      // case 1. `array` has only one dimension, so no need to generate any quantifiers
      case Vector() =>
        val typ = dims.last
        vpr.EqCmp(
          length(base, typ)(src)(ctx),
          vpr.IntLit(typ.length)(pos, info, errT)
        )(pos, info, errT)

      // case 2. `array` has at least two dimensions, so quantifiers need to be generated
      case tail =>
        val decls = tail.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
        val declVars = decls.map(d => d.localVar)
        val tailVars = declVars.zip(tail)
        val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
        val accessExp = access(base, tail.head, declVars)(src)(ctx)
        val lengthExp = length(accessExp, dims.last)(src)(ctx)

        vpr.Forall(
          decls,
          Seq(vpr.Trigger(Seq(lengthExp))(pos, info, errT)),
          vpr.Implies(
            conditions,
            vpr.EqCmp(
              lengthExp,
              vpr.IntLit(dims.last.length)(pos, info, errT)
            )(pos, info, errT)
          )(pos, info, errT)
        )(pos, info, errT)
    }
  }

  def lengthConditions(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node)(ctx : Context) : Vector[vpr.Exp] =
    dimensions(baseType).map(lengthCondition(base, _)(src)(ctx))


  /* ** Utilities */

  private def dimensions(typ : in.Type) : Vector[Vector[in.ArrayT]] = typ match {
    case typ : in.ArrayT => Vector(typ) +: dimensions(typ.elems).map(typ +: _)
    case _ => Vector()
  }

  private def inhale(expr : vpr.Exp)(src : in.Node) : vpr.Stmt = {
    val (pos, info, errT) = src.vprMeta

    if (expr.isPure) {
      vpr.Assume(expr)(pos, info, errT)
    } else {
      vpr.Inhale(expr)(pos, info, errT)
    }
  }

  def ownershipCondition(base : vpr.Exp, dims : Vector[in.ArrayT])(src : in.Node)(ctx : Context) : vpr.Forall = {
    require(0 < dims.length, s"no idea on how to handle a zero-dimensional array")

    val (pos, info, errT) = src.vprMeta
    val decls = dims.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
    val declVars = decls.map(d => d.localVar)
    val tailVars = declVars.zip(dims)
    val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
    val accessExp = access(base, dims.head, declVars)(src)(ctx).asInstanceOf[vpr.FieldAccess]

    vpr.Forall(
      decls,
      Seq(vpr.Trigger(Seq(accessExp))(pos, info, errT)),
      vpr.Implies(
        conditions,
        vpr.FieldAccessPredicate(
          accessExp, vpr.FullPerm()(pos, info, errT)
        )(pos, info, errT)
      )(pos, info, errT)
    )(pos, info, errT)
  }

  def ownershipConditions(base : vpr.Exp, baseType : in.ArrayT)(src : in.Node)(ctx : Context) : Vector[vpr.Forall] =
    dimensions(baseType).map(ownershipCondition(base, _)(src)(ctx))
}
