// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2022 ETH Zurich.

package viper.gobra.frontend.info.implementation.property

import org.bitbucket.inkytonik.kiama.util.Entity
import viper.gobra.ast.frontend.{PDot, PGlobalVarDecl, PNamedOperand, PNode, AstPattern => ap}
import viper.gobra.frontend.info.base.SymbolTable.{GlobalVariable, Regular, Wildcard}
import viper.gobra.frontend.info.base.Type
import viper.gobra.frontend.info.implementation.TypeInfoImpl

import scala.collection.mutable

trait Dependency extends BaseProperty { this: TypeInfoImpl =>

  // TODO: maybe move to program typing
  lazy val acyclicGlobalDeclaration: Property[PGlobalVarDecl] = createFlatProperty[PGlobalVarDecl](
    c => s"$c contains cyclic declarations"
  )(
    c => c.left.forall { l =>
      val e = entity(l) match {
        case r: Regular => r
        case _ => ??? // TODO: Violation
      }
      // TODO: quadratic, maybe optimize
      !samePackageDependenciesGlobals(e).exists(o => samePackageDependenciesGlobals(o).contains(e))
    }
  )

  // TODO: doc - finds lexical dependencies between global variables -> input and output consist all of glboal vars
  // TODO: Make it return Either[Messages, Vector[Entity]] (a pair could also be useful)
  lazy val samePackageDependenciesGlobals: Regular => Vector[Regular] = {
    attr[Regular, Vector[Regular]] {
      case v: GlobalVariable =>
        v.expOpt match {
          case None => Vector()
          case Some(exp) =>
            val stack = mutable.Stack[PNode](exp)
            var visitedEntities = Seq[Regular]() // TODO: should be visited dependencies
            // Stores all dependencies of a node (including itself).
            var dependencies = Seq[Regular]()

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
            while (stack.nonEmpty) {
              val elem = stack.pop()
              reflexiveAllChildren(elem) foreach {
                case n: PNamedOperand =>
                  // If it is a function call, it is not a ghost call,
                  // and it is not dynamically-bound, collect the method info if it has not been collected.
                  // If it is a global variable, collect its dependencies if it has not been traversed yet.
                  resolve(n) match {
                    // TODO: test with ghost calls to see if it makes a difference
                    case Some(g: ap.GlobalVariable) if !isEnclosingGhost(n) =>
                      if (!visitedEntities.contains(g.symb)) {
                        dependencies :+= g.symb
                        visitedEntities :+= g.symb
                        stack.push(g.symb.decl)
                      }
                    case Some(f: ap.Function) if !isEnclosingGhost(n) =>
                      if (!visitedEntities.contains(f.symb)) {
                        visitedEntities :+= f.symb
                        stack.push(f.symb.decl)
                      }
                    case _ =>
                  }
                case n: PDot =>
                  // If it is from another package, do not analyse.
                  // If it is a reference to a method declared in the current package, it is not in a ghost-context,
                  // and it is not dynamically-bound, collect the method info if it has not been collected.
                  // Otherwise, ignore.
                  // TODO: only check if it is in the same package
                  resolve(n) match {
                    // TODO: test with ghost calls to see if it makes a difference
                    case Some(f: ap.ReceivedMethod) if !isEnclosingGhost(n) && underlyingType(typ(f.recv)).isInstanceOf[Type.InterfaceT] =>
                      // TODO: report error
                      // occurrences of interface methods are not allowed during init
                      ???
                    case Some(f: ap.ReceivedMethod) if !isEnclosingGhost(n) =>
                      if (!visitedEntities.contains(f.symb)) {
                        visitedEntities :+= f.symb
                        stack.push(f.symb.rep) // TODO: check if this is the one
                      }
                    case Some(f: ap.MethodExpr) if !isEnclosingGhost(n) && underlyingType(symbType(f.typ)).isInstanceOf[Type.InterfaceT] =>
                      // TODO: report error
                      // occurrences of interface methods are not allowed during init
                      ???
                    case Some(f: ap.MethodExpr) if !isEnclosingGhost(n) =>
                      if (!visitedEntities.contains(f.symb)) {
                        visitedEntities :+= f.symb
                        stack.push(f.symb.rep) // TODO: check if this is the one
                      }
                    case _ =>
                  }
                  // TODO: only non-ghost references matter!
                  // TODO: non-ghost calls to interface methods disallowed
                case _ =>
              }
            }
          dependencies.toVector
      }
      case w: Wildcard =>
        // TODO: check if attribute is fine here
        ???
      case _ =>
        ??? // TODO Violation
    }
  }

  private def reflexiveAllChildren(n: PNode): Vector[PNode] = n +: allChildren(n)
}
