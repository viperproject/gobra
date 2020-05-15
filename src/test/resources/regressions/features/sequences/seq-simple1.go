package pkg

// ghost parameter of a sequence type
func example1(ghost xs seq[int]) {
}

// ghost function with a ghost-type parameter
ghost func example2(xs seq[int]) {
}

// ghost variable declaration
func example3() {
  ghost var xs seq[int]
}

// ghost assignment (just to be sure this works)
func example4(ghost xs seq[int]) {
  ghost ys := xs
}

// (ghost) output parameter of a sequence type
func example5(n int) (ghost xs seq[bool]) {
}

// an example of sequence nesting
func example6(ghost xs seq[seq[seq[int]]]) {
}

// empty integer sequence literal
func example7() {
  ghost xs := seq[int] { }
}

// singular integer sequence literal
func example8() {
  ghost xs := seq[int] { 1 }
}

// integer sequence literal
func example9() {
  ghost xs := seq[int] { 1, 17, 142 }
}

// Boolean sequence literal
func example10() {
  ghost xs := seq[bool] { true, false, true }
}

// nested sequences
func example11() {
  ghost xs := seq[seq[int]] { seq[int] { 1 }, seq[int] { 17, 142 } }
}

