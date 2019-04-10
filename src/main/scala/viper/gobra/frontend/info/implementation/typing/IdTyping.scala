package viper.gobra.frontend.info.implementation.typing

import org.bitbucket.inkytonik.kiama.util.Messaging.{message, noMessages}
import org.bitbucket.inkytonik.kiama.util.{MultipleEntity, UnknownEntity}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait IdTyping extends BaseTyping { this: TypeInfoImpl =>

  import viper.gobra.util.Violation._

  implicit lazy val wellDefID: WellDefinedness[PIdnNode] = createWellDef {
    case tree.parent(_: PUncheckedUse) => noMessages
    case id => entity(id) match {
      case _: UnknownEntity => message(id, s"got unknown identifier $id")
      case _: MultipleEntity => message(id, s"got duplicate identifier $id")

      case SingleConstant(exp, opt) => message(id, s"variable $id is not defined", ! {
        opt.exists(wellDefType.valid) || (wellDefExpr.valid(exp) && Single.unapply(exprType(exp)).nonEmpty)
      })

      case MultiConstant(idx, exp) => message(id, s"variable $id is not defined", ! {
        wellDefExpr.valid(exp) && (exprType(exp) match {
          case Assign(InternalTupleT(ts)) if idx < ts.size => true
          case _ => false
        })
      })

      case SingleLocalVariable(exp, opt) => message(id, s"variable $id is not defined", ! {
        opt.exists(wellDefType.valid) || (wellDefExpr.valid(exp) && Single.unapply(exprType(exp)).nonEmpty)
      })

      case MultiLocalVariable(idx, exp) => message(id, s"variable $id is not defined", ! {
        wellDefExpr.valid(exp) && (exprType(exp) match {
          case Assign(InternalTupleT(ts)) if idx < ts.size => true
          case _ => false
        })
      })

      case Function(PFunctionDecl(_, args, r, _)) => message(id, s"variable $id is not defined", ! {
        args.forall(wellDefMisc.valid) && miscType.valid(r)
      })

      case NamedType(_) => noMessages

      case TypeAlias(PTypeAlias(right, _)) => message(id, s"variable $id is not defined", ! {
        wellDefType.valid(right)
      })

      case InParameter(p) => message(id, s"variable $id is not defined", ! {
        wellDefType.valid(p.typ)
      })

      case ReceiverParameter(p) => message(id, s"variable $id is not defined", ! {
        wellDefType.valid(p.typ)
      })

      case OutParameter(p) => message(id, s"variable $id is not defined",! {
        wellDefType.valid(p.typ)
      })

      case TypeSwitchVariable(decl) => message(id, s"variable $id is not defined", ! {
        val constraints = typeSwitchConstraints(id)
        if (constraints.size == 1) wellDefType.valid(constraints.head) else wellDefExpr.valid(decl.exp)
      })

      case RangeVariable(idx, range) => message(id, s"variable $id is not defined",! {
        miscType(range) match {
          case Assign(InternalTupleT(ts)) if idx < ts.size => true
          case t => false
        }
      })

      case Field(PFieldDecl(_, typ)) => message(id, s"variable $id is not defined", ! {
        wellDefType.valid(typ)
      })

      case Embbed(PEmbeddedDecl(_, id)) => message(id, s"variable $id is not defined", ! {
        wellDefID.valid(id)
      })

      case _: MethodImpl => noMessages // not typed

      case _ => violation("untypable")
    }
  }

  lazy val idType: Typing[PIdnNode] = createTyping { id =>
    entity(id) match {

      case SingleConstant(exp, opt) => opt.map(typeType)
        .getOrElse(exprType(exp) match {
          case Single(t) => t
          case t => violation(s"expected single Type but got $t")
        })

      case MultiConstant(idx, exp) => exprType(exp) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
        case t => violation(s"expected tuple but got $t")
      }

      case SingleLocalVariable(exp, opt) => opt.map(typeType)
        .getOrElse(exprType(exp) match {
          case Single(t) => t
          case t => violation(s"expected single Type but got $t")
        })

      case MultiLocalVariable(idx, exp) => exprType(exp) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
        case t => violation(s"expected tuple but got $t")
      }

      case Function(PFunctionDecl(_, args, r, _)) =>
        FunctionT(args map miscType, miscType(r))

      case NamedType(decl) => DeclaredT(decl)
      case TypeAlias(PTypeAlias(right, _)) => typeType(right)

      case InParameter(p) => typeType(p.typ)

      case ReceiverParameter(p) => typeType(p.typ)

      case OutParameter(p) => typeType(p.typ)

      case TypeSwitchVariable(decl) =>
        val constraints = typeSwitchConstraints(id)
        if (constraints.size == 1) typeType(constraints.head) else exprType(decl.exp)

      case RangeVariable(idx, range) => miscType(range) match {
        case Assign(InternalTupleT(ts)) if idx < ts.size => ts(idx)
        case t => violation(s"expected tuple but got $t")
      }

      case Field(PFieldDecl(_, typ)) => typeType(typ)

      case Embbed(PEmbeddedDecl(_, id)) => idType(id)

      case _ => violation("untypable")
    }
  }
}
