// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.{SymbolTable, Type}
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation

import scala.annotation.tailrec

trait Enclosing { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  import decorators._

  lazy val enclosingScope: PNode => PScope =
    down((_: PNode) => violation("node does not root in a scope")) { case s: PScope => s }

  def enclosingIdScope(id: PIdnNode): PScope = enclosingScope(entity(id) match {
    case r: Regular => r.rep
    case _ => id
  })


  lazy val enclosingLoopUntilOutline: PNode => Either[Option[PNode], PGeneralForStmt] = {
    down[Either[Option[PNode], PGeneralForStmt]](Left(None)){
      case x: POutline => Left(Some(x))
      case x: PGeneralForStmt => Right(x)
    }
  }

  // Returns the enclosing loop that has a specific label
  def enclosingLabeledLoop(label: PLabelUse, node: PNode): Either[Option[PNode], PGeneralForStmt] = {
    enclosingLoopUntilOutline(node) match {
      case Right(encLoop) => encLoop match {
        case tree.parent(l: PLabeledStmt) if l.label.name == label.name => Right(encLoop)
        case tree.parent(p) => enclosingLabeledLoop(label, p)
        case _ => Left(None)
      }
      case r => r
    }
  }

  def enclosingInvariant(n: PExpression) : PExpression = {
    n match {
      case tree.parent(p) if enclosingExpr(p).isDefined => enclosingExpr(p).get
      case _ => n
    }
  }

  lazy val tryEnclosingPackage: PNode => Option[PPackage] =
    down[Option[PPackage]](None) { case x: PPackage => Some(x) }

  lazy val tryEnclosingUnorderedScope: PNode => Option[PUnorderedScope] =
    down[Option[PUnorderedScope]](None) { case x: PUnorderedScope => Some(x) }

  lazy val enclosingCodeRootWithResult: PStatement => PCodeRootWithResult =
    down((_: PNode) => violation("Statement does not root in a CodeRoot")) { case m: PCodeRootWithResult => m }

  lazy val tryEnclosingCodeRootWithResult: PStatement => Option[PCodeRootWithResult] =
    down[Option[PCodeRootWithResult]](None) { case m: PCodeRootWithResult => Some(m) }

  lazy val tryEnclosingFunction: PNode => Option[PFunctionDecl] =
    down[Option[PFunctionDecl]](None) { case m: PFunctionDecl => Some(m) }

  lazy val tryEnclosingFunctionOrMethod: PNode => Option[PFunctionOrMethodDecl] =
    down[Option[PFunctionOrMethodDecl]](None) { case f: PFunctionOrMethodDecl => Some(f) }

  lazy val tryEnclosingClosureImplementationProof: PNode => Option[PClosureImplProof] =
    down[Option[PClosureImplProof]](None) { case m: PClosureImplProof => Some(m) }

  lazy val enclosingCodeRoot: PNode => PCodeRoot with PScope =
    down((_: PNode) => violation("Statement does not root in a CodeRoot")) { case m: PCodeRoot with PScope => m }

  lazy val tryEnclosingOutline: PNode => Option[POutline] =
    down[Option[POutline]](None) { case x: POutline => Some(x) }

  lazy val tryEnclosingGlobalVarDeclaration: PNode => Option[PVarDecl] =
    down[Option[PVarDecl]](None) {
      case x: PVarDecl if isGlobalVarDeclaration(x) => Some(x)
    }

  lazy val isEnclosingGhost: PNode => Boolean =
    down(false){ case _: PGhostifier[_] | _: PGhostNode => true }

  // Returns true iff n occurs in an init() function, or a function marked with
  // 'mayInit' or in the rhs of a global variable declaration.
  def isEnclosingMayInit(n: PNode): Boolean = {
    val cond1 = tryEnclosingFunctionOrMethod(n) match {
      case Some(f: PFunctionDecl) => f.id.name == "init" || f.spec.mayBeUsedInInit
      case Some(m: PMethodDecl) => m.spec.mayBeUsedInInit
      case _ => false
    }
    val cond2 = tryEnclosingGlobalVarDeclaration(n).isDefined
    cond1 || cond2
  }

  def isGlobalVarDeclaration(n: PVarDecl): Boolean =
    enclosingCodeRoot(n).isInstanceOf[PPackage]

  lazy val tryEnclosingFunctionLit: PNode => Option[PFunctionLit] =
    down[Option[PFunctionLit]](None) { case x: PFunctionLit => Some(x) }

