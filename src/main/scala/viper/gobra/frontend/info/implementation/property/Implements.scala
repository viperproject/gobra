// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.PExplicitGhostStructClause
import viper.gobra.frontend.info.base.SymbolTable.{MPredicateSpec, Method}
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.base.Type.{GhostCollectionType, NilType, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait Implements { this: TypeInfoImpl =>

  def implements(l: Type, r: Type): PropertyResult = underlyingType(r) match {
    case itf: Type.InterfaceT =>
      val valid = syntaxImplements(l, r)
      if (valid.holds && l != NilType) {
        _requiredImplements ++= Set((l, itf))
      }
      valid

    case _ => errorProp()
  }

  private var _requiredImplements: Set[(Type, Type.InterfaceT)] = Set.empty
  def requiredImplements: Set[(Type, Type.InterfaceT)] = _requiredImplements

  def syntaxImplements(l: Type, r: Type): PropertyResult = (l, underlyingType(r)) match {
    case (NilType, _: Type.InterfaceT) => successProp
    case (_, _: Type.InterfaceT) =>
      supportedSortForInterfaces(l) and {
        val itfMemberSet = memberSet(r)
        val implMemberSet = memberSet(l)
        val counterReasons = itfMemberSet.flatMap{ case (name, (itfMember, _)) =>
          itfMember match {
            case _: MPredicateSpec => Vector.empty // an implementing type does not have to implement all predicates
            case _ =>
              implMemberSet.lookup(name) match {
                case None => Vector(s"$l has no member with name $name")
                case Some(implMember) =>
                  // all other members must have an identical signature
                  if (!identicalTypes(memberType(itfMember), memberType(implMember))) {
                    Vector(s"The member $name has a different signature for implementation and interface")
                  } else if (implMember.ghost != itfMember.ghost) {
                    Vector(s"For member $name, the 'ghost' declaration for implementation and interface does not match")
                  } else if ({
                    (implMember, itfMember) match {
                      case (implMember: Method, itfMember: Method) => implMember.isPure != itfMember.isPure
                      case _ => false
                    }
                  }) {
                    Vector(s"For member $name, the 'pure' annotation for implementation and interface does not match")
                  } else {
                    Vector.empty
                  }
              }
          }
        }
        failedPropFromMessages(counterReasons)
      }

    case _ => failedProp(s"$r is not an interface")
  }

  /** Returns true if the type is supported for interfaces. All finite types are supported. */
  def supportedSortForInterfaces(t: Type): PropertyResult = {
    failedProp(s"The type $t is not supported for interface", !isIdentityPreservingType(t))
  }

  /** Returns whether values of type 't' satisfy that [x] == [y] in Viper implies x == y in Gobra. */
  private def isIdentityPreservingType(t: Type, encounteredTypes: Set[Type] = Set.empty): Boolean = {
    if (encounteredTypes contains t) {
      true
    } else {
      def go(t: Type): Boolean = isIdentityPreservingType(t, encounteredTypes + t)
      underlyingType(t) match {
        case Type.NilType | Type.BooleanT | _: Type.IntT => true
        case ut: Type.PointerT => go(ut.elem)
        case ut: Type.StructT =>
          ut.decl.clauses.forall(!_.isInstanceOf[PExplicitGhostStructClause]) &&
            ut.clauses.forall{ case (_, (_, fieldType)) => go(fieldType) }
        case ut: Type.ArrayT => go(ut.elem)
        case _: Type.SliceT => true
        case ut: Type.OptionT => go(ut.elem)
        case ut: GhostCollectionType => go(ut.elem)
        case _ => false
      }
    }

  }

}
