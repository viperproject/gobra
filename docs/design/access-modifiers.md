Right now, Gobra does not enforce Go's access modifiers. This leads to larger generated Viper programs (because we need to encode all symbols from imported packages, even functions and methods that are never called in importing packages because they are private) and potentially, to slow-downs (we incur in additional work at every step of the way - desugaring, encoding, verification). Furthermore, it leads to non-idiomatic-looking specs.

This design document proposes a solution to overcome that.

### Type-checking
- First, Gobra should enforce Go's information visibility rules: names starting with upper case are public, and otherwise, they are private.
	- The only exceptions to this rule are (1) the symbols declared in the `builtin` stub, which contains Gobra's declaration of built-in types and symbols like `error` and `panic`, and (2) the built-in types (e.g., `map`) and operations (e.g., `copy`, `append`)
- The support for `private` interface methods or predicates may get a bit tricky and it is out of scope for the current design. Declaring such members should lead to a type error for now. We may consider lifting this limitation in the future.
- The rules for specifications are inspired by the rules for visibility. Specifications and proof annotations (e.g., loop invariants) may only use symbols that are known to the package where they occur. Because a contract for a public member is part of its public interface, it should be interpretable by a consumer of a package. Thus, a contract of a public member must itself only mention public members. The following is rejected.
```go
decreases
pure func fSpec(x int) int

// type error: Contract of public member `F` references private member `fSpec`
ensures res == fSpec(x)
func F(x int) (res int)
```

- The situation is more interesting with predicates and pure functions. Given that their bodies may be observed by the client, there are three levels of visibility for these members:
	- private - the entire member is not known to clients
	- public signature, private implementation (a.k.a., partially public or `closed`) - the client can observe the contract and signature of the member, but it cannot observe the body. For predicates, this means that the client cannot unfold the predicate (attempting to do so leads to a type error). For a pure function, it means that the client never sees the body. For pure functions marked with `opaque`, the client may not reveal them. (Functions may still be marked as opaque, as the packages where the functions are defined are still aware of the body -- `closed` members only affect what importing clients can see).
	- a predicate (pure function) may be fully public. In this case, all the members used in the body must themselves be closed or fully public.
- We introduce a new modifier for predicates and pure functions (`closed`) to mark them as such. It can only be applied to members with public names.
```go
// type error: private member cannot be marked as `closed`
closed
pred mem() {
	...
}

// by default, members with public names are fully public.
pred Mem(x *int, y int) {
	// type error: (fully) public predicate uses private member `fSpec`. Consider
	//   marking this predicate as closed
	acc(x) && *x == fSpec(y)
}

// this is completely fine, note the new `closed` annotations, which may be used for pure functions too.
closed pred Mem2() {
	mem()
}

```

- In general, we do not know the fields of imported types (other than the public ones). A consequence of this is that we can never know whether a type is comparable (as it may have incomparable fields, e.g., a slice). Imported types are not comparable.
- A package's invariants (a.k.a., initialization invariants) cannot mention private members for the same reason -- it is part of the package's public interface.
- Note that private types are allowed to occur in the signature of a public function or public predicate, but the importing package cannot name them. Gobra should not know which interface types private imported types implement, other than `interface{}`.

### Desugar
- Imported private functions, methods, and predicates may be fully skipped.
- Bodies of predicates of imported closed members may be fully skipped.
- Private types that do not occur in the signatures of public members can be fully skipped. Those that do occur in signatures of public member become uninterpreted types (domain types for which nothing is known).

### Termination checking transform
Gobra relies on Viper's termination checking to prove termination of programs. In Go, one could create an infinite loop by writing a program like the following:
```go
package pkg1

type T interface {
	// @ pure
	// @ requires t != nil
	M(t T) int
}

type TImpl1 struct {}

// @ pure
// @ requires t != nil
func (t TImpl1) M(t T) {
	return t.M(t)
}

//---------------

package pkg2

import "pkg1"

func main() {
	t1 := pkg1.TImpl1()
	t1.M(t1)
}
```
Clearly, `main` does not terminate. In order to detect these kinds of non-termination, Gobra relies on Viper's checks for detecting non-terminating mutual recursion, which Viper *only* applies when it detects loops in the call graph. Gobra originally translates `T.M()` as an abstract method in Viper. Then, both the implementations of `M()` are implemented as methods that ultimately call the interface's `M`. This leads to the following edges in the call graph:
- TImpl1.M() depends on T.M()
However, because T.M() does not depend on TImpl1.M() yet, the call-graph does not have a cycle and Viper does not check that the termination measure decreases through the recursive calls. To fix that, there is an internal transformation that adds such an edge in the call graph. This is implemented and documented in CGEdgesTerminationTransform.scala. With this check, Viper checks for termination and no measure can satisfy the generated constraints.

So far so good, but once we introduce `closed` functions, we can mark `TImpl1.M()` as closed and the client (`pkg2`) would not be aware of the call. Thus, even with CGEdgesTerminationTransform.scala, our call-graphs do not have all the necessary edges. There are two solutions to this:
- The easiest way is to prevent closed pure methods from implementing interface methods (e.g., through a type-check). This is by far the easiest solution and potentially preferable for now, but it may be puzzling for users.
- The other one is to extend CGEdges to introduce all relevant edges. This complicates this transformation even further, which is itself already complicated.
For now, I think the first option is preferable until some user requests the later.

### Encoding
- Uninterpreted private types are translated into an empty domain type.
- TODO: check that this is compatible with the go language spec:
	- imported types may have multiple fields that are private. As such, just because two values `v1` and `v2` of an imported type `T` have equal public fields, it does not mean that `v1 == v2` (or that equality is defined in the first place -- they may have incomparable types). As such, when encoding (public) imported struct types, we should make sure that equality in public fields does not imply equality. To that end, we can introduce a new field of a domain type `PrivateFields` which contains a single function: `default` that returns the zero value. This acts as a stub for all private fields the function may have and is not comparable (using `==`). Notice that we may still be able to prove `===` equalities: In particular, `T{} === T{}`, given that all public params get the 0 value, and the private ones too. Thus, all fields are `===` equal, even if they are not `==` equal. 

