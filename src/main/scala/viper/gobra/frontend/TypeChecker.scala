/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.attribution.{Attribution, Decorators}
import org.bitbucket.inkytonik.kiama.relation.Tree
import org.bitbucket.inkytonik.kiama.util.{Entity, Environments, UnknownEntity}
import viper.gobra.ast.parser._
import viper.gobra.reporting.VerifierError

import scala.collection.breakOut

trait TypeInfo {

}

object TypeChecker {

  type GoTree = Tree[PNode, PProgram]

  def check(program: PProgram): Either[Vector[VerifierError], TypeInfo] = {
    val tree = new GoTree(program)
    val info = new TypeInfoImpl(tree)

    Right(info)
  }

  private class TypeInfoImpl(tree: TypeChecker.GoTree) extends Attribution with TypeInfo {

    import SymbolTable._

    val decorators = new Decorators(tree)

    import decorators._

    lazy val defentity: PIdnNamespace => (String, Entity) =
      attr[PIdnNamespace, (String, Entity)] {
        case id@ tree.parent(p) =>
          val entity = p match {
            case decl: PConstDecl => Constant(decl, decl.left.zipWithIndex.find(_._1 == id).get._2)
            case decl: PVarDecl => Variable(decl, decl.left.zipWithIndex.find(_._1 == id).get._2)
            case decl: PTypeDef => NamedType(decl)
            case decl: PTypeAlias => TypeAlias(decl)
            case decl: PFunctionDecl => Function(decl)
            case decl: PMethodDecl => MethodImpl(decl)
            case spec: PMethodSpec => MethodSpec(spec)

            case tree.parent.pair(decl: PNamedParameter, _: PResultClause) => OutParameter(decl)
            case decl: PNamedParameter => InParameter(decl)

            case decl: PTypeSwitchStmt => TypeSwitchVariable(decl)

            case decl: PLabeledStmt => Label(decl)
            case decl: PQualifiedImport => Package(decl)
          }

          (serialize(id), entity)
      }


    // TODO: namespace split
    def serialize(id: PIdnNode): String = s"non_${id.name}"

    lazy val sequentialDefenv: Chain[Environment] =
      chain(defenvin, defenvout)

    def defenvin(in: PNode => Environment): PNode ==> Environment = {
      case _: PProgram => rootenv()
      case scope: PScope => enter(in(scope))
    }

    def defenvout(out: PNode => Environment): PNode ==> Environment = {

      case idef: PIdnNamespace if doesAddEntry(idef) =>
        val (key, entity) = defentity(idef)
        defineIfNew(out(idef), key, entity)

      case scope: PScope =>
        enter(out(scope))
    }

    lazy val doesAddEntry: PIdnNamespace => Boolean =
      attr[PIdnNamespace, Boolean] {
        case tree.parent(p) =>
          p match {
            case _: PMethodDecl => false
            case _              => true
          }
      }

    /**
      * The environment to use to lookup names at a node. Defined to be the
      * completed defining environment for the smallest enclosing scope.
      */
    lazy val scopedDefenv: PNode => Environment =
      attr[PNode, Environment] {

        case tree.lastChild.pair(_: PScope, c) =>
          sequentialDefenv(c)

        case tree.parent(p) =>
          scopedDefenv(p)
      }

    lazy val entity: PIdnNode => Entity =
    attr[PIdnNode, Entity] {

      case tree.parent.pair(id: PIdnUnqualifiedUse, e: PSelectionOrMethodExpr) =>
        resolveSelectionOrMethodExpr(e)
        { case (b,i) => findSelection(idExpTipe(b), i).getOrElse(UnknownEntity()) }
        { case (b,i) => findMember   (idExpTipe(b), i).getOrElse(UnknownEntity()) }

      case tree.parent.pair(id: PIdnUnqualifiedUse, e: PMethodExpr) =>
        findMember(abstractType(e.base), id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUnqualifiedUse, e: PSelection) =>
        findSelection(tipe(e.base), id).getOrElse(UnknownEntity())

      case n =>
        lookup(scopedDefenv(n), serialize(n), UnknownEntity())
    }

    import Type._


    class MemberSet(private val internal: Map[String, (TypeMember, Vector[MemberPath], Int)]) {

      type Record = (TypeMember, Vector[MemberPath], Int)

