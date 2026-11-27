# One implementation of hyperfuncs for both SIF encodings

Status: design, prototyped on the branch `claude/hyperfuncs-plugin-prototype` (based on #1116).

## Goals

1. **One hyperfunc translation.** The hyperfunc layer is implemented once. That layer covers:
   - hyperfunc definitions and calls;
   - `rel`, `low`, `active` and `lowEvent`;
   - lifting;
   - execution parameters;
   - termination of hyperfuncs.

   `--hyperMode on`, `--hyperMode extended` and the Viper hyperfuncs plugin (jcp19/hyperfuncs, `viper-plugin/`) all use that one implementation.
2. **One extended product construction.** The activation-based encoding is silver's `SIFExtendedTransformer`, extended by subclassing and never copied.

## Current state

There are three implementations of the same hyperfunc semantics. They differ only in the product construction underneath.

| | where | product construction | hyperfunc translation |
|---|---|---|---|
| `--hyperMode on` | `SIFLowGuardTransformer` (1247 lines), one of Gobra's `ViperTransformer`s, before ViperServer | lockstep, k executions, `active(i) = true` | `hyperFunction`, `hyperFuncApp`, `relationalAssertion`, `relationalExp` |
| `--hyperMode extended` | `GobraSIFExtendedTransformer` (2553 lines), loaded as the plugin `GobraSIFPlugin` after Viper's termination plugin; `HyperfuncMarkerTransformer` runs before, in Gobra | activation flags, k = 2 | `translateHyperFunction`, `translateHyperFuncApp`, `hyperAssertion`, `hyperValue` |
| Viper plugin | `HyperfuncsTransformer` (320 lines), a **subclass** of silver's `SIFExtendedTransformer` | the same as `extended` | the same as `extended`, reimplemented |

Both encodings take the same input. It is the annotated Viper program produced by Gobra's translator:
- hyperfuncs are Viper functions with the annotation `hyperFunc`;
- `exec` parameters are listed in `hyperFuncExecParams`;
- `low` and `rel` are silver's `SIFLowExp` and `SIFRelExp`;
- `active(i)` is an annotated abstract function.

Gobra's front end does the rest before translation:
- type checking, including the static rules for hyperfuncs;
- unrolling execution quantifiers;
- lowering `rel(e, i)` with an `exec` parameter `i` to a case distinction.

### Gobra's copy of the extended transformer

The copy was taken from silver commit `c4f54b8`. Compared with the silver version of Gobra's submodule, the diff is 302 changed lines in 14 hunks. Every hunk is about hyperfuncs, and the Viper plugin makes each change as an override of silver's class, without any change to silver:

| hunk in Gobra's copy | purpose | the plugin's override |
|---|---|---|
| `transform` | collect hyperfunc, `active` and marker names; emit helper functions for inactive executions | `transform` |
| loops over functions in `transform` and in the naming of heap-dependent functions | skip hyperfuncs, `active`, markers | the same, in `transform` |
| `translateMethod` | translate termination proofs of hyperfuncs unarily | `translateMethod` |
| about 230 new lines | hyperfunc definitions and calls, `rel`, `active`, lifting | `HyperfuncsTransformer` |
| `isDirectlyUnary` | hyperfunc calls and `active` are relational | `isDirectlyUnary` |
| `translateSIFExp1/2` | relational atoms are evaluated in each active execution, not only where both are active | `translateSIFExp1/2` |
| `translateNormal`, `translatePrime` | `rel`, `active` and hyperfunc calls | `translateNormal/Prime`, which replace the plugin's constructs with holes before the base translation (`withOwn`) |
| `translateToUnary`, `translatePredLowFuncBody` | relational atoms in unary contexts and in the low functions of predicates | the same two methods |

The copy contains no other fix to silver's transformer, so goal 2 is met once Gobra uses the subclass.

## Design

### A hyperfunc layer, parametric in the product construction

The layer is Viper to Viper. It depends only on silver's AST.

Each product construction provides the following primitives:

| primitive | lockstep (`on`) | activation-based (`extended`) |
|---|---|---|
| k | any k ≥ 2 | 2 |
| `inExecution(e, i)`: the unary expression `e` in execution i (copies of locals, fields, predicates and heap-dependent functions) | the copy with suffix `i`, minus atoms about MAJOR members in minor executions | `translateNormal` / `translatePrime` |
| `active(i)` in a method, at a program point | `true` | the activation condition at that point |
| `active(i)` in a hyperfunc | `true` | the flag parameter `aᵢ` |
| unfolding `P` around a body in execution i | `unfolding Pᵢ in` | conditional on the activation of execution i |
| queries | which executions a callee's contract relates (for pairwise reuse) | — |

The layer defines the following once, from those primitives (these are the paper's definitions):

| construct | definition | lockstep instance |
|---|---|---|
| `rel(e, i)` | `asserting active(i) in inExecution(e, i)` | `eᵢ` |
| `low(e)` | `∧_{i<j} (active(i) ∧ active(j) ⟹ eᵢ = eⱼ)` | `∧_{i<j} eᵢ = eⱼ` |
| `lowEvent` / `lowContext()` | `∧_{i<j} active(i) = active(j)` | `true` |
| a unary atom `b` | `∧ᵢ (active(i) ⟹ inExecution(b, i))` | `∧ᵢ bᵢ` |
| a rigid atom (`active(i)`, `rel` comparisons, hyperfunc calls) | evaluated once | the same |
| `&&`, `==>`, `?:`, `!`, `unfolding` | structural | the same |
| `forall`/`exists` over values | the bound variable is rigid (one variable for all executions) | the same |
| hyperfunc `H(x̄)` | `H(a₀…aₖ₋₁, ē, x̄₀…x̄ₖ₋₁)`: activation flags, `exec` parameters once, other parameters per execution | without flags: `H(ē, x̄₀…x̄ₖ₋₁)` |
| call `H(ā)` | the caller's activation, `exec` arguments once, `āᵢ` where execution i is active, a default value otherwise | `H(ē, ā₀…āₖ₋₁)` |
| termination of a hyperfunc | checked unarily, with relational values unconstrained | the same |

The derived lockstep instances must be simplified (`true ⟹ b`, `asserting true in e`), so that the generated Viper matches today's output.

### What stays with each product construction

- **Lockstep:**
  - low branch conditions;
  - k-ary method contracts;
  - reuse of contracts that relate only executions 0 and 1 on every pair (`pairwiseCallees`, `pairwiseWrapper`);
  - relational case splits;
  - unary copies of methods without relational specifications;
  - MAJOR members, see below.
- **Activation-based:**
  - silver's construction: control-flow variables, `goto`/`return` handling, `lowEvent`;
  - every activation is verified (`optimizeRestrictActVars = false`, jcp19/hyperfuncs#13).

### Pipeline

Both constructions run inside Viper as one plugin, after Viper's default plugins, in particular the termination plugin. Gobra emits the annotated program once and passes options to the plugin:

- `--plugin viper.hyperfuncs.HyperfuncsPlugin`;
- the construction to use (`lockstep` or `extended`) and k;
- `--hyperMode noMajor`.

Gobra then has no SIF transformer of its own. The marker functions that hide `low`, `rel` and `active` from the termination plugin belong to the hyperfunc layer.

### MAJOR

MAJOR is a property of the lockstep construction, not of hyperfuncs. A hyperfunc is never treated as MAJOR.

Today the transformer finds MAJOR members by searching Gobra's source text for `#[MAJOR]`. In the new design Gobra marks them with a Viper annotation instead, which the lockstep construction reads. Termination proofs, recognised by the name suffix `termination_proof`, stay "major".

Two local rules keep MAJOR out of the hyperfunc layer (see #1136):
- a hyperfunc's contract and body mention no MAJOR member;
- a MAJOR member contains no relational construct.

The second rule also fixes a crash: today, a hyperfunc call in a MAJOR member raises `Violation`.

### Adapting the plugin to Gobra's output

- Take hyperfunc names from the annotations in `beforeMethodFilter`. `beforeResolve` runs only when Viper parses a file, which never happens for Gobra's AST input.
- Accept Gobra's encoding of `active(i)` (an annotated function), or have Gobra emit the plugin's node.
- Use one annotation key for hyperfuncs. Today Gobra uses `hyperFunc` and the plugin uses `hf`.
- Provide a static configuration object, since Gobra runs ViperServer in the same process. It carries the goto low-event encoding, the callback that reports the product program (`SIFEncodedViperMessage`), `--enableExperimentalHyperFeatures`, `optimizeRestrictActVars` and the construction.
- Copy the `info` of the `rel` node into the generated `active(i)` assertions, so that Gobra can map the errors back to Go source.
- Skip the plugin's static checks (`HyperfuncsChecks`) for front ends that enforce the rules themselves, such as Gobra.

### Where the plugin's source lives

It must exist exactly once.

- **Prototype:** in Gobra, as a separate source tree with package `viper.hyperfuncs` and no Gobra dependencies. The hyperfuncs repository deletes its copy and runs its Viper tests with Gobra's jar, which contains Silicon and the plugin.
- **Eventually:** in silver, next to the SIF plugin. The subclass overrides internal methods of `SIFExtendedTransformer`, so it should live next to that class, and those methods should become documented extension points. This needs agreement from the Viper maintainers.

## Components

| component | kind | step | purpose |
|---|---|---|---|
| `hyperfuncs/` | sbt subproject of Gobra, depending only on Viper | 1 | the plugin's single source; the build enforces that it does not depend on Gobra |
| `HyperfuncsAnnotations` | object | 1 | the input convention for front ends: the annotation keys (hyperfunction, execution parameters, `active`) and `normalize`, which turns calls of the `active` function into the plugin's node. Gobra's `VprInfo` uses its constants |
| `HyperfuncsConfig` | object | 1 | configuration for front ends in the same process: static checks on or off, `rel` allowed, the SIF plugin's experimental features and goto encoding, the callback for the product program, and the info of generated `active(i)` assertions (`activeConditionInfo`) |
| `ProductConstruction` | trait | 2 | the primitives: `executions`, `inExecution(e, i)`, `unfoldingIn(u, i, body)`; the only interface between the shared layer and a construction |
| `HyperfuncEncoding` | class, parameterised by a `ProductConstruction` | 2 | the shared layer: hyperfunction definitions, calls, `rel`, `active`, `low`, lifting, termination proofs. The activation is a `Seq[Exp]` with one condition per execution |
| `Multiplicity` | object | 2 | the classification of unary and rigid expressions (`isRigid`, today in `HyperfuncsChecks`), shared by the checks and the encoding |
| `ExtendedProduct` | today's `HyperfuncsTransformer`: a subclass of silver's `SIFExtendedTransformer`, `with ProductConstruction` | 2 | the activation-based construction |
| `LockstepProduct` | today's `SIFLowGuardTransformer`, moved, `with ProductConstruction` | 3 | the lockstep construction, without its own hyperfunction translation |

## Steps

Each step ends with the acceptance tests passing:
- Gobra's `hyper_properties` tests;
- the hyperfuncs evaluation (`scripts/verify.sh`, 74 files);
- the plugin's tests (81).

1. **`extended` uses the plugin** (done).
   - Bring the plugin into Gobra and adapt it as described above.
   - Delete `GobraSIFExtendedTransformer`, `GobraSIFPlugin` and `HyperfuncMarkerTransformer`.
   - Two differences between Gobra's copy and the plugin must be covered by the tests or resolved:
     - the plugin inlines `let` with relational bodies;
     - the plugin chooses literal arguments for inactive executions where possible.
2. **The product interface.** Rewrite the plugin's hyperfunc layer against the primitives above. The extended construction is its first instance.
3. **Lockstep in the plugin.**
   - Move `SIFLowGuardTransformer` into the plugin as the second instance, and delete its hyperfunc translation.
   - Replace the `#[MAJOR]` source tag by an annotation, and add the two MAJOR rules.
   - Check that the termination behaviour of the lockstep encoding survives running after the termination plugin. This covers the lexicographic measures over k copies and methods with `decreases`.
4. **Upstream to silver** (with the Viper maintainers).

## Risks and open questions

- **Coupling to silver's internals.** Until step 4, a change to an internal method of silver's transformer can break the subclass silently. Gobra's tests catch this when the submodule is updated.
- **Termination in lockstep.** Today the termination plugin sees the lockstep product. After step 3 it sees the unary program. Termination proofs that depend on relational preconditions would then fail.
- **Generated Viper.** Deriving lockstep from the general definitions can change the generated programs, and with them the verification times. The comparison is the evaluation's timing data.
- **Error reporting.** Both transformers partly rely on Gobra's source info. The plugin must preserve `info` wherever it creates nodes from Gobra's nodes.
