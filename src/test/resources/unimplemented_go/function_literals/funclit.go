// Any copyright is dedicated to the Public Domain.
// http://creativecommons.org/publicdomain/zero/1.0/

package function_literals

func bar() func() int {
	return func() int {
		return 2020
	}
}
