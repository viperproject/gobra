---
name: gobra-advanced-features
description: >
  Diagnose and fix Gobra type-checking and verification errors involving closures and
  function literals, package initialization (globals, pkgInvariant, mayInit,
  importRequires, init), interfaces (implementation proofs, embedded interfaces, type
  assertions), and concurrency (goroutines, sync.Mutex, sync.WaitGroup, channels, atomics
  and invariants). Use it whenever a .gobra or annotated .go file fails with messages like
  "spec_not_implemented", "invalid body of an implementation proof", "Generated
  implementation proof ... failed", "Closures may not be called from code that may be
  executed during initialization", "is not 'mayInit'",
  "go_call_precondition_error", "main_pre_error", "import_pre_error", or with
  permission errors around go statements, locks, channels, closure calls or globals. Also
  use it when writing new annotations for these features, and even when the user only says
  "this doesn't verify" — if a closure, interface, goroutine, lock, channel or global is
  near the failing line, this applies.
---

# Gobra: closures, package init, interfaces, concurrency

These four features are where Gobra stops being "Go plus contracts" and starts asking for
proofs the user has to write by hand. The error messages are usually accurate but terse,
and they point at a symptom (a missing permission) rather than the missing proof
obligation. This skill maps symptoms onto the underlying discipline so you can write the
right annotation instead of guessing.

## The one idea that unifies all four

Permissions in Gobra are **linear resources**: holding `acc(&x)` is like holding a token
that only one part of the program can hold at a time. Verification is modular — Gobra only
ever looks at the callee's *specification*, never its body.

All four of these features are places where the callee is not statically known, so Gobra
cannot even find a specification to look at. Each feature is therefore a discipline for
naming a contract up front and proving, at the point where the unknown code is created,
that it satisfies that contract:

| Feature | Unknown callee | How you name the contract | Where you discharge it |
|---|---|---|---|
| Closures | a `func` value | a *spec instance* — a normal function whose signature is used as the spec, e.g. `spec{x}` | `proof cl implements spec{x} { ... }` |
| Interfaces | a dynamic type behind an interface | the interface's method specs and `pred`s | `(*T) implements I { ... }` (implementation proof) |
| Concurrency | another goroutine | a predicate: `go` precondition, `LockInv()`, channel `Init` predicates, `Invariant(P)` | `fold` before you hand it over |
| Package init | code running before `main` | `pkgInvariant`, `importRequires` | the package's initialization code, checked wholesale |

When you are stuck, ask: *what resource is crossing which boundary, and where is the proof
that the receiver's contract is met?* That question resolves most of these errors.

## Step 1 — classify the error before changing anything

Gobra reports two very different kinds of failure, and the fix strategy differs:

**Type errors** come from the frontend (`frontend/info/`) and are hard structural rules —
"you may not do this at all". They read like plain prose and mention no verification
condition. Example: `Closures may not be called from code that may be executed during
initialization`. Do not try to fix these with more annotations; the code shape has to
change.

**Verification errors** are printed in two parts: a first line naming the operation that
failed ("Precondition of call cl(-1) might not hold.") and a *reason* line naming what
specifically could not be shown. The reason is the more informative half, and it tells you
which kind of fix you need:

- *"Permission to ... might not suffice"* — a resource is missing. Someone else holds it, or
  you never folded / never obtained it. Follow the resource, not the assertion.
- *"Assertion ... might not hold"* — you hold the resources, but a *fact* is unknown.
  Usually a missing `unfold`/`unfolding`, or information that got lost across a lock,
  channel or goroutine boundary and has to be added to the invariant.
- *"... might not implement ..."* — a closure call `cl(...) as spec` has no
  `proof cl implements spec` in scope (or the proof is for a different spec instance).
- *"Type assertion ... might fail"* — missing `typeOf(x) == type[T]`.
- *"The termination measure of this method might exceed ..."* — the implementation's
  `decreases` is larger than the interface method's.

Internally each half has an id, and the pair appears in the regression suite as
`//:: ExpectedOutput(<error_id>:<reason_id>)` — e.g. `precondition_error:permission_error`,
`precondition_error:spec_not_implemented`. The tables in the reference files are keyed by
these ids, and grepping `src/test/resources/regressions/` for one finds a working example of
both the failure and its fix. That is often the fastest route to a correct annotation.

## Step 2 — go to the reference for the feature

Read only the file you need; each is self-contained.

