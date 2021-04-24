# A Tutorial on Gobra
## Introduction
*Gobra* is an automated, modular verifier for heap-manipulating, concurrent Go programs. 
Gobra supports a large subset of Go, including Go’s interfaces and primitive data structures.
Gobra verifies memory safety, crash safety, data-race freedom, and partial 
correctness based on user-provided specifications.
Gobra takes as input a `.gobra` file containing a Go program annotated with 
specifications such as function pre- and postconditions and loop invariants.

In order to prove the correctness of a program, Gobra verifies
all functions and methods according to their specifications. If one of the
methods fails to meet its specification, the verification fails and Gobra 
reports the error.

This tutorial provides a practical introduction to Gobra and showcases some of its main features. 

TODO: OUTLINE: Brief introduction: Say what this section shows: “First, we will introduce Gobra’s top-level declarations. This tutorial will provide more details for their use in the subsequent sections”

## Structure of Gobra Programs
As is the case with Go, Gobra programs consist of a *package clause* followed
by a list of *imports declarations*, finally followed by a list of 
*top-level declarations*.
The following section lists the available Top-level declarations in Gobra.
TODO: we delegate the treatment of package ...

### Top Level Declarations
#### Functions and Methods
```go
/* Functions */
requires ... /* preconditions */
ensures ... /* postconditions */
func funcName(arg1 type1, ..., argN typeN) typeRet {
    ... /* function body */
}
```

```go
/* Methods */
requires ... /* preconditions */
ensures ... /* postconditions */
func (receiver type0) methodName(arg1 type1, ..., argN typeN) typeRet {
    ... /* method body */
}
```
- functions and methods can be annotated with pre- and postconditions. If ommited, they default to `true`
- function and method calls are verified modularly
    - the body is ignored at call site (changing the body does not affect client code)
    - the precondition is checked before the function call
    - the postcondition is assumed after the function call
- functions and methods may not have bodies, in which case they are not verified. Nonetheless, their specifications
are still used to verify calls to these methods.


#### Predicates
```go
pred predName(arg1 type1, ..., argN typeN) {
    ... // any assertion parameterized by `arg1`, ..., `argN`
}
```
#### Type Declarations
##### Alias Declarations 
```go
type TypeAlias = SomeType
``` 
- binds an identifier to a given type. Both identifiers correspond to the same type. 
- The identifier acts as an alias for the type. In this example, `TypeAlias` is an alias for 
`SomeType`. 

##### Type Definitions 
```go
type TypeDef SomeType
```
- create a new type with the same underlying type and operations as the provided type.
- The two identifiers correspond to different types. 
- In this example, `TypeDef` is a new type whose underlying type is `SomeType`. 


#### Global Constants
```go
const untypedConst = 0 // untyped integer constant
const typedConst1 type1 = 1 // typed integer constant
const (
    const 

)
```

#### Implementation Proofs

## Basic Annotations
We start with a simple function that computes the sum of the first `n` positive integers.
The following table shows how the program would be written in Go (on the left) and
how it would be written and specified in Gobra (on the right).

<table>
<tr>
<td> Go code </td> <td> Gobra code </td>
</tr>
<tr>
<td> 

```go
package tutorial

func sum(n int) (sum int) {
    sum := 0

    for i := 0; i <= n; i++ {
            sum += i
    }
    return sum
}
```

</td>
<td>

```go
package tutorial

requires 0 <= n // precondition
ensures sum == n * (n+1)/2 // postcondition
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

</td>
</tr>
</table>

Besides the code present in the Go code, the Gobra version contains a **function specification**
in the form of pre- and postconditions:
- **preconditions** (assertion following the `requires` keyword): restricts the argument `n`
to non-negative values. Thus, `sum` can only be assumed to behave correctly for non-negative 
values. In general, preconditions are assertions parameterized by the function parameters
that must hold when a function is called.

- **postconditions** (assertion following the `ensures` keyword): defines the contents of the
result variable `res` when the program terminates. In general, postconditions are properties
that must hold when executions of the function starting from a state satisfying the 
precondition terminate. They are parameterized by the function parameters and the result 
variables.

Verification of loops also requires specification in the form of **loop invariants**,
following the `invariant` keyword.
The loop invariant in `sum` could also be written in one line with the operator 
`&&` placed between the two assertions.

If omitted, preconditions, postconditions, and loop invariants default to `true`,
which is typically not enough to prove interesting properties of the program such as memory
safety and functional correctness.

TODO: client

## Permissions and Heap-Manipulation
Gobra uses a variant of *separation logic* to support reasoning about the mutable heap data. 
In this model, each heap location is associated with an *access permission*.
Access permissions are held by method executions and transferred
between functions upon call and return.
A method may access a location only if it holds the associated permission.
Permission to a shared location `v` is denoted in Gobra by the accessbility predicate `acc(v)`.

Gobra's permission model is expressive enough to allow concurrent read accesses while still
ensuring exclusive writes. Furthermore, the concept of permissons extends to
(recursive) predicates to denote access to unbounded data structures.

This section demonstrates how the permissions and the access predicate are used with
memory locations (through pointers) and predicates. Furthermore, it also describes
how to allow multiple ... // TODO: fractional permissions

### Accessability Predicate
In pre- and postconditions, and generally in assertions, permission to a location `l`
is denoted by an accessibility predicate: an assertion which is written `acc(l)`.
Besides, `acc(l)` implies that l is not `nil`.

An accessibility predicate in a function’s precondition can be interpreted as
an obligation for a caller to transfer the permission to the callee,
thereby giving it up. Conversely, an accessibility predicate in a 
postcondition can be understood as a permission transfer
in the opposite direction: from callee to calleer.

The following code snippet shows the declaration of a new type `pair`, containing
two integer fields, and a method `incr` that increments both fields of a `pair` by `n`.
```go
package tutorial

