package pkg

// empty integer sequence literal
func example1() {
  ghost xs := seq[int] { }
}

// singular integer sequence literal
func example2() {
  ghost xs := seq[int] { 1 }
}

// integer sequence literal
func example3() {
  ghost xs := seq[int] { 1, 17, 142 }
}

// Boolean sequence literal
func example4() {
  ghost xs := seq[bool] { true, false, true }
}

// nested sequences
func example5() {
  ghost xs := seq[seq[int]] { seq[int] { 1 }, seq[int] { 17, 142 } }
}

// empty sequence literal in the function contract
requires xs == seq[int] { }
func example6(ghost xs seq[int]) {
}

// nonempty sequence literal in the function contract
requires xs == seq[int] { 1, 2, 3 }
func example7(ghost xs seq[int]){
}

// very simple function with sequences
requires xs == seq[int] { 1, 2, 3 }
ensures ys == seq[int] { 1, 2, 3 }
func example8(ghost xs seq[int]) (ghost ys seq[int]) {
  ys = xs
}
