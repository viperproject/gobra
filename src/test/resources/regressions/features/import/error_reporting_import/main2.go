package main

// ##(-I src/test/resources/regressions/features/import/error_reporting_import)
import f "foo"

func client() {
  //:: ExpectedOutput(type_error)
  f.inexistent()
}
