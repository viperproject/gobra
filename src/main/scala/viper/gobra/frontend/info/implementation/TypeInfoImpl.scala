package viper.gobra.frontend.info.implementation

import org.bitbucket.inkytonik.kiama.attribution.Attribution
import viper.gobra.frontend.info.implementation.property._
import viper.gobra.frontend.info.implementation.resolution.{AmbiguityResolution, Enclosing, MemberResolution, NameResolution}
import viper.gobra.frontend.info.implementation.typing._
import viper.gobra.frontend.info.{Info, TypeInfo}

class TypeInfoImpl(final val tree: Info.GoTree) extends Attribution with TypeInfo

  with NameResolution
  with MemberResolution
  with AmbiguityResolution
  with Enclosing

  with TopLevelTyping
  with StmtTyping
  with ExprTyping
  with TypeTyping
  with IdTyping
  with MiscTyping

  with Convertibility
  with Comparability
  with Assignability
  with Addressability
  with TypeIdentity
  with PointsTo
  with Executability
  with ConstantEvaluation
  with Implements
  with UnderlyingType

  with Errors
{
  import org.bitbucket.inkytonik.kiama.attribution.Decorators
  protected val decorators = new Decorators(tree)
}

