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
import org.bitbucket.inkytonik.kiama.util.{Entity, Environments, MultipleEntity, UnknownEntity}
import viper.gobra.ast.parser._
import viper.gobra.reporting.{TypeError, VerifierError}

import scala.collection.breakOut

trait TypeInfo {

}

object TypeChecker {

  type GoTree = Tree[PNode, PProgram]

  def check(program: PProgram): Either[Vector[VerifierError], TypeInfo] = {
    val tree = new GoTree(program)
//    println(program.declarations.head)
//    println("-------------------")
//    println(tree)
    val info = new TypeInfoImpl(tree)

    val errors = info.errors
    if (errors.isEmpty) {
      Right(info)
    } else {
      Left(program.positions.translate(errors, TypeError))
    }
  }

  private class TypeInfoImpl(tree: TypeChecker.GoTree) extends Attribution with TypeInfo {

    import SymbolTable._

    val decorators = new Decorators(tree)

    import decorators._


    lazy val errors: Messages =
      collectMessages(tree) {
        case n: PTopLevel   => wellDefTop(n).out
        case n: PStatement  => wellDefStmt(n).out
        case n: PExpression => wellDefExpr(n).out
        case n: PType       => wellDefType(n).out
        case n: PIdnNode    => wellDefID(n).out
//        case n: PIdnDef     => wellDefID(n).out
//        case n: PIdnUnk if isDef(n) => wellDefID(n).out
        case n: PMisc       => wellDefMisc(n).out
      }

    lazy val defEntity: PDefLikeId => Entity =
      attr[PDefLikeId, Entity] {
        case PWildcard() => Wildcard()
        case id@tree.parent(p) => p match {

          case decl: PConstDecl =>
            val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

            assignModi(decl.left.size, decl.right.size) match {
              case SingleAssign => SingleConstant(decl.right(idx), decl.typ)
              case MultiAssign => MultiConstant(idx, decl.right.head)
              case _ => UnknownEntity()
            }

          case decl: PVarDecl =>
            val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

            assignModi(decl.left.size, decl.right.size) match {
              case SingleAssign => SingleConstant(decl.right(idx), decl.typ)
              case MultiAssign => MultiConstant(idx, decl.right.head)
              case _ => UnknownEntity()
            }

          case decl: PTypeDef => NamedType(decl)
          case decl: PTypeAlias => TypeAlias(decl)
          case decl: PFunctionDecl => Function(decl)
          case decl: PMethodDecl => MethodImpl(decl)
          case spec: PMethodSpec => MethodSpec(spec)

          case decl: PFieldDecl => Field(decl)
          case decl: PEmbeddedDecl => Embbed(decl)

          case tree.parent.pair(decl: PNamedParameter, _: PResultClause) => OutParameter(decl)
          case decl: PNamedParameter => InParameter(decl)
          case decl: PNamedReceiver => ReceiverParameter(decl)

          case decl: PTypeSwitchStmt => TypeSwitchVariable(decl)
        }
      }

    lazy val unkEntity: PIdnUnk => Entity =
      attr[PIdnUnk, Entity] {
        case id@tree.parent(p) => p match {
          case decl: PShortVarDecl =>
            val idx = decl.left.zipWithIndex.find(_._1 == id).get._2

            assignModi(decl.left.size, decl.right.size) match {
              case SingleAssign => SingleConstant(decl.right(idx), None)
              case MultiAssign => MultiConstant(idx, decl.right.head)
              case _ => UnknownEntity()
            }

          case decl: PShortForRange =>
            val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
            val len = decl.shorts.size
            RangeVariable(idx, decl.range)

          case decl: PSelectShortRecv =>
            val idx = decl.shorts.zipWithIndex.find(_._1 == id).get._2
            val len = decl.shorts.size

            assignModi(len, 1) match {
              case SingleAssign => SingleConstant(decl.recv, None)
              case MultiAssign => MultiConstant(idx, decl.recv)
              case _ => UnknownEntity()
            }

          case _ => violation("unexpected parent of unknown id")
        }
      }

    def serialize(id: PIdnNode): String = id.name

    lazy val sequentialDefenv: Chain[Environment] =
      chain(defenvin, defenvout)

    def defenvin(in: PNode => Environment): PNode ==> Environment = {
      case n: PProgram => addShallowDefToEnv(rootenv())(n)
      case scope: PUnorderedScope => addShallowDefToEnv(enter(in(scope)))(scope)
      case scope: PScope => println("enter scope"); enter(in(scope))
    }

    def defenvout(out: PNode => Environment): PNode ==> Environment = {

      case id: PIdnDef if doesAddEntry(id) && !isUnorderedDef(id) =>
        println(s"add ${id.name} to" + out(id).map(_.keySet))
        defineIfNew(out(id), serialize(id), defEntity(id))

      case id: PIdnUnk if !isDefinedInScope(out(id), serialize(id)) =>
        define(out(id), serialize(id), unkEntity(id))

      case scope: PScope =>
        println("leave scope");
        leave(out(scope))
    }

    lazy val doesAddEntry: PIdnDef => Boolean =
      attr[PIdnDef, Boolean] {
        case tree.parent(_: PMethodDecl) => false
        case _ => true
      }

    def addShallowDefToEnv(env: Environment)(n: PUnorderedScope): Environment = {

      def shallowDefs(n: PUnorderedScope): Vector[PIdnDef] = n match {
        case n: PProgram => n.declarations flatMap {
          case d: PConstDecl => d.left
          case d: PVarDecl => d.left
          case d: PFunctionDecl => Vector(d.id)
          case d: PTypeDecl => Vector(d.left)
          case _: PMethodDecl => Vector.empty
        }

        case n: PStructType => n.clauses.flatMap {
          case d: PFieldDecls => d.fields map (_.id)
          case d: PEmbeddedDecl => Vector(d.id)
        }

        case n: PInterfaceType => n.specs map (_.id)
      }

      shallowDefs(n).foldLeft(env) {
        case (e, id) => defineIfNew(e, serialize(id), defEntity(id))
      }
    }

    def isUnorderedDef(id: PIdnDef): Boolean = id match { // TODO: a bit hacky, clean up at some point
      case tree.parent(tree.parent(c)) => enclosingScope(c).isInstanceOf[PUnorderedScope]
    }


