package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
import viper.gobra.frontend.info.base.{SymbolTable, Type}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl


trait MemberResolution { this: TypeInfoImpl =>

  import scala.collection.breakOut

  private def createField(decl: PFieldDecl): Field =
    defEntity(decl.id).asInstanceOf[Field]

  private def createEmbbed(decl: PEmbeddedDecl): Embbed =
    defEntity(decl.id).asInstanceOf[Embbed]

  private def createMethodImpl(decl: PMethodDecl): MethodImpl =
    defEntity(decl.id).asInstanceOf[MethodImpl]

  private def createMethodSpec(spec: PMethodSig): MethodSpec =
    defEntity(spec.id).asInstanceOf[MethodSpec]


  private lazy val receiverMethodSetMap: Map[Type, MemberSet] = {
    tree.root.declarations
      .collect { case m: PMethodDecl => createMethodImpl(m) }(breakOut)
      .groupBy { m: MethodImpl => miscType(m.decl.receiver) }
      .mapValues(ms => MemberSet.init(ms))
  }

  def receiverMethodSet(recv: Type): MemberSet =
    receiverMethodSetMap.getOrElse(recv, MemberSet.empty)

  lazy val interfaceMethodSet: InterfaceT => MemberSet =
    attr[InterfaceT, MemberSet] {
      case InterfaceT(PInterfaceType(es, specs)) =>
        MemberSet.init(specs.map(m => createMethodSpec(m))) union MemberSet.union {
          es.map(e => interfaceMethodSet(
            entity(e.typ.id) match {
              case NamedType(PTypeDef(t: PInterfaceType, _), _) => InterfaceT(t)
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
        MemberSet.init(fields map createField) union MemberSet.init(es map createEmbbed) union
          MemberSet.union(es.map { e => memberSet(transformer(miscType(e.typ))).promote(createEmbbed(e)) })

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

  override def memberLookup(t: Type.Type, id: PIdnUse): (SymbolTable.TypeMember, Vector[MemberPath]) = memberSet(t).lookupWithPath(id.name).get
  override def selectionLookup(t: Type.Type, id: PIdnUse): (SymbolTable.TypeMember, Vector[MemberPath]) = selectionSet(t).lookupWithPath(id.name).get

  def calleeEntity(callee: PExpression): Option[Regular] = callee match {
    case PNamedOperand(id)     => Some(regular(id))
    case PMethodExpr(base, id) => Some(findSelection(typeType(base), id).get)
    case PSelection(base, id)  => Some(findSelection(exprType(base), id).get)
    case n: PSelectionOrMethodExpr => resolveSelectionOrMethodExpr(n){
      case (base, id) => findSelection(idType(base), id).get // selection
    } {
      case (base, id) => findSelection(idType(base), id).get // methodExpr
    }
    case _ => None
  }

  def isCalleeMethodExpr(callee: PExpression): Boolean = callee match {
    case PMethodExpr(base, id) => true
    case n: PSelectionOrMethodExpr => resolveSelectionOrMethodExpr(n){
      case (base, id) => false // selection
    } {
      case (base, id) => true // methodExpr
    }.get
    case _ => false
  }
}