type pair struct {
  left, right int
}

// precondition
requires acc(&x.left) && acc(&x.right) 
// postcondition 1
ensures acc(&x.left) && acc(&x.right)
// postcondition 2
ensures x.left == old(x.left) + n
// postcondition 3
ensures x.right == old(x.right) + n
func (x *pair) incr(n int) {
  x.left += n
  x.left += n
}
```
Because `incr` changes the values of `x.left` and `x.right`, the function must state in
its precondition that it requires permissions to both of these fields. After the execution of
`incr`, the permissions to both fields are given back to the caller (*postcondition 1*).
Furthermore, *postcondition 2* and *postcondition 3* ensure that the fields `x.left` and `x.right`
are both incremented by `n`, respectively. Both postconditions make use of the `old` operator whose value
is the value of its operand in the beggining of the method execution.

As a short-hand,
one could have written `requires acc(x)` to require access to all fields of `x`.

Given the implementation and specification of `incr`, one can call it from other functions and methods.
For example, `client1` allocates a new `pair`. When a function allocates a new value on the heap, it also aquires
permissions to all of its fields. It then passes the location `p` of the newly created pair to method `incr`, which
intuitively  
1. obtains the permissions to `p.left` and `p.right`
2. modifies its values according to the specifcation of `incr`
3. returns the permissions of `p.left` and `p.right` back to `client1`

```go
package tutorial

func client1() {
  p := &pair{1,2}
  x.incr(42)
  assert x.left == 43
}
```
The assertion at the end of `client1` checks that `x.left == 43` at the end of its execution. If this was not
the case, verification would fail.

Similar to `client1`, `client2` also allocates a `pair`. However, it stores the new `pair` in a variable `x`
instead of storing a pointer to it, like in `client1`. Because we obtain its location in the call to `incr`,
the variable `x` must be followed by a `@` in its declaration. In general, every variable whose address is obtained
using `&` needs to be annotated with `@` at declaration site. 
```go
package tutorial

func client2() {
  x@ := pair{1,2} // if the reference of an address is taken, then add @
  (&x).incr(42)
  assert x.left == 43
}
```

### Fractional Permissions
### Quantified Permissions
```go
requires forall k int :: 0 <= k < len(s) ==> acc(&s[k])
ensures forall k int :: 0 <= k < len(s) ==> acc(&s[k])
ensures forall k int :: 0 <= k < len(s) ==> s[k] == old(s[k]) + n
func incr(s []int, n int) {
    invariant 0 <= i <= len(s)
    invariant forall k int :: 0 <= k < len(s) ==> acc(&s[k])
    invariant forall k int :: i <= k < len(s) ==> s[k] == old(s[k])
    invariant forall k int :: 0 <= k < i ==> s[k] == old(s[k]) + n
    for i := 0; i < len(s); i += 1 {
        s[i] = s[i] + n
    }
}
```

[also show client that allocates slice]


## Predicates
In general, listing the heap locations modified by a function via access predicates with pointers can
only be done with structures of bounded size. 

listing the access permissions to locations is always bounded. That limitation is overcome
TODO:
by using predicates
- predicates
- predicates don't mean the same thing as their body, they require an explicit unfolding
- fold
- unfold
- unfolding


```go
package preliminaries

type node struct {
  value int
  next *node
}

pred listPerm(ptr *node) {
  ptr != nil ⇒ acc(ptr) && listPerm(ptr.next)
}

requires list(ptr) && isComparable(value)
ensures list(ptr)
ensures idx >= 0
ensures contains(ptr, value)
func insert(ptr *node, value interface{}) (ghost idx int) {
 unfold list(ptr)
 if (ptr.next == nil) {
   newNode := &node{value: value}
   fold list(newNode)
   ptr.next = newNode
   idx = 1
 } else {
   idx = insert(ptr.next, value) + 1
 }
 fold list(ptr)
}
```




## Interfaces
stream example from the paper
```go
type stream interface{
    pred memory ()
    requires acc(memory(), _) // arbitrary fraction of memory(x)
    pure hasNext () bool
    requires memory () && hasNext () ensures memory()
    next() interface{}
}

type counter struct { 
    f, max int
}

requires acc(&x.f, _) && acc(&x.max, _)
pure func (x *counter) hasNext() bool {
    return x.f < x.max
}

requires acc(&x.f) && acc(&x.max, 1/2) && x.hasNext()
ensures acc(&x.f) && acc(&x.max, 1/2) && x.f == old(x.f)+1
ensures typeOf(y) == int && y.(int) == old(x.f)
func (x *counter) next() (y interface{}) {
    x.f++;return x.f-1
}

pred (x *counter) memory() {
    acc(&x.f) && acc(&x.max)
}

(*counter) implements stream {
    pure (x *counter) hasNextProof() bool {
        return unfolding acc(x.memory(), _) in x.hasNext()
    }

    (x *counter) nextProof() (res interface{}) {
        ... // TODO: put full proof here
    }
}
```

Comparability
look at the list example with value as an interface

## Concurrency
Goroutine
First-class Predicates
Lock example
Channel

## More examples ?

-----------------

## Not integrated in main text
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


- Additionaly, Gobra introduces mathematical data types, which are 
useful for specification:
    | Description      | Type Identifiers |
    | ----------- | ----------- |
    | sequence types | `seq[T]`, for some `T` |
    | set types | `set[T]`, for some `T` |
    | multi-set types | `mset[T]`, for some `T` |
    | domain types | TODO |

## Running Gobra

## other things to talk about
- verification is method modular
- Ghost Code
- after top-level declarations: statements, assertions(maybe explain permissions here), expressions