    //    /**
    //      * The environment to use to lookup names at a node. Defined to be the
    //      * completed defining environment for the smallest enclosing scope.
    //      */
    //    lazy val scopedDefenv: PNode => Environment =
    //      attr[PNode, Environment] {
    //
    //        case tree.lastChild.pair(_: PScope, c) =>
    //          sequentialDefenv(c)
    //
    //        case tree.parent(p) =>
    //          scopedDefenv(p)
    //      }

    lazy val entity: PIdnNode => Entity =
      attr[PIdnNode, Entity] {

        case tree.parent.pair(id: PIdnUse, e@ PSelectionOrMethodExpr(_, f)) if id == f =>
          resolveSelectionOrMethodExpr(e)
          { case (b, i) => findSelection(idType(b), i) }
          { case (b, i) => findMember(idType(b), i) }
            .flatten.getOrElse(UnknownEntity())

        case tree.parent.pair(id: PIdnUse, e: PMethodExpr) =>
          findMember(typeType(e.base), id).getOrElse(UnknownEntity())

        case tree.parent.pair(id: PIdnUse, e: PSelection) =>
          findSelection(exprType(e.base), id).getOrElse(UnknownEntity())

        case tree.parent.pair(id: PIdnDef, _: PMethodDecl) => defEntity(id)

        case n =>
          println(s"lookup of ${n.name} in" + sequentialDefenv(n).map(_.keySet))
          lookup(sequentialDefenv(n), serialize(n), UnknownEntity())
      }



    import Type._


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

    sealed trait ValidityMessages {
      def out: Messages
      def valid: Boolean
    }

    case object UnsafeForwardMessage extends ValidityMessages {
      override val out: Messages = noMessages
      override val valid: Boolean = false
    }

    case class LocalMessages(override val out: Messages) extends ValidityMessages {
      override def valid: Boolean = out.isEmpty
    }

    trait Error[-A] extends Validity[A, ValidityMessages] {
      override def invalid(ret: ValidityMessages): Boolean = !ret.valid
    }

    trait WellDefinedness[-A] extends Error[A]

    def children[T <: PNode](n: T): Vector[PNode] =
      tree.child(n)

    def childrenWellDefined(n: PNode): Boolean = children(n) forall {
      case s: PStatement => wellDefStmt.valid(s)
      case e: PExpression => wellDefExpr.valid(e)
      case t: PType => wellDefType.valid(t)
      case i: PIdnNode => wellDefID.valid(i)
      case o: PMisc => wellDefMisc.valid(o)
      case n: PNode => childrenWellDefined(n)
    }

    def createWellDef[T <: PNode](check: T => Messages): WellDefinedness[T] =
      new WellDefinedness[T] with Safety[T, ValidityMessages] with Memoization[T, ValidityMessages] {

        override def safe(n: T): Boolean = childrenWellDefined(n)

        override def unsafe: ValidityMessages = UnsafeForwardMessage

        override def compute(n: T): ValidityMessages = LocalMessages(check(n))
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
      * Top Level
      */

    private lazy val wellDefTop: WellDefinedness[PTopLevel] = createWellDef {

      case n: PFunctionDecl => noMessages

      case m: PMethodDecl => miscType(m.receiver) match {
        case DeclaredT(_) | PointerT(DeclaredT(_)) => noMessages
        case _ => message(m, s"method cannot have non-defined receiver")
      }
    }

    /**
      * Statements
      */

    private lazy val wellDefStmt: WellDefinedness[PStatement] = createWellDef {

      case n@PConstDecl(typ, right, left) =>
        declarableTo.errors(right map exprType, typ map typeType, left map idType)(n)

      case n@PVarDecl(typ, right, left) =>
        declarableTo.errors(right map exprType, typ map typeType, left map idType)(n)

      case n: PTypeDecl => noMessages

      case n@PExpressionStmt(exp) => isExecutable.errors(exp)(n)

      case n@PSendStmt(chn, msg) => (exprType(chn), exprType(msg)) match {
        case (ChannelT(elem, ChannelModus.Bi | ChannelModus.Send), t) => assignableTo.errors(t, elem)(n)
        case (chnt, _) => message(n, s"type error: got $chnt but expected send-permitting channel")
      }

      case n@PAssignment(lefts, rights) =>
        lefts.flatMap(a => assignable.errors(a)(a)) ++ multiAssignableTo.errors(rights map exprType, lefts map exprType)(n)

      case n@PAssignmentWithOp(left, op, right) =>
        assignable.errors(left)(n) ++ compatibleWithAssOp.errors(exprType(left), op)(n) ++
          assignableTo.errors(exprType(right), exprType(left))(n)

      case n@PShortVarDecl(rights, lefts) =>
        if (lefts.forall(pointsToData)) multiAssignableTo.errors(rights map exprType, lefts map idType)(n)
        else message(n, s"at least one assignee in $lefts points to a type")

      case n: PIfStmt => n.ifs.flatMap(ic => comparableTypes.errors(exprType(ic.condition), BooleanT)(ic))

      case n@PExprSwitchStmt(_, exp, _, dflt) =>
        message(n, s"found more than one default case", dflt.size > 1) ++
          comparableType.errors(exprType(exp))(n)

      case _: PExprSwitchDflt => noMessages

      case n@tree.parent.pair(PExprSwitchCase(left, _), sw: PExprSwitchStmt) =>
        left.flatMap(e => comparableTypes.errors(exprType(e), exprType(sw.exp))(n))

      case n: PTypeSwitchStmt =>
        message(n, s"found more than one default case", n.dflt.size > 1) ++ {
          val et = exprType(n.exp)
          val ut = underlyingType(et)
          message(n, s"type error: got $et but expected underlying interface type", !ut.isInstanceOf[InterfaceT])
        } // TODO: also check that cases have type that could implement the type

      case n@PForStmt(_, cond, _, _) => comparableTypes.errors(exprType(cond), BooleanT)(n)

      case n@PShortForRange(exp, lefts, _) =>
        if (lefts.forall(pointsToData)) multiAssignableTo.errors(Vector(miscType(exp)), lefts map idType)(n)
        else message(n, s"at least one assignee in $lefts points to a type")


      case n@PAssForRange(exp, lefts, _) =>
        multiAssignableTo.errors(Vector(miscType(exp)), lefts map exprType)(n) ++
          lefts.flatMap(t => addressable.errors(t)(t))

      case n@PGoStmt(exp) => isExecutable.errors(exp)(n)

      case n: PSelectStmt =>
        n.aRec.flatMap(rec =>
          multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.ass.map(exprType))(rec) ++
            rec.ass.flatMap(a => assignable.errors(a)(a))
        ) ++ n.sRec.flatMap(rec =>
          if (rec.shorts.forall(pointsToData))
            multiAssignableTo.errors(Vector(exprType(rec.recv)), rec.shorts map idType)(rec)
          else message(n, s"at least one assignee in ${rec.shorts} points to a type")

        )


      case n@PReturn(exps) =>
        (enclosingCodeRoot(n) match {
          case f: PFunctionDecl  => f.result
          case f: PFunctionLit   => f.result
          case m: PMethodDecl    => m.result
        }) match {
          case PVoidResult() => message(n, s"expected no arguments but got $exps", exps.nonEmpty)
          case PResultClause(outs) =>
            if (outs forall wellDefMisc.valid)
              multiAssignableTo.errors(exps map exprType, outs map miscType)(n)
            else message(n, s"return cannot be checked because the enclosing signature is incorrect")
        }

      case n@PDeferStmt(exp) => isExecutable.errors(exp)(n)

      case _: PBlock => noMessages
      case _: PSeq => noMessages

      case s => violation(s"$s was not handled")
    }