       def this(init: TraversableOnce[TypeMember]) = this(
         init.map( m =>
           (m match {
             case MethodImpl(PMethodDecl(id, _, _, _, _)) => id.name
             case MethodSpec(PMethodSpec(id, _, _))       => id.name
             case Field(PFieldDecl(id, _))                => id.name
             case Embbed(PEmbeddedDecl(typ))              => typ.name
           }) -> (m, Vector.empty[MemberPath], 0)
         ).toMap
       )

      def rank(path: Vector[MemberPath]): Int = path.count{
        case _: MemberPath.Next => true
        case _                  => false
      }

      def union(other: MemberSet): MemberSet = new MemberSet(
        (internal.keySet ++ other.internal.keySet).map[(String, Record), Map[String, Record]]( k =>
          (internal.get(k), other.internal.get(k)) match {
            case (Some(l@ (_, _, rl)), Some(r@ (_, _, rr))) => k -> (if (rl < rr) l else r)
            case (Some(l), None) => k -> l
            case (None, Some(r)) => k -> r
            case (None, None) => throw new IllegalStateException("Key not used by operand of union")
          })(breakOut)
      )

      private def updatePath(f: (TypeMember, Vector[MemberPath], Int) => (TypeMember, Vector[MemberPath], Int)): MemberSet =
        new MemberSet(internal.mapValues( f.tupled ))

      def surface: MemberSet = updatePath{ case (m,p,l) => (m, MemberPath.Underlying +: p, l) }

      def promote(f: Embbed): MemberSet = updatePath{ case (m,p,l) => (m, MemberPath.Next(f) +: p, l + 1) }

      def ref: MemberSet = updatePath{ case (m,p,l) => (m, MemberPath.Deref +: p, l) }

      def deref: MemberSet = updatePath{ case (m,p,l) => (m, MemberPath.Ref +: p, l) }

      def lookup(key: String): Option[TypeMember] = internal.get(key).map(_._1)

      def lookupWithPath(key: String): Option[(TypeMember, Vector[MemberPath])] =
        internal.get(key).map( r => (r._1, r._2) )

      def filter(f: TypeMember => Boolean): MemberSet = new MemberSet( internal.filterKeys( n => f(internal(n)._1)) )

      def methodSet: MemberSet = filter(m => m.isInstanceOf[Method])

      def implements(other: MemberSet): Boolean = other.internal.keySet.forall(internal.contains)

      def toMap: Map[String, TypeMember] = internal.mapValues(_._1)
    }

    object MemberSet {

      def empty: MemberSet = new MemberSet(Map.empty[String, (TypeMember, Vector[MemberPath], Int)])

      def union(mss: Vector[MemberSet]): MemberSet = mss.size match {
        case 0 => empty
        case 1 => mss.head
        case _ => mss.tail.fold(mss.head){ case (l,r) => l union r }
      }
    }

    sealed trait MemberPath

    object MemberPath {
      case object Underlying extends MemberPath
      case object Deref      extends MemberPath
      case object Ref        extends MemberPath
      case class  Next(decl: Embbed) extends MemberPath
    }

    private lazy val receiverMethodSetMap: Map[Type, MemberSet] = {
      tree.root.declarations
        .collect { case m: PMethodDecl => MethodImpl(m) }(breakOut)
        .groupBy { m: MethodImpl => miscTipe(m.decl.receiver) }
        .mapValues( ms => new MemberSet(ms) )
    }

    def receiverMethodSet(recv: Type): MemberSet = receiverMethodSetMap(recv)

    lazy val interfaceMethodSet: InterfaceT => MemberSet =
      attr[InterfaceT, MemberSet] {
        case InterfaceT(PInterfaceType(es, specs)) =>
          new MemberSet(specs.map(m => MethodSpec(m))) union MemberSet.union {
            es.map( e => interfaceMethodSet(
              entity(e.typ.id) match {
                case NamedType(PTypeDef(_, t: PInterfaceType)) => InterfaceT(t)
              }
            ))
          }
      }

    lazy val memberSet: Type => MemberSet =
      attr[Type, MemberSet] {

        case t: DeclaredT           =>
          receiverMethodSet(t) union promotedMemberSet(t)

        case PointerT(t: DeclaredT) =>
          receiverMethodSet(PointerT(t)) union receiverMethodSet(t).ref union promotedMemberSetRef(t)

        case _ => MemberSet.empty
      }

