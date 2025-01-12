// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2025 ETH Zurich.

package viper.gobra.util

import viper.silver.ast.utility.chopper.{ChopperLike, Cut, Edges, SCC, Vertices, ViperGraphs}

// Chopper with computation of dependencies extended to support the termination plugin and other plugins
// used by Gobra.
object PluginAwareChopper extends ChopperLike with ViperGraphs with PluginAwareEdges with Vertices with Cut with SCC

trait PluginAwareEdges extends Edges {
  this: Vertices =>

  import viper.silver.ast
  import viper.silver.ast.utility.chopper.Edges.Edge

  override def dependencies(member: ast.Member): Seq[Edge[Vertices.Vertex]] = member match {
    case d: ast.Domain if d.name.endsWith("WellFoundedOrder") =>
      val defVertex = toDefVertex(member)
      val useVertex = toUseVertex(member)

      val usageDependencies = {
        // If we are computing dependencies before the termination plugin has run, then
        // we conservatively include all domains that define well founded orders for
        // termination checking, given that, at this point, there is no explicit dependency between
        // the expressions in the termination measure and the "bounded" and "decreasing" functions defined
        // in the domains that establish a well-founded order, and these domains end up being removed,
        // leading to errors.
        val domainVertex = toDefVertex(d)
        val axiomDeps = d.axioms.flatMap { ax =>
          val axVertex = Vertices.DomainAxiom(ax, d)
          val dependenciesOfAxiom = dependenciesToChildren(ax.exp, axVertex)
          val dependenciesToAxiom = Seq(domainVertex -> axVertex)
          dependenciesOfAxiom ++ dependenciesToAxiom
        }
        val funcDeps = d.functions.flatMap { f =>
          val fVertex = Vertices.DomainFunction(f.name)
          val dependenciesOfFunction = dependenciesToChildren(f, fVertex)
          val dependenciesToFunction = Seq(domainVertex -> fVertex)
          dependenciesOfFunction ++ dependenciesToFunction

        }
        (Vertices.Always -> domainVertex) +: (axiomDeps ++ funcDeps)
      }
      // to ensure that nodes that depend on Vertex.Always are indeed always included
      val alwaysDependencies = Seq(defVertex -> Vertices.Always, useVertex -> Vertices.Always)
      usageDependencies ++ alwaysDependencies
    case _ => super.dependencies(member)
  }
}