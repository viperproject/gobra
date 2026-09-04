# Sound modular termination checking for interfaces

Status: **P3 and P4 implemented** (see §8); P1, P2 and P5 still proposals. Target:
`CGEdgesTerminationTransform`, `TerminationEncoding`, `Parser`, and two small additions to Silver's
termination plugin.

## 1. Problem

Termination checking is unsound for dynamically bound calls across packages. Every package below
verifies with 0 errors; the program diverges.

```go
// base
type A interface { /*@ requires b != nil; decreases @*/ F(b B) }
type B interface { /*@ requires a != nil; decreases @*/ G(a A) }

// p1 (imports base)          // p2 (imports base)
// @ requires b != nil        // @ requires a != nil
// @ decreases                // @ decreases
func (x X) F(b base.B) {      func (y Y) G(a base.A) {
    b.G(x)                        a.F(y)
}                             }

// main: var x base.A = p1.X{}; var y base.B = p2.Y{}; x.F(y)   →  stack overflow
```

The same program in **one** package is correctly rejected (it is
`regressions/features/termination/termination-fail-03.gobra`).

## 2. Root cause

The encoding intends two obligations, with the `ItfMethodMeasure`/`NonItfMethodMeasure` tail and the
`decreasing(NonItf, Itf)` axiom supplying the strict/non-strict distinction:

- **O1** at a dynamic call `i.m(a)` in member `C`: `Φ_{I.m}(i,a) ≺ Φ_C`
- **O2** for `T` implementing `I`: `Φ_{T.m}(t,a) ≺ Φ_{I.m}(t,a)` (i.e. `μ_T ⪯ μ_I`)

Both are emitted only for calls inside an SCC (`MethodCheck.scala:66`), and the call graph is built
solely from bodies (`MethodCheck.scala:270-283`). Imported packages are parsed spec-only
(`Parser.scala:154`) and non-`pure` bodies are dropped (`ParseTreeTranslator.scala:888`), so imported
members have no out-edges and can never sit in an SCC. Two gaps follow:

- **G1** — in a package with no visible implementation, the synthesised dispatch body of `I.m` is
  empty, so `I.m` has no out-edges and a call `i.m()` gets **no** obligation at all.
- **G2** — in a client package, the path back through an imported body is missing, so static
  cross-package edges that belong to a real cycle are never checked.

Erasing imported bodies is safe for *static* calls, because Go's import graph is acyclic. Dynamic
dispatch reintroduces cycles that run backwards through it, and no single package sees them.

## 3. Design

### P1 — make O1 unconditional (fixes G1)

Emit the tuple check at every dynamically bound call whose callee declares a measure, regardless of
SCC membership.

This cannot be delegated to the client package instead. A client could only discharge the imported
member's obligation from a stub body, and a stub body has neither the real call arguments nor a
reachable path — the `inhale false` that makes it safe is exactly what makes the check vacuous. The
obligation must be discharged where the call site is.

### P2 — make O2 unconditional

Same mechanism, applied to the calls that `CGEdgesTerminationTransform` emits into the dispatch body.
This keeps the existing `InvalidImplTermMeasureAnnotation` blame (reported at the implementation's
measure) and needs no change to `InterfaceEncoding.methodProof`.

### P3 — declare call-graph edges for body-less imported members (fixes G2)

Record each imported member's *direct* call edges when its package is parsed, and attach them to the
emitted `vpr.Method`. No transitive closure is needed: every imported member gets edges, so the graph
closes itself. No fabricated bodies, no synthetic arguments.

### P4 — keep the measure tuple on body-less imported members

`Parser.scala:474-490` rewrites imported tuples to `decreases _`. Retaining the tuple on a member with
no body costs nothing (`transformMethod` no-ops without a body) and changes nothing outside an SCC
(`getTerminationCondition` is `true` either way), but inside an SCC it turns an automatic failure
(`MethodCheck.scala:100-103`) into a real comparison. Keep the wildcard rewrite for `pure` members,
whose bodies survive and would otherwise be re-verified.

### Soundness

P1+P2 give: every dynamic call strictly decreases `Φ`, by transitivity of the lexicographic order.
P3+P4 give: every cycle in the global call graph is an SCC in the graph of the package that
transitively imports all of its members — the merged lookup table (`Desugar.combine`) carries
implementation relations demanded anywhere in the import closure, so `I.m → T.m` edges are present
there — hence every static edge in that cycle is checked too. An infinite call chain therefore forces
an infinite descent in `Φ`. Assumes every package of the program is verified.

