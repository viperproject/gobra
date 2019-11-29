
package pkg;

func test() {
    //:: ExpectedOutput(assert_error:assertion_error)
	assert 0 == 1;
};

func evens(n int) (e int) {

  //:: ExpectedOutput(invariant_establishment_error:assertion_error)
  invariant e == n/2;
  for i := 1; i <= n; i++ {
    if(n % 2 == 0) { e++; };
  };
};