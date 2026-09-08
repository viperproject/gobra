// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2026 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, error, noMessages}
import viper.gobra.ast.frontend.{PIdnUse, PLiteralValue, PNode, PReceiver, PTypeDef}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.base.Type.StructT
import viper.gobra.frontend.info.base.SymbolTable.{AdtClause, AdtDestructor, AdtDiscriminator, Constant, DomainFunction, Embbed, ErrorMsgEntity, FPredicate, Field, Function, GlobalVariable, MPredicateImpl, MethodImpl, Regular}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

import scala.annotation.tailrec

trait Visibility { this: TypeInfoImpl =>

  /** Go's visibility rule: a name is exported iff its first character is an upper-case letter. */
  def isExportedName(name: String): Boolean = name.nonEmpty && name.head.isUpper

  /**
    * True iff `context` belongs to Gobra's implicitly imported `builtin` package, whose members
    * (e.g., `error`) are accessible from every package despite having non-exported names.
    */
  def isBuiltinContext(context: ExternalTypeInfo): Boolean = context.pkgName.name == "builtin"

  /**
    * True iff member `m` with name `name` is exported. ADT discriminators are generated
    * members named `is<Clause>`; they are considered exported iff their clause is exported.
    */
  def isEffectivelyExported(m: Regular, name: String): Boolean = m match {
    case _: AdtDiscriminator => isExportedName(name.stripPrefix("is"))
    case _ => isExportedName(name)
  }

  /**
    * True iff accessing member `m` under name `name` from this package violates Go's visibility
    * rules, i.e., `m` is declared in another (non-builtin) package and `name` is not exported.
    */
  def isForeignPrivateAccess(m: Regular, name: String): Boolean =
    m.context.getTypeInfo != this.getTypeInfo && !isBuiltinContext(m.context) && !isEffectivelyExported(m, name)

  /** True iff `m` is declared in another package than the one this type checker belongs to. */
  def isImportedMember(m: Regular): Boolean = m.context.getTypeInfo != this.getTypeInfo

  /**
    * Replaces `e` by an error entity if accessing it under name `id` from this package
    * violates Go's visibility rules; returns `e` unchanged otherwise.
    */
  def filterForeignPrivate(e: Entity, id: PIdnUse): Entity = e match {
    case m: Regular if isForeignPrivateAccess(m, id.name) =>
      ErrorMsgEntity(error(id, s"${id.name} is not exported by package ${m.context.pkgName.name}"))
    case _ => e
  }

  /** True iff the receiver's declared type has an exported name. */
  def hasExportedReceiver(recv: PReceiver): Boolean = isExportedName(recv.typ.typ.id.name)

  /**
    * True iff `n` (e.g., an interface method signature) occurs within a type declaration
    * with an exported name, and is thus part of the package's interface.
    */
  def isClientFacingInterfaceMember(n: PNode): Boolean = {
    @tailrec def go(cur: PNode): Boolean = tree.parent(cur).headOption match {
      case Some(td: PTypeDef) => isExportedName(td.left.name)
      case Some(p) => go(p)
      case None => false
    }
    go(n)
  }

  /** Package-level members that constitute part of a package's (spec) interface. Named types are
    * deliberately excluded: private types may occur in signatures of exported members. */
  private def isPackageLevelMember(m: Regular): Boolean = m match {
    case _: Function | _: MethodImpl | _: FPredicate | _: MPredicateImpl | _: Constant |
         _: GlobalVariable | _: Field | _: Embbed | _: DomainFunction |
         _: AdtClause | _: AdtDestructor | _: AdtDiscriminator => true
    case _ => false
  }

  /**
    * True iff `lit` is a positional (i.e., non-keyed) struct literal that assigns to non-exported
    * fields. Such a literal does not name those fields, but it does constrain them, and it reveals
    * how many of them there are. Keyed literals that mention a non-exported field are caught by
    * the reference check below, and keyed literals that do not -- like the empty literal -- leave
    * the non-exported fields at their default value and are unproblematic.
    */
  private def assignsToNonExportedFields(lit: PLiteralValue): Boolean =
    lit.elems.nonEmpty && lit.elems.forall(_.key.isEmpty) && (underlyingType(expectedMiscType(lit)) match {
      case s: StructT => (s.fields.keys ++ s.embedded.keys).exists(!isExportedName(_))
      case _ => false
    })

  /**
    * Returns an error for every reference within `root` to a non-exported member of this package,
    * and for every composite literal within `root` that assigns to non-exported fields.
    * Used to enforce that the client-facing parts of a package -- contracts of exported members,
    * bodies of fully-public (non-closed) predicates and pure functions, package invariants, and
    * friend-package assertions -- can be interpreted by importing packages.
    */
  def privateMemberReferences(root: PNode, where: String): Messages =
    allChildren(root).flatMap {
      case id: PIdnUse => entity(id) match {
        case m: Regular if isPackageLevelMember(m) && !isImportedMember(m) && !isEffectivelyExported(m, id.name) =>
          error(id, s"$where cannot reference ${id.name}, which is not exported")
        case _ => noMessages
      }
      case lit: PLiteralValue if assignsToNonExportedFields(lit) =>
        error(lit, s"$where cannot use a positional composite literal of a struct type with " +
          s"non-exported fields, as it assigns to them")
      case _ => noMessages
    }
}
