// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PInterfaceType, PTypeDef, AstPattern => ap}
import viper.gobra.frontend.info.base.SymbolTable.{MPredicateSpec, Method}
import viper.gobra.frontend.info.base.{Type, SymbolTable => st}
import viper.gobra.frontend.info.base.Type.{GhostCollectionType, NilType, Type}
import viper.gobra.frontend.info.implementation.TypeInfoImpl


trait Implements { this: TypeInfoImpl =>

  def implements(l: Type, r: Type): PropertyResult = underlyingType(r) match {
    case itf: Type.InterfaceT =>
      val valid = syntaxImplements(l, r)
      if (valid.holds && l != NilType && !itf.isEmpty) {
        _requiredImplements ++= Set((l, itf))
      }
      valid

    case _ => errorProp()
  }

  private var _requiredImplements: Set[(Type, Type.InterfaceT)] = Set.empty
  private var _guaranteedImplements: Set[(Type, Type.InterfaceT)] = Set.empty
  def localRequiredImplements: Set[(Type, Type.InterfaceT)] = _requiredImplements
  def localGuaranteedImplements: Set[(Type, Type.InterfaceT)] = _guaranteedImplements
  def addDemandedImplements(subT: Type, superT: Type): Unit = {
    underlyingType(superT) match {
      case itf: Type.InterfaceT if !itf.isEmpty && subT != NilType =>
        _requiredImplements ++= Set((subT, itf))
      case _ =>
    }
  }
  def addDemandedEmbeddedInterfaceImplements(itf: Type.InterfaceT): Unit = {
    itf.decl.embedded.foreach{ x => resolve(x.typ) match { // interface implements its embedded types
      case Some(ap.NamedType(_, st.NamedType(PTypeDef(int: PInterfaceType, _), _, context))) =>
        context.symbType(int) match {
          case embeddedItfT: Type.InterfaceT => _guaranteedImplements ++= Set((itf, embeddedItfT))
          case _ =>
        }
      case _ =>
    }}
  }

  override def interfaceImplementations: Map[Type.InterfaceT, Set[Type]] = {
    (localRequiredImplements ++ localGuaranteedImplements).groupMap(_._2)(_._1)
  }

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
                  } else if ({
                    (implMember, itfMember) match {
                      case (implMember: Method, itfMember: Method) => itfMember.isAtomic && !implMember.isAtomic
                      case _ => false
                    }
                  }){
                    Vector(s"The implementation of atomic method $name is not marked as atomic")
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

  /** Returns whether values of type 't' satisfy that [x] == [y] in Viper (using `TypeEncoding.equal`) <==> x == y in Go (using Go equality). */
  private def isIdentityPreservingType(t: Type, encounteredTypes: Set[Type] = Set.empty): Boolean = {
    if (encounteredTypes contains t) {
      true
    } else {
      def go(subT: Type): Boolean = isIdentityPreservingType(subT, encounteredTypes + t)
      underlyingType(t) match {
        case Type.NilType | Type.BooleanT | _: Type.IntT | Type.StringT => true
        case _: Type.PointerT => true
        case ut: Type.StructT =>
          // a struct with ghost fields or ghost embeddings is not identity preserving.
          // E.g., for `type S struct { val int, ghost gval int }`, `S{0, 0} == S{0, 42}` holds in Go (after erasing the ghost fields).
          ut.clauses.forall{ case (_, info) => !info.isGhost && go(info.typ) }
        case ut: Type.ArrayT => go(ut.elem)
        case _: Type.SliceT => true
        case _: Type.MapT => true
        case ut: Type.OptionT => go(ut.elem)
        case ut: Type.AdtT =>
          ut.clauses.forall(_.fields.forall(f => go(f._2)))
        case ut: Type.DomainT =>
          // check that all types (besides `ut` itself) occurring as input or output parameter types are identity preserving
          val inAndOutParams = ut.decl.funcs.flatMap(f => f.args ++ f.result.outs)
          inAndOutParams
            .map(param => ut.context.typ(param))
            .filter(_ != ut) // ignore the domain itself
            .forall(typ => go(typ))
        case ut: GhostCollectionType => go(ut.elem)
        case _: Type.InterfaceT => true
        case _ => false
      }
    }

  }

}
