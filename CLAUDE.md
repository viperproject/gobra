# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What is Gobra

Gobra is a prototype verifier for Go programs built on the [Viper verification infrastructure](http://www.pm.inf.ethz.ch/research/viper.html). It takes annotated Go programs (`.gobra` files) and formally verifies them using symbolic execution (Silicon) or verification condition generation (Carbon) as backends, with Z3 as the SMT solver.

## Prerequisites

- **Java 64-bit** (11+)
- **SBT** (1.4.4+)
- **Z3 executable** (4.8.7 64-bit recommended) — set `Z3_EXE=/path/to/z3`
- **Git submodules** initialized: `git submodule update --init --recursive`
- Optional (Carbon backend): **Boogie** — set `BOOGIE_EXE=/path/to/Boogie`

## Common Commands

```bash
# Build
sbt compile
sbt assembly                  # Produces target/scala-2.13/gobra.jar

# Run tests
sbt test                      # All tests

# Run a specific test class or pattern (in sbt shell)
sbt
> test-only viper.gobra.GobraTests -- -z "pattern"

# Run Gobra directly
sbt "run -i src/test/resources/regressions/examples/swap.gobra"
java -jar -Xss128m target/scala-2.13/gobra.jar -i path/to/file.gobra

# License header check
npx github:viperproject/check-license-header#v1 check --config .github/license-check/config.json --strict
```

## Verification Pipeline

Gobra's verification flow:

1. **Parser** (`frontend/Parser.scala`) — ANTLR4 grammar (`src/main/antlr4/`) + Kiama, parses `.gobra` annotated Go files
2. **Type Checker / Info** (`frontend/info/`) — semantic analysis, type resolution, scope management
3. **Desugaring** (`frontend/Desugar.scala`) — lowers frontend AST to internal AST (`ast/internal/`)
4. **Internal Transforms** (`ast/internal/transform/`) — constant propagation, overflow checks, termination, CG edges
5. **Translator** (`translator/`) — translates internal AST to Viper Silver IR via encoding modules
6. **Backend** (`backend/`) — sends Silver program to ViperServer (Silicon or Carbon)
7. **Reporter** (`reporting/`) — maps Viper errors back to Go source locations and formats output

## Key Architecture Points

**Encoding modules** (`translator/encodings/`): each Go concept (maps, slices, interfaces, ADTs, channels, permissions) has its own encoding module. `MainTranslator` coordinates them via `Context`.

**ViperServer** (`viperserver/` submodule): manages verification jobs and communicates with Silicon/Carbon backends. Gobra wraps it via `backend/ViperBackends.scala`.

**Package resolution** (`frontend/PackageResolver.scala`): Gobra can verify multi-package programs. `Info` handles cross-package type information.

**Built-in stubs** (`src/main/resources/`): Go standard library stubs and built-in definitions provided as pre-verified packages.

**Test infrastructure**: Regression tests are `.gobra` files under `src/test/resources/regressions/` annotated with `//@ expectedError` etc. The test runner (`GobraTests.scala`) discovers and runs them via the Silver testing framework.

## Submodule Structure

```
viperserver/        ← HTTP server + job management
  silicon/          ← Symbolic execution backend
  carbon/           ← VCG backend (requires Boogie/Mono)
    silver/         ← Shared Viper IR (Silver)
```

## Parser Regeneration

The ANTLR4 parser is auto-generated before compilation via `genparser.sh`. Generated files land in `.genparser/`. Usually handled automatically by SBT; run `genparser.sh --download` manually if needed.
