# Modular termination checking with termination levels

*Written by Claude (AI) together with the implementation on branch `claude/youthful-goldberg-jxj0u1`,
2026-11-01. The design follows Jacobs, Bošnački and Kuiper, "Modular Termination Verification of
Single-Threaded and Multithreaded Programs" (TOPLAS 40(3), 2018), cited as **[JBK]**, and borrows
Dafny's module/SCC ordering. Sections 1–7 describe what is implemented. Section 8 lists what is not.*

## 0. Summary

Every terminating member (function, method, interface method, closure spec, function literal) gets a
**termination level**: a finite multiset of *atoms*. The level is the first component of the member's
termination measure, and the measure the user writes follows it. At **every** call, Gobra checks that the
callee's measure at the call is below the caller's measure in its pre-state. This covers static calls,
interface calls, closure calls, calls in contracts, and calls to imported members. The order on atoms is
fixed per package and needs no call graph of the whole program, so:

* a package is verified using only the *specifications* of the packages it imports
  (viperproject/gobra#1105 is rejected in the package that closes the cycle);
* calls through closures are checked against the level of the called closure
  (viperproject/gobra#1106 and Landin's knot are rejected);
* the textual order of declarations is irrelevant.

Specifications stay **abstract**. Users can't name atoms, so a contract can never list what a member
calls. With the default levels, `decreases` and `decreases n` keep their meaning, and code that only
makes static calls needs no new annotations. Contracts reveal only what [JBK] argues no modular approach
can hide:

* whether a member makes its arguments call each other (§3.3);
* how wrapper objects relate to the objects they call. This goes through an abstract `TerminationLevel()`
  method (§4).

## 1. Why the previous approach was unsound

Gobra used to delegate termination checking to Viper's termination plugin.
`CGEdgesTerminationTransform` first added call-graph edges from interface methods to their known
implementations. The plugin then checks that the measure decreases only for calls **inside a strongly
connected component of the call graph of the Viper program**. That is sound only if every cycle of the
real program is visible in that one Viper program, and it often isn't:

* Imported packages: the bodies of their non-pure members are erased. Gobra also turned imported
  measures into `decreases _`. So a cycle that leaves the package through a call into an imported
  package and comes back through dynamic dispatch went unnoticed (#1105).
* Closure specs: they are encoded as bodiless members after the transform, so calls through them were
  never part of a cycle (#1106).

## 2. Levels and atoms

**User-facing syntax.**

| Construct | Meaning |
|---|---|
| `level` | ghost type of termination levels (finite multisets of atoms) |
| `l1 + l2` | multiset union |
| `l1 < l2`, `l1 <= l2`, `==` | multiset (Dershowitz–Manna) order induced by the order on atoms |
| `emptyLevel()` | the empty multiset |
| `levelOf(x)` | the level of the object `x` (interface value, closure, value of a named type, §4–5) |
| `decreases L, m1, …, mk` | if the first component `L` has type `level`, it is the member's *dynamic level* (§3) |
| `ghost pure TerminationLevel() level` | optional method by which a type, or an interface, defines the level of its values (§4) |
| `decreases bottomLevel(), m…` | places the member in the *bottom stratum* (§3.4) |
| `ownLevel()` | in a member's contract: the member's own level, in its pre-state (§3.5) |

**Atoms.** Every function, method, function literal and named type has an atom. Atoms are never
written by users. For the package being verified, Gobra builds a dependency graph over its declarations:

* `f → g` if `f`'s body or contract statically calls `g`, or uses `g` as a function or method value;
* `f → lit` if `f` creates the function literal `lit`;
* `f → T` if `f` converts a value of named type `T` (or `*T`) to an interface with methods, or creates a
  method value `x.M` with `x` of type `T`. Converting to a method-less interface such as `interface{}`
  (e.g., to pass a value to `fmt.Sprintf`) adds no edge, because such values cannot be called;
* `T → T.M` for every method `M` of `T` (a type is above its methods).

Interface calls and closure calls add **no** edge. Each strongly connected component (SCC) of the graph
is **one** atom (`TerminationLevelsTransform.mainComponents`). The order on atoms is:

* within the package, callee SCC below caller SCC;
* every atom of an imported package is below every atom of the importing package;
* the atom of a method `T.M` is at most the atom of `T`. This holds in every package because of the edge
  `T → T.M`, and it reveals nothing about implementations. Implementation proofs in one package for a
  type of another package need it;
* nothing else. In particular, a package assumes nothing else about the relative order of two atoms of
  an imported package, not even that they differ.

Each package is thus verified under every extension of the facts it assumes. This is the modularity
condition of [JBK]'s formalization (Fig. 40). The intra-package order is derived from the code, not from
the order of declarations, and clients never see it. Reordering, splitting or merging the members of a
package, or making a member call a new helper or library, never changes a client-visible fact.

The dependency graph only serves to choose a permissive order. **Soundness does not depend on it**:
every call is checked against the order. A missing edge can only cause a spurious error, never an
unsoundness. Under the previous approach, a missing edge was an unsoundness.

## 3. The termination rule

### 3.1 Levels of members

`dyn` is the *dynamic level*: by default the `lmax` of `levelOf(p)` over the parameters `p` of function
type or of an interface type with methods, and the empty level if there are none. Parameters of
method-less interfaces (`interface{}`, or interfaces with only predicates) do not count: their values
cannot be called. A user-written first component of type `level`
replaces `dyn`. The member's **own** part is always added implicitly:

| Member | Level |
|---|---|
| function `f` | `{f} + dyn` |
| method `T.M` of a non-interface type | `{T.M} + recv.TerminationLevel() + dyn`. The middle summand appears only if `T` declares `TerminationLevel`. |
| interface method | `levelOf(recv) + dyn` |
| closure spec `s`, called as `c(args) as s` | `levelOf(c) + dyn` |
| function literal `lit` | `{lit} + dyn` |
| `TerminationLevel` methods, and members with `decreases bottomLevel(), …` | the empty level: the bottom stratum (§3.4) |

`lmax` is the maximum with respect to an unknown linearization of the order, as in [JBK] A.1.5. Users
cannot write it. It satisfies `l <= lmax(l, r)` and `lmax(l, r) ∈ {l, r}`.

### 3.2 Checks

* **(CALL)** At every call of a terminating member `g` in a terminating member `f`, the tuple
  `(level_g, local_g…)` evaluated at the call must be below `(level_f, local_f…)` evaluated in `f`'s
  pre-state. Tuples are compared lexicographically. This holds in bodies, contracts, loops and
  `unfolding`s, for static, interface and closure calls alike. For interface and closure calls, `g` is
  the interface method or closure spec used at the call. Conditional measures keep their meaning.
* **(IMPL)** Implementation proofs (`T implements I`, and `proof c implements s`) check that the
  implementation's measure is **at most** the spec's measure. Dispatch is not an extra call.
* **Loops** keep their own `decreases` measures. Viper's termination plugin still checks those.
* **Non-terminating members** (no `decreases`) may call anything. Terminating members may not call them,
  as before. `decreases _` and `trusted` remain explicit assumptions.
* **Imported members** are not re-checked. Their measures are used only to check calls to them.
* **Tuples of different length.** Every tuple ends in a *kind marker* (§7). If the callee's tuple ends
  where the caller's continues with local measures, the callee is smaller. A member without local
  measures cannot call a member of its own level, so this is sound. Viper's plugin instead treats the
  shorter tuple as larger, which rejected, e.g., an implementation with `decreases` of an interface method
  with `decreases ErrorMem()` whenever the two had the same level.
* **Methods of the same object in contracts.** All methods of an interface value share the level
  `levelOf(recv)`, but contracts commonly call other pure methods of the receiver
  (`requires IsElem(e)`). Interface methods therefore have a *rank*: one plus the highest rank of the
  interface methods their own contract calls. The rank is part of the kind marker, so it is compared
  only when levels and local measures are equal. Ranks depend only on interface declarations, which every
  importing package sees, so all packages agree on them.
* **Imported helpers in interface contracts.** The contract of an interface method (or of an
  implementation proof) may call a member whose level consists of imported atoms only, e.g.
  `seqs.ToSeqByte`. The interface's level `levelOf(recv)` is opaque, so the comparison could never
  succeed. It is not needed: such a member has no dynamic arguments through which it could call back,
  and the objects it can create or bound have types of its own package or of lower ones. The exception
  covers contracts only. It does not cover the body of an implementation proof, whose call of the
  implementation is the check that the implementation's measure is at most the interface's. Nor does
  it cover callees of the bottom stratum, which may call interface methods of the bottom stratum.
  (Both cases were accepted in an earlier version; found while testing the design.)

### 3.3 What the defaults buy

* **Static calls.** A call to a lower SCC or to an imported member decreases the own part. A call inside
  an SCC compares the local measures, exactly as the plugin did before.
* **Interface or closure arguments passed on**: `{g} + levelOf(x) < {f} + levelOf(x)`, because `g` is
  below `f`.
* **Calling methods of a parameter**: `levelOf(x) < {f} + levelOf(x)`.
* **One object passed twice**: `lmax(levelOf(a), levelOf(a)) == levelOf(a)` (see `contains` in
  `termination-levels-explicit.gobra`).
* **Arguments calling each other** cannot be hidden ([JBK] §2.2.5). Write the sum instead of the
  default max:

  ```go
  requires a != nil && b != nil
  decreases levelOf(a) + levelOf(b)   // a's methods may call b's
  func intersects(a Set, b Set) bool { return a.Intersects(b) }
  ```

### 3.4 The bottom stratum

Some members recurse over *data* whose dynamic type is unknown, not over layers of code. The ghost
methods `IsDuplicableMem` and `Duplicate` of `error` are an example: an error wrapper recurses into its
`Cause`, which may be another wrapper of any type. No level can bound "whatever error is nested
inside". [JBK]'s answer, already used for `TerminationLevel` methods, is the empty level. A member whose
measure starts with `bottomLevel()` gets exactly the empty level, with no atom of its own and no
dynamic part:

* it can be called from anywhere, because the empty level is below every other level;
* it may only call members of the bottom stratum (anything else has an atom, so it is bigger);
* calls within the bottom stratum compare the remaining measures. Predicate instances such as
  `ErrorMem()` are typical; their nesting order does not depend on any package.

Implementations of a bottom-stratum interface method must be in the bottom stratum as well (IMPL), and
they must say so explicitly. Inferring it from "implements a bottom interface method" would make a
method's level depend on implementation proofs in other packages, which is unsound. The built-in `error`
interface declares `IsDuplicableMem` and `Duplicate` with `decreases bottomLevel(), ErrorMem()`.

`bottomLevel()` is distinct from `emptyLevel()`: `decreases emptyLevel()` means "own atom, no dynamic
part", for members that do not call their arguments' methods (e.g., `serrors.WrapStr`, which only
stores its cause).

Interfaces whose terminating methods are all in the bottom stratum (e.g. `hash.Hash` or gopacket's
`SerializeBuffer` in VerifiedSCION) do not count as dynamic: calls through them are charged with the
empty level, so their arguments do not contribute to the levels of members. Objects of these
interfaces can be passed even if their level is unknown, e.g., when they are read from the heap or a
global variable.

### 3.5 Levels of returned objects: `ownLevel()`

A caller can only call methods on an object it got from a callee if it knows a bound on the object's
level. [JBK] §2.2.3 lets factories promise that their results are below their own level. Since users
cannot name atoms, Gobra provides `ownLevel()`, the level of the member in whose contract it occurs,
evaluated in the pre-state:

```go
ensures err != nil ==> levelOf(err) < ownLevel()
decreases
func (l LinkType) MarshalText() (res []byte, err error)
```

The caller knows that the callee's level is below its own, by the call check. Transitivity then gives
`levelOf(err)` below the caller, so the caller may call `err.Error()`. This is opt-in. An implicit
postcondition for every member returning an interface would be abstract too. It would, however, fail for
members returning stored objects, and it would bring the level theory into nearly every Go program,
which measurably slows Z3 (§7). The SMT encoding supports chaining two "below" facts through an opaque
level, e.g. `levelOf(mode) < {NewCBCEncrypter} + levelOf(block)` and `levelOf(block) < {NewCipher}`.

## 4. Objects: interface values and wrappers

The level of an interface value is the atom of its **dynamic type**, encoded as `$typeLevel(typeOf(x))`.
It is fixed only for the types the package knows (`$typeLevel(T) == {T}`) and is unconstrained
otherwise. This suits leaf implementations, whose methods call only things below their own type, and that
covers most code.

**Wrappers** call other objects, so their level must account for them ([JBK] §2.2.2 and §3.3). An
interface (or a type) declares the ghost method `TerminationLevel()`. Then `levelOf(x)` is
`$typeLevel(typeOf(x)) + x.TerminationLevel()`, and the methods of an implementation `T` run at
`{T.M} + recv.TerminationLevel() + dyn`. The body of `TerminationLevel` sits behind the predicate that
frames it, so clients only ever see `levelOf(x)`:

```go
type Func interface {
	pred Mem()

	ghost
	requires acc(Mem(), _)
	decreases Mem()
	pure TerminationLevel() level

	requires acc(Mem(), 1/2)
	ensures  acc(Mem(), 1/2) && TerminationLevel() == old(TerminationLevel())
	decreases
	Apply(x int) int
}

type Plus1 struct{ f Func }
pred (p Plus1) Mem() { p.f != nil && p.f.Mem() }

ghost
requires acc(p.Mem(), _)
decreases p.Mem()
pure func (p Plus1) TerminationLevel() level {
	return unfolding acc(p.Mem(), _) in levelOf(p.f)   // Plus1 calls p.f
}

// Apply runs at {Plus1.Apply} + levelOf(p.f); the call p.f.Apply(x) is at levelOf(p.f).    ✓
// Plus1 implements Func: {Plus1.Apply} + levelOf(p.f) <= {Plus1} + levelOf(p.f).             ✓

requires f != nil && acc(f.Mem(), 1/2)
ensures  acc(f.Mem(), 1/2)
decreases                     // {Deriv} + levelOf(f): Deriv's spec does not say what it calls
func Deriv(f Func, x int) int { return f.Apply(x+1) - f.Apply(x) }
```

A wrapper whose `TerminationLevel` omits `levelOf(p.f)` is rejected at its call of `p.f.Apply`
(`Bad` in `termination-levels-wrappers.gobra`). Levels may depend on the heap, because they are
evaluated at each call. A method that may change its receiver's level has to say how. The postcondition
`TerminationLevel() == old(TerminationLevel())` above is what lets callers call `Apply` twice.

**Mutual recursion through dynamic binding** ([JBK] Fig. 14) works with the defaults. If `e.IsEven(o, n)`
calls `o.IsOdd(e, n-1)`, both calls are at level `levelOf(e) + levelOf(o)`, and the local measure `n`
decreases (`termination-levels-mutual.gobra`).

## 5. Closures

A closure's level is fixed when the closure is created, by a postcondition of the closure getter. It is
encoded as `$closureLevel(c)`:

* `{lit}` for a function literal `lit`;
* `{F}` for a function `F` used as a value;
* `levelOf(x)` for a method value `x.M`. For a named type `T`, that is `{T}`, and `T.M`'s atom is at
  most `T`'s. For an interface, it is the level that interface calls are charged with. It is set only when
  the receiver's level does not depend on the heap, i.e., when the type declares no `TerminationLevel`.

Only closures with termination measures get a level, because only they can implement terminating
specs.

* A call `c(args) as s` is charged at `s`'s level, `levelOf(c) + dyn`.
* The body of a literal is checked at `{lit} + dyn`.
* `proof c implements s` checks (IMPL): the calls in the proof body are compared with the measure of `s`.

**Landin's knot (#1106) is rejected without a syntactic ban.** Storing a closure in the heap and calling
it from itself would need `levelOf(c) < levelOf(c)`:

```go
c := requires b.f implements spec1
     decreases
     func rec(b Box) (r int) {
         r = b.f(b) as spec1        // levelOf(b.f) is unknown: it might be {rec}            ✗
         return r
     }
```

A closure can still safely call closures it receives or reads from the heap, if its spec bounds their
level. In `run2` of `termination-closure-knot.gobra`, `decreases levelOf(b.f)` does this.

### Outlines

An `outline (...)` block is encoded as a separate method, which is called where the block occurs. It is
checked like a member at the level of the enclosing member (or literal, or implementation proof). Its
measure is `level, μ_block..., OutlineMeasure(h)`. The marker `OutlineMeasure(h)` is below every other
kind marker, and `h` is the nesting height of the block (an outline nested in another one has a smaller
height). The call of the block is checked like every other call, so the measure of the block has to be at
most the measure of the enclosing member where the block occurs. A block with the same measure as its
member is fine, because its marker is smaller. Calls in the block are compared with the measure of the
block. A cycle through the block therefore decreases the member's measure, whatever measure the block
declares:

```go
decreases n
func rec(n int) {
	decreases 1000                // ✗ 1000 is not at most n
	outline ( rec(n) )            // ✗ n is not below 1000
}

decreases n
func recOk(n int) {
	if n > 0 {
		requires n > 0
		decreases n               // ✓ n at most n, and OutlineMeasure(0) < NonItfMethodMeasure()
		outline ( recOk(n - 1) )  // ✓ n - 1 below n
	}
}
```

The level of the block is stated in terms of the copies of the parameters that the member's body works
with. If a copy is assigned before the block, the block gets a different level, and the check of its call
compares this level with the member's level on entry. Before this rule, blocks had no level at all. Calls
through interfaces and closures inside a block were therefore not checked, and a Landin knot wrapped in
an `outline` verified (found while testing the design; see `termination-levels-outline.gobra`).

## 6. Worked example: #1105

```go
// package util: verifies on its own
type I interface {
	decreases          // levelOf(recv)
	M()
}

requires i != nil
decreases              // {Loop} + levelOf(i)
func Loop(i I) { i.M() }   // levelOf(i) < {Loop} + levelOf(i)   ✓
```

```go
// package main
type T struct{}

decreases              // {T.M}; T.M and T share an SCC because T.M converts t to util.I
func (t T) M() {
	util.Loop(t)       // {util.Loop} + {T} is not below {T.M} = {T}   ✗ reported here
}
```

The error is reported in the package that closes the cycle, and neither package needs the other's
bodies (`termination-import-cycle/`).

## 7. Implementation

**Frontend.** The `level` type, `levelOf`, `emptyLevel`, and `+`, `<`, `<=` on levels. Imported measures
are kept: the parser no longer turns them into wildcards.

**Desugarer.** Collects the termination order seed: the package of each atom, the import relation, and
the imported members.

**`TerminationLevelsTransform`** (internal AST; replaces `CGEdgesTerminationTransform`). Computes the
SCC atoms, adds the levels of §3.1 as the first measure component, and marks imported measures.

**`LevelEncoding`.** Encodes the Viper domains:

```
domain $Atom   { unique-per-SCC functions $atom_c<i> for the package; non-unique ones for imported atoms;
                 $atomLt facts: SCC DAG (transitively closed), imported < main, import order }
domain $Level  { $lvl(Multiset[$Atom]) / $atoms(l) bijection; $union(l, r);
                 $cnt(x, l), $cntW(x, l): multiplicities, $cnt decomposes over $union;
                 $levelLt: Huet–Oppen characterization of the multiset order;
                 $levelMax: one of its arguments, and at least both }
$typeLevel(t)    the atom of a dynamic type, axiomatized for the package's named types
$closureLevel(c) the level of a closure value
```

Transitivity of `$atomLt` is only needed for chains that start at an atom of an opaque level, e.g., an
atom of `levelOf(res)` known from `levelOf(res) < ownLevel()`; the facts between the package's atoms are
transitively closed already. The witnesses of *assumed* comparisons are marked with `$atomLtW`, and the
transitivity axiom is triggered by `$atomLtW(a, b), $atomLt(b, c)`. A trigger on two `$atomLt`
applications would instead match all pairs of ground facts: `pkg/slayers` has 121 atoms and about 5,400
facts, which allows on the order of 10^5 redundant instances in every proof obligation.

The universally quantified atom of the Huet–Oppen characterization is matched through `$cnt`. The
existential witness is stated with `$cntW`. Both are the multiset count, but only `$cnt` triggers the
quantifier. With a single count function, every assumed `l < r` would start a matching loop, because
each witness re-triggers the quantifier and produces a new witness. Every check that passes is
afterwards assumed, so this happened routinely. A five-method test drove Z3 past 7 GB before the split
and verifies in 10 s after it.

The kind markers that end the tuples (`NonItfMethodMeasure()`, `ProofMeasure(rank)`,
`ItfMethodMeasure(rank)`, `ClosureProofMeasure()`) are ordered by a fixed set of quantified axioms. One
ground axiom per pair of ranks slowed Silicon's domain-instance computation, which unifies every ground
`decreasing` application with every axiom mentioning `decreasing`.

Termination measures get a `WellFoundedOrder` instance for `$Level`. Before each comparison, Gobra emits
*level hints*: the multiplicities of the syntactically occurring atoms. These let Z3 decide the typical
obligations `X < {f} + X` and `{g} + X < {f} + X` without instantiating the inner quantifier blindly.

**Static comparisons.** Most calls compare levels made of atoms only. Calls to lower SCCs or imported
members, and recursion within an SCC, are like this. The transformer decides these itself. Write
`callee = C + E1` and `caller = C + E2`, where `C` is the syntactically common part. The levels are equal
if `E1` and `E2` are both empty; then only the rest of the tuple is checked. The callee level is smaller
if `E2` is nonempty and every atom of `E1` has an axiomatized `$atomLt` fact to some atom of `E2`. That
holds in every model, even when atom names alias. If only the callee's level is made of atoms (the
caller's level has an opaque part such as `levelOf(df)` for an interface parameter `df`), the callee is
below the caller if it is below the atoms the caller certainly contains, as adding atoms makes a
multiset bigger. Without this, every call in a member with an interface parameter reached Z3 as a
multiset problem: `SCION.DecodeFromBytes` in VerifiedSCION has 22 such checks, repeated on every path.
The level theory then never reaches Z3 for programs without dynamically bound calls. This matters because Z3 is sensitive to unrelated declarations: a
magic-wand proof (`wands/ghost-list.gobra`) went from 12 s to 90 s and timed out with the level domain
merely present.

**`ModularTerminationTransformer`** (Viper level). Adds the (CALL) checks: assertions before method calls,
and check methods for function bodies and contracts. The checks reuse the plugin's
`DecreasesCheck`/`ExpTransformer` machinery. For the plugin itself, measures that start with a level are
replaced by `decreases _`. The plugin then only checks loops and members without levels, so no error is
reported twice.

**Interfaces and closures.** Implementation proofs turn the strict check at their call into `≤` through
the last tuple element: `NonItfMethodMeasure < ProofMeasure < ItfMethodMeasure < ClosureProofMeasure`.
Closure implementation proofs carry the spec's tuple, ending in `ClosureProofMeasure`, on the proof's
encoding loop. Their single call may therefore be an interface call at the spec's level.

**Pruning.** Many leveled measures are encoded but then hidden from the plugin and never used by a
check. Atoms that the final program does not reference are removed together with the order facts about
them. In `pkg/experimental/epic` of VerifiedSCION, this removed 756 of 815 atoms and 208,138 of 209,067
facts: the Viper program shrank from 870,826 to 31,508 lines, and Silicon no longer spends minutes
computing domain instances. The transformer therefore removes the level domains, or their `$typeLevel`/`$closureLevel`
axioms, when the final program does not use them. Without this, the multiset theory over atoms
perturbed Z3 on unrelated members. One closure test went from 14 s to minutes in about half of the runs,
with Z3 above 10 GB.

The order facts are spread over small domains (`$AtomOrder0`, `$AtomOrder1`, ...). Viper's chopper,
which VerifiedSCION's router uses (`chop: 10`), hashes the enclosing domain of every axiom it considers.
With all facts in one domain, chopping the router took longer than 30 minutes.

**Tests.** The following tests are in `src/test/resources/regressions/features/termination/`.

| Test | What it shows |
|---|---|
| `termination-import-cycle/` | #1105 across packages |
| `termination-closure-knot.gobra` | #1106 and a terminating variant |
| `termination-levels-wrappers.gobra` | wrappers via `TerminationLevel` |
| `termination-levels-explicit.gobra` | explicit levels for arguments that call each other |
| `termination-levels-mutual.gobra` | [JBK] Fig. 14 |
| `termination-levels-method-values.gobra` | method values implementing terminating specs |
| `termination-levels-outline.gobra` | outlines, including knots hidden in outlines |

`termination-fail-02.gobra` now reports its error where the cycle is closed rather than at the
interface method.

## 8. Limitations and future work

* **Mutual recursion between closures stored in the heap.** In `closures-recursion2-mutual.gobra`,
  `is_even` calls `o` and `is_odd` calls `e` through captured variables. To verify this, a literal would
  have to know the level of the closure stored in a variable relative to its own level. Literals cannot
  refer to their own level. The test previously verified only because closure-spec calls were not checked
  at all (#1106), and it now expects two termination errors. The interface version of the same pattern
  verifies (§4), because a method can pass its receiver to its partner, so both calls are charged
  `levelOf(e) + levelOf(o)`. A closure has no receiver. Closing the gap needs a way for a literal to
  refer to its own level, plus specs that charge both closures' levels.
* **Composing wrappers in clients** sometimes needs a hint such as
  `assert fz.TerminationLevel() == emptyLevel()`, because of dispatch and limited-function depth.
* **Method values of types with a `TerminationLevel` method** get no level, because that level depends on
  the heap, so they cannot implement terminating specs.
* **Standard-library stubs** do not declare `TerminationLevel`, so wrapper implementations of `io.Reader`
  and similar interfaces cannot verify yet.
* **Objects stored in globals or fields** must have their level bounded by an invariant before they can
  be called; [JBK] §3.3 requires the same of any modular approach. Two cases occur in VerifiedSCION:
  * `path.NewPath` calls constructor closures that higher packages register in the global
    `registeredPaths`: an up-call. These constructors only allocate a path, so they and `NewPathSpec` fit
    the bottom stratum (`decreases bottomLevel()`), which solves this case.
  * The methods of `slayers.SCION` call methods of the field `s.Path`, whose dynamic type comes from the
    registry. Any package above `slayers` could store a `path.Path` whose methods call back into
    `slayers`, so these calls genuinely depend on how clients use the registry. A modular proof needs
    a level bound carried from the registry through `NewPath` into `SCION`'s invariant, with callers of
    `SCION` paying for it. This is not done yet; four calls in `pkg/slayers` fail.
* **Chained bounds.** The encoding supports chaining two "below" facts per comparison (§3.5), not more.
* **SMT cost.** The multiset order is quantified. Comparisons of atom-only levels are decided by Gobra,
  and the level theory is removed from programs that do not need it. Where it is needed, Z3 is
  sensitive to its mere presence, so unrelated hard proofs can slow down.
* **Concurrency is not implemented.** Gobra's termination notion is per call. Blocking operations
  (channel operations, `select` without `default`, `Mutex.Lock`, `WaitGroup.Wait`) are still not
  rejected in terminating code; this gap predates the change. The planned stages are:
  * **Stage 2:** obligations and wait levels in the Chalice style ([JBK] §4.1–4.2). Wait levels are pairs
    `(termination level, local level)`, which lets a member take private locks without changing its spec
    ([JBK] Fig. 25).
  * **Stage 3:** call permissions `CallPerm(l)` ([JBK] §4.3–4.4). These cover worker pools that receive
    tasks of arbitrary level through channels, and CAS retry loops.

## 9. Soundness sketch

1. The global atom order is well founded. It is the union of the packages' SCC orders and the
   (acyclic) import order, over a finite program.
2. Each package's proof stays valid under any extension of the facts it assumes. It assumes only its own
   SCC order, the import order, and facts from imported specifications, which their packages verified.
3. The Dershowitz–Manna order on finite multisets over a well-founded set is well founded, and so is its
   lexicographic product with the local measures. `lmax` is interpreted in a fixed linearization.
4. (CALL) at every call and (IMPL) at every dynamically bound call imply that the measures along any
   call chain strictly decrease. Levels are evaluated at each call, so heap-dependent levels are fine.
   Infinite call chains are therefore impossible ([JBK] Theorem A.1).
