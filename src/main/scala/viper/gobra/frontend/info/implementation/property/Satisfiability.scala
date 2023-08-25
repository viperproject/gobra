package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PIdnUse, PInterfaceType, PNamedOperand, PTypeElement}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.resolution.TypeSet

trait Satisfiability extends BaseProperty { this: TypeInfoImpl =>

  lazy val satisfies: Property[(Type, PInterfaceType)] = createProperty {
    case (t, PInterfaceType(Vector(PTypeElement(Vector(PNamedOperand(PIdnUse("comparable"))))), methSpecs, _)) =>
      comparableType.result(t) and goImplements.result(t, PInterfaceType(Vector(), methSpecs, Vector()))
    case (t, constraintType) =>
      goImplements.result(t, constraintType)
  }

  private lazy val goImplements: Property[(Type, PInterfaceType)] = createProperty {
    case (typ: Type, interfacePType: PInterfaceType) =>
      val interfaceTypeSet = TypeSet.from(interfacePType, this)

      (typ match {
        case i: InterfaceT =>
          failedProp("is not a subset of the allowed type set", !TypeSet.isSubset(TypeSet.from(i.decl, this), interfaceTypeSet))
        case _ =>
          failedProp("is not an element of the allowed type set", !TypeSet.contains(interfaceTypeSet, typ))
      }) and syntaxImplements(typ, InterfaceT(interfacePType, this))
  }
}
