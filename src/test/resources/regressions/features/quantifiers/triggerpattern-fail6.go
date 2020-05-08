package pkg

type Point struct {
	x int
  y int
}

// invalid trigger: trigger pattern doesn't include `p.y`
//:: ExpectedOutput(logic_error)
requires forall p Point :: { p.x } 0 < p.x
func foo () { }
