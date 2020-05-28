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

