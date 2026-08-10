# Concurrency: goroutines, mutexes, wait groups, channels, atomics

## Contents

- [The model](#the-model)
- [Goroutines](#goroutines)
- [Goroutines running closures](#goroutines-running-closures)
- [sync.Mutex](#syncmutex)
- [sync.WaitGroup](#syncwaitgroup)
- [Channels](#channels)
- [Closing a channel: debts and tokens](#closing-a-channel-debts-and-tokens)
- [Atomic operations and invariants](#atomic-operations-and-invariants)
- [Error index](#error-index)
- [Working examples in the repo](#working-examples-in-the-repo)

## The model

Gobra proves data-race freedom by making permissions linear: at most one thread may hold
write permission to a location. Every synchronisation primitive is therefore a *permission
transfer mechanism*, and each one needs a predicate saying **what** is transferred:

| Primitive | What is transferred | Named by |
|---|---|---|
| `go f(x)` | `f`'s precondition, one-way | `f`'s spec |
| `sync.Mutex` | the lock invariant, on every `Lock`/`Unlock` | `m.LockInv()`, set once via `SetInv` |
| `sync.WaitGroup` | debts and tokens | `pred()` values passed to `Add`/`Wait` |
| channel | the message invariant, per message | the two predicates passed to `Init` |
| `Invariant(P)` + `critical` | `P`, for the duration of one atomic step | the first-class predicate `P` |

All of these use **first-class predicates**: `P{args}` is a predicate constructor, `_` marks
an unapplied argument, and `P{args}()` is an *instance* that you `fold` before handing it
over and `unfold` after receiving it. `PredTrue{}` is the built-in arity-zero predicate whose
body is `true`; it still has to be folded (`fold PredTrue{}()`) before it can be transferred.

## Goroutines

`go f(x)` **consumes** `f`'s precondition and gives nothing back — the postcondition is lost
to the spawning thread. That asymmetry is the whole race-freedom argument:

```go
requires acc(x)
ensures  acc(x)
func inc(x *int) { *x = *x + 1 }

func concurrentInc() {
    x@ := 1
    go inc(&x)
    go inc(&x)   // fails: acc(&x) was already transferred to the first goroutine
}
```

Practical consequences:

- If you want to spawn N goroutines, the precondition must ask only for a **fraction**
  (`acc(x, 1/2)`, or `acc(m.LockP(), _)`) so enough remains for the next `go`.
- To get anything *back* from a goroutine you need a join mechanism: a `WaitGroup`, a
  channel, or a lock invariant. There is no implicit join.
- A failing `go` reports `go_call_precondition_error`: *"... might not satisfy the
  precondition of the callee"*. Check the precondition term by term with `assert` before the
  `go` line — it is almost always a permission that a previous `go` took.
- The variable must be addressable (`x@ := 1`) for `&x` to be permitted at all.

## Goroutines running closures

A `go` statement on a closure needs the spec at the spawn point, just like an ordinary call,
and needs the implementation proof in scope:

```go
func intSeq() {
    i@ := 0
    tmp := preserves acc(&i, 1/10)
           func f() int { return i }

    proof tmp implements spec{&i} {
        return tmp() as f{}
    }
    go tmp() as spec{&i}
}

preserves acc(x, 1/2)
func spec(x *int) int { return *x }
```

Note the fraction: the closure's own spec asks for `acc(&i, 1/10)` so more remains for
further spawns. See `references/closures.md` for the proof-body rules.

## sync.Mutex

The stub is `src/main/resources/stubs/sync/mutex.gobra`. The API:

- `pred (m *Mutex) LockP()` — the "this mutex is initialised" resource. Callers need any
  fraction; use the wildcard `acc(m.LockP(), _)` so it can be shared by all threads.
- `pred (m *Mutex) UnlockP()` — held between `Lock` and `Unlock`; it is what prevents
  unlocking a mutex you do not hold.
- `m.LockInv() pred()` — the invariant bound to the mutex; a pure ghost function.
- `m.SetInv(inv)` — binds the invariant. Requires `inv()` (folded!), full permission to the
  mutex, and that it is still the zero value. Produces `m.LockP()`.
- `m.Lock()` — requires `acc(m.LockP(), _)`; produces `m.UnlockP()` and `m.LockInv()()`.
- `m.Unlock()` — consumes `m.UnlockP()` and `m.LockInv()()`.

The full pattern:

```go
pred mutexInvariant(x *int) { acc(x) }

requires acc(pmutex.LockP(), _) && pmutex.LockInv() == mutexInvariant{x}
ensures  acc(pmutex.LockP(), _) && pmutex.LockInv() == mutexInvariant{x}
func safeInc(pmutex *sync.Mutex, x *int) {
    pmutex.Lock()
    unfold mutexInvariant{x}()
    *x = *x + 1
    fold mutexInvariant{x}()
    pmutex.Unlock()
}

func client() {
    x@ := 0
    mutex@ := sync.Mutex{}
    fold mutexInvariant{&x}()          // establish the invariant first
    (&mutex).SetInv(mutexInvariant{&x})
    go safeInc(&mutex, &x)
    go safeInc(&mutex, &x)             // works: the precondition needs only a wildcard
}
```

Things that go wrong here:

- **Forgetting `pmutex.LockInv() == mutexInvariant{x}` in the precondition.** Without it,
  `Lock()` gives you an unknown predicate instance and the `unfold` fails. This equation is
  the only way a function learns *which* invariant it is getting.
- **Asking for `m.LockP()` in full.** Then only one goroutine can be spawned. Use `acc(..., _)`.
- **Not folding before `SetInv`.** `SetInv` consumes `inv()`, so the invariant must already
  hold.
- The predicate constructor must be *identical* on both sides — `mutexInvariant{x}` and
  `mutexInvariant{y}` are different values even when `x == y` is provable at the top level,
  because equality of constructors is point-wise on the applied arguments.

There is no `RWMutex` stub; model it with a `Mutex` or write your own stub.

## sync.WaitGroup

`src/main/resources/stubs/sync/waitgroup.gobra`. The accounting has two halves: **debts**
(what a worker owes) and **tokens** (what the waiter will collect).

- `wg.Init()` — from `acc(wg) && *wg == WaitGroup{}`, produces `wg.WaitGroupP()`.
- `wg.Add(n, p, P)` — adds `n` units of debt, taking permission fraction `p` of
  `WaitGroupP()`.
- `wg.GenerateTokenAndDebt(R)` — exchanges a `PredTrue{}` debt for a debt of `R` plus a
  `Token(R)`: this is how you say "some worker will produce `R`, and the waiter may claim it".
- `wg.Start(p, P)` — moves to the started phase, producing `WaitGroupStarted()`.
- `wg.SetWaitMode(p, q)` — closes registration; after this no new `Add` with a positive count.
- In the worker: `wg.PayDebt(P)` consumes `P()` and clears the debt, then `wg.Done()`.
- `wg.Wait(p, P)` — with a sequence of the token predicates, yields each `InjEval(P[i], i)`.

Because the protocol is phase-ordered, most `WaitGroup` errors are "right resource, wrong
phase": `Add` after `SetWaitMode`, `Wait` before `SetWaitMode`, or a `Done` without a
matching `PayDebt`. Work through `defunc/waitgroup-simple1.gobra`, which annotates the
proof state after every single line — it is the most useful file in the repo for this API.

## Channels

Channels must be initialised before use. `c.Init(sendPred, recvPred)` takes:

1. a `pred(T)` describing the message (permissions and properties travelling with it), and
2. a `pred()` that the sender receives back from the receiver — a rendez-vous of
   permissions for unbuffered channels. **For buffered channels this must be `PredTrue{}`.**

After `Init` you hold `c.SendChannel()` and `c.RecvChannel()`; a fraction of the former lets
you send, a fraction of the latter lets you receive. The four accessor methods let a
function state which invariants it expects:

- `c.SendGivenPerm()` — must hold when sending; `c.RecvGotPerm()` — obtained when receiving
  (currently always equal).
- `c.RecvGivenPerm()` — must hold when receiving; `c.SendGotPerm()` — obtained when sending
  (currently always equal, and `PredTrue{}` unless the channel is unbuffered).

```go
pred sendInvariant(v *int) { acc(v) && *v > 0 }

requires acc(c.SendChannel(), 1/2)
requires acc(c.RecvChannel(), 1/2)
requires c.SendGivenPerm() == sendInvariant{_}
requires c.SendGotPerm()   == PredTrue{}
requires c.RecvGivenPerm() == PredTrue{}
requires c.RecvGotPerm()   == sendInvariant{_}
func incChannel(c chan *int) {
    fold PredTrue{}()            // the receiver must give this to the sender
    res, ok := <- c
    if ok {
        unfold sendInvariant{_}(res)
        *res = *res + 1
        fold sendInvariant{_}(res)
        c <- res
    }
}
```

Recurring mistakes:

- **`_` in the wrong place.** `sendInvariant{_}` has type `pred(*int)` — the message
  argument stays unapplied. Writing `sendInvariant{p}` (fully applied, type `pred()`) will
  not match what `Init` expects.
- **Not folding `PredTrue{}()` before a receive.** The receive consumes `RecvGivenPerm()()`.
- **Missing the four `==` preconditions.** Without them a callee has no idea which
  predicates the channel carries, and every `unfold` after a receive fails.
- **A buffered channel with a non-`PredTrue{}` second argument** — rejected.
- `c.BufferSize()` and `c.IsChannel()` are available as pure facts about the channel.

## Closing a channel: debts and tokens

Closing transfers a final resource to whoever observes the closed channel. Before spawning,
the main thread splits a folded predicate into a *closure debt* (given to the sender) and a
*token* (kept by the receiver):

```go
fold someLocation{p}()
c.CreateDebt(1, 2 /* 1/2 */, someLocation{p})
// now: c.ClosureDebt(someLocation{p}, 1, 2) && c.Token(someLocation{p})
go foo(1, c, p)
```

The sender pays the debt when it closes: `close(c, 1, 2, someLocation{p})`, which requires
`someLocation{p}()` and the matching `ClosureDebt`, and produces `c.Closed()`. The receiver
sees `!ok ==> c.Closed()`, and then `c.Redeem(someLocation{p})` turns its token back into
`someLocation{p}()`.

Note that `close` takes the numerator and denominator as separate arguments (`close(c, 1, 2)`
for `1/2`), which is a frequent source of confusion.

## Atomic operations and invariants

For lock-free code, Gobra offers invariants that may be opened for the duration of exactly
one atomic step:

- Mark an abstract operation `atomic`. Atomic members must terminate unconditionally
  (a non-conditional `decreases`) and may not have a body — *Gobra does not support proving
  that implementations are atomic*.
- `EstablishInvariant(P)` (built-in, ghost) consumes `P()` and produces `Invariant(P)`, which
  is duplicable knowledge that `P` is a global invariant.
- A `critical P (...)` region opens `P`, runs **at most one non-ghost atomic operation plus
  arbitrary ghost code**, and must re-establish `P` before the region ends.
- A ghost function that opens invariants declares `opensInvariants`.

```go
pred Own(x *int) { acc(x) }

requires Invariant(Own{x})
decreases
func tryCAS(x *int) {
    var v int
    critical Own{x} (
        unfold Own{x}()
        v = Get(x)
        fold Own{x}()
    )
    critical Own{x} (
        unfold Own{x}()
        _ = CAS(x, v, v+1)
        fold Own{x}()
    )
}
```

Control flow may not escape a critical region: `return`, `break`, and `continue` inside one
are type errors, because they would jump past the region's closing exhale and leak the
invariant's resources. Gobra tracks open/closed invariants around loops, so a loop that
contains a critical region needs `invariant Invariant(P)` in its loop invariant.

## Error index

| Message / id | Cause | Fix |
|---|---|---|
| `go_call_precondition_error` | the spawning thread does not hold the callee's precondition | check with `assert`; usually an earlier `go` took the permission — switch to fractions |
| `precondition_error:permission_error` at `Lock`/`Unlock`/send/receive | missing `LockP`/`UnlockP`/`SendChannel`/`RecvChannel` fraction | thread the fraction through the specs; use `acc(..., _)` for shareable ones |
| `unfold_error` after `Lock()` or a receive | the invariant's identity is unknown | add `m.LockInv() == P{...}` / the four channel `== ` preconditions |
| `fold_error` before `SetInv`/`CreateDebt`/a send | the predicate body does not hold yet | establish the body first; check each conjunct with `assert` |
| `send_error` / `receive_error` | channel not initialised, or the required "got/given" instance is not folded | `c.Init(...)`; `fold PredTrue{}()` before receiving |
| `is_invariant_failed` | the assertion opened by `critical` is not a valid invariant | it must be a first-class predicate instance established via `EstablishInvariant` |
| `invariant_already_open` | nested or re-entered `critical` on the same invariant | restructure so each region opens it once |
| `invariant_not_restored` | the region ends without re-folding | `fold` before the closing paren |
| `Atomic members must be guaranteed to terminate ... non-conditional decreases-clause` | `atomic` without `decreases` | add `decreases` |
| `Gobra does not support proving that implementations are atomic` | `atomic` function with a body | make it abstract (no body) |
| `Ghost members cannot be marked as atomic` | `ghost` + `atomic` | drop one |
| type error on `return`/`break`/`continue` inside `critical` | control flow escaping the region | restructure with a flag variable and exit after the region |

## Working examples in the repo

- `docs/tutorial.md` §Concurrency — goroutines, `sync.Mutex`, channels, first-class predicates
- `src/test/resources/regressions/features/go_routines/` — permission transfer, closures in `go`
- `src/test/resources/regressions/features/defunc/waitgroup-simple1.gobra` — the WaitGroup
  protocol with the proof state annotated line by line; `mutex1.gobra` alongside it
- `src/test/resources/regressions/features/channels/` — `channel-simple6.gobra` covers
  init, send, receive, `CreateDebt`/`Token`/`Redeem`, and `close`; the `channel-fail*.gobra`
  files pair each error id with its cause
- `src/test/resources/regressions/features/atomicsAndInvariants/` — `atomics-simple1.gobra`,
  `spinlock.gobra`, and `atomics-critical-controlflow.gobra` for the escape restrictions
- `src/main/resources/stubs/sync/` — the authoritative Mutex and WaitGroup specifications
