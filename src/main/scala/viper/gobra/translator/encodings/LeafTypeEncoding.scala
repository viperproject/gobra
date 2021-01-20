// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.{Exclusive, Shared}
import viper.gobra.translator.interfaces.Context
import viper.gobra.translator.util.ViperWriter.CodeLevel.unit
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}

/**
  * Simplified type encoding interface for types T with a layout of:
  * (1) Layout(T°) = X
  * (2) Layout(T@) = ref |-> X
  * for some layout X
  *
  * Examples are bool, int, *T, and ghost types.
  */
trait LeafTypeEncoding extends TypeEncoding {

  import viper.gobra.translator.util.TypePatterns._

  /**
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
    * [loc: T@ = rhs] -> [loc] = [rhs]
    */
  override def assignment(ctx: Context): (in.Assignee, in.Expr, in.Node) ==> CodeWriter[vpr.Stmt] = default(super.assignment(ctx)){
    case (in.Assignee((loc: in.Location) :: t / Shared), rhs, src) if  typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = src.vprMeta
      for {
        rhs <- ctx.expr.translate(rhs)(ctx)
        lval <- ctx.expr.translate(loc)(ctx).map(_.asInstanceOf[vpr.FieldAccess])
      } yield vpr.FieldAssign(lval, rhs)(pos, info, errT)
  }

  /**
    * Encodes expressions as values that do not occupy some identifiable location in memory.
    *
    * To avoid conflicts with other encodings, a leaf encoding for type T should be defined at:
    * (1) exclusive operations on T, which includes literals and default values
    * Super implements exclusive variables and constants with [[variable]] and [[globalVar]], respectively.
    *
    * R[ dflt(T@) ] -> null
    * R[ loc: T@ ] -> Ref[loc].val
    */
  override def expr(ctx: Context): in.Expr ==> CodeWriter[vpr.Exp] = default(super.expr(ctx)){
    case (dflt: in.DfltVal) :: t / Shared if typ(ctx).isDefinedAt(t) =>
      unit(withSrc(vpr.NullLit(), dflt))

    case (loc: in.Location) :: t / Shared if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = loc.vprMeta
      for {
        vLoc <- ctx.typeEncoding.reference(ctx)(loc)
      } yield vpr.FieldAccess(vLoc, ctx.field.field(t.withAddressability(Exclusive))(ctx))(pos, info, errT)
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive r-value.
    * An encoding for type T should be defined at all shared locations of type T.
    *
    * Footprint[loc: T@, perm] -> acc([loc], [perm])
    */
  override def addressFootprint(ctx: Context): (in.Location, in.Expr) ==> CodeWriter[vpr.Exp] = {
    case (loc :: t / Shared, p) if typ(ctx).isDefinedAt(t) =>
      val (pos, info, errT) = loc.vprMeta
      for {
        vprPerm <- ctx.typeEncoding.expr(ctx)(p)
        l <- ctx.expr.translate(loc)(ctx).map(_.asInstanceOf[vpr.FieldAccess])
      } yield vpr.FieldAccessPredicate(l, vprPerm)(pos, info, errT)
  }
}