    /**
      * Expressions
      */

    private implicit lazy val wellDefExpr: WellDefinedness[PExpression] = createWellDef {

      case n@ PNamedOperand(id) => pointsToData.errors(id)(n)
      case _: PBoolLit | _: PIntLit | _: PNilLit => noMessages

      case n@PCompositeLit(t, lit) =>
        val bt = t match {
          case PImplicitSizeArrayType(elem) => ArrayT(lit.elems.size, typeType(elem))
          case t: PType => typeType(t)
        }
        literalAssignableTo.errors(lit, bt)(n)

      case _: PFunctionLit => noMessages

      case n@PConversion(t, arg) => convertibleTo.errors(exprType(arg), typeType(t))(n)

      case n@PCall(base, paras) => exprType(base) match {
        case FunctionT(args, _) =>
          if (paras.isEmpty && args.isEmpty) noMessages
          else multiAssignableTo.errors(paras map exprType, args)(n) // TODO: add special assignment
        case t => message(n, s"type error: got $t but expected function type")
      }

      case n: PConversionOrUnaryCall =>
        resolveConversionOrUnaryCall(n) {
          case (id, arg) => convertibleTo.errors(exprType(arg), idType(id))(n)
        } {
          case (id, arg) => idType(id) match {
            case FunctionT(args, _) => multiAssignableTo.errors(Vector(exprType(arg)), args)(n)
            case t => message(n, s"type error: got $t but expected function type")
          }
        }.getOrElse(message(n, s"could not determine whether $n is a conversion or unary call"))

      case n@PMethodExpr(t, id) => // Soundness: check if id is correct member done by findMember
        message(n, s"type ${typeType(t)} does not have method ${id.name}"
          , !findMember(typeType(t), id).exists(_.isInstanceOf[Method]))

      case n@PSelection(base, id) => // Soundness: check if id is correct member done by findMember
        message(n, s"type ${exprType(base)} does not have method ${id.name}"
          , findMember(exprType(base), id).isEmpty)

      case n: PSelectionOrMethodExpr => // Soundness: check if id is correct member done by findMember
        message(n, s"type ${idType(n.base)} does not have method ${n.id.name}"
          , resolveSelectionOrMethodExpr(n)
          { case (base, id) => findMember(idType(base), id).isEmpty }
          { case (t, id) => !findMember(idType(t), id).exists(_.isInstanceOf[Method]) }
            .getOrElse(false)
        )

      case n@PIndexedExp(base, index) => (exprType(base), exprType(index)) match {
        case (ArrayT(l, elem), IntT) =>
          val idxOpt = intConstantEval(index)
          message(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

        case (PointerT(ArrayT(l, elem)), IntT) =>
          val idxOpt = intConstantEval(index)
          message(n, s"index $index is out of bounds", !idxOpt.forall(i => i >= 0 && i < l))

        case (SliceT(elem), IntT) => noMessages
        case (MapT(key, elem), indexT) =>
          message(n, s"$indexT is not assignable to map key of $key", !assignableTo(indexT, key))

        case (bt, it) => message(n, s"$it index is not a proper index of $bt")
      }

      case n@PSliceExp(base, low, high, cap) => (exprType(base), exprType(low), exprType(high), cap map exprType) match {
        case (ArrayT(l, elem), IntT, IntT, None | Some(IntT)) =>
          val (lowOpt, highOpt, capOpt) = (intConstantEval(low), intConstantEval(high), cap map intConstantEval)
          message(n, s"index $low is out of bounds", !lowOpt.forall(i => i >= 0 && i < l)) ++
            message(n, s"index $high is out of bounds", !highOpt.forall(i => i >= 0 && i < l)) ++
            message(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => i >= 0 && i <= l))) ++
            message(n, s"array $base is not addressable", !addressable(base))

        case (PointerT(ArrayT(l, elem)), IntT, IntT, None | Some(IntT)) =>
          val (lowOpt, highOpt, capOpt) = (intConstantEval(low), intConstantEval(high), cap map intConstantEval)
          message(n, s"index $low is out of bounds", !lowOpt.forall(i => i >= 0 && i < l)) ++
            message(n, s"index $high is out of bounds", !highOpt.forall(i => i >= 0 && i < l)) ++
            message(n, s"index $cap is out of bounds", !capOpt.forall(_.forall(i => i >= 0 && i <= l)))

        case (SliceT(elem), IntT, IntT, None | Some(IntT)) => noMessages
        case (bt, lt, ht, ct) => message(n, s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
      }

      case n@PTypeAssertion(base, typ) => exprType(base) match {
        case t: InterfaceT =>
          val at = typeType(typ)
          message(n, s"type error: expression $base of type $at does not implement $typ", implements(at, t))
        case t => message(n, s"type error: got $t expected interface")
      }

      case n@PReceive(e) => exprType(e) match {
        case ChannelT(_, ChannelModus.Bi | ChannelModus.Recv) => noMessages
        case t => message(n, s"expected receive-permitting channel but got $t")
      }

      case n@PReference(e) => effAddressable.errors(e)(n)

      case n@PDereference(exp) => exprType(exp) match {
        case PointerT(t) => noMessages
        case t => message(n, s"expected pointer but got $t")
      }

      case n@PNegation(e) => assignableTo.errors(exprType(e), BooleanT)(n)

      case n: PBinaryExp => (n, exprType(n.left), exprType(n.right)) match {
        case (_: PEquals | _: PUnequals, l, r) => comparableTypes.errors(l, r)(n)
        case (_: PAnd | _: POr, l, r) => assignableTo.errors(l, BooleanT)(n) ++ assignableTo.errors(r, BooleanT)(n)
        case (_: PLess | _: PAtMost | _: PGreater | _: PAtLeast | _: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv
        , l, r) => assignableTo.errors(l, IntT)(n) ++ assignableTo.errors(r, IntT)(n)
        case (_, l, r) => message(n, s"$l and $r are invalid type arguments for $n")
      }


    }