    def promotedMemberSetGen(transformer: Type => Type): Type => MemberSet = {
      lazy val rec: Type => MemberSet = promotedMemberSetGen(transformer)

      attr[Type, MemberSet] {

        case StructT(PStructType(es, fields)) =>
          new MemberSet(fields map Field) union new MemberSet(es map Embbed) union
            MemberSet.union(es.map{ e => memberSet(transformer(miscTipe(e.typ))).promote(Embbed(e)) })

        case DeclaredT(decl)  => rec(abstractType(decl.right)).surface
        case inf: InterfaceT  => interfaceMethodSet(inf)

        case _ => MemberSet.empty
      }
    }

    lazy val promotedMemberSet: Type => MemberSet = promotedMemberSetGen(identity)

    lazy val promotedMemberSetRef: Type => MemberSet = {
      def maybeRef(t: Type): Type = t match {
        case _: PointerT => t
        case _           => PointerT(t)
      }

      promotedMemberSetGen(maybeRef)
    }

    lazy val selectionSet: Type => MemberSet =
      attr[Type, MemberSet] {
        case t: DeclaredT => memberSet(PointerT(t)).deref union promotedSelectionSet(t)
        case t@ PointerT(_: DeclaredT) => memberSet(t)
        case _ => MemberSet.empty
      }

    lazy val promotedSelectionSet: Type => MemberSet =
      attr[Type, MemberSet] {
        case DeclaredT(decl)  => promotedSelectionSet(abstractType(decl.right)).surface
        case PointerT(t: DeclaredT)  => promotedMemberSetRef(t)
        case _ => MemberSet.empty
      }

    def findSelection(t: Type, id: PIdnUnqualifiedUse): Option[TypeMember] = selectionSet(t).lookup(id.name)

    def findMember(t: Type, id: PIdnUnqualifiedUse): Option[TypeMember] = memberSet(t).lookup(id.name)

    def isRefMethod(m: PMethodDecl): Boolean = m.receiver.typ match {
      case _: PMethodReceiveName    => false
      case _: PMethodReceivePointer => true
    }

    lazy val abstractType: PType => Type =
      attr[PType, Type] {
        case PDeclaredType(id) =>
          entity(id) match {
            case NamedType(decl) => DeclaredT(decl)
          }

        case PBoolType() => BooleanT
        case PIntType() => IntT

        case PArrayType(len, elem) =>
          val lenOpt = intConstantEval(len)
          if (lenOpt.isDefined) {
            ArrayT(lenOpt.get, abstractType(elem))
          } else {
            UnknownType
          }

        case PSliceType(elem) => SliceT(abstractType(elem))

        case PMapType(key, elem) => MapT(abstractType(key), abstractType(elem))

        case PPointerType(elem) => PointerT(abstractType(elem))

        case PBiChannelType(elem) => ChannelT(abstractType(elem), ChannelModus.Bi)

        case PSendChannelType(elem) => ChannelT(abstractType(elem), ChannelModus.Send)

        case PRecvChannelType(elem) => ChannelT(abstractType(elem), ChannelModus.Recv)

        case t: PStructType => StructT(t)

        case PMethodReceiveName(t) => abstractType(t)

        case PMethodReceivePointer(t) => PointerT(abstractType(t))

        case PFunctionType(args, r) => FunctionT(args map miscTipe, miscTipe(r))

        case t: PInterfaceType => InterfaceT(t)
      }



    lazy val intConstantEval: PExpression => Option[BigInt] =
      attr[PExpression, Option[BigInt]] {
        case PIntLit(lit) => Some(lit)
        case e: PBinaryExp =>
          def aux(f: BigInt => BigInt => BigInt): Option[BigInt] =
            (intConstantEval(e.left), intConstantEval(e.right)) match {
              case (Some(a), Some(b)) => Some(f(a)(b))
              case _ => None
            }

          e match {
            case _: PAdd => aux(x => y => x + y)
            case _: PSub => aux(x => y => x - y)
            case _: PMul => aux(x => y => x * y)
            case _: PMod => aux(x => y => x % y)
            case _: PDiv => aux(x => y => x / y)
            case _ => None
          }
        case PNamedOperand(id) => entity(id) match {
          case Constant(PConstDecl(_, _, right), idx) if idx < right.size =>
            intConstantEval(right(idx))

          case _ => None
        }

        case _ => None
      }

