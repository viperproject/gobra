package viper.gobra.translator.util

import scala.collection.immutable.IndexedSeq
import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.translator.Names
import viper.gobra.translator.interfaces.Context
import viper.silver.{ast => vpr}

object ArrayUtil {

  def access(base : (vpr.Exp, in.ArrayType), index : vpr.Exp)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val (expr, typ) = base

    typ match {
      case in.ArrayT(_, t) => ctx.loc.arrayIndex(t, expr, index)(ctx)(pos, info, errT)
      case _ : in.ArraySequenceT => vpr.SeqIndex(expr, index)(pos, info, errT)
    }
  }

  def access(base : (vpr.Exp, in.ArrayType), indices : IndexedSeq[vpr.Exp])(src : in.Node)(ctx : Context) : vpr.Exp = {
    indices match {
      case Vector() => base._1
      case index +: tail => {
        val elem = access(base, index)(src)(ctx)
        base._2.typ match {
          case typ : in.ArrayType => access((elem, typ), tail)(src)(ctx)
          case _ => elem
        }
      }
    }
  }

  /** Gives a fresh local variable for an `typ`-typed array. */
  def anonymousLocalVar(typ : in.ArrayType)(info : Source.Parser.Info) =
    in.LocalVar.Inter(Names.freshName, typ)(info)

  def boundaryCondition(base : (vpr.Exp, in.ArrayType))(src : in.Node) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val (exp, typ) = base

    vpr.And(
      vpr.LeCmp(vpr.IntLit(0)(pos, info, errT), exp)(pos, info, errT),
      vpr.LtCmp(exp, vpr.IntLit(typ.length)(pos, info, errT))(pos, info, errT)
    )(pos, info, errT)
  }

  def boundaryCondition(base : IndexedSeq[(vpr.Exp, in.ArrayType)])(src : in.Node) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val init = vpr.TrueLit()(pos, info, errT)

    base.map(boundaryCondition(_)(src)).foldLeft[vpr.Exp](init) {
      case (l, r) => vpr.And(l, r)(pos, info, errT)
    }
  }

  def defaulValueAssumption(base : vpr.Exp, baseType : in.ArrayType)(src : in.Node)(ctx : Context) : vpr.Stmt =
    inhale(defaulValueCondition(base, baseType)(src)(ctx))(src)

  def defaulValueCondition(base : vpr.Exp, baseType : in.ArrayType)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val dims = dimensions(baseType).last
    val decls = dims.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
    val declVars = decls.map(d => d.localVar)
    val tailVars = declVars.zip(dims)
    val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
    val accessExp = access((base, dims.head), declVars)(src)(ctx)
    val dflt = ctx.loc.defaultValue(dims.last.typ)(src)(ctx).res

    vpr.Forall(
      decls,
      Seq(vpr.Trigger(Seq(accessExp))(pos, info, errT)),
      vpr.Implies(
        conditions,
        vpr.EqCmp(accessExp, dflt)(pos, info, errT)
      )(pos, info, errT)
    )(pos, info, errT)
  }

  def equalsAssumption(left : (vpr.Exp, in.ArrayType), right : (vpr.Exp, in.ArrayType))(src : in.Node)(ctx : Context) : vpr.Stmt =
    inhale(equalsCondition(left, right)(src)(ctx))(src)

  def equalsCondition(left : (vpr.Exp, in.ArrayType), right : (vpr.Exp, in.ArrayType))(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    (left, right) match {
      case ((leftExp, _: in.ArraySequenceT), (rightExp, _: in.ArraySequenceT)) =>
        vpr.EqCmp(leftExp, rightExp)(pos, info, errT)

      case ((_, leftTyp), (_, rightTyp)) => {
        val leftDims = dimensions(leftTyp).last
        val rightDims = dimensions(rightTyp).last

        require(leftDims.length == rightDims.length, s"cannot create an equality condition for two arrays of different dimensions")

        val decls = leftDims.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
        val declVars = decls.map(d => d.localVar)
        val tailVars = declVars.zip(leftDims)
        val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
        val accessLeft = access(left, declVars)(src)(ctx)
        val accessRight = access(right, declVars)(src)(ctx)

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
  }

  def footprintAssumptions(base : vpr.Exp, baseType : in.ArrayType)(src : in.Node)(ctx : Context) : Vector[vpr.Stmt] =
    footprintConditions(base, baseType)(src)(ctx).map(inhale(_)(src))

  def footprintCondition(array : vpr.Exp, arrayType : in.ArrayType)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta
    val base = vpr.TrueLit()(pos, info, errT)

    footprintConditions(array, arrayType)(src)(ctx).foldLeft[vpr.Exp](base) {
      case (a, b) => vpr.And(a, b)(pos, info, errT)
    }
  }

  def footprintConditions(base : vpr.Exp, baseType : in.ArrayType)(src : in.Node)(ctx : Context) : Vector[vpr.Exp] = {
    val lengths = lengthConditions(base, baseType)(src)(ctx)

    baseType match {
      case _: in.ArraySequenceT => lengths
      case baseType: in.ArrayT => {
        val ownerships = ownershipConditions(base, baseType)(src)(ctx)
        lengths zip ownerships flatMap { case (l, r) => Vector(l, r) }
      }
    }
  }

  def length(base : vpr.Exp, baseType : in.ArrayType)(src : in.Node)(ctx : Context) : vpr.Exp = {
    val (pos, info, errT) = src.vprMeta

    baseType match {
      case _ : in.ArrayT => ctx.array.length(base)(pos, info, errT)
      case _ : in.ArraySequenceT => vpr.SeqLength(base)(pos, info, errT)
    }
  }

  def lengthCondition(base : vpr.Exp, dims : IndexedSeq[in.ArrayType])(src : in.Node)(ctx : Context) : vpr.Exp = {
    require(0 < dims.length, s"no idea on how to handle a zero-dimensional array")

    val (pos, info, errT) = src.vprMeta

    dims.dropRight(1) match {
      // case 1. `array` has only one dimension, so no need to generate any quantifiers
      case Vector() => {
        val typ = dims.last
        vpr.EqCmp(
          length(base, typ)(src)(ctx),
          vpr.IntLit(typ.length)(pos, info, errT)
        )(pos, info, errT)
      }

      // case 2. `array` has at least two dimensions, so quantifiers need to be generated
      case tail => {
        val decls = tail.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
        val declVars = decls.map(d => d.localVar)
        val tailVars = declVars.zip(tail)
        val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
        val accessExp = access((base, tail.head), declVars)(src)(ctx)
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
  }

  def lengthConditions(base : vpr.Exp, baseType : in.ArrayType)(src : in.Node)(ctx : Context) : Vector[vpr.Exp] =
    dimensions(baseType).map(lengthCondition(base, _)(src)(ctx))

  def ownershipCondition(base : vpr.Exp, dims : Vector[in.ArrayType])(src : in.Node)(ctx : Context) : vpr.Forall = {
    require(0 < dims.length, s"no idea on how to handle a zero-dimensional array")

    val (pos, info, errT) = src.vprMeta
    val decls = dims.indices.map(i => vpr.LocalVarDecl(s"i$i", vpr.Int)(pos, info, errT))
    val declVars = decls.map(d => d.localVar)
    val tailVars = declVars.zip(dims)
    val conditions : vpr.Exp = boundaryCondition(tailVars)(src)
    val accessExp = access((base, dims.head), declVars)(src)(ctx).asInstanceOf[vpr.FieldAccess]

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

  def ownershipConditions(base : vpr.Exp, baseType : in.ArrayType)(src : in.Node)(ctx : Context) : Vector[vpr.Forall] =
    dimensions(baseType).map(ownershipCondition(base, _)(src)(ctx))

  private def dimensions(typ : in.Type) : Vector[Vector[in.ArrayType]] = typ match {
    case typ : in.ArrayType => Vector(typ) +: dimensions(typ.typ).map(typ +: _)
    case _ => Vector()
  }

  private def inhale(expr : vpr.Exp)(src : in.Node) : vpr.Stmt = {
    val (pos, info, errT) = src.vprMeta

    expr.isPure match {
      case true => vpr.Assume(expr)(pos, info, errT)
      case false => vpr.Inhale(expr)(pos, info, errT)
    }
  }
}
