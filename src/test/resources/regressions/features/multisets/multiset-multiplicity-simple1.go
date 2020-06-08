package pkg

func example1(ghost x int, y int, ghost m mset[int]) {
  ghost var n1 int
  ghost var n2 int
  
  n1 = x in m
  n2 = y in m
  
  assert 0 <= n1
  assert 0 <= n2
}

func example2(ghost x int, ghost y int, ghost m mset[int]) {
  ghost var b bool
  b = x in m == y in m
}

func example3(ghost x int, ghost m1 mset[int], ghost m2 mset[int]) {
  ghost var n int
  n = x in m1 in m2
  assert x in m1 in m2 == (x in m1) in m2
}

func example4() {
  assert 2 in mset[int] { 1, 2, 3 } == 1
  assert 2 in mset[int] { 1, 3 } == 0
  assert 2 in mset[int] { 1, 2, 2, 2, 3 } == 3
  assert 1 in mset[int] { 1 } in mset[int] { 1 } in mset[int] { 1 } == 1
}

func example5(ghost x int) {
  assert x in mset[int] { } in mset[int] { } in mset[int] { } in mset[int] { } == 0
}

func example6(ghost x int, ghost m1 mset[int], ghost m2 mset[int]) {
  assert x in m1 <= x in m1 union m2
  assert 0 < x in m1 ==> 0 < x in m1 union m2
  
  assert x in m1 intersection m2 <= x in m1
  assert 0 < x in m1 intersection m2 ==> 0 < x in m1
}

ensures x in m1 union m2 == (x in m1) + (x in m2)
func example7(ghost x int, ghost m1 mset[int], ghost m2 mset[int]) {
}
