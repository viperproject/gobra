package pkg

type Cell struct {
	val int
}

//:: ExpectedOutput(type_error)
requires forall c *Cell :: { cc.val } acc(c.val)
func foo(cc *Cell) { }
