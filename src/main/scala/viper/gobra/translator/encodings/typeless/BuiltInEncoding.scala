// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.translator.encodings.typeless

import org.bitbucket.inkytonik.kiama.==>
import viper.gobra.ast.{internal => in}
import viper.gobra.frontend.info.base.BuiltInMemberTag
import viper.gobra.frontend.info.base.BuiltInMemberTag._
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.encodings.combinators.Encoding
import viper.gobra.translator.context.Context
import viper.gobra.translator.util.ViperWriter.MemberWriter
import viper.gobra.translator.util.PrimitiveGenerator
import viper.gobra.util.Computation
import viper.gobra.util.Violation.violation
import viper.silver.{ast => vpr}

import scala.annotation.unused
import scala.language.postfixOps

/**
  * Encodes built-in members by translating them to 'regular' members and calling the corresponding encoding
  */
class BuiltInEncoding extends Encoding {

  // the implementation uses 4 distinct generators (instead of a single one) such that the exposed
  // methods (i.e. method, function, fpredicate, and mpredicate) can return the translated 'regular' member.

  override def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    methodGenerator.finalize(addMemberFn)
    functionGenerator.finalize(addMemberFn)
    fPredicateGenerator.finalize(addMemberFn)
    mPredicateGenerator.finalize(addMemberFn)
  }

  import viper.gobra.translator.util.ViperWriter.{MemberLevel => mw}

  override def member(ctx: Context): in.Member ==> MemberWriter[Vector[vpr.Member]] = {
    case x: in.BuiltInMember => builtInMember(x)(ctx); mw.unit(Vector.empty)
  }

  private def builtInMember(x: in.BuiltInMember)(ctx: Context): in.Member =
    x match {
      case m: in.BuiltInMethod => methodGenerator(m, ctx)
      case f: in.BuiltInFunction => functionGenerator(f, ctx)
      case p: in.BuiltInFPredicate => fPredicateGenerator(p, ctx)
      case p: in.BuiltInMPredicate => mPredicateGenerator(p, ctx)
    }

  override def builtInMethod(ctx: Context): in.BuiltInMethod ==> in.MethodMember = {
    case x => methodGenerator(x, ctx)
  }

  override def builtInFunction(ctx: Context): in.BuiltInFunction ==> in.FunctionMember = {
    case x => functionGenerator(x, ctx)
  }

  override def builtInFPredicate(ctx: Context): in.BuiltInFPredicate ==> in.FPredicate = {
    case x => fPredicateGenerator(x, ctx)
  }

  override def builtInMPredicate(ctx: Context): in.BuiltInMPredicate ==> in.MPredicate = {
    case x => mPredicateGenerator(x, ctx)
  }

  private val methodGenerator: PrimitiveGenerator.PrimitiveGenerator[(in.BuiltInMethod, Context), in.MethodMember] = PrimitiveGenerator.simpleGenerator {
    case (bm: in.BuiltInMethod, ctx: Context) =>
      val meth = translateMethod(bm)(ctx)
      val m = meth match {
        case meth: in.Method => ctx.method(meth).res
        case meth: in.PureMethod => ctx.function(meth).res
      }
      (meth, Vector(m))
  }

  private val functionGenerator: PrimitiveGenerator.PrimitiveGenerator[(in.BuiltInFunction, Context), in.FunctionMember] = PrimitiveGenerator.simpleGenerator {
    case (bf: in.BuiltInFunction, ctx: Context) =>
      val func = translateFunction(bf)(ctx)
      val f = func match {
        case func: in.Function => ctx.method(func).res
        case func: in.PureFunction => ctx.function(func).res
      }
      (func, Vector(f))
  }

  private val fPredicateGenerator: PrimitiveGenerator.PrimitiveGenerator[(in.BuiltInFPredicate, Context), in.FPredicate] = PrimitiveGenerator.simpleGenerator {
    case (bp: in.BuiltInFPredicate, ctx: Context) =>
      val pred = translateFPredicate(bp)(ctx)
      val p = ctx.predicate(pred).res
      (pred, Vector(p))
  }

  private val mPredicateGenerator: PrimitiveGenerator.PrimitiveGenerator[(in.BuiltInMPredicate, Context), in.MPredicate] = PrimitiveGenerator.simpleGenerator {
    case (bp: in.BuiltInMPredicate, ctx: Context) =>
      val pred = translateMPredicate(bp)(ctx)
      val p = ctx.predicate(pred).res
      (pred, Vector(p))
  }

  /**
    * Returns an already existing proxy for a tag or otherwise creates a new proxy and the corresponding member
    */
  private def getOrGenerateMethod(tag: BuiltInMethodTag, recv: in.Type, args: Vector[in.Type])(src: Source.Parser.Info)(ctx: Context): in.MethodProxy = {
    def create(): in.BuiltInMethod = {
      val proxy = in.MethodProxy(tag.identifier, freshNameForTag(tag)(ctx))(src)
      in.BuiltInMethod(recv, tag, proxy, args)(src)
    }
    val method = getOrGenerate(tag, Vector(recv), create)(ctx)
    method.name
  }

  /**
    * Returns an already existing proxy for a tag or otherwise creates a new proxy and the corresponding member
    */
  @unused
  private def getOrGenerateFunction(tag: BuiltInFunctionTag, args: Vector[in.Type])(src: Source.Parser.Info)(ctx: Context): in.FunctionProxy = {
    def create(): in.BuiltInFunction = {
      val proxy = in.FunctionProxy(freshNameForTag(tag)(ctx))(src)
      in.BuiltInFunction(tag, proxy, args)(src)
    }
    val function = getOrGenerate(tag, args, create)(ctx)
    function.name
  }

  /**
    * Returns an already existing proxy for a tag or otherwise creates a new proxy and the corresponding member
    */
  private def getOrGenerateFPredicate(tag: BuiltInFPredicateTag, args: Vector[in.Type])(src: Source.Parser.Info)(ctx: Context): in.FPredicateProxy = {
    def create(): in.BuiltInFPredicate = {
      val proxy = in.FPredicateProxy(freshNameForTag(tag)(ctx))(src)
      in.BuiltInFPredicate(tag, proxy, args)(src)
    }
    val predicate = getOrGenerate(tag, args, create)(ctx)
    predicate.name
  }

  /**
    * Returns an already existing proxy for a tag or otherwise creates a new proxy and the corresponding member
    */

  private def getOrGenerateMPredicate(tag: BuiltInMPredicateTag, recv: in.Type, args: Vector[in.Type])(src: Source.Parser.Info)(ctx: Context): in.MPredicateProxy = {
    def create(): in.BuiltInMPredicate = {
      val proxy = in.MPredicateProxy(tag.identifier, freshNameForTag(tag)(ctx))(src)
      in.BuiltInMPredicate(recv, tag, proxy, args)(src)
    }
    val predicate = getOrGenerate(tag, Vector(recv), create)(ctx)
    predicate.name
  }

  /**
    * stores all members that are not part of the lookup table but have been created because of a dependency of other built-in members
    */
  private var additionalMembers: Map[(BuiltInMemberTag, Vector[in.Type]), in.BuiltInMember] = Map.empty

  /**
    * Generic method to retrieve a built-in member or generate a new one in case it does not exist yet
    * @param createMember function that creates a new member (without encoding it)
    */
  private def getOrGenerate[T <: BuiltInMemberTag, M <: in.BuiltInMember](tag: T, args: Vector[in.Type], createMember: () => M)(ctx: Context): M = {
    def generate: M = {
      val m = createMember()
      additionalMembers = additionalMembers + ((tag, args) -> m)
      // encode member:
      builtInMember(m)(ctx)
      m
    }

    val members: Map[(T, Vector[in.Type]), M] = (lookupTableMembers(ctx) ++ additionalMembers) collect {
      case ((t: T@unchecked, as), m: M@unchecked) => (t, as) -> m
    }
    members.getOrElse((tag, args), generate)
  }

  /**
    * Returns all built-in members stored in the lookup table. The resulting map is cached for faster repeated
    * accesses since the lookup table cannot be modified.
    */
  private def lookupTableMembers(ctx: Context): Map[(BuiltInMemberTag, Vector[in.Type]), in.BuiltInMember] =
    Computation.cachedComputation[Context, Map[(BuiltInMemberTag, Vector[in.Type]), in.BuiltInMember]](ctx => {
      (ctx.table.getMethods ++ ctx.table.getFunctions ++ ctx.table.getFPredicates ++ ctx.table.getMPredicates) collect {
        case p: in.BuiltInMethod => (p.tag, Vector(p.receiverT)) -> p
        case f: in.BuiltInFunction => (f.tag, f.argsT) -> f
        case f: in.BuiltInFPredicate => (f.tag, f.argsT) -> f
        case p: in.BuiltInMPredicate => (p.tag, Vector(p.receiverT)) -> p
      } toMap
    })(ctx)


  private def freshNameForTag(tag: BuiltInMemberTag)(ctx: Context): String =
    s"${Names.builtInMember}_${tag.identifier}_${ctx.freshNames.next()}"


  //
  // Translation functions that translate a built-in member to a 'regular' member.
  // This 'regular' member is then encoded as if it is a user-provided member.
  //

  private def translateMethod(x: in.BuiltInMethod)(ctx: Context): in.MethodMember = {
    val src = x.info
    (x.tag, x.receiverT) match {
      case (BufferSizeMethodTag, recv: in.ChannelT) =>
        /**
          * requires acc(c.IsChannel(), _)
          * pure func (c chan T).BufferSize() (k Int)
          */
        assert(recv.addressability == Addressability.inParameter)
        val recvParam = in.Parameter.In("c", recv)(src)
        val kParam = in.Parameter.Out("k", in.IntT(Addressability.outParameter))(src)
        val isChannelInst = builtInMPredAccessible(BuiltInMemberTag.IsChannelMPredTag, recvParam, Vector())(src)(ctx)
        val pres: Vector[in.Assertion] = Vector(
          in.Access(isChannelInst, in.WildcardPerm(src))(src),
        )
        in.PureMethod(recvParam, x.name, Vector.empty, Vector(kParam), pres, Vector.empty, Vector.empty, Vector.empty, None, false)(src)

      case (tag: ChannelInvariantMethodTag, recv: in.ChannelT) =>
        /**
          * requires acc(c.[chanPredicateTag](), _)
          * pure func (c chan T).[tag.identifier] (res [returnType])
          *
          * where
          *   - chanPredicateTag is either SendChannel or RecvChannel
          *   - returnType is either pred(T) or pred()
          */
        assert(recv.addressability == Addressability.inParameter)
        val recvParam = in.Parameter.In("c", recv)(src)
        val resType = tag match {
          case SendGotPermMethodTag | RecvGivenPermMethodTag => in.PredT(Vector(), Addressability.outParameter) // pred()
          case _ => in.PredT(Vector(recv.elem), Addressability.outParameter) // pred(T)
        }
        val resParam = in.Parameter.Out("res", resType.withAddressability(Addressability.outParameter))(src)
        val chanPredicateTag = tag match {
          case _: SendPermMethodTag => BuiltInMemberTag.SendChannelMPredTag
          case _: RecvPermMethodTag => BuiltInMemberTag.RecvChannelMPredTag
        }
        val chanPredicate = builtInMPredAccessible(chanPredicateTag, recvParam, Vector())(src)(ctx)
        val pres: Vector[in.Assertion] = Vector(
          in.Access(chanPredicate, in.WildcardPerm(src))(src)
        )
        in.PureMethod(recvParam, x.name, Vector.empty, Vector(resParam), pres, Vector.empty, Vector.empty, Vector.empty, None, false)(src)

      case (InitChannelMethodTag, recv: in.ChannelT) =>
        /**
          * requires c.IsChannel()
          * requires c.BufferSize() > 0 ==> (B == pred_true{})
          * ensures c.SendChannel() && c.RecvChannel()
          * ensures c.SendGivenPerm() == A && c.SendGotPerm() == B
          * ensures c.RecvGivenPerm() == B && c.RecvGotPerm() == A
          * decreases _
          * ghost func (c chan T).Init(A pred(T), B pred())
          *
          * note that B is only of type pred() instead of pred(T) as long as we cannot deal with view shifts in Gobra.
          * as soon as we can, the third precondition can be changed to `[v] ((A(v) && C()) ==> (B(v) && D(v)))`
          */
        assert(recv.addressability == Addressability.inParameter)
        val recvParam = in.Parameter.In("c", recv)(src)
        val predTType = in.PredT(Vector(recv.elem), Addressability.inParameter) // pred(T)
        val predType = in.PredT(Vector(), Addressability.inParameter) // pred()
        val aParam = in.Parameter.In("A", predTType)(src)
        val bParam = in.Parameter.In("B", predType)(src)
        val isChannelInst = builtInMPredAccessible(BuiltInMemberTag.IsChannelMPredTag, recvParam, Vector())(src)(ctx)
        val bufferSizeType = in.IntT(Addressability.inParameter)
        val bufferSizeCall = builtInPureMethodCall(BuiltInMemberTag.BufferSizeMethodTag, recvParam, Vector(), bufferSizeType)(src)(ctx)
        val predTrueProxy = getOrGenerateFPredicate(BuiltInMemberTag.PredTrueFPredTag, Vector())(src)(ctx)
        val predTrueConstr = in.PredicateConstructor(predTrueProxy, predType, Vector())(src) // pred_true{}
        val bufferedImpl = in.Implication(
          in.GreaterCmp(bufferSizeCall, in.IntLit(0)(src))(src),
          in.ExprAssertion(in.EqCmp(bParam, predTrueConstr)(src))(src)
        )(src)
        val pres: Vector[in.Assertion] = Vector(
          in.Access(isChannelInst, in.FullPerm(src))(src),
          bufferedImpl
        )
        val sendChannelInst = builtInMPredAccessible(BuiltInMemberTag.SendChannelMPredTag, recvParam, Vector())(src)(ctx)
        val recvChannelInst = builtInMPredAccessible(BuiltInMemberTag.RecvChannelMPredTag, recvParam, Vector())(src)(ctx)
        val sendAndRecvChannel = in.SepAnd(
          in.Access(sendChannelInst, in.FullPerm(src))(src),
          in.Access(recvChannelInst, in.FullPerm(src))(src)
        )(src)
        val sendGivenPermCall = builtInPureMethodCall(BuiltInMemberTag.SendGivenPermMethodTag, recvParam, Vector(), predTType)(src)(ctx)
        val sendGivenPermEq = in.EqCmp(sendGivenPermCall, aParam)(src)
        val sendGotPermCall = builtInPureMethodCall(BuiltInMemberTag.SendGotPermMethodTag, recvParam, Vector(), predType)(src)(ctx)
        val sendGotPermEq = in.EqCmp(sendGotPermCall, bParam)(src)
        val sendChannelInvEq = in.And(sendGivenPermEq, sendGotPermEq)(src)
        val recvGivenPermCall = builtInPureMethodCall(BuiltInMemberTag.RecvGivenPermMethodTag, recvParam, Vector(), predType)(src)(ctx)
        val recvGivenPermEq = in.EqCmp(recvGivenPermCall, bParam)(src)
        val recvGotPermCall = builtInPureMethodCall(BuiltInMemberTag.RecvGotPermMethodTag, recvParam, Vector(), predTType)(src)(ctx)
        val recvGotPermEq = in.EqCmp(recvGotPermCall, aParam)(src)
        val recvChannelInvEq = in.And(recvGivenPermEq, recvGotPermEq)(src)
        val posts: Vector[in.Assertion] = Vector(
          sendAndRecvChannel,
          in.ExprAssertion(sendChannelInvEq)(src),
          in.ExprAssertion(recvChannelInvEq)(src),
        )
        in.Method(recvParam, x.name, Vector(aParam, bParam), Vector.empty, pres, posts, Vector(in.NonItfMethodWildcardMeasure(None)(src)), Vector.empty, None)(src)

      case (CreateDebtChannelMethodTag, recv: in.ChannelT) =>
        /**
          * requires dividend >= 0
          * requires divisor > 0
          * requires acc(c.SendChannel(), dividend/divisor /* p */)
          * ensures c.ClosureDebt(P, dividend, divisor /* p */) && c.Token(P)
          * ghost func (c chan T) CreateDebt(dividend int, divisor int /* p perm */, P pred())
          */
        assert(recv.addressability == Addressability.inParameter)
        val recvParam = in.Parameter.In("c", recv)(src)
        val dividendParam = in.Parameter.In("dividend", in.IntT(Addressability.inParameter))(src)
        val divisorParam = in.Parameter.In("divisor", in.IntT(Addressability.inParameter))(src)
        // val permissionAmountParam = in.Parameter.In("p", in.PermissionT(Addressability.inParameter))(src)
        val predicateParam = in.Parameter.In("P", in.PredT(Vector(), Addressability.inParameter))(src)
        val sendChannelInst = builtInMPredAccessible(BuiltInMemberTag.SendChannelMPredTag, recvParam, Vector())(src)(ctx)
        val pres: Vector[in.Assertion] = Vector(
          in.ExprAssertion(in.AtLeastCmp(dividendParam, in.IntLit(0)(src))(src))(src),
          in.ExprAssertion(in.GreaterCmp(divisorParam, in.IntLit(0)(src))(src))(src),
          in.Access(sendChannelInst, in.FractionalPerm(dividendParam, divisorParam)(src))(src)
          // in.Access(sendChannelInst, permissionAmountParam)(src)
        )
        val closureDebtArgs = Vector(predicateParam, dividendParam, divisorParam /* permissionAmountParam */)
        val closureDebtInst = builtInMPredAccessible(BuiltInMemberTag.ClosureDebtMPredTag, recvParam, closureDebtArgs)(src)(ctx)
        val tokenInst = builtInMPredAccessible(BuiltInMemberTag.TokenMPredTag, recvParam, Vector(predicateParam))(src)(ctx)
        val posts: Vector[in.Assertion] = Vector(
          in.Access(closureDebtInst, in.FullPerm(src))(src),
          in.Access(tokenInst, in.FullPerm(src))(src),
        )
        in.Method(recvParam, x.name, Vector(dividendParam, divisorParam /* permissionAmountParam */, predicateParam), Vector.empty, pres, posts, Vector.empty, Vector.empty, None)(src)

      case (RedeemChannelMethodTag, recv: in.ChannelT) =>
        /**
          * requires c.Token(P) && acc(c.Closed(), _)
          * ensures c.Closed() && P()
          * ghost func (c chan T) Redeem(P pred())
          *
          * note that c.Closed() is duplicable and thus full permission can be ensured
          */
        assert(recv.addressability == Addressability.inParameter)
        val recvParam = in.Parameter.In("c", recv)(src)
        val predicateParam = in.Parameter.In("P", in.PredT(Vector(), Addressability.inParameter))(src)
        val tokenInst = builtInMPredAccessible(BuiltInMemberTag.TokenMPredTag, recvParam, Vector(predicateParam))(src)(ctx)
        val closedInst = builtInMPredAccessible(BuiltInMemberTag.ClosedMPredTag, recvParam, Vector())(src)(ctx)
        val pres: Vector[in.Assertion] = Vector(
          in.Access(tokenInst, in.FullPerm(src))(src),
          in.Access(closedInst, in.WildcardPerm(src))(src)
        )
        val posts: Vector[in.Assertion] = Vector(
          in.Access(closedInst, in.FullPerm(src))(src),
          in.Access(in.Accessible.PredExpr(in.PredExprInstance(predicateParam, Vector())(src)), in.FullPerm(src))(src)
        )
        in.Method(recvParam, x.name, Vector(predicateParam), Vector.empty, pres, posts, Vector.empty, Vector.empty, None)(src)

      case (tag, recv) => violation(s"no method generation defined for tag $tag and receiver $recv")
    }
  }

  private def translateFunction(x: in.BuiltInFunction)(ctx: Context): in.FunctionMember = {
    val src = x.info

    var varCount = 0
    def freshBoundVar(): in.BoundVar = {
      varCount += 1
      in.BoundVar(s"i$varCount", in.IntT(Addressability.boundVariable))(src)
    }

    def inRange(exp: in.Expr, lower: in.Expr, upper: in.Expr): in.Expr = {
      in.And(
        in.AtLeastCmp(exp, lower)(src),
        in.LessCmp(exp, upper)(src)
      )(src)
    }

    def quantify(trigger: in.BoundVar => Vector[in.Trigger], range: in.BoundVar => in.Expr, body: in.BoundVar => in.Assertion): in.Assertion = {
      val i = freshBoundVar()
      in.SepForall(Vector(i), trigger(i), in.Implication(range(i), body(i))(src))(src)
    }

    def accessSlice(sliceExpr: in.Expr, perm: in.Expr): in.Assertion =
      quantify(
        trigger = { i => Vector(in.Trigger(Vector(in.Ref(in.IndexedExp(sliceExpr, i, sliceExpr.typ)(src))(src)))(src)) },
        range = { i => inRange(i, in.IntLit(0)(src), in.Length(sliceExpr)(src)) },
        body = { i => in.Access(in.Accessible.Address(in.IndexedExp(sliceExpr, i, sliceExpr.typ)(src)), perm)(src) }
      )

    (x.tag, x.argsT) match {
      case (CloseFunctionTag, Vector(channelT, dividendT, divisorT /* permissionAmountT */, predicateT)) =>
        /**
          * requires dividend >= 0
          * requires divisor > 0
          * requires acc(c.SendChannel(), dividend/divisor /* p */) && c.ClosureDebt(P, divisor - dividend, divisor /* 1-p */) && P()
          * ensures c.Closed()
          * func close(c chan T, ghost dividend int, divisor int /* p perm */, P pred())
          */
        assert(channelT.addressability == Addressability.inParameter)
        val channelParam = in.Parameter.In("c", channelT)(src)
        assert(dividendT.addressability == Addressability.inParameter)
        val dividendParam = in.Parameter.In("dividend", dividendT)(src)
        assert(divisorT.addressability == Addressability.inParameter)
        val divisorParam = in.Parameter.In("divisor", divisorT)(src)
        // assert(permissionAmountT.addressability == Addressability.inParameter)
        // val permissionAmountParam = in.Parameter.In("p", permissionAmountT)(src)
        val predicateParam = in.Parameter.In("P", predicateT)(src)
        val args = Vector(channelParam, dividendParam, divisorParam /* permissionAmountParam */, predicateParam)
        val sendChannelInst = builtInMPredAccessible(BuiltInMemberTag.SendChannelMPredTag, channelParam, Vector())(src)(ctx)
        val closureDebtArgs = Vector(
          predicateParam,
          in.Sub(divisorParam, dividendParam)(src),
          divisorParam
          // in.Sub(in.IntLit(1)(src), permissionAmountParam)(src)
        )
        val closureDebtInst = builtInMPredAccessible(BuiltInMemberTag.ClosureDebtMPredTag, channelParam, closureDebtArgs)(src)(ctx)
        val pres: Vector[in.Assertion] = Vector(
          in.ExprAssertion(in.AtLeastCmp(dividendParam, in.IntLit(0)(src))(src))(src),
          in.ExprAssertion(in.GreaterCmp(divisorParam, in.IntLit(0)(src))(src))(src),
          in.Access(sendChannelInst, in.FractionalPerm(dividendParam, divisorParam)(src))(src),
          // in.Access(sendChannelInst, permissionAmountParam)(src),
          in.Access(closureDebtInst, in.FullPerm(src))(src),
          in.Access(in.Accessible.PredExpr(in.PredExprInstance(predicateParam, Vector())(src)), in.FullPerm(src))(src)
        )
        val closedInst = builtInMPredAccessible(BuiltInMemberTag.ClosedMPredTag, channelParam, Vector())(src)(ctx)
        val posts: Vector[in.Assertion] = Vector(
          in.Access(closedInst, in.FullPerm(src))(src)
        )

        in.Function(x.name, args, Vector.empty, pres, posts, Vector.empty, Vector.empty, None)(src)

      /* João, 18/08/2021:
       *  The spec for `append` currently does not allow the first and second non-ghost arguments to be the same. The go
       *  spec however allows that to happen and the behavior is well-defined for those cases (in particular, the result
       *  of appending two slices is independent of whether they overlap). I did not change the spec to reflect this change
       *  at the moment to avoid surprises in the performance of Gobra when checking the VerifiedSCION codebase. I do expect
       *  to change that at a later point.
       */
      case (AppendFunctionTag, Vector(_: in.PermissionT, dst, _)) =>
        /**
          * requires p > 0
          * requires forall i int :: { &dst[i] } 0 <= i && i < len(dst) ==> acc(&dst[i])
          * requires forall i int :: { &src[i] } 0 <= i && i < len(src) ==> acc(&src[i], p)
          * ensures len(res) == len(dst) + len(src)
          * ensures forall i int :: { &res[i] } 0 <= i && i < len(res) ==> acc(&res[i])
          * ensures forall i int :: { &src[i] } 0 <= i && i < len(src) ==> acc(&src[i], p)
          * ensures forall i int :: { &res[i] } 0 <= i && i < len(dst) ==> res[i] === old(dst[i])
          * ensures forall i int :: { &res[i] } len(dst) <= i && i < len(res) ==> res[i] === src[i - len(dst)]
          */
        val elemType = ctx.underlyingType(dst) match {
          case t: in.SliceT => t.elems.withAddressability(Addressability.sliceLookup)
          case t => violation(s"Expected type with SliceT as underlying type, but got $t instead.")
        }

        val sliceType = in.SliceT(elemType, Addressability.inParameter)

        // parameters
        val sliceParam = in.Parameter.In("slice", sliceType)(src)
        val pParam = in.Parameter.In("p", in.PermissionT(Addressability.Exclusive))(src)
        val variadicParam = in.Parameter.In("elems", sliceType)(src)
        val args = Vector(pParam, sliceParam, variadicParam)

        // results
        val resultParam = in.Parameter.Out("res", sliceType)(src)
        val results = Vector(resultParam)

        // preconditions
        val preSlice = accessSlice(sliceParam, in.FullPerm(src))
        val preVariadic = accessSlice(variadicParam, pParam)
        val pPre = in.ExprAssertion(in.LessCmp(in.NoPerm(src), pParam)(src))(src)
        val pres: Vector[in.Assertion] = Vector(pPre, preSlice, preVariadic)

        // postconditions
        val postLen = in.ExprAssertion(
          in.EqCmp(
            in.Length(resultParam)(src),
            in.Add(in.Length(sliceParam)(src), in.Length(variadicParam)(src))(src)
          )(src)
        )(src)
        val postRes = accessSlice(resultParam, in.FullPerm(src))
        val postVariadic = accessSlice(variadicParam, pParam)
        val postCmpSlice = quantify(
          trigger = { i => Vector(in.Trigger(Vector(in.Ref(in.IndexedExp(resultParam, i, sliceType)(src))(src)))(src)) },
          range = { inRange(_, in.IntLit(0)(src), in.Length(sliceParam)(src)) },
          body = {
            i => in.ExprAssertion(
              in.GhostEqCmp(
                in.IndexedExp(resultParam, i, sliceType)(src),
                in.Old(in.IndexedExp(sliceParam, i, sliceType)(src))(src)
              )(src)
            )(src)
          }
        )
        val postCmpVariadic = quantify(
          trigger = { i => Vector(in.Trigger(Vector(in.Ref(in.IndexedExp(resultParam, i, sliceType)(src))(src)))(src)) },
          range = { inRange(_,  in.Length(sliceParam)(src), in.Length(resultParam)(src)) },
          body = { i =>
            in.ExprAssertion(
              in.GhostEqCmp(
                in.IndexedExp(resultParam, i, sliceType)(src),
                in.IndexedExp(variadicParam, in.Sub(i, in.Length(sliceParam)(src))(src), sliceType)(src),
              )(src)
            )(src)
          }
        )
        val posts: Vector[in.Assertion] = Vector(postLen, postRes, postVariadic, postCmpSlice, postCmpVariadic)

        in.Function(x.name, args, results, pres, posts, Vector(in.NonItfMethodWildcardMeasure(None)(src)), Vector.empty, None)(src)

      case (CopyFunctionTag, Vector(t1, t2, _)) =>
        /**
          * requires 0 < p
          * requires forall i int :: { &dst[i] } (0 <= i && i < len(dst)) ==> acc(&dst[i], write)
          * requires forall i int :: { &src[i] } (0 <= i && i < len(src)) ==> acc(&src[i], p)
          * ensures len(dst) <= len(src) ==> res == len(dst)
          * ensures len(src) < len(dst) ==> res == len(src)
          * ensures forall i int :: { &dst[i] } 0 <= i && i < len(dst) ==> acc(&dst[i], write)
          * ensures forall i int :: { &src[i] } 0 <= i && i < len(src) ==> acc(&src[i], p)
          * ensures forall i int :: { &dst[i] } (0 <= i && i < len(src) && i < len(dst)) ==> dst[i] === old(src[i])
          * ensures forall i int :: { &dst[i] } (len(src) <= i && i < len(dst)) ==> dst[i] === old(dst[i])
          * func copy(dst, src []int, ghost p perm) (res int)
          */

        // TODO: add support for the case where `src` and `dst` are aliased. According to the language spec, the result
        //       of copy should be independent of whether the memory referenced by the arguments overlaps.
        //       This case used to be supported but was disabled in PR #439
        //       (https://github.com/viperproject/gobra/pull/439/files) due to bad performance.

        // parameters
        val dstParam = in.Parameter.In("dst", t1)(src)
        val dstUnderlyingType: in.SliceT = ctx.underlyingType(t1) match {
          case t: in.SliceT => t
          case t => violation(s"Expected type with SliceT as underlying type, but got $t instead.")
        }
        val srcParam = in.Parameter.In("src", t2)(src)
        val srcUnderlyingType: in.SliceT = ctx.underlyingType(t2) match {
          case t: in.SliceT => t
          case t => violation(s"Expected type with SliceT as underlying type, but got $t instead.")
        }
        val pParam = in.Parameter.In("p", in.PermissionT(Addressability.inParameter))(src)
        val args = Vector(dstParam, srcParam, pParam)

        // results
        val resParam = in.Parameter.Out("res", in.IntT(Addressability.outParameter))(src)
        val results = Vector(resParam)

        // preconditions
        val pPre = in.ExprAssertion(in.LessCmp(in.NoPerm(src), pParam)(src))(src)
        val preDst = quantify(
          trigger = { i =>
            Vector(in.Trigger(Vector(in.Ref(in.IndexedExp(dstParam, i, dstUnderlyingType)(src))(src)))(src))
          },
          range = { i => inRange(i, in.IntLit(0)(src), in.Length(dstParam)(src)) },
          body = { i =>
            in.Access(in.Accessible.Address(in.IndexedExp(dstParam, i, dstUnderlyingType)(src)), in.FullPerm(src))(src)
          }
        )
        val preSrc = quantify(
          trigger = { i => Vector(in.Trigger(Vector(in.Ref(in.IndexedExp(srcParam, i, srcUnderlyingType)(src))(src)))(src)) },
          range = { i => inRange(i, in.IntLit(0)(src), in.Length(srcParam)(src)) },
          body = { i => in.Access(in.Accessible.Address(in.IndexedExp(srcParam, i, srcUnderlyingType)(src)), pParam)(src) }
        )

        val pres = Vector(pPre, preDst, preSrc)

        // postconditions
        val postRes1 = in.Implication(
          in.AtMostCmp(in.Length(dstParam)(src), in.Length(srcParam)(src))(src),
          in.ExprAssertion(in.EqCmp(in.Length(dstParam)(src), resParam)(src))(src)
        )(src)

        val postRes2 = in.Implication(
          in.LessCmp(in.Length(srcParam)(src), in.Length(dstParam)(src))(src),
          in.ExprAssertion(in.EqCmp(in.Length(srcParam)(src), resParam)(src))(src)
        )(src)

        // the assertions in the pre-conditions can be reused here
        val postDst = preDst
        val postSrc = preSrc
        val postUpdate = quantify(
          trigger = { i => Vector(in.Trigger(Vector(in.Ref(in.IndexedExp(dstParam, i, dstUnderlyingType)(src))(src)))(src)) },
          range = { i =>
            in.And(
              inRange(i, in.IntLit(0)(src), in.Length(srcParam)(src)),
              inRange(i, in.IntLit(0)(src), in.Length(dstParam)(src)),
            )(src)
          },
          body = { i =>
            in.ExprAssertion(
              in.GhostEqCmp(
                in.IndexedExp(dstParam, i, dstUnderlyingType)(src),
                in.Old(in.IndexedExp(srcParam, i, srcUnderlyingType)(src))(src)
              )(src)
            )(src)
          }
        )
        val postSame = quantify(
          trigger = { i => Vector(in.Trigger(Vector(in.Ref(in.IndexedExp(dstParam, i, dstUnderlyingType)(src))(src)))(src)) },
          range = { i => inRange(i, in.Length(srcParam)(src), in.Length(dstParam)(src)) },
          body = { i =>
            in.ExprAssertion(
              in.GhostEqCmp(
                in.IndexedExp(dstParam, i, dstUnderlyingType)(src),
                in.Old(in.IndexedExp(dstParam, i, dstUnderlyingType)(src))(src)
              )(src)
            )(src)
          }
        )

        val posts = Vector(postRes1, postRes2, postDst, postSrc, postUpdate, postSame)

        in.Function(x.name, args, results, pres, posts, Vector(in.NonItfMethodWildcardMeasure(None)(src)), Vector.empty, None)(src)

      case (tag, args) => violation(s"no function generation defined for tag $tag and arguments $args")
    }
  }

  private def translateFPredicate(x: in.BuiltInFPredicate)(@unused ctx: Context): in.FPredicate = {
    val src = x.info
    (x.tag, x.argsT) match {
      case (PredTrueFPredTag, args) =>
        /**
          * pred PredTrue([args]) {
          *   true
          * }
          */
        val params = args.zipWithIndex.map {
          case (arg, idx) =>
            assert(arg.addressability == Addressability.inParameter)
            in.Parameter.In(s"arg$idx", arg)(src)
        }
        val body: Option[in.Assertion] = Some(in.ExprAssertion(in.BoolLit(b = true)(src))(src))
        in.FPredicate(x.name, params, body)(src)
      case (tag, args) => violation(s"no fpredicate generation defined for tag $tag and arguments $args")
    }
  }

  private def translateMPredicate(x: in.BuiltInMPredicate)(@unused ctx: Context): in.MPredicate = {
    val src = x.info
    assert(x.receiverT.addressability == Addressability.inParameter)
    val recvParam = in.Parameter.In("c", x.receiverT)(src)

    x.tag match {
      case IsChannelMPredTag =>
        /**
          * pred (c chan T) IsChannel()
          */
        in.MPredicate(recvParam, x.name, Vector(), None)(src)
      case SendChannelMPredTag | RecvChannelMPredTag | ClosedMPredTag =>
        /**
          * pred (c chan T) [tag.identifier]()
          */
        in.MPredicate(recvParam, x.name, Vector(), None)(src)
      case ClosureDebtMPredTag =>
        /**
          * pred (c chan T) ClosureDebt(P pred(), dividend int, divisor int /* p perm */)
          */
        val predicateParam = in.Parameter.In("P", in.PredT(Vector(), Addressability.inParameter))(src)
        val dividendParam = in.Parameter.In("dividend", in.IntT(Addressability.inParameter))(src)
        val divisorParam = in.Parameter.In("divisor", in.IntT(Addressability.inParameter))(src)
        // val permissionAmountParam = in.Parameter.In("p", in.PermissionT(Addressability.inParameter))(src)
        in.MPredicate(recvParam, x.name, Vector(predicateParam, dividendParam, divisorParam /* permissionAmountParam */), None)(src)
      case TokenMPredTag =>
        /**
          * pred (c chan T) Token(P pred())
          */
        val predicateParam = in.Parameter.In("P", in.PredT(Vector(), Addressability.inParameter))(src)
        in.MPredicate(recvParam, x.name, Vector(predicateParam), None)(src)
      case tag => violation(s"no mpredicate generation defined for tag $tag")
    }
  }

  /**
    * Helper method to create an accessible predicate for a built-in mpredicate
    */
  private def builtInMPredAccessible(tag: BuiltInMPredicateTag, recv: in.Expr, args: Vector[in.Expr])(src: Source.Parser.Info)(ctx: Context): in.Accessible.Predicate = {
    val proxy = getOrGenerateMPredicate(tag, recv.typ, args.map(_.typ))(src)(ctx)
    in.Accessible.Predicate(in.MPredicateAccess(recv, proxy, args)(src))
  }

  /**
    * Helper method to create a pure method call to a built-in method
    */
  private def builtInPureMethodCall(tag: BuiltInMethodTag, recv: in.Expr, args: Vector[in.Expr], retType: in.Type)(src: Source.Parser.Info)(ctx: Context): in.PureMethodCall = {
    val method = getOrGenerateMethod(tag, recv.typ, args.map(_.typ))(src)(ctx)
    in.PureMethodCall(recv, method, args, retType, false)(src)
  }
}
