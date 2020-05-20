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

// very simple function with sequences
requires xs == seq[int] { 1, 2, 3 }
ensures ys == seq[int] { 1, 2, 3 }
func example4(ghost xs seq[int]) (ghost ys seq[int]) {
  ys = xs
}

func example5() {
  assert seq[1 .. 4] == seq[int] { 1, 2, 3 }
}

func example6() {
  assert seq[4 .. 1] == seq[int] { }
}

func example7() {
  assert seq[-4 .. -1] == seq[int] { -4, -3, -2 }
}

func example8() {
  assert |seq[1..4]| == 3
  assert seq[1..4] ++ seq[4..8] == seq[1..8]
}

requires x <= y
func example9(x int, y int) {
  assert |seq[x..y + 1]| == y - x + 1
}