    private lazy val exprType: Typing[PExpression] = createTyping {

      case PNamedOperand(id) => idType(id)

      case _: PBoolLit => BooleanT
      case _: PIntLit => IntT
      case _: PNilLit => NilType

      case PCompositeLit(PImplicitSizeArrayType(e), lit) =>
        ArrayT(lit.elems.size, typeType(e))

      case PCompositeLit(t: PType, _) => typeType(t)

      case PFunctionLit(args, r, _) =>
        FunctionT(args map miscType, miscType(r))

      case PConversion(t, _) => typeType(t)

      case n@PCall(callee, _) => exprType(callee) match {
        case FunctionT(_, res) => res
        case t => violation(s"expected function type but got $t") //(message(n, s""))
      }

      case PConversionOrUnaryCall(base, arg) =>
        idType(base) match {
          case t: DeclaredT => t // conversion
          case FunctionT(args, res) // unary call
            if args.size == 1 && assignableTo(args.head, exprType(arg)) => res
          case t => violation(s"expected function or declared type but got $t")
        }

      case n: PSelectionOrMethodExpr =>
        resolveSelectionOrMethodExpr(n)
        { case (base, id) => findSelection(idType(base), id) }
        { case (base, id) => findMember(idType(base), id) }
          .flatten.map(memberType).getOrElse(violation("no selection found"))

      case PMethodExpr(base, id) =>
        findMember(typeType(base), id).map(memberType).getOrElse(violation("no function found"))

      case PSelection(base, id) =>
        findSelection(exprType(base), id).map(memberType).getOrElse(violation("no selection found"))

      case PIndexedExp(base, index) => (exprType(base), exprType(index)) match {
        case (ArrayT(_, elem), IntT) => elem
        case (PointerT(ArrayT(_, elem)), IntT) => elem
        case (SliceT(elem), IntT) => elem
        case (MapT(key, elem), indexT) if assignableTo(indexT, key) =>
          InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
        case (bt, it) => violation(s"$it is not a valid index for the the base $bt")
      }

      case PSliceExp(base, low, high, cap) => (exprType(base), exprType(low), exprType(high), cap map exprType) match {
        case (ArrayT(_, elem), IntT, IntT, None | Some(IntT)) if addressable(base) => SliceT(elem)
        case (PointerT(ArrayT(_, elem)), IntT, IntT, None | Some(IntT)) => SliceT(elem)
        case (SliceT(elem), IntT, IntT, None | Some(IntT)) => SliceT(elem)
        case (bt, lt, ht, ct) => violation(s"invalid slice with base $bt and indexes $lt, $ht, and $ct")
      }

      case PTypeAssertion(_, typ) => typeType(typ)

      case PReceive(e) => exprType(e) match {
        case ChannelT(elem, ChannelModus.Bi | ChannelModus.Recv) =>
          InternalSingleMulti(elem, InternalTupleT(Vector(elem, BooleanT)))
        case t => violation(s"expected receive-permitting channel but got $t")
      }

      case PReference(exp) if effAddressable(exp) => PointerT(exprType(exp))

      case PDereference(exp) => exprType(exp) match {
        case PointerT(t) => t
        case t => violation(s"expected pointer but got $t")
      }

      case _: PNegation | _: PEquals | _: PUnequals | _: PAnd | _: POr |
           _: PLess | _: PAtMost | _: PGreater | _: PAtLeast =>
        BooleanT

      case _: PAdd | _: PSub | _: PMul | _: PMod | _: PDiv => IntT

      case e => violation(s"unexpected expression $e")
    }

    /**
      * Types
      */

    private implicit lazy val wellDefType: WellDefinedness[PType] = createWellDef {

      case n@ PDeclaredType(id) => pointsToType.errors(id)(n)

      case _: PBoolType | _: PIntType => noMessages

      case n@PArrayType(len, _) =>
        message(n, s"expected constant array length but got $len", intConstantEval(len).isEmpty)

      case _: PSliceType | _: PPointerType |
           _: PBiChannelType | _: PSendChannelType | _: PRecvChannelType |
           _: PMethodReceiveName | _: PMethodReceivePointer | _: PFunctionType => noMessages

      case n@ PMapType(key, _) => message(n, s"map key $key is not comparable", !comparableType(typeType(key)))

      case t: PStructType => memberSet(StructT(t)).errors(t)

      case t: PInterfaceType => memberSet(InterfaceT(t)).errors(t)
    }

    private lazy val typeType: Typing[PType] = createTyping {

      case PDeclaredType(id) => idType(id)

      case PBoolType() => BooleanT
      case PIntType() => IntT

      case PArrayType(len, elem) =>
        val lenOpt = intConstantEval(len)
        violation(lenOpt.isDefined, s"expected constant expression but got $len")
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

      case PFunctionType(args, r) => FunctionT(args map miscType, miscType(r))

      case t: PInterfaceType => InterfaceT(t)
    }

    /**
      * Identifiers
      */

    private implicit lazy val wellDefID: WellDefinedness[PIdnNode] = createWellDef {
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
      }
    }

