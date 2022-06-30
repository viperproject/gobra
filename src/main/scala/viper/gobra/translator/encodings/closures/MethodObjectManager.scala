package viper.gobra.translator.encodings.closures

import viper.gobra.ast.{internal => in}
import viper.gobra.reporting.Source
import viper.gobra.theory.Addressability
import viper.gobra.translator.Names
import viper.gobra.translator.context.Context
import viper.gobra.translator.encodings.interfaces.{InterfaceComponent, InterfaceComponentImpl, InterfaceUtils, PolymorphValueComponent, PolymorphValueComponentImpl, TypeComponent, TypeComponentImpl}
import viper.gobra.translator.util.ViperWriter.{CodeWriter, MemberWriter, MemberLevel => ml}
import viper.gobra.util.Violation.violation
import viper.silver.{ast => vpr}


class MethodObjectManager(domain: ClosureDomainManager) {

  private val interfaces: InterfaceComponent = new InterfaceComponentImpl
  private val types: TypeComponent = new TypeComponentImpl
  private val poly: PolymorphValueComponent = new PolymorphValueComponentImpl(interfaces, types)
  private val interfaceUtils = new InterfaceUtils(interfaces, types, poly)

  def callToMethodClosureGetter(m: in.MethodObject)(ctx: Context): CodeWriter[vpr.Exp] = {
    register(m)(ctx)
    ctx.expression(callToGetterFunction(m.recv, m.meth)(m.info))
  }

  def finalize(addMemberFn: vpr.Member => Unit): Unit = {
    closureGetters foreach { m => addMemberFn(m._2.res) }
  }

  private val genericFuncType: in.FunctionT = in.FunctionT(Vector.empty, Vector.empty, Addressability.rValue)

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

  private def getter(recvType: in.DefinedT, meth: in.MethodProxy)(ctx: Context): MemberWriter[vpr.Member] = {
    val proxy = getterFunctionProxy(meth)
    val info = meth.info
    val receiver = in.Parameter.In("self", recvType)(info)
    val result = in.Parameter.Out("closure", genericFuncType)(info)
    val getter = in.PureMethod(receiver, proxy, Vector.empty, Vector(result), Vector.empty, Vector.empty, Vector.empty, None)(info)
    ctx.defaultEncoding.pureMethod(getter)(ctx)
  }

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

  private def callToGetterFunction(recv: in.Expr, meth: in.MethodProxy)(info: Source.Parser.Info): in.PureMethodCall =
    in.PureMethodCall(recv, getterFunctionProxy(meth), Vector.empty, genericFuncType)(info)
}
