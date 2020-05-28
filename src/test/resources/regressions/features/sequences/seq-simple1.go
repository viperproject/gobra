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

