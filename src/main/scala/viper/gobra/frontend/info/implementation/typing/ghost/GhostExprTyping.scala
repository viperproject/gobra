// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing.ghost

import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.{AdtMember, BuiltInFPredicate, BuiltInFunction, BuiltInMPredicate, BuiltInMethod, Closure, Constant, DomainFunction, Embbed, Field, Function, Label, Method, Predicate, Variable, WandLhsLabel}
import viper.gobra.frontend.info.base.Type.{ArrayT, AssertionT, BooleanT, GhostCollectionType, GhostUnorderedCollectionType, IntT, MultisetT, OptionT, PermissionT, SequenceT, SetT, Single, SortT, Type}
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.frontend.{Config, Hyper}
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.typing.BaseTyping
import viper.gobra.util.Violation.violation

import scala.annotation.unused

trait GhostExprTyping extends BaseTyping { this: TypeInfoImpl =>

  private[typing] def wellDefGhostExpr(expr: PGhostExpression): Messages = expr match {

    case POld(op) => isExpr(op).out ++ isPureExpr(op)

    case PLabeledOld(l, op) =>
      isExpr(op).out ++ isPureExpr(op) ++ (
          label(l) match {
            case _: Label => noMessages
            case WandLhsLabel => noMessages
            case _ => error(l, s"$l is not a label in scope")
          }
        )

    case n: PBefore =>
      isExpr(n).out ++ isPureExpr(n) ++
        error(n, "before expressions can only be used inside outline statements.", tryEnclosingOutline(n).isEmpty)

    case n@PConditional(cond, thn, els) =>
      val mayInit = isEnclosingMayInit(n)
      // check whether all operands are actually expressions indeed
      isExpr(cond).out ++ isExpr(thn).out ++ isExpr(els).out ++
        // check that `cond` is of type bool
        assignableTo.errors(exprType(cond), BooleanT, mayInit)(expr) ++
        // check that `thn` and `els` have a common type
        mergeableTypes.errors(exprType(thn), exprType(els))(expr) ++
        // check that all subexpressions are pure.
        isPureExpr(cond) ++ isWeaklyPureExpr(thn) ++ isWeaklyPureExpr(els)

    case n@PForall(vars, triggers, body) =>
      // check whether all triggers are valid and consistent
      validTriggers(vars, triggers) ++
      // check that the quantifier `body` is either Boolean or an assertion
      assignableToSpec(body) ++
      // check that the user provided triggers when running with --requireTriggers
      error(n, "found a quantifier without triggers.", config.requireTriggers && triggers.isEmpty)

    case n@PExists(vars, triggers, body) =>
      val mayInit = isEnclosingMayInit(n)
      // check whether all triggers are valid and consistent
      validTriggers(vars, triggers) ++
      // check that the quantifier `body` is Boolean
      assignableToSpec(body) ++ assignableTo.errors(exprType(body), BooleanT, mayInit)(expr) ++
      // check that the user provided triggers when running with --requireTriggers
      error(n, "found a quantifier without triggers.", config.requireTriggers && triggers.isEmpty)

    case n: PImplication =>
      val mayInit = isEnclosingMayInit(n)
      isExpr(n.left).out ++ isExpr(n.right).out ++
        // check whether the left operand is a Boolean expression
        assignableTo.errors(exprType(n.left), BooleanT, mayInit)(expr) ++
        // check whether the right operand is either Boolean or an assertion
        assignableToSpec(n.right)

    case n: PMagicWand =>
      isExpr(n.left).out ++ isExpr(n.right).out ++
        // check whether the left operand is a Boolean or an assertion
        assignableToSpec(n.left) ++
        // check whether the right operand is either Boolean or an assertion
        assignableToSpec(n.right)

    case n: PClosureImplements => isPureExpr(n.closure) ++ wellDefIfClosureMatchesSpec(n.closure, n.spec)

    case n: PLet => isExpr(n.op).out ++ isWeaklyPureExpr(n.op) ++
      n.ass.right.foldLeft(noMessages)((a, b) => a ++ isPureExpr(b))

    case n: PAccess =>
      val mayInit = isEnclosingMayInit(n)
      val permWellDef = error(n.perm, s"expected perm or integer division expression, but got ${n.perm}",
        !assignableTo(typ(n.perm), PermissionT, mayInit))
      val expWellDef = resolve(n.exp) match {
        case Some(_: ap.PredicateCall) => noMessages
        case Some(_: ap.PredExprInstance) => noMessages
        case _ =>
          val argT = exprType(n.exp)
          // Not all pointer types are supported currently. Later, we can just check isPointerType.
          underlyingType(argT) match {
            case Single(Type.NilType | _: Type.PointerT | _: Type.SliceT | _: Type.MapT) => noMessages
            case _ => error(n, s"expected expression with pointer or predicate type, but got $argT")
          }
      }
      permWellDef ++ expWellDef

    case n: PPredicateAccess =>
      val predWellDef = resolve(n.pred) match {
        case Some(_: ap.PredicateCall) => noMessages
        case Some(_: ap.PredExprInstance) => noMessages
        case _ => error(n, s"expected reference, dereference, field selection, or predicate expression instance, but got ${n.pred}")
      }
      predWellDef ++ error(n, "Cannot reveal a predicate access.", n.pred.reveal)

    case PTypeOf(e) =>
      val isExp = isExpr(e).out
      if (isExp.isEmpty) {
        val typ = underlyingType(exprType(e))
        error(e, s"typeOf expects an argument of an interface type, but got $e instead.", !typ.isInstanceOf[InterfaceT])
      } else
        isExp
    case PTypeExpr(t) => isType(t).out
    case n@ PIsComparable(e) => typOfExprOrType(e) match {
      case t if isInterfaceType(t) => noMessages
      case Type.SortT =>  noMessages
      case t =>  error(n, s"expected interface or type, but got an expression of type $t")
    }

    case PLow(e) => isExpr(e).out
    case _: PLowContext => noMessages
    case n: PRel =>
      error(n, s"rel expressions are currently only supported if hyperMode '${Hyper.EnabledExtended.name}' and the '${Config.enableExperimentalHyperFeaturesOptionName}' flag are used.", config.hyperModeOrDefault != Hyper.EnabledExtended || !config.enableExperimentalHyperFeatures) ++
      error(n, s"currently, only 0 and 1 are valid execution identifiers, but ${n.lit.lit} was provided. ", n.lit.lit < 0 || 1 < n.lit.lit)

    case n: PGhostEquals =>
      val lType = typ(n.left)
      val rType = typ(n.right)
      ghostComparableTypes.errors(lType, rType)(n)

    case n: PGhostUnequals =>
      val lType = typ(n.left)
      val rType = typ(n.right)
      ghostComparableTypes.errors(lType, rType)(n)

    case POptionNone(t) => isType(t).out
    case POptionSome(e) => isExpr(e).out
    case POptionGet(e) => isExpr(e).out ++ {
      val t = exprType(e)
      error(e, s"expected an option type, but got $t", !t.isInstanceOf[OptionT])
    }

    case m@PMatchExp(exp, clauses) =>
      val mayInit = isEnclosingMayInit(m)
      val sameTypeE = allMergeableTypes.errors(clauses map { c => exprType(c.exp) })(exp)
      val patternE = m.caseClauses.flatMap(c => c.pattern match {
        case p: PMatchAdt => assignableTo.errors(miscType(p), exprType(exp), mayInit)(c)
        case _ => comparableTypes.errors((miscType(c.pattern), exprType(exp)))(c)
      })
      val pureExpE = error(exp, "Expression has to be pure", !isPure(exp)(strong = false))
      val pureClauses = clauses flatMap { c => error(c.exp, "Expressions of cases have to be pure", !isPure(c.exp)(strong = false)) }
      val moreThanOneDfltE = error(m, "Match Expression can only have one default case", m.defaultClauses.length > 1)
      sameTypeE ++ patternE ++ error(clauses, "Cases cannot be empty", clauses.isEmpty) ++ pureExpE ++ moreThanOneDfltE ++ pureClauses

    case expr : PGhostCollectionExp => expr match {
      case PElem(left, right) => isExpr(left).out ++ isExpr(right).out ++ {
        underlyingType(exprType(right)) match {
          case t : GhostCollectionType => ghostComparableTypes.errors(exprType(left), t.elem)(expr)
          case t : MapT => ghostComparableTypes.errors(exprType(left), t.key)(expr)
          case _ : AdtT => noMessages
          case t => error(right, s"expected a ghost collection, but got $t")
        }
      }

      case PMultiplicity(left, right) => isExpr(left).out ++ isExpr(right).out ++ {
        (exprType(left), underlyingType(exprType(right))) match {
          case (t1, t2 : GhostCollectionType) => comparableTypes.errors(t1, t2.elem)(expr)
          case (_, t) => error(right, s"expected a ghost collection, but got $t")
        }
      }

      case n@PGhostCollectionUpdate(seq, clauses) =>
        val mayInit = isEnclosingMayInit(n)
        isExpr(seq).out ++ (underlyingType(exprType(seq)) match {
          case SequenceT(t) => clauses.flatMap(wellDefSeqUpdClause(t, _, mayInit))
          case MathMapT(k, v) => clauses.flatMap(wellDefMapUpdClause(k, v, _, mayInit))
          case t => error(seq, s"expected a sequence or mathematical map, but got $t")
        })

      case expr : PSequenceExp => expr match {
        case PRangeSequence(low, high) => isExpr(low).out ++ isExpr(high).out ++ {
          val lowT = exprType(low)
          val highT = exprType(high)
          error(low, s"expected an integer, but got $lowT", !lowT.isInstanceOf[IntT]) ++
            error(high, s"expected an integer, but got $highT", !highT.isInstanceOf[IntT])
        }
        case PSequenceAppend(left, right) => isExpr(left).out ++ isExpr(right).out ++ {
          val t1 = exprType(left)
          val t2 = exprType(right)
          val ut1 = underlyingType(t1)
          val ut2 = underlyingType(t2)
          error(left, s"expected a sequence, but got $t1", !ut1.isInstanceOf[SequenceT]) ++
            error(right, s"expected a sequence, but got $t2", !ut2.isInstanceOf[SequenceT]) ++
            mergeableTypes.errors(t1, t2)(expr)
        }
        case PSequenceConversion(op) => exprType(op) match {
          case _: SequenceT => isExpr(op).out
          case _: ArrayT => isExpr(op).out ++ error(op, s"exclusive array expected, but shared array $op found", addressable(op))
          case _: OptionT => isExpr(op).out
          case t => error(op, s"expected an array or sequence type, but got $t")
        }
      }

      case expr : PUnorderedGhostCollectionExp => expr match {
        case expr: PBinaryGhostExp => isExpr(expr.left).out ++ isExpr(expr.right).out ++ {
          val t1 = exprType(expr.left)
          val t2 = exprType(expr.right)
          val ut1 = underlyingType(t1)
          val ut2 = underlyingType(t2)
          error(expr.left, s"expected an unordered collection, but got $t1", !ut1.isInstanceOf[GhostUnorderedCollectionType]) ++
            error(expr.right, s"expected an unordered collection, but got $t2", !ut2.isInstanceOf[GhostUnorderedCollectionType]) ++
            mergeableTypes.errors(t1, t2)(expr)
        }
        case PSetConversion(op) => underlyingType(exprType(op)) match {
          case SequenceT(_) | SetT(_) | OptionT(_) => isExpr(op).out
          case t => error(op, s"expected a sequence, set or option type, but got $t")
        }
        case PMultisetConversion(op) => underlyingType(exprType(op)) match {
          case SequenceT(_) | MultisetT(_) | OptionT(_) => isExpr(op).out
          case t => error(op, s"expected a sequence, multiset or option type, but got $t")
        }
        case PMapKeys(exp) => underlyingType(exprType(exp)) match {
          case Single(_: MathMapT | _: MapT) => isExpr(exp).out
          case t => error(expr, s"expected a map, but got $t")
        }
        case PMapValues(exp) => underlyingType(exprType(exp)) match {
          case Single(_: MathMapT | _: MapT) => isExpr(exp).out
          case t => error(expr, s"expected a map, but got $t")
        }
        case PMathMapConversion(op) => underlyingType(exprType(op)) match {
          case Single(_: MapT) => isExpr(op).out
          case t => error(expr, s"expected a map, but got a $t")
        }
      }
    }

    case expr: PPermission => expr match {
      case PFullPerm() => noMessages
      case PWildcardPerm() => noMessages
      case PNoPerm() => noMessages
    }
  }

