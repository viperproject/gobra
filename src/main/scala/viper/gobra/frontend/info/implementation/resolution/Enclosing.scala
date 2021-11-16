// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable.Regular
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.base.Type.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.ast.frontend.{AstPattern => ap}
import viper.gobra.util.Violation

import scala.annotation.tailrec
import viper.gobra.frontend.info.base.SymbolTable

trait Enclosing { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  import decorators._

  lazy val enclosingScope: PNode => PScope =
    down((_: PNode) => violation("node does not root in a scope")) { case s: PScope => s }

  def enclosingIdScope(id: PIdnNode): PScope = enclosingScope(entity(id) match {
    case r: Regular => r.rep
    case _ => id
  })

  lazy val tryEnclosingUnorderedScope: PNode => Option[PUnorderedScope] =
    down[Option[PUnorderedScope]](None) { case x: PUnorderedScope => Some(x) }

  lazy val enclosingCodeRootWithResult: PStatement => PCodeRootWithResult =
    down((_: PNode) => violation("Statement does not root in a CodeRoot")) { case m: PCodeRootWithResult => m }

  lazy val enclosingCodeRoot: PNode => PCodeRoot with PScope =
    down((_: PNode) => violation("Statement does not root in a CodeRoot")) { case m: PCodeRoot with PScope => m }

  lazy val isEnclosingExplicitGhost: PNode => Boolean =
    down(false){ case _: PGhostifier[_] => true }

  lazy val isEnclosingDomain: PNode => Boolean =
    down(false){ case _: PDomainType => true }

  lazy val enclosingInterface: PNode => PInterfaceType =
    down((_: PNode) => violation("Node does not root in an interface definition")) { case x: PInterfaceType => x }

  lazy val enclosingStruct: PNode => Option[PStructType] =
    down[Option[PStructType]](None) { case x: PStructType => Some(x) }

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
          case PConstDecl(t, _, _) => t.map(symbType)
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
          case p: PConditional => val t = typ(p); if (t == Type.NilType) None else Some(t)
            // no implication, access
            // no forall or exists body
          case PIn(`n`, s) => Some(typ(s).asInstanceOf[Type.GhostCollectionType].elem)
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

  // finds all used, modified and declared variables
  private lazy val variableAnalysis: Vector[PStatement] => (Set[PIdnNode], Set[PIdnNode], Set[PIdnNode]) =
    attr[Vector[PStatement], (Set[PIdnNode], Set[PIdnNode], Set[PIdnNode])] {stmts => 

      def idNodeInAssignee(ass: PAssignee) =
        allChildren(ass).find {
          case _: PIdnNode => true 
          case _ => false
        }

      val allModified = stmts.flatMap{s => s match {
        case PAssignment(_, left) => left.map(ass => idNodeInAssignee(ass))
        case PAssignmentWithOp(_, _, left) => Vector(idNodeInAssignee(left))
        case PShortVarDecl(_, left, _) => left.collect{case id: PIdnUnk if !isDef(id) => Some(id)}
        case _ => Vector.empty
      }}.collect{case Some(i: PIdnNode) => i}.toSet

      def isVariable(x: PIdnNode): Boolean = entity(x) match {
        case _: SymbolTable.SingleLocalVariable => true
        case _: SymbolTable.MultiLocalVariable => true
        case _: SymbolTable.InParameter => true
        case _: SymbolTable.ReceiverParameter => true
        case _: SymbolTable.OutParameter => true
        case _ => false
      }

      val allVariables = stmts.flatMap(s => 
        allChildren(s).collect{case x: PIdnNode if isVariable(x) => x}
      ).toSet

      val declared = allVariables.filter{
        case _: PIdnDef => true
        case unk: PIdnUnk => isDef(unk)
        case _ => false
      }

      val modified = allModified.filter(id => !declared.find(other => id.name == other.name).isDefined)

      val variables = allVariables.filter(id => 
        !declared.exists(other => other.name == id.name) && !modified.exists(other => other.name == id.name)
      )

      (variables, modified, declared)
    }

    def variables (s: Vector[PStatement]): Set[PIdnNode] =
      variableAnalysis(s)._1

    def modified (s: Vector[PStatement]): Set[PIdnNode] =
      variableAnalysis(s)._2

    def declared (s: Vector[PStatement]): Set[PIdnNode] =
      variableAnalysis(s)._3
}
