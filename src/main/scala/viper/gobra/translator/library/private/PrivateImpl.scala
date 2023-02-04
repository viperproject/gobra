// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.library.privates

import viper.gobra.ast.{internal => in}
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.{ViperUtil => vu}
import viper.gobra.reporting.{PrivateEntailmentError, AssertionFalseError, Source}
import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.silver.{ast => vpr}
import viper.silver.verifier.{errors => err, reasons}

class PrivateImpl extends Private {

  import viper.gobra.translator.util.ViperWriter.{CodeLevel => cl, _}
  import MemberLevel._

  override def finalize (addMemberFn: vpr.Member => Unit): Unit = {
    proofMembers.foreach(addMemberFn)
  } 

  private var proofMembers: List[vpr.Method] = List.empty

  /**
    * Takes the method 'x' and creates a method 'proof' with name 
    * '${x.name.name}_public' that is put into 'proofMembers'.
    * 
    * func rec F(A) ret 
    *   requires P
    *   ensures Q
    *   private {
    *     requires P'
    *     ensures Q'
    *     proof F {
    *       proof_body
    *     }
    *   }
    * {
    *   body
    * }
    * 
    * is transformed to the proof method
    * 
    * method rec F_public(A) ret
    *   requires [P]
    *   ensures [Q]
    * {
    *   [proof_body]
    * }
    * 
    * The proof is generated only if there is a private proof inside the private spec.
    */
  override def privateProofMethod(x: in.Method)(ctx: Context): Unit = {
    val (pos, info, errT) = x.vprMeta

    val vRecv = ctx.variable(x.receiver)
    val vRecvPres = ctx.varPrecondition(x.receiver).toVector

    val vArgs = x.args.map(ctx.variable)
    val vArgPres = x.args.flatMap(ctx.varPrecondition)

    val vResults = x.results.map(ctx.variable)
    val vResultPosts = x.results.flatMap(ctx.varPostcondition)
    val vResultInit = cl.seqns(x.results map ctx.initialization)

    val spec = x.privateSpec.getOrElse(null) //only called if not empty

    (for {
      pres <- sequence((vRecvPres ++ vArgPres) ++ x.pres.map(ctx.precondition))
      posts <- sequence(vResultPosts ++ x.posts.map(ctx.postcondition))
      measures <- sequence(x.terminationMeasures.map(e => pure(ctx.assertion(e))(ctx)))

      proof_body <- option(spec.proof.map{ b => block{
        for {
          init <- vResultInit
          core <- ctx.statement(b)
        } yield vu.seqn(Vector(init, core))(pos, info, errT)
      }})

      proof = if (proof_body.isEmpty) { None } 
      else {
        Some(vpr.Method(
          name = x.name.name + "_public",
          formalArgs = vRecv +: vArgs,
          formalReturns = vResults,
          pres = pres ++ measures,
          posts = posts,
          body = proof_body,
        )(pos, info, errT))
      } 

    } yield proof).map{
      case Some(p) => proofMembers ::= p
      case None => 
    }

  } 

  /**
    * Takes the function 'x' and creates a method 'proof' with name 
    * '${x.name.name}_public' that is put into 'proofMembers'.
    * 
    * func F(A) ret 
    *   requires P
    *   ensures Q
    *   private {
    *     requires P'
    *     ensures Q'
    *     proof F {
    *       proof_body
    *     }
    *   }
    * {
    *   body
    * }
    * 
    * is transformed to the proof method
    * 
    * method F_public(A) ret
    *   requires [P]
    *   ensures [Q]
    * {
    *   [proof_body]
    * }
    * 
    * The proof is generated only if there is a private proof inside the private spec.
    */
  override def privateProofFunction(x: in.Function)(ctx: Context): Unit = {
    val (pos, info, errT) = x.vprMeta

    val vArgs = x.args.map(ctx.variable)
    val vArgPres = x.args.flatMap(ctx.varPrecondition)

    val vResults = x.results.map(ctx.variable)
    val vResultPosts = x.results.flatMap(ctx.varPostcondition)
    val vResultInit = cl.seqns(x.results map ctx.initialization)

    val spec = x.privateSpec.getOrElse(null) //only called if not empty

    //proof if private specification entails public specification
    (for {
      pres <- sequence(vArgPres ++ x.pres.map(ctx.precondition))
      posts <- sequence(vResultPosts ++ x.posts.map(ctx.postcondition))
      measures <- sequence(x.terminationMeasures.map(e => pure(ctx.assertion(e))(ctx)))

      proof_body <- option(spec.proof.map{ b => block{
        for {
          init <- vResultInit
          core <- ctx.statement(b)
        } yield vu.seqn(Vector(init, core))(pos, info, errT)
      }})

      proof = if (proof_body.isEmpty) { None } 
      else {
        Some(vpr.Method(
          name = x.name.name + "_public",
          formalArgs = vArgs,
          formalReturns = vResults,
          pres = pres ++ measures,
          posts = posts,
          body = proof_body,
        )(pos, info, errT))
      } 

    } yield proof).map {
      case Some(p) => proofMembers ::= p
      case None =>
    }
  }


  override def privateProofError(funcId: String): ErrorTransformer = {
    case err.PreconditionInCallFalse(Source(info), reason, _) => reason match {
      case reason: reasons.AssertionFalse => PrivateEntailmentError(info, funcId, "Precondition")
            .dueTo(AssertionFalseError(reason.offendingNode.info.asInstanceOf[Source.Verifier.Info]))
    }
    case err.PostconditionViolated(Source(info), _, reason, _) => reason match {
      case reason: reasons.AssertionFalse => PrivateEntailmentError(info, funcId, "Postcondition")
            .dueTo(AssertionFalseError(reason.offendingNode.info.asInstanceOf[Source.Verifier.Info]))
    } 
  }

}
