package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{Constant, Embbed, Field, Function, MethodImpl, Variable}
import viper.gobra.frontend.info.base.Type.{AssertionT, BooleanT, GhostCollectionType, GhostUnorderedCollectionType, IntT, SequenceT, SetT, MultisetT, Type}
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation.violation

trait GhostExprTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostExpr(expr: PGhostExpression): Messages = expr match {

    case POld(op) => isExpr(op).out ++ isPureExpr(op)

    case PConditional(cond, thn, els) =>
      // check that cond is of type bool:
      isExpr(cond).out ++ isExpr(thn).out ++ isExpr(els).out ++
        assignableTo.errors(exprType(cond), BooleanT)(expr) ++
        // check that thn and els have a common type
        mergeableTypes.errors(exprType(thn), exprType(els))(expr)

    case n: PImplication =>
      isExpr(n.left).out ++ isExpr(n.right).out ++
      // check that left side is a boolean expression
        assignableTo.errors(exprType(n.left), BooleanT)(expr) ++
      // check that right side is either boolean or an assertion
        assignableTo.errors(exprType(n.right), AssertionT)(expr)

    case n: PAccess => resolve(n.exp) match {
      case Some(p: ap.Deref) => noMessages
      case Some(p: ap.FieldSelection) => noMessages
      case Some(p: ap.PredicateCall) => noMessages
      case _ => message(n, s"expected reference, dereference, or field selection, but got ${n.exp}")
    }

    case n: PPredicateAccess => resolve(n.pred) match {
      case Some(p: ap.PredicateCall) => noMessages
      case _ => message(n, s"expected reference, dereference, or field selection, but got ${n.pred}")
    }

