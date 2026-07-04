# Plan: IEEE 754 floating-point support in Gobra

Status: proposal / implementation plan
Scope: replace the current uninterpreted encoding of `float32`/`float64` with a
faithful encoding into the SMT theory of IEEE 754 floating points
(SMT-LIB `FloatingPoint`), following the approach used by Prusti.

---

## 1. Current state (baseline)

Float support today is deliberately partial:

- **Types** exist end-to-end: `PFloat32`/`PFloat64` (frontend AST),
  `Float32T`/`Float64T` (`frontend/info/base/Type.scala:44-49`),
  `in.Float32T`/`in.Float64T` (`ast/internal/Program.scala:1372-1380`),
  with `Float32HD`/`Float64HD` type heads for the interface/reflection theory.
- **Encoding** (`translator/encodings/FloatEncoding.scala`, a
  `LeafTypeEncoding`): both float types are mapped to `vpr.Int`, and
  `+ - * /` plus int↔float conversions become **uninterpreted, axiom-free
  `vpr.Function`s** (`addFloat32`, `mulFloat64`, `fromIntTo32`, …). No
  algebraic, rounding, or NaN semantics whatsoever; equality/ordering fall
  through to plain `Int` operations.
- **Float literals are rejected** at type-check time
  (`ExprTyping.scala:230`: *"floating point literals are not yet supported."*).
- **Missing operations**: unary minus, `float32 ↔ float64` conversion
  (absent from `property/Convertibility.scala:26-28`), any NaN/Inf reasoning.
- Registration happens in `translator/context/DfltTranslatorConfig.scala:73`;
  dispatch uses `TypePatterns.ctx.Float32/Float64`.
- Tests: `src/test/resources/regressions/features/floats/*.gobra`,
  `issues/000783-1.gobra`, `issues/000783-3.gobra`.

## 2. Target design (what Prusti does, adapted to Gobra)

### 2.1 The Viper mechanism: interpreted domains (`BackendType`)

Silver supports *backend-interpreted domains*: a `Domain` carries
per-backend interpretation strings and its functions carry SMT-LIB
interpretations. In Viper surface syntax:

```viper
domain Float64 interpretation (SMTLIB: "(_ FloatingPoint 11 53)", Boogie: "float53e11") {
  function f64_add(a: Float64, b: Float64): Float64 interpretation "fp.add RNE"
  function f64_lt(a: Float64, b: Float64): Bool interpretation "fp.lt"
  ...
}
```

Everything needed already exists at the Silver commit pinned by Gobra's
`viperserver` submodule:

- `vpr.Domain(..., interpretations: Option[Map[String, String]])`
  (`silver/src/main/scala/viper/silver/ast/Program.scala:542`),
  `vpr.DomainFunc(..., interpretation: Option[String])` (`Program.scala:595`),
  `vpr.BackendType(viperName, interpretations)` (`ast/Type.scala:203`),
  `vpr.BackendFuncApp` (`ast/Expression.scala:415`).
- **Ready-made factories**:
  `silver/src/main/scala/viper/silver/ast/utility/BackendTypeFactories.scala`
  provides `FloatFactory(mant, exp, roundingMode)` and `BVFactory(size)` that
  construct the interpreted domain and typed builders for
  `fp.add/sub/mul/div <RM>`, `fp.min/max`, `fp.eq/leq/geq/lt/gt`,
  `fp.neg/abs`, `fp.isZero/isInfinite/isNaN/isNegative/isPositive`,
  `from_bv` (`(_ to_fp e m)`), `to_bv` (`(_ fp.to_sbv n) <RM>`), and
  `RoundingMode` (RNE/RNA/RTP/RTN/RTZ).
- **Both backends supported.** Silicon emits the interpretation strings
  verbatim as SMT sorts/symbols (`silicon/src/main/scala/state/SymbolConverter.scala:39,57-60`);
  Carbon maps `BackendType` to Boogie's native float types and interpreted
  functions to `{:builtin "..."}` Boogie functions
  (`carbon/.../DefaultTypeModule.scala:52`, `DefaultFuncPredModule.scala:1206`).

