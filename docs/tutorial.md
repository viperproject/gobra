# A Tutorial on Gobra
## Introduction
*Gobra* is an automated, modular verifier for heap-manipulating, concurrent Go programs. 
It supports a large subset of Go, including Go’s interfaces and primitive data structures.
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
In the following sections, we briefly describe each of these components.

### Package Clauses

```go
package pkgName
```

- The `package` keyword defines the package to which the file belongs.
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
- Typically used to abstract over assertions and to specify the shape of recursive data structures
- See the [section on predicates](#predicates) for details

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
const untypedConst = constExpr // untyped constant
const typedConst type = consExpr // constExpr must be known at compile-time
```

#### Implementation Proofs

```go
(t Type) implements InterfaceType {
	...
}
```

- Explained in [the section on interfaces](#interfaces)

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



When a function or method does not have side effects such as changing the contents of heap-allocated structures, it may be annotated with the `pure` modifier. 

```go
package tutorial

// Computes the square of the binomium a + b
ensures res == (a+b) * (a+b)
pure func squareBin(a, b int) (res int) {
    return a * a + 2 * a * b + b * b
}
```

Pure functions may be used in the specifications of functions and methods. This is not true for non-`pure` functions. Currently, the implementation of Gobra limits `pure` functions to functions that return a side-effect-free expression.



TODO: exemplify occurrence in specification

## Permissions and Heap-Manipulation
Gobra uses a variant of *separation logic* to support reasoning about the mutable heap data. 
In this model, each heap location is associated with an *access permission*.
Access permissions are held by method executions and transferred
between functions upon call and return.
A method may access a location only if it holds the associated permission.
Permission to a shared location `v` is denoted in Gobra by the accessbility predicate, discussed in the next section. 

### Accessability Predicate
In pre- and postconditions, and generally in assertions, permission to a location `l`
is denoted by an accessibility predicate: an assertion which is written `acc(l, p)`.

In general, `p` is a rational number between 0 and 1.  If the value of `p` is 1, then the predicate can be written as `acc(l)` and represents the permission to read and modify the value of the memory location. If the value of `p` is more than 0, it represents the permission to read the memory location `l` but no permission to modify. A non-zero permission to `l` may be split using the `*` operator and the following equality

```
acc(l, p1) * acc(l, p2) == acc(l, p1 + p2), if p1 >= 0 and p2 >= 0
```

`acc(l, p)` implies that `l` is not `nil` when `p > 0`.

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
// the field requires permission to modify both fields
requires acc(&x.left) && acc(&x.right)
// postcondition 1
ensures acc(&x.left) && acc(&x.right)
// postcondition 2
ensures x.left == old(x.left) + n
// postcondition 3
ensures x.right == old(x.right) + n
func (x *pair) sumPair(n int) {
  x.left += n
  x.right += n
}
```
Because `incr` changes the values of `x.left` and `x.right`, the function must state in
its precondition that it requires permissions to both of these fields. After the execution of
`incr`, the permissions to both fields are given back to the caller (*postcondition 1*).
Furthermore, *postcondition 2* and *postcondition 3* ensure that the fields `x.left` and `x.right`
are both incremented by `n`, respectively. Both postconditions make use of the `old` operator whose value
is the value of its operand in the beggining of the method execution.

As a short-hand,
one could have written `acc(x)` instead of `acc(x.left) && acc(x.right)`  to require access to all fields of `x`.

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
  p.sumPair(42)
  assert p.left == 43
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
  (&x).sumPair(42)
  assert x.left == 43
}
```

### Quantified Permissions

Gobra provides two main mechanisms for specifying permission to a (potentially unbounded) number of heap locations: recursive [predicates](#predicates) and *quantified permissions*.

While predicates can be a natural choice for modelling entire data  structures which are traversed in an orderly top-down fashion,  quantified permissions enable point-wise specifications, suitable for modelling heap structures which can be traversed in multiple directions, random-access data structures such as arrays, and unordered data structures such as graphs.

The basic idea is to allow resource assertions to occur within the body of a `forall` quantifier. In our example, function `addToSlice` receives a slice `s` and adds `n` to each element of the slice. 

```go
package tutorial

