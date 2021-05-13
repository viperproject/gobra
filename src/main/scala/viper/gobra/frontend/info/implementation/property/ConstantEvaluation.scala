// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.SingleConstant
import viper.gobra.frontend.info.base.Type.{BooleanT, IntT}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.TypeBounds.{BoundedIntegerKind, DefaultInt, DefaultUInt, IntWith64Bit, Signed, SignedInteger16, SignedInteger32, SignedInteger64, SignedInteger8, UIntWith64Bit, UnboundedInteger, Unsigned, UnsignedInteger16, UnsignedInteger32, UnsignedInteger64, UnsignedInteger8}
import viper.gobra.util.Violation.violation

trait ConstantEvaluation { this: TypeInfoImpl =>

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
        case Some(conv: ap.Conversion) if underlyingTypeP(conv.typ).isInstanceOf[Some[PIntegerType]] =>
          intConstantEval(conv.arg)
        case _ => None
      }
      case e: PBitwiseNegation => ???
      case e: PBinaryExp[_,_] =>
        def aux(l: PExpression, r: PExpression)(f: BigInt => BigInt => BigInt): Option[BigInt] =
          (intConstantEval(l), intConstantEval(r)) match {
            case (Some(a), Some(b)) => Some(f(a)(b))
            case _ => None
          }

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
              for {
                constL <- intConstantEval(l)
                constR <- intConstantEval(r)
                v = underlyingType(typ(l)) match {
                  case IntT(t: Signed) => t match {
                    case SignedInteger8 =>
                      constL.byteValue << constR.intValue
                    case SignedInteger16 =>
                      constL.shortValue << constR.intValue
                    case SignedInteger32 | DefaultInt =>
                      constL.intValue << constR.intValue
                    case SignedInteger64 | IntWith64Bit =>
                      constL.longValue << constR.intValue
                  }
                  case IntT(t: Unsigned) => t match {
                    case UnsignedInteger8 =>
                      constL.byteValue << constR.intValue
                    case UnsignedInteger16 =>
                      constL.shortValue << constR.intValue
                    case UnsignedInteger32 | DefaultUInt =>
                      constL.intValue << constR.intValue
                    case UnsignedInteger64 | UIntWith64Bit =>
                      constL.longValue << constR.intValue
                  }
                  case IntT(UnboundedInteger) =>
                    ??? // TODO: should be violation
                    /*
                    if (config.int32bit) {
                      constL.intValue << constR.longValue
                    } else {
                      constL.longValue << constR.longValue
                    }
                     */
                }
              } yield BigInt(v)

            case _: PShiftRight =>
              for {
                constL <- intConstantEval(l)
                constR <- intConstantEval(r)
                v = underlyingType(typ(l)) match {
                  case IntT(t: Signed) => t match {
                    case SignedInteger8 =>
                      constL.byteValue >> constR.intValue
                    case SignedInteger16 =>
                      constL.shortValue >> constR.intValue
                    case SignedInteger32 | DefaultInt =>
                      constL.intValue >> constR.intValue
                    case SignedInteger64 | IntWith64Bit =>
                      constL.longValue >> constR.intValue
                  }
                  case IntT(t: Unsigned) => t match {
                    case UnsignedInteger8 =>
                      constL.byteValue >>> constR.intValue
                    case UnsignedInteger16 =>
                      constL.shortValue >>> constR.intValue
                    case UnsignedInteger32 | DefaultUInt =>
                      constL.intValue >>> constR.intValue
                    case UnsignedInteger64 | UIntWith64Bit =>
                      constL.longValue >>> constR.intValue
                  }
                  case _ => ??? // TODO: should be violation
                    /*
                    if (config.int32bit) {
                      constL.intValue >> constR.longValue
                    } else {
                      constL.longValue >> constR.longValue
                    }
                     */
                }
              } yield BigInt(v)
              // TODO: avoid repetition
            case exp: PBitwiseAnd =>
              for {
                l <- intConstantEval(exp.left)
                r <- intConstantEval(exp.right)

              } yield BigInt(typeMerge(typ(exp.left), typ(exp.right)) match {
                case Some(IntT(t: Signed)) => t match {
                  case SignedInteger8 =>
                    l.byteValue & r.byteValue
                  case SignedInteger16 =>
                    l.shortValue & r.shortValue
                  case SignedInteger32 | DefaultInt =>
                    l.intValue & r.intValue
                  case SignedInteger64 | IntWith64Bit =>
                    l.longValue & r.longValue
                }
                case Some(IntT(t: Unsigned)) => t match {
                  case UnsignedInteger8 =>
                    l.byteValue & r.byteValue
                  case UnsignedInteger16 =>
                    l.shortValue & r.shortValue
                  case UnsignedInteger32 | DefaultUInt =>
                    l.intValue & r.intValue
                  case UnsignedInteger64 | UIntWith64Bit =>
                    l.longValue & r.longValue
                }
              })

            case exp: PBitwiseOr =>
              for {
                l <- intConstantEval(exp.left)
                r <- intConstantEval(exp.right)

              } yield BigInt(typeMerge(typ(exp.left), typ(exp.right)) match {
                case Some(IntT(t: BoundedIntegerKind)) => t match {
                  case SignedInteger8 | UnsignedInteger8 =>
                    l.byteValue | r.byteValue
                  case SignedInteger16 | UnsignedInteger16 =>
                    l.shortValue | r.shortValue
                  case SignedInteger32 | DefaultInt | UnsignedInteger32 | DefaultUInt =>
                    l.intValue | r.intValue
                  case SignedInteger64 | IntWith64Bit | UnsignedInteger64 | UIntWith64Bit =>
                    l.longValue | r.longValue
                }
              })

            case exp: PBitwiseXor =>
              for {
                l <- intConstantEval(exp.left)
                r <- intConstantEval(exp.right)

              } yield BigInt(typeMerge(typ(exp.left), typ(exp.right)) match {
                case Some(IntT(t: Signed)) => t match {
                  case SignedInteger8 =>
                    l.byteValue ^ r.byteValue
                  case SignedInteger16 =>
                    l.shortValue ^ r.shortValue
                  case SignedInteger32 | DefaultInt =>
                    l.intValue ^ r.intValue
                  case SignedInteger64 | IntWith64Bit =>
                    l.longValue ^ r.longValue
                }
                case Some(IntT(t: Unsigned)) => t match {
                  case UnsignedInteger8 =>
                    l.byteValue ^ r.byteValue
                  case UnsignedInteger16 =>
                    l.shortValue ^ r.shortValue
                  case UnsignedInteger32 | DefaultUInt =>
                    l.intValue ^ r.intValue
                  case UnsignedInteger64 | UIntWith64Bit =>
                    l.longValue ^ r.longValue
                }
              })
            case exp: PBitClear =>
              for {
                l <- intConstantEval(exp.left)
                r <- intConstantEval(exp.right)

              } yield BigInt(typeMerge(typ(exp.left), typ(exp.right)) match {
                case Some(IntT(t: Signed)) => t match {
                  case SignedInteger8 =>
                    l.byteValue & ~r.byteValue
                  case SignedInteger16 =>
                    l.shortValue & ~r.shortValue
                  case SignedInteger32 | DefaultInt =>
                    l.intValue & ~r.intValue
                  case SignedInteger64 | IntWith64Bit =>
                    l.longValue & ~r.longValue
                }
                case Some(IntT(t: Unsigned)) => t match {
                  case UnsignedInteger8 =>
                    l.byteValue & ~r.byteValue
                  case UnsignedInteger16 =>
                    l.shortValue & ~r.shortValue
                  case UnsignedInteger32 | DefaultUInt =>
                    l.intValue & ~r.intValue
                  case UnsignedInteger64 | UIntWith64Bit =>
                    l.longValue & ~r.longValue
                }
              })

            case _ => None
          }
        } yield res

      case PNamedOperand(id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, context) => context.intConstantEvaluation(exp)
        case _ => None
      }
      case PDot(_, id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, _) => intConstantEval(exp)
        case _ => None
      }

      case _ => None
    }

  //TODO: rename
  private def binaryBitwiseOp(byteOp: (Byte, Byte) => Byte,
                              shortOp: (Short, Short) => Short,
                              intOp: (Int, Int) => Int,
                              longOp: (Long, Long) => Long
                             )(left: PExpression, right: PExpression): Option[BigInt] = {
    val v: Option[Option[BigInt]] = for {
      l <- intConstantEval(left)
      r <- intConstantEval(right)
    } yield typeMerge(typ(left), typ(right)) match {
      case Some(IntT(t: BoundedIntegerKind)) => Some(t match {
        case SignedInteger8 | UnsignedInteger8 => BigInt(byteOp(l.byteValue, r.byteValue))
        case SignedInteger16 | UnsignedInteger16 => BigInt(shortOp(l.shortValue, r.shortValue))
        case SignedInteger32 | DefaultInt | UnsignedInteger32 | DefaultUInt => BigInt(intOp(l.intValue, r.intValue))
        case SignedInteger64 | IntWith64Bit | UnsignedInteger64 | UIntWith64Bit => BigInt(longOp(l.longValue, r.longValue))
      })
      case Some(IntT(UnboundedInteger)) =>
        // this case should be unreacable: it does not make sense to apply bitwise operators to unbounded variables.
        // the type system should infer a suitable (bounded) type when applying a bitwise operator to untyped literals
        violation("This case should be unreachable")// TODO: put more info in the error
      case _ => None
    }
    v.get // TODO: simplify
  }

  lazy val stringConstantEval: PExpression => Option[String] = {
    attr[PExpression, Option[String]] {
      case PStringLit(lit) => Some(lit)

      case PAdd(l,r) => for {
        lStr <- stringConstantEval(l)
        rStr <- stringConstantEval(r)
      } yield lStr + rStr

      case _ => None
    }
  }
}
