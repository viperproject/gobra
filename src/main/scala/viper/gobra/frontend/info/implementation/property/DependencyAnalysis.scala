// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import viper.gobra.ast.frontend.{PDot, PExpression, PNamedOperand, PNode, PVarDecl, AstPattern => ap}
import viper.gobra.frontend.info.base.{Type, SymbolTable => st}
import viper.gobra.frontend.info.implementation.TypeInfoImpl
import viper.gobra.util.Violation

import scala.collection.mutable

trait DependencyAnalysis extends BaseProperty { this: TypeInfoImpl =>

  /**
    * Checks that a global declaration is acyclic.
    */
  lazy val acyclicGlobalDeclaration: Property[PVarDecl] = createProperty[PVarDecl]{ c =>
    val results = c.left.map{ l =>
      entity(l) match {
        case g: st.GlobalVariable =>
          samePackageDependenciesGlobals(g) match {
            case Right(gDeps) if gDeps.contains(g) => failedProp(s"The declaration of $l is cyclical")
            case Right(_) => successProp
            case Left(errs) => failedProp(errs)
          }
        case _: st.Wildcard =>
          // Wildcard declaration can never be cyclical
          successProp
        case e => Violation.violation(s"Unexpected entity $e found")
      }
    }
    PropertyResult.bigAnd(results)
  }

  /**
    * Find all dependencies of a global variable, or an error message if the analysis cannot be carried out to
    * completion
    */
  protected def samePackageDependenciesGlobals: st.GlobalVariable => Either[String, Vector[st.GlobalVariable]] = {
    _.expOpt.toVector.map(globalsExprDependsOn).fold(Right(Vector())){
      case (l, r) => for { lVars <- l; rVars <- r } yield lVars ++ rVars
    }
  }

  /**
    * Finds all global variables on which an expression depends. Returns a list of errors if dependency
    * analysis cannot be carried out successfully.
    * @param exp
    * @return
    */
  def globalsExprDependsOn(exp: PExpression): Either[String, Vector[st.GlobalVariable]] = {
    val enclosingPkg = tryEnclosingPackage(exp).get
    val stack = mutable.Stack[PNode](exp)
    var visitedEntities = Seq[st.Regular]()
    // Stores all dependencies of a node (including itself).
    var dependencies = Seq[st.GlobalVariable]()
    var foundError: Option[String] = None
    val currentPackageContext = this

    // According to the Go language spec:
    //  Dependency analysis does not rely on the actual values of the variables, only on lexical
    //  references to them in the source, analyzed transitively.
    //  For instance, if a variable x's initialization expression refers to a function whose body
    //  refers to variable y then x depends on y. Specifically:
    //    - A reference to a variable or function is an identifier denoting that variable or function.
    //    - A reference to a method m is a method value or method expression of the form t.m, where the (static)
    //      type of t is not an interface type, and the method m is in the method set of t. It is immaterial
    //      whether the resulting function value t.m is invoked.
    //    - A variable, function, or method x depends on a variable y if x's initialization expression or
    //      body (for functions and methods) contains a reference to y or to a function or
    //      method that depends on y.
    while (stack.nonEmpty && foundError.isEmpty) {
      val elem = stack.pop()
      reflexiveAllChildren(elem).foreach{
        case n: PNamedOperand =>
          // If it is from another package (checked with _.symb.context == currentPackageContext), do not analyse.
          // If it is a function call, it is not a ghost call,
          // and it is not dynamically-bound, collect the method info if it has not been collected.
          // If it is a global variable, collect its dependencies if it has not been traversed yet.
          resolve(n) match {
            case Some(g: ap.GlobalVariable) if !isEnclosingGhost(n) && g.symb.context == currentPackageContext =>
              if (!visitedEntities.contains(g.symb)) {
                dependencies :+= g.symb
                visitedEntities :+= g.symb
                stack.push(g.symb.decl)
              }
            case Some(f: ap.Function) if !isEnclosingGhost(n) && f.symb.context == currentPackageContext =>
              if (!visitedEntities.contains(f.symb)) {
                visitedEntities :+= f.symb
                stack.push(f.symb.decl)
              }
            case _ =>
          }
        case n: PDot =>
          // If it is from another package (checked with _.symb.context == currentPackageContext), do not analyse.
          // If it is a reference to a method declared in the current package, it is not in a ghost-context,
          // and it is not dynamically-bound, collect the method info if it has not been collected.
          resolve(n) match {
            case Some(f: ap.ReceivedMethod) if !isEnclosingGhost(n) && underlyingType(typ(f.recv)).isInstanceOf[Type.InterfaceT] && f.symb.context == currentPackageContext =>
              foundError = Some(s"Calls to dynamically-bound non-ghost methods are not allowed in initialization code, but found $n.")
            case Some(f: ap.ReceivedMethod) if !isEnclosingGhost(n) && tryEnclosingPackage(n).contains(enclosingPkg) && f.symb.context == currentPackageContext =>
              if (!visitedEntities.contains(f.symb)) {
                visitedEntities :+= f.symb
                stack.push(f.symb.rep)
              }
            case Some(f: ap.MethodExpr) if !isEnclosingGhost(n) && underlyingType(symbType(f.typ)).isInstanceOf[Type.InterfaceT] && f.symb.context == currentPackageContext =>
              foundError = Some(s"Calls to dynamically-bound non-ghost methods are not allowed in initialization code, but found $n.")
            case Some(f: ap.MethodExpr) if !isEnclosingGhost(n) && tryEnclosingPackage(n).contains(enclosingPkg) && f.symb.context == currentPackageContext =>
              if (!visitedEntities.contains(f.symb)) {
                visitedEntities :+= f.symb
                stack.push(f.symb.rep)
              }
            case _ =>
          }
        case _ =>
      }
    }
    foundError.toLeft(dependencies.toVector)
  }

  private def reflexiveAllChildren(n: PNode): Vector[PNode] = n +: allChildren(n)
}
