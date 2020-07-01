package main

// ##(-I src/test/resources/regressions/features/import/multi-package-folder)
// a type error is expected here as the bar folder contains packages foo and bar
//:: ExpectedOutput(type_error)
import b "bar"

func foo() {
  b.bar()
}
