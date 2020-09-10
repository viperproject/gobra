package pkg

func test1() {
  ghost s := seq[int] { 1:12, 0:24 }
  assert s == seq[int] { 24, 12 }
}

func test2() {
  ghost s := seq[seq[int]] { 0 : { } }
  assert len(s) == 1
  assert s[0] == seq[int] { }
}

func test3() {
  assert seq[seq[int]] { 0:{8}, 1:{8} } == seq[seq[int]] { 1:{8}, 0:{8} }
}

func test4() {
  assert (seq[int] { 1:10, 0:20 })[1] == 10
}