    case expr : PGhostCollectionExp => expr match {
      case PIn(left, right) => isExpr(left).out ++ isExpr(right).out ++ {
        exprType(right) match {
          case t : GhostCollectionType => comparableTypes.errors(exprType(left), t.elem)(expr)
          case t => message(right, s"expected a collection, but got $t")
        }
      }

      case PSize(op) => isExpr(op).out ++ {
        val t = exprType(op)
        message(op,s"expected a collection, but got $t", !t.isInstanceOf[GhostCollectionType])
      }

      case PMultiplicity(left, right) => isExpr(left).out ++ isExpr(right).out ++ {
        (exprType(left), exprType(right)) match {
          case (t1, SequenceT(t2)) => comparableTypes.errors(t1, t2)(expr)
          case (_, t) => message(right, s"expected a sequence, but got $t")
        }
      }

      case expr : PSequenceExp => expr match {
        case expr : PSequenceLiteral => wellDefGhostCollectionLiteral(expr)
        case PRangeSequence(low, high) => isExpr(low).out ++ isExpr(high).out ++ {
          val lowT = exprType(low)
          val highT = exprType(high)
          message(low, s"expected an integer, but got $lowT", lowT != IntT) ++
            message(high, s"expected an integer, but got $highT", highT != IntT)
        }
        case PSequenceAppend(left, right) => isExpr(left).out ++ isExpr(right).out ++ {
          val t1 = exprType(left)
          val t2 = exprType(right)
          message(left, s"expected a sequence, but got $t1", !t1.isInstanceOf[SequenceT]) ++
            message(right, s"expected a sequence, but got $t2", !t2.isInstanceOf[SequenceT]) ++
            comparableTypes.errors(t1, t2)(expr)
        }
        case PSequenceUpdate(seq, clauses) => isExpr(seq).out ++ (exprType(seq) match {
          case SequenceT(t) => clauses.flatMap(wellDefSeqUpdClause(t, _))
          case t => message(seq, s"expected a sequence, but got $t")
        })
      }

      case expr : PUnorderedGhostCollectionExp => expr match {
        case expr: PBinaryGhostExp => isExpr(expr.left).out ++ isExpr(expr.right).out ++ {
          val t1 = exprType(expr.left)
          val t2 = exprType(expr.right)
          message(expr.left, s"expected an unordered collection, but got $t1", !t1.isInstanceOf[GhostUnorderedCollectionType]) ++
            message(expr.right, s"expected an unordered collection, but got $t2", !t2.isInstanceOf[GhostUnorderedCollectionType]) ++
            comparableTypes.errors(t1, t2)(expr)
        }
        case expr : PSetExp => expr match {
          case expr : PSetLiteral => wellDefGhostCollectionLiteral(expr)
          case PSetConversion(op) => exprType(op) match {
            case SequenceT(_) | SetT(_) => isExpr(op).out
            case t => message(op, s"expected a sequence or a set, but got $t")
          }
        }
        case expr : PMultisetExp => expr match {
          case expr : PMultisetLiteral => wellDefGhostCollectionLiteral(expr)
          case PMultisetConversion(op) => exprType(op) match {
            case SequenceT(_) | MultisetT(_) => isExpr(op).out
            case t => message(op, s"expected a sequence or a multiset, but got $t")
          }
        }
      }
    }
  }

  private[typing] def wellDefGhostCollectionLiteral(lit : PGhostCollectionLiteral) : Messages = {
    val typ = typeType(lit.typ)
    lit.exprs.flatMap(e => assignableTo.errors(exprType(e), typ)(e) ++ isExpr(e).out)
  }

  private[typing] def wellDefSeqUpdClause(seqTyp : Type, clause : PSequenceUpdateClause) : Messages = exprType(clause.left) match {
    case IntT => isExpr(clause.left).out ++ isExpr(clause.right).out ++
      assignableTo.errors(exprType(clause.right), seqTyp)(clause.right)
    case t => message(clause.left, s"expected an integer type but got $t")
  }

  private[typing] def ghostExprType(expr: PGhostExpression): Type = expr match {
    case POld(op) => exprType(op)

    case PConditional(_, thn, els) =>
      typeMerge(exprType(thn), exprType(els)).getOrElse(violation("no common supertype found"))

    case n: PImplication => exprType(n.right) // implication is assertion or boolean iff its right side is

    case _: PAccess | _: PPredicateAccess => AssertionT

    case expr : PGhostCollectionExp => expr match {
      case PSize(_) => IntT
      case PMultiplicity(_, _) => IntT
      case PIn(_, right) => exprType(right) match {
        case MultisetT(_) => IntT
        case _ => BooleanT
      }
      case expr : PSequenceExp => expr match {
        case PSequenceLiteral(typ, _) => SequenceT(typeType(typ))
        case PRangeSequence(_, _) => SequenceT(IntT)
        case PSequenceAppend(left, _) => exprType(left)
        case PSequenceUpdate(seq, _) => exprType(seq)
      }
      case expr : PUnorderedGhostCollectionExp => expr match {
        case expr : PBinaryGhostExp => expr match {
          case PSubset(_, _) => BooleanT
          case _ => exprType(expr.left)
        }
        case expr : PSetExp => expr match {
          case PSetLiteral(typ, _) => SetT(typeType(typ))
          case PSetConversion(op) => exprType(op) match {
            case t : GhostCollectionType => SetT(t.elem)
            case t => violation(s"expected a ghost collection type, but got $t")
          }
        }
        case expr : PMultisetExp => expr match {
          case PMultisetLiteral(typ, _) => MultisetT(typeType(typ))
          case PMultisetConversion(op) => exprType(op) match {
            case t : GhostCollectionType => MultisetT(t.elem)
            case t => violation(s"expected a ghost collection type, but got $t")
          }
        }
      }
    }
  }

  private[typing] def isPureExpr(expr: PExpression): Messages = {
    message(expr, s"expected pure expression but got $expr", !isPureExprAttr(expr))
  }

  private def isPureId(id: PIdnNode): Boolean = entity(id) match {
    case _: Constant => true
    case _: Variable => true
    case _: Field => true
    case _: Embbed => true
    case Function(decl, _) => decl.spec.isPure
    case MethodImpl(decl, _) => decl.spec.isPure
    case _ => false
  }

  private lazy val isPureExprAttr: PExpression => Boolean =
    attr[PExpression, Boolean] {
      case n@ PNamedOperand(id) => isPureId(id)

      case _: PBoolLit | _: PIntLit | _: PNilLit => true

      case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
        case (Right(_), Some(p: ap.Conversion)) => false // Might change at some point
        case (Left(callee), Some(p: ap.FunctionCall)) => isPureExprAttr(callee) && p.args.forall(isPureExprAttr)
        case _ => false
      }

      case n: PDot => exprOrType(n.base) match {
        case Left(e) => isPureExprAttr(e) && isPureId(n.id)
        case Right(_) => isPureId(n.id) // Maybe replace with a violation
      }

      case n@PReference(e) => isPureExprAttr(e)
      case n: PDeref =>
        resolve(n) match {
          case Some(p: ap.Deref) => isPureExprAttr(p.base)
          case _ => true
        }

      case PNegation(e) => isPureExprAttr(e)

      case x: PBinaryExp => isPureExprAttr(x.left) && isPureExprAttr(x.right) && (x match {
          case _: PEquals | _: PUnequals |
               _: PAnd | _: POr |
               _: PLess | _: PAtMost | _: PGreater | _: PAtLeast |
               _: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv => true
          case _ => false
        })

      case n: PUnfolding => true

      case _: POld => true

      case PConditional(cond, thn, els) => Seq(cond, thn, els).forall(isPureExprAttr)

      case PImplication(left, right) => Seq(left, right).forall(isPureExprAttr)

      case expr : PGhostCollectionExp => expr match {
        case n : PBinaryGhostExp => isPureExprAttr(n.left) && isPureExprAttr(n.right)
        case n : PGhostCollectionLiteral => n.exprs.forall(isPureExprAttr)
        case PSetConversion(op) => isPureExprAttr(op)
        case PMultisetConversion(op) => isPureExprAttr(op)
        case PSize(op) => isPureExprAttr(op)
        case PRangeSequence(low, high) => isPureExprAttr(low) && isPureExprAttr(high)
        case PSequenceUpdate(seq, clauses) => isPureExprAttr(seq) && clauses.forall(isPureSeqUpdClause)
      }

      case _: PAccess | _: PPredicateAccess => false

      case n@PCompositeLit(t, lit) => true

      // Might change soon:
      case n@PIndexedExp(base, index) => false

      // Might change as some point
      case _: PFunctionLit => false
      case n@PSliceExp(base, low, high, cap) => false

      // Others
      case n@PTypeAssertion(base, typ) => false
      case n@PReceive(e) => false
    }

  private def isPureSeqUpdClause(clause : PSequenceUpdateClause) : Boolean =
    isPureExprAttr(clause.left) && isPureExprAttr(clause.right)
}