  private[typing] def wellDefMapUpdClause(keys: Type, values : Type, clause : PGhostCollectionUpdateClause, mayInit: Boolean) : Messages = {
    isExpr(clause.left).out ++ isExpr(clause.right).out ++
      assignableTo.errors(exprType(clause.left), keys, mayInit)(clause.left) ++
      assignableTo.errors(exprType(clause.right), values, mayInit)(clause.right)
  }

  private[typing] def wellDefSeqUpdClause(seqTyp : Type, clause : PGhostCollectionUpdateClause, mayInit: Boolean) : Messages =
    exprType(clause.left) match {
      case IntT(_) => isExpr(clause.left).out ++ isExpr(clause.right).out ++
        assignableTo.errors(exprType(clause.right), seqTyp, mayInit)(clause.right)
      case t => error(clause.left, s"expected an integer type but got $t")
    }

  private[typing] def ghostExprType(expr: PGhostExpression): Type = expr match {
    case POld(op) => exprType(op)
    case PLabeledOld(_, op) => exprType(op)
    case PBefore(op) => exprType(op)

    case PConditional(_, thn, els) =>
      typeMerge(exprType(thn), exprType(els)).getOrElse(violation("no common supertype found"))

    case PForall(_, _, body) => exprType(body)

    case PExists(_, _, body) => exprType(body)

    case n: PImplication => exprType(n.right) // implication is assertion or boolean iff its right side is

    case n: PLet => exprType(n.op)

    case _: PAccess | _: PPredicateAccess | _: PMagicWand => AssertionT

    case _: PClosureImplements => BooleanT

    case _: PTypeOf => SortT
    case _: PTypeExpr => SortT
    case _: PIsComparable => BooleanT

    case _: PLow | _: PLowContext => BooleanT
    case PRel(exp, _) => exprType(exp)
    case _: PGhostEquals | _: PGhostUnequals => BooleanT

    case POptionNone(t) => OptionT(typeSymbType(t))
    case POptionSome(e) =>
      val et = exprType(e)
      et match {
        case Single(t) => OptionT(t)
        case t => violation(s"expected a single type, but got $t")
      }

    case POptionGet(e) => exprType(e) match {
      case OptionT(t) => t
      case t => violation(s"expected an option type, but got $t")
    }

    case m: PMatchExp =>
      if (m.clauses.isEmpty) violation(s"expected that match exp always has a clause, but found none: $m.")
      else typeMergeAll(m.clauses map { c => exprType(c.exp) }).get

    case expr : PGhostCollectionExp => expr match {
      // The result of integer ghost expressions is unbounded (UntypedConst)
      case PMultiplicity(_, _) => IntT(config.typeBounds.UntypedConst)
      case PElem(_, _) => BooleanT
      case PGhostCollectionUpdate(seq, _) => exprType(seq)
      case expr : PSequenceExp => expr match {
        case PRangeSequence(_, _) => SequenceT(IntT(config.typeBounds.UntypedConst))

        case PSequenceAppend(left, right) =>
          val lType = exprType(left)
          val rType = exprType(right)
          typeMerge(lType, rType) match {
            case Some(seq) => seq
            case _ => violation(s"types $lType and $rType cannot be merged.")
          }
        case PSequenceConversion(op) => exprType(op) match {
          case t: SequenceT => t
          case t: ArrayT => SequenceT(t.elem)
          case t: OptionT => SequenceT(t.elem)
          case t => violation(s"expected an array, sequence or option type, but got $t")
        }
      }
      case expr : PUnorderedGhostCollectionExp => expr match {
        case expr : PBinaryGhostExp => expr match {
          case PSubset(_, _) => BooleanT
          case _ => exprType(expr.left)
        }
        case PSetConversion(op) => underlyingType(exprType(op)) match {
          case t: GhostCollectionType => SetT(t.elem)
          case t: OptionT => SetT(t.elem)
          case t => violation(s"expected a sequence, set, multiset or option type, but got $t")
        }
        case PMultisetConversion(op) => underlyingType(exprType(op)) match {
          case t : GhostCollectionType => MultisetT(t.elem)
          case t: OptionT => MultisetT(t.elem)
          case t => violation(s"expected a sequence, set, multiset or option type, but got $t")
        }
        case PMapKeys(exp) => underlyingType(exprType(exp)) match {
          case Single(t: MathMapT) => SetT(t.key)
          case Single(t: MapT) => SetT(t.key)
          case t => violation(s"expected a map, but got $t")
        }
        case PMapValues(exp) => underlyingType(exprType(exp)) match {
          case Single(t: MathMapT) => SetT(t.elem)
          case Single(t: MapT) => SetT(t.elem)
          case t => violation(s"expected a map, but got $t")
        }
        case PMathMapConversion(op) => underlyingType(exprType(op)) match {
          case MapT(k, v) => MathMapT(k, v)
          case t => violation(s"expected a map but got $t")
        }
      }
    }

    case _: PPermission => PermissionT
  }

