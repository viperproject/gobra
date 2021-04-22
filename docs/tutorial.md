# A Tutorial on Gobra
## Introduction
*Gobra* is an automated, modular verifier for heap-manipulating, concurrent Go programs. Gobra supports a large subset of Go, including Go’s interfaces and primitive data structures.
Gobra verifies memory safety, crash safety, data-race freedom, and user-provided specifications.
It takes as input a `.gobra` file containing a Go program annotated with assertions such as pre- and postconditions and loop invariants.

This tutorial provides a practical introduction to Gobra and showcases some of its main features.

## Language Overview
As is the case with Go, Gobra program consists of a *package clause* followed
by a list of *imports declarations* and followed by a list of *member 
declarations*. A member can be one of the following:
1. Functions and Methods
2. Predicates
2. User-defined types
3. Implementation Proofs
4. Global constants
5. Global variables (not supported yet)

The following section describes each kind of member in detail.
### Member Declarations
#### 1. Functions and Methods
Gobra supports Go's syntax for methods and functions. Furthermore, it allows
its users to provide specifications for functions and methods using pre- and postconditions in the following format:
```go
/* Functions */
requires ... /* preconditions */
ensures ... /* postconditions */
func funcName(arg1 type1, ..., argN typeN) {
    ... /* function body */
}

/* Methods */
requires ... /* preconditions */
ensures ... /* postconditions */
func (t T) methodName(arg1 type1, ..., argN typeN) {
    ... /* method body */
}
```
In the snippet before, the variables `argX`, with X ranging from 1 to N, are variable identifiers and `typeX` ... corresponds to names of types. The last argument in the list can also be of variadic type.

Explain pre and postconditions

Besides pre and postconditions, functions and methods can be qualified with
the `ghost` and `pure` modifiers


- Ghost
- Ghost arguments
- Pure (mention thesis to extend syntax of ...)
- Spec
- Abstract


include specifications

### Built-in types
## Type Definitions
- in the interface section, mention interface proofs

### Imports and Support for the Std Lib

### Statements

### Expressions
#### Assertions

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


## Other Features
### Go-ifying Printer
### Overflow Checker








Keep structure of viper tutorial

things to talk about
- Annotations, including acc(), predicates, pre-conditions, post-conditions, invariants
- Support for Go's default types: slices, channels
- Ghost types
- concurrency features, standard library, first-order predicates
- Data structures, interfaces, Domains (maybe can be skipped)
- control flow structures
- functions, methods, pure functions, method expressions, predicate expressions
