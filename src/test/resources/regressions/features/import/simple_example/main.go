package main

// ##(-I src/test/resources/regressions/features/import/simple_example)
import b "bar"

func foo() {
  b.bar()
}
