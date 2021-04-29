# A Tutorial on Gobra

## Introduction

*Gobra* is an automated, modular verifier for heap-manipulating, concurrent Go programs. It supports a large subset of Go, including Go’s interfaces and many primitive data structures. Gobra verifies memory safety, crash safety, data-race freedom, and partial correctness based on user-provided specifications. Gobra takes as input a `.gobra` file containing a Go program annotated with assertions such as method pre and postconditions and loop invariants. In case verification fails, Gobra reports at the level of the Go program which assertions it could not verify.

Verification proceeds by translating an annotated program into the Viper intermediate verification language and then applying an existing SMT-based verifier. 

This tutorial provides a practical introduction to Gobra and showcases Gobra's annotation syntax and main features. First, the tutorial covers the high-level structure of an annotated Go program. Afterwards, the tutorial gives a tour through the different features of Gobra  on several examples. Then, we demonstrate how to execute Gobra from the command line to verify `.gobra` files.

## Structure of annotated Go Programs

Like Go, annotated programs consist of a *package clause* followed by a list of *imports declarations*, followed by a list of *top-level declarations*.
In the following subsections, each of these components is described briefly.

### Package Clauses

```go
package pkgName
```

- The `package` keyword defines the package to which a file belongs.
- Every Gobra file is part of exactly one package.

### Import Declarations

```go
import (
    "strings"
    "sync"
    "time"
)
```
- Importing packages works as in Go.
- For an imported Go package, Gobra must find `.gobra` files with a matching package declaration in its included directories. Gobra will use those files as header files for the package declarations. 
- Files belonging to the same package have to be located in the same directory.
- The flag `-I path1 ... path2` includes the provided paths.
<!-- Gobra provides an incomplete but growing support for the Go standard library. Currently, it has partial support for the packages `encoding/binary`, `net`, `strconv`, `strings`, `sync`, and `time`. -->

### Top-Level Declarations

#### Functions and Methods

```go
requires ... // preconditions 
ensures  ... // postconditions 
func min(xs []int) int {
  ... // function body (optional)
}
```

```go
requires ... // preconditions 
ensures  ... // postconditions 
func (xs *list) contains(value int) bool {
  ... // method body (optional)
}
```
- Functions and methods can be annotated with pre and postconditions. If omitted, they default to `true`.
- Functions and methods are verified modularly. A call only uses the specification of the callee and cannot peek inside the body.
- The body (including `{}`) may be omitted. In this case, Gobra assumes that the specification holds.


#### Predicates

```go
pred listPermissions(xs *list) {
  ... // an assertion parameterized in the arguments (optional)
}
```