  /**
    * Determines whether `expr` is a (strongly) pure expression
    * in the standard separation logic sense.
    */
  def isPureExpr(expr: PExpression) : Messages =
    error(expr, s"expected pure expression without permissions, but got $expr", !isPureExprAttr(expr))

  /**
    * Determines whether `expr` is a weakly pure expression,
    * meaning that `expr` must be pure in the separation logic
    * sense but is allowed to contain (accessibility) predicates.
    */
  def isWeaklyPureExpr(expr: PExpression): Messages =
    error(expr, s"expected pure expression, but got '$expr'",
      !isWeaklyPureExprAttr(expr))

  /**
    * Determines whether `expr` is a _(strongly) pure_ expression,
    * where _(strongly) pure_ means purity in the standard
    * separation logic sense; that is, doesn't have side-effect
    * on the shared state.
    *
    * We say that `expr` is _weakly pure_ if `expr` is pure
    * but is allowed to contain (accessibility) predicates.
    * Any weakly pure expression would for example be
    * a valid quantified permission body.
    *
    * @param expr The expression to test for (strong/weak) purity.
    * @param strong If `true`, then `isPure` tests for strong purity;
    *               otherwise for weak purity. (Is `true`` by default.)
    */
  private def isPure(expr : PExpression)(strong : Boolean) : Boolean = {
    def go(e : PExpression) = isPure(e)(strong)

    val res = expr match {
      case PNamedOperand(id) => isPureId(id, strong)

      case PBlankIdentifier() => true

      case _: PMagicWand => !strong

      case _: PClosureImplements => true

      case _: PBoolLit | _: PIntLit | _: PNilLit | _: PStringLit | _: PFloatLit => true

      case _: PIota => true

      // Might change at some point
      case n: PInvoke => (exprOrType(n.base), resolve(n)) match {
        case (Right(_), Some(p: ap.Conversion)) =>
          !isEffectfulConversion(p) && go(p.arg)
        case (Left(callee), Some(p@ap.FunctionCall(f, _))) => go(callee) && p.args.forall(go) && (f match {
          case ap.Function(_, symb) => symb.isPure
          case ap.Closure(_, symb) => symb.isPure
          case ap.DomainFunction(_, _) => true
          case ap.ReceivedMethod(_, _, _, symb) => symb.isPure
          case ap.ImplicitlyReceivedInterfaceMethod(_, symb) => symb.isPure
          case ap.MethodExpr(_, _, _, symb) => symb.isPure
          case ap.BuiltInReceivedMethod(_, _, _, symb) => symb.isPure
          case ap.BuiltInMethodExpr(_, _, _, symb) => symb.isPure
          case ap.BuiltInFunction(_, symb) => symb.isPure
        })
        case (Left(callee), Some(_: ap.ClosureCall)) => resolve(n.spec.get.func) match {
          case Some(ap.Function(_, f)) => f.isPure && go(callee) && n.args.forall(a => go(a))
          case Some(ap.Closure(_, c)) => c.isPure && go(callee) && n.args.forall(a => go(a))
          case _ => false
        }
        case (Left(_), Some(_: ap.PredicateCall)) => !strong
        case (Left(_), Some(_: ap.PredExprInstance)) => !strong
        case _ => false
      }

      case n: PDot => exprOrType(n.base) match {
        case Left(e) => go(e) && isPureId(n.id, strong)
        case Right(_) => isPureId(n.id, strong) // Maybe replace with a violation
      }

      case PReference(e) => go(e)
      case n: PDeref =>
        resolve(n) match {
          case Some(p: ap.Deref) => go(p.base)
          case _ => true
        }

      case PNegation(e) => go(e)

      case PBitNegation(e) => go(e)

      case x: PBinaryExp[_,_] =>
        asExpr(x.left).forall(go) && asExpr(x.right).forall(go) && (x match {
        case _: PEquals | _: PUnequals |
             _: PAnd | _: POr |
             _: PLess | _: PAtMost | _: PGreater | _: PAtLeast |
             _: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv |
             _: PShiftLeft | _: PShiftRight | _: PBitAnd |
             _: PBitOr | _: PBitXor | _: PBitClear => true
        case _ => false
      })

      case _: PUnfolding => true
      case _: PLet => true // the well-definedness check makes sure that both sub-expressions are pure.
      case _: POld | _: PLabeledOld | _: PBefore => true
      case f: PForall => go(f.body)
      case e: PExists =>
        // The type checker currently enforces that all existential quantifiers must be strongly pure, so we wouldn't need to recurse here.
        // Nonetheless, this implementation is safer in case that ever changes.
        go(e.body)

      case PConditional(cond, thn, els) => Seq(cond, thn, els).forall(go)

      case PImplication(left, right) => Seq(left, right).forall(go)

      case PLength(op) => go(op)
      case PCapacity(op) => go(op)

      case PGhostEquals(l, r) => go(l) && go(r)
      case PGhostUnequals(l, r) => go(l) && go(r)

      case expr: PGhostCollectionExp => expr match {
        case n: PBinaryGhostExp => go(n.left) && go(n.right)
        case PSequenceConversion(op) => go(op)
        case PSetConversion(op) => go(op)
        case PMultisetConversion(op) => go(op)
        case PRangeSequence(low, high) => go(low) && go(high)
        case PGhostCollectionUpdate(seq, clauses) => go(seq) && clauses.forall(isPureGhostColUpdClause)
        case PMapKeys(exp) => go(exp)
        case PMapValues(exp) => go(exp)
        case PMathMapConversion(exp) => go(exp)
      }

      case _: PAccess | _: PPredicateAccess => !strong
      case PPredConstructor(_, args) => args.flatten.forall(go)

      case n: PTypeAssertion => go(n.base)
      case n: PTypeOf => go(n.exp)
      case _: PTypeExpr => true
      case n: PIsComparable => asExpr(n.exp).forall(go)

      case n: PLow => go(n.exp)
      case _: PLowContext => true
      case n: PRel => go(n.exp)

      case PCompositeLit(typ, _) => typ match {
        case _: PImplicitSizeArrayType => true
        case UnderlyingPType(t: PLiteralType) => t match {
          case g: PGhostLiteralType => g match {
            case _: PGhostSliceType => false
            case _: PAdtType | _: PDomainType | _: PMathematicalMapType |
              _: PMultisetType | _: POptionType | _: PSequenceType | _: PSetType => true
            case _: PExplicitGhostStructType => true
          }
          case _: PArrayType | _: PStructType => true
          case _: PMapType | _: PSliceType => false
          case d@(_: PDot | _: PNamedOperand) =>
            // UnderlyingPType should never return any of these types
            violation(s"Unexpected underlying type $d")
        }
        case t =>
          // the type system should already have rejected composite literals whose underlying type is not a valid
          // literal type.
          violation(s"Unexpected literal type $t")
      }

      case POptionNone(_) => true
      case POptionSome(e) => go(e)
      case POptionGet(e) => go(e)

      case PMatchExp(e, clauses) => go(e) && clauses.forall(c => go(c.exp))

      case PSliceExp(base, low, high, cap) =>
        go(base) && Seq(low, high, cap).flatten.forall(go)

      case PIndexedExp(base, index) => Seq(base, index).forall(go)

      case _: PMake | _: PNew => false

      case _: PFunctionLit => true

      // Others
      case PReceive(_) => false

      case PUnpackSlice(s) => go(s)

      case PFullPerm() | PNoPerm() | PWildcardPerm() => true
    }

    res
  }

