package pkg

func foo(ghost m1 mset[int], ghost m2 mset[int]) {
  // fails: clearly wrong
  //:: ExpectedOutput(assert_error:assertion_error)
  assert |m1| <= |m2| ==> m1 subset m2
}
