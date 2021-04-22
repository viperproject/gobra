# A Tutorial on Gobra
## Introduction
*Gobra* is an automated, modular verifier for heap-manipulating, concurrent Go programs. Gobra supports a large subset of Go, including Go’s interfaces and primitive data structures.
Gobra verifies memory safety, crash safety, data-race freedom, and user-provided specifications.
It takes as input a `.gobra` file containing a Go program annotated with 
specifications such as function pre- and postconditions and loop invariants.

In order to prove the correctness of a program, Gobra verifies
all functions and methods according to their specifications. If one of the
methods fails to meet its specification, the verification fails and Gobra 
reports the error.

This tutorial provides a practical introduction to Gobra and showcases some of its main features. TODO: OUTLINE

## Structure of Gobra Programs
As is the case with Go, Gobra programs consist of a *package clause* followed
by a list of *imports declarations*, finally followed by a list of 
*top-level declarations*.
The following sections describe in detail each component of a Gobra program.

## Language Overview
### Package Clauses
```go
package pkgName
```
- The `package` keyword defines the package to which the file belongs.
- A package clause begins must appear at the beginning of a source file.
- Every Gobra file belongs necessarily to one and only one package.

### Import Declarations
```go
import (
    "pkg1"
    "pkg2"
    ...
    "pkgN"
)
```
- Import Declarations begin with the `import` keyword and state that the enclosing Gobra file depends on the imported packages
- The imported packages are specified by name if they belong to the standard
library, or by path. In the second case, the path must be relative to the
`GOPATH` environment variable.
- Gobra provides an incomplete but growing support for the Go standard library. Currently, it has partial support for the packages 
`encoding/binary`, `net`, `strconv`, `strings`, `sync`, and `time`.
### Top-level declarations
#### Functions and Methods
```go
/* Functions */
requires ... /* preconditions */
ensures ... /* postconditions */
func funcName(arg1 type1, ..., argN typeN) typeRet {
    ... /* function body */
}

/* Methods */
requires ... /* preconditions */
ensures ... /* postconditions */
func (receiver type0) methodName(arg1 type1, ..., argN typeN) typeRet {
    ... /* method body */
}
```
- Gobra supports Go's syntax for functions and methods.
In the following observations, we will refer only to functions, 
but they generalize to methods as well.
- The specification for a function is provided as a list of pre- and 
postcontions.
    - Preconditions consist of the conditions that must hold when
    the method is called. Preconditions are specified using 
    the `requires` keyword, followed by an [assertion](#assertions)
    parameterized by the function arguments.
    - Postconditions consist of the conditions that must hold when the
    function terminates, assuming that it started in a state satisfying
    the preconditions. Postconditions are specified using 
    the `ensures` keyword, followed by an assertion parameterized by
    the function arguments and the return value.
    - If no pre- or postconditions are provided, they default to `true`.
- Function arguments are specified as pairs of identifiers and types. Gobra
also supports variadic parameters. Additionally, methods expect a special 
argument - the *receiver* - which appears before the method name in the 
method declaration.
- Functions and function calls are verified modularly: 
    - function calls assume the specification of the called function to be 
    true. Thus, changing its body does not affect the verification from the
    callers perspective, as long as the specification remains the same.
    - The precondition of the callee is checked before a function call 
    - The postcondition of the callee is assumed after the method call
- Gobra allows functions without a body, i.e., *abstract functions*. 
For example, the following abstract function computes the sum of the
first `n` naturals, according to its specification:
```go
requires n >= 0
ensures res == n * (n+1)/2
func sumToN(n int) (res int)
```
- When present, the body of a function consists of a block of [statements](#statements).

TODO: mention ghost and pure functions, ghost arguments

#### Type Declarations
- Currently, Gobra supports the following types from Go:
    | Description      | Type Identifiers |
    | ----------- | ----------- |
    | integer types| `int8`, `int16`, `int32`, `int64`, `int`, `uint8`, `uint16`, `uint32`, `uint64`, `uint`, `uintptr`, `byte`, `rune`|
    | boolean type | `bool` |
    | string type | `string` |
    | array types | `[n]T`, for some natural number `n` and some type `T` |
    | slice types | `[]T`, for some type `T` |
    | bidirectional channel types | `chan T`, for some type `T` |
    | send-only channel types | `chan<- T`, for some type `T` |
    | receive-only channel types | `<-chan T`, for some type `T` |
    | pointer types | `*T`, for some type `T` |
    | struct types | `struct { field1 T1, ..., fieldN TN}`, where `field1` .. `fieldN` correspond to identifiers and `T1`..`TN` are types |
    | interface types | `interface { m1, ..., mN}`, where `m1`..`mN` are method specifications|

- Additionaly, Gobra introduces mathematical data types, which are 
useful for specification:
    | Description      | Type Identifiers |
    | ----------- | ----------- |
    | sequence types | `seq[T]`, for some `T` |
    | set types | `set[T]`, for some `T` |
    | multi-set types | `mset[T]`, for some `T` |
- Furthermore, the user can define its own types using the `type` keyword
  (TODO: maybe pass this one as a first)

#### Predicates

#### Global Constants


Gobra allows additional kinds of functions and methods besides the ones in Gobra. We now present such variations for functions. All considerations also apply to methods.

(TODO: mention that methods consist of statements and specs consist of assertions and refer to the corresponding sections)
##### Pure Functions

##### Ghost Functions
Gobra supports *ghost code*, i.e., code introduced only for the purposes of
verification. Ghost code cannot influence the result of non-ghost code, which we call *actual code*.
```go
/* Ghost Functions */
ghost /* ghost modifier */
requires ... 
ensures ... 
func funcName(arg1 type1, ..., argN typeN) {
    ...
}
```
Ghost arguments in all kinds of functions.


Besides pre and postconditions, functions and methods can be qualified with
the `ghost` and `pure` modifiers



--------------------------------------

## Not integrated in main text
### Member Declarations
A member can be one of the following:
1. Functions and Methods
2. User-defined types (TODO: mention in this section the already supported types)
2. Predicates
4. Global constants
5. Global variables (not supported yet)
3. Implementation Proofs
This section presents each component of Gobra programs in order.
The following section describes each kind of member in detail.

TODO: after top-level declarations: statements, assertions(maybe explain permissions here), expressions

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

## Running Gobra

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



 - all signed and unsigned integer types: `int8`, `int16`, `int32`, 
    `int64`, `int`, `uint8`, `uint16`, `uint32`, 
    `uint64`, `uint`, `uintptr`, `byte`, `rune`
    - boolean: `bool`
    - arrays: `[n]T` for some non-negative length `n` and some type `T`
    - slices: `[]T` for some type `T`
    - strings: `string`
    - channels: 
    - pointers
    - structs
    - interfaces