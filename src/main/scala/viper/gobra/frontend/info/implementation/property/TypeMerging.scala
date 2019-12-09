package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type.{Single, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait TypeMerging extends BaseProperty { this: TypeInfoImpl =>

  /**
    * Returns the set of the minimal supertypes of l and r.
    * The resulting set may contain a single type, for example the interface l and r both implement.
    * However, the minimal supertype might not be unique, e.g. if l and r both implement the same two interfaces.
    * If no common supertype exists, the returned set is empty
    * @param l
    * @param r
    * @return non-null set
    */
  def typeMerge(l: Type, r: Type): Set[Type] =
    // currently, only merging of l and r being identical types is supported
    (l, r) match {
      case (Single(lst), Single(rst)) => if (identicalTypes(lst, rst)) Set[Type](lst) else Set[Type]()
      case _ => Set[Type]()
    }

  lazy val mergeableTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
    case (left, right) => s"$left is not mergeable with $right"
  } {
    case (left, right) => typeMerge(left, right).nonEmpty
  }

}
