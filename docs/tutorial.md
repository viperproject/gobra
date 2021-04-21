# A Tutorial on Gobra
## Introduction
*Gobra* is an automated, modular verifier for heap-manipulating, concurrent Go programs. Gobra supports a large subset of Go, including Go’s interfaces and primitive data structures.

Gobra verifies memory safety, crash safety, data-race freedom, and user-provided specifications. It takes as input a Go program annotated with assertions such as pre- and postconditions and loop invariants.

This tutorial presents Gobra's features and how they can be used ...

Talk about the extension `.gobra`

## Overview
A Gobra program consists of a list of imports followed by a list of member declarations. A member can be one of the following:
1. Functions and Methods
2. User-defined type
3. Proof of implementation
4. Global const
5. Global var (? not supported atm)

The following section describes each kind of member in detail.
### Top-Level Declarations
#### Functions and Methods 
include specifications

#### Type Definitions
- in the interface section, mention interface proofs
### Built-in types

### Imports and Support for the Std Lib

## Examples
### 1. ???
```go
package preliminaries

requires 0 <= n
ensures sum == n * (n+1)/2
func sum(n int) (sum int) {
    sum := 0
    invariant 0 <= i && i <= n + 1
    invariant sum == i * (i-1)/2
    for i := 0; i <= n; i++ {
            sum += i
    }
    return sum
}
```

### 2. ???

### 3. Concurrency

## Language Summary
### Members
### Expressions
(ghost and non-ghost)
### Statements










Keep structure of viper tutorial

things to talk about
- Annotations, including acc(), predicates, pre-conditions, post-conditions, invariants
- Support for Go's default types: slices, channels
- Ghost types
- concurrency features, standard library, first-order predicates
- Data structures, interfaces, Domains (maybe can be skipped)
- control flow structures
- functions, methods, pure functions, method expressions, predicate expressions