So, unlike Prusti (which reaches these factories over JNI), Gobra can call
`FloatFactory`/`BVFactory` directly as plain Scala utilities.

### 2.2 What Prusti generates (and what we copy / fix)

Prusti (`prusti-viper/src/encoder/definition_collector.rs`) emits
`FloatDomain24e8` and a 64-bit float domain, with operations mapped as in the
table below, rounding mode hard-coded to **RNE** inside the interpretation
string (no `RoundingMode` sort ever appears in Viper). Literals are encoded as
**raw IEEE bit patterns** routed through a bitvector:
`f32_from_bv(bv32_from_int(1067030938))` for `1.2f32` — exact, no decimal
re-rounding at the SMT level.

Two deliberate deviations from Prusti:

1. **Fix Prusti's f64 format bug**: Prusti uses `(_ FloatingPoint 12 52)`,
   which is *not* IEEE binary64. Gobra will use `FloatFactory(24, 8, RNE)`
   → `(_ FloatingPoint 8 24)` for `float32` and `FloatFactory(53, 11, RNE)`
   → `(_ FloatingPoint 11 53)` for `float64`.
2. **Go `==` semantics**: Go's `==`/`!=`/ordering on floats are IEEE
   comparisons (`NaN != NaN`, `+0.0 == -0.0`), so Gobra must encode Go float
   (in)equality as `fp.eq`, not Viper's structural `==` (SMT `=`, under which
   `NaN == NaN` holds and `+0 != -0`). See §3.4.

### 2.3 Operation mapping

| Go construct | SMT-LIB (via interpreted domain func) |
|---|---|
| `x + y`, `x - y`, `x * y` | `fp.add RNE`, `fp.sub RNE`, `fp.mul RNE` |
| `x / y` | `fp.div RNE` — **total** in IEEE (÷0 → ±Inf/NaN), so *no* non-zero precondition, unlike ints |
| `-x` | `fp.neg` (NOT `0 - x`: `-(+0.0)` is `-0.0` but `0.0 - 0.0` is `+0.0`) |
| `x < y`, `<=`, `>`, `>=` | `fp.lt`, `fp.leq`, `fp.gt`, `fp.geq` |
| `x == y`, `x != y` | `fp.eq`, `!fp.eq` |
| literal `1.2` (typed) | `f32_from_bv(bv32_from_int(<bits>))` — bits computed host-side |
| `float64(x)` for `x: float32` (and back) | `(_ to_fp 11 53) RNE` applied to the f32 value (fp→fp conversion) |
| `floatN(i)` for integer `i` | `(_ to_fp e m) RNE` applied to `(_ int2bv 64) i` (signed-int→fp overload) |
| `intK(f)` | `(_ fp.to_sbv 64) RTZ` then `sbv → Int` (Go truncates toward zero); guarded, see §3.6 |
| zero value `var x floatN` | `from_bv(int2bv(0))` = `+0.0` |
| spec helpers (later phase) | `fp.isNaN`, `fp.isInfinite`, `fp.abs`, `fp.min`, `fp.max`, … |

Go disallows `%` on floats — the frontend already enforces this; nothing to do.

## 3. Implementation plan

### Phase 1 — Rewrite `FloatEncoding` on top of `FloatFactory` (core)

`translator/encodings/FloatEncoding.scala` stays a `LeafTypeEncoding`, but:

- Instantiate `FloatFactory(24, 8, RNE)` / `FloatFactory(53, 11, RNE)` and
  `BVFactory(32)` / `BVFactory(64)` lazily; `typ` maps
  `Float32/Exclusive → BackendType("(_ FloatingPoint 8 24)"...)`, `Shared → vpr.Ref`
  as today.
- `expression` handles `Add/Sub/Mul/Div`, the new unary-minus and literal
  nodes (§3.2/§3.3), comparisons and conversions, producing
  `vpr.BackendFuncApp`s via the factory builders.
- `finalize` emits only the domains actually used (keep the `isUsed32/64`
  pattern, fixing the existing quirk where `from32ToInt` is gated on
  `isUsed64`). Emit the BV domains only when literals/conversions need them.
