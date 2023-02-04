// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyr (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.frontend.info.base.Type._
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.ast.frontend.{PNode}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.{SymbolTable => symb}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.TypeBounds
import viper.gobra.util.Violation

trait Visibility extends BaseProperty { this: TypeInfoImpl =>

  def isPrivate(expr: PExpression): Boolean = isPrivateExpression(expr)

  /* def isPrivate(p: PNode): Boolean = p match {
    case id: PIdnNode => isPrivateRegular(id)
    //case r: PReceiver => isPrivate(r.typ.typ.id)
    case typ: PType => isPrivateType(typ) //type private?
    //case stmt: PStatement => isPrivateStatement(stmt)
    case expr: PExpression => isPrivateExpression(expr) 
    case etyp: PExpressionOrType => etyp match {
      case e: PExpression => isPrivateExpression(e)
      case t: PType => isPrivateType(t)
      case t => Violation.violation(s"Unexpected ExpressionOrType in isPrivate, got $t")
    }
    //case spec: PSpecification => isPrivateSpecification(spec) //???
    case mem: PMember => isPrivateMember(mem) //for predicates and pure functions
    //case field: PFieldDecl => //checks if a field is private
    case t => Violation.violation(s"Unexpected Node in isPrivate, got $t")
  } */

  private def isPrivateExpType(etyp: PExpressionOrType): Boolean = etyp match {
    case expr: PExpression => isPrivateExpression(expr)
    case typ: PType => isPrivateType(typ)
    case t => Violation.violation(s"Unexpected Expression or Typ in isPrivateExpType, got $t") 
  }

  private def isPrivateRegular(id: PIdnNode): Boolean = regular(id) match {
    case g: symb.GlobalVariable => startsWithLowercase(g.id.name)
    case v: symb.Variable => false //startsWithLowercase(v.toString)
    case c: symb.Constant => false //startsWithLowercase(c.toString) //Constant but with IdnNode???
    case f: symb.Function => startsWithLowercase(f.decl.id.name) || (if (f.isPure) isPrivateParam(f.args) else false)
    case m: symb.Method => m match {
      case mi: symb.MethodImpl => startsWithLowercase(mi.decl.id.name) || (if (m.isPure) isPrivateParam(mi.args) else false)
      case ms: symb.MethodSpec => startsWithLowercase(ms.spec.id.name) || (if (m.isPure) isPrivateParam(ms.args) else false)
      case t => Violation.violation(s"Unexpected Method in isPrivateRegular, got $t") 
    } 
    case p: symb.Predicate => p match {
      case fp: symb.FPredicate => startsWithLowercase(fp.decl.id.name) || isPrivateParam(fp.args) || isPrivateExpression(fp.decl.body.getOrElse(null))
      case mp: symb.MPredicate => mp match {
        case mpi: symb.MPredicateImpl => startsWithLowercase(mpi.decl.id.name) || isPrivateParam(mpi.args) || isPrivateExpression(mpi.decl.body.getOrElse(null))
        case mps: symb.MPredicateSpec => startsWithLowercase(mps.decl.id.name) || isPrivateParam(mps.args)
      }
      case t => Violation.violation(s"Unexpected Predicate in isPrivateRegular, got $t") 
    }
    case f: symb.Field => startsWithLowercase(f.decl.id.name)
    case t: symb.NamedType => startsWithLowercase(t.decl.left.name) //isPrivateType(t.decl.right)
    case t => Violation.violation(s"Unexpected PIdnNode in isPrivateRegular, got $t") 
  }

  private def isPrivateType(typ: PType): Boolean = typ match {
    case atyp: PActualType => atyp match {
      case PNamedOperand(id) => isPrivateRegular(id)
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
      case PArrayType(len, elem) => isPrivateExpression(len) || isPrivateType(elem)
      case PSliceType(elem) => isPrivateType(elem)
      case PVariadicType(elem) => isPrivateType(elem)
      case PMapType(key, elem) => isPrivateType(key) || isPrivateType(elem)
      case PDeref(base) => isPrivateExpType(base)
      case PDot(base, id) => isPrivateExpType(base) || isPrivateRegular(id)
      case channelType: PChannelType => channelType match {
        case PBiChannelType(elem)   => isPrivateType(elem)
        case PSendChannelType(elem) => isPrivateType(elem)
        case PRecvChannelType(elem) => isPrivateType(elem)
        case t => Violation.violation(s"Unexpected ChannelType in isPrivateType, got $t") 
      }
      //case PStructType(clauses) => clauses.map(c => isPrivate(c)).reduce(_||_)
      case styp: PStructType => startsWithLowercase(styp.toString) //styp name?
      case PFunctionType(args, _) => isPrivateParam(args) //check args: Vector[PParameter]
      case PPredType(args) => args.map(arg => isPrivateType(arg)).reduceOption(_||_).getOrElse(false) //check the args: Vectro[PType]
      case ityp: PInterfaceType => startsWithLowercase(ityp.toString) //ityp name?
      //case PMethodReceiveName(t) => startsWithLowercase(t.typ.id.name)
      //case PMethodReceivePointer(t) => startsWithLowercase(t.typ.id.name)
      case t => Violation.violation(s"Unexpected ActualType in isPrivateType, got $t") 
    }
    case gtyp: PGhostType => gtyp match {
      case PSequenceType(elem) => isPrivateType(elem)
      case PSetType(elem) => isPrivateType(elem)
      case PMultisetType(elem) => isPrivateType(elem)
      case PMathematicalMapType(keys, values) => isPrivateType(keys) || isPrivateType(values)
      case POptionType(elem) => isPrivateType(elem)
      case PGhostSliceType(elem) => isPrivateType(elem)
      //case PDomainType(funcs, axioms) =>
      //case PAdtType(clauses) => "adt" <> block(ssep(clauses map showMisc, line))
      case t => Violation.violation(s"Unexpected GhostType in isPrivateType, got $t") 
    }
    case t => Violation.violation(s"Unexpected Type in isPrivateType, got $t") 
  }

