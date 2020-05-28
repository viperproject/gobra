package pkg

// very simple use of the unary sequence length operator
requires 0 < |xs|;
func example1(ghost xs seq[int]) {
}

// simple use of sequence append and sequence size operators
requires 0 < |xs|;
requires 0 < |ys|;
ensures 0 < |zs|;
func example2(ghost xs seq[int], ghost ys seq[int]) (ghost zs seq[int]) {
  zs = xs ++ ys
}

// ghost state and sequence length
func example3() {
  ghost xs := seq[int] { 1, 7, 32 }
  assert |xs| == 3
}

// simple example of sequence length in combination with sequence literals
func example4() {
  assert |seq[bool] { true, false }| == 2;
  assert |seq[bool] { true }| == |seq[int] { 42 }|;
  assert |seq[seq[int]] { seq[int] { 1 }, seq[int] { 17, 142 } }| == 2;
  assert seq[int] { |seq[int] { 1 }|, |seq[int] { 17, 142 }| } == seq[int] { 1, 2 };
}