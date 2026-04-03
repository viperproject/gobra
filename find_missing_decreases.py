#!/usr/bin/env python3
"""
Find pure and ghost functions in .gobra test files that are missing
`decreases` termination measures.

Gobra supports two annotation styles:
  1. //@-prefixed: //@ requires ..., //@ decreases ...
  2. Bare keywords:  requires ..., decreases ..., pure, ghost

A func is "pure" if:
  - The func line starts with `pure func` (possibly with ghost before), OR
  - There is a bare `pure` standalone keyword OR `//@\s*pure` in the contract block

A func is "ghost" if:
  - The func line starts with `ghost func`, OR
  - There is a bare `ghost` standalone keyword OR `//@\s*ghost` in the contract

A func needs `decreases` if it's pure or ghost and there's no `decreases`
anywhere in the contract block.

Contract block: lines before `func` that are //@-annotations or bare contract
keywords (requires, ensures, preserves, decreases, pure, ghost, opaque, trusted).
Bare `ghost` / `pure` means the keyword ALONE on a line (not `ghost type`, `ghost var`, etc.)

For interface method specs (inside interface { }) with pure/ghost prefix,
they also need `decreases`.

Skip:
  - Functions inside `(T) implements I { }` blocks
  - Functions inside `domain { }` blocks
  - Functions with `//:: ExpectedOutput(type_error)` nearby
  - Closure literals (pure func inside function bodies) - detected by indentation
"""

import os
import re
import sys
from pathlib import Path

ROOT = Path("/home/user/gobra/src/test/resources/regressions/")

# //@-style contract line
ANNOT_CONTRACT_RE = re.compile(r'^\s*//@\s*(.*)')

# Bare contract line patterns - must be one of these specific forms:
# - //@<anything>
# - requires <anything>
# - ensures <anything>
# - preserves <anything>
# - decreases <anything>
# - pure  (alone, or with trailing comment)
# - ghost  (alone, or with trailing comment)
# - opaque  (alone)
# - trusted (alone)
# NOT: ghost type, ghost var, pure func (those are declarations)

BARE_REQUIRES_RE = re.compile(r'^\s*(?:requires|ensures|preserves)\b')
BARE_DECREASES_RE = re.compile(r'^\s*decreases\b')
# bare "pure" alone (possibly followed by whitespace/comment, but NOT by a keyword that would make it a declaration)
BARE_PURE_STANDALONE_RE = re.compile(r'^\s*pure\s*(?://.*)?$')
# bare "ghost" alone
BARE_GHOST_STANDALONE_RE = re.compile(r'^\s*ghost\s*(?://.*)?$')
# bare "opaque" or "trusted" alone
BARE_OPAQUE_RE = re.compile(r'^\s*(?:opaque|trusted)\s*(?://.*)?$')


def is_contract_line(line):
    """True if this line is a contract annotation."""
    if re.match(r'^\s*//@', line):
        return True
    if BARE_REQUIRES_RE.match(line):
        return True
    if BARE_DECREASES_RE.match(line):
        return True
    if BARE_PURE_STANDALONE_RE.match(line):
        return True
    if BARE_GHOST_STANDALONE_RE.match(line):
        return True
    if BARE_OPAQUE_RE.match(line):
        return True
    return False


def line_has_decreases(line):
    """Check if a contract line has a decreases clause."""
    m = ANNOT_CONTRACT_RE.match(line)
    if m:
        return bool(re.match(r'\s*decreases\b', m.group(1)))
    if BARE_DECREASES_RE.match(line):
        return True
    return False


def line_has_pure(line):
    """Check if a contract line indicates 'pure'."""
    m = ANNOT_CONTRACT_RE.match(line)
    if m:
        return bool(re.match(r'\s*pure\b', m.group(1)))
    if BARE_PURE_STANDALONE_RE.match(line):
        return True
    return False


def line_has_ghost(line):
    """Check if a contract line indicates 'ghost'."""
    m = ANNOT_CONTRACT_RE.match(line)
    if m:
        return bool(re.match(r'\s*ghost\b', m.group(1)))
    if BARE_GHOST_STANDALONE_RE.match(line):
        return True
    return False


