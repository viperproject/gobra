# Interfaces and implementation proofs

## Contents

- [The model](#the-model)
- [Declaring an interface](#declaring-an-interface)
- [The three ways to prove an implementation](#the-three-ways-to-prove-an-implementation)
- [Implementation proofs in detail](#implementation-proofs-in-detail)
- [Proofs across packages](#proofs-across-packages)
- [Embedded interfaces](#embedded-interfaces)
- [Termination](#termination)
- [Dynamic types, assertions, comparability](#dynamic-types-assertions-comparability)
- [nil interfaces and nil receivers](#nil-interfaces-and-nil-receivers)
- [Ghost members](#ghost-members)
- [Error index](#error-index)
- [Working examples in the repo](#working-examples-in-the-repo)

## The model

An interface value hides its dynamic type, so at a call `y.next()` Gobra can only use the
*interface's* specification. For that to be sound, every implementation must be a
**behavioral subtype**: for each method, the interface's precondition must imply the
implementation's precondition, and the implementation's postcondition must imply the
interface's. The proof of that is an **implementation proof**.

The second half of the model is heap structure. Different implementations own different
memory, so an interface abstracts over it with a `pred` member. The interface's methods are
specified in terms of `mem()`; each implementation supplies the actual definition.

## Declaring an interface

```go
type stream interface {
    pred mem()

    decreases
    requires acc(mem(), 1/2)
    pure hasNext() bool

    requires mem() && hasNext()
    ensures  mem()
    next() interface{}
}
```

Inside the interface, `mem()` refers to the receiver's predicate implicitly. Method specs
may use pure interface methods (`hasNext()`), which is how you express state-dependent
contracts without exposing fields.

## The three ways to prove an implementation

**1. Inference.** Just assign the value; Gobra generates the proof:

```go
var y stream = x
```

Inference is a simple heuristic: it works when no folding/unfolding is required and when
every interface predicate has a definition it can find. Its failures appear as
`Generated implementation proof (*T implements I) failed`.

**2. The `implements` declaration** — a top-level assertion that inference should succeed,
useful for checking eagerly rather than at the first assignment:

```go
someImplementation implements someInterface
```

**3. A full implementation proof** with a body, needed whenever the proof requires
manipulating predicates:

```go
pred (x *counter) mem() { acc(x) }

(*counter) implements stream {
    pure (x *counter) hasNext() bool {
        return unfolding acc(x.mem(), 1/2) in x.hasNext()
    }

    (x *counter) next() (res interface{}) {
        unfold x.mem()
        res = x.next()
        fold x.mem()
    }
}
```

The empty interface `interface{}` is implemented by every type with no proof at all, so
assigning an `int` to an `interface{}` result is always fine.

## Implementation proofs in detail

Pre- and postconditions are **omitted** from the proof methods — they are inherited from the
interface method being implemented. The body is restricted, as with closures:

- exactly one call to the implementation method, with the right receiver and with the
  parameters and results in the same order;
- results assigned to the declared result names (or `return <expr>` for a `pure` method);
- around it, only `fold`, `unfold`, and `assert`.

The predicate definition is part of the proof. Inside the proof you `unfold x.mem()` to get
the concrete permissions the implementation method wants, and `fold` back afterwards so the
interface's postcondition holds.

A type may have at most one proof per interface (`There is more than one proof for type T
implementing an interface`).

## Proofs across packages

When the implementing type lives in another package, the predicate cannot be declared as a
method on it, so declare a plain predicate and bind it in the proof with `pred name := p`:

```go
pred counterMem(x *counterImpl.counter) { acc(x) }

(*counterImpl.counter) implements stream {
    pred mem := counterMem

    pure (x *counterImpl.counter) hasNext() bool {
        return unfolding acc(counterMem(x), 1/2) in x.hasNext()
    }

    (x *counterImpl.counter) next() (res interface{}) {
        unfold counterMem(x)
        res = x.next()
        fold counterMem(x)
    }
}
```

This is exactly what the error `predicate mem is not defined for type *T. Either declare a
predicate 'pred (*T) mem()' ... or declare a predicate 'pred p(*T)' ... and add 'pred mem :=
p' to the implementation proof` is asking for.

Multi-package examples need include directories on the command line: `-I ./` (test files
write this as `// ##(-I ./)`).

## Embedded interfaces

Embedding is flattening: `foo3` below requires every method of `foo2` and `foo1`, and
transitively of `foo` and `bar`. An implementation must satisfy all of them, and the
specifications come from wherever the method was originally declared.

```go
type foo1 interface {
    foo                     // embedded
    ensures 0 <= res
    h() (res int)
}
```

If a method name is reachable through two embedded interfaces with different specs, the
declaration is rejected — resolve it by restructuring the hierarchy so the method has a
single origin.

## Termination

If an interface method has a `decreases` clause, every implementation must have one too
(*This method tries to implement a terminating interface method, but it does not provide a
termination measure*), and its measure must not exceed the interface's
(`term_measure_impl_higher_than_interface`).

For recursive structures, the idiomatic measure is the predicate itself — see how the
built-in `error` interface uses `decreases ErrorMem()` in
`src/main/resources/builtin/builtin.gobra`.

## Dynamic types, assertions, comparability

- `typeOf(x)` yields the dynamic type; type literals are written `type[T]`.
- A type assertion `x.(T)` requires `typeOf(x) == type[T]`, otherwise
  `type_assertion_error:failed_type_assertion`. Establish it in a precondition or by
  branching on a safe assertion.
- The safe form `v, ok := x.(T)` never fails; `ok` tells you whether the dynamic type
  matched, and Gobra knows `ok == (typeOf(x) == type[T])`.
- Comparing two interfaces panics at runtime if either dynamic value is incomparable.
  `isComparable(x)` (over a value or a type) captures this; carry it in your predicates:

  ```go
  pred list(ptr *node) {
      acc(&ptr.value) && isComparable(ptr.value) && acc(&ptr.next) &&
      (ptr.next != nil ==> list(ptr.next))
  }
  ```

  Without it, comparisons fail with `incomparable_error`. Boxing a concrete value into an
  interface generates the comparability fact automatically.

## nil interfaces and nil receivers

A nil interface has no dynamic type, so any method call on it fails with
`method_object_nil_error` ("The receiver of ... might be nil"). Higher-order and interface-
taking functions therefore almost always need `requires i != nil`. This is easy to miss
because Go itself has no such requirement.

## Ghost members

Interfaces may declare `ghost` methods and `pure` methods; the implementation's ghostness
must match exactly:

```go
type someInterface interface {
    ghost pure getValue() int
}
```

Interface methods may **not** be annotated `mayInit` (`Interface declaration contains
methods annotated with 'mayInit'`), which is why calls through interfaces are restricted
during package initialization — see `package-init.md`.

## Error index

| Message / id | Cause | Fix |
|---|---|---|
| `Generated implementation proof (T implements I) failed. Precondition of call to implementation method might not hold` | inferred proof cannot get from the interface's precondition to the implementation's | write an explicit proof with the needed `unfold`s, or weaken the implementation's precondition |
| `Generated implementation proof (T implements I) failed. Postcondition of interface method might not hold` | implementation's postcondition does not re-establish the interface's | `fold` the interface predicate back in an explicit proof |
| `predicate mem is not defined for type T ...` | interface predicate has no definition for this type | declare `pred (x T) mem()`, or a free predicate plus `pred mem := p` in the proof |
| `An implementation proof cannot be inferred because predicate definitions are missing` | same, at an assignment site | as above |
| `There is more than one proof for type T implementing an interface` | duplicate `implements` declarations | keep one |
| `This method tries to implement a terminating interface method, but it does not provide a termination measure` | missing `decreases` on the implementation | add `decreases` |
| `term_measure_impl_higher_than_interface` | measure too large | pick a measure bounded by the interface's |
| `type_assertion_error:failed_type_assertion` | `x.(T)` without knowing the dynamic type | add `requires typeOf(x) == type[T]`, or use `v, ok := x.(T)` |
| `incomparable_error` | comparing interfaces without `isComparable` | add `isComparable(...)` to the invariant/predicate |
| `method_object_nil_error` | method call on a possibly-nil interface | `requires i != nil` |
| `type error: type T does not implement the interface I` | a method is missing or has a mismatched signature/ghostness | align signatures, including `ghost`/`pure` markers and pointer vs value receivers |
| `Call to interface method whose receiver is of an interface type defined in this package is disallowed within code that may run during the initialization of this package` | dynamic dispatch during init | see `package-init.md` |

## Working examples in the repo

`src/test/resources/regressions/features/interfaces/`:

- `counterStream.gobra` + `counterImpl/` — cross-package proof with `pred mem := counterMem`
- `embeddedInterfaces1.gobra` … `embeddedInterfaces7.gobra` and the `-fail` variants
- `safeTypeAssertion1.gobra`, `typeOf1.gobra` — assertions and dynamic types
- `comparable1.gobra`, `predicateWithInterfaceParam.gobra`, `ghostMembers.gobra`
- `docs/tutorial.md` §Interfaces — the `stream`/`counter` example end to end
