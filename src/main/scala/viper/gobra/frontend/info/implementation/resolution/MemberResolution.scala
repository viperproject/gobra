package viper.gobra.frontend.info.implementation.resolution

import viper.gobra.ast.frontend._
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

  private lazy val receiverMethodSetMap2: Map[Type, AdvancedMemberSet[Method]] = {
    tree.root.declarations
      .collect { case m: PMethodDecl => createMethodImpl(m) }(breakOut)
      .groupBy { m: MethodImpl => miscType(m.decl.receiver) }
      .mapValues(ms => AdvancedMemberSet.init(ms))
  }

  def receiverMethodSet2(recv: Type): AdvancedMemberSet[Method] =
    receiverMethodSetMap2.getOrElse(recv, AdvancedMemberSet.empty)

  lazy val interfaceMethodSet2: InterfaceT => AdvancedMemberSet[Method] =
    attr[InterfaceT, AdvancedMemberSet[Method]] {
      case InterfaceT(PInterfaceType(es, specs)) =>
        AdvancedMemberSet.init[Method](specs.map(m => createMethodSpec(m))) union AdvancedMemberSet.union {
          es.map(e => interfaceMethodSet2(
            entity(e.typ.id) match {
              case NamedType(PTypeDef(t: PInterfaceType, _), _) => InterfaceT(t)
            }
          ))
        }
    }


  private def pastPromotions[M <: TypeMember](cont: Type => AdvancedMemberSet[M]): Type => AdvancedMemberSet[M] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[M] = attr[Type, AdvancedMemberSet[M]] {

      case DeclaredT(decl) => go(pastDeref)(typeType(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case StructT(t) =>
        AdvancedMemberSet.union(t.embedded map { e =>
          val et = miscType(e.typ)
          (cont(et) union go(pastDeref = false)(et)).promote(createEmbbed(e))
        })

      case _ => AdvancedMemberSet.empty
    }

    go(pastDeref = false)
  }

  private val fieldSuffix: Type => AdvancedMemberSet[StructMember] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[StructMember] = attr[Type, AdvancedMemberSet[StructMember]] {

      case DeclaredT(decl) => go(pastDeref)(typeType(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case StructT(t) =>
        val (es, fs) = (t.embedded, t.fields)
        AdvancedMemberSet.init[StructMember](fs map createField) union AdvancedMemberSet.init(es map createEmbbed)

      case _ => AdvancedMemberSet.empty
    }

    go(pastDeref = false)
  }

  private val structMemberSet: Type => AdvancedMemberSet[StructMember] =
    attr[Type, AdvancedMemberSet[StructMember]] { t => fieldSuffix(t) union pastPromotions(fieldSuffix)(t) }

  private val pastPromotionsMethodSuffix: Type => AdvancedMemberSet[Method] =
    attr[Type, AdvancedMemberSet[Method]] {
      case t: InterfaceT => interfaceMethodSet2(t)
      case pt@ PointerT(t) => receiverMethodSet2(pt) union receiverMethodSet2(t).ref
      case t => receiverMethodSet2(t) union receiverMethodSet2(PointerT(t)).deref
    }

  private val nonAddressableMethodSet: Type => AdvancedMemberSet[Method] =
    attr[Type, AdvancedMemberSet[Method]] { t =>
      pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
        case pt@ PointerT(st) => receiverMethodSet2(pt) union receiverMethodSet2(st).ref
        case _ => receiverMethodSet2(t)
      })
    }

  private val addressableMethodSet: Type => AdvancedMemberSet[Method] =
    attr[Type, AdvancedMemberSet[Method]] { t =>
      pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
        case pt@ PointerT(st) => receiverMethodSet2(pt) union receiverMethodSet2(st).ref
        case _ => receiverMethodSet2(t) union receiverMethodSet2(PointerT(t)).deref
      })
    }

  override def fieldLookup(t: Type, id: PIdnUse): (StructMember, Vector[MemberPath]) =
    structMemberSet(t).lookupWithPath(id.name).get

  override def addressedMethodLookup(t: Type, id: PIdnUse): (Method, Vector[MemberPath]) =
    addressableMethodSet(t).lookupWithPath(id.name).get

  override def nonAddressedMethodLookup(t: Type, id: PIdnUse): (Method, Vector[MemberPath]) =
    nonAddressableMethodSet(t).lookupWithPath(id.name).get


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
