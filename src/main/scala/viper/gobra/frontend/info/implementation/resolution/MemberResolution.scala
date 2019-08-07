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

  private def createMPredImpl(decl: PMPredicateDecl): MPredicateImpl =
    defEntity(decl.id).asInstanceOf[MPredicateImpl]

  private def createMPredSpec(spec: PMPredicateSig): MPredicateSpec =
    defEntity(spec.id).asInstanceOf[MPredicateSpec]

  private lazy val receiverMethodSetMap: Map[Type, AdvancedMemberSet[MethodLike]] = {
    tree.root.declarations
      .collect { case m: PMethodDecl => createMethodImpl(m) }(breakOut)
      .groupBy { m: MethodImpl => miscType(m.decl.receiver) }
      .mapValues(ms => AdvancedMemberSet.init(ms))
  }

  def receiverMethodSet(recv: Type): AdvancedMemberSet[MethodLike] =
    receiverMethodSetMap.getOrElse(recv, AdvancedMemberSet.empty)

  private lazy val receiverPredicateSetMap: Map[Type, AdvancedMemberSet[MethodLike]] = {
    tree.root.declarations
      .collect { case m: PMPredicateDecl => createMPredImpl(m) }(breakOut)
      .groupBy { m: MPredicateImpl => miscType(m.decl.receiver) }
      .mapValues(ms => AdvancedMemberSet.init(ms))
  }

  def receiverPredicateSet(recv: Type): AdvancedMemberSet[MethodLike] =
    receiverPredicateSetMap.getOrElse(recv, AdvancedMemberSet.empty)

  lazy val receiverSet: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] (t => receiverMethodSet(t) union receiverPredicateSet(t))

  lazy val interfaceMethodSet: InterfaceT => AdvancedMemberSet[MethodLike] =
    attr[InterfaceT, AdvancedMemberSet[MethodLike]] {
      case InterfaceT(PInterfaceType(es, methSpecs, predSpecs)) =>
        AdvancedMemberSet.init[MethodLike](methSpecs.map(m => createMethodSpec(m))) union
          AdvancedMemberSet.init[MethodLike](predSpecs.map(m => createMPredSpec(m))) union
          AdvancedMemberSet.union {
            es.map(e => interfaceMethodSet(
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

  val structMemberSet: Type => AdvancedMemberSet[StructMember] =
    attr[Type, AdvancedMemberSet[StructMember]] { t => fieldSuffix(t) union pastPromotions(fieldSuffix)(t) }

  private val pastPromotionsMethodSuffix: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] {
      case t: InterfaceT => interfaceMethodSet(t)
      case pt@ PointerT(t) => receiverSet(pt) union receiverSet(t).ref
      case t => receiverSet(t) union receiverSet(PointerT(t)).deref
    }

  val nonAddressableMethodSet: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] { t =>
      pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
        case pt@ PointerT(st) => receiverSet(pt) union receiverSet(st).ref
        case _ => receiverSet(t)
      })
    }

  val addressableMethodSet: Type => AdvancedMemberSet[MethodLike] =
    attr[Type, AdvancedMemberSet[MethodLike]] { t =>
      pastPromotions(pastPromotionsMethodSuffix)(t) union (t match {
        case pt@ PointerT(st) => receiverSet(pt) union receiverSet(st).ref
        case _ => receiverSet(t) union receiverSet(PointerT(t)).deref
      })
    }

  override def fieldLookup(t: Type, id: PIdnUse): (StructMember, Vector[MemberPath]) =
    structMemberSet(t).lookupWithPath(id.name).get

  override def addressedMethodLookup(t: Type, id: PIdnUse): (Method, Vector[MemberPath]) = {
    val (m, p) = addressableMethodSet(t).lookupWithPath(id.name).get
    (m.asInstanceOf[Method], p)
  }

  override def nonAddressedMethodLookup(t: Type, id: PIdnUse): (Method, Vector[MemberPath]) = {
    val (m, p) = nonAddressableMethodSet(t).lookupWithPath(id.name).get
    (m.asInstanceOf[Method], p)
  }

  def methodLookup(e: PExpression, id: PIdnUse): (Method, Vector[MemberPath]) = {
    val (m, p) =
      if (effAddressable(e)) addressableMethodSet(exprType(e)).lookupWithPath(id.name).get
      else nonAddressableMethodSet(exprType(e)).lookupWithPath(id.name).get

    (m.asInstanceOf[Method], p)
  }

  def methodLookup(e: PIdnNode, id: PIdnUse): (Method, Vector[MemberPath]) = {
    val (m, p) = addressableMethodSet(idType(e)).lookupWithPath(id.name).get
    (m.asInstanceOf[Method], p)
  }

  def methodLookup(e: Type, id: PIdnUse): (Method, Vector[MemberPath]) = {
    val (m, p) = nonAddressableMethodSet(e).lookupWithPath(id.name).get
    (m.asInstanceOf[Method], p)
  }

  def predicateLookup(e: PExpression, id: PIdnUse): (MPredicate, Vector[MemberPath]) = {
    val (m, p) =
      if (effAddressable(e)) addressableMethodSet(exprType(e)).lookupWithPath(id.name).get
      else nonAddressableMethodSet(exprType(e)).lookupWithPath(id.name).get

    (m.asInstanceOf[MPredicate], p)
  }

  def predicateLookup(e: PIdnNode, id: PIdnUse): (MPredicate, Vector[MemberPath]) = {
    val (m, p) = addressableMethodSet(idType(e)).lookupWithPath(id.name).get
    (m.asInstanceOf[MPredicate], p)
  }

  def predicateLookup(e: Type, id: PIdnUse): (MPredicate, Vector[MemberPath]) = {
    val (m, p) = nonAddressableMethodSet(e).lookupWithPath(id.name).get
    (m.asInstanceOf[MPredicate], p)
  }


  def findField(t: Type, id: PIdnUse): Option[StructMember] =
    structMemberSet(t).lookup(id.name)

  def findMethodLike(e: PExpression, id: PIdnUse): Option[MethodLike] =
    if (effAddressable(e)) addressableMethodSet(exprType(e)).lookup(id.name)
    else nonAddressableMethodSet(exprType(e)).lookup(id.name)

  def findMethodLike(e: PIdnNode, id: PIdnUse): Option[MethodLike] =
    addressableMethodSet(idType(e)).lookup(id.name)

  def findMethodLike(e: Type, id: PIdnUse): Option[MethodLike] =
    nonAddressableMethodSet(e).lookup(id.name)

  def findSelection(e: PExpression, id: PIdnUse): Option[TypeMember] = {
    val methOpt = findMethodLike(e, id)
    if (methOpt.isDefined) methOpt
    else findField(exprType(e), id)
  }

  def findSelection(t: PIdnNode, id: PIdnUse): Option[TypeMember] = {
    val methOpt = findMethodLike(t, id)
    if (methOpt.isDefined) methOpt
    else findField(idType(t), id)
  }

  def calleeEntity(callee: PExpression): Option[Regular] = callee match {
    case PNamedOperand(id)     => Some(regular(id))
    case PMethodExpr(base, id) => Some(findMethodLike(typeType(base), id).get)
    case PSelection(base, id)  => Some(findSelection(base, id).get)
    case n: PSelectionOrMethodExpr => resolveSelectionOrMethodExpr(n){
      case (base, id) => findSelection(base, id).get // selection
    } {
      case (base, id) => findMethodLike(idType(base), id).get // methodExpr
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
