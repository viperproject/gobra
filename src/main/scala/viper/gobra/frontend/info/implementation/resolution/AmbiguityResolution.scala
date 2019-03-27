package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.isDefinedInScope
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait AmbiguityResolution { this: TypeInfoImpl =>

  def resolveConversionOrUnaryCall[T](n: PConversionOrUnaryCall)
                                     (conversion: (PIdnUse, PExpression) => T)
                                     (unaryCall: (PIdnUse, PExpression) => T): Option[T] =
    if (pointsToType(n.base))      Some(conversion(n.base, n.arg))
    else if (pointsToData(n.base)) Some(unaryCall(n.base, n.arg))
    else None

  def resolveSelectionOrMethodExpr[T](n: PSelectionOrMethodExpr)
                                     (selection: (PIdnUse, PIdnUse) => T)
                                     (methodExp: (PIdnUse, PIdnUse) => T): Option[T] =
    if (pointsToType(n.base))      Some(methodExp(n.base, n.id))
    else if (pointsToData(n.base)) Some(selection(n.base, n.id))
    else None

  def isDef[T](n: PIdnUnk): Boolean = !isDefinedInScope(sequentialDefenv.in(n), serialize(n))
}
