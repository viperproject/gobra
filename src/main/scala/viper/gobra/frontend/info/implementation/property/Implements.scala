package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Implements { this: TypeInfoImpl =>

  def implements(l: Type, r: Type): Boolean = false

}
