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
- Note that private types are allowed to occur in the signature of a public function or public predicate, but the importing package cannot name them. Gobra should not know which interface types private imported types implement, other than `interface{}`. For now, clients cannot access any members of private imported types, not even public ones (e.g., in the constructor pattern `func New() *client`, where `*client` has public methods, clients cannot call those methods). We may extend this in the future by exposing the public method set and the public fields of private types that occur in public signatures.

### Desugar
- Imported private non-pure functions and non-pure methods may be fully skipped.


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
- TODO: check that this is compatible with the go language spec:
	- imported types may have multiple fields that are private. As such, just because two values `v1` and `v2` of an imported type `T` have equal public fields, it does not mean that `v1 == v2` (or that equality is defined in the first place -- they may have incomparable types). Note that the type checker already rejects `==` on imported types that are not annotated as `comparable`, so the construction below matters for `comparable` imported types and for `===`. As such, when encoding (public) imported struct types, we should make sure that equality in public fields does not imply equality. To that end, we can introduce a new field of a domain type `PrivateFields` which contains a single function: `default` that returns the zero value. This acts as a stub for all private fields the function may have and is not comparable (using `==`). Notice that we may still be able to prove `===` equalities: In particular, `T{} === T{}`, given that all public params get the 0 value, and the private ones too. Thus, all fields are `===` equal, even if they are not `==` equal.
- Implementations of imported `closed` pure functions and methods should not be observable to clients. As such, they should be translated to `opaque` functions.

### Potential optimizations to implement in the future:
1. `closed` imported functions need not to be always present. We can skip the encoding of parts of closed and private functions. We need to keep the body interface method implementations and any imported functions called from it (be they private or closed or public). For unreachable imported functions, we can fully skip them.
