# SporeKart QA Sprint 2 — Sorting Report

**Date:** 2026-07-17

---

## Current State

**No sort controls exist on the public products page.** There is no product listing to sort.

Sorting does exist in:
- **CourseLibrary**: Sort by rating/duration
- **WishlistPage**: Sort by name/price/rating

## What's Missing

| Sort Option | Status | Notes |
|-------------|--------|-------|
| Price Low→High | ❌ | Not implemented |
| Price High→Low | ❌ | Not implemented |
| Newest first | ❌ | Not implemented |
| Popularity | ❌ | Not implemented |
| Alphabetical | ❌ | Not implemented |
| Sort after filtering | ❌ | Not implemented |
| Sort after searching | ❌ | Not implemented |

## Bugs

| ID | Issue | Severity |
|----|-------|----------|
| BUG-SRT-001 | No product sorting exists on public pages | High |

## Recommendation

Add sorting controls alongside the product listing implementation. Reuse sort components from the customer workspace (WishlistPage, CourseLibrary).
