package viper.gobra.frontend.info.implementation.property

import org.bitbucket.inkytonik.kiama.util.Messaging.error
import viper.gobra.ast.frontend.{PIdnUse, PInterfaceType, PNamedOperand, PType, PTypeElement}
import viper.gobra.frontend.info.base.Type.InterfaceT
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.resolution.TypeSet

trait Satisfiability extends BaseProperty { this: TypeInfoImpl =>

  lazy val satisfies: Property[(PType, PInterfaceType)] = createProperty {
    case (t, PInterfaceType(Vector(PTypeElement(Vector(PNamedOperand(PIdnUse("comparable"))))), methSpecs, _)) =>
      comparableType.result(symbType(t)) and goImplements.result(t, PInterfaceType(Vector(), methSpecs, Vector()))
    case (t, constraintType) =>
      goImplements.result(t, constraintType)
  }

  private lazy val goImplements: Property[(PType, PInterfaceType)] = createProperty {
    case (pType: PType, interfacePType: PInterfaceType) =>
      val typ = symbType(pType)
      val interfaceTypeSet = TypeSet.from(interfacePType, this)

      (pType match {
        case i: PInterfaceType =>
          failedProp("is not a subset of the allowed type set", !TypeSet.isSubset(TypeSet.from(i, this), interfaceTypeSet))
        case _ =>
          failedProp("is not an element of the allowed type set", !TypeSet.contains(interfaceTypeSet, typ))
      }) and syntaxImplements(typ, InterfaceT(interfacePType, this))
  }
}
