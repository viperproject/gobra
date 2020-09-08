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

trait ConstantEvaluation { this: TypeInfoImpl =>

  lazy val boolConstantEval: PExpression => Option[Boolean] =
    attr[PExpression, Option[Boolean]] {
      case PBoolLit(lit) => Some(lit)
      case e: PUnaryExp => e match {
        case _: PNegation => boolConstantEval(e.operand).map(!_)
        case _ => None
      }
      case e: PBinaryExp =>
        def auxBool[T](f: Boolean => Boolean => Boolean): Option[Boolean] =
          (boolConstantEval(e.left), boolConstantEval(e.right)) match {
            case (Some(a), Some(b)) => Some(f(a)(b))
            case _ => None
          }
        def auxInt[T](f: BigInt => BigInt => Boolean): Option[Boolean] =
          (intConstantEval(e.left), intConstantEval(e.right)) match {
            case (Some(a), Some(b)) => Some(f(a)(b))
            case _ => None
          }

        e match {
          case _: PEquals if typ(e.left) == BooleanT => auxBool(x => y => x == y)
          case _: PEquals if typ(e.left) == IntT => auxInt(x => y => x == y)
          case _: PUnequals if typ(e.left) == BooleanT => auxBool(x => y => x != y)
          case _: PUnequals if typ(e.left) == IntT => auxInt(x => y => x != y)
          case _: PAnd if typ(e.left) == BooleanT => auxBool(x => y => x && y)
          case _: POr if typ(e.left) == BooleanT => auxBool(x => y => x || y)
          case _: PLess if typ(e.left) == IntT => auxInt(x => y => x < y)
          case _: PAtMost if typ(e.left) == IntT => auxInt(x => y => x <= y)
          case _: PGreater if typ(e.left) == IntT => auxInt(x => y => x > y)
          case _: PAtLeast if typ(e.left) == IntT => auxInt(x => y => x >= y)
          case _ => None
        }
      case PNamedOperand(id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, _) => boolConstantEval(exp)
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
      case PIntLit(lit) => Some(lit)
      case e: PBinaryExp =>
        def aux(f: BigInt => BigInt => BigInt): Option[BigInt] =
          (intConstantEval(e.left), intConstantEval(e.right)) match {
            case (Some(a), Some(b)) => Some(f(a)(b))
            case _ => None
          }

        e match {
          case _: PAdd => aux(x => y => x + y)
          case _: PSub => aux(x => y => x - y)
          case _: PMul => aux(x => y => x * y)
          case _: PMod => aux(x => y => x % y)
          case _: PDiv => aux(x => y => x / y)
          case _ => None
        }
      case PNamedOperand(id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, _) => intConstantEval(exp)
        case _ => None
      }
      case PDot(_, id) => entity(id) match {
        case SingleConstant(_, _, exp, _, _, _) => intConstantEval(exp)
        case _ => None
      }

      case _ => None
    }
}