- Custom interpreted functions not covered by the factory (e.g. the
  int→float overload `(_ to_fp e m) RNE` on a bitvector, fp→fp widening) are
  added as extra `vpr.DomainFunc(..., interpretation = Some(...))` members of
  the same domains — the factory objects don't preclude that.
- Division: drop any inherited non-zero obligation — IEEE division is total.

### Phase 2 — Accept float literals

- Remove the rejection at `ExprTyping.scala:230`; `PFloatLit` already gets
  `UnboundedFloatT` and merges with `Float32T`/`Float64T` via
  `TypeMerging`/`Assignability`.
- Add an internal literal node `in.FloatLit(value: BigDecimal, kind)` (or
  store the bits directly) in `ast/internal/Program.scala`; desugar
  `PFloatLit` using the type checker's resolved type
  (default type for untyped float constants per Go spec: `float64`).
- The **rounding to the nearest representable value happens host-side** in
  the translator: `value.toFloat` / `value.toDouble`, then
  `java.lang.Float.floatToIntBits` / `Double.doubleToLongBits`, emitted as
  `from_bv(int2bv(bits))`. This matches Go's compile-time constant→value
  conversion and is exact at the SMT level.
- Hex float literals: lift the current float64-only limitation in
  `ParseTreeTranslator.visitFloat` where straightforward; otherwise keep the
  explicit failure.
- Constant *expressions* over untyped floats (Go's arbitrary-precision
  constant arithmetic in `ConstantEvaluation`) are **out of scope** for the
  first iteration: support literals and typed-float expressions; reject
  constant folding cases we cannot yet evaluate, with a clear message.

### Phase 3 — Frontend/typing gaps

- **Unary minus**: ensure `-x` on floats desugars to a dedicated internal
  negation (not `0 - x`, see §2.3). Add an `in.Neg`-style node for numeric
  negation if desugaring currently rewrites to subtraction.
- **`float32 ↔ float64` conversions**: add the two cases to
  `property/Convertibility.scala` and desugar/encode as fp→fp `to_fp`.
- **Overflow transform**: no changes — Go floats overflow to ±Inf silently;
  the IEEE theory models this natively. Ensure the overflow-check transform
  (`ast/internal/transform/`) does not touch float ops.

### Phase 4 — Equality with Go semantics

- Override the equality hook of `LeafTypeEncoding` for exclusive floats so
  Go-level `==`/`!=` encode to `fp.eq` / `¬fp.eq`.
- **Known, documented limitations** (follow-up work, not blockers):
  - Floats *inside composites* (struct/array `==`, map keys, interface
    comparison) keep the generic structural equality machinery initially, so
    `NaN` inside a struct compares reflexively — unsound w.r.t. Go. Document;
    later thread float-awareness through the comparability encoding.
  - Viper-internal equalities (snapshots, framing, `old(...)`) remain
    structural (SMT `=`) — that is correct and desirable; only *source-level*
    Go comparisons switch to `fp.eq`.
- Consider a ghost built-in for bit-identity (structural) equality on floats
  for specs (`x === old(x)`-style reasoning that also covers NaN).

### Phase 5 — float↔int conversions, guarded

- `floatN(i)`: `(_ to_fp e m) RNE` over `(_ int2bv 64) i`. Gobra integers are
  bounded per kind, so 64-bit is enough for all named int kinds; `int2bv`
  truncation can't bite when overflow checking is on. Document the caveat for
  unbounded ghost integers (reject or require in-range precondition).
- `intK(f)`: `(_ fp.to_sbv 64) RTZ` (truncation toward zero = Go), then
  bv→Int. `fp.to_sbv` is *undefined* for NaN/±Inf/out-of-range — mirror what
  we do for other partial operations: emit a verification obligation
  (`!isNaN(f) && <in range of K>`), consistent with Go's
  implementation-defined behaviour being something we don't want verified
  programs to rely on.
