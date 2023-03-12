// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2023 ETH Zurich.

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

  def isPrivateIdentifier(id: PIdnNode): Boolean = regular(id) match {
    case g: symb.GlobalVariable => isPrivateString(g.id.name)
    case _: symb.Variable => false
    case c: symb.Constant => c.decl.left.exists(id => isPrivateString(id.name))
    case f: symb.Function => isPrivateString(f.decl.id.name)
    case m: symb.Method => m match {
      case mi: symb.MethodImpl => isPrivateString(mi.decl.id.name)
      case ms: symb.MethodSpec => isPrivateString(ms.spec.id.name)
      case t => Violation.violation(s"Unexpected Method in isPrivateIdentifier, got $t") 
    } 
    case p: symb.Predicate => p match {
      case fp: symb.FPredicate => isPrivateString(fp.decl.id.name)
      case mp: symb.MPredicate => mp match {
        case mpi: symb.MPredicateImpl => isPrivateString(mpi.decl.id.name)
        case mps: symb.MPredicateSpec => isPrivateString(mps.decl.id.name)
      }
      case t => Violation.violation(s"Unexpected Predicate in isPrivateIdentifier, got $t") 
    }
    case f: symb.Field => isPrivateString(f.decl.id.name)
    case t: symb.NamedType => isPrivateString(t.decl.left.name) 
    case _: symb.Import => false
    case _: symb.Closure => false
    case _ => false
  }

  private def isPrivateTyp(typ: PType): Boolean = typ match {
    case atyp: PActualType => atyp match {
      case PBoolType() => false
      case PStringType() => false
      case PPermissionType() => false
      case PIntType()  | PInt8Type()   | PInt16Type()  | PInt32Type()  | PInt64Type() |
           PUIntType() | PUInt8Type()  | PUInt16Type() | PUInt32Type() | PUInt64Type() => false
      case PRune() => false
      case PByte() => false
      case PUIntPtr() => false
      case PFloat32() | PFloat64() => false
      case styp: PStructType => isPrivateString(styp.toString) 
      case PFunctionType(args, _) => isPrivateVParam(args)
      case PPredType(args) => args.exists(arg => isPrivateTyp(arg))
      case ityp: PInterfaceType => isPrivateString(ityp.toString) 
      case _ => isPrivateChild(atyp)
    }
    case gtyp: PGhostType => gtyp match {
      case PDomainType(funcs, _) => funcs.exists(f => isPrivateString(f.id.name))
      case PAdtType(clauses) => clauses.exists(c => isPrivateString(c.id.name))
      case _ => isPrivateChild(gtyp)
    }
    case t => Violation.violation(s"Unexpected Type in isPrivateTyp, got $t") 
  }

  private def isPrivateExp(expr: PExpression): Boolean = expr match {
    case expr: PActualExpression => expr match {
      case PBoolLit(_) => false
      case PIntLit(_, _) => false
      case PFloatLit(_) => false
      case PNilLit() => false
      case PStringLit(_) => false
      case PCompositeLit(typ, _) => s"$typ".split('.').tail.exists(isPrivateString)
      case PFunctionLit(id, _) => id.exists(s => isPrivateString(s.name))
      case PInvoke(base, args, _) => isPrivateExpTyp(base) || isPrivateVExp(args)
      case PSliceExp(base, low, high, cap) => isPrivateExp(base) || low.exists(isPrivateExp) || high.exists(isPrivateExp) || cap.exists(isPrivateExp)
      case PMake(typ, args) => isPrivateTyp(typ) || isPrivateVExp(args)
      case PBlankIdentifier() => false
      case PPredConstructor(id, _) => isPrivateString(id.id.name)
      case _ => isPrivateChild(expr)
    }
    case expr: PGhostExpression => expr match {
      case PForall(_, triggers, body) => triggers.exists(t => t.exps.exists(isPrivateExp)) || isPrivateExp(body)
      case PExists(_, triggers, body) => triggers.exists(t => t.exps.exists(isPrivateExp)) || isPrivateExp(body)
      case PMatchExp(exp, clauses) => isPrivateExp(exp) || isPrivateVExp(clauses.map(c => c.exp))
      case expr : PGhostCollectionExp => expr match {
        case PGhostCollectionUpdate(col, clauses) => isPrivateExp(col) || isPrivateVExp(clauses.map(c => c.left)) || isPrivateVExp(clauses.map(c => c.right))
        case _ => isPrivateChild(expr)
      }
      case _ => isPrivateChild(expr)
    }
    case t => Violation.violation(s"Unexpected Expression in isPrivateExp, got $t")
  }

  private def isPrivateVExp(v: Vector[PExpression]): Boolean = v.exists(arg => isPrivateExp(arg))
  private def isPrivateVParam(v: Vector[PParameter]): Boolean = v.exists(p => isPrivateTyp(p.typ))
  def isPrivateString(s: String): Boolean = s.charAt(0).isLower

  private def isPrivateChild(n: PNode): Boolean = 
    tree.child(n).exists {
      case e if e.isInstanceOf[PExpression] => isPrivateExp(e.asInstanceOf[PExpression])
      case t if t.isInstanceOf[PType] => isPrivateTyp(t.asInstanceOf[PType])
      case i if i.isInstanceOf[PIdnNode] => isPrivateIdentifier(i.asInstanceOf[PIdnNode])
      case _ => false
    }

  def getAndcheckIfMemberPrivateFromSpec(spec: PSpecification): (Option[PFunctionOrMethodDecl], Boolean) = {
    val func = tryEnclosingFunctionOrMethod(spec)
    (func, if (func.isEmpty) false else isPrivateIdentifier(func.get.id))
  }
}