requires forall k int :: 0 <= k && k < len(s) ==> acc(&s[k])
ensures forall k int :: 0 <= k && k < len(s) ==> acc(&s[k])
ensures forall k int :: 0 <= k && k < len(s) ==> s[k] == old(s[k]) + n
func addToSlice(s []int, n int) {
	invariant 0 <= i && i <= len(s)
	invariant forall k int :: 0 <= k && k < len(s) ==> acc(&s[k])
	invariant forall k int :: i <= k && k < len(s) ==> s[k] == old(s[k])
	invariant forall k int :: 0 <= k && k < i ==> s[k] == old(s[k]) + n
	for i := 0; i < len(s); i += 1 {
		s[i] = s[i] + n
	}
}
```

To be able to change the contents of the slice, the function must have access to all elements of `s`. This is guaranteed by the pre-condition `forall k int :: 0 <= k && k < len(s) ==> acc(&s[k])` asserting access to all elements of the form `acc(&s[k])`, where `k` is a valid index of the array. 

```go
package tutorial

func addToSliceClient() {
    s := make([]int, 10)
    assert forall i int :: 0 <= i && i < 10 ==> s[i] == 0
    addToSlice(s, 10)
    assert forall i int :: 0 <= i && i < 10 ==> s[i] == 10
}
```


## Predicates
Until now, listing the heap locations accessed by a function could only be done with structures of bounded size or by using quantified permissions.

Alternatively, one can use *Preidcates*, which abstract parameterised assertions; a predicate can have any number of parameters, and its body can be any self-framing Gobra assertion using only these parameters as variable names.

Predicate  definitions can be recursive, allowing them to denote permission to and properties of recursive heap structures such as linked lists and trees.

For example, the following snippet defines a type `node` which represents a node of a linked list. Furthermore, it defines the predicate `list`, parameterised by `ptr`. The predicate `list` abstracts the access to the value of the head of the list (implying that the head is not `nil`) and, recursively, abstracts the access to the rest of the the values in the list if the tail is not empty, that is, if `ptr.next != nil`

```go
package tutorial

type node struct {
  value int
  next *node
}

pred list(ptr *node) {
  acc(&ptr.value) && acc(&ptr.next) && (ptr.next != nil ==> list(ptr.next))
}
```

Predicate instances can occur in function specifications. Predicate instances are not equivalent to their body. As such, the following program does not verify

```go
package tutorial

requires list(ptr)
ensures list(ptr)
func testPred(ptr * node) {
  assert list(ptr) // succeeds
  assert acc(&ptr.value) && acc(&ptr.next) && (ptr.next != nil ==> list(ptr.next)) // fails
}
```

In order to make this verify, Gobra requires an additional `unfold` operation that replaces a predicate instance with its body. Using `unfold`, the following addapted version of the previous example verifies

```go
package tutorial

requires list(ptr)
ensures list(ptr)
func testPred(ptr * node) {
  assert list(ptr)
  unfold list(ptr) // replaces list(ptr) by its body
  assert acc(&ptr.value) && acc(&ptr.next) && (ptr.next != nil ==> list(ptr.next)) // succeeds
}
```

In cases where an unfold statement is not allowed (e.g. specifications and bodies of pure functions), it is possible to unfold a predicate in the context of an expression.



reverse operation `fold`

For example, expects `ptr` to be a list, represented by the precondition `list(ptr)` and returns the first element of the list. `header` is a `pure` function. As such, all predicate instances and access permissions in the precondition are implicitly part of the function post-condition.

```go
package tutorial

requires list(ptr)
pure func head(ptr *node) int {
  return unfolding list(ptr) in ptr.value
}
```





Access

```go
package tutorial

