package main

// ##(-I src/test/resources/regressions/features/import/package_typings/spec_in_different_dir/spec)
// ##(-I src/test/resources/regressions/features/import/package_typings/spec_in_different_dir/lib)
import b "bar"

func foo() {
  assert(b.get42() == 42)
}