# func declaration line (possibly with qualifiers on same line)
FUNC_LINE_RE = re.compile(r'^(?P<indent>\s*)(?P<quals>(?:(?:pure|ghost)\s+)*)func\b')

# Implements block: `(SomeType) implements SomeInterface {`
IMPLEMENTS_BLOCK_START_RE = re.compile(r'^\s*\([^)]+\)\s+implements\s+\S+\s*\{')

# Domain block: `[ghost] type X domain {` or `domain {`
DOMAIN_BLOCK_START_RE = re.compile(r'^\s*(?:ghost\s+)?type\s+\w+\s+domain\s*\{')

# Interface declaration
INTERFACE_START_RE = re.compile(r'^\s*(?:type\s+\w+\s+)?interface\s*\{')

# Pure/ghost interface method spec (inside interface block)
# e.g.:  `    pure methodName(...)`  or  `    ghost methodName(...)`
# These are NOT func declarations
PURE_IFACE_METHOD_RE = re.compile(r'^\s*pure\s+\w+\s*\(')
GHOST_IFACE_METHOD_RE = re.compile(r'^\s*ghost\s+\w+\s*\(')

# ExpectedOutput(type_error)
TYPE_ERROR_RE = re.compile(r'//:: ExpectedOutput\(type_error\)')


def find_block_ranges(lines, start_re):
    """
    Find line ranges for brace-delimited blocks starting with start_re.
    Returns set of 0-based indices inside such blocks.
    """
    inside = set()
    i = 0
    while i < len(lines):
        if start_re.match(lines[i]):
            depth = 0
            start = i
            j = i
            while j < len(lines):
                for ch in lines[j]:
                    if ch == '{':
                        depth += 1
                    elif ch == '}':
                        depth -= 1
                if depth <= 0 and j >= start:
                    for k in range(start, j + 1):
                        inside.add(k)
                    i = j
                    break
                j += 1
        i += 1
    return inside


def is_blank(line):
    return not line.strip()


def is_regular_comment(line):
    """Non-//@  comment line."""
    stripped = line.strip()
    return stripped.startswith('//') and not stripped.startswith('//@')


def gather_contract(lines, func_idx):
    """
    Walk backwards from func_idx-1 to gather the contract block.
    Stops at non-contract lines (excluding blank lines and regular comments).
    Returns list of contract lines in order (earliest first).
    """
    contract = []
    j = func_idx - 1
    blank_count = 0

    while j >= 0:
        line = lines[j]
        if is_contract_line(line):
            contract.append(line)
            blank_count = 0
            j -= 1
        elif is_blank(line):
            blank_count += 1
            if blank_count > 1:
                break
            # One blank line may separate contract lines - keep going
            contract.append(line)  # include blank as placeholder (won't affect analysis)
            j -= 1
        elif is_regular_comment(line):
            # Regular comment may appear between contract lines
            contract.append(line)
            j -= 1
        else:
            break

    contract.reverse()
    return contract


def contract_has_decreases(contract_lines):
    return any(line_has_decreases(l) for l in contract_lines)

def contract_has_pure(contract_lines):
    return any(line_has_pure(l) for l in contract_lines)

def contract_has_ghost(contract_lines):
    return any(line_has_ghost(l) for l in contract_lines)


def func_is_pure_or_ghost(func_line, contract_lines):
    """
    Determine if a func is pure or ghost based on func line qualifiers
    and contract annotations.
    """
    m = FUNC_LINE_RE.match(func_line)
    if m:
        quals = m.group('quals')
        if 'pure' in quals or 'ghost' in quals:
            return True
    if contract_has_pure(contract_lines) or contract_has_ghost(contract_lines):
        return True
    return False


