# A Tutorial on Gobra
Gobra is an automated, modular verifier for heap-manipulating, concurrent Go programs. Gobra supports a large subset of Go, including Go’s interfaces and primitive data structures.

Gobra verifies memory safety, crash safety, data-race freedom, and user-provided specifications. It takes as input a Go program annotated with assertions such as pre- and postconditions and loop invariants.

This tutorial presents Gobra's features and how they can be used ...

Talk about the extension `.gobra`

## Introduction
- Introduce Gobra

## Overview
Mention the structure of a program
### Top-Level Declarations

### Built-in types

### Imports and Support for the Std Lib

## Examples
### 1. ???

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
