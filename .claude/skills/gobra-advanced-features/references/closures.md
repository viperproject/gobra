# Closures and function literals

## Contents

- [The model](#the-model)
- [Writing a closure literal](#writing-a-closure-literal)
- [Calling a closure: `as` is mandatory](#calling-a-closure-as-is-mandatory)
- [Spec instances: partially applying a spec](#spec-instances-partially-applying-a-spec)
- [Implementation proofs for closures](#implementation-proofs-for-closures)
- [Higher-order functions](#higher-order-functions)
- [Captured variables](#captured-variables)
- [Pure closures](#pure-closures)
- [Recursion and termination](#recursion-and-termination)
- [Method values as closures](#method-values-as-closures)
- [Error index](#error-index)
- [Working examples in the repo](#working-examples-in-the-repo)

## The model

A `func` value carries no specification at runtime, so Gobra cannot know what a call to it
does. The solution has three parts:

1. **Every closure literal carries a spec**, written before `func`, and may carry a *name*
   that identifies that spec.
2. **Every closure call names the spec it is being called with**: `cl(args) as S`. The spec
   `S` determines the pre/postcondition of that call site — not the closure's own spec.
3. **Gobra checks that the value in `cl` actually implements `S`.** For the literal's own
   spec this is free; for any other spec you supply a `proof cl implements S { ... }`, which
   is exactly a behavioral-subtyping proof (S's precondition must imply the closure's, and
   the closure's postcondition must imply S's).

This is deliberately the same shape as interface implementation proofs — a spec is to a
closure what an interface is to a concrete type.

## Writing a closure literal

The specification clauses go between `:=` and `func`, and the literal may be given a name:

```go
x@ := 0
c := preserves acc(&x)
     ensures x == old(x) + n && m == x
     func f(n int) (m int) {
         x += n
         return x
     }

r := c(10) as f      // uses f's own spec
assert x == 10 && r == 10
```

The name `f` is **not a variable** — it names the spec only. Using it as a value gives
`expected valid operand, got closure declaration name f`. It is also package-local: the name
of a function literal is not accessible from another package.

Result parameters must be **named** in any spec you intend to use in a proof, because the
proof body has to assign to them by name.

## Calling a closure: `as` is mandatory

`cl(args)` without `as` is not a well-formed closure call. Each call site picks a spec:

```go
cl(2) as f            // f: the literal's own spec
cl(2) as spec         // spec: a separate (usually body-less) function used purely as a contract
cl(2) as spec{cs}     // a spec instance, see below
```

`as` on a call whose base is an ordinary function is unnecessary — reserve it for values of
function type and for method values.

The spec used in `as` is typically declared as a **body-less function**, which is how you
write a contract with no implementation:

```go
ghost
requires x >= 0
ensures  res >= x
decreases
func spec(x int) (res int)
```

## Spec instances: partially applying a spec

`S{d1, ..., dk}` applies the *leading* parameters of `S`, leaving the rest to be supplied at
the call. This is how a spec becomes parametric in captured state:

```go
ghost
requires x >= 0 && cs != nil && cs.inv()
ensures  cs.inv() && cs.called(x) && cs.res(x, res)
func spec(ghost cs Calls, x int) (res int)

// cs is applied; x remains a call argument:
res = f(2) as spec{cs}
```

With keyed arguments, `S{p: e}` applies the parameter named `p` and the remaining ones stay
open: `pos{p: proof1{&x}}`. Positional and keyed forms cannot be mixed (`mixture of
'field:expression' and 'expression' elements in closure spec instance`).

Every parameter that remains open must have a name in the spec's signature, otherwise the
proof cannot refer to it: `cannot find a name for all arguments or results required by S`.

## Implementation proofs for closures

```go
proof cl implements spec{Acc{&accum}} {
    unfold Acc{&accum}.inv()
    res = cl(x) as accumulate
    fold Acc{&accum}.inv()
}
```

The body is **syntactically restricted**, because it is a proof and not a computation:

- Exactly **one** call to the closure, with the open parameters of the spec passed in order
  and by name (`cl(x)`, not `cl(x+0)` or `cl(0)`), called with a spec the closure does
  implement (usually its own literal name).
- Its results assigned to the spec's **result names** (`res = ...`), or `return ...` for a
  pure spec.
- Around it, only `fold`, `unfold`, and `assert`. `assume`, `inhale`, and `exhale` are
  rejected: *Assume, inhale, and exhale are forbidden in implementation proofs*.
- Argument and result names of the spec must not shadow anything the closure expression
  refers to (`identifier ... is shadowed by an argument or result with the same name`).

A pure spec needs a pure closure and a single `return` whose expression is the call,
optionally wrapped in `unfolding ... in`:

```go
proof c implements pos{p: proof1{&x}} {
    return unfolding proof1{&x}.inv() in (c(a) as f)
}
```

Ghostness must match parameter-by-parameter: if the spec is `func spec(a int, ghost b int)
(c int, ghost d int)`, the closure must have exactly the same `ghost` markers in the same
positions.

If the spec has a `decreases` clause, the spec used for the call inside the proof must have
one too (*spec S has termination measures, so also ... (used inside the proof) must*).

The proof must be in scope at the call site. A proof written in one function does not carry
into another — pass the obligation along in a precondition instead (next section).

## Higher-order functions

To accept a function argument, require in the precondition that it implements the spec:

```go
requires f implements spec{cs}
requires cs != nil && cs.inv()
ensures  cs.inv() && cs.called(2) && cs.called(3)
func hof(ghost cs Calls, f func(int) int, choice bool) (res int) {
    res = (f(2) as spec{cs}) + (f(3) as spec{cs})
}
```

The caller discharges `f implements spec{cs}` with a `proof` before the call. This is the
standard way to make a proof travel across a function boundary, including into a goroutine.

Note the pattern in `closures-calldesc1.gobra`: a ghost interface (`Calls`) with a predicate
and pure "was-called" / "result" predicates lets a higher-order function state *that* calls
happened and what they returned, without fixing an order.

## Captured variables

A closure that mentions a local in its spec or body forces that local to be **addressable**:

```go
x@ := 0                          // note the @
cl := preserves acc(&x)
      func(n int) int { return n + x }
```

Without `@` you get a type error, both for the spec (`preserves acc(&x)` on a
non-addressable `x`) and for the plain body reference. The `@` must be on the declaration:
`x@ := 0`, `var x@ int`, or `var c@ func(int) int`.

Captured permissions behave like any other resource: `preserves acc(&x)` means each call
takes the permission and gives it back, so after `cl() as s` with only `requires acc(&x)`
and no matching `ensures`, the permission is gone and a second call fails with
`precondition_error:permission_error`.

## Pure closures

`pure` goes immediately before `func`, after the spec clauses:

```go
c := requires acc(&x, _)
     ensures  m == x + n
     pure func f(n int) (m int) {
         return x + n
     }

assert c(2) as f == 12
```

Pure closures capture with a wildcard fraction (`acc(&x, _)`) so the value stays readable,
and a pure call may appear in assertions. A pure spec can only be implemented by a pure
closure, and vice versa — mixing them is a type error at the `proof`.

## Recursion and termination

A recursive closure has to be able to refer to itself, which means storing it in an
addressable variable and stating in the spec that the variable still holds *this* closure,
using ghost equality `===`:

```go
var c@ func(int) int
c = requires n >= 0
    preserves acc(&c, 1/2) && c === factorial
    ensures  r == fact(n)
    decreases n
    func factorial(n int) (r int) {
        return n == 0 ? 1 : n * (c(n-1) as factorial)
    }
```

`c === factorial` is what lets the recursive call `c(n-1) as factorial` type-check: without
it Gobra cannot know the value in `c` still implements `factorial`. `decreases n` gives the
termination measure; lexicographic measures work as usual (`decreases m, n`).

## Method values as closures

A method value can be proved to implement a spec, which is how you strengthen an interface
method's contract at a particular call site:

```go
ghost
ensures r >= 2*n && r % 2 == 0
pure func more(n int) (r int)

requires i != nil && i.f implements more
ensures  r == (i.f(a) as more) + 2
func hof(i I1, a int) (r int) { ... }

// at the caller:
proof S1{10}.f implements more {
    return S1{10}.f(n)
}
```

## Error index

| Message / id | Cause | Fix |
|---|---|---|
| `precondition_error:spec_not_implemented` | no `proof cl implements S` in scope for this closure value and this spec instance | add the proof, or require `cl implements S` in the enclosing precondition |
| `precondition_error:permission_error` on a closure call | the captured permission was consumed by an earlier call | use `preserves` in the closure spec, or re-acquire |
| `precondition_error:assertion_error` | the call violates the spec's precondition | strengthen the caller's knowledge or weaken the spec |
| `expected valid operand, got closure declaration name f` | the literal's name used as a value | use the variable holding the closure; `f` names only the spec |
| `cannot find a name for all arguments or results required by S` | spec has unnamed parameters/results still open | name them in the spec's signature |
| `invalid body of an implementation proof` | body is not `[folds/unfolds/asserts] + exactly one call + assignment to result names` | reshape the body; `Only fold, unfold, assert, and one call to the implementation are allowed` |
| `invalid body of a pure implementation proof: expected a single return` | pure proof with statements | reduce to one `return`, use `unfolding ... in` for predicates |
| `Assume, inhale, and exhale are forbidden in implementation proofs` | using `assume` to shortcut | prove it, or move the assumption into the spec |
| `identifier x in cl is shadowed by an argument or result with the same name in S` | spec parameter name collides with a captured variable | rename the spec's parameter |
| `spec S has termination measures, so also ... must` | proof's inner call uses a spec without `decreases` | add `decreases` to the closure's own spec |
| `Closures may not be called from code that may be executed during initialization` | closure call reachable from a `mayInit` function or a global initializer | restructure so the call happens after init; see `package-init.md` |
| `function literal ... captures variables, so it cannot be used to derive a parametrized spec instance` | a capturing literal used as the base of a spec instance | use a top-level body-less function as the spec instead |
| `Cannot reveal a closure call` | `reveal` on a closure call | `reveal` only applies to opaque pure functions/methods |

## Working examples in the repo

`src/test/resources/regressions/features/closures/`:

- `closures-simple1.gobra` — the minimal capturing closure
- `closures-simple3-pure.gobra` — pure closure, spec instance with a keyed argument, HOF
- `closures-calldesc1.gobra` — a ghost interface describing which calls happened
- `closures-recursion1-simple.gobra`, `closures-termination.gobra` — recursion, `===`, measures
- `closures-refine-interface.gobra` — method value implementing a stronger spec
- `closures-fail1-precondition.gobra` … `closures-fail6-proofs.gobra` — each expected error
  annotated with its exact id; the best cross-reference when you have a message and no idea
  what triggered it
- `src/test/resources/regressions/features/go_routines/go-routines-closures1.gobra` — closure
  in a `go` statement