  /* private def isPrivateMember(mem: PMember): Boolean = mem match {
    case mem: PActualMember => mem match {
      case PConstDecl(specs) => specs.map(c => c.left.map(id => isPrivateRegular(id)).reduce(_||_)).reduce(_||_) //id: PDefLikeId
      case PVarDecl(_, _, l, _) => l.map(id => isPrivateRegular(id)).reduce(_||_) //l: PDefLikeId
      case n: PTypeDecl => isPrivateRegular(n.left) //n.left: PIdnDef
      case PFunctionDecl(id, _, _, _, _) => isPrivateRegular(id) 
      case PMethodDecl(id, _, _, _, _, _) => isPrivateRegular(id) 
      case t => Violation.violation(s"Unexpected ActualMember in isPrivateMember, got $t")
    }
    case gmem: PGhostMember => gmem match {
      case PExplicitGhostMember(_) => true //???
      //case PFPredicateDecl(id, args, body) => isPrivateRegular(id) || isPrivateParam(args) || isPrivateExpression(body.getOrElse(null))
      //case PMPredicateDecl(id, recv, args, body) => isPrivateRegular(id) || isPrivateType(recv.typ.typ) || isPrivateParam(args) || isPrivateExpression(body.getOrElse(null))
      case PFPredicateDecl(id, _, _) => isPrivateRegular(id) //only need this because isPrivateRegular does the above already
      case PMPredicateDecl(id, _, _, _) => isPrivateRegular(id) //only need this because isPrivateRegular does the above already
      case t => Violation.violation(s"Unexpected GhostMember in isPrivateMember, got $t" )
    }
    case t => Violation.violation(s"Unexpected Member in isPrivateMember, got $t")
  } */

