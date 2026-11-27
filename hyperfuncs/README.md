# Hyperfunctions for Viper

A [Viper](https://www.pm.inf.ethz.ch/research/viper.html) plugin that adds **hyperfunctions** to Viper's
information-flow plugin (`viper.silver.plugin.sif`, the "extended" product encoding of Eilers, Müller and Hitz,
TOPLAS'19). It implements the assertion language of the hyperfuncs paper (jcp19/hyperfuncs, Sections 3 and
"Secret Control Flow") and Gobra's static restrictions. Gobra uses it for `--hyperMode extended`: Gobra emits Viper
code in the form described in [HyperfuncsAnnotations.scala](src/main/scala/viper/hyperfuncs/HyperfuncsAnnotations.scala),
and the plugin builds the product program. Gobra's `--hyperMode on` uses the plugin's lockstep construction. See [../docs/design/hyperfuncs-plugin.md](../docs/design/hyperfuncs-plugin.md)
for the plan to implement both of Gobra's hyper modes with it.

```viper
hyperfunction NonInterf(input: Int, output: Int): Bool
{ low(input) ==> low(output) }

method Abs(x: Int) returns (res: Int)
  ensures NonInterf(x, res)
{
  res := x
  if (x < 0) { res := -x }   // a branch on a secret
}
```

The library of security definitions and Viper ports of the whole evaluation of the hyperfuncs repository (`viper/`
there) are verified with the plugin (`scripts/verify-viper.sh` there).

## Language

| construct | meaning |
|---|---|
| `hyperfunction H(x: T, ...): R requires P ensures Q { E }` | a function over the two executions: parameters are unary (one value per execution), the value is rigid (one value for both) |
| `rel(e, i)` | the value of the unary expression `e` in execution `i` (`0`, `1`, or of type `Exec`); defined only if `active(i)` |
| `low(e)` | `e` agrees in the active executions: `forall i, j: Exec :: active(i) && active(j) ==> rel(e, i) == rel(e, j)` (from the SIF plugin) |
| `active(i)` | execution `i` is active at the current program point |
| `lowEvent` | both executions reach this point, or none does (from the SIF plugin; Gobra's `lowContext()`) |
| `forall i: Exec :: E`, `exists i: Exec :: E` | quantifiers over the executions (unrolled) |
| `Exec` | the type of execution identifiers, for parameters of hyperfunctions (`Agree(x: Int, i: Exec, j: Exec)`) and quantified variables; literals `0` and `1` are accepted as arguments; supports only `==` and `!=` |

Unary boolean expressions in assertion positions are *lifted*: they must hold in every active execution. Hyperfunctions
may be abstract (no body), recursive (with `decreases`), read the heap (with permissions in their preconditions, in
each execution), have postconditions about `result`, and call other hyperfunctions.

### Semantics with secret control flow

Viper's encoding allows branches and loops on secrets, so an execution may be *inactive* at a program point. As in
Gobra's PR #1130:

- `active(i)` is the product's activation condition of execution `i` (including executions that left a loop).
- `rel(e, i)` is **partial**: it is defined only if `active(i)` holds; errors read "Assertion active(i) might not hold".
- **Activity preconditions are explicit.** A hyperfunction that reads an execution, with `rel` or through a call of a
  partial hyperfunction, must require that the execution is active, e.g.

  ```viper
  hyperfunction Dist(x: Int): Int
    requires AllActive()
  { rel(x, 0) >= rel(x, 1) ? rel(x, 0) - rel(x, 1) : rel(x, 1) - rel(x, 0) }

  hyperfunction Agree(x: Int, i: Exec, j: Exec): Bool
    requires active(i) && active(j)       // precise: Agree(x, 1, 1) needs only execution 1
  { rel(x, i) == rel(x, j) }
  ```

  Without it, Viper's well-definedness check rejects the definition ("Function might not be well-formed. Assertion
  active(1) might not hold."). Guarded reads need no precondition: `active(1) ==> rel(x, 1) > 0` is total. The
  activity precondition must come before the preconditions that use `rel`, since preconditions are checked in order.
  Gobra behaves the same (viperproject/gobra#1130).
- `low` relates the active executions, so it is total: `NonInterf` and `DelimitedRelease` are total, `Dist`, `Le` and
  `Agree` are partial.
- There are **no implicit guards**: a method is verified for every combination of activation flags, so a contract
  that uses a partial hyperfunction either requires `AllActive()` (library: `forall i: Exec :: active(i)`) or guards
  the relational part (`AllActive() ==> Dist(x) <= 1`).
- Relational atoms are evaluated in each active execution. (Viper's SIF plugin evaluates them only where both
  executions are active, so there `!low(x)` is vacuously true where one execution is inactive; here it is false.)
- In hyperfunction contracts and bodies, only *unary* boolean atoms are lifted (they must hold in each active
  execution); *rigid* atoms such as `active(1)`, `rel(x, 0) <= rel(x, 1)`, or calls of hyperfunctions have one value
  for both executions and are evaluated once. So `requires AllActive()` means that both executions are active, also
  in the case where neither is.

### Static rules (as in Gobra)

Checked after type checking; violations are reported as consistency errors:

- Relational constructs (`low`, `rel`, `active`, `lowEvent`, quantifiers over `Exec`, and calls of hyperfunctions)
  appear only in hyperfunctions, method pre- and postconditions, loop invariants, `assert`/`assume`/`inhale`/`exhale`,
  and predicate bodies. **Ordinary functions do not call hyperfunctions** or use relational constructs; nor do domain
  axioms, assignments, method calls, `fold`/`unfold`, or the conditions of `if` and `while`.
- The argument of `low` and `rel` is unary (no nesting).
- `Exec` is allowed only for hyperfunction parameters and quantified variables (not for method parameters, locals,
  fields, function parameters, or results); executions passed to `rel`, `active` and `Exec` parameters are literals
  or variables of type `Exec`; literals are 0 or 1 (type error otherwise).
- The body of a hyperfunction with a non-boolean result must be rigid (one value for both executions); a unary value
  must be projected with `rel(e, i)`, also the value of a parameter with the precondition `low(n)` (`rel(n, 0)`).
  Only `Exec` parameters are rigid. Boolean bodies are lifted.
- `lowEvent` is not allowed in hyperfunctions; relational constructs are not valid triggers.

## Implementation

The plugin is built on top of the SIF plugin; it reuses its AST nodes (`SIFLowExp`, `SIFRelExp`, `SIFLowEventExp`) and
its product construction (`SIFExtendedTransformer`), and overrides only what the semantics of hyperfunctions changes.

| file | what |
|---|---|
| [HyperfuncsPlugin.scala](src/main/scala/viper/hyperfuncs/HyperfuncsPlugin.scala) | the plugin: syntax (`rel` with an `Exec` index, `active`, `low`, `lowEvent`, the type `Exec`), hyperfunction declarations (`hyperfunction` is rewritten to an ordinary function with the annotation `@hf()`, so that Viper's own name analysis, type checking and termination checks apply), and the pipeline |
| [HyperfuncsChecks.scala](src/main/scala/viper/hyperfuncs/HyperfuncsChecks.scala) | the static rules, including the multiplicity check of non-boolean bodies |
| [HyperfuncsPreprocessing.scala](src/main/scala/viper/hyperfuncs/HyperfuncsPreprocessing.scala) | unrolls quantifiers over executions, lowers `Exec` to integers (`rel(e, i)` with a parameter `i` becomes `i == 0 ? rel(e, 0) : rel(e, 1)`), and replaces `low`/`rel`/`active` in hyperfunctions by marker functions while Viper's termination plugin runs |
| [HyperfuncEncoding.scala](src/main/scala/viper/hyperfuncs/HyperfuncEncoding.scala) | the encoding of hyperfunctions for k executions, independent of the product construction: a hyperfunction `H(x̄)` becomes `H(a₀ … aₖ₋₁, ē, x̄₀ … x̄ₖ₋₁)` over the activation flags, the execution parameters, and each execution's arguments (and heaps); calls pass the caller's activation; `rel(e, i)` becomes `asserting active(i) in e_i`; lifting of unary atoms; termination proofs of hyperfunctions stay unary, with relational values replaced by unconstrained ones |
| [ProductConstruction.scala](src/main/scala/viper/hyperfuncs/ProductConstruction.scala) | the primitives a product construction provides to the encoding (`inExecution`, `lowComparison`, parameter copies, …) |
| [ExtendedProduct.scala](src/main/scala/viper/hyperfuncs/ExtendedProduct.scala) | the activation-based product construction: extends silver's `SIFExtendedTransformer` and implements `ProductConstruction` (k = 2; the activation of a method is the SIF plugin's activation condition) |
| [lockstep/LockstepProduct.scala](src/main/scala/viper/hyperfuncs/lockstep/LockstepProduct.scala) | the lockstep product construction (Gobra's `--hyperMode on`, k executions, low-guard checks), with `HyperfuncsConfig.construction = Lockstep(k)`; implements `ProductConstruction` with a trivial activation |
| [Multiplicity.scala](src/main/scala/viper/hyperfuncs/Multiplicity.scala) | which expressions are rigid (one value for all executions) and which are unary |
| [HyperfuncsAst.scala](src/main/scala/viper/hyperfuncs/HyperfuncsAst.scala), [HyperfuncsPAst.scala](src/main/scala/viper/hyperfuncs/HyperfuncsPAst.scala) | AST nodes for `active`, `rel` with an `Exec` index, and execution literals |
| [HyperfuncsAnnotations.scala](src/main/scala/viper/hyperfuncs/HyperfuncsAnnotations.scala) | the input convention for front ends that produce Viper ASTs: annotation keys, and the encoding of `active(i)` as an annotated function |
| [HyperfuncsConfig.scala](src/main/scala/viper/hyperfuncs/HyperfuncsConfig.scala) | configuration for front ends that run Viper in the same process (Gobra): static checks, `rel`, the SIF plugin's options, the callback for the product program, and the info of generated `active(i)` checks |

The plugin replaces Gobra's former copy of the SIF plugin (`GobraSIFExtendedTransformer`): it implements the same
semantics as an extension of silver's transformer rather than a modified copy. `let`-expressions with relational bodies
are inlined (so that `let y == x in rel(y, 1)` is the value of `x` in execution 1, in every context), and placeholder
arguments of inactive executions are literals where possible.

**Limitations.** Two executions (Viper's encoding relates two; Gobra's lockstep encoding supports k). Hyperfunctions
are not generic: Gobra's `any` parameters need one hyperfunction per type (`NonInterf`, `NonInterfSeq`, ...). A
hyperfunction cannot read the state of an execution
that waits at another program point.

## Building and testing

The plugin is the sbt subproject `hyperfuncs` of Gobra. It depends only on Viper (silver and, for its tests, Silicon,
through Gobra's `viperserver` submodule), not on Gobra.

```bash
export Z3_EXE=/path/to/z3                    # from Gobra's root directory
sbt hyperfuncs/test                          # the plugin's regression tests
sbt assembly                                 # target/scala-2.13/gobra.jar contains Silicon and the plugin
java -Xss128m -cp target/scala-2.13/gobra.jar viper.silicon.SiliconRunner --z3Exe "$Z3_EXE" \
  --plugin viper.hyperfuncs.HyperfuncsPlugin file.vpr
```

Expected errors are annotated in the test files with `//:: ExpectedOutput(error.id:reason.id)` on the line before
(Viper's annotation-based test format). The tests:

| directory | what |
|---|---|
| [src/test/resources/hyperfuncs](src/test/resources/hyperfuncs) | `activation01.vpr` (methods are verified for every activation: regression test for jcp19/hyperfuncs#13), `extended01.vpr` (port of Gobra's `hyperfuncs/extended01.gobra` from PR #1130, with explicit activity preconditions), `features01.vpr` (execution quantifiers and parameters, guards, explicit activity preconditions (missing ones are rejected, also for reads through other hyperfunctions), total `low`, `let`, relational predicates calling hyperfunctions, loops on secrets, `lowEvent`, termination and postconditions of hyperfunctions), `restrictions01-03.vpr` (the static rules and type errors) |