  private def isPureGhostColUpdClause(clause : PGhostCollectionUpdateClause) : Boolean =
    isPureExprAttr(clause.left) && isPureExprAttr(clause.right)

  private def isPureId(id: PIdnNode, strong: Boolean): Boolean = {
    entity(id) match {
      case _: Constant => true
      case _: Variable => true
      case _: Field => true
      case _: Embbed => true
      // functions and method constants are always pure
      // the pureness of function or method invocations are checked separately
      case _: Function => true
      case _: Method => true
      case _: Closure => true
      case m: BuiltInFunction => m.isPure
      case m: BuiltInMethod => m.isPure
      case _: Predicate | _: BuiltInFPredicate | _: BuiltInMPredicate => !strong
      case _: DomainFunction => true
      case _: AdtMember => true
      case _ => false
    }
  }

  private lazy val isPureExprAttr: PExpression => Boolean =
    attr[PExpression, Boolean] { isPure(_)(true) }

  private lazy val isWeaklyPureExprAttr: PExpression => Boolean =
    attr[PExpression, Boolean] { isPure(_)(false) }

  /**
    * Helper operation for composing two results of `validTriggerPattern` into one.
    */
  private def combineTriggerResults(p1 : (Vector[String], Messages), p2 : (Vector[String], Messages)) : (Vector[String], Messages) =
    (p1._1 ++ p2._1, p1._2 ++ p2._2)

