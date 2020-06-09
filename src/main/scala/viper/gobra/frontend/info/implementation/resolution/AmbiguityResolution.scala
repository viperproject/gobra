package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.base.SymbolTable.isDefinedInScope
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait AmbiguityResolution { this: TypeInfoImpl =>

  def isDef[T](n: PIdnUnk): Boolean = !isDefinedInScope(sequentialDefenv.in(n), serialize(n))

  def exprOrType(n: PExpressionOrType): Either[PExpression, PType] = {
    n match {
      // Ambiguous nodes
      case n: PNamedOperand =>
        if (pointsToType(n.id)) Right(n) else Left(n)

      case n: PDeref =>
        if (exprOrType(n.base).isLeft) Left(n) else Right(n)

      case n: PDot => Left(n) // TODO: when we support packages, then it can also be the type defined in a package

      // Otherwise just expression or type
      case n: PExpression => Left(n)
      case n: PType => Right(n)
    }
  }

  def asExpr(n: PExpressionOrType): Option[PExpression] = exprOrType(n).left.toOption
  def asType(n: PExpressionOrType): Option[PType] = exprOrType(n).right.toOption



  def resolve(n: PExpressionOrType): Option[ap.Pattern] = n match {

    case n: PNamedOperand =>
      entity(n.id) match {
        case s: st.NamedType => Some(ap.NamedType(n.id, s))
        case s: st.Variable => Some(ap.LocalVariable(n.id, s))
        case s: st.Function => Some(ap.Function(n.id, s))
        case s: st.FPredicate => Some(ap.Predicate(n.id, s))
        case _ => None
      }

    case n: PDeref =>
      exprOrType(n.base) match {
        case Left(expr) => Some(ap.Deref(expr))
        case Right(typ) => Some(ap.PointerType(typ))
      }

    case n: PDot =>
      (exprOrType(n.base), tryDotLookup(n.base, n.id)) match {

        case (Left(base), Some((s: st.StructMember, path))) => Some(ap.FieldSelection(base, n.id, path, s))
        case (Left(base), Some((s: st.Method, path))) => Some(ap.ReceivedMethod(base, n.id, path, s))
        case (Left(base), Some((s: st.MPredicate, path))) => Some(ap.ReceivedPredicate(base, n.id, path, s))

        case (Right(base), Some((s: st.Method, path))) => Some(ap.MethodExpr(base, n.id, path, s))
        case (Right(base), Some((s: st.MPredicate, path))) => Some(ap.PredicateExpr(base, n.id, path, s))
        // imported members
        case (Right(base), Some((s: st.Function, path))) => Some(ap.Function(n.id, s))
        case (Right(base), Some((s: st.ActualTypeEntity, path))) => Some(ap.NamedType(n.id, s))
        case (Right(base), Some((s: st.Constant, path))) => Some(ap.Constant(n.id, s))

        case _ => None
      }

    case n: PInvoke =>
      exprOrType(n.base) match {
        case Right(t) => Some(ap.Conversion(t, n.args))
        case Left(e) =>
          resolve(e) match {
            case Some(p: ap.FunctionKind) => Some(ap.FunctionCall(p, n.args))
            case Some(p: ap.PredicateKind) => Some(ap.PredicateCall(p, n.args))
            case _ => None
          }
      }

      // unknown pattern
    case _ => None
  }


}