    private lazy val idType: Typing[PIdnNode] = createTyping { id =>
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

      }
    }

    /**
      * Miscellaneous
      */

    private implicit lazy val wellDefMisc: WellDefinedness[PMisc] = createWellDef {

      case n@PRange(exp) => exprType(exp) match {
        case _: ArrayT | PointerT(_: ArrayT) | _: SliceT |
             _: MapT | ChannelT(_, ChannelModus.Recv | ChannelModus.Bi) => noMessages
        case t => message(n, s"type error: got $t but expected rangable type")
      }

      case _: PParameter | _: PReceiver | _: PResult | _: PEmbeddedType => noMessages

    }

    private lazy val miscType: Typing[PMisc] = createTyping {

      case PRange(exp) => exprType(exp) match {
        case ArrayT(_, elem) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
        case PointerT(ArrayT(len, elem)) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
        case SliceT(elem) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
        case MapT(key, elem) => InternalSingleMulti(elem, InternalTupleT(Vector(elem, IntT)))
        case ChannelT(elem, ChannelModus.Recv | ChannelModus.Bi) => elem
        case t => violation(s"unexpected range type $t")
      }

      case p: PParameter => typeType(p.typ)
      case r: PReceiver => typeType(r.typ)
      case PVoidResult() => VoidType
      case PResultClause(outs) =>
        if (outs.size == 1) miscType(outs.head) else InternalTupleT(outs.map(miscType))

      case PEmbeddedName(t) => typeType(t)
      case PEmbeddedPointer(t) => PointerT(typeType(t))
    }

    lazy val memberType: TypeMember => Type =
      attr[TypeMember, Type] {

        case MethodImpl(PMethodDecl(_, _, args, result, _)) => FunctionT(args map miscType, miscType(result))

        case MethodSpec(PMethodSpec(_, args, result)) => FunctionT(args map miscType, miscType(result))

        case Field(PFieldDecl(_, typ)) => typeType(typ)

        case Embbed(PEmbeddedDecl(typ, _)) => miscType(typ)
      }

    /**
      * Error Reporting
      */

    case class PropertyResult(opt: Option[PNode => Messages]) {
      def errors(src: PNode): Messages = opt.fold(noMessages)(_.apply(src))

      def holds: Boolean = opt.isEmpty

      def and(other: PropertyResult): PropertyResult = PropertyResult((opt, other.opt) match {
        case (Some(l), Some(r)) => Some(src => l(src) ++ r(src))
        case (Some(l), None) => Some(l)
        case (None, Some(r)) => Some(r)
        case (None, None) => None
      })

      def or(other: PropertyResult): PropertyResult = PropertyResult((opt, other.opt) match {
        case (Some(l), Some(r)) => Some(src => l(src) ++ r(src))
        case _ => None
      })
    }

    def propForall[A](base: Traversable[A], prop: Property[A]): PropertyResult =
      base.foldLeft(successProp) { case (l, r) => l and prop.result(r) }


    def successProp: PropertyResult = PropertyResult(None)

    def failedProp(label: => String, cond: Boolean = true): PropertyResult =
      PropertyResult(if (cond) Some(src => message(src, label)) else None)

    case class Property[A](gen: A => PropertyResult) extends (A => Boolean) {
      def result(n: A): PropertyResult = gen(n)

      def errors(n: A)(src: PNode): Messages = result(n).errors(src)

      override def apply(n: A): Boolean = result(n).holds

      def before[Z](f: Z => A): Property[Z] = Property[Z](n => gen(f(n)))
    }

    def createProperty[A](gen: A => PropertyResult): Property[A] = Property[A](gen)

    def createFlatProperty[A](msg: A => String)(check: A => Boolean): Property[A] =
      createProperty[A](n => failedProp(s"property error: ${msg(n)}", !check(n)))

    def createBinaryProperty[A](name: String)(check: A => Boolean): Property[A] =
      createFlatProperty((n: A) => s"got $n that is not $name")(check)


    /**
      * Convertibility
      */

    // TODO: check where convertibility and where assignability is required.

    lazy val convertibleTo: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
      case (left, right) => s"$left is not convertible to $right"
    } {
      case (Single(lst), Single(rst)) => (lst, rst) match {
        case (left, right) if assignableTo(left, right) => true
        case (left, right) => (underlyingType(left), underlyingType(right)) match {
          case (l, r) if identicalTypes(l, r) => true
          case (PointerT(l), PointerT(r)) if identicalTypes(underlyingType(l), underlyingType(r)) &&
            !(left.isInstanceOf[DeclaredT] && right.isInstanceOf[DeclaredT]) => true
          case _ => false
        }
      }
      case _ => false
    }

    /**
      * Comparability
      */

    lazy val comparableTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
      case (left, right) => s"$left is not comparable with $right"
    } {
      case (Single(left), Single(right)) =>
        assignableTo(left, right) && assignableTo(right, left) && ((left, right) match {
          case (l, r) if comparableType(l) && comparableType(r) => true
          case (NilType, _: SliceT | _: MapT | _: FunctionT) => true
          case (_: SliceT | _: MapT | _: FunctionT, NilType) => true
          case _ => false
        })
      case _ => false
    }

    lazy val comparableType: Property[Type] = createBinaryProperty("comparable") {
      case Single(st) => st match {
        case t: StructT =>
          memberSet(t).collect { case (_, f: Field) => typeType(f.decl.typ) }.forall(comparableType)

        case _: SliceT | _: MapT | _: FunctionT => false
        case _ => true
      }
      case _ => false
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

    lazy val declarableTo: Property[(Vector[Type], Option[Type], Vector[Type])] =
      createProperty[(Vector[Type], Option[Type], Vector[Type])] {
        case (right, None, left) => multiAssignableTo.result(right, left)
        case (right, Some(t), _) => propForall(right, assignableTo.before((l: Type) => (l, t)))
      }

    lazy val multiAssignableTo: Property[(Vector[Type], Vector[Type])] = createProperty[(Vector[Type], Vector[Type])] {
      case (right, left) =>
        assignModi(left.size, right.size) match {
          case SingleAssign => propForall(right.zip(left), assignableTo)
          case MultiAssign => right.head match {
            case Assign(InternalTupleT(ts)) if ts.size == left.size => propForall(ts.zip(left), assignableTo)
            case t => failedProp(s"got $t but expected tuple type of size ${left.size}")
          }
          case ErrorAssign => failedProp(s"cannot assign ${right.size} to ${left.size} elements")
        }
    }

    lazy val parameterAssignableTo: Property[(Type, Type)] = createProperty[(Type, Type)] {
      case (Argument(InternalTupleT(rs)), Argument(InternalTupleT(ls))) if rs.size == ls.size =>
        propForall(rs zip ls, assignableTo)

      case (r, l) => assignableTo.result(r, l)
    }

    lazy val assignableTo: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
      case (left, right) => s"$left is not assignable to $right"
    } {
      case (Single(lst), Single(rst)) => (lst, rst) match {
        case (l, r) if identicalTypes(l, r) => true
        case (l, r) if !(l.isInstanceOf[DeclaredT] && r.isInstanceOf[DeclaredT])
          && identicalTypes(underlyingType(l), underlyingType(r)) => true
        case (l, r: InterfaceT) if implements(l, r) => true
        case (ChannelT(le, ChannelModus.Bi), ChannelT(re, _)) if identicalTypes(le, re) => true
        case (NilType, _: PointerT | _: FunctionT | _: SliceT | _: MapT | _: ChannelT | _: InterfaceT) => true
        case _ => false
      }
      case _ => false
    }

    lazy val assignable: Property[PExpression] = createBinaryProperty("assignable") {
      case PIndexedExp(b, _) if exprType(b).isInstanceOf[MapT] => true
      case e => addressable(e)
    }

    lazy val compatibleWithAssOp: Property[(Type, PAssOp)] = createFlatProperty[(Type, PAssOp)] {
      case (t, op) => s"type error: got $t, but expected type compatible with $op"
    } {
      case (Single(IntT), PAddOp() | PSubOp() | PMulOp() | PDivOp() | PModOp()) => true
      case _ => false
    }

    lazy val compositeKeyAssignableTo: Property[(PCompositeKey, Type)] = createProperty[(PCompositeKey, Type)] {
      case (PIdentifierKey(id), t) => assignableTo.result(idType(id), t)
      case (k: PCompositeVal, t) => compositeValAssignableTo.result(k, t)
    }

    lazy val compositeValAssignableTo: Property[(PCompositeVal, Type)] = createProperty[(PCompositeVal, Type)] {
      case (PExpCompositeVal(exp), t) => assignableTo.result(exprType(exp), t)
      case (PLitCompositeVal(lit), t) => literalAssignableTo.result(lit, t)
    }

    lazy val literalAssignableTo: Property[(PLiteralValue, Type)] = createProperty[(PLiteralValue, Type)] {
      case (PLiteralValue(elems), Single(right)) =>
        underlyingType(right) match {
          case StructT(decl) =>
            if (elems.isEmpty) {
              successProp
            } else if (elems.exists(_.key.nonEmpty)) {
              val tmap = (
                decl.embedded.map(e => (e.typ.name, miscType(e.typ))) ++
                  decl.fields.map(f => (f.id.name, typeType(f.typ)))
                ).toMap

              failedProp("for struct literals either all or none elements must be keyed"
                , !elems.forall(_.key.nonEmpty)) and
                propForall(elems, createProperty[PKeyedElement] { e =>
                  e.key.map {
                    case PIdentifierKey(id) if tmap.contains(id.name) =>
                      compositeValAssignableTo.result(e.exp, tmap(id.name))

                    case v => failedProp(s"got $v but expected field name")
                  }.getOrElse(successProp)
                })
            } else if (elems.size == decl.embedded.size + decl.fields.size) {
              propForall(
                elems.map(_.exp).zip(decl.clauses.flatMap {
                  case PEmbeddedDecl(typ, _) => Vector(miscType(typ))
                  case PFieldDecls(fields) => fields map (f => typeType(f.typ))
                }),
                compositeValAssignableTo
              )
            } else {
              failedProp("number of arguments does not match structure")
            }

          case ArrayT(len, elem) =>
            failedProp("expected integer as keys for array literal"
              , elems.exists(_.key.exists {
                case PExpCompositeVal(exp) => intConstantEval(exp).isEmpty
                case _ => true
              })) and
              propForall(elems.map(_.exp), compositeValAssignableTo.before((c: PCompositeVal) => (c, elem))) and
              failedProp("found overlapping or out-of-bound index arguments"
                , {
                  val idxs = constantIndexes(elems)
                  idxs.distinct.size == idxs.size && idxs.forall(i => i >= 0 && i < len)
                })

          case SliceT(elem) =>
            failedProp("expected integer as keys for slice literal"
              , elems.exists(_.key.exists {
                case PExpCompositeVal(exp) => intConstantEval(exp).isEmpty
                case _ => true
              })) and
              propForall(elems.map(_.exp), compositeValAssignableTo.before((c: PCompositeVal) => (c, elem))) and
              failedProp("found overlapping or out-of-bound index arguments"
                , {
                  val idxs = constantIndexes(elems)
                  idxs.distinct.size == idxs.size && idxs.forall(i => i >= 0)
                })

          case MapT(key, elem) =>
            failedProp("for map literals all elements must be keyed"
              , elems.exists(_.key.isEmpty)) and
              propForall(elems.flatMap(_.key), compositeKeyAssignableTo.before((c: PCompositeKey) => (c, key))) and
              propForall(elems.map(_.exp), compositeValAssignableTo.before((c: PCompositeVal) => (c, elem)))

          case t => failedProp(s"cannot assign literal to $t")
        }
      case (l, t) => failedProp(s"cannot assign literal $l to $t")
    }

    def constantIndexes(vs: Vector[PKeyedElement]): List[BigInt] =
      vs.foldLeft(List(-1: BigInt)) {
        case (last :: rest, PKeyedElement(Some(PExpCompositeVal(exp)), _)) =>
          intConstantEval(exp).getOrElse(last + 1) :: last :: rest

        case (last :: rest, _) => last + 1 :: last :: rest

        case _ => violation("left argument must be non-nil element")
      }.tail

    /**
      * Addressability
      */

    lazy val effAddressable: Property[PExpression] = createBinaryProperty("effective addressable") {
      case _: PCompositeLit => true
      case e => addressable(e)
    }

    // depends on: entity, tipe
    lazy val addressable: Property[PExpression] = createBinaryProperty("addressable") {
      case PNamedOperand(id) => entity(id).isInstanceOf[Variable]
      case _: PReference => true
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
      case Single(DeclaredT(t: PTypeDecl)) => typeType(t.right)
      case t => t
    }

    /**
      * Identical Types
      */

    // depends on: abstractType, interfaceMethodSet, memberTipe
    lazy val identicalTypes: Property[(Type, Type)] = createFlatProperty[(Type, Type)] {
      case (left, right) => s"$left is not identical to $right"
    } {
      case (Single(lst), Single(rst)) => (lst, rst) match {

        case (IntT, IntT) | (BooleanT, BooleanT) => true

        case (DeclaredT(l), DeclaredT(r)) => l == r

        case (ArrayT(ll, l), ArrayT(rl, r)) => ll == rl && identicalTypes(l, r)

        case (SliceT(l), SliceT(r)) => identicalTypes(l, r)

        case (StructT(l), StructT(r)) =>
          val (les, lfs, res, rfs) = (l.embedded, l.fields, r.embedded, r.fields)

          les.size == res.size && les.zip(res).forall {
            case (lm, rm) => identicalTypes(miscType(lm.typ), miscType(rm.typ))
          } && lfs.size == rfs.size && lfs.zip(rfs).forall {
            case (lm, rm) => lm.id.name == rm.id.name && identicalTypes(typeType(lm.typ), typeType(rm.typ))
          }

        case (l: InterfaceT, r: InterfaceT) =>
          val lm = interfaceMethodSet(l).toMap
          val rm = interfaceMethodSet(r).toMap
          lm.keySet.forall(k => rm.get(k).exists(m => identicalTypes(memberType(m), memberType(lm(k))))) &&
            rm.keySet.forall(k => lm.get(k).exists(m => identicalTypes(memberType(m), memberType(rm(k)))))

        case (PointerT(l), PointerT(r)) => identicalTypes(l, r)

        case (FunctionT(larg, lr), FunctionT(rarg, rr)) =>
          larg.size == rarg.size && larg.zip(rarg).forall {
            case (l, r) => identicalTypes(l, r)
          } && identicalTypes(lr, rr)

        case (MapT(lk, le), MapT(rk, re)) => identicalTypes(lk, rk) && identicalTypes(le, re)

        case (ChannelT(le, lm), ChannelT(re, rm)) => identicalTypes(le, re) && lm == rm

//        case (InternalTupleT(lv), InternalTupleT(rv)) =>
//          lv.size == rv.size && lv.zip(rv).forall {
//            case (l, r) => identicalTypes(l, r)
//          }

        case _ => false
      }
      case _ => false
    }

    /**
      * Executability
      */

    lazy val isExecutable: Property[PExpression] = createBinaryProperty("executable") {
      case PCall(callee, _) => !isBuildIn(callee)
      case _ => false
    }

    // TODO: probably will be unneccessary because build int functions always have to be called

    lazy val isBuildIn: Property[PExpression] = createBinaryProperty("buit-in") {
      case t: PBuildIn => true
      case _ => false
    }

    /**
      * Constant Evaluation
      */

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
          case SingleConstant(exp, _) => intConstantEval(exp)
          case _ => None
        }

        case _ => None
      }

    /**
      * Reference Identity
      */

    def satisfies(n: PIdnNode)(f: Entity ==> Boolean): Boolean = f.lift(entity(n)).getOrElse(false)

    lazy val pointsToData: Property[PIdnNode] = createBinaryProperty("use of a variable or constant"){
      n => satisfies(n){ case _: DataEntity => true}
    }

    lazy val pointsToType: Property[PIdnNode] = createBinaryProperty("use of a Type"){
      n => satisfies(n){ case _: TypeEntity => true}
    }

    /**
      * Enclosing
      */

    lazy val enclosingScope: PNode => PScope =
      down((_: PNode) => violation("node does not root in a scope")) { case s: PScope => s }


    lazy val enclosingCodeRoot: PStatement => PCodeRoot =
      down((_: PNode) => violation("Statement does not root in a CodeRoot")) { case m: PCodeRoot => m }

    def typeSwitchConstraints(id: PIdnNode): Vector[PType] =
      typeSwitchConstraintsLookup(id)(id)

    lazy val typeSwitchConstraintsLookup: PIdnNode => PNode => Vector[PType] =
      paramAttr[PIdnNode, PNode, Vector[PType]] { id => {
        case tree.parent.pair(PTypeSwitchCase(left, _), s: PTypeSwitchStmt)
          if s.binder.exists(_.name == id.name) => left

        case s: PTypeSwitchStmt // Default case
          if s.binder.exists(_.name == id.name) => Vector.empty

        case tree.parent(p) => typeSwitchConstraintsLookup(id)(p)
      }
      }

    /**
      * Resolver
      */

    def resolveConversionOrUnaryCall[T](n: PConversionOrUnaryCall)
                                       (conversion: (PIdnUse, PExpression) => T)
                                       (unaryCall: (PIdnUse, PExpression) => T): Option[T] =
      if (pointsToType(n.base))      Some(conversion(n.base, n.arg))
      else if (pointsToData(n.base)) Some(unaryCall(n.base, n.arg))
      else None

    def resolveSelectionOrMethodExpr[T](n: PSelectionOrMethodExpr)
                                       (selection: (PIdnUse, PIdnUse) => T)
                                       (methodExp: (PIdnUse, PIdnUse) => T): Option[T] =
      if (pointsToType(n.base))      Some(methodExp(n.base, n.id))
      else if (pointsToData(n.base)) Some(selection(n.base, n.id))
      else None

    def isDef[T](n: PIdnUnk): Boolean = !isDefinedInEnv(sequentialDefenv.in(n), serialize(n))

    /**
      * Implements
      */

    def implements(l: Type, r: Type): Boolean = false

    /**
      * Method and Selection Set
      */

    class MemberSet private(
                             private val internal: Map[String, (TypeMember, Vector[MemberPath], Int)]
                             , private val duplicates: Set[String]
                           ) {

      type Record = (TypeMember, Vector[MemberPath], Int)

      def rank(path: Vector[MemberPath]): Int = path.count {
        case _: MemberPath.Next => true
        case _ => false
      }

      def union(other: MemberSet): MemberSet = {
        val keys = internal.keySet ++ other.internal.keySet
        val (newMap, newDups) = keys.map{k => (internal.get(k), other.internal.get(k)) match {
          case (Some(l@(_, _, rl)), Some(r@(_, _, rr))) =>
            (k -> (if (rl < rr) l else r), if (rl == rr) Some(k) else None)

          case (Some(l), None) => (k -> l, None)
          case (None, Some(r)) => (k -> r, None)
          case (None, None) => violation("Key not used by operand of union")
        }}.unzip

        new MemberSet(newMap.toMap, duplicates ++ other.duplicates ++ newDups.flatten)
      }

      private def updatePath(f: (TypeMember, Vector[MemberPath], Int) => (TypeMember, Vector[MemberPath], Int)): MemberSet =
        new MemberSet(internal.mapValues(f.tupled), duplicates)

      def surface: MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Underlying +: p, l) }

      def promote(f: Embbed): MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Next(f) +: p, l + 1) }

      def ref: MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Deref +: p, l) }

      def deref: MemberSet = updatePath { case (m, p, l) => (m, MemberPath.Ref +: p, l) }


      def lookup(key: String): Option[TypeMember] = internal.get(key).map(_._1)

      def lookupWithPath(key: String): Option[(TypeMember, Vector[MemberPath])] =
        internal.get(key).map(r => (r._1, r._2))

      def filter(f: TypeMember => Boolean): MemberSet =
        new MemberSet(internal.filterKeys(n => f(internal(n)._1)), duplicates)

      def methodSet: MemberSet = filter(m => m.isInstanceOf[Method])

      def collect[T](f: (String, TypeMember) ==> T): Vector[T] = internal.collect {
        case (n, (m, _, _)) if f.isDefinedAt(n, m) => f(n, m)
      }(breakOut)

      def implements(other: MemberSet): Boolean = other.internal.keySet.forall(internal.contains)

      def toMap: Map[String, TypeMember] = internal.mapValues(_._1)

      def valid: Boolean = duplicates.isEmpty

      def errors(src: PType): Messages = {
        duplicates.flatMap(n => message(src, s"type $src has member $n more than once"))(breakOut)
      }
    }

    object MemberSet {

      def init(s: TraversableOnce[TypeMember]): MemberSet = {
        val nmp: Vector[(String, TypeMember)] = s.map {
          case e@ MethodImpl(m) => m.id.name -> e
          case e@ MethodSpec(m) => m.id.name -> e
          case e@ Field(m)      => m.id.name -> e
          case e@ Embbed(m)     => m.id.name -> e
        }.toVector

        val groups = nmp.unzip._1.groupBy(identity)
        val member = nmp.toMap

        val dups: Set[String] = groups.collect{ case (x, ys) if ys.size > 1 => x }(breakOut)
        val distinct = groups.keySet

        new MemberSet(distinct.map(n => n -> (member(n), Vector.empty[MemberPath], 0)).toMap, dups)
      }

      def empty: MemberSet = new MemberSet(Map.empty[String, (TypeMember, Vector[MemberPath], Int)], Set.empty[String])

      def union(mss: Vector[MemberSet]): MemberSet = mss.size match {
        case 0 => empty
        case 1 => mss.head
        case _ => mss.tail.fold(mss.head) { case (l, r) => l union r }
      }
    }


    sealed trait MemberPath

    object MemberPath {

      case object Underlying extends MemberPath
      case object Deref extends MemberPath
      case object Ref extends MemberPath
      case class Next(decl: Embbed) extends MemberPath
    }

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
              entity(e.typ.id) match {
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

    def promotedMemberSetGen(transformer: Type => Type): Type => MemberSet = {
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

    /**
      * Debug
      */

    def containsUse(n: PNode, ref: String): Boolean = n match {
      case PIdnUse(`ref`) => true
      case _ => children(n) exists (containsUse(_, ref))
    }

    @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
    @inline
    def violation(cond: Boolean, msg: => String): Unit = if (!cond) violation(msg)

    @scala.annotation.elidable(scala.annotation.elidable.ASSERTION)
    @inline
    def violation(msg: String): Nothing = throw new java.lang.IllegalStateException(s"Logic error: $msg")

  }

  private object SymbolTable extends Environments {

    sealed trait Regular extends Entity with Product

    sealed trait DataEntity extends Regular

    case class Function(decl: PFunctionDecl) extends DataEntity

    sealed trait Constant extends DataEntity

    case class SingleConstant(exp: PExpression, opt: Option[PType]) extends Constant
    case class MultiConstant(idx: Int, exp: PExpression) extends Constant

    sealed trait Variable extends DataEntity

    case class SingleLocalVariable(exp: PExpression, opt: Option[PType]) extends Variable
    case class MultiLocalVariable(idx: Int, exp: PExpression) extends Variable
    case class InParameter(decl: PNamedParameter) extends Variable
    case class ReceiverParameter(decl: PNamedReceiver) extends Variable
    case class OutParameter(decl: PNamedParameter) extends Variable
    case class TypeSwitchVariable(decl: PTypeSwitchStmt) extends Variable
    case class RangeVariable(idx: Int, exp: PRange) extends Variable


    sealed trait TypeEntity extends Regular

    case class NamedType(decl: PTypeDef) extends TypeEntity
    case class TypeAlias(decl: PTypeAlias) extends TypeEntity


    sealed trait TypeMember extends Regular

    case class Field(decl: PFieldDecl) extends TypeMember
    case class Embbed(decl: PEmbeddedDecl) extends TypeMember

    sealed trait Method extends TypeMember

    case class MethodImpl(decl: PMethodDecl) extends Method
    case class MethodSpec(spec: PMethodSpec) extends Method


    case class Package(decl: PQualifiedImport) extends Regular

    case class Label(decl: PLabeledStmt) extends Regular

    case class Wildcard() extends Regular

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

    case class InternalSingleMulti(sin: Type, mul: InternalTupleT) extends Type


    sealed trait TypeContext {
      def unapply(arg: Type): Option[Type]
    }

    case object Argument extends TypeContext {
      override def unapply(arg: Type): Option[Type] = arg match {
        case t: InternalSingleMulti => Some(t.sin)
        case UnknownType => None
        case t => Some(t)
      }
    }

    case object Assign extends TypeContext {
      override def unapply(arg: Type): Option[Type] = arg match {
        case t: InternalSingleMulti => Some(t.mul)
        case UnknownType => None
        case t => Some(t)
      }
    }

    case object Single extends TypeContext {
      override def unapply(arg: Type): Option[Type] = arg match {
        case InternalSingleMulti(sin, _) => Some(sin)
        case _: InternalTupleT => None
        case UnknownType => None
        case t => Some(t)
      }
    }

    //    case class InternalSumT(ts: Vector[Type]) extends Type {
    //      lazy val flattenedTs: Vector[Type] = ts flatMap {
    //        case t: InternalSumT => t.flattenedTs
    //        case t => Vector(t)
    //      }
    //      def find[T](f: Type ==> T): Option[T] = flattenedTs.collectFirst(f)
    //    }

  }

  private implicit class OptionPlus[A](self: Option[A]) {
    def \>(other: => Option[A]): Option[A] = if (self.isDefined) self else other
  }

  private implicit class OptionJoin[A](self: Option[Option[A]]) {
    def join: Option[A] = if (self.isDefined) self.get else None
  }


}