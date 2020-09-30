// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{message, noMessages}
import org.bitbucket.inkytonik.kiama.util.{Entity, MultipleEntity, UnknownEntity}
import viper.gobra.ast.frontend.{PIdnNode, _}
import viper.gobra.frontend.info.ExternalTypeInfo
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait IdTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  implicit lazy val wellDefID: WellDefinedness[PIdnNode] = createWellDefWithValidityMessages {
    id => entity(id) match {
      case _: UnknownEntity => LocalMessages(message(id, s"got unknown identifier $id"))
      case _: MultipleEntity => LocalMessages(message(id, s"got duplicate identifier $id"))
      case ErrorMsgEntity(msg) => LocalMessages(msg) // use provided error message instead of creating an own one
      case entity: Regular if entity.context != this => LocalMessages(noMessages) // imported entities are assumed to be well-formed
      case entity: ActualRegular => wellDefActualRegular(entity, id)
      case entity: GhostRegular  => wellDefGhostRegular(entity, id)
    }
  }

  /**
    * Returns true if n or any of its children (in the current package) has an entity that is contained in the set e
    */
  private def isNotCyclic(n: PNode, e: Set[Entity]): ValidityMessages = LocalMessages(message(n, s"got cyclic structure starting at $n", n match {
    case n: PIdnNode => entity(n) match {
      case r if e.contains(r) => true
      // we do not follow the evaluation into different packages, because the other package cannot point back to the
      // current one as import cycles are not allowed:
      case SingleConstant(_, _, _, _, _, ctx) if this != ctx => false
      case r@SingleConstant(_, _, exp, _, _, _) => !isNotCyclic(exp, e + r).valid
      case _ => false
    }
    case _ => children(n).exists(!isNotCyclic(_, e).valid)
  }))

  private[typing] def wellDefActualRegular(r: ActualRegular, id: PIdnNode): ValidityMessages = r match {

    case SingleConstant(_, _, exp, opt, _, _) =>
      val cyclicMsg = isNotCyclic(exp, Set(r))
      if (cyclicMsg.valid) unsafeMessage(! {
        opt.exists(wellDefAndType.valid) || (wellDefAndExpr.valid(exp) && Single.unapply(exprType(exp)).nonEmpty)
      }) else cyclicMsg

    case SingleLocalVariable(exp, opt, _, _, _) => unsafeMessage(! {
      opt.exists(wellDefAndType.valid) || exp.exists(e => wellDefAndExpr.valid(e) && Single.unapply(exprType(e)).nonEmpty)
    })

    case MultiLocalVariable(idx, exp, _, _, _) => unsafeMessage(! {
      wellDefAndExpr.valid(exp) && (exprType(exp) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => true
        case _ => false
      })
    })

    case Function(PFunctionDecl(_, args, r, _, _), _, _) => unsafeMessage(! {
      args.forall(wellDefMisc.valid) && miscType.valid(r)
    })

    case NamedType(_, _, _) => LocalMessages(noMessages)

    case TypeAlias(PTypeAlias(right, _), _, _) => unsafeMessage(! {
      wellDefAndType.valid(right)
    })

    case InParameter(p, _, _, _) => unsafeMessage(! {
      wellDefAndType.valid(p.typ)
    })

    case ReceiverParameter(p, _, _, _) => unsafeMessage(! {
      wellDefAndType.valid(p.typ)
    })

    case OutParameter(p, _, _, _) => unsafeMessage(! {
      wellDefAndType.valid(p.typ)
    })

    case TypeSwitchVariable(decl, _, _, _) => unsafeMessage(! {
      val constraints = typeSwitchConstraints(id)
      if (constraints.size == 1) wellDefAndType.valid(constraints.head) else wellDefAndExpr.valid(decl.exp)
    })

    case RangeVariable(idx, range, _, _, _) => unsafeMessage(! {
      miscType(range) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => true
        case t => false
      }
    })

    case Field(PFieldDecl(_, typ), _, _) => unsafeMessage(! {
      wellDefAndType.valid(typ)
    })

    case Embbed(PEmbeddedDecl(_, fieldId), _, _) => unsafeMessage(! {
      wellDefID.valid(fieldId)
    })

    case _: MethodImpl => LocalMessages(noMessages) // not typed

    case _: Import => LocalMessages(noMessages)

    case _: Wildcard => LocalMessages(noMessages) // not typed

    case _ => violation("untypable")
  }

  lazy val idType: Typing[PIdnNode] = createTyping { id =>
    entity(id) match {
      case entity: ActualRegular => actualEntityType(entity, id)
      case entity: GhostRegular =>  ghostEntityType(entity, id)
    }
  }

  private[typing] def actualEntityType(entity: ActualRegular, id: PIdnNode): Type = entity match {

    case SingleConstant(_, _, exp, opt, _, context) => opt.map(context.typ)
      .getOrElse(context.typ(exp) match {
        case Single(t) => t
        case t => violation(s"expected single Type but got $t")
      })

    case SingleLocalVariable(exp, opt, _, _, context) => opt.map(context.typ)
      .getOrElse(context.typ(exp.get) match {
        case Single(t) => t
        case t => violation(s"expected single Type but got $t")
      })

    case MultiLocalVariable(idx, exp, _, _, context) => context.typ(exp) match {
      case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
      case t => violation(s"expected tuple but got $t")
    }

    case Function(PFunctionDecl(_, args, r, _, _), _, context) =>
      FunctionT(args map context.typ, context.typ(r))

    case NamedType(decl, _, context) => DeclaredT(decl, context)
    case TypeAlias(PTypeAlias(right, _), _, context) => context.typ(right)

    case InParameter(p, _, _, context) => context.typ(p.typ)

    case ReceiverParameter(p, _, _, context) => context.typ(p.typ)

    case OutParameter(p, _, _, context) => context.typ(p.typ)

    case TypeSwitchVariable(decl, _, _, context) =>
      val constraints = typeSwitchConstraints(id)
      if (constraints.size == 1) context.typ(constraints.head) else context.typ(decl.exp)

    case RangeVariable(idx, range, _, _, context) => context.typ(range) match {
      case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
      case t => violation(s"expected tuple but got $t")
    }

    case Field(PFieldDecl(_, typ), _, context) => context.typ(typ)

    case Embbed(PEmbeddedDecl(_, fieldId), _, context) => context.typ(fieldId)

    case Import(decl, _) => ImportT(decl)

    case _ => violation("untypable")
  }
}
