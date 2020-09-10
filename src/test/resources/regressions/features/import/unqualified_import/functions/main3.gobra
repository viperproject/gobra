package main3

// ##(-I src/test/resources/regressions/features/import/unqualified_import/functions)
import . "bar3"

func foo() {
  assert bar() == 42 // this is fine and should call bar in this package even though bar3 defines another bar function
}

ensures res == 42
func bar() (res int) {
    return 42
}