    lazy val miscTipe: NonExpression => Type =
      attr[NonExpression, Type] {
        case p: PParameter => abstractType(p.typ)
        case r: PReceiver  => abstractType(r.typ)
        case PVoidResult() => VoidType
        case PResultClause(outs) => InternalTupleT(outs.map(miscTipe))
        case PEmbeddedName(t) => abstractType(t)
        case PEmbeddedPointer(t) => PointerT(abstractType(t))
      }

    def leftSideType(i: Int, right: Vector[PExpression]): Type = {
      if (right.size == 1) {
        tipe(right.head) match {
          case InternalTupleT(ts) if i < ts.size => ts(i)
          case t: Type if i == 0 => t
          case _ => UnknownType
        }
      } else if (i < right.size) {
        tipe(right(i))
      } else {
        UnknownType
      }
    }


    lazy val idExpTipe: PIdnUse => Type =
      attr[PIdnUse, Type] { id =>
        entity(id) match {

          case Constant(PConstDecl(_, t, right), i) =>
            t.map(abstractType).getOrElse(leftSideType(i, right))

          case Variable(PVarDecl(_, t, right), i) =>
            t.map(abstractType).getOrElse(leftSideType(i, right))

          case Function(PFunctionDecl(_, args, r, _)) =>
            FunctionT(args map miscTipe, miscTipe(r))

          case NamedType(decl) => DeclaredT(decl)

          case InParameter(p) => abstractType(p.typ)

          case OutParameter(p) => abstractType(p.typ)

          case TypeSwitchVariable(decl) =>
            val constraints = typeSwitchConstraints(id)
            if (constraints.size == 1) constraints.head else tipe(decl.exp)
        }
      }

    def typeSwitchConstraints(id: PIdnUse): Vector[Type] =
      typeSwitchConstraintsLookup(id)(id)

    lazy val typeSwitchConstraintsLookup: PIdnUse => PNode => Vector[Type] =
      paramAttr[PIdnUse, PNode, Vector[Type]] { id => {
        case tree.parent.pair(PTypeSwitchCase(left, _), s: PTypeSwitchStmt)
          if s.binder.exists(_.name == id.name) => left.map(abstractType)

        case s: PTypeSwitchStmt // Default case
          if s.binder.exists(_.name == id.name) => Vector.empty

        case tree.parent(p) => typeSwitchConstraintsLookup(id)(p)
      }}

    lazy val tipe : PExpression => Type =
      attr[PExpression, Type] {
        case PNamedOperand(id) => idExpTipe(id)

        case _: PBoolLit => BooleanT
        case _: PIntLit  => IntT
        case _: PNilLit  => NilType

        case PCompositeLit(PImplicitSizeArrayType(e), lit) =>
          ArrayT(lit.elems.size, abstractType(e))

        case PCompositeLit(t: PType, _) => abstractType(t)

        case PFunctionLit(args, r, _) =>
          FunctionT(args map miscTipe, miscTipe(r))

        case PConversion(t, _) => abstractType(t)

        case PCall(callee, paras) => tipe(callee) match {
          case FunctionT(args, res) if assignableTo(args, paras map tipe) => res
          case _ => UnknownType
        }

        case PConversionOrUnaryCall(base, arg) =>
          idExpTipe(base) match {
            case t: DeclaredT => t // conversion // TODO: add additional constraints
            case FunctionT(args, res) // unary call
              if args.size == 1 && assignableTo(args.head, tipe(arg)) => res
            case _ => UnknownType
          }

        case n: PSelectionOrMethodExpr =>
          resolveSelectionOrMethodExpr(n)
          { case (base, id) => findSelection(idExpTipe(base), id) }
          { case (base, id) => findMember   (idExpTipe(base), id) }
            .map(memberTipe).getOrElse(UnknownType)

        case PMethodExpr(base, id) =>
          findMember(abstractType(base), id).map(memberTipe).getOrElse(UnknownType)

        case PSelection(base, id) =>
          findSelection(tipe(base), id).map(memberTipe).getOrElse(UnknownType)

        case PIndexedExp(base, index) => (tipe(base), tipe(index)) match {
          case (          ArrayT(_, elem), IntT) => elem // TODO: constant indexes have to be within range
          case (PointerT(ArrayT(_, elem)), IntT) => elem
          case (SliceT(elem), IntT) => elem
          case (MapT(key, elem), indexT) if assignableTo(indexT, key) => elem
          case _ => UnknownType
        }

        case PSliceExp(base, low, high, cap) => (tipe(base), tipe(low), tipe(high), cap map tipe) match {
          case (          ArrayT(_, elem), IntT, IntT, None | Some(IntT)) if addressable(base) => SliceT(elem)
          case (PointerT(ArrayT(_, elem)), IntT, IntT, None | Some(IntT)) => SliceT(elem)
          case (SliceT(elem), IntT, IntT, None | Some(IntT)) => SliceT(elem)
          case _ => UnknownType
        }

        case PTypeAssertion(base, typ) => abstractType(typ) // TODO: add additional constraints

        case PReceive(e) => tipe(e) match {
          case ChannelT(elem, ChannelModus.Bi | ChannelModus.Recv) => elem
          case _ => UnknownType
        }

        case PReference(exp) if addressable(exp) => PointerT(tipe(exp))

        case PDereference(exp) => tipe(exp) match {
          case PointerT(t) => t
          case _ => UnknownType
        }


        case _ => UnknownType

      }



