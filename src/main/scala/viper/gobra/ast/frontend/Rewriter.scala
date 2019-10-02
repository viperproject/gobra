package viper.gobra.ast.frontend

import org.bitbucket.inkytonik.kiama.rewriting.{Cloner, PositionedRewriter}
import org.bitbucket.inkytonik.kiama.util.Positions

object Rewriter {

  trait AmbiguityResolver {
    def isType(x: PIdnNode): Boolean
    def isFPred(x: PIdnNode): Boolean
    def isMPred(x: PIdnNode): Boolean
  }

  class PRewriter(override val positions: Positions) extends PositionedRewriter with Cloner {
    implicit class PositionedPAstNode[N <: PNode](node: N) {
      def at(other: PNode): N = {
        positions.dupPos(other, node)
      }

      def range(from: PNode, to: PNode): N = {
        positions.dupRangePos(from, to, node)
      }

      def copy: N = deepclone(node)
    }

    /**
      * Resolver for the ambiguous parser-Ast nodes
      */

    // PSelectionOrMethodExpr

    def resolveSelectionOrMethodExpr(x: PSelectionOrMethodExpr, resolver: AmbiguityResolver): PExpression = {
      if (!resolver.isType(x.base)) asSelection(x) else asMethodExp(x)
    }

    def asSelection(x: PSelectionOrMethodExpr): PSelection = {
      val unrefBase = PNamedOperand(x.base).at(x.base)
      val base = if (x.refBase) PDereference(unrefBase).at(x.base) else unrefBase
      PSelection(base, x.id).at(x)
    }

    def asMethodExp(x: PSelectionOrMethodExpr): PMethodExpr = {
      val unrefBase = PDeclaredType(x.base).at(x.base)
      val base = if (x.refBase) PMethodReceivePointer(unrefBase).at(x.base)
                           else PMethodReceiveName(unrefBase).at(x.base)
      PMethodExpr(base, x.id).at(x)
    }

    // PConversionOrUnaryCall

    def resolveConversionOrUnayCall(x: PConversionOrUnaryCall, resolver: AmbiguityResolver): PExpression = {
      if (!resolver.isType(x.base)) asUnaryCall(x) else asConversion(x)
    }

    def asConversion(x: PConversionOrUnaryCall): PConversion = {
      PConversion(PDeclaredType(x.base).at(x.base), x.arg).at(x)
    }

    def asUnaryCall(x: PConversionOrUnaryCall): PCall = {
      PCall(PNamedOperand(x.base).at(x.base), Vector(x.arg)).at(x)
    }

    // PFPredOrBoolFuncCall

    def resolveFPredOrBoolFuncCall(x: PFPredOrBoolFuncCall, resolver: AmbiguityResolver): PAssertion = {
      if (resolver.isFPred(x.id)) asFPredCall(x) else asBoolFuncCall(x)
    }

    def asFPredCall(x: PFPredOrBoolFuncCall): PFPredCall = {
      PFPredCall(x.id, x.args).at(x)
    }

    def asBoolFuncCall(x: PFPredOrBoolFuncCall): PExprAssertion = {
      PExprAssertion(PCall(PNamedOperand(x.id).at(x.id), x.args).at(x)).at(x)
    }

    // PMPredOrBoolMethCall

    def resolveMPredOrBoolMethCall(x: PMPredOrBoolMethCall, resolver: AmbiguityResolver): PAssertion = {
      if (resolver.isMPred(x.id)) asMPredCall(x) else asBoolMethodCall(x)
    }

    def asMPredCall(x: PMPredOrBoolMethCall): PMPredCall = {
      PMPredCall(x.recv, x.id, x.args).at(x)
    }

    def asBoolMethodCall(x: PMPredOrBoolMethCall): PExprAssertion = {
      PExprAssertion(PCall(PSelection(x.recv, x.id).range(x.recv, x.id), x.args).at(x)).at(x)
    }

    // PMPredOrMethExprCall

    def resolveMPredOrMethExprCall(x: PMPredOrMethExprCall, resolver: AmbiguityResolver): PAssertion = {
      if (resolver.isMPred(x.id)) asMPredExprCall(x) else asBoolMethodExprCall(x)
    }

    def asMPredExprCall(x: PMPredOrMethExprCall): PMPredExprCall = {
      PMPredExprCall(x.base, x.id, x.args).at(x)
    }

    def asBoolMethodExprCall(x: PMPredOrMethExprCall): PExprAssertion = {
      PExprAssertion(PCall(PMethodExpr(x.base, x.id).range(x.base, x.id), x.args).at(x)).at(x)
    }

    // PMPredOrMethRecvOrExprCall

    def resolveMPredOrMethRecvOrExprCall(x: PMPredOrMethRecvOrExprCall, resolver: AmbiguityResolver): PAssertion = {
      if (resolver.isMPred(x.id)) {
        if (!resolver.isType(x.base)) asMPredCall(x) else asMPredExprCall(x)
      } else {
        if (!resolver.isType(x.base)) asBoolMethodCall(x) else asBoolMethodExprCall(x)
      }
    }

    def asMPredCall(x: PMPredOrMethRecvOrExprCall): PMPredCall = {
      val unrefBase = PNamedOperand(x.base).at(x.base)
      val base = if (x.refBase) PDereference(unrefBase).at(x.base) else unrefBase
      PMPredCall(base, x.id, x.args).at(x)
    }

    def asBoolMethodCall(x: PMPredOrMethRecvOrExprCall): PExprAssertion = {
      val unrefBase = PNamedOperand(x.base).at(x.base)
      val base = if (x.refBase) PDereference(unrefBase).at(x.base) else unrefBase
      PExprAssertion(PCall(PSelection(base, x.id).range(base, x.id), x.args).at(x)).at(x)
    }

    def asMPredExprCall(x: PMPredOrMethRecvOrExprCall): PMPredExprCall = {
      val unrefBase = PDeclaredType(x.base).at(x.base)
      val base = if (x.refBase) PMethodReceivePointer(unrefBase).at(x.base)
                           else PMethodReceiveName(unrefBase).at(x.base)
      PMPredExprCall(base, x.id, x.args).at(x)
    }

    def asBoolMethodExprCall(x: PMPredOrMethRecvOrExprCall): PExprAssertion = {
      val unrefBase = PDeclaredType(x.base).at(x.base)
      val base = if (x.refBase) PMethodReceivePointer(unrefBase).at(x.base)
                           else PMethodReceiveName(unrefBase).at(x.base)
      PExprAssertion(PCall(PMethodExpr(base, x.id).range(x.base, x.id), x.args).at(x)).at(x)
    }

  }
}
