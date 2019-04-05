package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.SingleConstant
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait ConstantEvaluation { this: TypeInfoImpl =>

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
      case PNamedOperand(id) => regular(id) match {
        case SingleConstant(exp, _) => intConstantEval(exp)
        case _ => None
      }

      case _ => None
    }
}
