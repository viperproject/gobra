// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import java.math.MathContext

import viper.gobra.ast.frontend.{AstPattern => ap, _}
import viper.gobra.frontend.info.base.SymbolTable.SingleConstant
import viper.gobra.frontend.info.base.Type.{BooleanT, IntT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.TypeBounds._
import viper.gobra.util.Violation.violation
import viper.gobra.util.GoString

trait ConstantEvaluation { this: TypeInfoImpl =>

  def evalBoolOrFail(exp: PExpression): Boolean = {
    boolConstantEval(exp).getOrElse(violation(s"expected constant bool expression, but got $exp"))
  }

  def evalIntOrFail(exp: PExpression): BigInt = {
    intConstantEval(exp).getOrElse(violation(s"expected constant int expression, but got $exp"))
  }

  def evalStringOrFail(exp: PExpression): GoString = {
    stringConstantEval(exp).getOrElse(violation(s"expected constant string expression, but got $exp"))
  }

  def evalPermOrFail(exp: PExpression): (BigInt, BigInt) = {
    permConstantEval(exp).getOrElse(violation(s"expected constant permission expression, but got $exp"))
  }

  lazy val boolConstantEval: PExpression => Option[Boolean] =
    attr[PExpression, Option[Boolean]] {
      case PBoolLit(lit) => Some(lit)
      case e: PUnaryExp => e match {
        case _: PNegation => boolConstantEval(e.operand).map(!_)
        case _ => None
      }
      case e: PBinaryExp[_,_] =>
        def auxBool[T](l: PExpression, r: PExpression)(f: Boolean => Boolean => Boolean): Option[Boolean] =
          (boolConstantEval(l), boolConstantEval(r)) match {
            case (Some(a), Some(b)) => Some(f(a)(b))
            case _ => None
          }
        def auxInt[T](l: PExpression, r: PExpression)(f: BigInt => BigInt => Boolean): Option[Boolean] =
          (intConstantEval(l), intConstantEval(r)) match {
            case (Some(a), Some(b)) => Some(f(a)(b))
            case _ => None
          }

        for {
          l <- asExpr(e.left)
          r <- asExpr(e.right)
          res <- e match {
            case _: PEquals if typ(l) == BooleanT => auxBool(l,r)(x => y => x == y)
            case _: PEquals if typ(l).isInstanceOf[IntT] => auxInt(l,r)(x => y => x == y)
            case _: PUnequals if typ(l) == BooleanT => auxBool(l,r)(x => y => x != y)
            case _: PUnequals if typ(l).isInstanceOf[IntT] => auxInt(l,r)(x => y => x != y)
            case _: PAnd if typ(l) == BooleanT => auxBool(l,r)(x => y => x && y)
            case _: POr if typ(l) == BooleanT => auxBool(l,r)(x => y => x || y)
            case _: PLess if typ(l).isInstanceOf[IntT] => auxInt(l,r)(x => y => x < y)
            case _: PAtMost if typ(l).isInstanceOf[IntT] => auxInt(l,r)(x => y => x <= y)
            case _: PGreater if typ(l).isInstanceOf[IntT] => auxInt(l,r)(x => y => x > y)
            case _: PAtLeast if typ(l).isInstanceOf[IntT] => auxInt(l,r)(x => y => x >= y)
            case _ => None
          }
        } yield res

      case PNamedOperand(id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, context) => context.boolConstantEvaluation(exp)
        case _ => None
      }
      case PDot(_, id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, _) => boolConstantEval(exp)
        case _ => None
      }

      case _ => None
    }

  lazy val intConstantEval: PExpression => Option[BigInt] =
    attr[PExpression, Option[BigInt]] {
      case PIntLit(lit, _) => Some(lit)
      case inv: PInvoke => resolve(inv) match {
        case Some(conv: ap.Conversion) => underlyingTypeP(conv.typ) match {
          case Some(_: PIntegerType) => intConstantEval(conv.arg)
          case _ => None
        }
        case _ => None
      }
      case PBitNegation(op) =>
        // Not sufficient to do `intConstantEval(op) map (_.unary_~)`, produces wrong results for unsigned int values
        exprType(op) match {
          case IntT(t) =>
            val constEval = intConstantEval(op)
            constEval map { constValue =>
              t match {
                case UnboundedInteger | _: Signed => ~constValue
                case u: Unsigned => ~constValue mod (u.upper + 1)
              }
            }
          case _ => None
        }
      case e: PBinaryExp[_,_] =>
        def aux(l: PExpression, r: PExpression)(f: BigInt => BigInt => BigInt): Option[BigInt] =
          for {
            a <- intConstantEval(l)
            b <- intConstantEval(r)
          } yield f(a)(b)

        for {
          l <- asExpr(e.left)
          r <- asExpr(e.right)
          res <- e match {
            case _: PAdd => aux(l, r)(x => y => x + y)
            case _: PSub => aux(l, r)(x => y => x - y)
            case _: PMul => aux(l, r)(x => y => x * y)
            case _: PMod => aux(l, r)(x => y => x % y)
            case _: PDiv => aux(l, r)(x => y => x / y)
            case _: PShiftLeft =>
              aux(l, r){
                x => y =>
                  // The type system ensures that y is convertible to int
                  violation(y <= Int.MaxValue, s"right-hand operand bigger than expected")
                  x << y.toInt
              }
            case _: PShiftRight => exprType(l) match {
              case IntT(t) => t match {
                case UnboundedInteger | _: Signed =>
                  aux(l, r){
                    x => y =>
                      // The type system ensures that y is convertible to int
                      violation(y <= Int.MaxValue, s"right-hand operand bigger than expected")
                      x >> y.toInt
                  }
                case _: Unsigned =>
                  aux(l, r){
                    x => y =>
                      // The type system ensures that x is convertible to long and y is convertible to int
                      violation(x <= Long.MaxValue, s"left-hand operand bigger than expected")
                      violation(y <= Int.MaxValue, s"right-hand operand bigger than expected")
                      BigInt(x.toLong >>> y.toInt) // >>> is not implemented for BigInt
                  }
              }
              case _ => None
            }
            case _: PBitAnd => aux(l, r)(x => y => x & y)
            case _: PBitOr => aux(l, r)(x => y => x | y)
            case _: PBitXor => aux(l, r)(x => y => x ^ y)
            case _: PBitClear => aux(l, r)(x => y => x &~ y)

            case _ => None
          }
        } yield res

      case PNamedOperand(id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, context) => context.intConstantEvaluation(exp)
        case _ => None
      }
      case PDot(_, id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, context) => context.intConstantEvaluation(exp)
        case _ => None
      }

      case p: PIota =>
        // obtains the intended value for iota from the context
        val res = for {
          constBlock <- enclosingPConstBlock(p)
          constClause <- enclosingPConstDecl(p)
          iota = constBlock.specs.indexOf(constClause)
        } yield BigInt(iota)
        violation(res.nonEmpty, "iota expression could not be found in a constant declaration")
        res

      case _ => None
    }

  def evalFloatOrFail(exp: PExpression): BigDecimal = {
    floatConstantEval(exp).getOrElse(violation(s"expected constant float expression, but got $exp"))
  }

  /** Evaluates constant floating-point expressions. Go requires constant arithmetic to be carried
    * out with a mantissa of at least 256 bits; here, exact arithmetic is used except for division,
    * which is evaluated with 100 decimal digits (roughly 332 bits, exceeding Go's requirement).
    * The result is rounded to the target float type when the constant is used (see the desugarer
    * and the float32/float64 conversion cases below).
    */
  lazy val floatConstantEval: PExpression => Option[BigDecimal] =
    attr[PExpression, Option[BigDecimal]] {
      case PFloatLit(lit) => Some(lit)
      case PIntLit(lit, _) => Some(BigDecimal(lit))
      case inv: PInvoke => resolve(inv) match {
        case Some(conv: ap.Conversion) => underlyingTypeP(conv.typ) match {
          // conversions of constants round to the precision of the target type (Go spec)
          case Some(_: PFloat32) => floatConstantEval(conv.arg)
            .map(_.toFloat).filter(f => !f.isInfinite).map(f => BigDecimal(f.toDouble))
          case Some(_: PFloat64) => floatConstantEval(conv.arg)
            .map(_.toDouble).filter(d => !d.isInfinite).map(BigDecimal(_))
          case _ => None
        }
        case _ => None
      }
      case e: PBinaryExp[_,_] =>
        def aux(l: PExpression, r: PExpression)(f: (BigDecimal, BigDecimal) => Option[BigDecimal]): Option[BigDecimal] =
          for {
            a <- floatConstantEval(l)
            b <- floatConstantEval(r)
            res <- f(a, b)
          } yield res

        for {
          l <- asExpr(e.left)
          r <- asExpr(e.right)
          res <- e match {
            case _: PAdd => aux(l, r)((a, b) => Some(a + b))
            case _: PSub => aux(l, r)((a, b) => Some(a - b))
            case _: PMul => aux(l, r)((a, b) => Some(a * b))
            case _: PDiv => aux(l, r)((a, b) => if (b == 0) None else Some(a(new MathContext(100)) / b))
            case _ => None
          }
        } yield res

      case PNamedOperand(id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, context) => context.floatConstantEvaluation(exp)
        case _ => None
      }
      case PDot(_, id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, context) => context.floatConstantEvaluation(exp)
        case _ => None
      }

      case _ => None
    }

  lazy val permConstantEval: PExpression => Option[(BigInt, BigInt)] = {
    attr[PExpression, Option[(BigInt, BigInt)]] {
      case PDiv(a, b) =>
        // Here, we support the cases where 'a' and 'b' are int. In the future, this can be expanded to also
        // support 'a' of type perm.
        for {
          dividend <- intConstantEval(a)
          divisor  <- intConstantEval(b)
        } yield (dividend, divisor)

      case inv: PInvoke => resolve(inv) match {
        case Some(ap.Conversion(t, e)) if underlyingTypeP(t).contains(PPermissionType()) => permConstantEval(e)
        case _ => None
      }

      case expr => violation(s"Unexpected constant perm expression: $expr.")
    }
  }

  lazy val stringConstantEval: PExpression => Option[GoString] = {
    attr[PExpression, Option[GoString]] {
      case PStringLit(lit) => Some(lit)

      case PAdd(l,r) => for {
        lStr <- stringConstantEval(l)
        rStr <- stringConstantEval(r)
      } yield lStr ++ rStr

      case _ => None
    }
  }
}
