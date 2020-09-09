package pkg

func foo() {
  //:: ExpectedOutput(type_error)
  ghost m := mset[mset[int]] { mset[int] { 1, 2 }, mset[bool] { } }
}
