package main

// ##(-I src/test/resources/regressions/features/import/multi-package-folder)
// a type error is expected here as the bar folder contains packages foo and bar
//:: ExpectedOutput(parser_error)
import "bar"

func foo() {
  b.bar()
}
