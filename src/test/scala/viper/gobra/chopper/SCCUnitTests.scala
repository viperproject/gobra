// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.
//
// Copyright (c) 2011-2020 ETH Zurich.

package viper.gobra.chopper

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import viper.gobra.util.ViperChopper
import viper.gobra.util.ViperChopper.SCC.Component

class SCCUnitTests extends AnyFunSuite with Matchers with Inside {

  def compareChopped[T](one: Vector[Component[T]], other: Vector[Component[T]]): Unit = {
    one.map(c => c.nodes.toSet).toSet shouldEqual other.map(c => c.nodes.toSet).toSet
  }
    
  test("Component 1: should find strongly connected components as expected") {
    val graph = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "C"),
      ("D", "E")
    )
    compareChopped (ViperChopper.SCC.components(graph), Vector(
      new ViperChopper.SCC.Component(Seq("A")),
      new ViperChopper.SCC.Component(Seq("B")),
      new ViperChopper.SCC.Component(Seq("C", "D")),
      new ViperChopper.SCC.Component(Seq("E"))
    ))
  }

  test("Component 2: should find strongly connected components as expected") {
    val graph = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "B"),
      ("D", "C"),
      ("D", "E")
    )
    compareChopped (ViperChopper.SCC.components(graph), Vector(
      new ViperChopper.SCC.Component(Seq("A")),
      new ViperChopper.SCC.Component(Seq("D", "C", "B")),
      new ViperChopper.SCC.Component(Seq("E"))
    ))
  }

  test("Component 3: should find strongly connected components as expected") {
    val graph = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "E")
    )
    compareChopped (ViperChopper.SCC.components(graph), Vector(
      new ViperChopper.SCC.Component(Seq("A")),
      new ViperChopper.SCC.Component(Seq("B")),
      new ViperChopper.SCC.Component(Seq("C")),
      new ViperChopper.SCC.Component(Seq("D")),
      new ViperChopper.SCC.Component(Seq("E"))
    ))
  }

  test("Compute: should find strongly connected components and helper data as expected") {
    val graph = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "C"),
      ("D", "E")
    )
    val choppedReference = Vector(
      new ViperChopper.SCC.Component(Seq("A")),
      new ViperChopper.SCC.Component(Seq("B")),
      new ViperChopper.SCC.Component(Seq("C", "D")),
      new ViperChopper.SCC.Component(Seq("E"))
    )
    val (components, inverse, dag) = ViperChopper.SCC.compute(graph)
    compareChopped(components, choppedReference)
    inverse shouldEqual Map(
      "A" -> new ViperChopper.SCC.Component(Seq("A")),
      "B" -> new ViperChopper.SCC.Component(Seq("B")),
      "C" -> new ViperChopper.SCC.Component(Seq("D", "C")),
      "D" -> new ViperChopper.SCC.Component(Seq("D", "C")),
      "E" -> new ViperChopper.SCC.Component(Seq("E")),
    )
    dag shouldEqual Seq(
      (new ViperChopper.SCC.Component(Seq("A")), new ViperChopper.SCC.Component(Seq("D", "C"))),
      (new ViperChopper.SCC.Component(Seq("B")), new ViperChopper.SCC.Component(Seq("D", "C"))),
      (new ViperChopper.SCC.Component(Seq("D", "C")), new ViperChopper.SCC.Component(Seq("E")))
    )
  }

  test("Paths: should find paths in forest as expected") {
    val graph = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("C", "G"),
      ("D", "E"),
      ("D", "F"),
      ("G", "F")
    )
    ViperChopper.Paths.paths("A", graph) shouldEqual Seq(
      Seq("A", "C", "D", "E"),
      Seq("A", "C", "D", "F"),
      Seq("A", "C", "G", "F")
    )
    ViperChopper.Paths.paths("B", graph) shouldEqual Seq(
      Seq("B", "C", "D", "E"),
      Seq("B", "C", "D", "F"),
      Seq("B", "C", "G", "F")
    )
  }
}