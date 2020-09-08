package main

// ##(-I src/test/resources/regressions/features/import/omitted_package_name)
import "lib/foo"
import bar "bar"

func client() {
  assert(bar.Answer == 42)
  assert bar.DoesDeclOrderMatter && bar.BoolExprConst
  assert foo.Foo == 42
}
