package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend.{PIdnUse, PInterfaceType, PMethodDecl, PTypeDef}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl


trait MemberResolution { this: TypeInfoImpl =>

  import scala.collection.breakOut

  private lazy val receiverMethodSetMap: Map[Type, MemberSet] = {
    tree.root.declarations
      .collect { case m: PMethodDecl => MethodImpl(m) }(breakOut)
      .groupBy { m: MethodImpl => miscType(m.decl.receiver) }
      .mapValues(ms => MemberSet.init(ms))
  }

  def receiverMethodSet(recv: Type): MemberSet =
    receiverMethodSetMap.getOrElse(recv, MemberSet.empty)

  lazy val interfaceMethodSet: InterfaceT => MemberSet =
    attr[InterfaceT, MemberSet] {
      case InterfaceT(PInterfaceType(es, specs)) =>
        MemberSet.init(specs.map(m => MethodSpec(m))) union MemberSet.union {
          es.map(e => interfaceMethodSet(
            regular(e.typ.id) match {
              case NamedType(PTypeDef(t: PInterfaceType, _)) => InterfaceT(t)
            }
          ))
        }
    }

  lazy val memberSet: Type => MemberSet =
    attr[Type, MemberSet] {
      case PointerT(t) => receiverMethodSet(PointerT(t)) union receiverMethodSet(t).ref union promotedMemberSetRef(t)
      case t => receiverMethodSet(t) union promotedMemberSet(t)
    }

  private def promotedMemberSetGen(transformer: Type => Type): Type => MemberSet = {
    lazy val rec: Type => MemberSet = promotedMemberSetGen(transformer)

    attr[Type, MemberSet] {

      case StructT(t) =>
        val (es, fields) = (t.embedded, t.fields)
        MemberSet.init(fields map Field) union MemberSet.init(es map Embbed) union
          MemberSet.union(es.map { e => memberSet(transformer(miscType(e.typ))).promote(Embbed(e)) })

      case DeclaredT(decl) => rec(typeType(decl.right)).surface
      case inf: InterfaceT => interfaceMethodSet(inf)

      case _ => MemberSet.empty
    }
  }

  lazy val promotedMemberSet: Type => MemberSet = promotedMemberSetGen(identity)

  lazy val promotedMemberSetRef: Type => MemberSet = {
    def maybeRef(t: Type): Type = t match {
      case _: PointerT => t
      case _ => PointerT(t)
    }

    promotedMemberSetGen(maybeRef)
  }

  lazy val selectionSet: Type => MemberSet =
    attr[Type, MemberSet] {
      case t: PointerT => memberSet(t)
      case t => memberSet(PointerT(t)).deref union promotedSelectionSet(t)
    }

  lazy val promotedSelectionSet: Type => MemberSet =
    attr[Type, MemberSet] {
      case DeclaredT(decl) => promotedSelectionSet(typeType(decl.right)).surface
      case PointerT(t: DeclaredT) => promotedMemberSetRef(t)
      case _ => MemberSet.empty
    }

  def findSelection(t: Type, id: PIdnUse): Option[TypeMember] = selectionSet(t).lookup(id.name)

  def findMember(t: Type, id: PIdnUse): Option[TypeMember] = memberSet(t).lookup(id.name)
}
