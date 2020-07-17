package main

// ##(-I src/test/resources/regressions/features/import/function_import)
import b "bar"

func foo() {
  b.bar()
}
