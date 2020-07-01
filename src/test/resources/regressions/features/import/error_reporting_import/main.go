package main

// ##(-I src/test/resources/regressions/features/import/error_reporting_import)
//:: ExpectedOutput(type_error)
import b "bar"

func foo() {
  b.bar()
}
