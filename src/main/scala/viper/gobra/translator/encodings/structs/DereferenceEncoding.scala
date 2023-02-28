// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.structs

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.silver.{ast => vpr}


class DereferenceEncoding extends Encoding {

  import viper.gobra.translator.util.TypePatterns._
  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def function(ctx: Context): in.Member ==> MemberWriter[vpr.Function] = {
    case x: in.Dereference if x.ret.isInstanceOf[in.PointerT] => dereference(x)(ctx)
  }

  private def dereference(deref: in.Dereference)(ctx: Context): MemberWriter[vpr.Function] = {
    require(deref.results.size == 1)

    val name = deref.id.uniqueName

    val (pos, info, errT) = deref.vprMeta
    val vArgs = deref.args.map(ctx.variable)
    val vArgPres = deref.args.flatMap(ctx.varPrecondition)

    val vResults = deref.results.map(ctx.variable)
    assert(vResults.size == 1)
    val resultType = if (vResults.size == 1) vResults.head.typ else ctx.tuple.typ(vResults map (_.typ))

    
    for {
      pres <- sequence(vArgPres ++ deref.pres.map(ctx.precondition))

      body <- option(deref.body map { b =>
        b match {
          case u@in.Unfolding(acc, exp) => {
            val (pos, info, errT) = u.vprMeta
            pure( for {
              a <- ctx.assertion(acc)
              e <- exp match {
                case (loc: in.Location) :: ctx.CompleteStruct(fs) =>
                  for {
                    x <- cl.bind(loc)(ctx)
                    locFAs = fs.map(f => in.FieldRef(x, f)(loc.info))
                    args <- cl.sequence(locFAs.map(fa => ctx.expression(fa)))
                  } yield withSrc(ctx.tuple.create(args), u)
                case _ => cl.pure(ctx.expression(exp))(ctx)
              }
            } yield vpr.Unfolding(a.asInstanceOf[vpr.PredicateAccessPredicate], e)(pos, info, errT))(ctx)
          }
          case _ => pure( for {
            results <- ctx.expression(b)
          } yield results)(ctx)
        }
      })

      function = vpr.Function(
        name = name,
        formalArgs = vArgs,
        typ = resultType,
        pres = pres,
        posts = Vector.empty,
        body = body
      )(pos, info, errT)

    } yield function
  }
}