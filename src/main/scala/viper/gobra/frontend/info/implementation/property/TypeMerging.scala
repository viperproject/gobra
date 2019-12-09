package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.{Single, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TypeMerging extends BaseProperty { this: TypeInfoImpl =>

  /**
    * Returns the unique common assignable type of l and r (if it exists).
    */
  def typeMerge(l: Type, r: Type): Option[Type] =
    // currently, only merging of l and r being identical types is supported
    // possible future improvement: if l is assignable to r, then return r instead of None (or vice versa)
    (l, r) match {
      case (Single(lst), Single(rst)) => if (identicalTypes(lst, rst)) Some(lst) else None
      case _ => None
    }

  lazy val mergeableTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not mergeable with $right"
  } {
    case (left, right) => typeMerge(left, right).isDefined
  }

}
