package pkg

// An example of sequences with a very simple contract
// (that tests comparibility of sequences).
requires xs == ys
func example1(ghost xs seq[int], ghost ys seq[int]) {
}

// empty sequence literal in the function contract
requires xs == seq[int] { }
func example2(ghost xs seq[int]) {
}

// nonempty sequence literal in the function contract
requires xs == seq[int] { 1, 2, 3 }
func example3(ghost xs seq[int]){
}

// very simple (lemma) function with sequences
requires xs == seq[int] { 1, 2, 3 }
ensures ys == seq[int] { 1, 2, 3 }
func example4(ghost xs seq[int]) (ghost ys seq[int]) {
  ys = xs
}