requires acc(list(ptr), _)
pure func contains(ptr *node, value int) bool {
    return unfolding list(ptr) in ptr.value == value || (ptr.next != nil && contains(ptr.next, value))
}
```



TODO: predicates can occur in assertions and inside `acc`, fold, unfold and unfolding

- predicates don't mean the same thing as their body, they require an explicit unfolding



Like functions, Gobra predicates can also be abstract, i.e. have no body.



listing the access permissions to locations is always bounded. That limitation is overcome
TODO:
by using predicates

- predicates
- fold
- unfold
- unfolding






## Interfaces

Comparability
look at the list example with value as an interface

```go
package tutorial

type node struct {
  value interface{}
  next *node
}

pred list(ptr *node) {
  acc(&ptr.value) && isComparable(ptr.value) && acc(&ptr.next) && (ptr.next != nil ==> list(ptr.next))
}

requires list(ptr)
pure func contains(ptr *node, value interface{}) bool {
    return unfolding list(ptr) in ptr.value == value || (ptr.next != nil && contains(ptr.next, value))
}
```

TODO: remove myBox after the PR
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


## Concurrency
the following examples are enough to show:
- Goroutines
- First-class Predicates
- Lock example

```go
package tutorial

requires acc(x)
ensures acc(x)
func inc(x *int) {
	*x = *x + 1
}

func main() {
    x@ := 1
    go inc(x)
    // fails, race condition
    go inc(x)
}
```


```go
package pkg

import "sync"

pred mutexInvariant(x *int) {
	acc(x)
}

// For now, requires parentheses surrounding exp!<...!>
requires acc(pmutex.LockP(), _) && pmutex.LockInv() == mutexInvariant!<x!>;
ensures acc(pmutex.LockP(), _) && pmutex.LockInv() == mutexInvariant!<x!>;
func inc(pmutex *sync.Mutex, x *int) {
	pmutex.Lock()
	unfold mutexInvariant!<x!>()
	*x = *x + 1
	fold mutexInvariant!<x!>()
	pmutex.Unlock()
}

func client() {
	var x@ int = 0
	var px *int = &x
	var mutex@ = sync.Mutex{}
	var pmutex *sync.Mutex = &mutex
	fold mutexInvariant!<px!>()
	pmutex.SetInv(mutexInvariant!<px!>)
	go inc(pmutex, px)
	go inc(pmutex, px)
	go inc(pmutex, px)
}
```

### Channels
```go
package tutorial

pred sendInvariant(v *int) {
    acc(v) && *v > 0
}

func main() {
  var c@ = make(chan *int)
  var pc *chan *int = &c


  var x@ int = 42
  var p *int = &x
  (*pc).Init(sendInvariant!<_!>, PredTrue!<!>)
  go inc(pc, x)
  assert *p == 42
  fold sendInvariant!<_!>(p)
  *pc <- p

  fold PredTrue!<!>()
  res, ok := <- *pc
  if (ok) {
    unfold sendInvariant!<_!>(res)
    assert *res > 0
    // we have regained write access:
    *res = 1
  }
}

requires acc(pc, 1/2)
requires acc((*pc).SendChannel(), 1/2)
requires acc((*pc).RecvChannel(), 1/2)
requires (*pc).SendGivenPerm() == sendInvariant!<_!>;
requires (*pc).SendGotPerm() == PredTrue!<!>;
requires (*pc).RecvGivenPerm() == PredTrue!<!>;
requires (*pc).RecvGotPerm() == sendInvariant!<_!>;
func inc(pc *chan *int, ghost x int) {
    fold PredTrue!<!>()
    res, ok := <- *pc
    if (ok) {
        unfold sendInvariant!<_!>(res)
        // we should have write access and thus can write to it
        // before being able to fold again, we have to revert the value:
        *res = *res + 1
        // send pointer and permission back:
        fold sendInvariant!<_!>(res)
        *pc <- res
    }
}
```

## Running Gobra





## Not integrated in main text

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