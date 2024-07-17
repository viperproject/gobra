// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.translator.encodings.closures

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.BackTranslator.RichErrorMessage
import viper.gobra.reporting.BackTranslator.{ErrorTransformer, RichErrorMessage}
import viper.gobra.reporting.{ReceiverIsNilReason, MethodObjectGetterPreconditionError, Source}
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.interfaces.{InterfaceComponent, InterfaceComponentImpl, InterfaceUtils, PolymorphValueComponent, PolymorphValueComponentImpl, TypeComponent, TypeComponentImpl}
import viper.gobra.translator.util.ViperWriter.CodeLevel.errorT
import viper.gobra.translator.util.ViperWriter.CodeWriter
import viper.silver.verifier.{errors => vprerr}
import viper.silver.{ast => vpr}


/** Encoding of method objects */
class MethodObjectEncoder(domain: ClosureDomainEncoder) {

  import viper.gobra.translator.util.TypePatterns._

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    generatedConversions foreach (x => addMemberFn(x._2))
  }

  private val interfaces: InterfaceComponent = new InterfaceComponentImpl
  private val types: TypeComponent = new TypeComponentImpl
  private val poly: PolymorphValueComponent = new PolymorphValueComponentImpl(interfaces, types)
  private val interfaceUtils = new InterfaceUtils(interfaces, types, poly)

  /**
    * Encodes method objects.
    * [e.m] -> closureGet$[m]([e])
    */
  def encodeMethodObject(m: in.MethodObject)(ctx: Context): CodeWriter[vpr.Exp] = {
    val (pos, info, errT) = m.vprMeta
    for {
      arg <- ctx.expression(m.recv)
      func = genConversion(m.recv.typ, m.meth)(ctx)
      call = vpr.FuncApp(func = func, args = Seq(arg))(pos, info, errT)
      _ <- errorT(receiverNilErr(m, call))
    } yield call
  }

  /** Generates a function that converts expressions of type `recvType` to a closure for method `meth`.
    *
    * If `recvType` is not an interface type, then the following function is generated:
    *
    *     function closureGet$[meth](self: [recvType]): Closure
    *       requires decreases _ // the conversion always terminates
    *
    * Otherwise, for interface types, the following function is generated:
    *
    *     function closureGet$[meth](thisItf: [recvType]): Closure
    *       requires decreases _ // the conversion always terminates
    *       requires [thisItf != nil]
    *         // foreach type T that implements the interface type
    *       ensures [typeOf(thisItf) == T] ==> result == closureGet$[meth]([unbox(thisItf)])
    *
    */
  private def genConversion(recvType: in.Type, meth: in.MethodProxy)(ctx: Context): vpr.Function = {
    generatedConversions.getOrElse(meth, {
      val (pos, info: Source.Verifier.Info, errT) = meth.vprMeta

      val proxy = getterFunctionProxy(meth)
      val receiver = in.Parameter.In("self", recvType)(meth.info)
      val vprReceiver =  ctx.variable(receiver)
      val vprReceiverVar = vprReceiver.localVar

      val result = underlyingType(recvType)(ctx) match {
        case recvType: in.InterfaceT =>
          val recvNotNil = interfaceUtils.receiverNotNil(vprReceiverVar)(pos, info, errT)(ctx)
          val defTCall: in.Type => vpr.Exp = t => {
            val func = genConversion(t, defTMethodProxy(t, meth.name)(ctx))(ctx)
            vpr.FuncApp(func = func, args = Seq(poly.unbox(interfaces.polyValOf(vprReceiverVar)()(ctx), t)(pos, info, errT)(ctx)))(pos, info, errT)
          }
          val valueMatchesIfTypeIs: in.Type => vpr.Exp = t => vpr.Implies(
            vpr.EqCmp(interfaces.dynTypeOf(vprReceiverVar)(pos, info, errT)(ctx), types.typeToExpr(t)(pos, info, errT)(ctx))(pos, info, errT),
            vpr.EqCmp(vpr.Result(domain.vprType)(), defTCall(t))()
          )()
          val posts = ctx.table.getImplementations(recvType).toSeq.map(valueMatchesIfTypeIs)
          vpr.Function(proxy.uniqueName, Seq(ctx.variable(receiver)), domain.vprType, Seq(recvNotNil), posts, None)(pos, info, errT)

        case _ =>
          vpr.Function(proxy.uniqueName, Seq(ctx.variable(receiver)), domain.vprType, Seq.empty, Seq.empty, None)(pos, info, errT)
      }

      generatedConversions += (meth -> result)
      result
    })
  }
  private var generatedConversions: Map[in.MethodProxy, vpr.Function] = Map.empty

  private def defTMethodProxy(t: in.Type, name: String)(ctx: Context): in.MethodProxy =
    ctx.table.lookup(t, name).get.asInstanceOf[in.MethodProxy]

  private def getterFunctionProxy(meth: in.MethodProxy): in.MethodProxy =
    in.MethodProxy(s"${Names.closureGetter}$$${meth.name}", s"${Names.closureGetter}$$${meth.uniqueName}")(meth.info)

  private def receiverNilErr(m: in.MethodObject, call: vpr.Exp): ErrorTransformer = {
    case e@vprerr.PreconditionInAppFalse(_, _, _) if e causedBy call=>
      val info = m.vprMeta._2.asInstanceOf[Source.Verifier.Info]
      val recvInfo = m.recv.vprMeta._2.asInstanceOf[Source.Verifier.Info]
      MethodObjectGetterPreconditionError(info).dueTo(ReceiverIsNilReason(recvInfo))
  }
}