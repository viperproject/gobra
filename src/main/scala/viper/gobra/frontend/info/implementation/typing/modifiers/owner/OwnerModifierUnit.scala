package viper.gobra.frontend.info.implementation.typing.modifiers.owner

import org.bitbucket.inkytonik.kiama.util.Messaging.noMessages
import viper.gobra.ast.frontend.{PAccess, PBefore, PBinaryExp, PBitNegation, PBlankIdentifier, PCapacity, PClosureImplements, PConditional, PDeref, PDot, PExists, PExpression, PForall, PGhostCollectionExp, PGhostEquals, PGhostUnequals, PIdnNode, PImplication, PIn, PIndexedExp, PIntersection, PInvoke, PIota, PIsComparable, PLabeledOld, PLength, PLiteral, PMagicWand, PMake, PMapKeys, PMapValues, PMatchExp, PMultiplicity, PNamedOperand, PNegation, PNew, PNode, POld, POptionGet, POptionNone, POptionSome, PParameter, PPermission, PPredConstructor, PPredicateAccess, PRangeSequence, PReceive, PReference, PSequenceAppend, PSetMinus, PSliceExp, PSubset, PTypeAssertion, PTypeExpr, PTypeOf, PUnfolding, PUnion, PUnpackSlice, AstPattern => ap}
import viper.gobra.frontend.info.base.Type._
import viper.gobra.frontend.info.base.{SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.frontend.info.implementation.resolution.MemberPath
import viper.gobra.frontend.info.implementation.typing.modifiers.ModifierUnit
import viper.gobra.util.Violation

class OwnerModifierUnit(final val ctx: TypeInfoImpl) extends ModifierUnit[OwnerModifier] {

  override def hasWellDefModifier: WellDefinedness[PNode] = createIndependentWellDef[PNode] {
    case _ => noMessages
  }(n => ctx.children(n).forall(ctx.childrenWellDefined))

  override def getModifier: ModifierTyping[PNode, OwnerModifier] = createModifier[PNode, OwnerModifier] {
    case PNamedOperand(id) => getVarModifier(id)
    case PBlankIdentifier() => OwnerModifier.defaultValue
    case _: PTypeExpr => OwnerModifier.defaultValue
    case _: PDeref => OwnerModifier.dereference
    case PIndexedExp(base, _) =>
      val baseType = ctx.underlyingType(ctx.exprType(base))
      baseType match {
        case _: SliceT | _: GhostSliceT => OwnerModifier.sliceLookup
        case _: VariadicT => OwnerModifier.variadicLookup
        case _: ArrayT => OwnerModifier.arrayLookup(getModifier(base).get)
        case _: SequenceT => OwnerModifier.mathDataStructureLookup
        case _: MathMapT => OwnerModifier.mathDataStructureLookup
        case _: MapT => OwnerModifier.mapLookup
        case t => Violation.violation(s"Expected slice, array, map, or sequence, but got $t")
      }
    case n: PDot => ctx.resolve(n) match {
      case Some(s: ap.FieldSelection) => OwnerModifier.fieldLookup(getMemberPathModifier(getModifier(s.base).get, s.path))
      case Some(_: ap.Constant) => OwnerModifier.constant
      case Some(_: ap.ReceivedMethod | _: ap.MethodExpr | _: ap.ReceivedPredicate | _: ap.PredicateExpr | _: ap.AdtField) => OwnerModifier.rValue
      case Some(_: ap.NamedType | _: ap.BuiltInType | _: ap.Function | _: ap.Predicate | _: ap.DomainFunction) => OwnerModifier.rValue
      case Some(_: ap.ImplicitlyReceivedInterfaceMethod | _: ap.ImplicitlyReceivedInterfacePredicate) => OwnerModifier.rValue
      case Some(g: ap.GlobalVariable) => if (g.symb.shared) OwnerModifier.Shared else OwnerModifier.Exclusive
      case p => Violation.violation(s"Unexpected dot resolve, got $p")
    }
    case _: PLiteral => OwnerModifier.literal
    case _: PIota => OwnerModifier.iota
    case n: PInvoke => ctx.resolve(n) match {
      case Some(_: ap.Conversion) => OwnerModifier.conversionResult
      case Some(_: ap.FunctionCall) => OwnerModifier.callResult
      case Some(_: ap.ClosureCall) => OwnerModifier.callResult
      case Some(_: ap.PredicateCall) => OwnerModifier.rValue
      case p => Violation.violation(s"Unexpected invoke resolve, got $p")
    }
    case _: PLength | _: PCapacity => OwnerModifier.callResult
    case _: PSliceExp => OwnerModifier.sliceExpr
    case _: PTypeAssertion => OwnerModifier.typeAssertionResult
    case _: PReceive => OwnerModifier.receive
    case _: PReference => OwnerModifier.reference
    case _: PNegation => OwnerModifier.rValue
    case _: PBitNegation => OwnerModifier.rValue
    case _: PBinaryExp[_, _] => OwnerModifier.rValue
    case _: PGhostEquals => OwnerModifier.rValue
    case _: PGhostUnequals => OwnerModifier.rValue
    case _: PPermission => OwnerModifier.rValue
    case _: PPredConstructor => OwnerModifier.rValue
    case n: PUnfolding => OwnerModifier.unfolding(getModifier(n.op).get)
    case _: POld | _: PLabeledOld | _: PBefore => OwnerModifier.old
    case _: PConditional | _: PImplication | _: PForall | _: PExists => OwnerModifier.rValue
    case _: PAccess | _: PPredicateAccess | _: PMagicWand => OwnerModifier.rValue
    case _: PClosureImplements => OwnerModifier.rValue
    case _: PTypeOf | _: PIsComparable => OwnerModifier.rValue
    case _: PIn | _: PMultiplicity | _: PSequenceAppend |
         _: PGhostCollectionExp | _: PRangeSequence | _: PUnion | _: PIntersection |
         _: PSetMinus | _: PSubset | _: PMapKeys | _: PMapValues => OwnerModifier.rValue
    case _: POptionNone | _: POptionSome | _: POptionGet => OwnerModifier.rValue
    case _: PMatchExp => OwnerModifier.rValue
    case _: PMake | _: PNew => OwnerModifier.make
    case _: PUnpackSlice => OwnerModifier.rValue
    case id: PIdnNode => getVarModifier(id)
    case _: PParameter => OwnerModifier.inParameter
    // case _ => OwnerModifier.Exclusive // conservative choice
  }(hasWellDefModifier)

  override def getFunctionLikeCallArgModifier: ModifierTyping[ap.FunctionLikeCall, Vector[OwnerModifier]] = createVectorModifier[ap.FunctionLikeCall, OwnerModifier](
    f => f.args.map(_ => OwnerModifier.Exclusive)
  )

  private def getVarModifier(n: PIdnNode): OwnerModifier = ctx.regular(n) match {
    case g: st.GlobalVariable => if (g.shared) OwnerModifier.sharedVariable else OwnerModifier.exclusiveVariable
    case v: st.Variable => if (v.shared) OwnerModifier.sharedVariable else OwnerModifier.exclusiveVariable
    case _: st.Constant => OwnerModifier.constant
    case _: st.Wildcard => OwnerModifier.defaultValue
    case e => Violation.violation(s"Expected variable, but got $e")
  }

  private def getMemberPathModifier(base: OwnerModifier, path: Vector[MemberPath]): OwnerModifier = {
    path.foldLeft(base) {
      case (b, MemberPath.Underlying) => b
      case (b, _: MemberPath.Next) => OwnerModifier.fieldLookup(b)
      case (b, _: MemberPath.EmbeddedInterface) => b
      case (_, MemberPath.Deref) => OwnerModifier.dereference
      case (_, MemberPath.Ref) => OwnerModifier.reference
    }
  }

  override def addressable(exp: PExpression): Boolean = getModifier(exp) match {
    case Some(OwnerModifier.Shared) => true
    case _ => false
  }
}