    lazy val memberTipe: TypeMember => Type =
      attr[TypeMember, Type] {

        case MethodImpl(PMethodDecl(_, _, args, result, _)) => FunctionT(args map miscTipe, miscTipe(result))

        case MethodSpec(PMethodSpec(_, args, result)) => FunctionT(args map miscTipe, miscTipe(result))

        case Field(PFieldDecl(_, typ)) => abstractType(typ)

        case Embbed(PEmbeddedDecl(typ)) => miscTipe(typ)
      }

    lazy val isConversionOrMethodExpr: PConversionOrUnaryCall => Boolean =
      attr[PConversionOrUnaryCall, Boolean] { n => entity(n.base).isInstanceOf[NamedType] }



    lazy val exptipe: PExpression => Type = ???



    def resolveSelectionOrMethodExpr[T](n: PSelectionOrMethodExpr)
                                       (selection: (PIdnUse, PIdnUnqualifiedUse) => T)
                                       (methodExp: (PIdnUse, PIdnUnqualifiedUse) => T): T =
      entity(n.base) match {
        case _: NamedType => methodExp(n.base, n.id)
        case _            => selection(n.base, n.id)
      }

    def assignableTo(self: Vector[Type], other: Vector[Type]): Boolean =
      self.size == other.size && self.zip(other).forall{ case (l,r) => assignableTo(l,r)}

    def assignableTo(self: Type.Type, other: Type.Type): Boolean = assignableToTypes(self)(other)

    lazy val assignableToTypes: Type => Type => Boolean =
      paramAttr[Type, Type, Boolean] { left => right => (left, right) match {
        case (l, r) if identicalTypes(l)(r) => true
        case (l, r) if !(l.isInstanceOf[DeclaredT] && r.isInstanceOf[DeclaredT])
                    && identicalTypes(underlyingType(l))(underlyingType(r)) => true
        // TODO: interface is implemented
        case (ChannelT(le, ChannelModus.Bi), ChannelT(re, _)) if identicalTypes(le)(re) => true
        case (NilType, _: PointerT | _: FunctionT | _: SliceT | _: MapT | _: ChannelT | _: InterfaceT) => true
        case _ => false
      }}

    lazy val methodSubSet: Type => InterfaceT => Boolean =
      paramAttr[Type, InterfaceT, Boolean] { left => right =>
        memberSet(left).methodSet.implements(interfaceMethodSet(right))
      }

    lazy val underlyingType: Type => Type =
      attr[Type, Type] {
        case DeclaredT(t: PTypeDecl) => abstractType(t.right)
        case t => t
      }