  lazy val enclosingExpr: PNode => Option[PExpression] =
    down[Option[PExpression]](None) { case x: PExpression => Some(x) }

  lazy val enclosingStruct: PNode => Option[PStructType] =
    down[Option[PStructType]](None) { case x: PStructType => Some(x) }

  lazy val enclosingPConstBlock: PNode => Option[PConstDecl] =
    down[Option[PConstDecl]](None) { case x: PConstDecl => Some(x) }

  lazy val enclosingPConstDecl: PNode => Option[PConstSpec] =
    down[Option[PConstSpec]](None) { case x: PConstSpec => Some(x) }

  def typeSwitchConstraints(id: PIdnNode): Vector[PExpressionOrType] =
    typeSwitchConstraintsLookup(id)(id)

  private lazy val typeSwitchConstraintsLookup: PIdnNode => PNode => Vector[PExpressionOrType] =
    paramAttr[PIdnNode, PNode, Vector[PExpressionOrType]] { id => {
      case tree.parent.pair(PTypeSwitchCase(left, _), s: PTypeSwitchStmt)
        if s.binder.exists(_.name == id.name) => left

      case s: PTypeSwitchStmt // Default case
        if s.binder.exists(_.name == id.name) => Vector.empty

      case tree.parent(p) => typeSwitchConstraintsLookup(id)(p)
      case n => Violation.violation(s"every node, except the root, must have a parent: $n")
    }}

  def containedIn(n: PNode, s: PNode): Boolean = contained(n)(s)

  private lazy val contained: PNode => PNode => Boolean =
    paramAttr[PNode, PNode, Boolean] { l => r => l match {
      case `r` => true
      case tree.parent(p) => contained(p)(r)
      case _ => false
    }}


  def nilType(nil: PNilLit): Option[Type] = {

    @tailrec
    def aux(n: PNode): Option[Type] = {
      n match {
        case tree.parent(p) => p match {
          case PConstSpec(t, _, _) => t.map(symbType)
          case PVarDecl(t, _, _, _) => t.map(symbType)
          case _: PExpressionStmt => None
          case PSendStmt(channel, `n`) => Some(typ(channel).asInstanceOf[Type.ChannelT].elem)
          case PAssignment(right, left) => Some(typ(left(right.indexOf(n))))
          case PShortVarDecl(right, left, _) => Some(typ(left(right.indexOf(n))))
            // no if statement
          case _: PExprSwitchStmt => None
          case s: PTypeSwitchCase => s match {
            case tree.parent(p) => p match {
              case switch: PTypeSwitchStmt => Some(typ(switch.exp))
              case c => violation(s"The parent of a type-switch case should always be a switch statement, but got $c")
            }
            case c => violation(s"The parent of a type-switch case should always be a switch statement, but got $c")
          }
            // no for stmt
            // no go stmt
          case p: PReturn => Some(typ(enclosingCodeRootWithResult(p).result.outs(p.exps.indexOf(n))))
            // no defer stmt
          case p: PExpCompositeVal => Some(expectedMiscType(p))
          case i: PInvoke => (exprOrType(i.base), resolve(i)) match {
            case (Right(target), Some(_: ap.Conversion)) => Some(symbType(target))
            case (Left(callee), Some(p: ap.FunctionCall)) => Some(typ(callee).asInstanceOf[Type.FunctionT].args(p.args.indexOf(n)))
            case (Left(callee), Some(p: ap.PredicateCall)) => Some(typ(callee).asInstanceOf[Type.FunctionT].args(p.args.indexOf(n)))
            case c => Violation.violation(s"This case should be unreachable, but got $c")
          }
            // no not
          case PIndexedExp(base, `n`) => Some(typ(base).asInstanceOf[Type.MapT].key)
            // no length
            // no capacity
            // no slice exp
            // no type assertion
            // no receive
            // no reference
            // no deref
            // no negation
          case PEquals(`n`, r) => val t = exprOrTypeType(r); if (t == Type.NilType) None else Some(t)
          case PEquals(l, `n`) => val t = exprOrTypeType(l); if (t == Type.NilType) None else Some(t)
          case PUnequals(`n`, r) => val t = exprOrTypeType(r); if (t == Type.NilType) None else Some(t)
          case PUnequals(l, `n`) => val t = exprOrTypeType(l); if (t == Type.NilType) None else Some(t)
            // no and, or, less, at most, greater, at least, add, sub, mul, mod, div
          case p: PUnfolding => aux(p)
            // no array type
            // no range
            // no function spec, no invariants, no predicate body
            // no assert, assume, exhale, inhale
          case p: POld => aux(p)
          case p: PLabeledOld => aux(p)
          case p: PBefore => aux(p)
          case p: PConditional => val t = typ(p); if (t == Type.NilType) None else Some(t)
            // no implication, access
            // no forall or exists body
          case PElem(`n`, s) => Some(typ(s).asInstanceOf[Type.GhostCollectionType].elem)
          case PMultiplicity(`n`, s) => Some(typ(s).asInstanceOf[Type.GhostCollectionType].elem)
            // no cardinality
            // no sequence append, sequence conversion
          case PGhostCollectionUpdateClause(_, `n`) => p match {
            case tree.parent(pp: PGhostCollectionUpdate) => Some(typ(pp.col).asInstanceOf[Type.SequenceT].elem)
            case c => Violation.violation(s"Only the root has not parent, but got $c")
          }
            // no range sequence
            // no union, intersection, set minus, subset, set conversion, multiset conversion
            // no trigger

          case _ => Violation.violation(s"Encountered unexpected parent of nil: $p")
        }
        case c => Violation.violation(s"Only the root has no parent, but got $c")
      }
    }

    aux(nil)
  }

