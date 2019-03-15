/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package viper.gobra.frontend

import org.bitbucket.inkytonik.kiama.==>
import org.bitbucket.inkytonik.kiama.attribution.{Attribution, Decorators}
import org.bitbucket.inkytonik.kiama.relation.Tree
import org.bitbucket.inkytonik.kiama.util.Messaging._
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

    lazy val defentity: PDefLike => (String, Entity) =
      attr[PDefLike, (String, Entity)] {
        case id@ tree.parent(p) =>
          val entity = p match { // TODO: unknown cases
            case decl: PConstDecl => Constant(decl, decl.left.zipWithIndex.find(_._1 == id).get._2)
            case decl: PVarDecl => LocalVariable(decl, decl.left.zipWithIndex.find(_._1 == id).get._2)
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

      case idef: PDefLike if doesAddEntry(idef) =>
        val (key, entity) = defentity(idef)
        defineIfNew(out(idef), key, entity)

      case scope: PScope =>
        enter(out(scope))
    }

    lazy val doesAddEntry: PDefLike => Boolean =
      attr[PDefLike, Boolean] {
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
        { case (b,i) => findSelection(idType(b), i).getOrElse(UnknownEntity()) }
        { case (b,i) => findMember   (idType(b), i).getOrElse(UnknownEntity()) }

      case tree.parent.pair(id: PIdnUnqualifiedUse, e: PMethodExpr) =>
        findMember(typeType(e.base), id).getOrElse(UnknownEntity())

      case tree.parent.pair(id: PIdnUnqualifiedUse, e: PSelection) =>
        findSelection(exprType(e.base), id).getOrElse(UnknownEntity())

      case n =>
        lookup(scopedDefenv(n), serialize(n), UnknownEntity())
    }

    import Type._



    def isRefMethod(m: PMethodDecl): Boolean = m.receiver.typ match {
      case _: PMethodReceiveName    => false
      case _: PMethodReceivePointer => true
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
        case p: PParameter => typeType(p.typ)
        case r: PReceiver  => typeType(r.typ)
        case PVoidResult() => VoidType
        case PResultClause(outs) => InternalTupleT(outs.map(miscTipe))
        case PEmbeddedName(t) => typeType(t)
        case PEmbeddedPointer(t) => PointerT(typeType(t))
      }

    // post typed
    def leftSideType(i: Int, right: Vector[PExpression]): Type = { // TODO: export decision to different function
      if (right.size == 1) {
        exprType(right.head) match {
          case InternalTupleT(ts) if i < ts.size => ts(i)
          case t: Type =>
            violation(i == 0, "index exceeds right side elements")
            t // TODO: (message(src, s"expected no more assignees but got $src"))
        }
      } else {
        violation(i < right.size, "index exceeds right side elements")
        exprType(right(i)) // TODO: (message(src, s"expected no more assignees but got $src"))
      }
    }




    lazy val memberTipe: TypeMember => Type =
      attr[TypeMember, Type] {

        case MethodImpl(PMethodDecl(_, _, args, result, _)) => FunctionT(args map miscTipe, miscTipe(result))

        case MethodSpec(PMethodSpec(_, args, result)) => FunctionT(args map miscTipe, miscTipe(result))

        case Field(PFieldDecl(_, typ)) => typeType(typ)

        case Embbed(PEmbeddedDecl(typ)) => miscTipe(typ)
      }

    lazy val isConversionOrMethodExpr: PConversionOrUnaryCall => Boolean =
      attr[PConversionOrUnaryCall, Boolean] { n => entity(n.base).isInstanceOf[NamedType] }

    def compatibleWithAssOp(t: Type, op: PAssOp): Boolean = (t, op) match {
      case (IntT, PAddOp() | PSubOp() | PMulOp() | PDivOp() | PModOp() ) => true
      case _ => false
    }


    private def useKnown(t: Type)(thn: Type => Messages): Messages = t match {
      case UnknownType => noMessages
      case _ => thn(t)
    }

    private def useWith[B](t: B)(thn: B ==> Messages): Messages =
      thn.lift.apply(t).getOrElse(noMessages)


    trait Computation[-A, +R] extends (A => R) {

      def compute(n: A): R

      override def apply(n: A): R = compute(n)
    }

    trait Memoization[-A, +R] extends Computation[A, R] {

      lazy val store: A => R = attr(super.apply)

      override def apply(v: A): R = store(v)
    }

    trait Safety[-A, +R] extends Computation[A, R] {
      def safe(n: A): Boolean
      def unsafe: R

      override def apply(n: A): R = if (safe(n)) super.apply(n) else unsafe
    }

    trait Validity[-A, R] extends Computation[A, R] {

      def invalid(ret: R): Boolean

      def valid(n: A): Boolean = !invalid(apply(n))
    }

    trait Error[-A] extends Validity[A, Messages] {

      override def invalid(ret: Messages): Boolean = ret.nonEmpty
    }

    trait WellDefinedness[-A] extends Error[A]

    def children[T <: PNode](n: T): Vector[PNode] =
      tree.child(n)

    def childrenWellDefined(n: PNode): Boolean = children(n) forall {
      case s: PStatement  => wellDefStmt.valid(s)
      case e: PExpression => wellDefExpr.valid(e)
      case t: PType       => wellDefType.valid(t)
      case i: PIdnNode    => wellDefID.valid(i)
      case _ => true
    }

    def createWellDef[T <: PNode](check: T => Messages): WellDefinedness[T] =
      new WellDefinedness[T] with Safety[T, Messages] with Memoization[T, Messages] {

        override def safe(n: T): Boolean = childrenWellDefined(n)

        override def unsafe: Messages = noMessages

        override def compute(n: T): Messages = check(n)
      }

    trait Typing[-A] extends Safety[A, Type] with Validity[A, Type] {

      override def unsafe: Type = UnknownType

      override def invalid(ret: Type): Boolean = ret == UnknownType
    }

    def createTyping[T](inference: T => Type)(implicit wellDef: WellDefinedness[T]): Typing[T] =
      new Typing[T] with Memoization[T, Type] {

        override def safe(n: T): Boolean = wellDef.valid(n)

        override def compute(n: T): Type = inference(n)
      }

    /**
      * Statements
      */

    private lazy val wellDefStmt: WellDefinedness[PStatement] = createWellDef{

//      case PConstDecl(left, typ, right) =>
//        declarableTo(right map exprType, typ map typeType, left map idType)
//
//      case PVarDecl(left, typ, right)   =>
//        declarableTo(right map exprType, typ map typeType, left map idType)

???

    }



    /**
      * Expressions
      */

    private implicit lazy val wellDefExpr: WellDefinedness[PExpression] = createWellDef{
      ???
    }

    private lazy val exprType: Typing[PExpression] = createTyping{

      case PNamedOperand(id) => idType(id)

      case _: PBoolLit => BooleanT
      case _: PIntLit  => IntT
      case _: PNilLit  => NilType

      case PCompositeLit(PImplicitSizeArrayType(e), lit) =>
        ArrayT(lit.elems.size, typeType(e))

      case PCompositeLit(t: PType, _) => typeType(t)

      case PFunctionLit(args, r, _) =>
        FunctionT(args map miscTipe, miscTipe(r))

      case PConversion(t, _) => typeType(t)

      case n@ PCall(callee, _) => exprType(callee) match {
        // case FunctionT(args, res) if assignableTo(args, paras map tipe) => res TODO: Move to expTipe
        case FunctionT(_, res) => res
        case t => violation(s"expected function type but got $t") //(message(n, s""))
      }

      case PConversionOrUnaryCall(base, arg) =>
        idType(base) match {
          case t: DeclaredT => t // conversion // TODO: add additional constraints
          case FunctionT(args, res) // unary call
            if args.size == 1 && assignableTo(args.head, exprType(arg)) => res
          case t => violation(s"expected function or declared type but got $t")
        }

      case n: PSelectionOrMethodExpr =>
        resolveSelectionOrMethodExpr(n)
        { case (base, id) => findSelection(idType(base), id) }
        { case (base, id) => findMember   (idType(base), id) }
          .map(memberTipe).getOrElse(violation("no selection found"))

      case PMethodExpr(base, id) =>
        findMember(typeType(base), id).map(memberTipe).getOrElse(violation("no function found"))

      case PSelection(base, id) =>
        findSelection(exprType(base), id).map(memberTipe).getOrElse(violation("no selection found"))

      case PIndexedExp(base, index) => (exprType(base), exprType(index)) match {
        case (          ArrayT(_, elem), IntT) => elem // TODO: constant indexes have to be within range
        case (PointerT(ArrayT(_, elem)), IntT) => elem
        case (SliceT(elem), IntT) => elem
        case (MapT(key, elem), indexT) if assignableTo(indexT, key) => elem
        case (bt, it) => violation(s"$it is not a valid index for the the base $bt")
      }

      case PSliceExp(base, low, high, cap) => (exprType(base), exprType(low), exprType(high), cap map exprType) match {
        case (          ArrayT(_, elem), IntT, IntT, None | Some(IntT)) if addressable(base) => SliceT(elem)
        case (PointerT(ArrayT(_, elem)), IntT, IntT, None | Some(IntT)) => SliceT(elem)
        case (SliceT(elem), IntT, IntT, None | Some(IntT)) => SliceT(elem)
        case (bt, lt, ht, ct) => violation(s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
      }

      case PTypeAssertion(base, typ) => typeType(typ) // TODO: add additional constraints

      case PReceive(e) => exprType(e) match {
        case ChannelT(elem, ChannelModus.Bi | ChannelModus.Recv) => elem
        case t => violation(s"expected receive-permitting channel but got $t")
      }

      case PReference(exp) if addressable(exp) => PointerT(exprType(exp))

      case PDereference(exp) => exprType(exp) match {
        case PointerT(t) => t
        case t => violation(s"expected pointer but got $t")
      }

      case _ => ???
    }

    /**
      * Types
      */

    private implicit lazy val wellDefType: WellDefinedness[PType] = createWellDef{
      ???
    }

    private lazy val typeType: Typing[PType] = createTyping{

      case PDeclaredType(id) =>
        entity(id) match {
          case NamedType(decl) => DeclaredT(decl)
        }

      case PBoolType() => BooleanT
      case PIntType() => IntT

      case PArrayType(len, elem) =>
        val lenOpt = intConstantEval(len)
        violation(lenOpt.isDefined, s"expected constant epxression but got $len")
        ArrayT(lenOpt.get, typeType(elem))

      case PSliceType(elem) => SliceT(typeType(elem))

      case PMapType(key, elem) => MapT(typeType(key), typeType(elem))

      case PPointerType(elem) => PointerT(typeType(elem))

      case PBiChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Bi)

      case PSendChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Send)

      case PRecvChannelType(elem) => ChannelT(typeType(elem), ChannelModus.Recv)

      case t: PStructType => StructT(t)

      case PMethodReceiveName(t) => typeType(t)

      case PMethodReceivePointer(t) => PointerT(typeType(t))

      case PFunctionType(args, r) => FunctionT(args map miscTipe, miscTipe(r))

      case t: PInterfaceType => InterfaceT(t)
    }

    /**
      * Identifiers
      */

    private implicit lazy val wellDefID: WellDefinedness[PIdnNode] = createWellDef{
      ???
    }

    private lazy val idType: Typing[PIdnNode] = createTyping{ id =>
      entity(id) match {

        case Constant(n@ PConstDecl(_, t, right), i) =>
          t.map(typeType).getOrElse(ifTypable(n)(leftSideType(i, right)))

        case LocalVariable(n@ PVarDecl(_, t, right), i) =>
          t.map(typeType).getOrElse(ifTypable(n)(leftSideType(i, right)))

        case Function(PFunctionDecl(_, args, r, _)) =>
          FunctionT(args map miscTipe, miscTipe(r))

        case NamedType(decl) => DeclaredT(decl)

        case InParameter(p) => typeType(p.typ)

        case OutParameter(p) => typeType(p.typ)

        case TypeSwitchVariable(decl) =>
          val constraints = typeSwitchConstraints(id)
          if (constraints.size == 1) constraints.head else exprType(decl.exp)
      }
    }

    /**
      * Error Reporting
      */

    trait Property[-A] extends (A => Boolean) {
      def errors(src: PNode)(n: A): Messages
    }

    def createProperty[A](msg: A => String)(check: A => Boolean): Property[A] = new Property[A] {

      override def errors(src: PNode)(n: A): Messages = message(src, msg(n), !apply(n))

      override def apply(v: A): Boolean = check(v)
    }

    def createBinaryProperty[A](name: String)(check: A => Boolean): Property[A] =
      createProperty((n: A) => s"property error: got $n that is not $name")(check)


    /**
      * Comparability
      */

    def comparable(self: Type, other: Type): Boolean = comparableTypes(self)(other)

    lazy val comparable2: Property[(Type, Type)] = createBinaryProperty("comparable with each other"){
      case (left, right) =>
        assignableTo(left, right) && assignableTo(right, left) && ((left, right) match {
          case (l, r) if comparableType(l) && comparableType(r) => true
          case (NilType, _: SliceT | _: MapT | _: FunctionT) => true
          case (_: SliceT | _: MapT | _: FunctionT, NilType) => true
          case _ => false
        })
    }

    lazy val comparableType2: Property[Type] = createBinaryProperty("comparable"){
      case t: StructT =>
        memberSet(t).collect{ case (_, f: Field) => typeType(f.decl.typ) }.forall(comparableType)

      case _: SliceT | _: MapT | _: FunctionT => false

      case _ =>  true
    }

    lazy val comparableTypes: Type => Type => Boolean =
      paramAttr[Type, Type, Boolean] { left => right =>
        assignableTo(left, right) && assignableTo(right, left) && ((left, right) match {
          case (l, r) if comparableType(l) && comparableType(r) => true
          case (NilType, _: SliceT | _: MapT | _: FunctionT) => true
          case (_: SliceT | _: MapT | _: FunctionT, NilType) => true
          case _ => false
        })
      }

    lazy val comparableType: Type => Boolean =
      attr[Type, Boolean] {
        case t: StructT =>
          memberSet(t).collect{ case (_, f: Field) => typeType(f.decl.typ) }.forall(comparableType)

        case _: SliceT | _: MapT | _: FunctionT => false

        case _ =>  true
      }

    /**
      * Assignability
      */

    sealed trait AssignModi
    case object SingleAssign extends AssignModi
    case object MultiAssign extends AssignModi
    case object ErrorAssign extends AssignModi



    def assignModi(left: Int, right: Int): AssignModi =
      if (left > 0 && left == right) SingleAssign
      else if (left > right && right == 1) MultiAssign
      else ErrorAssign

    def declarableTo(right: Vector[Type], t: Option[Type], left: Vector[Type]): Boolean =
      t match {
        case Some(lt) => left.nonEmpty && right.forall(assignableTo(_, lt))
        case None     => assignableTo(right, left)
      }

    def assignableTo(right: Vector[Type.Type], left: Vector[Type.Type]): Boolean =
      assignModi(left.size, right.size) match {
        case SingleAssign => right.zip(left).forall{ case (l,r) => assignableTo(l,r) }
        case MultiAssign  => right.head match {
          case InternalTupleT(ts) => ts.zip(left).forall{ case (l,r) => assignableTo(l,r) }
          case _ => false
        }
        case _ => false
      }

    def assignableTo(right: Type.Type, left: Type.Type): Boolean = assignableToTypes(right)(left)

    lazy val assignableToTypes: Type => Type => Boolean =
      paramAttr[Type, Type, Boolean] { right => left => (right, left) match {
        case (l, r) if identicalTypes(l)(r) => true
        case (l, r) if !(l.isInstanceOf[DeclaredT] && r.isInstanceOf[DeclaredT])
          && identicalTypes(underlyingType(l))(underlyingType(r)) => true
        // TODO: interface is implemented
        case (ChannelT(le, ChannelModus.Bi), ChannelT(re, _)) if identicalTypes(le)(re) => true
        case (NilType, _: PointerT | _: FunctionT | _: SliceT | _: MapT | _: ChannelT | _: InterfaceT) => true
        case _ => false
      }}



    lazy val assignable: PExpression => Boolean =
      attr[PExpression, Boolean] {
        case PIndexedExp(b, _) if exprType(b).isInstanceOf[MapT] => true
        case e => addressable(e)
      }

    /**
      * Addressability
      */

    lazy val effAddressable: PExpression => Boolean =
      attr[PExpression, Boolean] {
        case _: PCompositeLit  => true
        case e => addressable(e)
      }

    // depends on: entity, tipe
    lazy val addressable: PExpression => Boolean =
      attr[PExpression, Boolean] {
        case PNamedOperand(id) => entity(id).isInstanceOf[Variable]
        case _: PReference     => true
        case PIndexedExp(b, _) => val bt = exprType(b); bt.isInstanceOf[SliceT] || (b.isInstanceOf[ArrayT] && addressable(b))
        case PSelection(b, id) => entity(id).isInstanceOf[Field] && addressable(b)
        case PSelectionOrMethodExpr(_, id) => entity(id).isInstanceOf[Field] // variables are always addressable
        case _ => false
      }

    /**
      * Underlying Types
      */

    // depends on: typeType
    lazy val underlyingType: Type => Type =
    attr[Type, Type] {
      case DeclaredT(t: PTypeDecl) => typeType(t.right)
      case t => t
    }

    /**
      * Identical Types
      */

    // depends on: abstractType, interfaceMethodSet, memberTipe
    lazy val identicalTypes: Type => Type => Boolean =
    paramAttr[Type, Type, Boolean] { left => right => (left, right) match {

      case (DeclaredT(l), DeclaredT(r)) => l == r

      case (ArrayT(ll, l), ArrayT(rl, r)) => ll == rl && identicalTypes(l)(r)

      case (SliceT(l), SliceT(r)) => identicalTypes(l)(r)

      case (StructT(PStructType(les, lfs)), StructT(PStructType(res, rfs))) =>
        les.size == res.size && les.zip(res).forall{
          case (l,r) => identicalTypes(miscTipe(l.typ))(miscTipe(r.typ))
        } && lfs.size == rfs.size && lfs.zip(rfs).forall{
          case (l,r) => l.id.name == r.id.name && identicalTypes(typeType(l.typ))(typeType(r.typ))
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

    /**
      * Executability
      */

    def isExecutable(e: PExpression): Boolean = e match {
      case PCall(callee, _) => !isBuildIn(callee)
      case _ => false
    }

    def isBuildIn(e: PExpression): Boolean = e match {
      case _ => true
    }

    /**
      * Enclosing
      */

    lazy val enclosingMethod: PStatement => PMethodDecl =
      down( (_: PNode) => violation("Statement does not root in a method")){ case m: PMethodDecl => m }

    def typeSwitchConstraints(id: PIdnNode): Vector[Type] =
      typeSwitchConstraintsLookup(id)(id)

    lazy val typeSwitchConstraintsLookup: PIdnNode => PNode => Vector[Type] =
      paramAttr[PIdnNode, PNode, Vector[Type]] { id => {
        case tree.parent.pair(PTypeSwitchCase(left, _), s: PTypeSwitchStmt)
          if s.binder.exists(_.name == id.name) => left.map(typeType)

        case s: PTypeSwitchStmt // Default case
          if s.binder.exists(_.name == id.name) => Vector.empty

        case tree.parent(p) => typeSwitchConstraintsLookup(id)(p)
      }}

    /**
      * Method and Selection Set
      */

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

      def collect[T](f: (String, TypeMember) ==> T): Vector[T] = internal.collect{
        case (n, (m, _, _)) if f.isDefinedAt(n, m) => f(n, m)
      }(breakOut)

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

        case DeclaredT(decl)  => rec(typeType(decl.right)).surface
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
        case DeclaredT(decl)  => promotedSelectionSet(typeType(decl.right)).surface
        case PointerT(t: DeclaredT)  => promotedMemberSetRef(t)
        case _ => MemberSet.empty
      }

    def findSelection(t: Type, id: PIdnUnqualifiedUse): Option[TypeMember] = selectionSet(t).lookup(id.name)

    def findMember(t: Type, id: PIdnUnqualifiedUse): Option[TypeMember] = memberSet(t).lookup(id.name)










    @scala.annotation.elidable(scala.annotation.elidable.ASSERTION) @inline
    def violation(cond: Boolean, msg: => String): Unit = if (!cond) violation(msg)

    @scala.annotation.elidable(scala.annotation.elidable.ASSERTION) @inline
    def violation(msg: String): Nothing = throw new java.lang.IllegalStateException(s"Logic error: $msg")

    def ifTypable[T <: PNode](n: T)(block: => Type): Type = {
      if (children(n) forall isTypable)
        block
      else
        UnknownType
    }

    def ifTypable[T <: PNode](block: T => Type): T => Type = n => ifTypable(n)(block(n))

    def isTypable(n: PNode): Boolean = n match {
      case e: PExpression => typable(e).isEmpty
      case i: PIdnNode => validID(i).isEmpty
      case t: PType    => validType(t).isEmpty
      case _ => true
    }

    lazy val validID: PIdnNode => Messages = ???

    lazy val validType: PType => Messages = ???

    lazy val typable: PExpression => Messages =
      attr[PExpression, Messages] { e =>

        val act = exprType(e)

        def typeMsg(msg: String, cond: Boolean = true): Messages =
          message(e, s"type error: $cond")

        def satisfies(pred: Type => Boolean)(msg: String): Messages =
          typeMsg(s"got $act, but expected $msg", !pred(act))

        def satisfiesP(pred: Type ==> Boolean)(msg: String): Messages =
          satisfies(pred.lift(_).getOrElse(false))(msg)

        def satisfiedForOneOf(pred: Type => Type => Boolean)(msg: String)(es: Seq[Type]): Messages = {
          require(es.nonEmpty)

          if (es contains UnknownType) {
            noMessages
          } else {
            val postMsg = if (es.size == 1) s"${es.head}" else s"one of $es"
            satisfies( t => es exists (pred(_)(t)) )(msg ++ postMsg)
          }
        }

        def isOneOf(es: Type*): Messages = satisfiedForOneOf(l => r => l == r)("")(es)

        def assignableToOneOf(es: Type*): Messages =
          satisfiedForOneOf(l => r => assignableTo(r, l))("type assignable to")(es)

        (e, act) match {
          case (_, UnknownType) => noMessages
          case (tree.parent(p), _) => p match {

            case PConstDecl(_, Some(t), _) => isOneOf(typeType(t)) // TODO: add number constraints
            case PVarDecl(_, Some(t), _) => isOneOf(typeType(t))

            case _: PExpressionStmt => typeMsg(s"got $e but expected non-build-in expression", isExecutable(e))

            case PSendStmt(`e`, _) =>
              satisfiesP{
                case ChannelT(elem, ChannelModus.Bi | ChannelModus.Send) => true
              }("send-permitting channel")

            case PSendStmt(ch, `e`) =>
              useWith(exprType(ch)) { case ChannelT(elem, _) =>
                satisfies(assignableTo(_, elem))(s"a type that is assignable to $elem")
              }

            case PAssignment(left, _) if left contains e =>
              typeMsg(s"got $e but expected assignable expression", assignable(e))

            case PAssignment(left, right) if right contains e =>
              if (right.size == 1 && left.size > 1) {
                assignableToOneOf(InternalTupleT(left map exprType))
              } else {
                assignableToOneOf(exprType(left(right indexOf e)))
              }

            case PAssignmentWithOp(`e`, op,  _) =>
              typeMsg(s"got $e but expected assignable expression", assignable(e)) ++
                satisfies(compatibleWithAssOp(_, op))(s"type that is compatible with $op")

            case PAssignmentWithOp(left, _, `e`) => assignableToOneOf(exprType(left))

            case PShortVarDecl(left, right) =>
              if (right.size == 1 && left.size > 1) {
                assignableToOneOf(InternalTupleT(left map idType))
              } else {
                assignableToOneOf(idType(left(right indexOf e)))
              }

            case _: PIfStmt | _: PForStmt => isOneOf(BooleanT)

            case _: PExprSwitchStmt => satisfies(comparableType)("comparable type")

            case tree.parent.pair(_: PExprSwitchCase, sw: PExprSwitchStmt) =>
              useKnown(exprType(sw.exp))(st => satisfies(comparable(st, _))(s"type comparable to $st"))

            case _: PTypeSwitchCase => satisfies(_.isInstanceOf[InterfaceT])("interface type")

            case n@ (_: PShortForRange | PAssForRange(_, `e`, _))  =>
              val lefts = n match {
                case PShortForRange(ls, _, _) => ls map idType
                case PAssForRange(ls, _, _) => ls map exprType
                case _ => violation("node must be a for comprehension")
              }
              satisfies{ t =>
                (t, lefts) match {
                  case (ArrayT(_, e),  Vector(v)) => assignableTo(e, v)
                  case (SliceT(e), Vector(v)) => assignableTo(e, v)
                  case (PointerT(ArrayT(_, e)), Vector(v)) => assignableTo(e, v)
                  case (MapT(_, e), Vector(v)) => assignableTo(e, v)
                  case (ChannelT(e, ChannelModus.Recv | ChannelModus.Bi), Vector(v)) => assignableTo(e, v)

                  case (ArrayT(_, e),  _ +: v +: _) => assignableTo(e, v)
                  case (SliceT(e), _ +: v +: _) => assignableTo(e, v)
                  case (PointerT(ArrayT(_, e)), _ +: v +: _) => assignableTo(e, v)
                  case (MapT(k, e), i +: v +: _) => assignableTo(e, v) && assignableTo(k, i)

                  case _ => false
                }
              }(s"type that ranges over $lefts")

            case PAssForRange(lefts, _, _) if lefts contains e =>
              satisfies(_ => assignable(e))("addressable type or a map indexing")

            case _: PGoStmt | _: PDeferStmt =>
              typeMsg(s"got $e but expected non-build-in expression", isExecutable(e)) ++
              satisfies(_.isInstanceOf[FunctionT])("function type")

            case ret@ PReturn(rs) =>
              val idx  = rs indexOf e
              enclosingMethod(ret).result match {
                case PVoidResult() =>
              }
              val mrts = ???
              val rts  = if (rs.isEmpty) VoidType
              ???

            case _ => noMessages
          }
        }
      }






    def resolveSelectionOrMethodExpr[T](n: PSelectionOrMethodExpr)
                                       (selection: (PIdnUse, PIdnUnqualifiedUse) => T)
                                       (methodExp: (PIdnUse, PIdnUnqualifiedUse) => T): T =
      entity(n.base) match {
        case _: NamedType => methodExp(n.base, n.id)
        case _            => selection(n.base, n.id)
      }





    lazy val methodSubSet: Type => InterfaceT => Boolean =
      paramAttr[Type, InterfaceT, Boolean] { left => right =>
        memberSet(left).methodSet.implements(interfaceMethodSet(right))
      }





  }

  private object SymbolTable extends Environments {

    sealed trait Regular extends Entity with Product

    case class Constant(decl: PConstDecl, idx: Int) extends Regular

    sealed trait Variable extends Regular

    case class LocalVariable(decl: PVarDecl, idx: Int) extends Variable

    case class NamedType(decl: PTypeDef) extends Regular

    case class TypeAlias(decl: PTypeAlias) extends Regular

    case class Function(decl: PFunctionDecl) extends Regular

    sealed trait TypeMember extends Regular

    sealed trait Method extends TypeMember

    case class MethodImpl(decl: PMethodDecl) extends Method

    case class MethodSpec(spec: PMethodSpec) extends Method

    case class Field(decl: PFieldDecl) extends TypeMember

    case class Embbed(decl: PEmbeddedDecl) extends TypeMember

    case class InParameter(decl: PNamedParameter) extends Variable

    case class OutParameter(decl: PNamedParameter) extends Variable

    case class TypeSwitchVariable(decl: PTypeSwitchStmt) extends Variable

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