- Not part of the Go language and declared with the keyword `pred`.
- Typically used to abstract over assertions and to specify the shape of recursive data structures.
- See the [section on predicates](#predicates) for details.

#### Type Definitions

```go
type pair struct {
  left, right int
}
```

- Type declarations work as in Go.
- In this example, `pair` is the newly declared type whose underlying type is `struct{ left, right int }`. 


#### Constants

```go
const maxScore = 60 // untyped constant
const minScore int = 10 // typed constant
```
- Constants work as in Go.
- The value of a constant must be known at compile-time.
- Since constants cannot be assigned to, permissions are not required to access them.

#### Implementation Proofs

```go
(*counter) implements stream {
  ...
}
```

- Explained in [the section on interfaces](#interfaces)

## Basic Annotations

All examples shown in this tutorial can be found [here](../src/test/resources/regressions/examples/tutorial-examples/). We start with a simple function `sum` computing the sum of the integers from 0 to `n`. The table below shows a Go implementation (left) and a corresponding annotated version (right).

<table>
<tr>
<td> Go code </td> <td> Gobra code </td>
</tr>
<tr>
<td> 


```go
package tutorial



func sum(n int) (sum int) {
  sum = 0



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
ensures  sum == n * (n+1)/2 // postcondition
func sum(n int) (sum int) {
  sum = 0

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

In addition to the Go code, the annotated version has a *method specification*, in the form of a pre and postcondition, and an invariant for the loop. The precondition `0 <= n` restricts the argument `n` to non-negative values. A caller of `sum` has to guarantee this assertion. The postcondition `sum == n * (n+1)/2` expresses an assertion that holds after a call returns, which has to be guaranteed by the function body. Pre and postconditions can be split into multiple `requires` and `ensures` clauses. The invariant `0 <= i && i <= n + 1` and `sum == i * (i-1)/2` is used to the verify the loop. An invariant has to hold before a loop and must hold at the end of the loop body. Note that the initial statement `i := 0` is considered to happen before the loop. After the loop, we know that the invariant and the negated loop condition holds. At this point in time, Gobra does not prove termination so only partial correctness is provided.

As assertions, Gobra supports Go's side-effect-free, determinsitic boolean expressions (e.g. `x > y + z`), implications (`==>`), conditionals (`cond ? e1 : e2`), separating conjunctions (`&&`), universal quantifiers (e.g. `forall x int :: x >= 5 ==> x >= 0`), and existential quantifiers (e.g. `exists x int :: n > 42`). More specific assertions, e.g. for the access of heap locations and interfaces are introduced in subsequent sections.

When a function or method does not have side-effects, such as modifying heap-allocated structures, and is deterministic, it may be annotated with a `pure` clause. We refer to these functions and methods as `pure`. Different from non-pure code, calls to pure code can be used in specifications. For that reason, they are a powerful specification mechanism. Consider the following snippet. The function `isEven` is pure and returns whether the argument is even. The `halfRoundedUp` uses `isEven` in its method specifiction.

```go
package tutorial

ensures res == (n % 2 == 0)
pure func isEven(n int) (res bool) {
  return n % 2 == 0
}

ensures isEven(n) ==> res == n / 2
ensures !isEven(n) ==> res == n / 2 + 1
func halfRoundedUp(n int) (res int) {
  if isEven(n) {
    res = n / 2
  } else {
    res = n / 2 + 1
  }
  return res
}
```

Currently, the `pure` modifier has to be written after the pre and postcondition and before the `func` keyword. Furthermore, pure functions and methods have strict syntax constrains, the body must be a single return statement that returns a single deterministic side-effect free expression.


## Permissions and Heap-Manipulation

Gobra uses *implicit dynamic frames*, a variant of *separation logic*, to support reasoning about mutable heap data. In this model, each heap location is associated with an *access permission*. Access permissions are held by method executions and transferred between methods upon call and return. A method may access a location or make a call only if the appropriate permission is held. 

### Accesibility Predicate

In Go, heap locations can be thought of as addresses of pointers. The permission to access a pointer `x` is denoted by the *accessibility predicate* `acc(x)`. Having permission to a pointer implies that the pointer is not `nil`, Go's version of null. Access predicates are not duplicable resources, meaning that an accessibility predicate `P` does not entail `P && P`, where `&&` is a separating conjunction.  


The following code snippet declares a type `pair`, consisting of two integer fields, and a method `incr` that increments both fields of a `pair` pointer by an integer `n`.

```go
package tutorial

type pair struct {
  left, right int
}

// incr requires permission to modify both fields
requires acc(&x.left) && acc(&x.right)
ensures  acc(&x.left) && acc(&x.right)
ensures  x.left == old(x.left) + n
ensures  x.right == old(x.right) + n
func (x *pair) incr(n int) {
  x.left += n
  x.right += n
}
```

The method `incr` modifies heap locations by assigning to `x.left` and `x.right`. For these assignments, `incr` requires permissions to the pointers of the two fields `&x.left` and `&x.right`. It gets these permissions from its precondition `acc(&x.left) && acc(&x.right)`. When calling `incr` a caller has to transfer these permissions to the method. The postcondition `acc(&x.left) && acc(&x.right)` specifies that the permissions are returned to the caller when the call returns. As a short-hand, one can write `acc(x)` instead of `acc(&x.left) && acc(&x.right)`. Method specifications always have to specify the permissions that are returned except for pure code, which implicitly returns all permissions specified in the precondition. 


When modifying the heap it is useful to refer to the old state of the heap. An *old expression*, `old(e)` evaluates the expression `e` in the state of the precondition. As such, the postcondition `x.left == old(x.left) + n` specifies that after `incr` returns the value of `x.left` is equal to the value of `x.left` when the function was called plus `n`.

Below, we show two clients of `incr`. The function`client1` allocates a `pair` pointer. When memory is allocated, permissions to the allocated heap locations are acquired. After `p := &pair{1,2}`, permissions to both field pointers are held. The call to `incr` on the subsequent line then uses these permissions. The assertion at the end checks that `x.left == 43` holds after the call.

```go
func client1() {
  p := &pair{1,2}
  p.sumPair(42)
  assert p.left == 43
}
```

Similar to `client1`, `client2` allocates a `pair` pointer, as well. However, the allocation happens implicitly. In Gobra, when a variable is referenced (or captured by a closure), it has to be annotated with `@`. The annotation expresses that the variable is a heap location and as such involves permissions. The annotation could be inferred automatically, but we believe that inferring it makes the behavior harder to predict. Otherwise, `client1` is identical to `client2`.

```go
func client2() {
  x@ := pair{1,2} // if taking the reference of a variable should be possible, then add @
  (&x).sumPair(42)
  assert x.left == 43
}
```

Gobra offers fractional permissions, meaning that it can distinguish between different amounts of permissions. A permission amount is rational number between 0 and 1. Modifying the value of a heap location requires full permission, denoted as `acc(x, write)`. Reading from a heap location requires any non-zero permission amount. Read permissions are usually denoted as factions, e.g., `acc(x, 1/2)`. We can also write `acc(x, _)` for an arbitrary non-zero permission amount.



### Quantified Permissions

Gobra provides two key mechanisms for specifying permission to a (potentially unbounded) number of heap locations: recursive [predicates](#predicates) and *quantified permissions*.

While predicates can be a natural choice for modeling entire data  structures which are traversed in an orderly top-down fashion,  quantified permissions enable point-wise specifications, suitable for modeling heap structures that can be traversed in multiple directions, random-access data structures such as arrays, and unordered data structures such as graphs.

The basic idea is to allow resource assertions to occur within the body of a `forall` quantifier. In our example, function `addToSlice` receives a slice `s` and adds the integer `n` to each element of the slice. 

```go
package tutorial

requires forall k int :: 0 <= k && k < len(s) ==> acc(&s[k])
ensures  forall k int :: 0 <= k && k < len(s) ==> acc(&s[k])
ensures  forall k int :: 0 <= k && k < len(s) ==> s[k] == old(s[k]) + n
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

To be able to change the contents of the slice, the function must have access to all elements of `s`. This is provided by the precondition `forall k int :: 0 <= k && k < len(s) ==> acc(&s[k])` asserting access to all locations `&s[k]`, where `k` is a valid index of the slice. 

The function below is a client of the `addToSlice` function. The `make` call allocates the permissions required for the call.

```go
func addToSliceClient() {
  s := make([]int, 10)
  assert forall i int :: 0 <= i && i < 10 ==> s[i] == 0
  addToSlice(s, 10)
  assert forall i int :: 0 <= i && i < 10 ==> s[i] == 10
}
```


## Predicates

Predicates give a name to a parameterized assertion. A predicate can have any number of parameters, and its body can be any self-framing Gobra assertion using only these parameters as variable names. Predicate  definitions can be recursive, allowing them to denote permission to and properties of recursive heap structures such as linked lists and trees.

The following snippet defines a type `node`, representing a node of a linked list. The predicate `list`, parameterized by `ptr`, abstracts the permissions of a linked list. The predicate body contains permissions to the `value` and `next` fields of the head and, recursively, contains permissions to the rest of the list if the tail is not empty, that is, if `ptr.next != nil`.

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

Predicate instances can occur in function specifications. In Gobra, predicates are *iso-recursive*, meaning that predicate instances are not equivalent to their body. As such, the following program does not verify:

```go
requires list(ptr)
ensures  list(ptr)
func testPredFail(ptr * node) {
  assert list(ptr) // succeeds
  assert acc(&ptr.value) && acc(&ptr.next) // fails
}
```

To make the snippet verify, Gobra requires an additional `unfold` annotation, telling Gobra to replace a predicate instance with its body. Conversely, the `fold` annotation exchanges the predicate body with the predicate instance. Using `unfold`, the adapted version of the previous snippet verifies:

```go
requires list(ptr)
ensures  list(ptr)
func testPred(ptr * node) {
  assert list(ptr)
  unfold list(ptr) // exchanges list(ptr) with its body
  assert acc(&ptr.value) && acc(&ptr.next) 
  fold list(ptr)   // folding back the predicate for the postcondition
}
```

In cases where statements are not allowed (e.g., specifications and bodies of pure functions), we use `unfolding` expressions, which temporarily unfold and fold a predicate instance. For instance, in the body of the `header` function below, the only way to acquire access to `ptr.value` is by `unfolding list(ptr)`.

```go
requires list(ptr)
pure func head(ptr *node) int {
  return unfolding list(ptr) in ptr.value
}
```

In the previous examples of this section, the precondition requires write permissions to the list. If a function does not modify the list, write permissions are stronger than required. To specify that we want less than write permissions, we use accessibility predicates, similar to heap locations. In the following example, the argument `p` of type `perm` specifies the permission amount we use for the predicate. The precondition `p > 0` asserts that this permissions amount is positive and, thus, grants read access to the elements of the list.

```go
requires p > 0
requires acc(list(ptr), p)
pure func (ptr *node) contains(value int, ghost p perm) bool {
  return unfolding list(ptr) in ptr.value == value || (ptr.next != nil && ptr.next.contains(value, p))
}
```

Often, in particular for pure functions, we do not care about the exact permission amount since all permissions in the precondition are returned implicitly in the postcondition. In these cases, we can use a wildcard permission amount `_`specifying an arbitrary positive permission amount.

```go
requires acc(list(ptr), _)
pure func (ptr *node) contains(value int) bool {
  return unfolding list(ptr) in ptr.value == value || (ptr.next != nil && ptr.next.contains(value))
}
```

Gobra predicates can also be abstract, i.e., have no body, to represent opaque permissions. Abstract predicates cannot be unfolded.


## Ghost Code

Often, introducing additional state, such as additional variables or arguments is useful for specification and verification. Consider the following `contains` function. The function returns whether or not an element `x` is contained in a slice `s`. One way to express that an element is contained in a slice is to specify that there is an index within the slice at which the element is stored. In the example, we use the ghost out-parameter `idx`, annotated with the keyword `ghost`, to store this index, allowing us to write an appropiate postcondition.

```go
package tutorial

requires forall k int :: 0 <= k && k < len(s) ==> acc(&s[k], 1/2)
ensures  forall k int :: 0 <= k && k < len(s) ==> acc(&s[k], 1/2)
ensures  isContained ==> 0 <= idx && idx < len(s) && s[idx] == x
func contains(s []int, x int) (isContained bool, ghost idx int) {

  invariant 0 <= i && i <= len(s)
  invariant forall k int :: 0 <= k && k < len(s) ==> acc(&s[k], 1/4)
  for i := 0; i < len(s); i += 1 {
    if s[i] == x {
      return true, i
    }
  }

  return false, 0
}
```

Apart from the ghost in and out-parameters, Gobra offers additional ghost constructs: ghost variables, ghost types, ghost statements, and ghost methods and functions. A ghost variable is defined via a ghost declaration. For instance, the statement`ghost var x int = 42` declares a ghost variable `x` and assigns it 42. Ghost types such as sequences, sets, and multisets are mathematical types useful for specification purposes (see the [datatype section](#Datatypes) for more details).  Ghost if-then-else and loops are used to perform case splits and inductions, respectively. To make an actual statement a ghost statement, just add the `ghost` keyword before the statement. Pure ghost functions can be used to abstract heap state to a mathematical representation. The `toSeq` function below takes as parameter a slice and returns a mathematical sequence containing the elements of the slice. A ghost function or method is annotated with the keyword `ghost`, which at this point, has to be written before the pre and postconditions. For ghost functions and methods, all in- and out-parameters are implicitly ghost. Gobra checks that no ghost code influences actual code. Note that the opposite does not have to hold. For instance, the value of an actual variable can be assigned to a ghost variable.

```go
ghost
requires forall j int :: 0 <= j && j < len(s) ==> acc(&s[j],_)
ensures len(res) == len(s)
ensures forall j int :: {s[j]} {res[j]} 0 <= j && j < len(s) ==> s[j] == res[j]
pure func toSeq(s []int) (res seq[int]) {
  return (len(s) == 0 ? seq[int]{} :
      toSeq(s[:len(s)-1]) ++ seq[int]{s[len(s) - 1]})
}
```

## Datatypes

Gobra supports many of Go's native types, namely integers (`int`, `int8`, `int16`, `int32`, `int64`, `byte`, `uint8`, `rune`, `uint16`, `uint32`, `uint64`, `uintptr`), strings, structs, pointers, arrays, slices, interfaces, and channels. Note that currently the support for strings and specific types of integers such as `rune` is very limited. 

In addition, Gobra introduces additional ghost types for specification purposes. These are sequences (`seq[T]`), sets (`set[T]`), multisets (`mset[T]`), and permission amounts (`perm`). Gobra supports their common operations: sequence concatenation (`seq1 ++ seq2`), set union (`set1 union set2`), membership (`x in set1`), multiplicity (`x # set1`), sequence length (`len(seq1)`), and set cardinality (`|set1|`). 


## Interfaces

In Go, interfaces consist of method signatures. In Gobra, all these method signatures additionally have a method specification. Furthermore, an interface can define predicates to abstract over the heap structure of the underlying dynamic value. Consider the following declaration for an interface `stream`. The interface has two methods, the pure method `hasNext`, returning whether more elements are available, and the method `next`, returning the next element of the stream. The predicate `mem` abstracts over the underlying heap structure.

<table>
<tr>
<td> Go code </td> <td> Gobra code </td>
</tr>
<tr>
<td> 

```go
package tutorial

type stream interface {


  
  hasNext() bool

  
  
  next() interface{}
}
```

</td>
<td>

```go
package tutorial

type stream interface {
  pred mem()

  requires acc(mem(), 1/2)
  pure hasNext() bool

  requires mem() && hasNext()
  ensures  mem()
  next() interface{}
}
```

</td>
</tr>
</table>

The following snippet illustrates an implementation of the `stream` interface. The function `typeOf` returns the dynamic type of an interface. The type assertion `y.(int)` requires that the interface `y` has the dynamic type `int` and returns the dynamic value of the interface. Note that without a preceding `typeOf(y) == int` the type assertion fails.

```go
// implementation
type counter struct{ f, max int }

requires acc(x, 1/2)
pure func (x *counter) hasNext() bool {
  return x.f < x.max
}

requires acc(&x.f) && acc(&x.max, 1/2) 
requires x.hasNext()
ensures  acc(&x.f) && acc(&x.max, 1/2) && x.f == old(x.f)+1 
ensures  typeOf(y) == int && y.(int) == old(x.f)
func (x *counter) next() (y interface{}) {
  y = x.f
  x.f += 1
  return
}
```

Before a value of type `*counter` can be assigned to the `stream` interface, one has to show that `*counter` is a behavioral subtype of `stream`. This means that for all methods the precondition of the interface method implies the precondition of the implementation method and the postcondition of the implementation method implies the postcondition of the interface method.  In Gobra, such proofs are done via *implementation proofs*. The snippet below shows such an implementation proof for `*counter` and `stream`. The definition of the predicate `mem` is part of the implementation proof and defines the permissions that `*counter` requires to satisfy the interface specification. The implementation proof clause is denoted with the keyword `implements`. Inside the implementation proof clause, for each method of the interface, there is a proof method. The pre and postconditon of the proof method are omitted as they are determined by the corresponding method of the interface. Implementation proofs are heavily syntactically restricted. The body must have a single call to the method of the implementation with the right receiver and the in- and out-parameters in the correct order. Additionally, the body is only allowed to contain assert statements and fold and unfold annotations to manipulate predicate instances. In the example, we require unfolds and folds to shift from the `mem()` predicate instance of the interface methods to the direct pointer permissions of the implementation methods and vice versa. At a later point in time, additional proof annotations will be allowed.


```go
// implementation proof
pred (x *counter) mem() { acc(x) }

(*counter) implements stream {
  pure (x *counter) hasNext() bool {
    return unfolding acc(x.mem(), 1/2) in x.hasNext()
  }

  (x *counter) next() (res interface{}) {
    unfold x.mem()
    res = x.next()
    fold x.mem()
  }
}
```

If an implementation proof or part of it is omitted, Gobra tries to infer the missing parts. However, at this point in time, Gobra uses a simple heuristic that fails if the proof requires folding and unfolding of predicates. As a special case, the empty interface `interface{}` is trivially implemented by all types. As such, in `*counter.next`, the statement `y = x.f` assigning an integer to the empty interface does not require a manual implementation proof.

The snippet below shows a small client that instantiates a `*counter`, assigns it to the `stream` interface, and calls the method `next`. Note that Gobra can show that the precondition of `y.next()`, namely `y.hasNext()` holds.

```go
// client code
func client() {
  x := &counter{0, 50}
  var y stream = x
  fold y.mem()
  var z interface{} = y.next()
}
```

Implementation proofs can be placed in a different package than the orginal type definition. In this case, the syntax for the predicate is slightly different. You can find such an example [here](https://github.com/viperproject/gobra/blob/master/src/test/resources/regressions/features/interfaces/counterStream.gobra).

### Comparability

In Go, comparing two interfaces can cause a runtime panic. This happens if both dynamic values of an interface are incomparable. For that purpose, Gobra provides the function `isComparable`. The function takes as input an interface or a type and returns whether it is comparable according to Go. The snippet below shows a variation of a list where the values are interfaces. As an invariant, written in the `list` predicate, all elements of the list are comparable, meaning that a comparison with another interface is guaranteed to not cause a runtime panic. When the value of an implementation is put into an interface, Gobra generates the information of whether the resulting interface is comparable.


```go
package tutorial

type node struct {
  value interface{}
  next *node
}

pred list(ptr *node) {
  acc(&ptr.value) && isComparable(ptr.value) && acc(&ptr.next) && 
  (ptr.next != nil ==> list(ptr.next))
}

requires list(ptr)
pure func contains(ptr *node, value interface{}) bool {
  return unfolding list(ptr) in ptr.value == value || (ptr.next != nil && contains(ptr.next, value))
}
```

## Concurrency

Gobra supports verification of concurrent Go programs that incorporate multi-threaded programming via goroutines, channel message passing, and shared memory. Gobra ensures that every verified program is free of race-conditions. The snippet below shows code with a race condition that cannot be verified. The function `concurrentInc` spawns two goroutines that concurrently modify the value of `x`. Only one of the spawned goroutines can own the permission to `x` at any point in time. When the first goroutine is started `go inc(&x)`, the permission `acc(x)` is transferred to the goroutine. Different from a normal call, the permissions in the postcondition of the goroutine are not returned after `go inv(&x)`. As a consequence, starting the second goroutine fails, as `acc(x)` is not held anymore by the execution of `concurrentInc`.

```go
package tutorial

requires acc(x)
ensures acc(x)
func inc(x *int) {
  *x = *x + 1
}

func concurrentInc() {
  x@ := 1
  go inc(&x)
  // fails with a permission error due to a race condition
  go inc(&x)
}
```

The race condition in the previous example can be avoided by using concurrency primitives to synchronize the accesses to `x`. In Gobra, concurrency primitives make extensive use of Gobra's first-class predicates. Because of that, we first introduce predicate expressions in the next section. Afterwards, we show a race-condition free version of `concurrentInc` that uses the `Mutex` type defined in the `sync` package. Finally, we describe how to verify programs with channels.

### First-Class Predicates

Gobra has support for first-class predicates, i.e., expressions with a predicate type. First-class predicates are of type `pred(x1 T1, ..., xn Tn)`. The types `T1, ..., Tn` define that the predicate has an arity of `n` with the corresponding parameter types. 

To instantiate a first-class predicate, Gobra provides *predicate constructors*. A predicate constructor `P!<d1, ..., dn!>` partially applies a declared predicate `P` with the constructor arguments `d1, ..., dn`. A constructor argument is either an expression or a wildcard `_`, symbolizing that this argument of `P` remains unapplied. In particular, the type of `P!<d1, ..., dn!>` is `pred(u1,...,uk)`, where `u1,...,uk` are the types of the unapplied arguments. As an example, consider the declared predicate `pred sameValue(i1 int8, i2 uint32){ ... }`. The predicate constructor `sameValue!<int8(1), _!>` has type `pred(uint32)`, since the first argument is applied and the second is not. Conversely, `sameValue!<_, uint32(1)!>` has type `pred(int8)`. Finally, `sameValue!<int8(1), uint32(1)!>` and `sameValue!< _, _!>` have types `pred()` and `pred(int8, uint32)`, respectively.

The equality operator for predicate constructors is defined as a point-wise comparison, that is, `P1!<d1, ..., dn!>` is equal to `P2!<d'1, ... , d'n!>` if and only if `P1` and `P2` are the same declared predicate and if `di == d'i` for all `i` ranging from 1 to `n`.

The body of the predicate `P!<d1, ..., dn!>` is the body of `P` with the arguments applied accordingly. Like with other predicates, `P!<d1, ..., dn!>` can be instantiated and its instances may occur in assertions and in `fold` and `unfold` statements. The fold statement `fold P!<d1, ..., dk!>(e1, ..., en)` exchanges the first-class predicate instance with its body. The unfold statement does the reverse.

> **Note**: In the paper, we use the notation `P{...}` instead of `P!<...!>`. Currently, Gobra uses `!<` and `!>` as delimiters to simplify Gobra's parser. In the future, we will change to the `P{...}` syntax.

### Reasoning about Mutual Exclusion with `sync.Mutex`


In the following example, we show the function `safeInc`, a data-race free version of the previously seen function `inc` in section [Concurrency](#concurrency). The snippet uses a lock `pmutex` to synchronize the write accesses to the pointer `x`. `safeInc`'s pre and postcondition assert that `pmutex` was initialized (`acc(pmutex.LockP(), _)`) with invariant `mutexInvariant` (`pmutex.LockInv() == mutexInvariant!<x!>`), protecting the access to variable `x`. In the body of `safeInc`, first `pmutex.Lock()` is called to acquire the invariant `mutexInvariant!<x!>`. Then the first-class predicate instance is unfolded to acquire `acc(x)`, required to modify the value of `x`. At the end, the invariant is folded back again, after which `pmutex` can be released via a call to method `Unlock`.

```go
package tutorial

import "sync"

pred mutexInvariant(x *int) {
  acc(x)
}

requires acc(pmutex.LockP(), _) && pmutex.LockInv() == mutexInvariant!<x!>;
ensures  acc(pmutex.LockP(), _) && pmutex.LockInv() == mutexInvariant!<x!>;
func safeInc(pmutex *sync.Mutex, x *int) {
  pmutex.Lock()
  unfold mutexInvariant!<x!>()
  *x = *x + 1
  fold mutexInvariant!<x!>()
  pmutex.Unlock()
}
```
> **Note:** At this point in time, using semicolons is mandatory to terminate lines that end with the `!<` `!>` delimiters like in the pre and postcondition of `safeInc`. This is a known limitation of our current parser and it will be fixed when we adopt the `{` `}` delimeters for predicate constructors. 

The snippet below shows a client. First, `mutex` is allocated and the invariant is established for the first time by folding `mutexInvariant!<&x!>()`. To bind an invariant to a mutex, the `SetInv` method is called. `SetInv` expects as argument a first-class predicate with arity zero. After the invariant is bound to `&mutex`, the mutex is initialized and the permission `&mutex.LockP()` is acquired. This permission is necessary to call `Lock` and `Unlock`. At this point, the preconditions of the goroutines are satisfied and the goroutines can be started. Note that the preconditon of `safeInc` does not require write permission to the lock and thus, after starting the first goroutine, there are sufficient permissions remaining to start the second goroutine.

```go
func client() {
  x@ := 0
  mutex@ := sync.Mutex{}
  fold mutexInvariant!<&x!>()
  (&mutex).SetInv(mutexInvariant!<&x!>)
  go safeInc(&mutex, &x)
  go safeInc(&mutex, &x)
}
```

### Channels

We now shift our attention to channels. Similar to mutexes, channels must be initialized before any message can be sent and received through them. This is done via a call to the method `Init`. The method has two parameters: The first parameter is a first-class predicate with type `pred(T)`, where `T` is the type of messages communicated via the channel. It specifies properties and permissions of the data that is sent through the channel. The second parameter is a first-class predicate with type `pred()` that specifies properties and permissions that the sender obtains from the receiver when it sends a message. This is useful to model a rendez-vous of permissions in synchronous communication, which happens for unbuffered channels. In this example, and whenever the channel is buffered, the second argument will always be `PredTrue!<!>`, a predicate with arity zero whose body is `true`. Initializing a channel `c` through `Init` also produces permissions `m.SendChannel()` and `m.ReceiveChannel()`. Any read fraction of `m.SendChannel()` allows a function to send messages on `m`, whereas a read fraction of `m.ReceiveChannel()` allows a function to receive from `m`.

After initialization, the predicates passed as arguments to `Init` can be retrieved via several methods. The method `c.SendGivenPerm()` returns the invariant that must hold when the sender sends a message. The method `c.RecvGotPerm()` returns the invariant that the receiver of a message can assume. Currently, these are always the same. Likewise, `c.SendGotPerm()` and `c.RecvGivenPerm()` are the invariants that must hold when a message is received and that can be assumed when a message is sent, respectively. Currently, these are also always the same. Furthermore, they must be `PredTrue!<!>` unless the channel is unbuffered.

In the following example, the function `incChannel` receives an `*int` from channel `c` and increments the value on the received location. The first two preconditions of `incChannel` require a fraction of `c.SendChannel()` and `c.RecvChannel()` to send on and receive from channel `c`. The rest of the preconditions establish that function `incChannel` expects `c` to have been initialized with the arguments `sendInvariant!<_!>` and `PredTrue!<!>`. The predicate `sendInvariant` contains the permission to the pointer sent over the channel `c` and restricts that `*v` is positive.

The body of `incChannel` starts by folding `PredTrue!<!>()` to be able to receive from the channel. Recall that an instance of `PredTrue` must be sent from receiver to sender. It then receives from `c` and in case of success, unfolds `sendInvariant!<_!>(res)` to acquire write permissions to `res`. It then modifies `*res` and folds back `sendInvariant!<_!>(res)` to send the reply.

```go
package tutorial

pred sendInvariant(v *int) {
  acc(v) && *v > 0
}

requires acc(c.SendChannel(), 1/2)
requires acc(c.RecvChannel(), 1/2)
requires c.SendGivenPerm() == sendInvariant!<_!>;
requires c.SendGotPerm() == PredTrue!<!>;
requires c.RecvGivenPerm() == PredTrue!<!>;
requires c.RecvGotPerm() == sendInvariant!<_!>;
func incChannel(c chan *int) {
  fold PredTrue!<!>()
  res, ok := <- c
  if (ok) {
    unfold sendInvariant!<_!>(res)
    // we now have write access after unfolding the invariant: 
    *res = *res + 1
    // fold the invariant and send pointer and permission back:
    fold sendInvariant!<_!>(res)
    c <- res
  }
}
```

The next snippet shows a client. In the body, a channel `c` is created and initialized with `sendInvariant` and `PredTrue`. It then spawns a goroutine, executing `incChannel` with the initialized channel. Next, it folds `sendInvariant!<_!>(p)` to send `p` over the channel `c`. The subsequent fold of `PredTrue!<!>()` is required to satisfy the precondition of receive. At this point, `PredTrue!<!>()` must be sent from `clientChannel` (the receiver) to `incChannel` (the sender). After successfully receiving the reply, the receiver acquires `sendInvariant!<_!>(res)`, which can then be unfolded to obtain the permission to `res` and to check that its value is positive.

```go
func clientChannel() {
  var c@ = make(chan *int)

  var x@ int = 42
  var p *int = &x
  c.Init(sendInvariant!<_!>, PredTrue!<!>)
  go incChannel(c)
  assert *p == 42
  fold sendInvariant!<_!>(p)
  c <- p

  fold PredTrue!<!>()
  res, ok := <- c
  if (ok) {
    unfold sendInvariant!<_!>(res)
    assert *res > 0
    // we have regained write access:
    *res = 1
  }
}
```


## Running Gobra

A description of how to install and configure Gobra can be found in Gobra's [README](https://github.com/viperproject/gobra/blob/master/README.md) file. After setting up Gobra and obtaining a `gobra.jar` file, one can run it using the command
```bash
java -Xss128m -jar gobra.jar -i [FILES_TO_VERIFY]
```
To check the full list of flags available in Gobra, run the command
```bash
java -jar gobra.jar -h
```