  override def freeVariables(n: PNode): Vector[PIdnNode] = freeVariablesAttr(n)
  private lazy val freeVariablesAttr: PNode => Vector[PIdnNode] = {
    def free(x: PIdnNode, scope: PNode): Boolean = entity(x) match {
      case r: SymbolTable.SingleLocalVariable => !containedIn(enclosingScope(r.rep), scope)
      case r: SymbolTable.MultiLocalVariable  => !containedIn(enclosingScope(r.rep), scope)
      case _: SymbolTable.InParameter         => true
      case _: SymbolTable.ReceiverParameter   => true
      case _: SymbolTable.OutParameter        => true
      case _ => false
    }

    attr[PNode, Vector[PIdnNode]] { node =>
      allChildren(node).collect{ case x: PIdnNode if free(x, node) => x }.distinctBy(_.name)
    }
  }

  override def freeDeclared(n: PNode): Vector[PIdnNode] = freeDeclaredAttr(n)
  private lazy val freeDeclaredAttr: PNode => Vector[PIdnNode] = {
    attr[PNode, Vector[PIdnNode]] { node =>
      val allDeclared = allChildren(node).collect[Vector[PIdnNode]] {
        case decl: PVarDecl => decl.left.collect{ case id: PIdnDef => id }
        case decl: PShortVarDecl => decl.left.collect { case id: PIdnUnk if isDef(id) => id }
      }.flatten.distinctBy(_.name)

      freeVariables(node).filter(l => allDeclared.exists(r => l.name == r.name))
    }
  }

  override def freeModified(n: PNode): Vector[PIdnNode] = freeModifiedAttr(n)
  private lazy val freeModifiedAttr: PNode => Vector[PIdnNode] = {
    def modified(ass: PAssignee): Option[PIdnNode] = {
      ass match {
        case PNamedOperand(id) => Some(id)
        case PDot(_, id) => Some(id)
        case _ => None
      }
    }

    attr[PNode, Vector[PIdnNode]] { node =>
      val allModified = (allChildren(node).collect[Vector[PIdnNode]] {
        case ass: PAssignment => ass.left.flatMap(modified)
        case ass: PAssignmentWithOp => modified(ass.left).toVector
        case ass: PShortVarDecl => ass.left.collect { case id: PIdnUnk if !isDef(id) => id }
      }.flatten ++ freeDeclared(node)).distinctBy(_.name) // free declarations also count as modifications
      freeVariables(node).filter(l => allModified.exists(r => l.name == r.name))
    }
  }

  override def capturedLocalVariables(decl: PClosureDecl): Vector[PIdnNode] =
    capturedVariablesAttr(tree.parent(decl).head.asInstanceOf[PFunctionLit])
  private lazy val capturedVariablesAttr: PFunctionLit => Vector[PIdnNode] = {
    def capturedVar(x: PIdnNode, lit: PFunctionLit): Boolean = entity(x) match {
      case _: SymbolTable.GlobalVariable => false
      case r: SymbolTable.Variable => !containedIn(enclosingScope(r.rep), lit)
      case _ => false
    }

    attr[PFunctionLit, Vector[PIdnNode]] { lit =>
      allChildren(lit.decl).collect{ case x: PIdnNode if capturedVar(x, lit) => x }.distinctBy(_.name)
    }
  }
}