P1 alone is not enough. This survives it and needs P3:

```go
// util                                    // main
// @ requires 0 <= n; decreases n + 1      // @ requires 0 <= n; decreases n
func Loop(i I, n int) { i.M(n) }           func (t T) M(n int) { util.Loop(t, n) }
//   O1: (n, Itf) ≺ (n+1, NonItf)   ✓      //   static, cross-SCC → unchecked today
```

Verified: both packages currently pass with 0 errors.

## 4. Cost

P1 rejects any member with a bare `decreases` that dispatches dynamically, since `(Itf) ≺ (NonItf)` is
false. That is the correct answer for an open interface — a future package may implement it — but it
is a breaking change and the main risk in this proposal. Users express the intended argument by giving
the interface a strictly smaller measure (a rank threaded through the contract); `decreases _` remains
the escape hatch.

Optional precision recovery (**P5**, follow-up): keep SCC gating for *sealed* interfaces — unexported,
or restricted by `friend` clauses — where the declaring package really does see every implementation.

## 5. Alternatives rejected

- **Stub body calling every interface method.** Sound (edges only enlarge SCCs), and it does reject the
  counterexample — but only because imported members are tuple-less, so every intra-SCC call to one
  fails. One SCC swallows every implementation that calls into any imported package. It also blames the
  client rather than the package whose contract is wrong.
- **Forbid the pattern** (Dafny's `{:termination false}`): sound and cheap, but rules out legitimate
  cross-package dynamic recursion.
- **Whole-program mode**: abandons modularity; unusable against stub packages that have no bodies.
- **Call permissions** (Jacobs et al., VeriFast): the general contract-carried solution, and what P1's
  rank idiom approximates by hand. Larger surface change, but it needs no whole-program assumption and
  deletes more than it adds — written up separately in `modular-termination-call-permissions.md`, and
  preferred over this plan if the syntax cost is acceptable.

## 6. Implementation

Two additive `Info` markers in Silver, neither affecting existing programs:

| | change |
|---|---|
| `PotentiallyRecursive` | widens the guard at `MethodCheck.scala:66` (and the `FunctionCheck` equivalent) so a marked call always gets the tuple check |
| `DeclaredCallees` | `methodCallGraph`/`functionCallGraph` also read edges from this marker, so body-less members contribute edges |

Gobra side, in order:

1. Attach `PotentiallyRecursive` to interface-receiver `MethodCall`s in the encoding, and to the calls
   `CGEdgesTerminationTransform` emits. → P1, P2.
2. Record call edges for imported members during spec-only parsing; carry them through the internal AST
   and attach `DeclaredCallees`. → P3. Stub packages with no bodies need an annotation; defer.
3. Restrict the wildcard rewrite in `TerminationMeasurePostprocessor` to `pure` members. → P4.

If the upstream changes are unwelcome, both are replaceable Gobra-side: emit the assertion directly
using the `WellFoundedOrder` domain functions, and emit `if (false) { … }` stub bodies. Uglier, and
step 2 then needs synthetic arguments.

## 8. What was implemented

P3 and P4, in the coarse form: every member whose body was dropped by spec-only parsing gets a
vacuous body calling *every* interface method of the program, and the same stub is appended to each
interface method's dispatch body (an interface may be implemented by any package importing it, so a
method with no visible implementation must still be assumed to dispatch). Non-`pure` imported members
keep their measure tuples. `PFunctionSpec` and the internal `Method`/`Function` carry a `bodyErased`
flag so that members which are abstract or `trusted` by design — whose contracts the author asked to
be assumed — are left alone; stubbing those breaks the SIF encoding and retracts the assumption.

All three counterexamples are now rejected, and the two mutually recursive interfaces are rejected in
`p1` and `p2` *individually*, neither package knowing the other exists. `GobraTests` and
`GobraPackageTests`: 1227/1229, the two failures being the Carbon tests, which need `BOOGIE_EXE` and
fail identically without this change.

### Closures have the same defect, and it is worse

