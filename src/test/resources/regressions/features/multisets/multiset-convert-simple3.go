package pkg

func example1(ghost x int, ghost xs seq[int]) {
  assert x in set(xs) ==> 0 < x in mset(xs)
  assert 0 < x in mset(xs) ==> x in set(xs)
}
