package main

// ##(-I src/test/resources/regressions/features/import/constant_import)
import . "bar3"

const NewAnswer = Answer
const HasNewAnswer = HasAnswer

func foo() {
  assert(Answer == 42)
  assert(NewAnswer == 42)
  assert(HasAnswer)
  assert(HasNewAnswer)
  //:: ExpectedOutput(assert_error:assertion_error)
  assert(false)
}
