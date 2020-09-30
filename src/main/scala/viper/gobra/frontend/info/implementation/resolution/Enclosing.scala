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

trait Enclosing { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  import decorators._

  lazy val enclosingScope: PNode => PScope =
    down((_: PNode) => violation("node does not root in a scope")) { case s: PScope => s }

  def enclosingIdScope(id: PIdnNode): PScope = enclosingScope(entity(id) match {
    case r: Regular => r.rep
    case _ => id
  })

  lazy val enclosingCodeRootWithResult: PStatement => PCodeRootWithResult =
    down((_: PNode) => violation("Statement does not root in a CodeRoot")) { case m: PCodeRootWithResult => m }

  lazy val enclosingCodeRoot: PNode => PCodeRoot with PScope =
    down((_: PNode) => violation("Statement does not root in a CodeRoot")) { case m: PCodeRoot with PScope => m }

  lazy val isEnclosingExplicitGhost: PNode => Boolean =
    down(false){ case g: PGhostifier[_] => true }

  def typeSwitchConstraints(id: PIdnNode): Vector[PType] =
    typeSwitchConstraintsLookup(id)(id)

  private lazy val typeSwitchConstraintsLookup: PIdnNode => PNode => Vector[PType] =
    paramAttr[PIdnNode, PNode, Vector[PType]] { id => {
      case tree.parent.pair(PTypeSwitchCase(left, _), s: PTypeSwitchStmt)
        if s.binder.exists(_.name == id.name) => left

      case s: PTypeSwitchStmt // Default case
        if s.binder.exists(_.name == id.name) => Vector.empty

      case tree.parent(p) => typeSwitchConstraintsLookup(id)(p)
    }}

  def containedIn(n: PNode, s: PNode): Boolean = contained(n)(s)

  private lazy val contained: PNode => PNode => Boolean =
    paramAttr[PNode, PNode, Boolean] { l => r => l match {
      case `r` => true
      case tree.parent(p) => contained(p)(r)
      case _ => false
    }}


  def nilType(nil: PNilLit): Option[Type] = {

    def aux(n: PNode): Option[Type] = {
      n match {
        case tree.parent(p) => p match {
          case PConstDecl(t, _, _) => t.map(typ)
          case PVarDecl(t, _, _, _) => t.map(typ)
          case _: PExpressionStmt => None
          case PSendStmt(channel, `n`) => Some(typ(channel).asInstanceOf[Type.ChannelT].elem)
          case PAssignment(right, left) => Some(typ(left(right.indexOf(n))))
          case PShortVarDecl(right, left, _) => Some(typ(left(right.indexOf(n))))
            // no if statement
          case _: PExprSwitchStmt => None
            // no for stmt
            // no go stmt
          case p: PReturn => Some(typ(enclosingCodeRootWithResult(p).result.outs(p.exps.indexOf(n))))
            // no defer stmt
          case p: PExpCompositeVal => Some(expectedMiscType(p))
          case i: PInvoke => (exprOrType(i.base), resolve(i)) match {
            case (Right(target), Some(_: ap.Conversion)) => Some(typ(target))
            case (Left(callee), Some(p: ap.FunctionCall)) => Some(typ(callee).asInstanceOf[Type.FunctionT].args(p.args.indexOf(n)))
            case (Left(callee), Some(p: ap.PredicateCall)) => Some(typ(callee).asInstanceOf[Type.FunctionT].args(p.args.indexOf(n)))
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
          case PEquals(`n`, r) => val t = typ(r); if (t == Type.NilType) None else Some(t)
          case PEquals(l, `n`) => val t = typ(l); if (t == Type.NilType) None else Some(t)
          case PUnequals(`n`, r) => val t = typ(r); if (t == Type.NilType) None else Some(t)
          case PUnequals(l, `n`) => val t = typ(l); if (t == Type.NilType) None else Some(t)
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
          case PSequenceUpdateClause(left, `n`) => p match {
            case tree.parent(pp: PSequenceUpdate) => Some(typ(pp.seq).asInstanceOf[Type.SequenceT].elem)
          }
            // no range sequence
            // no union, intersection, set minus, subset, set conversion, multiset conversion
            // no trigger

          case _ => Violation.violation(s"Encountered unexpected parent of nil: $p")
        }
      }
    }

    aux(nil)
  }
}
