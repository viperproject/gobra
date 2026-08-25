# Modular termination for interfaces via call permissions

Status: proposal, and the preferred alternative to `modular-termination-for-interfaces.md`. That plan
closes the call graph with imported members' edges, so it is sound only if every package of the
program is verified. This one discharges every obligation from contracts alone.

## Idea

Jacobs, Bosnacki and Kuiper (*Modular Termination Verification*, ECOOP'15 / TOPLAS'18, implemented in
VeriFast) make termination a resource. A **call permission** carries a *degree* from a well-founded
order; a call consumes one; a ghost step burns a permission of degree δ to mint any number of
permissions at degrees `< δ` (the paper uses bags of ordinals under the multiset ordering). No
infinite supply exists, so no infinite call chain does either. Because the permission lives in the
precondition, dynamic dispatch needs no whole-program analysis.

Gobra needs only the sequential fragment, where the permission collapses to a **degree bound** carried
by contracts. Bounds only ever decrease, so they need not be linear.

## Rules

1. An interface method declares a **degree** `δ_I`, a term over its receiver and arguments. Its
   contract means: the caller must hold a bound `> δ_I`, and the method body runs with bound `δ_I`.
2. A dynamically bound call `i.m(a)` requires `δ_I(i,a) < bound`.
3. An implementation `T.m` is verified with `bound = δ_I(t,a)`. **Nothing relates `μ_T` to `μ_I`** —
   the behavioural-subtyping obligation on measures disappears.
4. Any other member that may dispatch dynamically states its need explicitly: `requires CallPerm(d)`,
   meaning "runs with bound `d`". Members that never dispatch state nothing and are unaffected.
5. Static calls are ordinary precondition checking. To call something requiring `CallPerm(d)` you
   must show `d ≤ bound`. Requirements propagate through contracts by themselves.

Degrees are compared with the existing `WellFoundedOrder` domain, so any comparable Gobra term works.
`decreases` keeps its present meaning for static recursion inside a package.

## Why it is modular

Each package checks its own share against contracts only. No package needs to know an implementation
exists elsewhere.

**Mutually recursive interfaces.** Give the interfaces degrees:

```go
type A interface { decreases degree 2; F(b B) }
type B interface { decreases degree 1; G(a A) }
```

`p1`: `X.F` runs with bound 2 and calls `b.G(x)`, needing `1 < 2`. Verifies.
`p2`: `Y.G` runs with bound 1 and calls `a.F(y)`, needing `2 < 1`. **Rejected in `p2` alone**, with no
knowledge that `p1` exists. Whichever degrees the author picks, one of the two packages fails.

**The static-call leak** (`Loop` at `decreases n+1` calling `i.M(n)`, implementation at `decreases n`
calling `Loop(t, n)` — accepted today, and still accepted under the SCC-closure plan without its
graph-closure step):

```go
// util                                        // main
requires CallPerm(d) && n < d                  // implicit from I.M: bound = n
decreases                                      decreases
func Loop(i I, n int) { i.M(n) }               func (t T) M(n int) { util.Loop(t, n, d) }
```

`T.M` runs with bound `n`, so it may only pass `d ≤ n`; `Loop` requires `n < d`. Unsatisfiable,
**rejected in `main`**. The permission requirement survives body erasure because it is a
precondition — which is exactly what the erased body could not tell us.

## What it deletes

`CGEdgesTerminationTransform` in full, the `ItfMethodMeasure`/`NonItfMethodMeasure` constants and
their `decreasing` axiom, and every reliance on SCC analysis for interfaces. Spec-only parsing stays
untouched: no dispatch summaries, no declared edges for imported members, no whole-program assumption.

## Cost

- New syntax for degrees on interface methods, and `CallPerm` in preconditions.
- A bare `decreases` gives the empty tuple, which is minimal, so any dynamic call from such a member
  fails. That is the correct answer for an open interface, and the same breaking change the other plan
  carries — but here it comes with an actionable message ("no degree declared for `I.m`").
- Degrees are a design decision library authors must make and publish, like any other contract.

## Implementation

Encode the bound as an implicit ghost parameter. For a member with a `CallPerm(d)` requirement, add
`ghost $bound int` (or the degree's type), translate `requires CallPerm(d)` to `d <= $bound`, and pass
`δ_I(i,a)` as the callee's `$bound` at a dynamic call together with `assert δ_I(i,a) < $bound`. That
is the desugaring; no new Viper feature is needed.

Order of work:

1. Degree syntax on interface method specs, plus `CallPerm` as a spec-level assertion.
2. Desugaring of `$bound` threading and the two assertions above.
3. Delete `CGEdgesTerminationTransform` and the `TerminationDomain` interface constants.
4. Regression suite; the counterexamples in `modular-termination-for-interfaces.md` §1 and §3 as
   expected-error tests, and the bounded-recursion example as a positive test.

A prototype of the desugared form already verifies with today's Gobra: threading an explicit rank
parameter with `requires 0 < n` preconditions rejects the divergent program (`Precondition of call
util.Loop(t, n) might not hold`) and accepts the terminating one, both modularly. Steps 1–2 are
surface syntax over that.

## Open questions

- **Concurrency.** `go` statements need the full linear call-permission version with bags of ordinals;
  the pure bound is a single-threaded simplification.
- **Defaults.** Can a useful default degree be inferred for interfaces whose methods never dispatch,
  so that existing code keeps verifying unchanged?
- **Interaction with `decreases`.** Whether degree and measure should stay separate clauses or be one
  lexicographic tuple with the degree as its head.