`cl(args) as spec` is dynamic dispatch through `spec`'s contract, and it received neither of the two
ingredients interfaces got: `ClosureSpecsEncoder` emits `closureCall$<spec>` as a **body-less** method
carrying the spec's measure (`ClosureSpecsEncoder.scala:298`), created during encoding — after
`CGEdgesTerminationTransform` runs — so it has no dispatch body over the proven implementations and no
stub, and its measure carries the plain `NonItfMethodMeasure()` tail rather than an interface-style
tier. Calls to it are therefore never inside a component and never receive a decrease obligation. The
following diverging program verifies with 0 errors **in a single package** (the existing
`closures-recursion1-simple` test does not catch this because a literal used as its own spec keeps its
body, so self-spec recursion is visible — dispatch through a declared spec is not):

```go
type Box struct { f func(Box) int }

decreases
requires b.f implements spec1
func spec1(b Box) (r int) { return 0 }

decreases
func run() int {
	c := requires b.f implements spec1
	     decreases
	     func rec(b Box) (r int) { r = b.f(b) as spec1; return r }
	proof c implements spec1 { r = c(b) as rec }
	return c(Box{f: c}) as spec1        // run → rec → rec → …  stack overflow in Go
}
```

A fix needs the closure analogues of both interface ingredients (a dispatch body over proven
implementations plus a spec-tier in the measure encoding), or the call-permission design, which covers
interfaces and closures uniformly because the required bound rides in the spec's contract. Method
values (`x.m implements spec`, `MethodObjectEncoder`) are the same family and are unaudited.

### Residual unsoundness: the obligation inside the erased body

Retaining the measures (P4) leaves a hole of its own. Checks the plugin inserts inside a stub are
placed after its leading `assume false`, so they are vacuous — the stub contributes edges, never
obligations. When the only non-descending edge of a cycle is one of those, nothing fires:

```go
// util                                    // main
requires 0 <= k                            requires 0 <= n
decreases k                                decreases n
func Loop(i I, k int) {                    func (t T) M(n int) {
    i.M(k + 1)   // ← the bad edge             if n > 0 { util.Loop(t, n-1) }
}                                          }
```

`I.M` has `decreases n`. Both packages verify with 0 errors; `T.M(n)` calls `Loop(t, n-1)` which
calls back `t.M(n)`, and the Go equivalent stack-overflows. The two checks that do fire both pass
honestly — `T.M → Loop` is `(n-1) ≺ (n)`, and `I.M → T.M` is `(n, NonItf) ≺ (n, Itf)`. The one that
would fail, `Loop → I.M` needing `(k+1, Itf) ≺ (k, NonItf)`, sits inside the stub.

Wildcarding imported measures instead closes this, because an intra-component call to a tuple-less
member is an unconditional failure — but it is a poison pill, not a check, and it costs every
cross-package dynamic recursion. Measured on one build, flipping only `TerminationMeasurePostprocessor`:

| | measures kept | measures wildcarded |
|---|---|---|
| the divergent program above | **0 errors** (unsound) | rejected |
| a terminating cross-package recursion | verifies | **rejected** (incomplete) |

So under "obligations inside erased bodies are ignored" the choice is soundness or completeness, never
both: the fact that separates the two programs lives exactly where the design has chosen not to look.
Both entries are repaired by P1, which discharges `Loop → I.M` in `util`, where the call site and its
real arguments are — `Loop` calling `i.M(k+1)` above its own measure is a local property of `util`.
With P1 in place the measures are safe to keep and the completeness row survives.

Two further limitations remain, and they are why the call-permission design is still preferred:

- **Blame can land on the client.** In §1's `Loop` example the wrong contract is `util`'s, but `util`
  still verifies and the importing package is rejected. Edges cannot fix this: putting `Loop` in a
  component would need an edge from an interface method back to it, i.e. edges from every interface
  method to every member. Only the unconditional obligation P1 reports it in `util`.
- **The whole-program assumption stands.** A cycle is only caught in a package where all of its
  members are visible.

## 7. Tests

- The two counterexamples above (four- and two-package), as expected-error tests.
- The rank example from §3, as a positive test — cross-package dynamic recursion must still verify.
- `termination-fail-03.gobra` split across packages, mirroring the single-package original.
- Positive test: implementation that dispatches with a strictly smaller interface measure.
- **Run the full regression suite after step 1.** P1's blast radius is the open question; if positive
  tests break, that is the signal to bring P5 forward.
