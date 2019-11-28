package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{message, noMessages}
import org.bitbucket.inkytonik.kiama.util.{MultipleEntity, UnknownEntity}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait IdTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  implicit lazy val wellDefID: WellDefinedness[PIdnNode] = createWellDefWithValidityMessages {
    case tree.parent(_: PUncheckedUse) => LocalMessages(noMessages)
    case id => entity(id) match {
      case _: UnknownEntity => LocalMessages(message(id, s"got unknown identifier $id"))
      case _: MultipleEntity => LocalMessages(message(id, s"got duplicate identifier $id"))
      case entity: ActualRegular => wellDefActualRegular(entity, id)
      case entity: GhostRegular  => wellDefGhostRegular(entity, id)
    }
  }

  private[typing] def wellDefActualRegular(entity: ActualRegular, id: PIdnNode): ValidityMessages = entity match {


    case SingleConstant(exp, opt, _) => unsafeMessage(! {
      opt.exists(wellDefType.valid) || (wellDefExpr.valid(exp) && Single.unapply(exprType(exp)).nonEmpty)
    })

    case MultiConstant(idx, exp, _) => unsafeMessage(! {
      wellDefExpr.valid(exp) && (exprType(exp) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => true
        case _ => false
      })
    })

    case SingleLocalVariable(exp, opt, _, _) => unsafeMessage(! {
      opt.exists(wellDefType.valid) || exp.exists(e => wellDefExpr.valid(e) && Single.unapply(exprType(e)).nonEmpty)
    })

    case MultiLocalVariable(idx, exp, _, _) => unsafeMessage(! {
      wellDefExpr.valid(exp) && (exprType(exp) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => true
        case _ => false
      })
    })

    case Function(PFunctionDecl(_, args, r, _, _), _) => unsafeMessage(! {
      args.forall(wellDefMisc.valid) && miscType.valid(r)
    })

    case NamedType(_, _) => LocalMessages(noMessages)

    case TypeAlias(PTypeAlias(right, _), _) => unsafeMessage(! {
      wellDefType.valid(right)
    })

    case InParameter(p, _, _) => unsafeMessage(! {
      wellDefType.valid(p.typ)
    })

    case ReceiverParameter(p, _, _) => unsafeMessage(! {
      wellDefType.valid(p.typ)
    })

    case OutParameter(p, _, _) => unsafeMessage(! {
      wellDefType.valid(p.typ)
    })

    case TypeSwitchVariable(decl, _, _) => unsafeMessage(! {
      val constraints = typeSwitchConstraints(id)
      if (constraints.size == 1) wellDefType.valid(constraints.head) else wellDefExpr.valid(decl.exp)
    })

    case RangeVariable(idx, range, _, _) => unsafeMessage(! {
      miscType(range) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => true
        case t => false
      }
    })

    case Field(PFieldDecl(_, typ), _) => unsafeMessage(! {
      wellDefType.valid(typ)
    })

    case Embbed(PEmbeddedDecl(_, id), _) => unsafeMessage(! {
      wellDefID.valid(id)
    })

    case _: MethodImpl => LocalMessages(noMessages) // not typed

    case _ => violation("untypable")
  }

  lazy val idType: Typing[PIdnNode] = createTyping { id =>
    entity(id) match {
      case entity: ActualRegular => actualEntityType(entity, id)
      case entity: GhostRegular =>  ghostEntityType(entity, id)
    }
  }

  private[typing] def actualEntityType(entity: ActualRegular, id: PIdnNode): Type = entity match {

    case SingleConstant(exp, opt, _) => opt.map(typeType)
      .getOrElse(exprType(exp) match {
        case Single(t) => t
        case t => violation(s"expected single Type but got $t")
      })

    case MultiConstant(idx, exp, _) => exprType(exp) match {
      case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
      case t => violation(s"expected tuple but got $t")
    }

    case SingleLocalVariable(exp, opt, _, _) => opt.map(typeType)
      .getOrElse(exprType(exp.get) match {
        case Single(t) => t
        case t => violation(s"expected single Type but got $t")
      })

    case MultiLocalVariable(idx, exp, _, _) => exprType(exp) match {
      case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
      case t => violation(s"expected tuple but got $t")
    }

    case Function(PFunctionDecl(_, args, r, _, _), _) =>
      FunctionT(args map miscType, miscType(r))

    case NamedType(decl, _) => DeclaredT(decl)
    case TypeAlias(PTypeAlias(right, _), _) => typeType(right)

    case InParameter(p, _, _) => typeType(p.typ)

    case ReceiverParameter(p, _, _) => typeType(p.typ)

    case OutParameter(p, _, _) => typeType(p.typ)

    case TypeSwitchVariable(decl, _, _) =>
      val constraints = typeSwitchConstraints(id)
      if (constraints.size == 1) typeType(constraints.head) else exprType(decl.exp)

    case RangeVariable(idx, range, _, _) => miscType(range) match {
      case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
      case t => violation(s"expected tuple but got $t")
    }

    case Field(PFieldDecl(_, typ), _) => typeType(typ)

    case Embbed(PEmbeddedDecl(_, id), _) => idType(id)

    case _ => violation("untypable")
  }
}
