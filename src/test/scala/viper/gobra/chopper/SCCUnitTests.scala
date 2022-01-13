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

  def compareComponent[T](one: Component[T], other: Component[T]): Unit = {
    one.nodes.toSet shouldEqual other.nodes.toSet
  }

  def compareComponents[T](one: Iterable[Component[T]], other: Iterable[Component[T]]): Unit = {
    one.map(c => c.nodes.toSet).toSet shouldEqual other.map(c => c.nodes.toSet).toSet
  }

  def compareDAG[T](one: Iterable[(Component[String], Component[String])], other: Iterable[(Component[String], Component[String])]): Unit = {
    one.map(c => (c._1.nodes.toSet, c._2.nodes.toSet)).toSet shouldEqual other.map(c => (c._1.nodes.toSet, c._2.nodes.toSet)).toSet
  }

  test("Component 0: should find strongly connected components as expected") {
    compareComponents (ViperChopper.SCC.components(Seq("A"), Seq.empty), Vector(
      ViperChopper.SCC.Component(Vector("A"))
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
    compareComponents (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("B")),
      ViperChopper.SCC.Component(Vector("C", "D")),
      ViperChopper.SCC.Component(Vector("E"))
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
    compareComponents (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("D", "C", "B")),
      ViperChopper.SCC.Component(Vector("E"))
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
    compareComponents (ViperChopper.SCC.components(vertices, edges), Vector(
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("B")),
      ViperChopper.SCC.Component(Vector("C")),
      ViperChopper.SCC.Component(Vector("D")),
      ViperChopper.SCC.Component(Vector("E"))
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
      ViperChopper.SCC.Component(Vector("A")),
      ViperChopper.SCC.Component(Vector("B")),
      ViperChopper.SCC.Component(Vector("C", "D")),
      ViperChopper.SCC.Component(Vector("E"))
    )
    val (components, inverse, dag) = ViperChopper.SCC.compute(vertices, edges)
    compareComponents(components, choppedReference)
    inverse.keySet shouldEqual Set("A", "B", "C", "D", "E")
    compareComponent(inverse("A"), Component(Vector("A")))
    compareComponent(inverse("B"), Component(Vector("B")))
    compareComponent(inverse("C"), Component(Vector("D", "C")))
    compareComponent(inverse("D"), Component(Vector("D", "C")))
    compareComponent(inverse("E"), Component(Vector("E")))
    compareDAG(dag, Seq(
      (ViperChopper.SCC.Component(Vector("A")), ViperChopper.SCC.Component(Vector("D", "C"))),
      (ViperChopper.SCC.Component(Vector("B")), ViperChopper.SCC.Component(Vector("D", "C"))),
      (ViperChopper.SCC.Component(Vector("D", "C")), ViperChopper.SCC.Component(Vector("E")))
    ))
  }
}