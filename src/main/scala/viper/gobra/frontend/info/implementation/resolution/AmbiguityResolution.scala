package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.resolution.{AstPattern => ap}
import org.bitbucket.inkytonik.kiama.util.{Entity, UnknownEntity}
import viper.gobra.frontend.info.implementation.resolution.AstPattern.EntityLike

trait AmbiguityResolution { this: TypeInfoImpl =>

  def resolve(n: PTypeOrExpr): Option[AstPattern] = {
    n match {
      case n: PNamedOperand => resolveNamedOperand(n)
      case n: PDereference => resolveDereference(n)
      case n: PDot => resolveDot(n)
      case n: PInvoke => resolveInvoke(n)
      case _ => None
    }
  }

  def resolveNamedOperand(n: PNamedOperand): Option[AstPattern with EntityLike] = {
    // named type
    // variable
    // function
    // predicate
    // (constant)
    entity(n.id) match {
      case t: st.TypeEntity => Some(ap.NamedType(n.id, t))
      case v: st.Variable => Some(ap.Variable(n.id, v))
      case f: st.Function => Some(ap.Function(n.id, f, None))
      case p: st.Predicate => Some(ap.Predicate(n.id, p))
      case _ => None
    }
  }

  def resolveDereference(n: PDereference): Option[AstPattern] = {
    // pointer type
    // dereference
    isType(n.operand) match {
      case Some(Left(operand)) => Some(ap.PointerType(operand))
      case Some(Right(operand)) => Some(ap.Dereference(operand))
      case _ => None
    }
  }

  def resolveDot(n: PDot): Option[AstPattern with EntityLike] = {
    // method expression
    // selection
    // received method
    // received predicate
    def retMethodExpr(t: PType): Option[AstPattern with EntityLike] = {
      findMethodLike(typeType(t), n.id) match {
        case Some(e) => Some(ap.MethodExpression(t, e))
        case _ => None
      }
    }
    isType(n.base) match {
      case Some(Left(t)) => t match {
        case no: PNamedOperand => resolveNamedOperand(no) match {
          case Some(_: Package) => None // TODO perform ST lookup of n.arg in package
          case _ => retMethodExpr(t)
        }
        case _ => retMethodExpr(t)
      }
      case Some(Right(e)) => findDot(e, n.id) match {
        case Some((f: st.Field, mp: Vector[MemberPath])) => Some(ap.Selection(e, f, mp))
        case Some((m: st.Method, mp: Vector[MemberPath])) => Some(ap.ReceivedMethod(m, mp))
        case Some((p: st.MPredicate, mp: Vector[MemberPath])) => Some(ap.ReceivedPredicate(p, mp))
        case _ => None
      }
      case _ => None
    }
  }

  def resolveDotEntity(n: PDot): Entity = {
    resolveDot(n) match {
      case Some(e: AstPattern) => e.entity
      case _ => UnknownEntity()
    }
  }

  def resolveInvoke(n: PInvoke): Option[AstPattern] = {
    // conversion
    // predicate access
    // call
    isType(n.callee) match {
      case Some(Left(t)) => Some(ap.Conversion(t, n.args))
      case Some(Right(_)) => resolve(n.callee) match {
        case Some(ce: ap.CallableExpression) => Some(ap.Call(ce, n.args))
        case Some(ca: ap.CallableAssertion) => Some(ap.PredicateAccess(ca, n.args))
        case _ => None
      }
      case _ => None
    }
  }
  /*
  def resolveConversionOrUnaryCall[T](n: PConversionOrUnaryCall)
                                     (conversion: (PUseLikeId, PExpression) => T)
                                     (unaryCall: (PUseLikeId, PExpression) => T): Option[T] =
    if (pointsToType(n.base))      Some(conversion(n.base, n.arg))
    else if (pointsToData(n.base)) Some(unaryCall(n.base, n.arg))
    else None
  *//*
  def resolveSelectionOrMethodExpr[T](n: PSelectionOrMethodExpr)
                                     (selection: (PUseLikeId, PIdnUse) => T)
                                     (methodExp: (PUseLikeId, PIdnUse) => T): Option[T] =
    if (pointsToType(n.base))      Some(methodExp(n.base, n.id))
    else if (pointsToData(n.base)) Some(selection(n.base, n.id))
    else None
  *//*
  def resolveMPredOrMethExprOrRecvCall[T](n: PMPredOrMethRecvOrExprCall)
                                         (predOrMethCall: (PUseLikeId, PIdnUse, Vector[PExpression]) => T)
                                         (predOrMethExprCall: (PUseLikeId, PIdnUse, Vector[PExpression]) => T)
                                         : Option[T] =
    if (pointsToType(n.base))      Some(predOrMethCall(n.base, n.id, n.args))
    else if (pointsToData(n.base)) Some(predOrMethExprCall(n.base, n.id, n.args))
    else None
  */
  def isDef[T](n: PIdnUnk): Boolean = !st.isDefinedInScope(sequentialDefenv.in(n), serialize(n))

  def isType(n: PTypeOrExpr): Option[Either[PType, PExpression]] = {
    def decide[A <: PType with PExpression](n: A, r: Option[AstPattern]): Option[Either[PType, PExpression]] = {
      r match {
        case Some(_: ap.Type) => Some(Left(n))
        case Some(_) => Some(Right(n))
        case None => n match {
          case _: PType => Some(Left(n))
          case _ => Some(Right(n))
        }
      }
    }
    n match {
      case n: PNamedOperand => decide(n, resolveNamedOperand(n))
      case n: PDereference => decide(n, resolveDereference(n))
      case n: PDot => decide(n, resolveDot(n))
      case _ => None
    }
  }
}
