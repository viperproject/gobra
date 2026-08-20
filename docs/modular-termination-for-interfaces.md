# Sound modular termination checking for interfaces

Status: proposal. Target: `CGEdgesTerminationTransform`, `TerminationEncoding`, `Parser`, and two small
additions to Silver's termination plugin.

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
  rank idiom approximates by hand. Too large a change to Gobra's specification language for this fix.

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

## 7. Tests

- The two counterexamples above (four- and two-package), as expected-error tests.
- The rank example from §3, as a positive test — cross-package dynamic recursion must still verify.
- `termination-fail-03.gobra` split across packages, mirroring the single-package original.
- Positive test: implementation that dispatches with a strictly smaller interface measure.
- **Run the full regression suite after step 1.** P1's blast radius is the open question; if positive
  tests break, that is the signal to bring P5 forward.
