package main2

// ##(-I src/test/resources/regressions/features/import/unqualified_import/functions)
import . "bar2_1"
import . "bar2_2"

func foo() {
  //:: ExpectedOutput(type_error)
  bar() // bar2_1 and bar2_2 both define a function bar
}