    lazy val identicalTypes: Type => Type => Boolean =
      paramAttr[Type, Type, Boolean] { left => right => (left, right) match {

        case (DeclaredT(l), DeclaredT(r)) => l == r

        case (ArrayT(ll, l), ArrayT(rl, r)) => ll == rl && identicalTypes(l)(r)

        case (SliceT(l), SliceT(r)) => identicalTypes(l)(r)

        case (StructT(PStructType(les, lfs)), StructT(PStructType(res, rfs))) =>
          les.size == res.size && les.zip(res).forall{
            case (l,r) => identicalTypes(miscTipe(l.typ))(miscTipe(r.typ))
          } && lfs.size == rfs.size && lfs.zip(rfs).forall{
            case (l,r) => l.id.name == r.id.name && identicalTypes(abstractType(l.typ))(abstractType(r.typ))
          }

        case (l: InterfaceT, r: InterfaceT) =>
          val lm = interfaceMethodSet(l).toMap
          val rm = interfaceMethodSet(r).toMap
          lm.keySet.forall( k => rm.get(k).exists( m => identicalTypes(memberTipe(m))(memberTipe(lm(k))) ) ) &&
            rm.keySet.forall( k => lm.get(k).exists( m => identicalTypes(memberTipe(m))(memberTipe(rm(k))) ) )

        case (PointerT(l), PointerT(r)) => identicalTypes(l)(r)

        case (FunctionT(larg, lr), FunctionT(rarg, rr)) =>
          larg.size == rarg.size && larg.zip(rarg).forall{
            case (l, r) => identicalTypes(l)(r)
          } && identicalTypes(lr)(rr)

        case (MapT(lk, le), MapT(rk, re)) => identicalTypes(lk)(rk) && identicalTypes(le)(re)

        case (ChannelT(le, lm), ChannelT(re, rm)) => identicalTypes(le)(re) && lm == rm

        case (InternalTupleT(lv), InternalTupleT(rv)) =>
          lv.size == rv.size && lv.zip(rv).forall{
            case (l, r) => identicalTypes(l)(r)
          }

        case _ => false
      }}

    def addressable(e: PExpression): Boolean = ???

  }

  private object SymbolTable extends Environments {

    sealed trait Regular extends Entity with Product

    case class Constant(decl: PConstDecl, idx: Int) extends Regular

    case class Variable(decl: PVarDecl, idx: Int) extends Regular

    case class NamedType(decl: PTypeDef) extends Regular

    case class TypeAlias(decl: PTypeAlias) extends Regular

    case class Function(decl: PFunctionDecl) extends Regular

    sealed trait TypeMember extends Regular

    sealed trait Method extends TypeMember

    case class MethodImpl(decl: PMethodDecl) extends Method

    case class MethodSpec(spec: PMethodSpec) extends Method

    case class Field(decl: PFieldDecl) extends TypeMember

    case class Embbed(decl: PEmbeddedDecl) extends TypeMember

    case class InParameter(decl: PNamedParameter) extends Regular

    case class OutParameter(decl: PNamedParameter) extends Regular

    case class TypeSwitchVariable(decl: PTypeSwitchStmt) extends Regular

    case class Package(decl: PQualifiedImport) extends Regular

    case class Label(decl: PLabeledStmt) extends Regular

  }

  object Type {

    sealed trait Type

    case object UnknownType extends Type

    case object VoidType extends Type

    case object NilType extends Type

    case class DeclaredT(decl: PTypeDecl) extends Type

    case object BooleanT extends Type

    case object IntT extends Type

    case class ArrayT(length: BigInt, elem: Type) extends Type {
      require(length >= 0, "The length of an array must be non-negative")
    }

    case class SliceT(elem: Type) extends Type

    case class MapT(key: Type, elem: Type) extends Type

    case class PointerT(elem: Type) extends Type

    case class ChannelT(elem: Type, mod: ChannelModus) extends Type

    sealed trait ChannelModus

    object ChannelModus {
      case object Bi extends ChannelModus
      case object Recv extends ChannelModus
      case object Send extends ChannelModus
    }

    case class StructT(decl: PStructType) extends Type

    case class FunctionT(args: Vector[Type], result: Type) extends Type

    case class InterfaceT(decl: PInterfaceType) extends Type

    case class InternalTupleT(ts: Vector[Type]) extends Type



  }

  private implicit class OptionPlus[A](self: Option[A]) {
    def \> (other: => Option[A]): Option[A] = if (self.isDefined) self else other
  }

  private implicit class OptionJoin[A](self: Option[Option[A]]) {
    def join: Option[A] = if (self.isDefined) self.get else None
  }

  private implicit class CoupleTuple[A](self: A) {
    def couple[B](f: A => B): (A, B) = (self, f(self))
  }

}