def func_is_closure_literal(func_line, lines, func_idx):
    """
    Heuristic: if the func appears indented inside a function body
    (i.e., it's a closure literal), skip it.

    A closure literal is typically indented more deeply and assigned to a variable.
    We detect this by checking if the func line is inside another func's body
    (indentation > 0 for a non-method).

    More precisely: a top-level pure/ghost func declaration should be at
    column 0 (no leading whitespace) unless it's a method.
    Closure literals appear indented.

    Actually, we check: if the func keyword is preceded by `:=` or `=` on
    the same context (the closure is part of an expression), look for `:=`
    on the line itself or look at indentation.

    Simplest heuristic: a pure func with leading spaces that isn't a method
    signature is likely a closure literal.
    """
    stripped = func_line.lstrip()
    indent = len(func_line) - len(stripped)

    # If indented with tabs, it's inside a function body
    if func_line.startswith('\t'):
        return True
    # If indented with multiple spaces beyond typical interface indentation
    if indent >= 4 and not func_line.lstrip().startswith('//'):
        # Could be inside something. Check if the line above has := or =
        if func_idx > 0:
            prev = lines[func_idx - 1].strip()
            if prev.endswith(':=') or prev.endswith('=') or 'decreases' in prev:
                return True
    return False


def analyze_file(filepath):
    """
    Analyze a single .gobra file and return list of findings.
    Each finding is (line_number_1based, signature).
    """
    try:
        with open(filepath, 'r', encoding='utf-8', errors='replace') as f:
            lines = f.readlines()
    except Exception:
        return []

    findings = []

    # Precompute block ranges
    implements_lines = find_block_ranges(lines, IMPLEMENTS_BLOCK_START_RE)
    domain_lines = find_block_ranges(lines, DOMAIN_BLOCK_START_RE)
    interface_lines = find_block_ranges(lines, INTERFACE_START_RE)

    # Collect lines with type_error annotations
    type_error_lines = set()
    for i, line in enumerate(lines):
        if TYPE_ERROR_RE.search(line):
            type_error_lines.add(i)

    def is_type_error_nearby(idx, contract):
        contract_len = len([l for l in contract if not is_blank(l)])
        contract_start = max(0, idx - contract_len - 3)
        for k in range(contract_start, min(len(lines), idx + 5)):
            if k in type_error_lines:
                return True
        return False

    for i, line in enumerate(lines):
        # --- Case 1: func declarations ---
        if FUNC_LINE_RE.match(line):
            # Skip if inside implements or domain block
            if i in implements_lines or i in domain_lines:
                continue

            # Skip closure literals (indented funcs inside function bodies)
            if func_is_closure_literal(line, lines, i):
                continue

            contract = gather_contract(lines, i)

            # Skip if type_error nearby
            if is_type_error_nearby(i, contract):
                continue

            # Check if pure or ghost
            if not func_is_pure_or_ghost(line, contract):
                continue

            # Check if has decreases
            if not contract_has_decreases(contract):
                findings.append((i + 1, line.rstrip()))

        # --- Case 2: pure/ghost interface method specs ---
        elif i in interface_lines and i not in implements_lines and i not in domain_lines:
            if PURE_IFACE_METHOD_RE.match(line) or GHOST_IFACE_METHOD_RE.match(line):
                # Ensure it's not a func declaration (those handled above)
                if FUNC_LINE_RE.match(line):
                    continue

                contract = gather_contract(lines, i)

                if is_type_error_nearby(i, contract):
                    continue

                if not contract_has_decreases(contract):
                    findings.append((i + 1, line.rstrip()))

    return findings


def main():
    all_findings = []

    gobra_files = sorted(ROOT.rglob("*.gobra"))
    print(f"Scanning {len(gobra_files)} .gobra files under {ROOT}", file=sys.stderr)

    for filepath in gobra_files:
        findings = analyze_file(filepath)
        for (lineno, sig) in findings:
            all_findings.append((str(filepath), lineno, sig))

    print(f"Found {len(all_findings)} pure/ghost functions missing `decreases`:\n")
    current_file = None
    for (fp, lineno, sig) in all_findings:
        if fp != current_file:
            print(f"\n{'='*80}")
            print(f"File: {fp}")
            current_file = fp
        print(f"  Line {lineno:5d}: {sig}")

    print(f"\n\nTotal: {len(all_findings)} findings across {len(set(f for f,_,_ in all_findings))} files")


if __name__ == "__main__":
    main()