  private def isPrivateExpression(expr: PExpression): Boolean = expr match {
    case null => false
    case expr: PActualExpression => expr match {
      case PReceive(op) => isPrivateExpression(op)
      case PReference(op) => isPrivateExpression(op)
      case PDeref(base) => isPrivateExpType(base)
      case PDot(base, id) => isPrivateExpType(base) || isPrivateRegular(id)
      case PNegation(op) => isPrivateExpression(op)
      case PNamedOperand(id) => isPrivateRegular(id)
      case PBoolLit(_) => false
      case PIntLit(_, _) => false
      case PFloatLit(_) => false
      case PNilLit() => false
      case PStringLit(_) => false
      case PCompositeLit(typ, lit) => false //typ: PLiteralType, lit: PLiteralValue
      case PFunctionLit(id, _) => isPrivateRegular(id.getOrElse(null)) //id: Option[PIdnDef], args: Vector[PParameter]
      case PInvoke(base, args, _) => isPrivateExpType(base) || isPrivateVector(args)
      case PIndexedExp(base, index) => isPrivateExpression(base) || isPrivateExpression(index)
      case PSliceExp(base, low, high, cap) => isPrivateExpression(base) || isPrivateExpression(low.getOrElse(null)) || isPrivateExpression(high.getOrElse(null)) || isPrivateExpression(cap.getOrElse(null))
      case PUnpackSlice(elem) => isPrivateExpression(elem)
      case PTypeAssertion(base, typ) => isPrivateExpression(base) || isPrivateType(typ) //typ: PType
      case PEquals(l, r) => isPrivateExpType(l) || isPrivateExpType(r)
      case PUnequals(l, r) => isPrivateExpType(l) || isPrivateExpType(r)
      case PAnd(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case POr(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PLess(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PAtMost(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PGreater(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PAtLeast(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PAdd(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PSub(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PMul(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PMod(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PDiv(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PUnfolding(pred, op) => isPrivateExpression(pred) || isPrivateExpression(op)
      case PLength(exp) => isPrivateExpression(exp)
      case PCapacity(exp) => isPrivateExpression(exp)
      case PMake(typ, args) => isPrivateType(typ) || isPrivateVector(args)
      case PNew(typ) => isPrivateType(typ) 
      case PBlankIdentifier() => false
      // case PPredConstructor(id, args) => isPrivate(id.id) || isPrivateVector(args)
      case PBitAnd(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PBitOr(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PBitXor(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PBitClear(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PShiftLeft(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PShiftRight(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PBitNegation(op) => isPrivateExpression(op)
      case t => Violation.violation(s"Unexpected ActualExpression in isPrivateExpression, got $t")
    }
    case expr: PGhostExpression => expr match {
      case PGhostEquals(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PGhostUnequals(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case POld(op) => isPrivateExpression(op)
      case PLabeledOld(_, op) => isPrivateExpression(op)
      case PBefore(op) => isPrivateExpression(op)
      case PConditional(cond, thn, els) => isPrivateExpression(cond) || isPrivateExpression(thn) || isPrivateExpression(els)
      case PForall(_, triggers, body) => triggers.map(t => t.exps.map(e => isPrivateExpression(e)).reduceOption(_||_).getOrElse(false)).reduceOption(_||_).getOrElse(false) || isPrivateExpression(body)
      case PExists(_, triggers, body) => triggers.map(t => t.exps.map(e => isPrivateExpression(e)).reduceOption(_||_).getOrElse(false)).reduceOption(_||_).getOrElse(false) || isPrivateExpression(body)
      case PImplication(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PAccess(exp, PFullPerm()) => isPrivateExpression(exp)
      case PAccess(exp, perm) => isPrivateExpression(exp) || isPrivateExpression(perm)
      case PPredicateAccess(exp, PFullPerm()) => isPrivateExpression(exp) 
      case PPredicateAccess(exp, perm) => isPrivateExpression(exp) || isPrivateExpression(perm)
      case PPrivate(exp) => isPrivateExpression(exp)
      case PMagicWand(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
      case PClosureImplements(closure, _) => isPrivateExpression(closure) //spec: PClosureSpecInstance
      case PTypeOf(exp) => isPrivateExpression(exp)
      case PTypeExpr(typ) => isPrivateType(typ)
      case PIsComparable(exp) => isPrivateExpType(exp)
      case POptionNone(t) => isPrivateType(t)
      case POptionSome(exp) => isPrivateExpression(exp)
      case POptionGet(exp) => isPrivateExpression(exp)
      case PMatchExp(exp, clauses) => isPrivateExpression(exp) || isPrivateVector(clauses.map(c => c.exp)) //clauses: Vector[PMatchExpClause]
      case expr : PGhostCollectionExp => expr match {
        case PIn(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
        case PMultiplicity(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
        case PGhostCollectionUpdate(col, clauses) => isPrivateExpression(col) || isPrivateVector(clauses.map(c => c.left)) || isPrivateVector(clauses.map(c => c.right)) //clauses: Vector[PGhostCollectionUpdateClause]
        case expr : PSequenceExp => expr match {
          case PSequenceConversion(exp) => isPrivateExpression(exp)
          case PRangeSequence(low, high) => isPrivateExpression(low) || isPrivateExpression(high)
          case PSequenceAppend(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
          case t => Violation.violation(s"Unexpected SequenceExp in isPrivateExpression, got $t")
        }
        case expr : PUnorderedGhostCollectionExp => expr match {
          case PUnion(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
          case PIntersection(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
          case PSetMinus(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
          case PSubset(l, r) => isPrivateExpression(l) || isPrivateExpression(r)
          case PSetConversion(exp) => isPrivateExpression(exp)
          case PMultisetConversion(exp) => isPrivateExpression(exp)
          case PMapKeys(exp) => isPrivateExpression(exp)
          case PMapValues(exp) => isPrivateExpression(exp)
          case t => Violation.violation(s"Unexpected UnorderedGhostCollectionExp in isPrivateExpression, got $t")
        }
        case t => Violation.violation(s"Unexpected GhostCollectionExp in isPrivateExpression, got $t")
      } 
      case t => Violation.violation(s"Unexpected GhostExpression in isPrivateExpression, got $t")
    }
    case t => Violation.violation(s"Unexpected Expression in isPrivateExpression, got $t")
  }

  private def isPrivateVector(v: Vector[PExpression]): Boolean = v.map(arg => isPrivateExpression(arg)).reduceOption(_||_).getOrElse(false)
  private def isPrivateParam(v: Vector[PParameter]): Boolean = v.map(p => isPrivateType(p.typ)).reduceOption(_||_).getOrElse(false)

  //private def privateFunctionArgumentsType(w: WithArguments): Boolean = if (w.isPure) privateType(w.args) else false
  //private def privateArgumentsType(w: WithArguments): Boolean = privateType(w.args)

  private def startsWithLowercase(s: String): Boolean = s.charAt(0).isLower

  //private def isArgument(p: PParameter): Boolean = isParamGhost(p)
}