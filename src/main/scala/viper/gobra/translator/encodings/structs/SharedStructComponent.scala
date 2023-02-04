// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import viper.gobra.ast.{internal => in}
import viper.gobra.theory.Addressability.Shared
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.{ast => vpr}
import viper.gobra.translator.util.ViperWriter.CodeLevel._
import viper.gobra.translator.util.TypePatterns._
import viper.gobra.translator.util.{ViperUtil => VU}
import viper.gobra.util.Violation
import StructEncoding.{ComponentParameter, cptParam}
import viper.gobra.translator.library.Generator
import viper.gobra.translator.context.Context

trait SharedStructComponent extends Generator {

  /** Returns type of shared-struct domain. */
  def typ(t: ComponentParameter)(ctx: Context): vpr.Type

  /** Getter of shared-struct domain. */
  def get(base: vpr.Exp, idx: Int, t: ComponentParameter)(src: in.Node)(ctx: Context): vpr.Exp

  /**
    * Encodes the conversion from a shared value to an exclusive value.
    * All permissions involved in the conversion should be returned by [[addressFootprint]].
    *
    * The default implementation is:
    * Convert[loc: Struct{F}@] -> create_ex_struct( R[loc.f] | f in F )
    */
  def convertToExclusive(loc: in.Location)(ctx: Context, ex: ExclusiveStructComponent): CodeWriter[vpr.Exp] = {
    loc match {
      case _ :: ctx.CompleteStruct(fs) / Shared =>
        val vti = cptParam(fs)(ctx)
        pure(
          for {
            x <- bind(loc)(ctx)
            locFAs = fs.map(f => in.FieldRef(x, f)(loc.info))
            args <- sequence(locFAs.map(fa => ctx.expression(fa)))
          } yield ex.create(args, vti)(loc)(ctx)
        )(ctx)

      case _ :: ctx.PartialStruct(fs) / Shared =>
        val vti = cptParam(fs)(ctx)
        pure(
          for {
            x <- bind(loc)(ctx)
            locFAs = fs.map(f => in.FieldRef(x, f)(loc.info))
            args <- sequence(locFAs.map(fa => ctx.expression(fa)))
          } yield ex.create(args, vti)(loc)(ctx)
        )(ctx) 

      case _ :: t => Violation.violation(s"expected struct, but got $t")
    }
  }

  /**
    * Encodes the permissions for all addresses of a shared type,
    * i.e. all permissions involved in converting the shared location to an exclusive value ([[convertToExclusive]]).
    * An encoding for type T should be defined at all shared locations of type T.
    *
    * The default implementation is:
    * Footprint[loc: Struct{F}@] -> AND f in F: Footprint[loc.f]
    */
  def addressFootprint(loc: in.Location, perm: in.Expr)(ctx: Context): CodeWriter[vpr.Exp] = {
    loc match {
      case _ :: ctx.CompleteStruct(fs) / Shared =>
        val (pos, info, errT) = loc.vprMeta
        pure(
          for {
            x <- bind(loc)(ctx)
            locFAs = fs.map(f => in.FieldRef(x, f)(loc.info))
            parts <- sequence(locFAs.map(fa => ctx.footprint(fa, perm)))
          } yield VU.bigAnd(parts)(pos, info, errT)
        )(ctx)
        
      case _ :: ctx.PartialStruct(fs) / Shared =>
        val (pos, info, errT) = loc.vprMeta
        pure(
          for {
            x <- bind(loc)(ctx)
            locFAs = fs.map(f => in.FieldRef(x, f)(loc.info))
            parts <- sequence(locFAs.map(fa => ctx.footprint(fa, perm)))
          } yield VU.bigAnd(parts)(pos, info, errT)
        )(ctx)

      case _ :: t => Violation.violation(s"expected struct, but got $t")
    }
  }
}
