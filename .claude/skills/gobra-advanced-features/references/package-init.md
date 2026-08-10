# Package initialization and global variables

## Contents

- [The model](#the-model)
- [Declaring globals](#declaring-globals)
- [Package invariants](#package-invariants)
- [Duplicable invariants and `openDupPkgInv`](#duplicable-invariants-and-openduppkginv)
- [`mayInit`: what may run during initialization](#mayinit-what-may-run-during-initialization)
- [What initialization code may not do](#what-initialization-code-may-not-do)
- [Crossing package boundaries: `importRequires` and `friendPkg`](#crossing-package-boundaries-importrequires-and-friendpkg)
- [`main`](#main)
- [Annotating `.go` files](#annotating-go-files)
- [Error index](#error-index)
- [Working examples in the repo](#working-examples-in-the-repo)

## The model

Go runs package-level variable initializers and `init()` functions before `main`, in
dependency order. Gobra treats all of that as one **initialization phase** whose job is to
establish a stated contract:

- the package's own **`pkgInvariant`**, which is what the rest of the package may rely on;
- whatever importing packages claim via **`importRequires`**;
- the precondition of **`main`**, if this is the main package.

Everything else follows from one soundness concern: code running during initialization must
not be able to *assume* an invariant that has not been established yet. That is why so many
ordinary constructs — closure calls, dynamic dispatch, opening invariants — are simply
forbidden in initialization code, and why functions reachable from it must be marked
`mayInit`.

## Declaring globals

A global is addressable only if declared with `@`, and only addressable globals can be
written or appear under `acc`:

```go
var A@ int = 0
var B@, C@ = f()
var _ = g()          // result discarded; no permission needed
ghost var allocs@ set[*Client]
```

Without `@` the variable is effectively immutable: `x.f = 2` on a non-addressable global
`x *T` is a type error, even where it would be harmless.

Constraints Gobra imposes on the declarations themselves:

- **Declaration order matters.** Every dependency of a global must be declared before it;
  cycles (including through function calls) are rejected. This is currently stricter than Go.
- No redeclaration of the same name.
- Calls in an initializer must be statically bound — `var B = newT().f()` where `newT`
  returns an interface is rejected, because the dependency analysis cannot see through it.
- Every function called from an initializer must be marked `mayInit` and must **terminate**
  (a missing `decreases` shows up as `function_termination_error` on the *variable
  declaration*, which is confusing the first time you see it).

## Package invariants

`pkgInvariant` is written **before the package clause** — it is part of the file preamble,
alongside `friendPkg` and `importRequires`:

```go
pkgInvariant acc(&A) && acc(&B) && acc(&C, 1/2)
package pkg
```

More often the invariant is a single predicate, so that functions can pass it around
abstractly:

```go
// @ pkgInvariant StaticInv()
package fib

var cache /*@@@*/ map[int]int = make(map[int]int)

func init() {
    cache[0] = 1
    cache[1] = 1
    // @ fold StaticInv()
}
```

The initialization code must **establish** the invariant — hence the `fold` at the end of
`init()`. After that, functions that need the globals mention it in their contracts like any
other resource:

```go
// @ requires  0 <= n
// @ preserves StaticInv()
// @ ensures   res == FibSpec(n)
// @ decreases n
func Fib(n int) (res int) {
    // @ unfold StaticInv()
    // @ defer fold StaticInv()
    ...
}
```

A non-duplicable invariant is a linear resource: something has to hand it to the first
caller (a `main` precondition, an `importRequires`, or a `friendPkg` grant). If a function
"cannot get" the invariant, that chain is what is missing.

`old(...)` may not appear in package invariants, import preconditions, or friend clauses —
there is no earlier state to refer to.

## Duplicable invariants and `openDupPkgInv`

Marking the invariant `dup` makes it duplicable, which is the right choice when the
invariant only grants wildcard permissions (`acc(P(), _)`) that every caller should be able
to obtain independently:

```go
// @ dup pkgInvariant acc(StaticInv(), _)
package byte
```

Any function may then obtain it with the statement `openDupPkgInv`:

```go
// @ ensures acc(res.Mem(), _)
// @ decreases
func ToVal(val byte) (res *Byte) {
    // @ openDupPkgInv
    // @ unfold acc(StaticInv(), _)
    res = byteCache[val+128]
    ...
}
```

`openDupPkgInv` takes no argument — it always refers to the current package's duplicable
invariant. The idiomatic way to expose it to other packages is a small ghost wrapper, as in
`concfib/fib_spec.gobra`:

```go
ghost
ensures acc(StaticInv(), _)
decreases
func AcquireDupPkgInv() { openDupPkgInv }
```

Two restrictions: `openDupPkgInv` is forbidden in `mayInit` functions (the invariant may not
hold yet) and in pure functions and methods (*Pure functions and methods cannot open package
invariants, and thus, they must not be annotated with 'mayInit'*).

## `mayInit`: what may run during initialization

`mayInit` is an annotation on a function or method meaning "this may execute during package
initialization". It is a colouring that propagates through calls:

```go
mayInit
decreases
func f() (int, bool)
```

- Anything called (transitively) from a variable initializer or `init()` must be `mayInit`.
- Inside a `mayInit` function you may only call other `mayInit` functions — otherwise
  *Function called from 'mayInit' context is not 'mayInit'*.
- **Pure functions are the exception**: they may always be called from a `mayInit` context,
  because they cannot assume the package invariants. Correspondingly, a pure function must
  *not* itself be annotated `mayInit`.
- Interface methods may not be `mayInit` (*Interface declaration contains methods annotated
  with 'mayInit'*).

When you hit the "is not mayInit" error, the fix is usually to add `mayInit` to the callee
and follow the chain — but check as you go that each callee is genuinely safe to run before
the invariant holds, since `mayInit` functions cannot rely on it.

## What initialization code may not do

These are all type errors, not verification failures, and all trace back to the same
soundness argument:

| Forbidden in init / `mayInit` code | Message |
|---|---|
| Calling a closure | `Closures may not be called from code that may be executed during initialization` |
| Dynamically-bound non-ghost method calls | `Calls to dynamically-bound non-ghost methods are not allowed in initialization code` |
| Calling an interface method whose interface is defined in this package | `Call to interface method whose receiver is of an interface type defined in this package is disallowed within code that may run during the initialization of this package` |
| Assigning a value of a local type to a variable of an **imported** interface type | `Assigning values of types defined in the current package to locations of an interface type that is defined in imported packages is disallowed in code that may run during package initialization` |
| Converting to an imported interface type | `Type T may not be converted to type I in code that may run during the initialization ...` |
| `openDupPkgInv` | *Opening the package invariant in a function that may execute during initialization is not allowed* |
| Non-terminating code | `function_termination_error` |

One idiom is explicitly carved out because it is so common — the blank-identifier subtype
check compiles even though the general assignment does not:

```go
var _ defs.I = (*T)(nil)   // allowed
var A defs.I = (*T)(nil)   // rejected
```

`init` functions themselves have extra rules: they may not carry a specification, may not be
`ghost`, and may not be called (`init()` and `go init()` are both errors). A *method* named
`init` is an ordinary method and is unaffected.

## Crossing package boundaries: `importRequires` and `friendPkg`

An importing package states what it needs the imported package's initialization to have
established, in the preamble, immediately before the import:

```go
importRequires acc(&bar.A) && acc(&bar.B)
import "bar"
```

Gobra then checks the imported package's initialization code actually establishes it; the
failure is `import_pre_error` — *"The import precondition might not be established by the
initialization code of the imported package"*.

The exporting side can grant resources to a *specific* importer with `friendPkg`, written
after the package clause and before the imports:

```go
package bar

// ##(-I ../ --experimentalFriendClauses)

friendPkg "../../pkg" acc(&A) && acc(&B) && acc(&C)

var A@ int = 1
```

`friendPkg` is experimental and requires the `--experimentalFriendClauses` flag; without it
you get *Usage of experimental 'friendPkg' clauses is disallowed by default*. The path is
resolved the same way as an import path. As with package invariants, `old(...)` is not
allowed in the assertion.

## `main`

`main`'s precondition is discharged by the initialization code of the main package. If it
asks for more than initialization establishes you get `main_pre_error` — *"The precondition
of the function main might not be established by the initialization code"*. Either weaken
`main`'s precondition or strengthen what `init()` establishes.

## Annotating `.go` files

Real Go files carry annotations in comments, which Gobra's preprocessor (`Gobrafier`) strips
back out:

- `// @ <annotation>` for single-line and `/*@ ... @*/` for inline annotations.
- Preamble clauses go in comments **above the package clause**: `// @ pkgInvariant StaticInv()`.
- The addressability modifier inline is `/*@@@*/` — that is `/*@`, a literal `@`, then `@*/`:
  `var cache /*@@@*/ map[int]int`. The alternative, for locals, is a trailing
  `//@ addressable: x, y` comment on the declaration line.

## Error index

| Message / id | Cause | Fix |
|---|---|---|
| `Function called from 'mayInit' context is not 'mayInit'` | callee not annotated | add `mayInit` to the callee (not to pure functions), and follow the call chain |
| `function_termination_error` on a `var` declaration | an initializer calls something without a termination measure | add `decreases` to the callee |
| `assignment_error:permission_error` reading a global | the package invariant granting `acc(&A)` was never obtained | `preserves StaticInv()` + `unfold`, or `openDupPkgInv` for a `dup` invariant |
| `main_pre_error` | `main`'s precondition is stronger than what init establishes | weaken it, or `fold` the missing resource in `init()` |
| `import_pre_error` | `importRequires` asks for more than the imported package establishes | weaken the clause, or add a `friendPkg` grant / strengthen the exporter's init |
| `Usage of experimental 'friendPkg' clauses is disallowed by default` | missing flag | pass `--experimentalFriendClauses` |
| `'old' expressions cannot occur in import-preconditions, friend clause assertions, and package invariants` | `old(...)` in a preamble clause | remove it |
| `Opening the package invariant in a function that may execute during initialization is not allowed` | `openDupPkgInv` in a `mayInit` function | restructure so the caller opens it after init |
| `Pure functions and methods cannot open package invariants, and thus, they must not be annotated with 'mayInit'` | `pure` + `mayInit` | drop `mayInit`; pure functions may already be called from init code |
| type error on a cyclic or forward-referencing global | declaration order | reorder so dependencies come first; break cycles |
| type error on `init()` with a spec, on `ghost func init()`, or on calling `init` | init function rules | remove the spec / the `ghost` / the call |

## Working examples in the repo

- `src/test/resources/regressions/features/globals/` — `globals-simple01.gobra` for the basic
  shape; `globals-type-fail01..06.gobra` enumerate the restrictions one by one with the
  reason for each in a comment; `itfAssign/main.gobra` covers the interface restrictions
- `src/test/resources/same_package/pkg_init/fib/` — non-duplicable `pkgInvariant` over a
  cache, annotated `.go` style
- `src/test/resources/same_package/pkg_init/byte/` and `concfib/` — `dup pkgInvariant` and
  `openDupPkgInv`, including the ghost-wrapper idiom
- `src/test/resources/same_package/pkg_init/import1/`, `import2/` — `importRequires` paired
  with `friendPkg`
- `src/test/resources/regressions/features/globals/scion/` — a realistic multi-package example
