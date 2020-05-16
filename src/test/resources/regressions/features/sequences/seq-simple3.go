package pkg

// appending two input sequences
ghost func example1(xs seq[int], ys seq[int]) {
  zs := xs ++ ys
}

// appending `xs` and `ys` inside function body
requires xs == seq[int] { 1, 2, 3 }
requires ys == seq[int] { 4, 5 }
ensures zs == seq[int] { 1, 2, 3, 4, 5 }
func example2(ghost xs seq[int], ghost ys seq[int]) (ghost zs seq[int]) {
  zs = xs ++ ys
}

// slightly more complicated contract
ensures zs == xs ++ seq[seq[bool]] { ys }
func example3(ghost xs seq[seq[bool]], ghost ys seq[bool]) (ghost zs seq[seq[bool]]) {
  zs = xs ++ seq[seq[bool]] { ys }
}

// associativity of '++' operator
ensures xs ++ (ys ++ zs) == (xs ++ ys) ++ zs
func example4(ghost xs seq[bool], ghost ys seq[bool], ghost zs seq[bool]) {
}

// very simple use of the unary sequence length operator
requires 0 < |xs|;
func example5(ghost xs seq[int]) {
}

// simple use of sequence append and sequence size operators
requires 0 < |xs|;
requires 0 < |ys|;
ensures 0 < |zs|;
func example6(ghost xs seq[int], ghost ys seq[int]) (ghost zs seq[int]) {
  zs = xs ++ ys
}

// ghost state and sequence length
func example7() {
  ghost xs := seq[int] { 1, 7, 32 }
  assert |xs| == 3
}

// simple example of sequence length in combination with sequence literals
func example8() {
  assert |seq[bool] { true, false }| == 2;
  assert |seq[bool] { true }| == |seq[int] { 42 }|;
  assert |seq[seq[int]] { seq[int] { 1 }, seq[int] { 17, 142 } }| == 2;
  assert seq[int] { |seq[int] { 1 }|, |seq[int] { 17, 142 }| } == seq[int] { 1, 2 };
}
