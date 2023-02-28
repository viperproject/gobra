// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyr (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.{SymbolTable => symb}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation

trait Visibility extends BaseProperty { this: TypeInfoImpl =>

  def isPrivate(expr: PExpression): Boolean = isPrivateExp(expr)

  private def isPrivateExpTyp(etyp: PExpressionOrType): Boolean = etyp match {
    case exp: PExpression => isPrivateExp(exp)
    case typ: PType => isPrivateTyp(typ)
    case t => Violation.violation(s"Unexpected Expression or Type in isPrivateExpTyp, got $t") 
  }

  def isPrivateReg(id: PIdnNode): Boolean = regular(id) match {
    case g: symb.GlobalVariable => isPrivateString(g.id.name)
    case _: symb.Variable => false 
    case c: symb.Constant => c.decl.left.exists(id => isPrivateString(id.name))
    case f: symb.Function => isPrivateString(f.decl.id.name) || (if (f.isPure) isPrivateVParam(f.args) else false)
    case m: symb.Method => m match {
      case mi: symb.MethodImpl => isPrivateString(mi.decl.id.name) || (if (m.isPure) isPrivateVParam(mi.args) else false)
      case ms: symb.MethodSpec => isPrivateString(ms.spec.id.name) || (if (m.isPure) isPrivateVParam(ms.args) else false)
      case t => Violation.violation(s"Unexpected Method in isPrivateReg, got $t") 
    } 
    case p: symb.Predicate => p match {
      case fp: symb.FPredicate => isPrivateString(fp.decl.id.name) || isPrivateVParam(fp.args) // || isPrivateExp(fp.decl.body.getOrElse(null))
      case mp: symb.MPredicate => mp match {
        case mpi: symb.MPredicateImpl => isPrivateString(mpi.decl.id.name) || isPrivateVParam(mpi.args) // || isPrivateExp(mpi.decl.body.getOrElse(null))
        case mps: symb.MPredicateSpec => isPrivateString(mps.decl.id.name) || isPrivateVParam(mps.args)
      }
      case t => Violation.violation(s"Unexpected Predicate in isPrivateReg, got $t") 
    }
    case f: symb.Field => isPrivateString(f.decl.id.name)
    case t: symb.NamedType => isPrivateString(t.decl.left.name) 
    case _: symb.Import => false
    case _: symb.Closure => false
    case _ => false
  }

  private def isPrivateTyp(typ: PType): Boolean = typ match {
    case atyp: PActualType => atyp match {
      case PNamedOperand(id) => isPrivateReg(id)
      case PBoolType() => false
      case PStringType() => false
      case PPermissionType() => false
      case PIntType() => false
      case PInt8Type() => false
      case PInt16Type() => false
      case PInt32Type() => false
      case PRune() => false
      case PInt64Type() => false
      case PUIntType() => false
      case PUInt8Type() => false
      case PByte() => false
      case PUInt16Type() => false
      case PUInt32Type() => false
      case PUInt64Type() => false
      case PUIntPtr() => false
      case PFloat32() => false
      case PFloat64() => false
      case PArrayType(len, elem) => isPrivateExp(len) || isPrivateTyp(elem)
      case PSliceType(elem) => isPrivateTyp(elem)
      case PVariadicType(elem) => isPrivateTyp(elem)
      case PMapType(key, elem) => isPrivateTyp(key) || isPrivateTyp(elem)
      case PDeref(base) => isPrivateExpTyp(base)
      case PDot(base, id) => isPrivateExpTyp(base) || isPrivateReg(id)
      case channelType: PChannelType => channelType match {
        case PBiChannelType(elem)   => isPrivateTyp(elem)
        case PSendChannelType(elem) => isPrivateTyp(elem)
        case PRecvChannelType(elem) => isPrivateTyp(elem)
        case t => Violation.violation(s"Unexpected ChannelType in isPrivateTyp, got $t") 
      }
      case styp: PStructType => isPrivateString(styp.toString) 
      case PFunctionType(args, _) => isPrivateVParam(args)
      case PPredType(args) => args.exists(arg => isPrivateTyp(arg))
      case ityp: PInterfaceType => isPrivateString(ityp.toString) 
      case t => Violation.violation(s"Unexpected ActualType in isPrivateTyp, got $t") 
    }
    case gtyp: PGhostType => gtyp match {
      case PSequenceType(elem) => isPrivateTyp(elem)
      case PSetType(elem) => isPrivateTyp(elem)
      case PMultisetType(elem) => isPrivateTyp(elem)
      case PMathematicalMapType(keys, values) => isPrivateTyp(keys) || isPrivateTyp(values)
      case POptionType(elem) => isPrivateTyp(elem)
      case PGhostSliceType(elem) => isPrivateTyp(elem)
      case PDomainType(funcs, axioms) => funcs.exists(f => isPrivateString(f.id.name)) || axioms.exists(a => isPrivateExp(a.exp))
      case PAdtType(clauses) => clauses.exists(c => isPrivateString(c.id.name))
      case t => Violation.violation(s"Unexpected GhostType in isPrivateTyp, got $t") 
    }
    case t => Violation.violation(s"Unexpected Type in isPrivateTyp, got $t") 
  }