- If the extra bv machinery proves painful in a first iteration, ship Phase 1–4
  with float↔int conversions still uninterpreted-but-axiomatised (injectivity),
  and do this phase separately.

### Phase 6 — Spec vocabulary / stdlib stubs

- Expose `fp.isNaN/isInfinite/abs/min/max/...` to users. Preferred route:
  built-in members (`frontend/info/base/BuiltInMemberTag.scala`) backing a
  verified `math` stub subset (`math.IsNaN`, `math.IsInf`, `math.Abs`,
  `math.Min`, `math.Max`, `math.Inf`, `math.NaN`, `math.Float64bits`, …),
  so user code and specs can talk about the interesting float predicates.
- `math.Float32/64bits` + `...frombits` map beautifully onto the bv route
  already needed for literals (`fp.to_ieee_bv` caveat: not in the factory —
  can be added as a custom interpreted function; note it is underspecified
  for NaN payloads).

### Phase 7 — Config flag, tests, CI, docs

- **Flag**: make the IEEE encoding the default (the current encoding cannot
  even express literals), keep the old uninterpreted encoding reachable via
  a config flag (e.g. `--uninterpretedFloats`) as a performance escape hatch —
  the FP theory is expensive for Z3, and existing users may prefer opaque
  floats. Wire through `frontend/Config.scala` +
  `DfltTranslatorConfig.scala:73` (choose encoding instance by flag).
- **Tests** (`src/test/resources/regressions/features/floats/`):
  - Re-baseline: `floats-simple-01` (still passes), `floats-fail-02` —
    `x + y - y == x` still fails under IEEE (rounding), keep as failing test;
    `issues/000783-1.gobra` expected `type_error` for literals — becomes a
    positive test.
  - New: literal exactness (`0.1 + 0.2 != 0.3`, `0.5 + 0.25 == 0.75`);
    NaN (`x != x` satisfiable, `isNaN` propagation); signed zero
    (`-0.0 == 0.0` true, `1.0 / -0.0` is `-Inf`); totality of division;
    overflow to `Inf`; unary minus vs `0 - x` on `-0.0`;
    `float32↔float64` conversion rounding; guarded float→int conversions;
    non-associativity (`(a+b)+c == a+(b+c)` fails); interpreted ops inside
    pure functions and specs.
  - Keep tests small: FP decision procedures are slow; watch CI timeouts.
  - Carbon: interpreted floats work in principle (Boogie `float24e8` types);
    add at least a smoke test if the Carbon CI job is active.
- **Docs**: `docs/tutorial.md` section on floats (semantics of `==`, NaN,
  rounding mode fixed to RNE, performance caveats, the flag).

## 4. Risks / open points

- **Solver performance**: the FP theory can blow up; hence the fallback flag
  and small regression tests. Consider recommending `--parallelizeBranches`
  etc. in docs if needed.
- **Triggers**: interpreted symbols can't serve as SMT quantifier trigger
  patterns; quantified specs over float expressions may need manual triggers
  on uninterpreted wrappers. Document; if it becomes pressing, wrap fp ops in
  trivially-defined Viper functions to make them triggerable.
- **Rounding mode**: fixed RNE (Go's only mode) — baked into interpretation
  strings, no `RoundingMode` sort surfaces in Viper.
- **Composite equality with NaN** (§ Phase 4) is the main known soundness
  gap of the first iteration; it must be listed in the release notes.
- **Silicon `--prover`**: works with Z3 (default); cvc5 also implements
  FP but is experimental in Silicon.

## 5. Suggested implementation order / PR slicing

1. PR 1 (core): Phases 1–3 + re-baselined tests — floats become genuinely
   usable (literals, arithmetic, comparisons, negation, fp↔fp conversion).
2. PR 2: Phase 4 (Go `==` semantics) + NaN/zero-sign tests.
3. PR 3: Phase 5 (guarded float↔int) — needs the bv plumbing.
4. PR 4: Phase 6 (math stubs / built-ins) + docs (Phase 7 items land with
   whichever PR introduces them; flag lands in PR 1).

A sketch of the target Viper encoding lives in
`encoding-sketches/floats.vpr`.
