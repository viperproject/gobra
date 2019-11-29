package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PDeclaredType, PEmbeddedName, PEmbeddedPointer, PEmbeddedType, PInterfaceType, PPointerType, PStructType, PType, PTypeDecl}
import viper.gobra.frontend.info.base.Type.{ChannelT, DeclaredT, FunctionT, InterfaceT, MapT, NilType, PointerT, Single, SliceT, StructT, Type}
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl

trait UnderlyingType { this: TypeInfoImpl =>

  lazy val underlyingType: Type => Type =
    attr[Type, Type] {
      case Single(DeclaredT(t: PTypeDecl)) => underlyingType(typeType(t.right))
      case t => t
    }

  lazy val underlyingTypeP: PType => Option[PType] =
    attr[PType, Option[PType]] {
      case PDeclaredType(t) => entity(t) match {
        case st.NamedType(decl, _) => underlyingTypeP(decl.right)
        case st.TypeAlias(decl, _) => underlyingTypeP(decl.right)
        case _ => None // type not defined
      }
      case t => Some(t)
    }

  lazy val underlyingTypePE: PEmbeddedType => Either[PEmbeddedPointer, Option[PType]] =
    attr[PEmbeddedType, Either[PEmbeddedPointer, Option[PType]]] {
      case PEmbeddedName(t) => Right(underlyingTypeP(t))
      case t: PEmbeddedPointer => Left(t)
    }


  lazy val derefType: Type => Option[Type] =
    attr[Type, Option[Type]] {
      case Single(DeclaredT(t: PTypeDecl)) => derefType(typeType(t.right))
      case Single(PointerT(elem)) => Some(elem)
      case _ => None
    }

  lazy val derefTypeP: PType => Option[PType] =
    attr[PType, Option[PType]] { t => underlyingTypeP(t) match {
      case Some(PPointerType(dt)) => Some(dt)
      case _ => None
    }}

  lazy val derefTypePE: PEmbeddedType => Option[PType] =
    attr[PEmbeddedType, Option[PType]] { t => underlyingTypePE(t) match {
      case Left(PEmbeddedPointer(dt)) => Some(dt)
      case Right(Some(PPointerType(dt))) => Some(dt)
      case _ => None
    }}


  lazy val isClassType: Property[Type] = createBinaryProperty("a class type") { t =>
      val relevantT = derefType(t) match {
        case Some(value) => underlyingType(value)
        case None => underlyingType(t)
      }
      relevantT.isInstanceOf[StructT]
    }

  lazy val isClassTypeP: Property[PType] = createBinaryProperty("a class type") { t =>
    val relevantT = derefTypeP(t) match {
      case Some(value) => underlyingTypeP(value)
      case None => underlyingTypeP(t)
    }

    relevantT match {
      case Some(_: PStructType) => true
      case _ => false
    }
  }

  lazy val isClassTypePE: Property[PEmbeddedType] = createBinaryProperty("a class type") { t =>
    val relevantT = derefTypePE(t) match {
      case Some(value) => Right(underlyingTypeP(value))
      case None => underlyingTypePE(t)
    }

    relevantT match {
      case Right(Some(_: PStructType)) => true
      case _ => false
    }
  }


  lazy val isInterfaceType: Property[Type] = createBinaryProperty("an interface type") { t =>
    underlyingType(t) match {
      case InterfaceT(decl) => true
      case _ => false
    }
  }

  lazy val isInterfaceTypeP: Property[PType] = createBinaryProperty("an interface type") { t =>
    underlyingTypeP(t) match {
      case Some(_: PInterfaceType) => true
      case _ => false
    }
  }

  lazy val isInterfaceTypePE: Property[PEmbeddedType] = createBinaryProperty("an interface type") { t =>
    underlyingTypePE(t) match {
      case Right(Some(_: PInterfaceType)) => true
      case _ => false
    }
  }


  lazy val isClassOrInterfaceType: Property[Type] = createBinaryProperty("a class or interface type"){
    t => isClassType(t) || isInterfaceType(t)
  }

  lazy val isClassOrInterfaceTypeP: Property[PType] = createBinaryProperty("a class or interface type"){
    t => isClassTypeP(t) || isInterfaceTypeP(t)
  }

  lazy val isClassOrInterfaceTypePE: Property[PEmbeddedType] = createBinaryProperty("a class or interface type"){
    t => isClassTypePE(t) || isInterfaceTypePE(t)
  }


  lazy val isPointerType: Property[Type] = createBinaryProperty(("is a pointer type")){ t =>
    underlyingType(t) match {
      case NilType => true
      case _: PointerT => true
      case _: SliceT => true
      case _: MapT => true
      case _: ChannelT => true
      case _: FunctionT => true
      case _ => false
    }
  }


  lazy val isNotPointerType: Property[Type] = createBinaryProperty("not a pointer type"){ t =>
    val relevantT = derefType(t) match {
      case Some(value) => underlyingType(value)
      case None => underlyingType(t)
    }
    !relevantT.isInstanceOf[PointerT]
  }

  lazy val isNotPointerTypeP: Property[PType] = createBinaryProperty("not a pointer type"){ t =>
    val relevantT = derefTypeP(t) match {
      case Some(value) => underlyingTypeP(value)
      case None => underlyingTypeP(t)
    }

    relevantT match {
      case Some(_: PPointerType) => false
      case _ => true
    }
  }

  lazy val isNotPointerTypePE: Property[PEmbeddedType] = createBinaryProperty("not a pointer type"){ t =>
    val relevantT = derefTypePE(t) match {
      case Some(value) => Right(underlyingTypeP(value))
      case None => underlyingTypePE(t)
    }

    relevantT match {
      case Right(Some(_: PPointerType)) => false
      case Left(_: PEmbeddedPointer) => false
      case _ => true
    }
  }

}
