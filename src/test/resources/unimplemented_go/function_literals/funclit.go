package function_literals

func bar() func() int {
	return func() int {
		return 2020
	}
}
