package viper.gobra.frontend.info.implementation.resolution

import org.bitbucket.inkytonik.kiama.util.Entity
import org.bitbucket.inkytonik.kiama.util.Messaging.{Messages, message}
import viper.gobra.ast.frontend._
import viper.gobra.frontend.{PackageResolver, Parser}
import viper.gobra.frontend.info.{ExternalTypeInfo, Info}
import viper.gobra.frontend.info.base.SymbolTable._
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.reporting.{NotFoundError, VerifierError}


trait MemberResolution { this: TypeInfoImpl =>

  import scala.collection.breakOut

  override def createField(decl: PFieldDecl): Field =
    defEntity(decl.id).asInstanceOf[Field]

  override def createEmbbed(decl: PEmbeddedDecl): Embbed =
    defEntity(decl.id).asInstanceOf[Embbed]

  override def createMethodImpl(decl: PMethodDecl): MethodImpl =
    defEntity(decl.id).asInstanceOf[MethodImpl]

  override def createMethodSpec(spec: PMethodSig): MethodSpec =
    defEntity(spec.id).asInstanceOf[MethodSpec]

  override def createMPredImpl(decl: PMPredicateDecl): MPredicateImpl =
    defEntity(decl.id).asInstanceOf[MPredicateImpl]

  override def createMPredSpec(spec: PMPredicateSig): MPredicateSpec =
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
                case NamedType(PTypeDef(t: PInterfaceType, _), _, _) => InterfaceT(t)
              }
            ))
          }
    }


  private def pastPromotions[M <: TypeMember](cont: Type => AdvancedMemberSet[M]): Type => AdvancedMemberSet[M] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[M] = attr[Type, AdvancedMemberSet[M]] {

      case DeclaredT(decl, context) => go(pastDeref)(context.typ(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case s: StructT =>
        AdvancedMemberSet.union(s.decl.embedded map { e =>
          val et = miscType(e.typ)
          (cont(et) union go(pastDeref = false)(et)).promote(createEmbbed(e))
        })

      case _ => AdvancedMemberSet.empty
    }

    go(pastDeref = false)
  }

  private val fieldSuffix: Type => AdvancedMemberSet[StructMember] = {

    def go(pastDeref: Boolean): Type => AdvancedMemberSet[StructMember] = attr[Type, AdvancedMemberSet[StructMember]] {

      case DeclaredT(decl, context) => go(pastDeref)(context.typ(decl.right)).surface
      case PointerT(t) if !pastDeref => go(pastDeref = true)(t).ref

      case s: StructT =>
        val (es, fs) = (s.decl.embedded, s.decl.fields)
        AdvancedMemberSet.init[StructMember](fs map s.context.createField) union AdvancedMemberSet.init(es map s.context.createEmbbed)

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




  def tryFieldLookup(t: Type, id: PIdnUse): Option[(StructMember, Vector[MemberPath])] =
    structMemberSet(t).lookupWithPath(id.name)

  def tryMethodLikeLookup(e: PExpression, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])] = {
    if (effAddressable(e)) addressableMethodSet(exprType(e)).lookupWithPath(id.name)
    else nonAddressableMethodSet(exprType(e)).lookupWithPath(id.name)
  }

  def tryMethodLikeLookup(e: Type, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])] = {
    nonAddressableMethodSet(e).lookupWithPath(id.name)
  }

  def tryMethodLikeLookup(e: PType, id: PIdnUse): Option[(MethodLike, Vector[MemberPath])] = tryMethodLikeLookup(typeType(e), id)

  def tryPackageLookup(importedPkg: ImportT, id: PIdnUse): Option[(Entity, Vector[MemberPath])] = {
    def parseAndTypeCheck(importedPkg: ImportT): Either[Vector[VerifierError], ExternalTypeInfo] = {
      val pkgName = importedPkg.decl.pkg
      val pkgFiles = PackageResolver.resolve(pkgName, config.includeDirs)
      val res = for {
        nonEmptyPkgFiles <- if (pkgFiles.isEmpty)
          Left(Vector(NotFoundError(s"No source files for package '$pkgName' found")))
          else Right(pkgFiles)
        parsedProgram <- Parser.parse(nonEmptyPkgFiles, specOnly = true)(config)
        // TODO maybe don't check whole file but only members that are actually used/imported
        // By parsing only declarations and their specification, there shouldn't be much left to type check anyways
        // Info.check would probably need some restructuring to type check only certain members
        info <- Info.check(parsedProgram, context)(config)
      } yield info
      res.fold(
        errs => context.addErrenousPackage(pkgName, errs),
        info => context.addPackage(info)
      )
      res
    }

    def getTypeChecker(importedPkg: ImportT): Either[Messages, ExternalTypeInfo] = {
      def createImportError(errs: Vector[VerifierError]): Messages = {
        val notFoundErr = errs.collectFirst { case e: NotFoundError => e }
        // create an error message located at the import statement to indicate errors in the imported package
              // we distinguish between parse and type errors and packages whose source files could not be found
        notFoundErr.map(e => message(importedPkg.decl, e.message))
          .getOrElse(message(importedPkg.decl, s"Package '${importedPkg.decl.pkg}' contains errors"))
      }

      // check if package was already parsed, otherwise do parsing and type checking:
      val cachedInfo = context.getTypeInfo(importedPkg.decl.pkg)
      cachedInfo.getOrElse(parseAndTypeCheck(importedPkg)).left.map(createImportError)
    }

    val foreignPkgResult = for {
      typeChecker <- getTypeChecker(importedPkg)
      entity = typeChecker.externalRegular(id)
    } yield entity
    foreignPkgResult.fold(
      msgs => Some((ErrorMsgEntity(msgs), Vector())),
      m => m.flatMap(m => Some((m, Vector())))
    )
  }


  def tryDotLookup(b: PExpressionOrType, id: PIdnUse): Option[(Entity, Vector[MemberPath])] = {
    exprOrType(b) match {
      case Left(expr) =>
        val methodLikeAttempt = tryMethodLikeLookup(expr, id)
        if (methodLikeAttempt.isDefined) methodLikeAttempt
        else tryFieldLookup(exprType(expr), id)

      case Right(typ) => typeType(typ) match {
        case pkg: ImportT => tryPackageLookup(pkg, id)
        case _ => tryMethodLikeLookup(typ, id)
      }
    }
  }





  def findField(t: Type, id: PIdnUse): Option[StructMember] =
    structMemberSet(t).lookup(id.name)


}
