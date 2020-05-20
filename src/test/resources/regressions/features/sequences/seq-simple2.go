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

// simple use of sequence updates
requires 2 < |xs|;
func example5(ghost xs seq[bool]) {
  ghost ys := xs[2 = false]
}

// simple use of sequence updates with singleton sequences
requires |xs| == 1
ensures ys == seq[bool] { true }
func example6(ghost xs seq[bool]) (ghost ys seq[bool]) {
  ys = xs[0 = true]
}

func example7() {
  assert seq[int] { 2, 1 }[1 = 42] == seq[int] { 2, 42 }
  assert seq[int] { 2, 3, 4 }[0 = 10][1 = 11][2 = 12] == seq[int] { 10, 11, 12 }
  assert (seq[int] {  } ++ seq[int] { 2 })[0 = 42] == seq[int] { 42 }
}

// simple lemma function with sequence updates
requires 0 <= x1 && x1 < x2 && x2 < |xs|;
ensures xs[x1 = v1][x2 = v2] == xs[x2 = v2][x1 = v1]
func example8(ghost xs seq[bool], x1 int, x2 int, v1 bool, v2 bool) {
}

func example9() {
  assert seq[1 .. 4] == seq[int] { 1, 2, 3 }
}

func example10() {
  assert seq[4 .. 1] == seq[int] { }
}

func example11() {
  assert seq[-4 .. -1] == seq[int] { -4, -3, -2 }
}

func example12() {
  assert |seq[1..4]| == 3
  assert seq[1..4] ++ seq[4..8] == seq[1..8]
}

requires x <= y
func example13(x int, y int) {
  assert |seq[x..y + 1]| == y - x + 1
}

