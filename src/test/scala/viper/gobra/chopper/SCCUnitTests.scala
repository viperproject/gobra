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
  
  test("Component 0: should find strongly connected components as expected") {
    compareChopped (ViperChopper.SCC.components(Seq("A"), Seq.empty), Vector(
      ViperChopper.SCC.Component(Seq("A"))
    ))
  }
    
  test("Component 1: should find strongly connected components as expected") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "C"),
      ("D", "E")
    )
    compareChopped (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Seq("A")),
      ViperChopper.SCC.Component(Seq("B")),
      ViperChopper.SCC.Component(Seq("C", "D")),
      ViperChopper.SCC.Component(Seq("E"))
    ))
  }

  test("Component 2: should find strongly connected components as expected") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "B"),
      ("D", "C"),
      ("D", "E")
    )
    compareChopped (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Seq("A")),
      ViperChopper.SCC.Component(Seq("D", "C", "B")),
      ViperChopper.SCC.Component(Seq("E"))
    ))
  }

  test("Component 3: should find strongly connected components as expected") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "E")
    )
    compareChopped (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Seq("A")),
      ViperChopper.SCC.Component(Seq("B")),
      ViperChopper.SCC.Component(Seq("C")),
      ViperChopper.SCC.Component(Seq("D")),
      ViperChopper.SCC.Component(Seq("E"))
    ))
  }

  test("Compute: should find strongly connected components and helper data as expected") {
    val vertices = Seq("A", "B", "C", "D", "E")
    val edges = Seq(
      ("A", "C"),
      ("B", "C"),
      ("C", "D"),
      ("D", "C"),
      ("D", "E")
    )
    val choppedReference = Vector(
      ViperChopper.SCC.Component(Seq("A")),
      ViperChopper.SCC.Component(Seq("B")),
      ViperChopper.SCC.Component(Seq("C", "D")),
      ViperChopper.SCC.Component(Seq("E"))
    )
    val (components, inverse, dag) = ViperChopper.SCC.compute(vertices, edges)
    compareChopped(components, choppedReference)
    inverse shouldEqual Map(
      "A" -> ViperChopper.SCC.Component(Seq("A")),
      "B" -> ViperChopper.SCC.Component(Seq("B")),
      "C" -> ViperChopper.SCC.Component(Seq("D", "C")),
      "D" -> ViperChopper.SCC.Component(Seq("D", "C")),
      "E" -> ViperChopper.SCC.Component(Seq("E")),
    )
    dag shouldEqual Seq(
      (ViperChopper.SCC.Component(Seq("A")), ViperChopper.SCC.Component(Seq("D", "C"))),
      (ViperChopper.SCC.Component(Seq("B")), ViperChopper.SCC.Component(Seq("D", "C"))),
      (ViperChopper.SCC.Component(Seq("D", "C")), ViperChopper.SCC.Component(Seq("E")))
    )
  }
}