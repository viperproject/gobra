package pkg

func example1() {
  ghost s := set[int] { }
}

ensures s == set[bool] { }
func example2() (ghost s set[bool]) {
  s = set[bool] { }
}

func example3() {
  ghost s := set[int] { 42 }
}

func example4() {
  ghost s := set[set[set[int]]] { set[set[int]] { set[int] { 1 }, set[int] { 2, 3 }, set[int] { } } }
}

requires s == set[int] { }
func example5(ghost s set[int]) {
}

ensures s == set[bool] { true, false, false }
func example6() (ghost s set[bool]) {
  s = set[bool] { true, false, false }
}

requires xs == set[int] { 1, 2, 3 }
ensures ys == set[int] { 1, 2, 3 }
func example7(ghost xs set[int]) (ghost ys set[int]) {
  ys = xs
}
