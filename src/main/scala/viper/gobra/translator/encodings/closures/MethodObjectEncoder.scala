// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.closures

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.ErrorTransformer
import viper.gobra.reporting.{InterfaceReceiverIsNilReason, MethodObjectGetterPreconditionError, Source}
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.interfaces.{InterfaceComponent, InterfaceComponentImpl, InterfaceUtils, PolymorphValueComponent, PolymorphValueComponentImpl, TypeComponent, TypeComponentImpl}
import viper.gobra.translator.util.ViperWriter.CodeLevel.errorT
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter, MemberLevel => ml}
import viper.gobra.util.Violation.violation
import viper.silver.verifier.{errors => vprerr}
import viper.silver.{ast => vpr}


class MethodObjectEncoder(domain: ClosureDomainEncoder) {

  private val interfaces: InterfaceComponent = new InterfaceComponentImpl
  private val types: TypeComponent = new TypeComponentImpl
  private val poly: PolymorphValueComponent = new PolymorphValueComponentImpl(interfaces, types)
  private val interfaceUtils = new InterfaceUtils(interfaces, types, poly)

  def callToMethodClosureGetter(m: in.MethodObject)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(m)(ctx)
    callToGetterFunction(m)(m.info)(ctx)
  }

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    closureGetters foreach { m => addMemberFn(m._2.res) }
  }

  private val genericFuncType: in.FunctionT = in.FunctionT(Vector.empty, Vector.empty, Addressability.rValue)

  /** For all methods that are used as closure variables (e.g. assigned to a variables, or used within a call with spec),
    * a getter function is generated. */
  private def register(m: in.MethodObject)(ctx: Context): Unit = m.recv.typ match {
    case d: in.DefinedT => ctx.table.lookup(d) match {
      case i: in.InterfaceT => ctx.table.implementations(i) foreach {
          case t: in.DefinedT =>
            val defTProxy = defTMethodProxy(t, m.meth.name)(ctx)
            if (!closureGetters.contains(defTProxy)) closureGetters += defTProxy -> getter(t, defTProxy)(ctx)
          case _ => violation("expected defined type")
        }
        if (!closureGetters.contains(m.meth)) closureGetters += m.meth -> getter(i, m.meth)(ctx)
      case _ => if (!closureGetters.contains(m.meth)) closureGetters += m.meth -> getter(d, m.meth)(ctx)
    }
    case _ => violation("expected defined type")
  }

  var closureGetters: Map[in.MethodProxy, MemberWriter[vpr.Member]] = Map.empty

  private def defTMethodProxy(t: in.DefinedT, name: String)(ctx: Context): in.MethodProxy =
    ctx.table.lookup(t, name).get.asInstanceOf[in.MethodProxy]

  private def getterFunctionProxy(meth: in.MethodProxy): in.MethodProxy =
    in.MethodProxy(s"${Names.closureGetter}$$${meth.name}", s"${Names.closureGetter}$$${meth.uniqueName}")(meth.info)

  /** Encodes simple getter function for struct method objects, encoded as:
    *   function closureGet$[methodName](self: [structType]): Closure */
  private def getter(recvType: in.DefinedT, meth: in.MethodProxy)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = getterFunctionProxy(meth)
    val info = meth.info
    val receiver = in.Parameter.In("self", recvType)(info)
    val result = in.Parameter.Out("closure", genericFuncType)(info)
    val getter = in.PureMethod(receiver, proxy, Vector.empty, Vector(result), Vector.empty, Vector.empty, Vector.empty, None)(info)
    ctx.defaultEncoding.pureMethod(getter)(ctx)
  }

  /** Encodes a getter function for interface method objects, encoded as follows:
    *   function closureGet$[methodName](thisItf: [interfaceType]): Closure
    *     requires [thisItf != nil]
    *     ensures [typeOf(thisItf) == strN] ==> result == closureGet$[strNMethodName]([unbox(thisItf)])
    *
    *     where there is a postcondition for all implementing structs
    */
  private def getter(recvType: in.InterfaceT, meth: in.MethodProxy)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = getterFunctionProxy(meth)
    val receiver = in.Parameter.In("thisItf", recvType)(meth.info)
    val recvExp = ctx.expression(receiver).res
    val (pos, info: Source.Verifier.Info, errT) = meth.vprMeta
    val recvNotNil = interfaceUtils.receiverNotNil(recvExp)(pos, info, errT)(ctx)
    val defTCall: in.DefinedT => vpr.Exp = t => vpr.FuncApp(getterFunctionProxy(defTMethodProxy(t, meth.name)(ctx)).uniqueName,
      Seq(poly.unbox(interfaces.polyValOf(recvExp)()(ctx), t)(pos, info, errT)(ctx)))(pos, info, domain.vprType, errT)
    val valueMatchesIfTypeIs: in.DefinedT => vpr.Exp = t => vpr.Implies(
      vpr.EqCmp(interfaces.dynTypeOf(recvExp)(pos, info, errT)(ctx), types.typeToExpr(t)(pos, info, errT)(ctx))(pos, info, errT),
      vpr.EqCmp(vpr.Result(domain.vprType)(), defTCall(t))())()
    val posts = ctx.table.implementations(recvType).toSeq.map(t => valueMatchesIfTypeIs(t.asInstanceOf[in.DefinedT]))
    ml.unit(vpr.Function(proxy.uniqueName, Seq(ctx.variable(receiver)), domain.vprType, Seq(recvNotNil), posts, None)())
  }

  private def receiverNilErr(m: in.MethodObject, call: vpr.Exp): ErrorTransformer = {
    case vprerr.PreconditionInAppFalse(offendingNode, _, _) if call eq offendingNode =>
      val info = m.vprMeta._2.asInstanceOf[Source.Verifier.Info]
      val recvInfo = m.recv.vprMeta._2.asInstanceOf[Source.Verifier.Info]
      MethodObjectGetterPreconditionError(info).dueTo(InterfaceReceiverIsNilReason(recvInfo))
  }

  private def callToGetterFunction(m: in.MethodObject)(info: Source.Parser.Info)(ctx: Context): CodeWriter[vpr.Exp] = for {
    call <- ctx.expression(in.PureMethodCall(m.recv, getterFunctionProxy(m.meth), Vector.empty, genericFuncType)(info))
    _ <- errorT(receiverNilErr(m, call))
  } yield call
}
