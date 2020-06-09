package main

// ##(-I src/test/resources/regressions/features/import/constant_import)
import b "bar"

func foo() {
  assert(b.Answer == 42)
}