  /**
    * Helper operator for composing a sequence of results
    * of of `validTriggerPattern` into one.
    */
  private def combineTriggerResults(xs : Vector[(Vector[String], Messages)]) : (Vector[String], Messages) =
    xs.fold(Vector(), noMessages)(combineTriggerResults)

  /**
    * Determines whether `expr` is a valid trigger pattern.
    * @param expr The expression that is to be checked for validity.
    * @return A pair consisting of (1) all identifiers occurring freely in `expr`,
    *         to be used later for extra consistency checking; and (2) a sequence
    *         of error messages. The latter sequence is empty if
    *         (but currently not only if) `expr` is a valid trigger pattern.
    */
  private def validTriggerPattern(expr : PExpression) : (Vector[String], Messages) = {
    // shorthand definition
    def goEorT(node : PExpressionOrType) : (Vector[String], Messages) = node match {
      case PDeref(base) => goEorT(base)
      case e : PExpression => validTriggerPattern(e)
      case _ => (Vector(), noMessages)
    }

    expr match {
      case PDot(base, _) => goEorT(base)
      case PInvoke(base, args, None, _) => {
        val res1 = goEorT(base)
        val res2 = combineTriggerResults(args.map(validTriggerPattern))
        combineTriggerResults(res1, res2)
      }
      case PNamedOperand(id) => (Vector(id.name), noMessages)
      case _ => (Vector(), error(expr, s"invalid trigger pattern '$expr'"))
    }
  }