  private def isPrivateExp(expr: PExpression): Boolean = expr match {
    case expr: PActualExpression => expr match {
      case PReceive(op) => isPrivateExp(op)
      case PReference(op) => isPrivateExp(op)
      case PDeref(base) => isPrivateExpTyp(base)
      case PDot(base, id) =>  isPrivateExpTyp(base) || isPrivateReg(id)
      case PNegation(op) => isPrivateExp(op)
      case PNamedOperand(id) => isPrivateReg(id)
      case PBoolLit(_) => false
      case PIntLit(_, _) => false
      case PFloatLit(_) => false
      case PNilLit() => false
      case PStringLit(_) => false
      case PCompositeLit(typ, _) => s"$typ".split('.').tail.exists(isPrivateString)
      case PFunctionLit(id, _) => id.exists(s => isPrivateString(s.name))
      case PInvoke(base, args, _) => isPrivateExpTyp(base) || isPrivateVExp(args)
      case PIndexedExp(base, index) => isPrivateExp(base) || isPrivateExp(index)
      case PSliceExp(base, low, high, cap) => isPrivateExp(base) || low.exists(isPrivateExp) || high.exists(isPrivateExp) || cap.exists(isPrivateExp)
      case PUnpackSlice(elem) => isPrivateExp(elem)
      case PTypeAssertion(base, typ) => isPrivateExp(base) || isPrivateTyp(typ)
      case PEquals(l, r) => isPrivateExpTyp(l) || isPrivateExpTyp(r)
      case PUnequals(l, r) => isPrivateExpTyp(l) || isPrivateExpTyp(r)
      case PAnd(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case POr(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PLess(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PAtMost(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PGreater(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PAtLeast(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PAdd(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PSub(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PMul(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PMod(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PDiv(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PUnfolding(pred, op) => isPrivateExp(pred) || isPrivateExp(op)
      case PLength(exp) => isPrivateExp(exp)
      case PCapacity(exp) => isPrivateExp(exp)
      case PMake(typ, args) => isPrivateTyp(typ) || isPrivateVExp(args)
      case PNew(typ) => isPrivateTyp(typ) 
      case PBlankIdentifier() => false
      case PPredConstructor(id, args) => isPrivateString(id.id.name)
      case PBitAnd(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PBitOr(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PBitXor(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PBitClear(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PShiftLeft(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PShiftRight(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PBitNegation(op) => isPrivateExp(op)
      case t => Violation.violation(s"Unexpected ActualExpression in isPrivateExp, got $t")
    }
    case expr: PGhostExpression => expr match {
      case PGhostEquals(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PGhostUnequals(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case POld(op) => isPrivateExp(op)
      case PLabeledOld(_, op) => isPrivateExp(op)
      case PBefore(op) => isPrivateExp(op)
      case PConditional(cond, thn, els) => isPrivateExp(cond) || isPrivateExp(thn) || isPrivateExp(els)
      case PForall(_, triggers, body) => triggers.exists(t => t.exps.exists(isPrivateExp)) || isPrivateExp(body)
      case PExists(_, triggers, body) => triggers.exists(t => t.exps.exists(isPrivateExp)) || isPrivateExp(body)
      case PImplication(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PAccess(exp, PFullPerm()) => isPrivateExp(exp)
      case PAccess(exp, perm) => isPrivateExp(exp) || isPrivateExp(perm)
      case PPredicateAccess(exp, PFullPerm()) => isPrivateExp(exp) 
      case PPredicateAccess(exp, perm) => isPrivateExp(exp) || isPrivateExp(perm)
      case PMagicWand(l, r) => isPrivateExp(l) || isPrivateExp(r)
      case PClosureImplements(closure, _) => isPrivateExp(closure) 
      case PTypeOf(exp) => isPrivateExp(exp)
      case PTypeExpr(typ) => isPrivateTyp(typ)
      case PIsComparable(exp) => isPrivateExpTyp(exp)
      case POptionNone(t) => isPrivateTyp(t)
      case POptionSome(exp) => isPrivateExp(exp)
      case POptionGet(exp) => isPrivateExp(exp)
      case PMatchExp(exp, clauses) => isPrivateExp(exp) || isPrivateVExp(clauses.map(c => c.exp))
      case expr : PGhostCollectionExp => expr match {
        case PIn(l, r) => isPrivateExp(l) || isPrivateExp(r)
        case PMultiplicity(l, r) => isPrivateExp(l) || isPrivateExp(r)
        case PGhostCollectionUpdate(col, clauses) => isPrivateExp(col) || isPrivateVExp(clauses.map(c => c.left)) || isPrivateVExp(clauses.map(c => c.right)) //clauses: Vector[PGhostCollectionUpdateClause]
        case expr : PSequenceExp => expr match {
          case PSequenceConversion(exp) => isPrivateExp(exp)
          case PRangeSequence(low, high) => isPrivateExp(low) || isPrivateExp(high)
          case PSequenceAppend(l, r) => isPrivateExp(l) || isPrivateExp(r)
          case t => Violation.violation(s"Unexpected SequenceExp in isPrivateExp, got $t")
        }
        case expr : PUnorderedGhostCollectionExp => expr match {
          case PUnion(l, r) => isPrivateExp(l) || isPrivateExp(r)
          case PIntersection(l, r) => isPrivateExp(l) || isPrivateExp(r)
          case PSetMinus(l, r) => isPrivateExp(l) || isPrivateExp(r)
          case PSubset(l, r) => isPrivateExp(l) || isPrivateExp(r)
          case PSetConversion(exp) => isPrivateExp(exp)
          case PMultisetConversion(exp) => isPrivateExp(exp)
          case PMapKeys(exp) => isPrivateExp(exp)
          case PMapValues(exp) => isPrivateExp(exp)
          case t => Violation.violation(s"Unexpected UnorderedGhostCollectionExp in isPrivateExp, got $t")
        }
        case t => Violation.violation(s"Unexpected GhostCollectionExp in isPrivateExp, got $t")
      } 
      case t => Violation.violation(s"Unexpected GhostExpression in isPrivateExp, got $t")
    }
    case t => Violation.violation(s"Unexpected Expression in isPrivateExp, got $t")
  }

  private def isPrivateVExp(v: Vector[PExpression]): Boolean = v.exists(arg => isPrivateExp(arg))
  private def isPrivateVParam(v: Vector[PParameter]): Boolean = v.exists(p => isPrivateTyp(p.typ))
  private def isPrivateString(s: String): Boolean = s.charAt(0).isLower
}