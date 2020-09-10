package pkg

requires 4 < i
func foo(i int) {
	//:: ExpectedOutput(assignment_error:seq_index_exceeds_length_error)
	n := ([4]int{1, 2, 3, 4})[i]
}