| Read this | When you see |
|---|---|
| `references/closures.md` | function literals with specs, `cl(x) as spec`, `proof ... implements ...`, `spec_not_implemented`, `invalid body of an implementation proof`, `cannot find a name for all arguments`, closures captured variables, higher-order functions |
| `references/interfaces.md` | `T implements I`, implementation proofs, `Generated implementation proof ... failed`, `predicate ... is not defined for type`, embedded interfaces, `typeOf`, type assertions, `isComparable`, interface `pred` members |
| `references/concurrency.md` | `go` statements, `go_call_precondition_error`, `sync.Mutex`/`RWMutex`/`WaitGroup`, `LockInv`, `SetInv`, channels (`Init`, `SendGivenPerm`, `RecvGotPerm`), `PredTrue{}`, `atomic`, `critical`, `Invariant`, `is_invariant_failed`, `invariant_already_open`, `invariant_not_restored` |
| `references/package-init.md` | global variables, `pkgInvariant`, `dup pkgInvariant`, `openDupPkgInv`, `mayInit`, `importRequires`, `friendPkg`, `init()`, `main_pre_error`, `import_pre_error`, "Function called from 'mayInit' context is not 'mayInit'" |

Errors frequently span two of these — a goroutine running a closure, or a global holding a
mutex. Read both references; the failure is usually at the seam.

## Step 3 — debug by shrinking, not by weakening

The productive loop:

1. **Reproduce in isolation.** Copy the failing member into a scratch `.gobra` file with
   stub declarations (a function with a spec and no body verifies vacuously and is the
   ideal stand-in for its callees). Run:
   ```bash
   java -jar -Xss128m target/scala-2.13/gobra.jar -i scratch.gobra
   # or, in the source tree:
   sbt "run -i scratch.gobra"
   ```
   Test files pass extra flags via a magic comment, e.g. `// ##(-I ./ --experimentalFriendClauses)`;
   `-I` adds include directories for multi-package examples.
2. **Bisect the state with `assert`.** Insert `assert acc(&x)` / `assert x > 0` before the
   failing line and walk backwards until an assert passes. The first failing assert is
   where the resource or fact was actually lost — usually much earlier than the reported
   line, especially with locks and goroutines.
3. **Name what is missing**, then apply the recipe from the reference file.
4. **Re-run and iterate.** Fix one error at a time; Gobra's later errors are often
   downstream of the first.

Two escape hatches deserve care: `assume` and `trusted` make the error disappear without
proving anything, and a `pure` function with no body is trusted axiomatically. They are
legitimate for stubbing out-of-scope code, but using one to silence a real proof obligation
turns a verified program into an unverified one. If you use one, say so explicitly in your
report to the user rather than presenting the file as verifying.

## Cross-cutting causes worth checking first

A large share of "advanced feature" errors are actually one of these basics:

- **Addressability.** `acc(&x)` requires `x` to be addressable: declare it `x@ := 0` (or
  `var x@ int`). Non-addressable locals cannot be captured by a closure spec, cannot be
  shared with a goroutine, and cannot appear under `acc`. Globals need `var A@ int` too.
  Struct fields are addressable when the struct itself is.
- **Missing `decreases`.** Every pure or ghost function and method needs a termination
  measure ("All pure or ghost functions and methods must have termination measures"), and
  so does anything reachable from package initialization or marked `atomic`.
- **Predicate constructors vs composite literals.** `P{a, b}` is a first-class predicate
  (`pred(...)` typed), and `_` marks an unapplied argument: `sendInvariant{_}` has type
  `pred(*int)`. `P{...}()` is the *instance*; `fold`/`unfold` act on instances. Gobra reads
  `name{args}` as a predicate constructor when `name` resolves to a top-level predicate or
  when `_` appears; for an imported predicate whose name collides with a local type,
  parenthesise: `(pkg.P){1}()`. `PredTrue{}` is the built-in arity-zero `true` predicate
  and still needs `fold PredTrue{}()` before it can be handed over.
- **Wildcard permissions.** `acc(P(), _)` is an unspecified positive fraction: duplicable,
  never usable for writing, and the right choice for things every thread should keep, like
  `acc(m.LockP(), _)`.
- **Fractions.** Write permission is `acc(x)` = `acc(x, 1/1)`. Splitting is manual: to pass
  `acc(x, 1/2)` to each of two goroutines, the caller must start with the full permission.

## Reporting back

When you fix one of these, tell the user *which contract was missing*, not just which line
you edited — e.g. "the closure needed `proof cl implements spec{&i}` because `go` requires
the spec instance to be known at the spawn point". These features are unfamiliar enough
that the reasoning is the useful part of the answer. If you could not discharge an
obligation and stubbed it instead, say so plainly.