  /**
    * Determines whether `trigger` is a valid and consistent trigger.
    * Validity and consistency in this sense mean:
    *
    * 1. Any trigger expression in `trigger` must be valid according to `validTriggerPattern`;
    * 2. All variables `boundVars` bound by the quantified must occur in `trigger`.
    *
    * @param boundVars The variables bound by the quantifier in which `trigger` is used.
    * @param trigger The trigger to be tested for validity and consistency.
    * @return True if  `trigger` is a valid and consistent trigger.
    */
  @unused
  private def validTrigger(boundVars: Vector[PBoundVariable], trigger : PTrigger) : Messages = {
    // [validity] check whether all expressions in `trigger` are valid trigger expressions
    val (usedVars, msgs1) = combineTriggerResults(trigger.exps.map(validTriggerPattern))

    // [consistency] check whether all `boundVars` occur inside `trigger`
    val msgs2 = (boundVars filterNot (v => usedVars.contains(v.id.name)))
      .flatMap(v => error(v, s"consistency error: variable '${v.id}' is not mentioned in the trigger pattern '$trigger'"))

    msgs1 ++ msgs2
  }

  /**
    * Determines whether `triggers` is a valid and consistent sequence of triggers.
    * Currently `triggers` is valid if every trigger `t` in this
    * sequence is valid and consistent with respect to `validTrigger(t)`.
    * @param triggers The sequence of triggers to be tested for validity.
    * @return True if `triggers` is a valid sequence of triggers.
    */
  private def validTriggers(@unused vars: Vector[PBoundVariable], @unused triggers: Vector[PTrigger]) : Messages = {
    // TODO: As a temporary workaround, the validity checks for triggers are disabled because they are too restrictive.
    //       We should either extend the validity checks or, somehow, defer that task to Viper
    // triggers.flatMap(validTrigger(vars, _))
    noMessages
  }
}
