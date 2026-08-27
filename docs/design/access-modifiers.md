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

- In general, we do not know the fields of imported types (other than the public ones). A consequence of this is that we can never know whether an imported type is comparable (as it may have private incomparable fields, e.g., a slice). By default, imported types are treated as incomparable: clients may not compare their values with `==`, nor use them as map keys. To recover comparability, we introduce a `comparable` annotation for type declarations. In the package declaring the type, the annotation is *checked* rather than assumed: it is a type error to mark a type as `comparable` if it is not comparable according to the Go spec. Importing packages may then rely on the annotation without inspecting private fields, and know that comparing values of that type does not crash. E.g., `time.Time` would be annotated as `comparable` in its stub.
- For the same reason, conversions whose legality depends on the private structure of an imported type (e.g., struct conversions `T(v)`, whose legality requires identical underlying types) are rejected. Only conversions that do not require knowledge of private fields (e.g., identity conversions or conversions justified by assignability) remain allowed for imported types.
- A package's invariants (a.k.a., initialization invariants) cannot mention private members for the same reason -- it is part of the package's public interface.
- Note that private types are allowed to occur in the signature of a public function or public predicate. The importing package cannot *name* such a type, but it can hold values of it and use them: it may access the public members of the type (fields, methods, and predicates), and it knows which interfaces the type implements. This is what makes the constructor pattern work: given `func New() *client`, where `client` is private, a client of the package can call the public methods of the returned value and assign it to any interface that `*client` implements.

### Desugar
- All members of the imported packages are desugared, including the ones that the package under verification cannot refer to. See the section on future work below.


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
func (i TImpl1) M(t T) int {
	return t.M(t)
}

//---------------

package pkg2

import "pkg1"

func main() {
	t1 := pkg1.TImpl1{}
	t1.M(t1)
}
```
Clearly, `main` does not terminate. In order to detect these kinds of non-termination, Gobra relies on Viper's checks for detecting non-terminating mutual recursion, which Viper *only* applies when it detects loops in the call graph. Gobra originally translates `T.M()` as an abstract method in Viper. Then, both the implementations of `M()` are implemented as methods that ultimately call the interface's `M`. This leads to the following edges in the call graph:
- TImpl1.M() depends on T.M()
However, because T.M() does not depend on TImpl1.M() yet, the call-graph does not have a cycle and Viper does not check that the termination measure decreases through the recursive calls. To fix that, there is an internal transformation that adds such an edge in the call graph. This is implemented and documented in CGEdgesTerminationTransform.scala. With this check, Viper checks for termination and no measure can satisfy the generated constraints.

So far so good, but once we introduce `closed` functions, we can mark `TImpl1.M()` as closed and the client (`pkg2`) would not be aware of the call. Thus, even with CGEdgesTerminationTransform.scala, our call-graphs do not have all the necessary edges. It could also be the case that, even if an implementation is not closed, it calls a pure function that is and that would still cause non-termination. As such, imported pure functions and methods (even private ones) may need to be included.

### Encoding
- Implementations of imported `closed` pure functions and methods should not be observable to clients. As such, they are translated to `opaque` functions.

### Future work: using the access modifiers to reduce the context given to Viper
The access modifiers describe what a client of a package may observe. In the future, we can use them to infer what does not need to be translated at all from the imported packages. Ideally, we would not translate:
- the signatures and the bodies of the private members of imported packages,
- the private fields of imported structs,
- the bodies of imported public predicates and pure functions.

This would reduce the context that is present in the programs we give to Viper when we verify a package, which should reduce verification times.

Doing so is much more subtle than it seems. In particular, we cannot freely drop functions from imported packages, as this may affect the results of termination checking, which is not modular at all: as described in the section above, Viper only checks that the termination measures decrease when it detects a cycle in the call graph, so dropping the body of an imported function may hide such a cycle from the client and make us miss non-termination.
Dropping the private fields of imported structs is delicate as well: two values of an imported struct type that agree on all of their public fields are not necessarily equal (they may not even be comparable, as they may have private fields of incomparable types). The encoding would thus have to keep a stub for the private fields, e.g., a field of a domain type `PrivateFields` with a single function `default` that returns the zero value, which is not comparable using `==`. Notice that `===` equalities would still hold as expected: in particular, `T{} === T{}`, given that all public fields get the zero value, and so does the stub.

We leave this for future work, for when we have a more modular solution to termination checking.
